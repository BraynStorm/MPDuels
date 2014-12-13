package braynstorm.mpduels.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
	
	public static Socket socket;
	public static InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 24500);
	public static Client client;
	public static ConnectionProcessor connection;
	public static Heart hb;
	public static Thread connectionThread,heartThread;
	public static volatile long lastHeartbeat;
	
	public static boolean isConnected(){
		// If the last 'heartbeat' was 10 or less seconds ago, then the Client is connected
		return System.currentTimeMillis() - lastHeartbeat < 10000;
	}

	public static void connect(String name){
		
		if(Client.isConnected() && connection != null)
			return;
		
		try {
			if(socket != null){
				socket.close();
			}
			
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 24500);
			socket.setSoTimeout(10000);
			lastHeartbeat = System.currentTimeMillis();
			if(heartThread == null){
				hb = new Heart();
				heartThread = new Thread(hb);
				heartThread.start();
			}
			
			connection = new ConnectionProcessor(name);
			connectionThread = new Thread(connection, "ConnectionThread");
			connectionThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
