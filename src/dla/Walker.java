package dla;

import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class Walker {

	public enum State {
		STOPPED,
		WANDER
	}
	
	private PVector pos;
	private State state;
	private int color;
	private float radius          = 7;
	public static int num_wanders = 0;
	public static int num_stopped = 0;
	
	public Walker(PApplet p, float radius) {
		// pos   = new PVector(p.random(p.width), p.random(p.height));
		this.radius = radius;
		pos = new PVector(p.width/2, p.height/2);
		PVector r = PVector.random2D();
		r.mult(p.width/2);
		pos.add(r);
		
		setState(p, State.WANDER);
	}
	
	public Walker(PApplet p, PVector pos) {
		this.pos = pos;
		setState(p, State.STOPPED);
	}
	
	/**
	 * Returns Walker's radius 
	 */
	public float getRadius() {
		
		return this.radius;
		
	}
	
	/**
	 * Sets Walker's radius
	 */
	public void setRadius(int rad) {
		
		this.radius = rad;
		
	}
	
	/**
	 * Sets Walker's color
	 */
	public void setColor(int c) {
		
		this.color = c;
		
	}
	
	public State getState() {
		return state;
	}
	
	/**
	 * Method to set state and color of a Walker
	 */
	public void setState(PApplet p, State state) {
		this.state = state;
		if (state == State.STOPPED) {
			// color = p.color(0);
			num_stopped++;
		}
		else {
			color = p.color(255);
			num_wanders++;
		}
	}
	
	/**
	 * Method to update state of the walkers as they collide
	 * with other walkers and subtract them from walkers List
	 */
	public void updateState(PApplet p, List<Walker> walkers) {
		if (state == State.STOPPED) return;
		
		for (Walker w : walkers) 
			if (w.state == State.STOPPED) {
				float dist = PVector.dist(pos,  w.pos);
				if (dist < 2*radius) {
					setState(p, State.STOPPED);
					num_wanders--;
					break;
				}
			}
	}
	
	public void wander(PApplet p) {
		PVector step = PVector.random2D();
		pos.add(step);
		pos.lerp(new PVector(p.width/2, p.height/2), 0.0002f);
		pos.x = PApplet.constrain(pos.x, 0, p.width);
		pos.y = PApplet.constrain(pos.y, 0, p.height);
	}
	
	public void display(PApplet p) {
		// p.fill(color);
		// p.circle(pos.x, pos.y, 2*radius);
		p.strokeWeight(radius * 2);
		p.stroke(color);
		p.point(pos.x, pos.y);
	}
	
}