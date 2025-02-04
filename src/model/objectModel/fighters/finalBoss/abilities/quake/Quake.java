package model.objectModel.fighters.finalBoss.abilities.quake;

import controller.Controller;
import constants.Constants;
import model.animations.DashAnimation;
import model.logics.Impact;
import model.objectModel.fighters.finalBoss.Boss;
import model.objectModel.fighters.finalBoss.abilities.Ability;
import model.objectModel.frameModel.FrameModel;
import utils.Math;
import utils.Vector;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Quake extends Ability {

    private Boss boss;
    private FrameModel epsilonFrame;
    private Timer reorderTimer;
    private Timer randomizeTimer;

    public Quake(Boss boss ,FrameModel frameModel) {
        this.boss = boss;
        this.epsilonFrame = frameModel;
        setUpTimers();
    }

    private void setUpTimers() {
        reorderTimer = new Timer(8000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.reorderKeys();
                endAbility();
                reorderTimer.stop();
            }
        });
        randomizeTimer = new Timer(Constants.ABILITY_SETUP_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.randomizeKeys();
                new Impact(
                        boss.getPunch().getPosition(),
                        Constants.SCREEN_SIZE.height,
                        3000,
                        true
                ).MakeImpact();
                randomizeTimer.stop();
            }
        });
    }

    @Override
    protected void setUp() {
        ownHelper(boss.getPunch());
        boss.getPunch().setHovering(true);
    }

    @Override
    protected void unsetUp() {
        disownHelper(boss.getPunch());
        boss.getPunch().setHovering(false);
    }

    @Override
    public void activate(){
        super.activate();
        punchAnimation();
        randomizeTimer.start();
        reorderTimer.start();
    }

    @Override
    public void endAbility() {
        super.endAbility();
    }

    private void punchAnimation() {
        Vector destination = new Vector(
                epsilonFrame.getPosition().x + epsilonFrame.getSize().width / 2d,
                epsilonFrame.getPosition().y + epsilonFrame.getSize().height + Constants.PUNCH_DIMENSION.height / 2d
        );
        Vector direction = Math.VectorAdd(
                destination ,
                Math.ScalarInVector(-1 ,boss.getPunch().getPosition())
        );
        new DashAnimation(
                boss.getPunch(),
                direction,
                Constants.ABILITY_SETUP_DELAY,
                Math.VectorSize(direction),
                0,
                true
        ).StartAnimation();
    }


}
