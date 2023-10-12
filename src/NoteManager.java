import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class NoteManager {
    // define all the variables
    private final static int laneCenter = 657;
    private final static int placeholderValue = -768;
    private final static int arbitVal = 200;
    private final static int arbitValH = 250;
    private final static int WINDOW_HEIGHT = 768;
    private final Image LEFT_NOTE;
    private final Image RIGHT_NOTE;
    private final Image UP_NOTE;
    private final Image DOWN_NOTE;
    private final Image LEFT_NOTE_HOLD;
    private final Image RIGHT_NOTE_HOLD;
    private final Image UP_NOTE_HOLD;
    private final Image DOWN_NOTE_HOLD;
    public int normalOrHold = 0;
    private int FrameCount;
    public int recOver = 0, noteDistance, frameSpeed = 2;
    public NoteManager(){
        //loading all the images
        LEFT_NOTE = new Image("res/noteLeft.png");
        RIGHT_NOTE = new Image("res/noteRight.png");
        UP_NOTE = new Image("res/noteUp.png");
        DOWN_NOTE = new Image("res/noteDown.png");
        LEFT_NOTE_HOLD = new Image("res/holdNoteLeft.png");
        RIGHT_NOTE_HOLD = new Image("res/holdNoteRight.png");
        UP_NOTE_HOLD = new Image("res/holdNoteUp.png");
        DOWN_NOTE_HOLD = new Image("res/holdNoteDown.png");
    }
    //checking when the note is pressed what is the position of the note image
    public void notePressed(int noteNC, int noteNCTrav, int noteHC, int noteHCTrav, int[] noteNormal, int[] noteHold,
                             int frameCount){
        // checking all the normal notes to find which need to be displayed
        while(noteNC > noteNCTrav){
            if(noteNormal[noteNCTrav] <= frameCount &&
                    ((100+((frameCount - noteNormal[noteNCTrav])*frameSpeed)) <= WINDOW_HEIGHT)){
                normalOrHold = 1;
                recOver++;
                break;
            }
            noteNCTrav++;
        }
        //removing the normal note present if the note was clicked
        if (normalOrHold == 1) {
            noteDistance = laneCenter - (100 + ((frameCount - noteNormal[noteNCTrav]) * frameSpeed));
            noteNormal[noteNCTrav] = placeholderValue;
            normalOrHold = 0;
        }
        // now checking the same for hold notes
        else {
            while(noteHC > noteHCTrav){
                if(noteHold[noteHCTrav] <= frameCount &&
                        ((24+((frameCount - noteHold[noteHCTrav])*frameSpeed)) <= WINDOW_HEIGHT)){
                    normalOrHold = -1;
                    break;
                }
                noteHCTrav++;
            }
            if (normalOrHold == -1) {
                noteDistance = laneCenter - (24 + ((frameCount - noteHold[noteHCTrav]) * frameSpeed) + 82);
            }
        }
    }

    public void noteReleased(int noteHC, int noteHCTrav, int[] noteHold, int frameCount){
        //if the note was released where was it positioned
        while(noteHC > noteHCTrav){
            if(noteHold[noteHCTrav] <= frameCount &&
                    ((24+((frameCount - noteHold[noteHCTrav])*frameSpeed)-82) <= WINDOW_HEIGHT)){
                recOver++;
                break;
            }
            noteHCTrav++;
        }
        normalOrHold = 0;
        //finding the distance accordingly
        noteDistance = laneCenter - (24+((frameCount - noteHold[noteHCTrav])*frameSpeed)-82);
        noteHold[noteHCTrav] = placeholderValue;
    }
    //if the note comes in the frame rate then drawing the note
    public void noteNDraw(int noteNTrav, int[] noteNormal, Image dirNote, int noteX, int frameCount){
        if(noteNormal[noteNTrav] <= frameCount &&
                ((100+((frameCount - noteNormal[noteNTrav])*frameSpeed)) <= WINDOW_HEIGHT)){
            dirNote.draw(noteX, 100+((frameCount - noteNormal[noteNTrav])*frameSpeed));
        }
        //if the note goes out of the window height then remove the note and count it as a miss
        if((100+((frameCount - noteNormal[noteNTrav])*frameSpeed) <= WINDOW_HEIGHT + 2) &&
                (100+((frameCount - noteNormal[noteNTrav])*frameSpeed) >= WINDOW_HEIGHT)){
            noteDistance = arbitVal;
            noteNormal[noteNTrav] = placeholderValue;
            recOver++;
        }
    }
    //if the hold note comes in the frame rate then draw the hold note
    public void noteHDraw(int noteHTrav, int[] noteHold, Image dirNote, int noteX, int frameCount){
        if(noteHold[noteHTrav] <= frameCount &&
                ((24+((frameCount - noteHold[noteHTrav])*frameSpeed)-82) <= WINDOW_HEIGHT)){
            dirNote.draw(noteX, 24+((frameCount - noteHold[noteHTrav])*frameSpeed));
        }
        //if the bottom note goes out of the window unpressed then count it as a miss
        if((24+((frameCount - noteHold[noteHTrav])*frameSpeed)+82) <= WINDOW_HEIGHT + 2 &&
                (24+((frameCount - noteHold[noteHTrav])*frameSpeed)+82) >= WINDOW_HEIGHT && normalOrHold != -1){
            noteDistance = arbitValH;
            normalOrHold = 0;
            noteHold[noteHTrav] = placeholderValue;
            recOver++;
        }
    }

    public void update(Input input, ShadowDance game){
        //initializing all the variables locally
        int leftNTrav = 0, leftHTrav = 0, rightNTrav = 0, rightHTrav = 0, upNTrav = 0,
                upHTrav = 0, downNTrav = 0, downHTrav = 0;
        int leftN = game.leftN, leftH = game.leftH, rightN = game.rightN, rightH = game.rightH, upN = game.upN,
                upH = game.upH, downN = game.downN, downH = game.downH;
        FrameCount = game.FrameCount;
        noteDistance = game.noteDistance;
        int leftX = game.leftX;
        int rightX = game.rightX;
        int upX = game.upX;
        int downX = game.downX;
        int[] leftNormal = game.leftNormal;
        int[] rightNormal = game.rightNormal;
        int[] upNormal = game.upNormal;
        int[] downNormal = game.downNormal;
        int[] leftHold = game.leftHold;
        int[] rightHold = game.rightHold;
        int[] upHold = game.upHold;
        int[] downHold = game.downHold;
        //if any key is pressed then check its corresponding lane for notes
        if(input.wasPressed(Keys.LEFT)) {
            notePressed(leftN, leftNTrav, leftH, leftHTrav, leftNormal, leftHold, FrameCount);
        }
        else if(input.wasPressed(Keys.RIGHT)){
            notePressed(rightN, rightNTrav, rightH, rightHTrav, rightNormal, rightHold, FrameCount);
        }
        else if(input.wasPressed(Keys.UP)){
            notePressed(upN, upNTrav, upH, upHTrav, upNormal, upHold, FrameCount);
        }
        else if(input.wasPressed(Keys.DOWN)){
            notePressed(downN, downNTrav, downH, downHTrav, downNormal, downHold, FrameCount);
        }
        else if(input.wasReleased(Keys.LEFT) && normalOrHold == -1){
            noteReleased(leftH, leftHTrav, leftHold, FrameCount);
        }
        else if(input.wasReleased(Keys.RIGHT) && normalOrHold == -1){
            noteReleased(rightH, rightHTrav, rightHold, FrameCount);
        }
        else if(input.wasReleased(Keys.UP) && normalOrHold == -1){
            noteReleased(upH, upHTrav, upHold, FrameCount);
        }
        else if(input.wasReleased(Keys.DOWN) && normalOrHold == -1){
            noteReleased(downH, downHTrav, downHold, FrameCount);
        }
        //while there are still notes remaining in the array check the array at each frame
        while(leftN > leftNTrav){
            noteNDraw(leftNTrav, leftNormal, LEFT_NOTE, leftX, FrameCount);
            leftNTrav++;
        }
        while(rightN > rightNTrav){
            noteNDraw(rightNTrav, rightNormal, RIGHT_NOTE, rightX, FrameCount);
            rightNTrav++;
        }
        while(upN > upNTrav){
            noteNDraw(upNTrav, upNormal, UP_NOTE, upX, FrameCount);
            upNTrav++;
        }
        while(downN > downNTrav){
            noteNDraw(downNTrav, downNormal, DOWN_NOTE, downX, FrameCount);
            downNTrav++;
        }
        while(leftH > leftHTrav){
            noteHDraw(leftHTrav, leftHold, LEFT_NOTE_HOLD, leftX, FrameCount);
            leftHTrav++;
        }
        while(rightH > rightHTrav){
            noteHDraw(rightHTrav, rightHold, RIGHT_NOTE_HOLD, rightX, FrameCount);
            rightHTrav++;
        }
        while(upH > upHTrav){
            noteHDraw(upHTrav, upHold, UP_NOTE_HOLD, upX, FrameCount);
            upHTrav++;
        }
        while(downH > downHTrav){
            noteHDraw(downHTrav, downHold, DOWN_NOTE_HOLD, downX, FrameCount);
            downHTrav++;
        }
    }
}
