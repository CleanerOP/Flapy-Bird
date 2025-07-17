import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{

    int boardWidth = 360;
    int boardHight = 640;


    Image backgroundImg ;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //bird
    int birdX = boardWidth/8;
    int birdY = boardHight/2;
    int birdWidth = 34;
    int birdHight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int hight = birdHight;
        Image img ;
        Bird(Image img){
            this.img = img;
        }
    }

    //pipes
    int pipeX = boardWidth;
    int pipeY = 0;

    int pipeWidth = 64;
    int pipeHight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int hight = pipeHight;

        Image img;
        boolean passed = false;
        Pipe(Image img){
            this.img = img;

        }
    }

    //game Logic

    Bird bird ;
    int velocityX = -4;
    int velocityY=-9; 
    double gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random(); 

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;
    double score = 0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHight));
       // setBackground(Color.blue);

       setFocusable(true);
       addKeyListener(this);
        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg =  new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg =  new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg =  new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //bird
        bird = new Bird(birdImg);

        pipes = new ArrayList<Pipe>();

        //[lace pipes timer
        
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();

        // game loop
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

    }
    public void placePipes(){
        // random number between 0 - 256
        //128
        //0-128-(0-256) --->1/4 pipeHight --> 3/4 pipeHight

        int randomPpeY = (int) (pipeY - pipeHight/4 - Math.random()*(pipeHight/2));
        int openingSpace = boardHight/4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPpeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHight + openingSpace;
        pipes.add(bottomPipe);

    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        
        // BACKGROUNG
        g.drawImage(backgroundImg, 0, 0,boardWidth,boardHight,null);
        // BIRD
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.hight, null);

        //pipes
        for(int i =0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipeWidth, pipeHight, null);
        }

        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int)score),10,35 );
          }
          else {
            g.drawString(String.valueOf((int) score ),10, 35);
          }
    }
    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //pipe 
        for(int i =0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x> pipe.x+pipeWidth) {
                pipe.passed = true;
                score += 0.5; // two pipes 
            }
            if (collision(bird, pipe)) {
                gameOver = true;
            }

        }
        if(bird.y >boardHight){
            gameOver = true;
        }


    }
    public boolean collision(Bird a , Pipe b){
        return a.x < b.x + b.width &&  //a's top left corner doesn't reach b's top rght corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner 
               a.y < b.y + b.hight &&   // a's top left corner doesn't reach b's bottom left corner  
               a.y + a.hight > b.y;     // a's bottom left corner passes b's top left corner 


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }

    }

        @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY =-9;
            if (gameOver) {
                //restart the game by reseting the conditions
                bird.y = birdY;
                velocityY =0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();

            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }
}
 
