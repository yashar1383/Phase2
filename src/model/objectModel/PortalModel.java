package model.objectModel;

import constants.Constants;
import controller.Controller;
import controller.enums.ModelType;
import model.interfaces.Ability;
import model.interfaces.Fader;
import model.interfaces.IsCircle;
import model.objectModel.frameModel.FrameModel;
import utils.Vector;

public class PortalModel extends ObjectModel implements IsCircle, Fader ,Ability {
    private double fadeTime;
    private FrameModel epsilonFrame;

    public PortalModel(FrameModel epsilonFrame, String id) {
        this.id = id;
        this.epsilonFrame = epsilonFrame;
        position = new Vector(
                epsilonFrame.getPosition().x + epsilonFrame.getSize().width,
                epsilonFrame.getPosition().y
        );
        this.velocity = new Vector(0 ,0);
        this.acceleration = new Vector(0 ,0);
        HP = 1;
        this.type = ModelType.portal;
    }

    @Override
    public void die() {
        Controller.removeObject(this);
    }

    @Override
    public double getRadios() {
        return Constants.PORTAL_RADIOS;
    }

    @Override
    public Vector getCenter() {
        return position;
    }

    @Override
    public void addTime(double time) {
        fadeTime += time;
    }

    @Override
    public void fadeIf() {
        if (fadeTime >= Constants.PORTAL_FADE_TIME) {
            die();
        }
    }

    @Override
    public void ability() {
        position = new Vector(
                epsilonFrame.getPosition().x + epsilonFrame.getSize().width,
                epsilonFrame.getPosition().y
        );
    }

    @Override
    public boolean hasAbility() {
        return true;
    }
}
