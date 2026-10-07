package setup;
import processing.core.PApplet;

public interface IProcessingApp {
	void setup(PApplet p);
	void draw(PApplet p, float dt);
	void mousePressed(PApplet p);
	void keyPressed(PApplet p);
}