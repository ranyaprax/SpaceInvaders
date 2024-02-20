package W5;

import java.awt.*;

public abstract class Sprite {

    //variables for Sprite object (inherited by Aliens and Player)
    protected int x,y;
    protected int xSpeed = 0;
    protected Image myImage,myImage2;
    public int framesDrawn = 0;

    public Sprite(Image i,Image i2) {
        myImage = i;
        myImage2 = i2;
    }

    public void setPosition(double xx, double yy) {
        //sets the position to the xx and yy parameters
        x = (int)xx;
        y = (int)yy;
    }

    public void setXSpeed(double dx) {
        //on key pressed event send number to setXSpeed method
        xSpeed = (int) dx;
    }

    public void paint(Graphics g) {
        g.drawImage(myImage, x, y, 30, 30, null);
    }
}
