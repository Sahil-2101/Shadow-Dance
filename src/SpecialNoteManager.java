import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class SpecialNoteManager {
    private final NoteManager sNoteManager;
    // define all the variables
    private final static int laneCenter = 657;
    private final static int placeholderValue = -768;
    private final static int arbitVal = 200;
    private final static int arbitValH = 250;
    private final static int WINDOW_HEIGHT = 768;
    private final Image LEFT_NOTE;
    private final Image RIGHT_NOTE;
    private final Image BOMB_NOTE;
    private final Image SLOW_NOTE;
    private final Image SPEED_NOTE;
    private final Image DOUBLE_NOTE;
    private final Image DOWN_NOTE;
    private final Image LEFT_NOTE_HOLD;
    private final Image RIGHT_NOTE_HOLD;
    private final Image DOWN_NOTE_HOLD;
    private int FrameCount;
    public int recOver = 0, noteDistance, frameSpeed = 2, chosenS = 0, minFrame = 1000000, chosenSD = 0, frameAct = -30;
    public SpecialNoteManager(){
        sNoteManager = new NoteManager();
        //loading all the images
        LEFT_NOTE = new Image("res/noteLeft.png");
        RIGHT_NOTE = new Image("res/noteRight.png");
        BOMB_NOTE = new Image("res/noteBomb.png");
        SLOW_NOTE = new Image("res/noteSlowDown.png");
        SPEED_NOTE = new Image("res/noteSpeedUp.png");
        DOUBLE_NOTE = new Image("res/note2x.png");
        DOWN_NOTE = new Image("res/noteDown.png");
        LEFT_NOTE_HOLD = new Image("res/holdNoteLeft.png");
        RIGHT_NOTE_HOLD = new Image("res/holdNoteRight.png");
        DOWN_NOTE_HOLD = new Image("res/holdNoteDown.png");
    }

    public void specialNotePressed(int bombN, int slowN, int speedN, int doubleN, int sBombTrav, int slowTrav,
                                   int speedTrav, int doubleTrav, int[] doubleScoreS, int[] bombS,
                                   int[] slowDownS, int[] speedUpS, int frameCount){
        //checking all the bomb notes
        while(sBombTrav < bombN){
            if(bombS[sBombTrav] <= frameCount &&
                    ((100+((frameCount - bombS[sBombTrav])*frameSpeed)) <= WINDOW_HEIGHT)){
                minFrame = bombS[sBombTrav];
                chosenS = 1;
                chosenSD = laneCenter - (100+((frameCount - bombS[sBombTrav])*frameSpeed));
                break;
            }
            sBombTrav++;
        }
        //checking all the slow notes
        while(slowTrav < slowN){
            if(slowDownS[slowTrav] <= frameCount &&
                    ((100+((frameCount - slowDownS[slowTrav])*frameSpeed)) <= WINDOW_HEIGHT)){
                if(minFrame > slowDownS[slowTrav]){
                    minFrame = slowDownS[slowTrav];
                    chosenS = 2;
                    chosenSD = laneCenter - (100+((frameCount - slowDownS[slowTrav])*frameSpeed));
                }
                break;
            }
            slowTrav++;
        }
        //checking all the speed notes
        while(speedTrav < speedN){
            if(speedUpS[speedTrav] <= frameCount &&
                    ((100+((frameCount - speedUpS[speedTrav])*frameSpeed)) <= WINDOW_HEIGHT)){
                if(minFrame > speedUpS[speedTrav]){
                    minFrame = speedUpS[speedTrav];
                    chosenS = 3;
                    chosenSD = laneCenter - (100+((frameCount - speedUpS[speedTrav])*frameSpeed));
                }
                break;
            }
            speedTrav++;
        }
        //checking all the double notes
        while(doubleTrav < doubleN){
            if(doubleScoreS[doubleTrav] <= frameCount &&
                    ((100+((frameCount - doubleScoreS[doubleTrav])*frameSpeed)) <= WINDOW_HEIGHT)){
                if(minFrame > doubleScoreS[doubleTrav]){
                    minFrame = doubleScoreS[doubleTrav];
                    chosenS = 4;
                    chosenSD = laneCenter - (100+((frameCount - doubleScoreS[doubleTrav])*frameSpeed));
                }
                break;
            }
            doubleTrav++;
        }
        //if chosenSD is negative then turn it into positive
        if (chosenSD < 0){
            chosenSD *= -1;
        }
        //if and only if the distance is less than 50 then the note is activated
        if (chosenSD <= 50) {
            if (chosenS == 1) {
                bombS[sBombTrav] = placeholderValue;
                noteDestroy(bombS, sBombTrav, bombN, frameCount);
                noteDestroy(doubleScoreS, doubleTrav, doubleN, frameCount);
                noteDestroy(speedUpS, speedTrav, speedN, frameCount);
                noteDestroy(slowDownS, slowTrav, slowN, frameCount);
                frameAct = frameCount;
                recOver++;
            } else if (chosenS == 2) {
                slowDownS[slowTrav] = placeholderValue;
                sNoteManager.frameSpeed -= 1;
                frameSpeed -= 1;
                frameAct = frameCount;
                recOver++;
            } else if (chosenS == 3) {
                speedUpS[speedTrav] = placeholderValue;
                sNoteManager.frameSpeed += 1;
                frameSpeed += 1;
                frameAct = frameCount;
                recOver++;
            } else if (chosenS == 4) {
                doubleScoreS[doubleTrav] = placeholderValue;
                frameAct = frameCount;
                recOver++;
            }
        }
    }

    public void bombPressed(int noteNC, int noteNCTrav, int[] noteNormal, int laneNC, int laneNCTrav,
                            int[] laneNormal, int noteHC, int noteHCTrav, int[] noteHold, int frameCount){
        //check if the bomb is on screen and then it is considered activated
        while(noteNC > noteNCTrav) {
            if (noteNormal[noteNCTrav] <= frameCount &&
                    ((100 + ((frameCount - noteNormal[noteNCTrav]) * frameSpeed)) <= WINDOW_HEIGHT)) {
                if((laneCenter - (100 + ((frameCount - noteNormal[noteNCTrav]) * frameSpeed))) <= 50 &&
                        (laneCenter - (100 + ((frameCount - noteNormal[noteNCTrav]) * frameSpeed))) >= -50){
                    chosenS = 1;
                    noteNormal[noteNCTrav] = placeholderValue;
                    noteDestroy(laneNormal, laneNCTrav, laneNC, frameCount);
                    noteDestroy(noteHold, noteHCTrav, noteHC, frameCount);
                    recOver++;
                }
            }
            noteNCTrav++;
        }
    }

    public void bombDraw(int noteNTrav, int[] noteNormal, Image dirNote, int noteX, int frameCount) {
        //if the frame is in the window then print the image of the note
        if (noteNormal[noteNTrav] <= frameCount &&
                ((100 + ((frameCount - noteNormal[noteNTrav]) * frameSpeed)) <= WINDOW_HEIGHT)) {
            dirNote.draw(noteX, 100 + ((frameCount - noteNormal[noteNTrav]) * frameSpeed));
        }
        //if the note goes out of the window height then remove the note
        if((100+((frameCount - noteNormal[noteNTrav])*frameSpeed) <= WINDOW_HEIGHT + 2) &&
                (100+((frameCount - noteNormal[noteNTrav])*frameSpeed) >= WINDOW_HEIGHT)){
            noteNormal[noteNTrav] = placeholderValue;
            recOver++;
        }
    }

    public void noteDestroy(int[] notesAll, int nTrav, int nMax, int frameCount){
        //all the notes present in the window are cleared if bomb is pressed
        while(nMax > nTrav){
            if(notesAll[nTrav] <= frameCount &&
                    ((100+((frameCount - notesAll[nTrav])*frameSpeed)) <= WINDOW_HEIGHT)){
                notesAll[nTrav] = placeholderValue;
                recOver++;
            }
            nTrav++;
        }
    }

    public void update(Input input, ShadowDance game){
        //initializing all the variables locally
        int leftNTrav = 0, leftHTrav = 0, rightNTrav = 0, rightHTrav = 0, downNTrav = 0,
                downHTrav = 0, sBombTrav = 0, slowTrav = 0, speedTrav = 0, doubleTrav = 0, leftBTrav = 0,
                rightBTrav = 0, downBTrav = 0;
        int leftN = game.leftN, leftH = game.leftH, rightN = game.rightN, rightH = game.rightH,
                downN = game.downN, downH = game.downH, bombN = game.sBomb, slowN = game.sSlow, speedN = game.sSpeed,
                doubleN = game.sDouble, leftB = game.leftB, rightB = game.rightB, downB = game.downB;
        FrameCount = game.FrameCount;
        noteDistance = game.noteDistance;
        minFrame = 1000000;
        chosenSD = 100;
        int minVal = 1000000;
        int choiceVal = 0;
        int normalOrHold = sNoteManager.normalOrHold;
        int leftX = game.leftX;
        int rightX = game.rightX;
        int specialX = game.specialX;
        int downX = game.downX;
        int[] leftNormal = game.leftNormal;
        int[] rightNormal = game.rightNormal;
        int[] doubleScoreS= game.doubleScoreS;
        int[] bombS= game.bombS;
        int[] slowDownS= game.slowDownS;
        int[] speedUpS= game.speedUpS;
        int[] downNormal = game.downNormal;
        int[] leftHold = game.leftHold;
        int[] rightHold = game.rightHold;
        int[] downHold = game.downHold;
        int[] leftBomb = game.leftBomb;
        int[] rightBomb = game.rightBomb;
        int[] downBomb = game.downBomb;

        //if left key is pressed then check its corresponding lane for notes and check if the note is normal or bomb
        if(input.wasPressed(Keys.LEFT)) {
            while(leftNTrav < leftN){
                if(leftNormal[leftNTrav] >= 0 && leftNormal[leftNTrav] < minVal){
                    minVal = leftNormal[leftNTrav];
                    choiceVal = 1;
                }
                leftNTrav++;
            }
            while(leftBTrav < leftB){
                if(leftBomb[leftBTrav] >= 0 && leftBomb[leftBTrav] < minVal){
                    minVal = leftBomb[leftBTrav];
                    choiceVal = 2;
                }
                leftBTrav++;
            }
            leftBTrav = 0;
            leftNTrav = 0;
            minVal = 1000000;
            if(choiceVal == 1) {
                sNoteManager.notePressed(leftN, leftNTrav, leftH, leftHTrav, leftNormal, leftHold,
                        FrameCount);
                noteDistance = sNoteManager.noteDistance;
                if(noteDistance != 0){
                    recOver++;
                }
            }
            else if(choiceVal == 2) {
                bombPressed(leftB, leftBTrav, leftBomb, leftN, leftNTrav, leftNormal,
                        leftH, leftHTrav, leftHold, FrameCount);
            }
        }
        //if right key is pressed then check its corresponding lane for notes and check if the note is normal or bomb
        if(input.wasPressed(Keys.RIGHT)){
            while(rightNTrav < rightN){
                if(rightNormal[rightNTrav] >= 0 && rightNormal[rightNTrav] < minVal){
                    minVal = rightNormal[rightNTrav];
                    choiceVal = 1;
                }
                rightNTrav++;
            }
            while(rightBTrav < rightB){
                if(rightBomb[rightBTrav] >= 0 && rightBomb[rightBTrav] < minVal){
                    minVal = rightBomb[rightBTrav];
                    choiceVal = 2;
                }
                rightBTrav++;
            }
            rightBTrav = 0;
            rightNTrav = 0;
            minVal = 1000000;
            if(choiceVal == 1) {
                sNoteManager.notePressed(rightN, rightNTrav, rightH, rightHTrav, rightNormal, rightHold,
                        FrameCount);
                noteDistance = sNoteManager.noteDistance;
                if(noteDistance != 0){
                    recOver++;
                }
            }
            else if(choiceVal == 2) {
                bombPressed(rightB, rightBTrav, rightBomb, rightN, rightNTrav, rightNormal,
                        rightH, rightHTrav, rightHold, FrameCount);
            }
        }
        //if space key is pressed then check its corresponding lane for notes
        if(input.wasPressed(Keys.SPACE)){
            specialNotePressed(bombN, slowN, speedN, doubleN, sBombTrav, slowTrav, speedTrav, doubleTrav,
                    doubleScoreS, bombS, slowDownS, speedUpS, FrameCount);
            if(chosenS == 2 || chosenS == 3){
                game.finalScore += 15;
            }
        }
        //if down key is pressed then check its corresponding lane for notes and check if the note is normal or bomb
        if(input.wasPressed(Keys.DOWN)){
            while(downNTrav < downN){
                if(downNormal[downNTrav] >= 0 && downNormal[downNTrav] < minVal){
                    minVal = downNormal[downNTrav];
                    choiceVal = 1;
                }
                downNTrav++;
            }
            while(downBTrav < downB){
                if(downBomb[downBTrav] >= 0 && downBomb[downBTrav] < minVal){
                    minVal = downBomb[downBTrav];
                    choiceVal = 2;
                }
                downBTrav++;
            }
            downBTrav = 0;
            downNTrav = 0;
            minVal = 1000000;
            if(choiceVal == 1) {
                sNoteManager.notePressed(downN, downNTrav, downH, downHTrav, downNormal, downHold,
                        FrameCount);
                noteDistance = sNoteManager.noteDistance;
                if(noteDistance != 0){
                    recOver++;
                }
            }
            else if(choiceVal == 2) {
                bombPressed(downB, downBTrav, downBomb, downN, downNTrav, downNormal,
                        downH, downHTrav, downHold, FrameCount);
            }
        }
        //if left key is released then check if the key pressed was a hold note and count score accordingly
        if(input.wasReleased(Keys.LEFT) && normalOrHold == -1){
            sNoteManager.noteReleased(leftH, leftHTrav, leftHold, FrameCount);
            noteDistance = sNoteManager.noteDistance;
        }
        //if right key is released then check if the key pressed was a hold note and count score accordingly
        if(input.wasReleased(Keys.RIGHT) && normalOrHold == -1){
            sNoteManager.noteReleased(rightH, rightHTrav, rightHold, FrameCount);
            noteDistance = sNoteManager.noteDistance;
        }
        //if down key is released then check if the key pressed was a hold note and count score accordingly
        if(input.wasReleased(Keys.DOWN) && normalOrHold == -1){
            sNoteManager.noteReleased(downH, downHTrav, downHold, FrameCount);
            noteDistance = sNoteManager.noteDistance;
        }
        //while there are still notes remaining in the array check the array at each frame
        while(leftN > leftNTrav){
            sNoteManager.noteNDraw(leftNTrav, leftNormal, LEFT_NOTE, leftX, FrameCount);
            leftNTrav++;
            if(sNoteManager.noteDistance == arbitVal){
                noteDistance = sNoteManager.noteDistance;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(rightN > rightNTrav){
            sNoteManager.noteNDraw(rightNTrav, rightNormal, RIGHT_NOTE, rightX, FrameCount);
            rightNTrav++;
            if(sNoteManager.noteDistance == arbitVal){
                noteDistance = sNoteManager.noteDistance;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(bombN > sBombTrav){
            sNoteManager.noteNDraw(sBombTrav, bombS, BOMB_NOTE, specialX, FrameCount);
            sBombTrav++;
            if(sNoteManager.noteDistance == arbitVal){
                noteDistance = 0;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(speedN > speedTrav){
            sNoteManager.noteNDraw(speedTrav, speedUpS, SPEED_NOTE, specialX, FrameCount);
            speedTrav++;
            if(sNoteManager.noteDistance == arbitVal){
                noteDistance = 0;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(slowN > slowTrav){
            sNoteManager.noteNDraw(slowTrav, slowDownS, SLOW_NOTE, specialX, FrameCount);
            slowTrav++;
            if(sNoteManager.noteDistance == arbitVal){
                noteDistance = 0;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(doubleN > doubleTrav){
            sNoteManager.noteNDraw(doubleTrav, doubleScoreS, DOUBLE_NOTE, specialX, FrameCount);
            doubleTrav++;
            if(sNoteManager.noteDistance == arbitVal){
                noteDistance = 0;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(downN > downNTrav){
            sNoteManager.noteNDraw(downNTrav, downNormal, DOWN_NOTE, downX, FrameCount);
            downNTrav++;
            if(sNoteManager.noteDistance == arbitVal){
                noteDistance = sNoteManager.noteDistance;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(leftH > leftHTrav){
            sNoteManager.noteHDraw(leftHTrav, leftHold, LEFT_NOTE_HOLD, leftX, FrameCount);
            leftHTrav++;
            if(sNoteManager.noteDistance == arbitValH){
                noteDistance = sNoteManager.noteDistance;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(rightH > rightHTrav){
            sNoteManager.noteHDraw(rightHTrav, rightHold, RIGHT_NOTE_HOLD, rightX, FrameCount);
            rightHTrav++;
            if(sNoteManager.noteDistance == arbitValH){
                noteDistance = sNoteManager.noteDistance;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(downH > downHTrav){
            sNoteManager.noteHDraw(downHTrav, downHold, DOWN_NOTE_HOLD, downX, FrameCount);
            downHTrav++;
            if(sNoteManager.noteDistance == arbitValH){
                noteDistance = sNoteManager.noteDistance;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(leftB > leftBTrav){
            bombDraw(leftBTrav, leftBomb, BOMB_NOTE, leftX, FrameCount);
            leftBTrav++;
        }
        //while there are still notes remaining in the array check the array at each frame
        while(rightB > rightBTrav){
            bombDraw(rightBTrav, rightBomb, BOMB_NOTE, rightX, FrameCount);
            rightBTrav++;
        }

        //while there are still notes remaining in the array check the array at each frame
        while(downB > downBTrav){
            bombDraw(downBTrav, downBomb, BOMB_NOTE, downX, FrameCount);
            downBTrav++;
        }
        //print special messages
        if (FrameCount <= frameAct + 30) {
            if (chosenS == 1) {
                game.successFont.drawString("LANE CLEAR", 400, 250);
            } else if (chosenS == 2) {
                game.successFont.drawString("SLOW DOWN", 400, 250);
            } else if (chosenS == 3) {
                game.successFont.drawString("SPEED UP", 400, 250);
            } else if (chosenS == 4) {
                game.freezeFrame = FrameCount;
                game.successFont.drawString("DOUBLE SCORE", 400, 250);
            }
        }
        else{
            chosenS = 0;
        }

    }
}