package braynstorm.mpduels.client.utils;


import braynstorm.mpduels.client.gui.GUIGameWindow;

public class ToolTip extends Rectangle {
	
	public static final String TOOLTIP_GENERAL_PATH = "/img/tooltip_";
	
	public int type = 0;
	public boolean render = false;
	
	private PlayableCard card;
	private String description;
	private int padding = 5;
	
	//100% summon tooltip
	public ToolTip(PlayableCard card) {
		super();
		if(card != null){
			this.card = card;
		}else{
			System.out.println("SEVERE TOOLTIP ERROR !!!");
			return;
		}
	}
	
	// 50% summon tootlip
	public ToolTip(PlayableCard card, int type){
		this(card);
		this.type = type;
		if(type == 1){ // Not summon
			loadDescription();
		}
	}
	
	private void loadDescription() {
		//TODO Load the card description from a file (enUS.lang ?).
	}
	
	@Override
	public void additionalRendering(){
		/*
		 *  TODO Card tooltip.  (Type = 0) Contains the description and some specifications of the card.
		 *  					(Type = 1) Normal_Summon, Normal_Set, Special_Summon, Special_Set 
		 */
		if(render){
			//GL11.glPushMatrix();
			
			GUIGameWindow.font.drawString(x - w/2 + padding, y - w/2 + padding, description);
			
			
			//GL11.glPopMatrix();
		}
	}
	
}
