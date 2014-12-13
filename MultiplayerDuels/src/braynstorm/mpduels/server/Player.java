package braynstorm.mpduels.server;

import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import braynstorm.mpduels.common.Card;
import braynstorm.mpduels.common.Packet;


public class Player {
	
	public Socket socket;
	public String name;
	public int currentGame = -1;
	public Card[] unlockedCards;
	public volatile long lastHeartbeat;
	public int winCount = 0;
	public int lossCount = 0;
	public PreparedStatement loginStatement,registerStatement,saveStatement,isRegisteredStatement;
	public boolean isLoggedIn = false;
	
	//TODO : FIX THAT SHITTY LIST!
	public static volatile List<Player> players = new ArrayList<Player>();
	
	public Player(String name, Socket s){
		this.name = name;
		this.socket = s;
		
		try {
			loginStatement = Server.getSQLConnection().prepareStatement("SELECT * FROM " + Server.DB_PLAYERS_TABLE + " WHERE name=? AND password=?");
			registerStatement = Server.getSQLConnection().prepareStatement("INSERT INTO " + Server.DB_PLAYERS_TABLE + " (name,password,winCount,lossCount,cardsunlocked) VALUES(?,?,?,?,?) ");
			//TODO: AFTERCARD; Save player's progress (in card unlocking)
			saveStatement = Server.getSQLConnection().prepareStatement("UPDATE " + Server.DB_PLAYERS_TABLE + " SET winCount=?, lossCount=? WHERE name=?");
			isRegisteredStatement = Server.getSQLConnection().prepareStatement("SELECT * FROM " + Server.DB_PLAYERS_TABLE + " WHERE name=?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Deprecated
	public static Player fromPacket(Packet p, Socket s){
		if(p.is(Packet.LOGIN_PACKET) && s.isConnected()){
			Player pp = new Player(p.getData().get(0), s);
			if(pp.login(p.getData().get(1)))
				return pp;
		}
		
		if(p.is(Packet.REGISTER_PACKET)){
			Player pp = new Player(p.getData().get(0), s);
			if(!pp.isRegistered()){
				pp.registerPlayer(p.getData().get(1));
				pp.isLoggedIn = true;
				return pp;
			}
		}
		return null;
	}
	
	
	public boolean login(String password){
		try {
			
			loginStatement.setString(1, name);
			loginStatement.setString(2, password);
			
			ResultSet rs = loginStatement.executeQuery();
			Server.getSQLConnection().commit();
			if(rs.next()){
				winCount = rs.getInt(4);
				lossCount = rs.getInt(5);
				isLoggedIn = true;
				players.add(this);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean registerPlayer(String password){
		try {
			registerStatement.setString(1, name);
			registerStatement.setString(2, password);
			registerStatement.setInt(3, 0); // WinCount
			registerStatement.setInt(4, 0); // LossCount
			registerStatement.setString(5, " ");
			System.out.println("REGISTER:"+registerStatement.toString());
			int res = registerStatement.executeUpdate();
			Server.getSQLConnection().commit();
			return res > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public void save(){
		
		try {
			if(isRegistered()){
				//TODO: AFTERCARD; Save player's progress (in card unlocking)
				
				saveStatement.setInt(1, winCount);
				saveStatement.setInt(2, lossCount);
				saveStatement.setString(3, name);
				//saveStatement.setString(4, unlockedCards.toString());
				saveStatement.executeUpdate();
				Server.getSQLConnection().commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isRegistered(){
		try {
			isRegisteredStatement.setString(1, name);
			ResultSet rs = isRegisteredStatement.executeQuery();
			Server.getSQLConnection().commit();
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isConnected(){
		if(socket != null && socket.isConnected() && !socket.isClosed() && System.currentTimeMillis() -  lastHeartbeat < 10000)
			return true;
		return false;
	}
	
	public boolean isPlaying(){
		return currentGame != -1;
	}


	public void logout() {
		// TODO Auto-generated method stub
		save();
		isLoggedIn = false;
		if(players.contains(this))
			players.remove(this);
	}
	
}
