package braynstorm.mpduels.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import braynstorm.mpduels.common.Packet;


public class ConnectionHandler implements Runnable {

	private Socket clientSocket;
	private Player thePlayer;
	private long lastHeartbeat;
	public ConnectionHandler(Socket s) {
		clientSocket = s;
	}
	
	public void sendFile(OutputStream os, String fileName) throws Exception
	{
	    // File to send
	    File myFile = new File(fileName);
	    int fSize = (int) myFile.length();
	    if (fSize < myFile.length())
	    {
	        System.out.println("File is too big'");
	        throw new IOException("File is too big.");
	    }

	    // Send the file's size
	    byte[] bSize = new byte[4];
	    bSize[0] = (byte) ((fSize & 0xff000000) >> 24);
	    bSize[1] = (byte) ((fSize & 0x00ff0000) >> 16);
	    bSize[2] = (byte) ((fSize & 0x0000ff00) >> 8);
	    bSize[3] = (byte) (fSize & 0x000000ff);
	    // 4 bytes containing the file size
	    os.write(bSize, 0, 4);

	    // In case of memory limitations set this to false
	    boolean noMemoryLimitation = true;

	    FileInputStream fis = new FileInputStream(myFile);
	    BufferedInputStream bis = new BufferedInputStream(fis);
	    try
	    {
	        if (noMemoryLimitation)
	        {
	            // Use to send the whole file in one chunk
	            byte[] outBuffer = new byte[fSize];
	            int bRead = bis.read(outBuffer, 0, outBuffer.length);
	            os.write(outBuffer, 0, bRead);
	        }
	        else
	        {
	            // Use to send in a small buffer, several chunks
	            int bRead = 0;
	            byte[] outBuffer = new byte[8 * 1024];
	            while ((bRead = bis.read(outBuffer, 0, outBuffer.length)) > 0)
	            {
	                os.write(outBuffer, 0, bRead);
	            }
	        }
	        os.flush();
	    }
	    finally
	    {
	        bis.close();
	    }
	}
	  
	
	
	private boolean isConnected(){
		return (System.currentTimeMillis() - lastHeartbeat) < 10000;
	}

	@Override
	public void run() {
		String nextLine;
		
		lastHeartbeat = System.currentTimeMillis();
		try {
			clientSocket.setSoTimeout(10000);
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream(),true);
			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (!clientSocket.isClosed() && isConnected()){
				
				nextLine = br.readLine();
				
				
				if(nextLine == null || nextLine.equals("") )
					continue;
				
				//If it's a heartbeat, there is no point going through all this, just do the job.
				if(nextLine.charAt(0) == Packet.HEARTBEAT_PACKET){
					lastHeartbeat = System.currentTimeMillis();
					if(thePlayer != null)
						thePlayer.lastHeartbeat = lastHeartbeat;
					continue;
				}
				Packet p = Packet.fromString(nextLine);
				
				if(!p.hasValidData())
					continue;
				
				System.out.println(p.toString());
				
				
				switch(p.getPacketType()){
					case Packet.REGISTER_PACKET:
						thePlayer = new Player(p.getData().get(0), clientSocket);
						if(thePlayer.isRegistered()){
							ArrayList <String> name = new ArrayList<String>();
							name.add(thePlayer.name);
							new Packet(Packet.PLAYER_ALREADY_EXISTS, name).send(output);
						}else{
							thePlayer.registerPlayer(p.getData().get(1));
							thePlayer.login(p.getData().get(1));
							new Packet(Packet.PLAYER_SUCCESSFUL_REGISTER).addData(thePlayer.name).send(output);
							new Packet(Packet.PLAYER_SUCCESSFUL_LOGIN).addData(thePlayer.name).send(output);
						}
						break;
					case Packet.LOGIN_PACKET:
						if(thePlayer != null && thePlayer.isLoggedIn)
							thePlayer.logout();
						thePlayer = new Player(p.getData().get(0), clientSocket);
						
						if(!thePlayer.login(p.getData().get(1))){

							ArrayList <String> name = new ArrayList<String>();
							name.add(thePlayer.name);
							new Packet(Packet.INVALID_CREDENTIALS,name).send(output);
							break;
						}
						
						new Packet(Packet.PLAYER_SUCCESSFUL_LOGIN).addData(thePlayer.name).send(output);
						break;
					case Packet.LOGOUT_PACKET:
						thePlayer.logout();
						return;
				}
			}
			
			if(thePlayer != null && thePlayer.isLoggedIn)
				thePlayer.logout();
			
			clientSocket.close();
			System.out.println("Thread finnished.");
		} catch (IOException e) {
			lastHeartbeat = 0;
			if(thePlayer != null && thePlayer.isLoggedIn){
				thePlayer.lastHeartbeat = lastHeartbeat;
				thePlayer.logout();
			}
		}
		
		
	}

}
