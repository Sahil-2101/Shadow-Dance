import bagel.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2023
 * Please enter your name below
 * @author Sahil Khatri
 */
public class ShadowDance extends AbstractGame {
    //importing other classes
    private final LaneManager laneManager;
    private final NoteManager noteManager;
    //all the definitions
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");

    private boolean showInstructions = true;
    private boolean gameOver = false;
    public final Font mainFont;
    public final Font smallFont;
    public final Font scoreFont;
    public final Font successFont;
    public int leftX, rightX, upX, downX;
    public int[] leftNormal = new int[100];
    public int[] leftHold = new int[20];
    public int[] rightNormal = new int[100];
    public int[] rightHold = new int[20];
    public int[] upNormal = new int[100];
    public int[] upHold = new int[20];
    public int[]downNormal = new int[100];
    public int[] downHold = new int[20];
    public int leftN = 0, leftH = 0, rightN = 0, rightH = 0, upN = 0, upH = 0, downN = 0, downH = 0;
    private int finalScore = 0, frameCatch = -40, messageDisplay;
    private double numRec = -4;
    public int FrameCount = 0, noteDistance;


    public ShadowDance() {

        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        //defining fonts for the welcome screen
        mainFont = new Font("res/FSO8BITR.TTF", 64);
        smallFont = new Font("res/FSO8BITR.TTF", 24);
        scoreFont = new Font("res/FSO8BITR.TTF", 30);
        successFont = new Font("res/FSO8BITR.TTF", 40);
        //importing other classes
        laneManager = new LaneManager();
        noteManager = new NoteManager();
        //calling readCSV to read the file for the coordinates of the lane and the notes
        readCSV();

    }

    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     */
    private void readCSV() {
        // reading the csv file and including an exception if file not found
        try(BufferedReader reader = new BufferedReader(new FileReader("res/level1.csv"))){
            String dataPoint;
            // reading each line and separating it
            while((dataPoint = reader.readLine()) != null){
                numRec++;
                String[] separatedData = dataPoint.split(",");
                //reading x coordinates of all the lanes
                if (separatedData[0].equalsIgnoreCase("lane")){
                    if(separatedData[1].equalsIgnoreCase("left")){
                        leftX = Integer.parseInt(separatedData[2]);
                    }
                    else if(separatedData[1].equalsIgnoreCase("right")){
                        rightX = Integer.parseInt(separatedData[2]);
                    }
                    else if(separatedData[1].equalsIgnoreCase("up")){
                        upX = Integer.parseInt(separatedData[2]);
                    }
                    else if(separatedData[1].equalsIgnoreCase("down")){
                        downX = Integer.parseInt(separatedData[2]);
                    }
                }
                else if(separatedData[0].equalsIgnoreCase("left")){
                    if(separatedData[1].equalsIgnoreCase("normal")){
                        leftNormal[leftN] = Integer.parseInt(separatedData[2]);
                        leftN++;
                    }
                    else if(separatedData[1].equalsIgnoreCase("hold")){
                        leftHold[leftH] = Integer.parseInt(separatedData[2]);
                        leftH++;
                    }
                }
                else if(separatedData[0].equalsIgnoreCase("right")){
                    if(separatedData[1].equalsIgnoreCase("normal")){
                        rightNormal[rightN] = Integer.parseInt(separatedData[2]);
                        rightN++;
                    }
                    else if(separatedData[1].equalsIgnoreCase("hold")){
                        rightHold[rightH] = Integer.parseInt(separatedData[2]);
                        rightH++;
                    }
                }
                else if(separatedData[0].equalsIgnoreCase("up")){
                    if(separatedData[1].equalsIgnoreCase("normal")){
                        upNormal[upN] = Integer.parseInt(separatedData[2]);
                        upN++;
                    }
                    else if(separatedData[1].equalsIgnoreCase("hold")){
                        upHold[upH] = Integer.parseInt(separatedData[2]);
                        upH++;
                    }
                }
                else if(separatedData[0].equalsIgnoreCase("down")){
                    if(separatedData[1].equalsIgnoreCase("normal")){
                        downNormal[downN] = Integer.parseInt(separatedData[2]);
                        downN++;
                    }
                    else if(separatedData[1].equalsIgnoreCase("hold")){
                        downHold[downH] = Integer.parseInt(separatedData[2]);
                        downH++;
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }
    // this function is used to update distance and print the message
    public void updateDistance(int distance){
        //converting distance from negative to positive
        if(distance < 0){
            distance *= -1;
        }
        if(distance == 0){
            // skip each frame where note distance is nothing as key is not pressed
            distance = 1;
        }
        //updating score and selecting appropriate message
        else if(distance <= 15){
            finalScore += 10;
            frameCatch = FrameCount;
            messageDisplay = 0;
        }
        else if(distance <= 50){
            finalScore += 5;
            frameCatch = FrameCount;
            messageDisplay = 1;
        }
        else if(distance <= 100){
            finalScore -= 1;
            frameCatch = FrameCount;
            messageDisplay = 2;
        }
        else{
            finalScore -= 5;
            frameCatch = FrameCount;
            messageDisplay = 3;
        }
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        // exiting the game if esc is pressed
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        // otherwise refreshing note distance to 0
        else {
            noteDistance = 0;
        }
        // if space is pressed then instructions are removed
        if (showInstructions && input.wasPressed(Keys.SPACE)) {
            showInstructions = false;
        }
        // drawing the background, the welcome page, and the lanes
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        if (showInstructions) {
            mainFont.drawString("SHADOW DANCE", 220, 250);
            smallFont.drawString("PRESS SPACE TO START", 320, 440);
            smallFont.drawString("USE ARROW KEYS TO PLAY", 305, 500);
        }
        // while game is not over run the notes
        else if(!gameOver){
            // count frames with each update
            FrameCount++;
            // calling update functions from both classes and printing the score
            laneManager.update(this);
            scoreFont.drawString(("SCORE " + finalScore), 30, 30);
            noteManager.update(input, this);
            updateDistance(noteManager.noteDistance);
            //print the message selected in updateDistance
            if (FrameCount <= frameCatch + 30){
                if(messageDisplay == 0){
                    successFont.drawString("PERFECT", 400, 250);
                }
                else if(messageDisplay == 1){
                    successFont.drawString("GOOD", 450, 250);
                }
                else if(messageDisplay == 2){
                    successFont.drawString("BAD", 460, 250);
                }
                else if(messageDisplay == 3){
                    successFont.drawString("MISS", 450, 250);
                }
            }
        }
        //if all notes have been passed then game is over and print the final message
        if(numRec == noteManager.recOver){
            gameOver = true;
            if(finalScore >= 150){
                mainFont.drawString("CLEAR!", 350, 250);
            }
            else {
                mainFont.drawString("TRY AGAIN", 300, 250);
            }
        }

    }
}
