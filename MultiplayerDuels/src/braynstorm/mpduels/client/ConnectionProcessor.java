package braynstorm.mpduels.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.InvalidParameterException;

import braynstorm.mpduels.common.Packet;

public class ConnectionProcessor implements Runnable {
	public String name;
	
	public ConnectionProcessor(String name) {
		this.name = name;
	}
	/*
	public Bitmap receiveFile(InputStream is, String fileName) throws Exception
	{

		String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
	    String fileInES = baseDir + File.separator + fileName;

	    // read 4 bytes containing the file size
	    byte[] bSize = new byte[4];
	    int offset = 0;
	    while (offset < bSize.length)
	    {
	        int bRead = is.read(bSize, offset, bSize.length - offset);
	        offset += bRead;
	    }
	    // Convert the 4 bytes to an int
	    int fileSize;
	    fileSize = (int) (bSize[0] & 0xff) << 24 
	               | (int) (bSize[1] & 0xff) << 16 
	               | (int) (bSize[2] & 0xff) << 8
	               | (int) (bSize[3] & 0xff);

	    // buffer to read from the socket
	    // 8k buffer is good enough
	    byte[] data = new byte[8 * 1024];

	    int bToRead;
	    FileOutputStream fos = new FileOutputStream(fileInES);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    while (fileSize > 0)
	    {
	        // make sure not to read more bytes than filesize
	        if (fileSize > data.length) bToRead = data.length;
	        else bToRead = fileSize;
	        int bytesRead = is.read(data, 0, bToRead);
	        if (bytesRead > 0)
	        {
	            bos.write(data, 0, bytesRead);
	            fileSize -= bytesRead;
	        }
	    }
	    bos.close();

	    // Convert the received image to a Bitmap
	    // If you do not want to return a bitmap comment/delete the folowing lines
	    // and make the function to return void or whatever you prefer.
	    Bitmap bmp = null;
	    FileInputStream fis = new FileInputStream(fileInES);
	    try
	    {
	        bmp = BitmapFactory.decodeStream(fis);
	        return bmp;
	    }
	    finally
	    {
	        fis.close();
	    }
	}
	
	*/
	public void run(){
		
		try {
			BufferedReader inStream = new BufferedReader(new InputStreamReader(Client.socket.getInputStream()));
			Packet p;
			
			
			while(Client.isConnected()){
				try{
					String nextLine = inStream.readLine();
					if(nextLine.length() < 1)
						continue;
					
					p = Packet.fromString(nextLine);
					
					if(!p.hasValidData())
						System.out.println("Invalid packet data.");
					
					//System.out.println(p.toString());
					
					switch(p.getPacketType()){
						case Packet.HEARTBEAT_PACKET:
							Client.lastHeartbeat = System.currentTimeMillis();
							break;
						case Packet.PLAYER_SUCCESSFUL_LOGIN:
							//TODO Open the OpenGL stuff and show menus and stuff
							break;
						case Packet.PLAYER_SUCCESSFUL_REGISTER:
							
							
							break;
						case Packet.PLAYER_LIST_PACKET:
							// Add to table/container/whatever;
							System.out.println(p.toString());
							break;
					}
				}catch(InvalidParameterException e){
					e.printStackTrace();
					// Then, ignore this string.
				}
			}
			p = new Packet(Packet.LOGOUT_PACKET);
			p.addData(name);
			p.send(new PrintWriter(Client.socket.getOutputStream(), true));
			Client.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}