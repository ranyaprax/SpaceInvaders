package W5;

import java.awt.*;

public class Shooter extends Sprite {

    public Shooter(Image i){
        super(i,i);//just need the 1 image but sprite takes in 2 for the alien, so we use the same image
    }

    public boolean move(){
        if (y > 0) { //if it hasn't hit the top of the page
            y-=15;
            return true;
        }
        return false;
    }

    public void paint(Graphics g){
        g.drawImage(myImage, x, y, 10, 20, null); //same method but as sprite but different width/height
    }
}
