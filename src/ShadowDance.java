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
    private final SpecialLaneManager specialLaneManager;
    private final SpecialNoteManager specialNoteManager;
    private final GuardianLaneManager guardianLaneManager;
    private final GuardianNoteManager guardianNoteManager;
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
    public int leftX = 0, rightX = 0, upX = 0, downX = 0, specialX = 0;
    public int[] leftNormal = new int[100];
    public int[] leftHold = new int[20];
    public int[] leftBomb = new int[100];
    public int[] rightNormal = new int[100];
    public int[] rightHold = new int[20];
    public int[] rightBomb = new int[100];
    public int[] upNormal = new int[100];
    public int[] upHold = new int[20];
    public int[] upBomb = new int[100];
    public int[]downNormal = new int[100];
    public int[] downHold = new int[20];
    public int[] downBomb = new int[100];
    public int[] bombS = new int[100];
    public int[] speedUpS = new int[100];
    public int[] slowDownS = new int[100];
    public int[] doubleScoreS = new int[100];
    public int leftN = 0, leftH = 0, leftB = 0, rightN = 0, rightH = 0, rightB = 0, upN = 0, upH = 0, upB = 0,
            downN = 0, downH = 0, downB = 0, sBomb = 0, sSpeed = 0, sSlow = 0, sDouble = 0;
    private int frameCatch = -40, messageDisplay;
    private double numRec = -4;
    public int FrameCount = 0, noteDistance, levelSelected = 0, freezeFrame = -500, finalScore = 0;


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
        specialLaneManager = new SpecialLaneManager();
        specialNoteManager = new SpecialNoteManager();
        guardianLaneManager = new GuardianLaneManager();
        guardianNoteManager = new GuardianNoteManager();

    }

    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     */
    private void readCSV(String fileName) {
        // reading the csv file and including an exception if file not found
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String dataPoint;
            // reading each line and separating it
            while ((dataPoint = reader.readLine()) != null) {
                numRec++;
                String[] separatedData = dataPoint.split(",");
                //reading x coordinates of all the lanes
                if (separatedData[0].equalsIgnoreCase("lane")) {
                    if (separatedData[1].equalsIgnoreCase("left")) {
                        leftX = Integer.parseInt(separatedData[2]);
                    } else if (separatedData[1].equalsIgnoreCase("right")) {
                        rightX = Integer.parseInt(separatedData[2]);
                    } else if (separatedData[1].equalsIgnoreCase("up")) {
                        upX = Integer.parseInt(separatedData[2]);
                    } else if (separatedData[1].equalsIgnoreCase("down")) {
                        downX = Integer.parseInt(separatedData[2]);
                    } else if (separatedData[1].equalsIgnoreCase("special")) {
                        specialX = Integer.parseInt(separatedData[2]);
                    }
                } else if (separatedData[0].equalsIgnoreCase("left")) {
                    if (separatedData[1].equalsIgnoreCase("normal")) {
                        leftNormal[leftN] = Integer.parseInt(separatedData[2]);
                        leftN++;
                    } else if (separatedData[1].equalsIgnoreCase("hold")) {
                        leftHold[leftH] = Integer.parseInt(separatedData[2]);
                        leftH++;
                    } else if (separatedData[1].equalsIgnoreCase("bomb")) {
                        leftBomb[leftB] = Integer.parseInt(separatedData[2]);
                        leftB++;
                    }
                } else if (separatedData[0].equalsIgnoreCase("right")) {
                    if (separatedData[1].equalsIgnoreCase("normal")) {
                        rightNormal[rightN] = Integer.parseInt(separatedData[2]);
                        rightN++;
                    } else if (separatedData[1].equalsIgnoreCase("hold")) {
                        rightHold[rightH] = Integer.parseInt(separatedData[2]);
                        rightH++;
                    } else if (separatedData[1].equalsIgnoreCase("bomb")) {
                        rightBomb[rightB] = Integer.parseInt(separatedData[2]);
                        rightB++;
                    }
                } else if (separatedData[0].equalsIgnoreCase("up")) {
                    if (separatedData[1].equalsIgnoreCase("normal")) {
                        upNormal[upN] = Integer.parseInt(separatedData[2]);
                        upN++;
                    } else if (separatedData[1].equalsIgnoreCase("hold")) {
                        upHold[upH] = Integer.parseInt(separatedData[2]);
                        upH++;
                    } else if (separatedData[1].equalsIgnoreCase("bomb")) {
                        upBomb[upB] = Integer.parseInt(separatedData[2]);
                        upB++;
                    }
                } else if (separatedData[0].equalsIgnoreCase("down")) {
                    if (separatedData[1].equalsIgnoreCase("normal")) {
                        downNormal[downN] = Integer.parseInt(separatedData[2]);
                        downN++;
                    } else if (separatedData[1].equalsIgnoreCase("hold")) {
                        downHold[downH] = Integer.parseInt(separatedData[2]);
                        downH++;
                    } else if (separatedData[1].equalsIgnoreCase("bomb")) {
                        downBomb[downB] = Integer.parseInt(separatedData[2]);
                        downB++;
                    }
                } else if (separatedData[0].equalsIgnoreCase("special")) {
                    if (separatedData[1].equalsIgnoreCase("speedup")) {
                        speedUpS[sSpeed] = Integer.parseInt(separatedData[2]);
                        sSpeed++;
                    } else if (separatedData[1].equalsIgnoreCase("slowdown")) {
                        slowDownS[sSlow] = Integer.parseInt(separatedData[2]);
                        sSlow++;
                    } else if (separatedData[1].equalsIgnoreCase("doublescore")) {
                        doubleScoreS[sDouble] = Integer.parseInt(separatedData[2]);
                        sDouble++;
                    } else if (separatedData[1].equalsIgnoreCase("bomb")) {
                        bombS[sBomb] = Integer.parseInt(separatedData[2]);
                        sBomb++;
                    }
                }
            }
        } catch (IOException e) {
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

    public void doubleUpdateDistance(int distance){
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
            finalScore += 20;
            frameCatch = FrameCount;
            messageDisplay = 0;
        }
        else if(distance <= 50){
            finalScore += 10;
            frameCatch = FrameCount;
            messageDisplay = 1;
        }
        else if(distance <= 100){
            finalScore -= 2;
            frameCatch = FrameCount;
            messageDisplay = 2;
        }
        else{
            finalScore -= 10;
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
        // if number is pressed then instructions are removed
        if (showInstructions && input.wasPressed(Keys.NUM_1)) {
            showInstructions = false;
            levelSelected = 1;
            //calling readCSV to read the file for the coordinates of the lane and the notes
            readCSV("res/level1.csv");
        }
        else if (showInstructions && input.wasPressed(Keys.NUM_2)) {
            showInstructions = false;
            levelSelected = 2;
            //calling readCSV to read the file for the coordinates of the lane and the notes
            readCSV("res/level2.csv");
        }
        else if (showInstructions && input.wasPressed(Keys.NUM_3)) {
            showInstructions = false;
            levelSelected = 3;
            //calling readCSV to read the file for the coordinates of the lane and the notes
            readCSV("res/level3.csv");
        }
        // drawing the background, the welcome page, and the lanes
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        if (showInstructions) {
            mainFont.drawString("SHADOW DANCE", 220, 250);
            smallFont.drawString("SELECT LEVELS WITH", 320, 440);
            smallFont.drawString("NUMBER KEYS", 320, 460);
            smallFont.drawString("\n", 320, 480);
            smallFont.drawString("1 2 3", 450, 500);
        }
        // while game is not over run the notes
        else if(levelSelected == 1) {
            if (!gameOver) {
                // count frames with each update
                FrameCount++;
                // calling update functions from both classes and printing the score
                laneManager.update(this);
                scoreFont.drawString(("SCORE " + finalScore), 30, 30);
                noteManager.update(input, this);
                updateDistance(noteManager.noteDistance);
                //print the message selected in updateDistance
                if (FrameCount <= frameCatch + 30) {
                    if (messageDisplay == 0) {
                        successFont.drawString("PERFECT", 400, 250);
                    } else if (messageDisplay == 1) {
                        successFont.drawString("GOOD", 450, 250);
                    } else if (messageDisplay == 2) {
                        successFont.drawString("BAD", 460, 250);
                    } else if (messageDisplay == 3) {
                        successFont.drawString("MISS", 450, 250);
                    }
                }
            }
            //if all notes have been passed then game is over and print the final message
            if (numRec == noteManager.recOver) {
                gameOver = true;
                if (finalScore >= 150) {
                    mainFont.drawString("CLEAR!", 350, 250);
                } else {
                    mainFont.drawString("TRY AGAIN", 300, 250);
                }
                smallFont.drawString("PRESS SPACE TO RETURN TO LEVEL SELECTION.", 150, 500);
            }
        }
        else if (levelSelected == 2){
            if (!gameOver){
                // count frame with each update
                FrameCount++;
                // calling update functions from both classes and printing the score
                specialLaneManager.update(this);
                scoreFont.drawString(("SCORE " + finalScore), 30, 30);
                specialNoteManager.update(input, this);
                // if double score is pressed
                if (FrameCount <= freezeFrame + 450){
                    doubleUpdateDistance(specialNoteManager.noteDistance);
                }
                else {
                    updateDistance(specialNoteManager.noteDistance);
                }
                //print the message selected in updateDistance

                if (FrameCount <= frameCatch + 30) {
                    if (messageDisplay == 0) {
                        successFont.drawString("PERFECT", 400, 250);
                    } else if (messageDisplay == 1) {
                        successFont.drawString("GOOD", 450, 250);
                    } else if (messageDisplay == 2) {
                        successFont.drawString("BAD", 460, 250);
                    } else if (messageDisplay == 3) {
                        successFont.drawString("MISS", 450, 250);
                    }
                }
            }
            // print final message
            if (numRec == specialNoteManager.recOver) {
                gameOver = true;
                if (finalScore >= 400) {
                    mainFont.drawString("CLEAR!", 350, 250);
                } else {
                    mainFont.drawString("TRY AGAIN", 300, 250);
                }
                smallFont.drawString("PRESS SPACE TO RETURN TO LEVEL SELECTION.", 100, 500);
            }
        }
        else if (levelSelected == 3){
            if(!gameOver){
                // count frames with each update
                FrameCount++;
                guardianLaneManager.update(this);
                scoreFont.drawString(("SCORE " + finalScore), 30, 30);
                guardianNoteManager.update(input, this);
                //if double score is pressed
                if (FrameCount <= freezeFrame + 450){
                    doubleUpdateDistance(guardianNoteManager.noteDistance);
                }
                else {
                    updateDistance(guardianNoteManager.noteDistance);
                }
                //print the message selected in updateDistance

                if (FrameCount <= frameCatch + 30) {
                    if (messageDisplay == 0) {
                        successFont.drawString("PERFECT", 400, 250);
                    } else if (messageDisplay == 1) {
                        successFont.drawString("GOOD", 450, 250);
                    } else if (messageDisplay == 2) {
                        successFont.drawString("BAD", 460, 250);
                    } else if (messageDisplay == 3) {
                        successFont.drawString("MISS", 450, 250);
                    }
                }

            }
            //print final message
            if (numRec == guardianNoteManager.recOver) {
                gameOver = true;
                if (finalScore >= 350) {
                    mainFont.drawString("CLEAR!", 350, 250);
                } else {
                    mainFont.drawString("TRY AGAIN", 300, 250);
                }
                smallFont.drawString("PRESS SPACE TO RETURN TO LEVEL SELECTION.", 100, 500);
            }
        }
        //if space is pressed then restart from start
        if(input.wasPressed(Keys.SPACE)){
            showInstructions = true;
            gameOver = false;
            finalScore = 0;
            FrameCount = 0;
            noteManager.recOver = 0;
            guardianNoteManager.recOver = 0;
            messageDisplay = -1;
        }

    }
}