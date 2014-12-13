package braynstorm.mpduels.server;

import java.io.IOException;
import java.io.PrintWriter;

import braynstorm.mpduels.common.Packet;

public class Heartbeat implements Runnable {
	
	public static boolean beating = true;
	public Heartbeat() {
	}

	@Override
	public void run() {
		Packet hbPacket = new Packet(Packet.HEARTBEAT_PACKET);
		while (beating){
			
			for(Player p : Player.players){
				try {
					new PrintWriter(p.socket.getOutputStream(),true).println(hbPacket.toString());
				} catch (IOException e) {
					//Player.players.remove(p);
					e.printStackTrace();
				}
			}
			
			try {
				this.wait(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
