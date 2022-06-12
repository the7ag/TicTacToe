package TicTacToe;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	Stage mainStage;
	Scene mainScene;

	Button[] xo = new Button[9]; // XO buttons

	Label topLabel = new Label(); // Show whose turn & who's won

	Line line = new Line(); // The winning line

	GridPane topGrid = new GridPane(); // Contain top buttons & topLabel

	int turn; // 0 --> The game is finished | 1 --> X turn | 2 --> O turn

	double height = 600, width = 500; // Initial dimensions

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		mainStage = stage; // set the app stage
		topLabel.setText("First-Player Turn"); // set top label
		line = new Line(); // initialize the line that sets on top of the winning buttons
		turn = 1; // set the turn for X
		BorderPane layout = new BorderPane(); // the main layout to set two grids in it

		// setting a gradient as a background
		LinearGradient paint = new LinearGradient(0.0, 0.5, 1.0, 0.5, true, null, // starting and ending points
																					// (X1,Y1),(X2,Y2)
				new Stop(0.0, Color.web("#69ff69")), // the first color (left)
				new Stop(1.0, Color.web("#69edff"))); // the second color (right)
		layout.setBackground(new Background(new BackgroundFill(paint, null, null)));

		topGrid = new GridPane(); // add a new grid pane to the top
		topGrid.setHgap(20); // set the gap between objects in the top grid
		topGrid.setPrefHeight(80); // set the grid height
		topGrid.setAlignment(Pos.CENTER); // set the Alignment of the grid

		Button[] top = new Button[2]; // set up the top buttons
		// set up the common features
		for (int i = 0; i < 2; i++) {
			top[i] = new Button(); // initializing the buttons
			top[i].setFont(Font.font("System", FontWeight.BOLD, 19));
			top[i].setTextFill(Color.WHITE);
			topBtnOpacity(top[i], 0.15); // set the background color & opacity
			int num = i; // copy i value in num as lambda can't read i

			// make the background darker when hovering on top of the button
			top[i].setOnMouseEntered(e -> topBtnOpacity(top[num], 0.45));
			// return the background opacity when the mouse leaves the button
			top[i].setOnMouseExited(e -> topBtnOpacity(top[num], 0.15));
		}

		top[0].setText("   Quit   "); // the button to quit the game
		topGrid.add(top[0], 0, 0); // add it to the left
		top[0].setOnAction(e -> System.exit(0)); // set the action of the quit button
		top[1].setText(" Restart "); // the button to restart the game
		topGrid.add(top[1], 2, 0); // add it to the right
		top[1].setOnAction(e -> { // sets the action on the restart button
			height = mainScene.getHeight();
			width = mainScene.getWidth(); // change the dimensions when restarting
			start(mainStage); // and call the program again
		});

		topLabel.setFont(Font.font("System", FontWeight.BOLD, 19)); // changes the font of top label
		topLabel.setAlignment(Pos.CENTER); // make the Label centered
		topLabel.setMinWidth(185); // set minimum width
		topGrid.add(topLabel, 1, 0); // set it to the middle

		GridPane centerGrid = new GridPane(); // center grid pane in the middle of the layout pane
		centerGrid.setPadding(new Insets(5)); // change the spacing between the grid and other borders
		centerGrid.setGridLinesVisible(true); // show the lines that Separates to XO buttons added to the grid

		for (int i = 0; i < 9; i++) {
			xo[i] = new Button(); // initializing the buttons
			// set the dimensions of the buttons relative to the center grid
			xo[i].prefWidthProperty().bind(centerGrid.widthProperty().divide(3.0));
			xo[i].prefHeightProperty().bind(centerGrid.heightProperty().divide(3.0));
			xo[i].setFont(Font.font("Bookshelf Symbol 7", 65)); // set the X & O font
			xo[i].setBackground(null); // make the background transparent
			centerGrid.add(xo[i], i % 3, i / 3);
			/*
			 * Distribute the buttons on the grid as i % 3 is the column address as it
			 * equals 0 then 1 then 2 in repeat i / 3 is the row address as it increases
			 * once every 3 loops The XO buttons : 0 | 1 | 2 3 | 4 | 5 6 | 7 | 8
			 */
			int num = i; // copy i value in num as lambda can't read i
			xo[i].setOnAction(e -> click(num)); // call the click method with each click
		}

		layout.setTop(topGrid); // adds the top grid to the main layout pane
		layout.setCenter(centerGrid); // adds the center grid to the main layout pane
		layout.getChildren().add(line); // adds the line to the main layout pane

		// set the icon of the stage
		mainStage.getIcons().add(new Image("file:src/main/java/TicTacToe/icon.png"));
		mainStage.setTitle("Tic Tac Toe"); // set the title of the stage
		mainScene = new Scene(layout, width, height); // add layout to the scene
		mainStage.setScene(mainScene); // add the scene to the stage
		mainStage.setMinHeight(500); // set the minimum height
		mainStage.setMinWidth(450); // set the minimum width
		mainStage.show(); // shows the main stage
	}

	// Change the opacity of the top buttons while hovering
	public void topBtnOpacity(Button btn, double i) {
		// make the background black and set the opacity of it as sent in the method
		// CornerRadii is for curving the edges
		btn.setBackground(new Background(new BackgroundFill(Color.web("#000000", i), new CornerRadii(100), null)));
	}

	// When XO button is clicked
	public void click(int num) {
		// only activate code when the clicked button is empty
		if (xo[num].getText() == "") {
			fade(xo[num], 1); // fades the text as its shown
			if (turn == 1) { // checks if it's X turn
				xo[num].setText("X"); // shows X on the button
				topLabel.setText("Second-Player Turn"); // changes the label
				turn = 2; // changes the turn
				winCheck(xo[num].getText()); // check if X win
			} else if (turn == 2) { // if it's O turn
				xo[num].setText("O"); // shows O on the buttons
				topLabel.setText("First-Player Turn"); // changes the label
				turn = 1; // changes the turn
				winCheck(xo[num].getText()); // checks if O win
			}
		}
	}

	// Check all win conditions
	public void winCheck(String s) {
		for (int x = 0; x < 3; x++) {
			if (checkAll(s, x, x + 3, x + 6)) { // the 3 Vertical lines
				win(s, x, x + 6); // send the winning letter & the buttons at both ends
				return; // end the method
			}
		}
		for (int x = 0; x < 9; x += 3) {
			if (checkAll(s, x, x + 1, x + 2)) { // the 3 Horizontal lines
				win(s, x, x + 2); // send the winning letter & the buttons at both ends
				return; // end the method
			}
		}
		if (checkAll(s, 0, 4, 8)) { // the first Diagonal \
			win(s, 0, 8); // send the winning letter & the buttons at both ends
			return; // end the method
		}
		if (checkAll(s, 2, 4, 6)) { // the second Diagonal /
			win(s, 2, 6); // send the winning letter & the buttons at both ends
			return; // end the method
		}
		if (allFull()) { // when buttons are filled & no one's won
			topLabel.setText("Draw"); // show the draw message
			endGame(); // end the game
		}
	}

	// Return true if all 3 buttons have the same mentioned text
	public boolean checkAll(String s, int i1, int i2, int i3) {
		return ((xo[i1].getText() == s) & (xo[i2].getText() == s) & (xo[i3].getText() == s));
	}

	// Return true when all buttons are filled
	public boolean allFull() {
		for (int i = 0; i < 9; i++) {
			if (xo[i].getText() == "")
				return false;
		}
		return true;
	}

	// Display who win with line & topLabel
	public void win(String s, int i1, int i2) {

		// changes the top label to the winner
		if (s == "X")
			topLabel.setText("First-Player WON");
		else
			topLabel.setText("Second-Player WON");

		// set the line from the center of the first button to the center of last one
		line.startXProperty().bind(xo[i1].layoutXProperty().add(xo[i1].widthProperty().divide(2.0)));
		line.startYProperty()
				.bind(xo[i1].layoutYProperty().add(xo[i1].heightProperty().divide(2.0)).add(topGrid.heightProperty()));
		line.endXProperty().bind(xo[i2].layoutXProperty().add(xo[i2].widthProperty().divide(2.0)));
		line.endYProperty()
				.bind(xo[i2].layoutYProperty().add(xo[i2].heightProperty().divide(2.0)).add(topGrid.heightProperty()));

		line.setStrokeLineCap(StrokeLineCap.ROUND); // changes the end of the lines to be rounded
		line.setStrokeWidth(5); // changes the line thickness
		// change the line color for each winner
		if (s == "X")
			line.setStroke(Color.RED);
		else
			line.setStroke(Color.BLUE);

		scale(line, 1.2); // scale animation for the line
		fade(line, 0.7); // fade animation for the line
		endGame(); // call the end game method
	}

	// Scale animation for 0.25 second
	public void scale(Node node, double i) {
		ScaleTransition scale = new ScaleTransition(); // create a scaling animation
		scale.setNode(node); // set it for the sent node (Ex: line)
		scale.setDuration(Duration.millis(250)); // the time of the animation
		scale.setFromX(0); // starting dimensions is 0%
		scale.setFromY(0);
		scale.setToX(i); // ending dimensions is i*100% of the original
		scale.setToY(i);
		scale.play(); // start the animation
	}

	// Fade animation for 0.2 second
	public void fade(Node node, double i) {
		FadeTransition fade = new FadeTransition(); // create a fading animation
		fade.setNode(node); // set it for the sent node (Ex: line)
		fade.setDuration(Duration.millis(200)); // the time of the animation
		fade.setFromValue(0); // starting opacity is 0%
		fade.setToValue(i); // ending opacity is i*100% of the original
		fade.play(); // start the animation
	}

	// disable the click method
	public void endGame() {
		turn = 0; // it sets the game to end turn
		scale(topLabel, 1); // scale animation
		fade(topLabel, 1); // fade animation
	}
}