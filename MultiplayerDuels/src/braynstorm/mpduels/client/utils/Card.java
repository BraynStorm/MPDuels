package braynstorm.mpduels.client.utils;

import java.util.ArrayList;



import braynstorm.mpduels.common.SubType;
import braynstorm.mpduels.common.Type;

/**
 * 
 * @author BlueForce
 * This is just a 'referance' class. Practicle class(the one that's visible is {@link PlayableCard}.
 */
public class Card {
	
	public static final ArrayList<Card> list = new ArrayList<Card>();
	
	private int id;
	
	private Type type;
	private SubType subType;
	
	private String unlocalizedName;
	private String textureFilepath;
	
	public PlayableCard getPlayableCard(){
		
		PlayableCard playableCard = new PlayableCard(id);
		playableCard.setTexture(textureFilepath);
		return playableCard;
	}
	
	private Card(String unlocalizedName, String filepath, Type t, SubType st){
		this.id = list.size();
		this.unlocalizedName = unlocalizedName;
		this.textureFilepath = filepath;
		type = t;
		subType = st;
	}
	
	private Card(String unlocalizedName, Type t, SubType st){
		this(unlocalizedName, unlocalizedName + ".png", t, st);
	}
	
	
	public static void init(){
		//list.add(new Card("",			Type.,		SubType.));
		list.clear();
		
		list.add(new Card("geaman",					Type.MONSTER,	SubType.MONSTER_GENERAL));
		list.add(new Card("theDarkMagician",		Type.MONSTER,	SubType.MONSTER_GENERAL));
		list.add(new Card("blueEyesWhiteDragon",	Type.MONSTER,	SubType.MONSTER_GENERAL));
		list.add(new Card("unitedWeStand",			Type.SPELL,		SubType.SPELL_EQUIP));
		list.add(new Card("mirrorForce",			Type.TRAP,		SubType.TRAP_GENERAL));
		list.add(new Card("magePower",				Type.SPELL,		SubType.SPELL_EQUIP));
		
		
		list.add(new Card("CARDBACK",				Type.CARDBACK,		SubType.CARDBACK));
		
		
	}
	public int getID() {
		return id;
	}
	public Type getType() {
		return type;
	}
	public SubType getSubType() {
		return subType;
	}
	public String getUnlocalizedName() {
		return unlocalizedName;
	}
	
}
