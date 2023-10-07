import bagel.Image;

public class LaneManager {
    //declaring all the variables
    private final static int laneY = 384;
    private final Image LEFT_IMAGE;
    private final Image RIGHT_IMAGE;
    private final Image UP_IMAGE;
    private final Image DOWN_IMAGE;
    public LaneManager(){
        //initializing all the images
        LEFT_IMAGE = new Image("res/laneLeft.png");
        RIGHT_IMAGE = new Image("res/laneRight.png");
        UP_IMAGE = new Image("res/laneUp.png");
        DOWN_IMAGE = new Image("res/laneDown.png");

    }

    public void update(ShadowDance game) {
        //drawing all the images everytime called
        LEFT_IMAGE.draw(game.leftX, laneY);
        RIGHT_IMAGE.draw(game.rightX, laneY);
        UP_IMAGE.draw(game.upX, laneY);
        DOWN_IMAGE.draw(game.downX, laneY);
    }

}
