package braynstorm.mpduels.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PacketManager {
	
	
	
	public static void sendPacket(char packetType, String packet, Socket socket){
		try {
			if(socket.isConnected() && !socket.isClosed() && socket.getInetAddress().isReachable(1500)){
				new PrintWriter(socket.getOutputStream()).println(packetType + Packet.PACKET_DELIMITER + packet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendPacket(Packet packet){
		
	}
	
}
