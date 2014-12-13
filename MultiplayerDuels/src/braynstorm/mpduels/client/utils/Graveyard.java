package braynstorm.mpduels.client.utils;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class Graveyard{
	
	public int owner;
	
	private float x;
	private float y;
	private float z = CardContainer.CARD_CONTAINER_DEFAULT_Z;
	
	private ArrayList<PlayableCard> cards = new ArrayList<PlayableCard>();
	
	public Graveyard(int player) {
		//super();
		owner = player;
		//System.out.println(owner);
		switch(owner){
			case 1:
				x = 64;
				y = 501;
				break;
			case 2:
				x = 1220;
				y = 500;
				break;
			case 3:
				x = 64;
				y = 222;
				break;
			case 4:
				x = 1220;
				y = 222;
				break;
		}
	}
	
	public void render(){
		GL11.glPushMatrix();
		
		
		
		for(PlayableCard p : cards){
			GL11.glTranslatef(x, y, z - ((cards.indexOf(p)+1) * CardContainer.CARD_CONTAINER_DEFAULT_Z));
			p.render();
		}
		
		GL11.glPopMatrix();
	}
	
	public void insertCard(PlayableCard p){
		
		if(p.getOwner() == this.owner)
			cards.add(p);
		else 
			System.out.println("OWNER PROBLEM!");
	}
	
	public void removeCard(int position){
		removeCard(cards.get(position));
	}
	
	public void removeCard(PlayableCard p){
		if(p == null || cards.contains(p))
			return;
		
		cards.remove(p);
	}
	
}
