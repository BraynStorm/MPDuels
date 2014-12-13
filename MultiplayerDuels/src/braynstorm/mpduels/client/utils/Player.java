package braynstorm.mpduels.client.utils;

import braynstorm.mpduels.common.Type;

public class Player extends Rectangle {
	
	private int id;
	private String name;
	
	public CardContainer hand;
	public CardContainer monsters;
	public CardContainer spells;
	public Graveyard graveyard;
	public Deck deck;
	
	public static final float PLAYER_DEFAULT_Z = -(1/6000);
	
	public Player(int id, String name) {
		super();
		if(id < 1)
			throw new IllegalArgumentException("PlayerID is invalid.");
		
		this.id = id;
		this.name = name;
		
		// TODO: make the new player position itself, contain its containers(deck,hand,monsters,spells,graveyerds) and fix (again) the findByGLName.
		//setTexture("pl1.png");
		
		hand = new CardContainer(6);
		monsters = new CardContainer(6);
		spells = new CardContainer(6);
		graveyard = new Graveyard(id);
		deck = new Deck(id);
		
		setScale(0);
		
		setSize(640, 360);
		//setScale(0);
		if(id == 1){
			setPosition(320, 540, PLAYER_DEFAULT_Z);
			setColor(181,55,171);
			
			monsters.setPosition(388,414,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0xF0, 0xAA, 0xFF).setSize(504, 108);
			spells.setPosition(388,522,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0x00, 0xFF, 0x00).setSize(504, 108);
			hand.setPosition(388,648,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0xEC, 0xB5, 0xE8).setSize(504, 144);
			
		}
		
		if(id == 2){
			setPosition(960, 540, PLAYER_DEFAULT_Z);
			setColor(56,106,220);
			
			monsters.setPosition(388 + 504,414,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0xF0, 0xAA, 0xFF).setSize(504, 108);
			spells.setPosition(388+504,522,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0x00, 0xFF, 0x00).setSize(504, 108);
			hand.setPosition(388+504,648,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0xEC, 0xB5, 0xE8).setSize(504, 144);
			
		}
		
		if(id == 3){
			setPosition(320, 180, PLAYER_DEFAULT_Z);
			setColor(243,203,15);
			
			monsters.setPosition(388, 306, CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0xF0, 0xAA, 0xFF).setSize(504, 108);
			spells.setPosition(388, 198,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0x00, 0xFF, 0x00).setSize(504, 108);
			hand.setPosition(388, 72,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0xEC, 0xB5, 0xE8).setSize(504, 144);
			
		}
		
		if(id == 4){
			setPosition(960, 180, PLAYER_DEFAULT_Z);
			setColor(27,148,70);
			
			monsters.setPosition(892, 306, CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0xF0, 0xAA, 0xFF).setSize(504, 108);
			spells.setPosition(892, 198,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0x00, 0xFF, 0x00).setSize(504, 108);
			hand.setPosition(892, 72,CardContainer.CARD_CONTAINER_DEFAULT_Z).setColor(0xEC, 0xB5, 0xE8).setSize(504, 144);
		}
		
		
		graveyard.insertCard(Card.list.get(1).getPlayableCard().setOwner(id));
		
		hand.k = "HAND";
		monsters.k = "MONSTERS";
		spells.k = "SPELLS";
		
	}
	
	public boolean transferCard(PlayableCard p){
		
		if(p.container != null && !p.container.removeCard(p,true))
			return false;
		
		if(!monsters.insertCard(p,false))
			return false;
		
		if(!monsters.insertCard(p,false))
			return false;
		
		if(p.getCard().getType() == Type.MONSTER){
			return monsters.insertCard(p,true);
		}
		if(p.getCard().getType() == Type.SPELL || p.getCard().getType() == Type.TRAP){
			
			return spells.insertCard(p,true);
		}
		return false;
	}
	
	public boolean drawCard(PlayableCard p){
		p.startAnimationFromDeck();
		return hand.insertCard(p,true);
	}
	
	
	@Override
	protected void additionalRendering() {
		super.additionalRendering();
		
		hand.render();
		monsters.render();
		spells.render();
		graveyard.render();
		deck.render();
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
}
