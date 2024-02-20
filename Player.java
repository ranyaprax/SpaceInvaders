package W5;

import java.awt.Image;

public class Player extends Sprite{

    public Player(Image i) {
        super(i,i); //just need the 1 image but sprite takes in 2 for the alien, so we use the same image
    }

    public void move() {
        x+=xSpeed;//if it's between the borders keep moving
        if(x<=0) { //if it hit the right side, stop moving the player ship
            x = 0;
            xSpeed = 0;
        }
        else if(x>=770){ //if it hit the left side, stop moving the player ship
            x = 770;
            xSpeed = 0;
        }
    }
}
