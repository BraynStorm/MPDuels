package braynstorm.mpduels.client.gui;

import java.awt.Font;
import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.TrueTypeFont;

import braynstorm.mpduels.client.utils.Card;
import braynstorm.mpduels.client.utils.CardContainer;
import braynstorm.mpduels.client.utils.PlayableCard;
import braynstorm.mpduels.client.utils.Player;
import braynstorm.mpduels.client.utils.Rectangle;
import braynstorm.mpduels.client.utils.Shape;
import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL15.*;

public class GUIGameWindow implements Runnable{
	
	private boolean render = true;
	public static GUIGameWindow instance;
	public static Font awtFont = new Font("Consolas", Font.PLAIN , 24);
	public static TrueTypeFont font;
	
	public static BufferedReader langFile;
	
	public GUIGameWindow(){
		instance = this; 
	}
	
	public static int getHeight(){
		return Display.getDisplayMode().getHeight();
	}
	
	public static int getWidth(){
		return Display.getDisplayMode().getWidth();
	}
	
	public HashMap<String, Shape> shapes = new HashMap<String, Shape>();
	
	
	public static Player player1;
	public static Player player2;
	public static Player player3;
	public static Player player4;
	
	public void run(){

		try {

			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.setTitle("Multiplayer Duels");
			Display.create();
			Display.setResizable(true);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, getWidth(), getHeight(), 0, 10, -10);
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glViewport(0,0,Display.getWidth(),Display.getHeight());
			
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_VERTEX_ARRAY);
			glEnable(GL_DEPTH_TEST);
			glEnable(GL_ALPHA_TEST);
			
			glEnable (GL_BLEND);
			glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			//glHint (GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
			//glEnable (GL_POLYGON_SMOOTH);
			//GL11.glLineWidth (1.5f);
			//glBlendFunc(GL_BLEND_SRC, GL_BLEND_DST);
			
			glClearColor(.5f, .5f, .7f, 1f);
			font = new TrueTypeFont(awtFont, false);
			init();
			
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//public HashMap<String, CardContainer> containers = new HashMap<String, CardContainer>();
	
	public void init(){
		System.gc();
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nINITIALIZING.");
		Shape.resetNames();
		Card.init();
		
		shapes = new HashMap<String, Shape>();
		shapes.clear();
		shapes.put("board", new Rectangle().setTexture("board.png").setPosition(getWidth()/2, getHeight()/2,0).setSize(1280,720).setColor(0xFF, 0xFF, 0xFF));
		
		//shapes.put("player1", new Player(1, "BraynStorm"));
		player1 = new Player(1, "BraynStorm");
		player2 = new Player(2, "player2");
		player3 = new Player(3, "player3");
		player4 = new Player(4, "player4");
				
		player1.hand.insertCard(Card.list.get(0).getPlayableCard(),true);
		player1.spells.insertCard(Card.list.get(1).getPlayableCard(),true);
		//player1.monsters.insertCard(Card.list.get(2).getPlayableCard());
		
		cycle();
	}
	
	
	
	
	public void cycle(){
		boolean flag = false;
		
		while (render && !Display.isCloseRequested()){
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			
			boolean f = CardContainer.renderCards;
			CardContainer.renderCards = true;
			render();
			CardContainer.renderCards = f;
			
			while (Keyboard.next()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_R)
					flag = true;
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE || Display.isCloseRequested()){
					Display.destroy();
					return;
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()){
					System.out.println();
					for(Entry<String, Shape> e : shapes.entrySet()){
						System.out.println(e.getKey() + ':' + e.getValue().toString());
					}
				}
			}
			
			while(Mouse.next())
				mouseInteraction(Mouse.getEventX(), Mouse.getEventY());
			
			glDisableClientState(GL_COLOR_ARRAY);
			glDisableClientState(GL_VERTEX_ARRAY);
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);


			Shape.renderedAmount = 0;
			Display.update();
			Display.sync(60);
			
			if(flag)
				break;
		}
		
		//reboot stuff (easy debug :))
		
		init();
		
		
	}
	
	public boolean hasACardInMouse = false;
	public PlayableCard pickedCard;
	//Code from : https://docs.google.com/document/d/1MEcdkcCCM-BKmuL2Rg5SR-_RG6cCH7Ek9_hU7xXlW4Y/pub
	private void mouseInteraction(int x, int y )
    {
	    IntBuffer selBuffer = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder()).asIntBuffer();
	    int buffer[] = new int[256];
	   
	    IntBuffer vpBuffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
	    int[] viewport = new int[4];
	   
		int hits;
	    GL11.glGetInteger(GL11.GL_VIEWPORT, vpBuffer);
	    vpBuffer.get(viewport);
	   
	    GL11.glSelectBuffer(selBuffer);
	   
	    GL11.glRenderMode(GL11.GL_SELECT);
	   
	    GL11.glInitNames();
	    GL11.glPushName(0);
	   
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glPushMatrix();
	    GL11.glLoadIdentity();
	   
	    GLU.gluPickMatrix( (float) x, (float) y, 5.0f, 5.0f,IntBuffer.wrap(viewport));
	   
		    //GLU.gluPerspective(40f, 800/600f, 0.001f, 400f);
		glOrtho(0, getWidth(), getHeight(), 0, 10, -10);
		render();
		GL11.glPopMatrix();
		hits = GL11.glRenderMode(GL11.GL_RENDER);
		   
	    selBuffer.get(buffer);
        // Objects Were Drawn Where The Mouse Was
        if (hits > 0) {
        	
			int choose = buffer[3];
			int depth = buffer[1];
			for (int i = 1; i < hits; i++) {
				if (buffer[i * 4 + 1] < depth) {
					choose = buffer[i * 4 + 3];
					depth = buffer[i * 4 + 1];
				}
			}
	        Shape first = Shape.getShapeByGLName(choose);
	        PlayableCard p = null;
	        CardContainer c = null;
	        
	        if(first == null)
	        	return;
	        
	        int btn = Mouse.getEventButton();
	        boolean btnState = Mouse.getEventButtonState();
	        
	        if(first instanceof PlayableCard){
	        	p = (PlayableCard) first;
	        	//TODO p.mouseClick(btn, btnState);
	        	if(btn == 0){
	        		if(btnState){
	            		p.isPickedUp = true;
	            		hasACardInMouse = true;
	            		pickedCard = p;
	            		CardContainer.renderCards = false;
	        		}
	        	}
	        }
	        if(first instanceof CardContainer){
	        	c = (CardContainer) first;
				if(btn == 0){
					CardContainer.renderCards = true;
					if(!btnState){
						if(hasACardInMouse){
							pickedCard.isPickedUp = false;
							
							c.transferCardToMe(pickedCard);
						}
						hasACardInMouse = false;
						pickedCard = null;
					}
				}
			}
	        
	        if(btn == 1 && !btnState){
	        	System.out.println("First  : " + choose);
	        }
        
        }
        GL11.glPopName();
    }
	
	
	public void render(){
		player1.render();
		player2.render();
		player3.render();
		player4.render();
		for(Entry<String, Shape> e : shapes.entrySet()){
			//e.getValue().rotateBy(0f, 0f, 1f);
			e.getValue().render();
		}
		//glPushMatrix();
		//glTranslatef(200, 0, -0.09f);
		//glRotatef(0,0,0,1);
		//font.drawString(10, 10, "Dancho e lud", Color.black);
		//glPopMatrix();
	}
	
	public void stopRendering(){
		render = false;
	}
	
}
