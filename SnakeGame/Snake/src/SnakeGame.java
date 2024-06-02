import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;//To store segments of the snakes body.
import java.util.Random;// to get random x and y values to place food on screen
import javax.swing.*;
public class SnakeGame extends JPanel implements ActionListener, KeyListener
{

    private class Tile{
        int x;
        int y;

        Tile(int x,int y){
          this.x=x;
          this.y=y;
        }
      }

   int boardWidth;
   int boardHeight;
   int tileSize = 25;
//Snake
   Tile snakeHead;
   ArrayList<Tile> snakeBody;

   //Food
   Tile food;
   Random random;

   //game logic
    Timer gameLoop;
    int  velocityX;
    int  velocityY;
    boolean gameOver = false;


    SnakeGame(int boardWidth,int boardHeight){
    this.boardWidth=boardWidth;
    this.boardHeight=boardHeight;
    setPreferredSize(new Dimension(this.boardWidth, this. boardHeight));
    setBackground(Color.BLACK);
    addKeyListener(this);
    setFocusable(true);

    snakeHead = new Tile(5, 5);
    snakeBody = new ArrayList<Tile>();  

    food = new Tile(10, 10);

    random = new Random();
    placeFood();

    velocityX = 0;
    velocityY = 0;
     
    gameLoop = new Timer(100,this);
    gameLoop.start();
    

   }
   //for drawing
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
        
        }
        public void draw(Graphics g){  
          
           //Grid
         //   for(int i = 0; i< boardWidth/tileSize;i++){
         //    //(x1,y1,x2,y2)
         //    g.drawLine(i*tileSize, 0, i* tileSize, boardHeight);//vertical
         //    g.drawLine(0 , i*tileSize , boardWidth, i*tileSize);//horizontal
            
         //   }
      
           //Food 
           g.setColor(Color.RED);
           g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize,true);

           //Snake Head
            g.setColor(Color.WHITE);
            g.fill3DRect(snakeHead.x * tileSize, snakeHead.y*tileSize, tileSize, tileSize,true); //tile size for the width and height

           //Snake Body
           for(int i = 0; i < snakeBody.size(); i++ ){
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y*tileSize,tileSize, tileSize,true);
           }

           //Score
           g.setFont(new Font("Arial", Font.PLAIN, 16));
           if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
            
           }else{
             g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
           }

            
        } 
        //Randomly sets the x and y coordinates of the food
        public void placeFood(){
         food.x = random.nextInt(boardWidth/tileSize);  //600/25 = 24;
         food.x = random.nextInt(boardHeight/tileSize);
        }

        public boolean collision(Tile tile1, Tile tile2){
         return tile1.x == tile2.x && tile1.y == tile2.y;
        }

        public void move(){
         //eatFood
         if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
         }

         //Snake Body
         for(int i = snakeBody.size() - 1; i >= 0 ; i-- ){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
               snakePart.x = snakeHead.x;
               snakePart.y = snakeHead.y;
            }else{
              Tile prevSnakePart = snakeBody.get(i-1);
              snakePart.x = prevSnakePart.x;
              snakePart.y = prevSnakePart.y;

            }
         }

         //snake head
         snakeHead.x += velocityX;
         snakeHead.y += velocityY;

         //gameover conditions
         for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //collide with snake head
            if (collision(snakeHead, snakePart)){
               gameOver = true;
            }

         }

         if(snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardWidth){
            gameOver = true;
         }

        }

      @Override
      public void actionPerformed(ActionEvent e) {
         move();
         repaint();// calls draw over and over again
         if(gameOver){
            gameLoop.stop();
         }
      }

      @Override
      public void keyPressed(KeyEvent e) {
         if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityX = 0;
            velocityY = 0;
            
         }
         if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
         }else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
         }else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY =  0;
         }else  if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
         }
      }
      //DoNOt Need
      @Override
      public void keyTyped(KeyEvent e) {}
      
      @Override
      public void keyReleased(KeyEvent e) {}
    } 