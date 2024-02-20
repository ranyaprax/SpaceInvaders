package W5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.Iterator;
import java.util.ArrayList;


public class SpaceInvaders extends JFrame implements Runnable, KeyListener {


    private static final Dimension WindowSize = new Dimension(800,600);
    private BufferStrategy strategy;
    private static boolean isInitialised = false;

    private static final int NUMALIENS = 30;
    private Aliens[] AliensArray = new Aliens[NUMALIENS];
    private Player PlayerShip;
    private ArrayList bulletList = new ArrayList();

    private static String workingDirectory;
    private Graphics offscreenGraphics;
    private Image alienImage;
    private Image alienImage2;
    private Image playerImage;
    private Image bulletImage;

    //variables that will be changed depending on game status
    private int wave = 3;
    private boolean endGame = false;
    private int numAliensDead;
    private int status = 0;
    private int score = 0;
    private int best = 0;

    public SpaceInvaders(){
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);
        this.addKeyListener(this);
        setFocusable(true);

        //import the image for the aliens
        ImageIcon icon = new ImageIcon(workingDirectory + "/alien_ship_1.png");
        alienImage = icon.getImage();
        ImageIcon icon2 = new ImageIcon(workingDirectory + "/alien_ship_2.png");
        alienImage2 = icon2.getImage();

        //import image for the player
        ImageIcon icon3 = new ImageIcon(workingDirectory + "/player_ship.png");
        playerImage = icon3.getImage();

        //import image for the bullet
        ImageIcon icon4 = new ImageIcon(workingDirectory + "/bullet.png");
        bulletImage = icon4.getImage();


        //initialises the aliens and spaceship
        startNewGame();

        repaint();

        Thread t = new Thread(this);
        t.start();//runs the 'run' method

        createBufferStrategy(2);
        strategy = getBufferStrategy();
        offscreenGraphics = strategy.getDrawGraphics();

        isInitialised = true;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(25);
                //if the wave/game ended then either start a new wave or game
                if(endGame){ //alien and spaceship collision
                    status = 1;
                    if(score > best){ //if the score is more than the best, make this one the new best
                        best = score;
                    }
                    startNewGame();
                    endGame = false;
                    continue;
                }
                else if(numAliensDead == 30){ //all aliens died
                    startNewWave();
                    continue;
                }
                this.repaint(); //calls the paint method in this class
            } catch (InterruptedException e) {
                e.printStackTrace(); }



            //move player section
            PlayerShip.move();

            //move aliens section
            boolean allCanMove =true;

            for(int i=0;i<NUMALIENS;i++) { //moves all the aliens using the move method
                if(!AliensArray[i].move() && AliensArray[i].isAlive) { //if the current (alive) alien can't move
                    allCanMove = false; //flag that none of them can move anymore
                }
            }
            if(!allCanMove) { //if one (alive) alien can't move, change all of their direction
                for(int j=0;j<NUMALIENS;j++) {
                    AliensArray[j].setXSpeed(AliensArray[j].changeDirection());
                }
            }


            //move bullet and check for collision between aliens and each bullet in arraylist
            Iterator iterator = bulletList.iterator();
            while(iterator.hasNext()){
                Shooter shooter = (Shooter) iterator.next();
                if (shooter.move()) { //if the current bullet can move
                    boolean removeShooter = false;
                    for(int i=0;i<NUMALIENS;i++){// collision checker with all (Alive) aliens in the array
                        if (
                                AliensArray[i].isAlive && (((AliensArray[i].x < shooter.x && (AliensArray[i].x + 30) > shooter.x) || (shooter.x < AliensArray[i].x && (shooter.x + 10) > AliensArray[i].x))
                                        && ((AliensArray[i].y < shooter.y && (AliensArray[i].y + 30) > shooter.y) || (shooter.y < AliensArray[i].y && (shooter.y + 20) > AliensArray[i].y)))
                            ) {
                            //if they have collided
                                AliensArray[i].isAlive = false; //set the current alien as dead so it's not painted from now on
                                numAliensDead++; //increment the amount of aliens so if it hits 30 a new wave starts
                                removeShooter = true; //mark this shooter as one to remove
                                score += 10; //add to score number
                                break;
                        }
                    }
                    if(removeShooter){ //remove the marked shooter
                        iterator.remove();
                    }

                }
            }

            //collision checker for all alive aliens and playership
            for(int j=0;j<NUMALIENS;j++){
                if (
                        AliensArray[j].isAlive && (((AliensArray[j].x < PlayerShip.x && (AliensArray[j].x + 30) > PlayerShip.x) || (PlayerShip.x < AliensArray[j].x && (PlayerShip.x + 10) > AliensArray[j].x))
                                && ((AliensArray[j].y < PlayerShip.y && (AliensArray[j].y + 30) > PlayerShip.y) || (PlayerShip.y < AliensArray[j].y && (PlayerShip.y + 20) > AliensArray[j].y)))
                ) {
                    endGame = true; //end the current game
                }
            }
        }
    }


    public void keyPressed(KeyEvent e) {
        if(status == 1){ //if the game ended, it will change the status to 0 if any key is pressed
            status = 0;
        }
        //tell the spaceship to move right/left depending on key pressed
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT :
                PlayerShip.setXSpeed(5); //change x to the right (i.e. add)
                break;
            case KeyEvent.VK_LEFT :
                PlayerShip.setXSpeed(-5); //change x to the left (i.e subtract)
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        //tell spaceship you're no longer moving
        if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT)
            PlayerShip.setXSpeed(0);

        //shoots the bullet
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            shootBullet();
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void shootBullet(){
        Shooter b = new Shooter(bulletImage); //create a bullet object
        b.setPosition(PlayerShip.x+54/2,PlayerShip.y); //set the position to slightly right to the player ship
        bulletList.add(b); //add it to the bullet arraylist
    }

    public void paint(Graphics g) {
        if (!isInitialised)
            return;
        //double buffering
        g = offscreenGraphics;

        //set background colour
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 600);

        if(status == 1){//if the game is over (i.e alien and invader collision)
            //initialise text for user to restart game on any key pressed
            g.setFont(new Font("ITC Machine", Font.PLAIN, 30));
            g.setColor(Color.white);
            g.drawString("Game Over", 320, 300);
            g.setFont(new Font("ITC Machine", Font.PLAIN, 18));
            g.drawString("Press any key to start again", 280,370);
        }
        else { //normal game play status
            //top bar with score and best score
            g.setFont(new Font("ITC Machine", Font.PLAIN, 15));
            g.setColor(Color.white);
            g.drawString("Score: " + score + "  Best: " + best, 350, 45);

            //paint all the alien objects
            for (int i = 0; i < NUMALIENS; i++) {
                AliensArray[i].paint(g);
            }
            //paint the player object
            PlayerShip.paint(g);

            //paint the bullets in the arraylist
            Iterator iterator = bulletList.iterator();
            while (iterator.hasNext()) {
                Shooter b = (Shooter) iterator.next();
                b.paint(g);
            }
        }

        //double buffering
        strategy.show();
    }

    public void startNewWave(){
        //set the number of aliens dead to 0 so they can start counting again
        numAliensDead = 0;
        wave += 1; //increases the speed of the aliens

        //initialise each null point in the array to an alien sprite
        //and set position of the aliens in a grid
        int count = 0;
        for(int i = 20;i<240;i+=40){
            for(int j = 40;j<240;j+=40) {
                AliensArray[count] = new Aliens(alienImage,alienImage2);
                AliensArray[count].setXSpeed(wave);
                AliensArray[count].setPosition(i,j);
                count++;
            }
        }

        //initialise the player ship
        PlayerShip = new Player(playerImage);
        PlayerShip.setPosition(400, 550);
    }

    public void startNewGame(){
        //set the number of aliens dead to 0 so they can start counting again
        numAliensDead =0;
        score = 0;//reset the score

        //initialise each null point in the array to an alien sprite
        //and set position of the aliens in a grid
        int count = 0;
        for(int i = 20;i<240;i+=40){
            for(int j = 40;j<240;j+=40) {
                AliensArray[count] = new Aliens(alienImage,alienImage2);
                AliensArray[count].setXSpeed(3);
                AliensArray[count].setPosition(i,j);
                count++;
            }
        }

        //initialise the player ship
        PlayerShip = new Player(playerImage);
        PlayerShip.setPosition(400, 550);
    }

    public static void main(String[] args) {
        workingDirectory = System.getProperty("user.dir");
        System.out.println("Working Directory = " + workingDirectory);
        SpaceInvaders d = new SpaceInvaders();
    }
}
