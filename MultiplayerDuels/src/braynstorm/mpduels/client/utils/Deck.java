package braynstorm.mpduels.client.utils;

import org.lwjgl.opengl.GL11;

public class Deck extends Rectangle {
	
	private int owner;
	
	private PlayableCard drawer = Card.list.get(6).getPlayableCard();
	
	public Deck(int player) {
		setOwner(player);
		
		switch(owner){
			case 1:
				x = 64;
				y = 650;
				break;
			case 2:
				x = 1220;
				y = 650;
				break;
			case 3:
				x = 62;
				y = 70;
				break;
			case 4:
				x = 1220;
				y = 70;
				break;
		}
	}
	
	public void render(){
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, CardContainer.CARD_CONTAINER_DEFAULT_Z);
		drawer.render();
		
		GL11.glPopMatrix();
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
}
