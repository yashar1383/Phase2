package model.logics;


import constants.Constants;
import model.ModelData;
import model.animations.DashAnimation;
import model.interfaces.ImpactAble;
import model.objectModel.fighters.EpsilonModel;
import utils.Math;
import utils.Vector;
import view.soundEffects.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Impact {
    private final Vector collisionPoint;
    private double distance;
    private int time;
    private boolean sameForce;

    public Impact(Vector collisionPoint ,double distance){
        this.collisionPoint = collisionPoint;
        this.distance = distance;
        time = Constants.DASH_TIME;
    }


    public Impact(Vector collisionPoint ,double distance ,int time){
        this.collisionPoint = collisionPoint;
        this.distance = distance;
        this.time = time;
    }

    public Impact(Vector collisionPoint ,double distance ,int time ,boolean sameForce){
        this.collisionPoint = collisionPoint;
        this.distance = distance;
        this.time = time;
        this.sameForce = sameForce;
    }

    public void MakeImpact(){
        try {
            new Sound(Constants.impactSound).play();
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        double distance;
        for (int i = 0; i < ModelData.getModels().size() ; i++){
            if (ModelData.getModels().get(i) instanceof ImpactAble) {
                Vector direction;
                direction = Math.VectorAdd(Math.ScalarInVector(-1, collisionPoint), ModelData.getModels().get(i).getPosition());
                distance = Math.VectorSize(direction);
                //////////////////todo
                if (distance >= this.distance) {
                    continue;
                }
                //////////////////todo
                if (distance == 0)
                    continue;
                double dashDistance;
                if (sameForce) {
                    dashDistance = this.distance;
                }
                else {
                    dashDistance = this.distance - distance;
                }
                if (!(ModelData.getModels().get(i) instanceof EpsilonModel)) {
                    new DashAnimation(
                            ModelData.getModels().get(i),
                            direction,
                            time,
                            dashDistance,
                            dashDistance / 100 * java.lang.Math.PI,
                            false
                    ).StartAnimation();
                }
                else {
                    if (sameForce) {
                        new DashAnimation(
                                ModelData.getModels().get(i),
                                direction,
                                time,
                                dashDistance,
                                0,
                                false
                        ).StartAnimation();
                    }
                    else {
                        new DashAnimation(
                                ModelData.getModels().get(i),
                                direction,
                                time,
                                100,
                                0,
                                false
                        ).StartAnimation();
                    }
                }
            }
        }
    }

}
