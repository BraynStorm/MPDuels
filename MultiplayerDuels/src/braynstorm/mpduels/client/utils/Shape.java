package braynstorm.mpduels.client.utils;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import braynstorm.mpduels.client.gui.GUIGameWindow;

public abstract class Shape {
	
	//public static final String generalTexturePath = "";///res/";
	public static final String cardTexturePath = "card/";
	public static int renderedAmount = 0;
	protected static int names = 0;
	
	protected int name = 0; // For 'selection'
	
	protected float x = 0;
	protected float y = 0;
	protected float z = 0;
	
	protected float w = 0;
	protected float h = 0;
	
	protected float rotX = 0;
	protected float rotY = 0;
	protected float rotZ = 0;
	
	protected float scaleX = 1;
	protected float scaleY = 1;
	protected float scaleZ = 1;
	
	protected float r = 0xFF;
	protected float g = 0xFF;
	protected float b = 0xFF;
	
	protected Texture texture;
	
	protected TexturedVertex[] vertices = new TexturedVertex[] {new TexturedVertex(), new TexturedVertex(), new TexturedVertex(), new TexturedVertex() };
	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer colorBuffer;
	protected FloatBuffer textureBuffer;
	
	protected int vboVertexHandle;
	protected int vboColorHandle;
	protected int vboTextureHandle;
	private static Player[] ppp;
	protected boolean readyForRendering = false;
	
	public String toString(){
		return name + " X:" + x + " Y: " + y;
	}
	
	public static Shape getShapeByGLName(int name) {
		for (Entry<String, Shape> s : GUIGameWindow.instance.shapes.entrySet()) {
			if (s.getValue().getGLName() == name){
				return s.getValue();
			}
			else if(s.getValue() instanceof CardContainer){
				CardContainer c = (CardContainer) s.getValue();
				
				PlayableCard p = c.getCardByGLName(name);
				
				if(p != null)
					return p;
			}
		}
		
		ppp = new Player[]{
				GUIGameWindow.player1,
				GUIGameWindow.player2,
				GUIGameWindow.player3,
				GUIGameWindow.player4
		};
		
		for(Player pl : ppp){
			
			PlayableCard p1 = pl.hand.getCardByGLName(name);
			PlayableCard p2 = pl.monsters.getCardByGLName(name);
			PlayableCard p3 = pl.spells.getCardByGLName(name);
			
			
			if(pl.hand.getGLName() == name)
				return pl.hand;
			
			if(pl.monsters.getGLName() == name)
				return pl.monsters;
			
			if(pl.spells.getGLName() == name)
				return pl.spells;
			
			if(p1 != null)
				return p1;
			if(p2 != null)
				return p2;
			if(p3 != null)
				return p3;
			
			
		}
		return null;
	}
	
	@Override
	public boolean equals(Object ss){
		Shape s;
		
		if(ss instanceof Shape){
			s = (Shape) ss;
			return s.x==this.x && s.y == this.y && s.z == this.z
					&& s.texture == this.texture
					&& this.w == s.w && this.h == s.h
					&& this.r == s.r && this.g == s.g && s.b == this.b
					&& this.vertices.equals(s.vertices);
		}
		return false;
		
	}
	
	public void render() {
		
		
		if (!readyForRendering)
			getReady();
		
		
		
		normalizeScale();
		
		GL11.glLoadName(name);
		//GL11.glPushName(name);
		
		glPushMatrix();

		glTranslatef(x, y, z);
		glScalef(scaleX, scaleY, scaleZ);
		
		glRotatef(rotX, 1, 0, 0);
		glRotatef(rotY, 0, 1, 0);
		glRotatef(rotZ, 0, 0, 1);
		
		additionalTranslation();
		renderedAmount++;
		// Actual mode: (VBO)
		
		//glBindBuffer(GL_ARRAY_BUFFER, 0);
		if(texture != null){
			texture.bind();
			glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandle);
			glTexCoordPointer(2, GL_FLOAT, 0, 0);
		}else{
			GL11.glDisable(GL_TEXTURE_2D);
			GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		GL11.glVertexPointer(4, GL_FLOAT, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		GL11.glColorPointer(4, GL_FLOAT, 0, 0);
		
		glDrawArrays(GL_QUADS, 0, vertices.length);
		//g
		
		//GL11.glPopName();
		//glBindBuffer(GL_ARRAY_BUFFER, 0);
		if(texture == null){
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			GL11.glEnable(GL_TEXTURE_2D);
		}
		glPopMatrix();
		additionalRendering();
	}
	
	// TODO when the mouse moves over.	(Hide Tooltip)
	public abstract void mouseOver();
	// TODO when the mouse moves out (duh) (Show Tooltip)
	public abstract void mouseOut();
	public abstract void mouseClick(int button, boolean state);
	protected abstract void additionalRendering();
	protected abstract void additionalTranslation();
	
	public Shape rotate(float x, float y, float z) {
		if (x > 0)
			rotX += x;
		
		if (y > 0)
			rotY += y;
		
		if (z > 0)
			rotZ += z;
		normalizeRotation();
		return this;
	}
	
	public Shape rotateBy(float x, float y, float z) {
		this.rotX += x;
		this.rotY += y;
		this.rotZ += z;
		normalizeRotation();
		return this;
	}
	
	public Shape rotateTo(float x, float y, float z) {
		this.rotX = x;
		this.rotY = y;
		this.rotZ = z;
		normalizeRotation();
		return this;
	}
	
	public Shape scaleBy(float x, float y, float z) {
		this.scaleX += x;
		this.scaleY += y;
		this.scaleZ += z;
		
		return this;
	}
	
	public Shape setColor(int red, int green, int blue) {
		if (red < 0 || red > 255 || green > 255 || green < 0 || blue < 0
				|| blue > 255)
			throw new IllegalArgumentException("Colors are invalid.");
		
		red /= 255;
		green /= 255;
		blue /= 255;
		
		vertices[0].setRGB(red, green, blue);
		vertices[1].setRGB(red, green, blue);
		vertices[2].setRGB(red, green, blue);
		vertices[3].setRGB(red, green, blue);
		return this;
	}
	
	public Shape setColor(int red, int green, int blue, float alpha) {
		if (red < 0 || red > 255 || green > 255 || green < 0 || blue < 0
				|| blue > 255)
			throw new IllegalArgumentException("Colors are invalid.");
		
		red /= 255;
		green /= 255;
		blue /= 255;
		
		vertices[0].setRGBA(red, green, blue, alpha);
		vertices[1].setRGBA(red, green, blue, alpha);
		vertices[2].setRGBA(red, green, blue, alpha);
		vertices[3].setRGBA(red, green, blue, alpha);
		return this;
	}
	
	public Shape setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Shape moveBy(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	
	public Shape setScale(float s) {
		this.scaleX = s;
		this.scaleY = s;
		this.scaleZ = s;
		return this;
	}
	
	public Shape setScale(float x, float y, float z) {
		this.scaleX = x;
		this.scaleY = y;
		this.scaleZ = z;
		return this;
	}
	
	public Shape setSize(float width, float height) {
		this.w = width;
		this.h = height;
		vertices[0].setXYZ(-width / 2, height / 2, z);
		vertices[1].setXYZ(width / 2, height / 2, z);
		vertices[2].setXYZ(width / 2, -height / 2, z);
		vertices[3].setXYZ(-width / 2, -height / 2, z);
		return this;
		
	}
	
	public Shape setTexture(String filepath) {
		Texture t = null;
		try {
			t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
					cardTexturePath + "testcard.png"), false, GL_LINEAR);
			t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(
					cardTexturePath + filepath), false, GL_NEAREST);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return setTexture(t);
		
	}

	public Shape setTexture(Texture t) {
		texture = t;
		
		int texW = texture.getImageWidth();
		int texH = texture.getImageHeight();
		
		vertices[0].setST(0, (float) texH / nearestPowerOf2(texH));
		vertices[1].setST((float) texW / nearestPowerOf2(texW), (float) texH / nearestPowerOf2(texH));
		vertices[2].setST((float) texW / nearestPowerOf2(texW), 0);
		vertices[3].setST(0, 0);
		return this;
	}

	protected void getReady() {
		vertexBuffer = BufferUtils.createFloatBuffer(vertices.length
				* TexturedVertex.positionElementCount);
		
		for (int i = 0; i < vertices.length; i++) {
			// Add position floats to the buffer
			vertexBuffer.put(vertices[i].getXYZW());
		}
		vertexBuffer.flip();
		
		colorBuffer = BufferUtils.createFloatBuffer(vertices.length
				* TexturedVertex.colorElementCount);
		for (int i = 0; i < vertices.length; i++) {
			// Add color floats to the buffer
			colorBuffer.put(vertices[i].getRGBA());
		}
		colorBuffer.flip();
		
		textureBuffer = BufferUtils.createFloatBuffer(vertices.length
				* TexturedVertex.textureElementCount);
		for (int i = 0; i < vertices.length; i++) {
			// Add texture floats to the buffer
			textureBuffer.put(vertices[i].getST());
		}
		textureBuffer.flip();
		
		
		
		vboVertexHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		
		vboColorHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		
		vboTextureHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandle);
		glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		readyForRendering = true;
	}
	
	protected void normalizeRotation() {
		rotX = (rotX >= 360) ? rotX % 360 : rotX;
		rotY = (rotY >= 360) ? rotY % 360 : rotY;
		rotZ = (rotZ >= 360) ? rotZ % 360 : rotZ;
	}
	
	protected void normalizeScale() {
		if (scaleX < Float.MIN_VALUE)
			scaleX = Float.MIN_VALUE;
		if (scaleY < Float.MIN_VALUE)
			scaleY = Float.MIN_VALUE;
		if (scaleZ < Float.MIN_VALUE)
			scaleZ = Float.MIN_VALUE;
		
	}
	
	public static void resetNames() {
		names = 0;
	}
	
	protected static int nearestPowerOf2(int v) {
		v--;
		v |= v >> 1;
		v |= v >> 2;
		v |= v >> 4;
		v |= v >> 8;
		v |= v >> 16;
		v++;
		return v;
	}
	
	public Shape() {
		name = ++names;
	}
	
	public int getGLName() {
		return name;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
}
