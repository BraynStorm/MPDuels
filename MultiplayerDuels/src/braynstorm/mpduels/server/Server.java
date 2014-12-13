package braynstorm.mpduels.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {
	
	private static Connection dbConnection;
	private static ServerSocket serverSocket;
	
	public static final String DB_USERNAME = "MPDuels";
	public static final String DB_PASSWORD = "123456";
	public static final String DB_NAME = "mpduels";
	public static final String DB_PLAYERS_TABLE = "players";
	public static final String DB_CARDLIST_TABLE = "cardlist";
	
	//public static List<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();
	
	public static void main(String args[]){
		startServer();
	}
	
	public static void startServer(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost/" + DB_NAME + "?user=" + DB_USERNAME + "&password=" + DB_PASSWORD);
			dbConnection.setAutoCommit(false);

			try{
				serverSocket = new ServerSocket(24500);
				ServerHeart hb = new ServerHeart();
				new Thread(hb).start();
				while(!serverSocket.isClosed()){
					Socket s = serverSocket.accept();
					new Thread(new ConnectionHandler(s)).start();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			
			ServerHeart.beating = false;
			serverSocket.close();
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static Connection getSQLConnection(){
		return dbConnection;
	}
	public static ServerSocket getServerSocket(){
		return serverSocket;
	}
	
	
}
