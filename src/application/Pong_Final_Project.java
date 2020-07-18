package application;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Pong_Final_Project extends Application {
	//variables that are made global as they are applied to many blocks of code
	private final int H = 700;
	private final int W = 1000;
	private static int playerOnescore;
	private static int playerTwoscore; 
	private double movement=0;
	private double ballX;
	private double ballY;
	
	@Override
	public void start(Stage stage) {	
		Group gamepieces = new Group();
	 	Scene gamescene = new Scene(gamepieces, W, H , Color.BLACK);
	 	
	 	VBox vert = new VBox();
	 	vert.setAlignment(Pos.TOP_CENTER);
	    //A string representation of the CSS style associated with the specific object.
	    vert.setStyle("-fx-padding: 10;" + 
	    		"-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-background-color: BLACK;"+
                "-fx-color : BLACK;");
	    vert.setSpacing(10);
	 	Scene instructionscene = new Scene(vert, W, H);
	    	    
	 	VBox box = new VBox(); 
	    box.setAlignment(Pos.TOP_CENTER);
	    //A string representation of the CSS style associated with the specific object.
	    box.setStyle("-fx-padding: 10;" + 
	    		"-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-background-color: BLACK;"+
                "-fx-color : BLACK;");
	    Scene mainscene = new Scene(box, W, H);     
	 
	    final Rectangle playerOne = new Rectangle(); 
		playerOne.setWidth(20);
		playerOne.setHeight(H/4);
		playerOne.setX(0);
		playerOne.setY(H/3);
		playerOne.setArcWidth(20);
		playerOne.setArcHeight(20);
		playerOne.setFill(Color.RED); 
		
		final Rectangle playerTwo = new Rectangle();
		playerTwo.setWidth(20);
		playerTwo.setHeight(H*0.25);	
		playerTwo.setX(W - playerTwo.getWidth());
		playerTwo.setY(H/3);
		playerTwo.setArcWidth(20);
		playerTwo.setArcHeight(20);
		playerTwo.setFill(Color.BLUE);     
		//creates centre circle of the arena
		final Circle circle = new Circle();
		circle.setLayoutX(W/2);
		circle.setLayoutY(H/2);
		circle.setRadius(100);
		circle.setStroke(Color.WHITE);
		circle.setStrokeWidth(3);  
		circle.setFill(Color.BLACK);

		Rectangle line = new Rectangle();
		line.setX(W/2);
		line.setY(0);
		line.setWidth(3);
		line.setHeight(H);
		line.setFill(Color.WHITE);
     
		Circle ball = new Circle(20, Color.WHITE);
		ball.setLayoutX(W/2);
		ball.setLayoutY(H/2);

		gamepieces.getChildren().addAll(circle,line,ball,playerOne,playerTwo); 
   
		Label label = new Label();
		label.setFont(new Font("Arial", 40));
		label.setTextFill(Color.WHITE);
		gamepieces.getChildren().add(label);
    
		
		//Main game loop that displaces balls positions and checks its conditions set to an amount of instances according to the Duration Method
		Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			@Override
            	public void handle(ActionEvent wallhit) {
				ball.setLayoutX(ball.getLayoutX() + ballX);
            	ball.setLayoutY(ball.getLayoutY() + ballY);
            	
            	boolean atRightBorder = ball.getLayoutX() == (0 + ball.getRadius());
            	boolean atLeftBorder = ball.getLayoutX() == (W - ball.getRadius());
            	boolean atBottomBorder = ball.getLayoutY() == (0 + ball.getRadius());
            	boolean atTopBorder = ball.getLayoutY()  == (H - ball.getRadius());
            	boolean atPlayerOne = playerOne.getBoundsInParent().intersects(ball.getBoundsInParent());
            	boolean atPlayertwo = playerTwo.getBoundsInParent().intersects(ball.getBoundsInParent());
               
            	//Multiplies the displacement variable by -1 in order to completely reverse the direction in either X or Y Direction from the ball
            	if (atBottomBorder)
            		ballY *=-1;
               
            	if(atTopBorder)
            		ballY*=-1;
               
            	if (atLeftBorder){            	   
            		ballX *= -1;
            		playerOnescore++;
            	}
        
            	if(atRightBorder){
            		ballX *= -1;;
            		playerTwoscore++;
            	}             
               
            	if (atPlayerOne||atPlayertwo)
            		ballX *= -1;
            
            	// changes label declared outside timeline loop, declaring inside timeline loop causes multiple labels to be created
            	label.setText("\t\t"+ playerOnescore+"\t\t\t\t\t\t\t"+playerTwoscore+"\t\t\t");
               
            	/*Main win condition that ends game upon the restriction that either 
            	 * player's score must equal 10. When this is met, All scores are set
            	 *  to zero and postions are set to where there were at the start. 
            	 *  This allows a new counter for a new game the user might want to play.
            	*/
            	if(playerOnescore ==10 || playerTwoscore ==10){
            		playerTwoscore = 0;
            		playerOnescore = 0;
            		ball.setLayoutX(W/2);
            		ball.setLayoutY(H/2);
            		playerTwo.setY(H/3);
            		playerOne.setY(H/3); 
            		movement = 0;
            		ballX = 0; 
            		ballY = 0;
            		stage.setScene(mainscene);
            	}
			}	
		}
				));
    
		loop.setCycleCount(Timeline.INDEFINITE);
		loop.play();
		
		/*Maps keys stated in instructions to displace the rectangles playerOne and playerTwo
		 *  by a certain amount of pixels & allows you to end end any game with BACKSPACE.
		 *  Conditions for BACKSPACE are essentially the same as the previous game winning 
		 *  conditions met in the timeline loop.
		 */
		gamescene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) { 
				if(event.getCode()==KeyCode.W)
					playerOne.setY(playerOne.getY() - movement);
				if(event.getCode()==KeyCode.S)
					playerOne.setY(playerOne.getY() + movement);
				if (event.getCode()==KeyCode.UP)
					playerTwo.setY(playerTwo.getY() - movement); 
				if (event.getCode()==KeyCode.DOWN)
					playerTwo.setY(playerTwo.getY() + movement);
				if (event.getCode()==KeyCode.BACK_SPACE){
					playerTwoscore = 0;
					playerOnescore = 0;
					ball.setLayoutX(W/2);
					ball.setLayoutY(H/2);
					playerTwo.setY(H/3); 
					playerOne.setY(H/3);
					movement = 0;
					ballX = 0;
					ballY = 0;	
					stage.setScene(mainscene); 
				}
			}    
		}
				);
		
		//Main Menu Screen 
		Text title = new Text("Pong: Game by Naod & Aedan");
		title.setFont(new Font("",40));
		title.setFill(Color.GOLD);
		
		//blank label creates space between inital title and button and/or instructions
		Label spacing  = new Label();
		spacing.setPrefHeight(50);		
		Label sacing  = new Label();
		sacing.setPrefHeight(100);
		
		Button easy = new Button("Easy");
		easy.setPrefSize(300, 100);
		easy.setFont(new Font("Arial",30));
    
		Button medium = new Button("Medium");
		medium.setPrefSize(300, 100);
		medium.setFont(new Font("Arial",30));
    
		Button hard = new Button("Hard");
		hard.setPrefSize(300, 100);
		hard.setFont(new Font("Arial",30));
		
		Button howtoplay = new Button("How to Play");
		howtoplay.setPrefSize(300, 100);
		howtoplay.setFont(new Font("Arial",30));
		box.getChildren().addAll(title,sacing,easy,medium,hard,howtoplay);

		//Instructions page
    	Text title2 = new Text("How to play");
    	title2.setFont(new Font("Arial",25));
    	title2.setUnderline(true);
    	title2.setFill(Color.WHITE);
    	
    	Text P1instructions = new Text("Player One(Red): Move Up - W \n \t\t\t Move Down - S");
    	P1instructions.setFont(new Font("Arial",20));
    	P1instructions.setFill(Color.WHITE);
    	
    	Text P2instructions = new Text("Player Two(Blue): Move Up - ↑ \n \t\t\t Move Down - ↓");
    	P2instructions.setFont(new Font("Arial",20));
    	P2instructions.setFill(Color.WHITE);
    	
    	Text gamebasis = new Text("The main objective of this game is to beat your opponent in a table tennis-like game to 10 points."
    								+ "\n This version is different than most Pong games in the fact that games are continuous"
    								+ "\n To return to the Main Menu Screen, press BACKSPACE");
    	gamebasis.setFont(new Font("Arial",20));
    	gamebasis.setFill(Color.WHITE);
    	
    	vert.getChildren().addAll(title2,spacing, P1instructions,P2instructions,gamebasis);

    	easy.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			stage.setScene(gamescene);
    			movement = 20;
    			ballX = 3; 
    			ballY = 3;
    			playerOnescore = 0;
    			playerTwoscore = 0;             
    		}
    	});
    
    	medium.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			stage.setScene(gamescene);
    			movement = 20;
    			ballX = 5; 
    			ballY = 5;
    			playerOnescore = 0;
    			playerTwoscore = 0;             
    		}
    	});
    	
    	hard.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			stage.setScene(gamescene);
    			movement = 20;
    			ballX = 10; 
    			ballY = 10;
    			playerOnescore = 0;
    			playerTwoscore = 0;  	
    		}
    	});
    	howtoplay.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    		public void handle(ActionEvent e) {
    			stage.setScene(instructionscene);
    			 	
    		}
    	});
    	instructionscene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) { 
				if (event.getCode()==KeyCode.BACK_SPACE){	
					stage.setScene(mainscene); 
				}
			}    
		}
				);
    	
    	stage.setScene(mainscene);
	    stage.show();    
	}
  
  public static void main(String[] args) {
	  launch(args);
	  }

}
