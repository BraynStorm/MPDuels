package braynstorm.mpduels.server;

import java.io.IOException;
import java.io.PrintWriter;

import braynstorm.mpduels.common.Packet;

public class ServerHeart implements Runnable {
	
	public static boolean beating = true;
	public ServerHeart() {
	}

	@Override
	public void run() {
		Packet hbPacket = new Packet(Packet.HEARTBEAT_PACKET);
		while (beating){
			System.out.print("List of players: ");
			for(Player p : Player.players){
				try {
					System.out.print(p.name);
					System.out.print(", ");
					hbPacket.send(new PrintWriter(p.socket.getOutputStream(),true));
				} catch (IOException e) {
					Player.players.remove(p);
					e.printStackTrace();
				}
			}
			System.out.println("\n");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
