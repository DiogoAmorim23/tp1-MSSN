package setup;
import ca.TestGOL;
import ca.TestCA;
import dla.DLA;
import processing.core.PApplet;

public class ProcessingSetup extends PApplet {
	
	public static IProcessingApp app;
	private int lastUpdate;

	@Override
	public void settings() {
		size(800, 600);
	}
	
	@Override
	public void setup() {
		app.setup(this);
		lastUpdate = millis();
	}
	
	@Override
	public void draw() {
		int now    = millis();
		float dt   = (now - lastUpdate)/1000f;
		lastUpdate = now;
		app.draw(this, dt);
	}
	
	@Override
	public void mousePressed() {
		app.mousePressed(this);
	}

	@Override
	public void keyPressed() {
		app.keyPressed(this);
	}
	
	public static void main(String[] args) {
		// app = new Hello(); // Correr o Hello
		// app = new TestCA(); 
		app = new DLA(); // Correr o DLA
		// app = new TestGOL(); // Correr o jogo da vida
		PApplet.main(ProcessingSetup.class);
	}
	
}
