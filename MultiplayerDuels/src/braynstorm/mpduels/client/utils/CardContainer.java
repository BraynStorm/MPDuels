package braynstorm.mpduels.client.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.lwjgl.input.Mouse;

public class CardContainer extends Rectangle {
	
	private int holdLimit;
	public static final float CARD_CONTAINER_DEFAULT_Z = -(1/5000);
	public static boolean renderCards = true;
	public boolean isEnabled = false;
	private volatile HashMap<Integer,PlayableCard> cards = new HashMap<Integer,PlayableCard>();
	protected float internalPadding = 0;
	public String k;
	
	public CardContainer(int holdLimit){
		super();
		this.holdLimit = holdLimit;
	}
	
	public int getHoldLimit(){
		return holdLimit;
	}
	
	public boolean isFull(){
		return cards.size() >= holdLimit;
	}
	
	public HashMap<Integer,PlayableCard> getCards(){
		return cards;
	}
	
	public PlayableCard getCardByGLName(int glName){
		for(Entry <Integer, PlayableCard> p : cards.entrySet()){
			if(p.getValue().name == glName)
				return p.getValue();
		}
		return null;
	}
	
	public static boolean between (double a,double b,double c){
		if(b > c)
			return a > c && a < b;
		else if(c > b)
			return a < c && a > b;
		else return  a == b ;
	}
	public int determinePosition(int mouseX){
		int pos = 0;
		pos = (int)( ((mouseX - (x - w/2) )/w)  * holdLimit);
		//System.out.println(pos + " sss " + mouseX);
		return Math.max(Math.min(pos, holdLimit), 0);
	}
	
	
	public boolean isPositionFree(int position){
		if(cards.get(position) == null)
			return true;
		return false;
	}
	
	public boolean transferCardToMe(PlayableCard p){
		//TODO
		if(p == null || p.container.equals(this))
			return false;
		
		//TODO Ask the server if the move is right
		boolean removable  = p.container.removeCard(p,false);
		boolean insertable = this.insertCard(p,false);
		if(removable && insertable){
			p.container.removeCard(p,true);
			this.insertCard(p,true);
			System.out.println();
			return true;
		}
		return false;
	}
	
	public boolean contains(PlayableCard p){
		return cards.containsValue(p);
	}
	
	public PlayableCard removeCard(int position, boolean really){
		
		System.out.println("Remove card (pos)");
		
		if(!isPositionFree(position)){
			PlayableCard p = cards.get(position);
			if(really)
				cards.remove(position);
			return p;
		}
		return null;
	}
	
	
	
	public boolean removeCard(PlayableCard p, boolean really){
		if(really)
			System.out.println("Trying to remove a card from " + k);
		boolean f = false;
		if(cards.containsValue(p)){
			
			Iterator<Entry<Integer, PlayableCard>> it = cards.entrySet().iterator();
			
			while (it.hasNext()){
				Entry<Integer, PlayableCard> item = it.next();
				if(item.getValue().equals(p)){
					if(really)
						it.remove();
					f = true;
				}
			}
			//System.out.println(p.toString());
			if(f){
				if(really){
					p.container = null;
					System.out.println("Successful remove from "+ k );
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean insertCard(int cardID, boolean really){
		return insertCard(Card.list.get(cardID).getPlayableCard(),cards.size(), really);
	}
	
	
	public boolean insertCard(PlayableCard pCard, boolean really){
		return insertCard(pCard,  determinePosition(Mouse.getEventX()), really);
	}
	
	public boolean insertCard(PlayableCard pCard, int position, boolean really){
		
		if(pCard != null){
			if(really)
				System.out.println("Trying to insert a card in " + k);
			if(!isFull()){
				if(isPositionFree(position)){
					if(really){
						positionCard(pCard, position);
						pCard.container = this;
						cards.put(position, pCard);
						System.out.println("Successful insert in " + k);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void setPaddingTop(float padding){
		internalPadding = padding;
	}
	
	protected void positionCard(PlayableCard pCard, int position){
		float X = (x - (w/2)) + ((position + 1) * (pCard.w));
		pCard.setPosition(X, y + internalPadding);
	}
	
	@Override
	protected void additionalRendering() {
		//GL11.glLoadName(this.name);
		//GL11.glPushMatrix();
		//GL11.glTranslatef(x, y, z);
		if(renderCards)
			for(Entry <Integer, PlayableCard> p : cards.entrySet()){
				positionCard(p.getValue(), p.getKey());
				p.getValue().render();
			}
		
	}

	@Override
	public String toString(){
		return this.name + "";
	}
}
