import bagel.Image;

public class SpecialLaneManager {
    //initialize all variables
    private final static int laneY = 384;
    private final Image SPECIAL_IMAGE;
    private final Image RIGHT_IMAGE;
    private final Image LEFT_IMAGE;
    private final Image DOWN_IMAGE;
    public SpecialLaneManager(){
        //initializing all the images
        SPECIAL_IMAGE = new Image("res/laneSpecial.png");
        RIGHT_IMAGE = new Image("res/laneRight.png");
        LEFT_IMAGE = new Image("res/laneLeft.png");
        DOWN_IMAGE = new Image("res/laneDown.png");

    }

    public void update(ShadowDance game) {
        //drawing all the images everytime called
        SPECIAL_IMAGE.draw(game.specialX, laneY);
        RIGHT_IMAGE.draw(game.rightX, laneY);
        LEFT_IMAGE.draw(game.leftX, laneY);
        DOWN_IMAGE.draw(game.downX, laneY);
    }
}
