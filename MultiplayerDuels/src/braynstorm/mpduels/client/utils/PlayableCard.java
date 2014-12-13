package braynstorm.mpduels.client.utils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


public class PlayableCard extends Rectangle{
	
	private int id;
	private int cardOwner = 0;
	private int currentCardOwner = 0;
	public boolean isPickedUp = false;
	public CardContainer container;
	
	public static float width = 72;
	public static float height = 105; // 90
	
	private float hx=0; //67
	private float hy=0; // 650
	private float hz=-0.09999f;
	
	public float animationSpeed = 1f; 
	public float distanceMoved = 0;
	
	public float getRealX(){
		return x + hx;
	}
	public float getRealY(){
		return y + hy;
	}
	
	public PlayableCard(int id){
		super();
		
		setSize(width, height);
		//setScale(1.2f);
		if(id >= 0){
			this.id = id;
			this.z = (-1-this.id)/100f;
		}
		else
			throw new IllegalArgumentException("INVALID CARD ID");
	}
	
	public PlayableCard setOwner(int owner){
		
		if(owner > 0){
			if(cardOwner != 0){
				currentCardOwner = owner;
				return this;
			}
			cardOwner = owner;
		}
		else
			throw new IllegalArgumentException("INVALID OWNER ID");
		
		return this;
	}
	
	public int getOwner(){
		return cardOwner;
	}
	
	public int getCurrentOwner(){
		return currentCardOwner;
	}
	
	public void startAnimationFromDeck(){
		hx = 67;
		hy = 650;
	}
	
	// Optional use.
	public void setAnimStartPosition(float x, float y){
		hx = x;
		hy = y;
	}
	
	public Card getCard(){
		return Card.list.get(id);
	}
	
	@Override
	public void additionalTranslation(){
		distanceMoved = (float)Math.sqrt(Math.abs(x - hx) + Math.abs(y - hy));
		animationSpeed = (float)Math.min(distanceMoved/5,1.5f);
		if(isPickedUp){
			hx = Mouse.getX();
			hy = Display.getHeight() - Mouse.getY();
			GL11.glTranslatef(-x, -y, hz);
		}else{
			
			//X
			if(hx > x)
				hx -= animationSpeed;
			else if(hx < x)
				hx += animationSpeed;
			
			
			//Y
			if(hy > y)
				hy -= animationSpeed;
			else if(hy < y)
				hy += animationSpeed;
			
			
			
			if(( Math.floor(hy) != x && Math.floor(hx) != y) || (Math.ceil(hy) != x && Math.ceil(hx) != y) ){
				// Sliding animation OP 
				GL11.glTranslatef(-x, -y, z);
			}
			
		}
		GL11.glTranslatef(hx,hy,z);
	}
	@Override
	public boolean equals(Object ss){
		PlayableCard s;
		///Y U NO WORK?!
		if(ss instanceof PlayableCard){
			s = (PlayableCard) ss;
			return s.x==this.x && s.y == this.y && s.z == this.z
					&& s.texture == this.texture
					&& this.w == s.w && this.h == s.h
					&& this.r == s.r && this.g == s.g && s.b == this.b
					&& this.vertices.equals(s.vertices)
					&& this.id == s.id
					&& ((s.container == null && this.container == null)||this.container.equals(s.container));
		}
		return false;
		
	}
	
	
	public PlayableCard setPosition(float x, float y){
		this.x = x;
		this.y = y;
		return this;
	}
}
