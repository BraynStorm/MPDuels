package braynstorm.mpduels.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Packet {
	
	public static final char
		PACKET_DELIMITER = 1,
		REGISTER_PACKET = 2,
		PLAYER_LIST_PACKET = 3,
		PLAYER_ALREADY_EXISTS = 4,
		LOGIN_PACKET = 5,
		LOGOUT_PACKET = 6,
		HEARTBEAT_PACKET = 7,
		INVALID_CREDENTIALS = 8,
		PLAYER_SUCCESSFUL_LOGIN = 9,
		PLAYER_SUCCESSFUL_REGISTER = 10;
	
	private char type;
	private ArrayList <String> data = new ArrayList<String>();
	
	public boolean is(char type){
		return getPacketType() == type;
	}
	
	public boolean hasData(){
		if(data.isEmpty() || data.size() < 1)
			return false;
		return true;
	}
	
	public Packet(char packetType) {
		type = packetType;
	}
	
	public Packet(char packetType, String[] packetData) {
		this(packetType);
		
		setData(packetData);
	}
	
	public Packet(char packetType,ArrayList<String> packetData){
		this(packetType);
		this.data = packetData;
	}
	
	public void setData(String []data){
		this.data = (ArrayList<String>) Arrays.asList(data);
	}
	
	public void setData(ArrayList <String>data){
		this.data = data;
	}
	
	public Packet addData(String data){
		this.data.add(data);
		return this;
	}
	
	public void send(PrintWriter stream) throws IOException{
		stream.println(this.toString());
	}
	
	public char getPacketType(){
		return type;
	}
	public String[] getData(int asStringArray){
		return (String[]) data.toArray();
	}
	
	public ArrayList<String> getData(){
		return data;
	}
	public static Packet fromString(String rawData){
		if(rawData.charAt(0) == HEARTBEAT_PACKET)
			return new Packet(HEARTBEAT_PACKET);
		
		if(rawData.length() < 3 || rawData.charAt(1) != PACKET_DELIMITER)
			if(rawData.length() > 0){
				return new Packet(rawData.charAt(0));
			}else
				return null;
				
		String[] tmp1 = rawData.split(PACKET_DELIMITER + "");
		ArrayList<String> l = new ArrayList<String>();
		
		for(String s : tmp1){
			l.add(s);
		}
		
		Packet p = new Packet(l.get(0).charAt(0));
		l.remove(0);
		p.setData((ArrayList<String>)l);
		return p;
	}
	
	public boolean hasValidData(){
		String name;
		switch(type){
			case PACKET_DELIMITER:
				break;
			case HEARTBEAT_PACKET:
				return true;
			case PLAYER_SUCCESSFUL_LOGIN:
				return true;
			case PLAYER_SUCCESSFUL_REGISTER:
				return true;
			case PLAYER_ALREADY_EXISTS:
				return true;
			case INVALID_CREDENTIALS:
				return true;
			case LOGIN_PACKET:
				return data != null && data.get(0) != null && data.get(1) != null;
			case REGISTER_PACKET:
				name = data.get(0).trim();
				String password = data.get(1).trim();
				return name.length() > 4 && name.length() < 30 && password.length() == 32;
			case PLAYER_LIST_PACKET:
				name = data.get(0).trim();
				int winCount = Integer.parseInt(data.get(1).trim());
				int lossCount = Integer.parseInt(data.get(2).trim());
				return name.length() > 4 && name.length() < 30 && winCount >= 0 && lossCount >=0;
		}
		return false;
	}
	
	@Override
	public String toString(){
		StringBuilder j = new StringBuilder();
		j.append(type);
		for(String k : data){
			j.append(PACKET_DELIMITER).append(k);
		}
		return j.toString();
	}
	
}
