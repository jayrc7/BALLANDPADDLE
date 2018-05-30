import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
/**
 * Filename: BallandPaddle.java
 * @author Jason Cabrera
 * @version 1.0
 *
 * Date: December 18, 2017
 * Email: jrcabrer@ucsd.edu
 *
 *
 */
//Contains GUI and implementation for game.
public class BallandPaddle extends Application{
 //screen properties
 public final int sceneWidth = 350;
 public final int sceneHeight = 600;
 
 public int count = 0;             //used to increase ball speed
 
 //ball properties
 public Circle ball;
 public final int ballRadius = 10;
 public int ballX = 300;
 public int ballY = 300;
 public int ballXSpeed = 3;
 public int ballYSpeed= 3;
 public int currentBallXSpeed;          //used to stop ball with space key
 public int currentBallYSpeed;     //used to stop ball with space key
 
 //paddle properties
 public final int paddleWidth = 60;
 public final int paddleHeight = 10;
 public int paddleX = sceneWidth/2;
 public int paddleY = sceneHeight - 50;
 
 //brick properties
 public final int brickWidth = 40;
 public final int brickHeight = 15;
 
 //brick array properties
 public Rectangle[][] filled = new Rectangle[11][5];
 public Rectangle[][] framed = new Rectangle[11][5];
 
 public int score = 0;
 
 //Gui properties
 public BorderPane pane;
 public StackPane stack;
 public AnimationTimer animator;
 public boolean gameOver = false;
 public Text forScore;
 public Pane center;
 public int forHit = 0;
 public boolean hit = false;
 
 //main method
 public static void main(String[] args) {
  launch(args);
 }
    
 /** method controls the game
  * @param stage where the game will be displayed.
  */
 public void start(Stage stage) {
  //setup
  pane = new BorderPane();
  stack = new StackPane(pane);
  Scene scene = new Scene(stack, sceneWidth, sceneHeight);
  stage.setTitle("Ball and Paddle");
  stage.setScene(scene);
  stage.setResizable(false);
  stage.show();
  
  //displays name of game
  HBox top = new HBox();
  Text game = new Text();
  game.setText("Ball and Paddle");
  game.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
  game.setFill(Color.BLACK);
  top.getChildren().add(game);
 
  //displays score
  forScore = new Text();
  forScore.setText("Score: "+ score);
  forScore.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
  forScore.setFill(Color.BLACK);
  top.getChildren().add(forScore);
  top.setAlignment(Pos.CENTER);
  top.setStyle("-fx-background-color: rgb(255,100,201)");
  top.setSpacing(60);
  pane.setTop(top);
  
 
  //will contain game
  center = new Pane();  
    
  Rectangle rect = new Rectangle(paddleX, paddleY, paddleWidth, paddleHeight);
  
  //creates bricks
  for(int j = 0; j<=4;j++) {
  for(int i = 0;i<11;i++) {
   filled[i][j] = new Rectangle(brickWidth*i,brickHeight*j,brickWidth, brickHeight);
   center.getChildren().add(filled[i][j]);
   framed[i][j] = new Rectangle(brickWidth*i, brickHeight*j,brickWidth,brickHeight);
   framed[i][j].setFill(null);
   framed[i][j].setStroke(Color.BLUE);
   center.getChildren().add(framed[i][j]);
   
      
  }
  }
  
  pane.setCenter(center);
  pane.setStyle("-fx-background-color: rgb(57,150,200)");
  
  //takes care of keyboard input
  final Box keyboardNode = new Box();
  keyboardNode.setFocusTraversable(true);
  keyboardNode.requestFocus();
  keyboardNode.setOnKeyPressed(e ->{
   if(gameOver == false) {
   if(e.getCode() == KeyCode.SPACE) {
       if(count++ % 2 == 1) {
         ballXSpeed = currentBallXSpeed;
            ballYSpeed = currentBallYSpeed;
       }
       
       else {
        currentBallXSpeed = ballXSpeed;
        currentBallYSpeed = ballYSpeed;
        ballXSpeed = 0;
        ballYSpeed =0;
       }
   }
   
   if(e.getCode() == KeyCode.LEFT) {
    paddleX -= 20;       
    rect.setX(paddleX);
   }
   
   if(e.getCode() == KeyCode.RIGHT) {
    paddleX +=20;
    rect.setX(paddleX);
   }
   }
 
  });
  
  //added to pane
  ball = new Circle(ballX, ballY, ballRadius);
  center.getChildren().add(keyboardNode);
  center.getChildren().add(rect);
  center.getChildren().add(ball);
  
      animator = new AnimationTimer() {
   public void handle(long arg0) {
    //ball movement
    ballX+= ballXSpeed;
    ballY+= ballYSpeed;
    
    //right boundary
    if(ballX+ballRadius > 350) {
     ballXSpeed *= -1;
    }
    
    //game over
    if(ballY - ballRadius > scene.getHeight()) {
     gameOver = true;
     animator.stop();
     Rectangle over = new Rectangle(pane.getWidth(),pane.getHeight(), Color.rgb(238,228,218,0.73));
     Text overText = new Text();
     overText.setFill(Color.RED);
     overText.setText("Game Over!");
     overText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
     StackPane.setAlignment(overText, Pos.CENTER);
     stack.getChildren().add(over);
     stack.getChildren().add(overText);
     
    }
    
    //left boundary
    if(ballX - ballRadius < 0) {
     ballXSpeed *= -1;
    }
    
    //top boundary
    if(ballY - ballRadius < 0) {
     ballYSpeed *= -1;
    }
    
    
    //when ball hits paddle
    if(ballY + ballRadius >= paddleY + 5 && ballY+ballRadius-5 <= paddleY+rect.getHeight() && ballX-ballRadius>= paddleX -10 && ballX+ballRadius <= paddleX+rect.getWidth() + 15) {
     if(hit == false) {
     //boolean added so that ball will only change direction once
     hit =true;
     ballYSpeed *= -1;
     }
    }
    
    //paddle right boundary
    if(paddleX + 60 > sceneWidth + 15) {
     paddleX = sceneWidth - 55;
    }
    
    //paddle left boundary
    if(paddleX< 0-5) {
     paddleX = 0-5;
    }
   
    //ball movement
    ball.setCenterX(ballX);
    ball.setCenterY(ballY);
    
    isHit();
    
    hit = false;
    
   }
   
  };
  animator.start();
  
  
 }
 
 /** removes brick when hit. */
 public void isHit() {
  for(int j = 0; j<=4; j++) {
   for(int i = 0; i<11;i++) {
    if(ball.intersects(framed[i][j].getX(), framed[i][j].getY(), framed[i][j].getWidth(), framed[i][j].getHeight())) {
     if(contains(framed[i][j])) {
     forHit++;
        score+=10;
        forScore.setText("Score: "+ score);
     ballYSpeed *= -1;
     center.getChildren().remove(framed[i][j]);
     center.getChildren().remove(filled[i][j]);
     framed[i][j].setX(-1);
     if(forHit % 10 == 0 && forHit<=20) {
      if(ballXSpeed>0) {
      ballXSpeed++;
      }
      else {
       ballXSpeed--;
      }
      if(ballYSpeed >0) {
      ballYSpeed++;
      }
      else {
       ballYSpeed--;
      }
      
     }
     
     }
    }
   }
  }
  
 }
 
 /** method is called to check whether brick array contains rectangle
  * @param r rectangle that has currently been hit
  * @return true if the rectangle has yet to be hit, false if otherwise.
  */
 public boolean contains(Rectangle r) {
    if(r.getX()==-1) {
     return false;
    }
    else {
     
     return true;
    }
 }
 
 
}


