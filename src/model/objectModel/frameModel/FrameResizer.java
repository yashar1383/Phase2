package model.objectModel.frameModel;

import constants.Constants;
import model.ModelData;
import model.logics.collision.Collision;
import utils.Math;
import utils.Vector;

import java.util.ArrayList;

public class FrameResizer {

    private FrameModel frameModel;
    ArrayList<FrameModel> frames;

    public FrameResizer(FrameModel frameModel){
        this.frameModel = frameModel;
        synchronized (ModelData.getModels()){
            frames = (ArrayList<FrameModel>) ModelData.getFrames().clone();
        }
    }

    public void resize() {
        frameModel.setUpDownV(
                frameModel.getUpDownV().x + frameModel.getUpDownA().x * Constants.FRAME_ANIMATION_REFRESH_RATE ,
                frameModel.getUpDownV().y + frameModel.getUpDownA().y * Constants.FRAME_ANIMATION_REFRESH_RATE
        );
        frameModel.setLeftRightV(
                frameModel.getLeftRightV().x + frameModel.getLeftRightA().x * Constants.FRAME_ANIMATION_REFRESH_RATE ,
                frameModel.getLeftRightV().y + frameModel.getLeftRightA().y * Constants.FRAME_ANIMATION_REFRESH_RATE
        );
        Vector upDownMoved = new Vector();
        Vector leftRightMoved = new Vector();
        if (frameModel.canTopResize()){
            upDownMoved.setX((2 * frameModel.getUpDownV().x - frameModel.getUpDownA().x * Constants.FRAME_ANIMATION_REFRESH_RATE)
                    * Constants.FRAME_ANIMATION_REFRESH_RATE / 2);
        }
        if (frameModel.canBottomResize()){
            upDownMoved.setY((2 * frameModel.getUpDownV().y - frameModel.getUpDownA().y * Constants.FRAME_ANIMATION_REFRESH_RATE)
                    * Constants.FRAME_ANIMATION_REFRESH_RATE / 2);
        }
        if (frameModel.canLeftResize()){
            leftRightMoved.setX((2 * frameModel.getLeftRightV().x - frameModel.getLeftRightA().x
                    * Constants.FRAME_ANIMATION_REFRESH_RATE) * Constants.FRAME_ANIMATION_REFRESH_RATE / 2);
        }
        if (frameModel.canRightResize()){
            leftRightMoved.setY((2 * frameModel.getLeftRightV().y - frameModel.getLeftRightA().y * Constants.FRAME_ANIMATION_REFRESH_RATE)
                    * Constants.FRAME_ANIMATION_REFRESH_RATE / 2);
        }
        frameModel.setUpDownP(Math.VectorAdd(upDownMoved ,frameModel.getUpDownP()));
        frameModel.setLeftRightP(Math.VectorAdd(leftRightMoved ,frameModel.getLeftRightP()));
        frameModel.revalidate(upDownMoved ,leftRightMoved);

        if (Collision.IsCollidingWithSolidFrames(frameModel ,frames)){
            frameModel.setUpDownP(Math.VectorAdd(
                    frameModel.getUpDownP() ,
                    Math.ScalarInVector(-1 ,upDownMoved))
            );
            frameModel.setLeftRightP(Math.VectorAdd(
                    frameModel.getLeftRightP() ,
                    Math.ScalarInVector(-1 ,leftRightMoved))
            );
            frameModel.revalidate(
                    Math.ScalarInVector(-1 ,upDownMoved) ,
                    Math.ScalarInVector(-1 ,leftRightMoved)
            );
        }

        if (frameModel.getSize().width < Constants.MINIMUM_FRAME_DIMENSION.width) {
            frameModel.setLeftRightP(Math.VectorAdd(
                    frameModel.getLeftRightP() ,
                    Math.ScalarInVector(-1 ,leftRightMoved))
            );
            frameModel.revalidate(
                    new Vector() ,
                    Math.ScalarInVector(-1 ,leftRightMoved)
            );
        }

        if (frameModel.getSize().height < Constants.MINIMUM_FRAME_DIMENSION.height) {
            frameModel.setUpDownP(Math.VectorAdd(
                    frameModel.getUpDownP() ,
                    Math.ScalarInVector(-1 ,upDownMoved))
            );
            frameModel.revalidate(
                    Math.ScalarInVector(-1 ,upDownMoved) ,
                    new Vector()
            );
        }

    }


}
