package braynstorm.mpduels.client;

import java.io.IOException;
import java.io.PrintWriter;

import braynstorm.mpduels.common.Packet;

public class Heart implements Runnable {
	
	public boolean beating = true;
	
	public Heart() {
	}
	
	@Override
	public void run() {
		try {
			PrintWriter pr = new PrintWriter(Client.socket.getOutputStream(),true);
			Packet p = new Packet(Packet.HEARTBEAT_PACKET);
			while(Client.socket != null && !Client.socket.isClosed())
				while (beating){
					
					if(Client.socket.isConnected() && !Client.socket.isClosed()){
						p.send(pr);
						//pr.close();
					}
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			Client.socket.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
	}
	
}
