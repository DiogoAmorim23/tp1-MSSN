package setup;
import processing.core.*;
import processing.core.PApplet;
import processing.core.PImage; // Para carregar imagens

public class Hello extends PApplet {
    PImage img;

    public static void main(String[] args) {
        PApplet.main(Hello.class);
    }

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void setup() {
        img = loadImage("C:\\Users\\carla\\Downloads\\Eu sou fixe, mas o Tiago não. - Discord 16_02_2021 16_56_48.png"); // Declare an image variable
        if (img != null) img.resize(100,100);

        fill(255, 0, 0); // Set circle color to red
        circle(400, 300, 80); // Draw a red circle at (400, 300) with a diameter of 80
        stroke(400, 300, 0); // Set stroke color to yellow
    }

    @Override
    public void draw() {
        background(200);
        if (img != null) {
            image(img, mouseX, mouseY); // Draw the image at the mouse position
        }
    }

}


