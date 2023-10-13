import bagel.Image;
public class GuardianLaneManager {
    private final static int laneY = 384;
    private final Image SPECIAL_IMAGE;
    private final Image RIGHT_IMAGE;
    private final Image LEFT_IMAGE;
    private final Image GUARDIAN_IMAGE;
    private final int guardianX = 800;
    private final int guardianY = 600;
    public GuardianLaneManager(){
        SPECIAL_IMAGE = new Image("res/laneSpecial.png");
        RIGHT_IMAGE = new Image("res/laneRight.png");
        LEFT_IMAGE = new Image("res/laneLeft.png");
        GUARDIAN_IMAGE = new Image("res/guardian.png");
    }

    public void update(ShadowDance game) {
        //drawing all the images everytime called
        SPECIAL_IMAGE.draw(game.specialX, laneY);
        RIGHT_IMAGE.draw(game.rightX, laneY);
        LEFT_IMAGE.draw(game.leftX, laneY);
        GUARDIAN_IMAGE.draw(guardianX, guardianY);
    }
}
