package model.logics;

import constants.Constants;
import model.animations.FrameAnimation;
import model.interfaces.FrameAttacher;
import model.objectModel.ObjectModel;
import model.objectModel.frameModel.FrameLocations;
import model.objectModel.frameModel.FrameModel;
import utils.FrameHelper.FrameCalculationHelper;

import java.util.ArrayList;

public class FrameHit {
    private FrameModel frame;
    private ObjectModel model;
    private ArrayList<ObjectModel> models;

    public FrameHit(FrameModel frame , ObjectModel model ,ArrayList<ObjectModel> models){
        this.frame = frame;
        this.model = model;
        this.models = models;
    }

    public void handle() {
        FrameLocations frameLocation = FrameCalculationHelper.findClosestLocalFrameLocation(
                frame,
                model.getPosition()
        );
        /////////////////conditions.....
        resize(frame ,frameLocation);

    }

    private void resize(FrameModel frame, FrameLocations frameLocation) {
        switch (frameLocation){
            case top :
                new FrameAnimation(
                        frame,
                        Constants.FRAME_BULLET_RESIZE,
                        0,
                        0,
                        0,
                        Constants.FRAME_SHRINKAGE_TIME
                ).StartAnimation();
                for (ObjectModel model1 : models) {
                    if (model1 instanceof FrameAttacher) {
                        FrameLocations frameLocations = ((FrameAttacher) model1).getAttachedLocation();
                        if
                        (
                                frameLocations == FrameLocations.top
                                || frameLocations == FrameLocations.topLeft
                                || frameLocations == FrameLocations.topRight
                        )
                            ((FrameAttacher) model1).damage();
                    }
                }
                break;
            case bottom:
                new FrameAnimation(
                        frame,
                        0,
                        Constants.FRAME_BULLET_RESIZE,
                        0,
                        0,
                        Constants.FRAME_SHRINKAGE_TIME
                ).StartAnimation();
                for (ObjectModel model1 : models) {
                    if (model1 instanceof FrameAttacher) {
                        FrameLocations frameLocations = ((FrameAttacher) model1).getAttachedLocation();
                        if
                        (
                                frameLocations == FrameLocations.bottom
                                        || frameLocations == FrameLocations.bottomLeft
                                        || frameLocations == FrameLocations.bottomRight
                        )
                            ((FrameAttacher) model1).damage();
                    }
                }
                break;
            case right:
                new FrameAnimation(
                        frame,
                        0,
                        0,
                        Constants.FRAME_BULLET_RESIZE,
                        0,
                        Constants.FRAME_SHRINKAGE_TIME
                ).StartAnimation();
                for (ObjectModel model1 : models) {
                    if (model1 instanceof FrameAttacher) {
                        FrameLocations frameLocations = ((FrameAttacher) model1).getAttachedLocation();
                        if
                        (
                                frameLocations == FrameLocations.right
                                        || frameLocations == FrameLocations.topRight
                                        || frameLocations == FrameLocations.bottomRight
                        )
                            ((FrameAttacher) model1).damage();
                    }
                }
                break;
            case left:
                new FrameAnimation(
                        frame,
                        0,
                        0,
                        0,
                        Constants.FRAME_BULLET_RESIZE,
                        Constants.FRAME_SHRINKAGE_TIME
                ).StartAnimation();
                for (ObjectModel model1 : models) {
                    if (model1 instanceof FrameAttacher) {
                        FrameLocations frameLocations = ((FrameAttacher) model1).getAttachedLocation();
                        if
                        (
                                frameLocations == FrameLocations.left
                                        || frameLocations == FrameLocations.topLeft
                                        || frameLocations == FrameLocations.bottomLeft
                        )
                            ((FrameAttacher) model1).damage();
                    }
                }
                break;
        }
    }
}
