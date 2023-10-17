import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Vector2;
import bagel.Input;
import bagel.Keys;
import java.util.Random;
public class GuardianNoteManager {
    private final NoteManager sNoteManager;
    // define all the variables
    private final static int laneCenter = 657, guardianX = 800, guardianY = 600;
    private final static int placeholderValue = -768;
    private final static int arbitVal = 200;
    private final static int arbitValH = 250;
    private final static int WINDOW_HEIGHT = 768;
    private final static int WINDOW_WIDTH = 1024;
    private final Image LEFT_NOTE;
    private final Image RIGHT_NOTE;
    private final Image BOMB_NOTE;
    private final Image SLOW_NOTE;
    private final Image SPEED_NOTE;
    private final Image DOUBLE_NOTE;
    private final Image LEFT_NOTE_HOLD;
    private final Image RIGHT_NOTE_HOLD;
    private final Image ENEMY_IMAGE;
    private final Image ARROW_IMAGE;
    private int[] enemyX;
    private int[] enemyY;
    private int eX = 0;
    private int eY = 0;
    private int FrameCount;
    public int lefthc = 0;
    public int righthc = 0;
    public int recOver = 0, noteDistance, frameSpeed = 2, chosenS = 0, minFrame = 1000000, chosenSD = 0, frameAct = -30;
    private int minX, minY, projT = 0, eA = 0;
    private int[] enemyAdder;
    private double enemyDistance = 200;
    private Vector2 position, target;
    double minDist = 100000000;
    int mindist;
    Vector2 displacement;
    double distance;
    double movement;
    Random random = new Random();

    public GuardianNoteManager(){
        sNoteManager = new NoteManager();
        enemyX = new int[100];
        enemyY = new int[100];
        enemyAdder = new int[100];
        //loading all the images
        LEFT_NOTE = new Image("res/noteLeft.png");
        RIGHT_NOTE = new Image("res/noteRight.png");
        BOMB_NOTE = new Image("res/noteBomb.png");
        SLOW_NOTE = new Image("res/noteSlowDown.png");
        SPEED_NOTE = new Image("res/noteSpeedUp.png");
        DOUBLE_NOTE = new Image("res/note2x.png");
        LEFT_NOTE_HOLD = new Image("res/holdNoteLeft.png");
        RIGHT_NOTE_HOLD = new Image("res/holdNoteRight.png");
        ENEMY_IMAGE = new Image("res/enemy.png");
        ARROW_IMAGE = new Image("res/arrow.png");
        enemyX[eX] = random.nextInt(800)+100;
        enemyY[eY] = random.nextInt(400)+100;
        enemyAdder[eA] = 1;
        eX++;
        eY++;
        eA++;
    }

    public void guardianSpecialNotePressed(int bombN, int slowN, int speedN, int doubleN, int sBombTrav, int slowTrav,
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
        if (chosenSD < 0){
            chosenSD *= -1;
        }
        //if the note is within 50 frames then consider it activated
        if (chosenSD <= 50) {
            if (chosenS == 1) {
                bombS[sBombTrav] = placeholderValue;
                guardianNoteDestroy(bombS, sBombTrav, bombN, frameCount);
                guardianNoteDestroy(doubleScoreS, doubleTrav, doubleN, frameCount);
                guardianNoteDestroy(speedUpS, speedTrav, speedN, frameCount);
                guardianNoteDestroy(slowDownS, slowTrav, slowN, frameCount);
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

    public void guardianBombPressed(int noteNC, int noteNCTrav, int[] noteNormal, int laneNC, int laneNCTrav,
                            int[] laneNormal, int noteHC, int noteHCTrav, int[] noteHold, int frameCount){
        //check if the bomb is on screen and then it is considered activated
        while(noteNC > noteNCTrav) {
            if (noteNormal[noteNCTrav] <= frameCount &&
                    ((100 + ((frameCount - noteNormal[noteNCTrav]) * frameSpeed)) <= WINDOW_HEIGHT)) {
                if((laneCenter - (100 + ((frameCount - noteNormal[noteNCTrav]) * frameSpeed))) <= 50 &&
                        (laneCenter - (100 + ((frameCount - noteNormal[noteNCTrav]) * frameSpeed))) >= -50){
                    chosenS = 1;
                    noteNormal[noteNCTrav] = placeholderValue;
                    guardianNoteDestroy(laneNormal, laneNCTrav, laneNC, frameCount);
                    guardianNoteDestroy(noteHold, noteHCTrav, noteHC, frameCount);
                    recOver++;
                }
            }
            noteNCTrav++;
        }
    }

    public void guardianBombDraw(int noteNTrav, int[] noteNormal, Image dirNote, int noteX, int frameCount) {
        //if the frame is in the window then print the image of the note
        if (noteNormal[noteNTrav] <= frameCount &&
                ((100 + ((frameCount - noteNormal[noteNTrav]) * frameSpeed)) <= WINDOW_HEIGHT)) {
            dirNote.draw(noteX, 100 + ((frameCount - noteNormal[noteNTrav]) * frameSpeed));
        }
        //if the note goes out of the window height then remove the note and count it as a miss
        if((100+((frameCount - noteNormal[noteNTrav])*frameSpeed) <= WINDOW_HEIGHT + 2) &&
                (100+((frameCount - noteNormal[noteNTrav])*frameSpeed) >= WINDOW_HEIGHT)){
            noteNormal[noteNTrav] = placeholderValue;
            recOver++;
        }
    }

    public void guardianNoteDestroy(int[] notesAll, int nTrav, int nMax, int frameCount){
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

    public void shootProj(int[] enemyx, int[] enemyy, int eTrav){
        int eT = 0;
        minDist = 100000000;
        double dist;
        int HD;
        int WD;
        //find the closest enemy to the guardian
        while(eT < eTrav){
            WD = guardianX - enemyx[eT];
            HD = guardianY - enemyy[eT];
            if(WD < 0){
                WD *= -1;
            }
            if(HD < 0){
                HD *= -1;
            }
            dist = Math.sqrt((HD*HD) + (WD+WD));
            if(dist < minDist){
                minDist = dist;
                mindist = eT;
                minX = enemyx[eT];
                minY = enemyy[eT];
            }
            eT++;
        }
        position = new Vector2(guardianX, guardianY);
        target = new Vector2(minX, minY);
    }

    public void update(Input input, ShadowDance game){
        //initializing all the variables locally
        int leftNTrav = 0, leftHTrav = 0, rightNTrav = 0, rightHTrav = 0,
                sBombTrav = 0, slowTrav = 0, speedTrav = 0, doubleTrav = 0, leftBTrav = 0, rightBTrav = 0;
        int leftN = game.leftN, leftH = game.leftH, rightN = game.rightN, rightH = game.rightH,
                bombN = game.sBomb, slowN = game.sSlow, speedN = game.sSpeed,
                doubleN = game.sDouble, leftB = game.leftB, rightB = game.rightB;
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
        int[] leftNormal = game.leftNormal;
        int[] rightNormal = game.rightNormal;
        int[] doubleScoreS= game.doubleScoreS;
        int[] bombS= game.bombS;
        int[] slowDownS= game.slowDownS;
        int[] speedUpS= game.speedUpS;
        int[] leftHold = game.leftHold;
        int[] rightHold = game.rightHold;
        int[] leftBomb = game.leftBomb;
        int[] rightBomb = game.rightBomb;
        int HeightD = 0;
        int WidthD = 0;
        int eXTrav = 0;
        //every 600 frames create a new enemy
        if(FrameCount%600 == 0){
            enemyX[eX] = random.nextInt(800)+100;
            enemyY[eY] = random.nextInt(400)+100;
            enemyAdder[eA] = 1;
            eX++;
            eY++;
            eA++;
        }
        //everytime an enemy hits the sides of the window it starts moving the other way
        while(eXTrav < eX) {
            if (enemyX[eXTrav] == WINDOW_WIDTH) {
                enemyAdder[eXTrav] = -1;
            } else if (enemyX[eXTrav] == 0) {
                enemyAdder[eXTrav] = 1;
            }

            ENEMY_IMAGE.draw(enemyX[eXTrav], enemyY[eXTrav]);
            enemyX[eXTrav] += enemyAdder[eXTrav];
            eXTrav++;
        }
        eXTrav = 0;


        //if any key is pressed then check its corresponding lane for notes
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
                if(sNoteManager.normalOrHold == -1){
                    lefthc = 1;
                }
                noteDistance = sNoteManager.noteDistance;
                if(noteDistance != 0){
                    recOver++;
                }
            }
            else if(choiceVal == 2) {
                guardianBombPressed(leftB, leftBTrav, leftBomb, leftN, leftNTrav, leftNormal,
                        leftH, leftHTrav, leftHold, FrameCount);
            }
        }
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
                if(sNoteManager.normalOrHold == -1){
                    righthc = 1;
                }
                noteDistance = sNoteManager.noteDistance;
                if(noteDistance != 0){
                    recOver++;
                }
            }
            else if(choiceVal == 2) {
                guardianBombPressed(rightB, rightBTrav, rightBomb, rightN, rightNTrav, rightNormal,
                        rightH, rightHTrav, rightHold, FrameCount);
            }
        }
        //if space is pressed then check for special notes
        if(input.wasPressed(Keys.SPACE)){
            guardianSpecialNotePressed(bombN, slowN, speedN, doubleN, sBombTrav, slowTrav, speedTrav, doubleTrav,
                    doubleScoreS, bombS, slowDownS, speedUpS, FrameCount);
            if(chosenS == 2 || chosenS == 3){
                game.finalScore += 15;
            }
        }
        //if left shift is pressed then shoot an arrow
        if(input.wasPressed(Keys.LEFT_SHIFT)){
            shootProj(enemyX, enemyY, eX);
            projT = 1;
        }
        if(projT == 1){
            //check the angle and the velocity of the arrow
            displacement = target.sub(position);
            distance = displacement.length();
            movement = 6;
            ARROW_IMAGE.draw(position.x, position.y,
                    new DrawOptions().setRotation(Math.atan2((target.y - position.y), (target.x - position.x))));
            if(distance <= movement){
                position = target;
                if(minX - enemyX[mindist] <= 62){
                    enemyX[mindist] = placeholderValue;
                    enemyY[mindist] = placeholderValue;
                }
                projT = 0;
            }
            else{
                displacement = displacement.normalised();
                position = position.add(displacement.mul(movement));
            }
        }
        //if keys are released then check if the note was a hold note
        if(input.wasReleased(Keys.LEFT) && normalOrHold == -1){
            sNoteManager.noteReleased(leftH, leftHTrav, leftHold, FrameCount);
            noteDistance = sNoteManager.noteDistance;
            if(righthc == 1){
                sNoteManager.normalOrHold = -1;
                righthc = 0;
            }
        }
        if(input.wasReleased(Keys.RIGHT) && normalOrHold == -1){
            sNoteManager.noteReleased(rightH, rightHTrav, rightHold, FrameCount);
            noteDistance = sNoteManager.noteDistance;
            if(lefthc == 1){
                sNoteManager.normalOrHold = -1;
                lefthc = 0;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(leftN > leftNTrav){
            sNoteManager.noteNDraw(leftNTrav, leftNormal, LEFT_NOTE, leftX, FrameCount);
            while(eXTrav < eX) {
                HeightD = (FrameCount - leftNormal[leftNTrav])*(sNoteManager.frameSpeed) + 100 - enemyY[eXTrav];
                WidthD = leftX - enemyX[eXTrav];
                if (HeightD < 0) {
                    HeightD *= -1;
                }
                if (WidthD < 0) {
                    WidthD *= -1;
                }
                enemyDistance = Math.sqrt((HeightD * HeightD) + (WidthD * WidthD));
                if(enemyDistance < 105){
                    leftNormal[leftNTrav] = placeholderValue;
                    recOver++;
                }
                eXTrav++;
            }
            eXTrav = 0;
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
            while(eXTrav < eX) {
                HeightD = (FrameCount - rightNormal[rightNTrav])*(sNoteManager.frameSpeed) + 100 - enemyY[eXTrav];
                WidthD = rightX - enemyX[eXTrav];
                if (HeightD < 0) {
                    HeightD *= -1;
                }
                if (WidthD < 0) {
                    WidthD *= -1;
                }
                enemyDistance = Math.sqrt((HeightD * HeightD) + (WidthD * WidthD));
                if(enemyDistance < 105){
                    rightNormal[rightNTrav] = placeholderValue;
                    recOver++;
                }
                eXTrav++;
            }
            eXTrav = 0;
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
        while(leftH > leftHTrav){
            sNoteManager.noteHDraw(leftHTrav, leftHold, LEFT_NOTE_HOLD, leftX, FrameCount);
            while(eXTrav < eX) {
                HeightD = (FrameCount - leftHold[leftHTrav])*(sNoteManager.frameSpeed) + 100 - enemyY[eXTrav];
                WidthD = leftX - enemyX[eXTrav];
                if (HeightD < 0) {
                    HeightD *= -1;
                }
                if (WidthD < 0) {
                    WidthD *= -1;
                }
                enemyDistance = Math.sqrt((HeightD * HeightD) + (WidthD * WidthD));
                if (enemyDistance < 105) {
                    leftHold[leftHTrav] = placeholderValue;
                    recOver++;
                }
                eXTrav++;
            }
            eXTrav = 0;
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
            while(eXTrav < eX) {
                HeightD = (FrameCount - rightHold[rightHTrav])*(sNoteManager.frameSpeed) + 100 - enemyY[eXTrav];
                WidthD = rightX - enemyX[eXTrav];
                if (HeightD < 0) {
                    HeightD *= -1;
                }
                if (WidthD < 0) {
                    WidthD *= -1;
                }
                enemyDistance = Math.sqrt((HeightD * HeightD) + (WidthD * WidthD));
                if (enemyDistance < 105) {
                    rightHold[rightHTrav] = placeholderValue;
                    recOver++;
                }
                eXTrav++;
            }
            eXTrav = 0;
            rightHTrav++;
            if(sNoteManager.noteDistance == arbitValH){
                noteDistance = sNoteManager.noteDistance;
                sNoteManager.noteDistance = 0;
                recOver++;
            }
        }
        //while there are still notes remaining in the array check the array at each frame
        while(leftB > leftBTrav){
            guardianBombDraw(leftBTrav, leftBomb, BOMB_NOTE, leftX, FrameCount);
            leftBTrav++;
        }
        //while there are still notes remaining in the array check the array at each frame
        while(rightB > rightBTrav){
            guardianBombDraw(rightBTrav, rightBomb, BOMB_NOTE, rightX, FrameCount);
            rightBTrav++;
        }
        //print the special messages
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
