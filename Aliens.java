package W5;

import java.awt.*;

public class Aliens extends Sprite {

    boolean isAlive = true;
    public Aliens(Image i,Image i2) {
        super(i,i2);
    }

    public boolean move() {
        x += xSpeed; //moves right/left depending on xSpeed
        //if it hits the border
        return x <= 770 && x >= 0;
    }

    public int changeDirection() {
        y+=15; //move down
        return (int)(xSpeed*=-1); //changes direction
    }

    public void paint(Graphics g) {
        if(isAlive) {
            //draw the image at the position given if the alien is alive
            framesDrawn++;
            if (framesDrawn % 100 < 50) {
                g.drawImage(myImage, x, y, 30, 30, null);
            } else {
                g.drawImage(myImage2, x, y, 30, 30, null);
            }
        }
    }
}

