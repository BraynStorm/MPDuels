package braynstorm.mpduels.client.utils;


public class Rectangle extends Shape {
	
	public Rectangle(){
		super();
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append("X: ").append(x).append(", ");
		s.append("Y: ").append(y).append(", ");
		s.append("Z: ").append(z).append(", ");
		
		return s.toString();
	}

	@Override
	protected void additionalRendering() {
		//TODO: Check if it is a card and add a toolTip.
	}

	@Override
	public void mouseOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void additionalTranslation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClick(int button, boolean state) {
		// TODO Auto-generated method stub
		
	}
	
	
}
