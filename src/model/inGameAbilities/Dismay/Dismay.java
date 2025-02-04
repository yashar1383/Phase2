package model.inGameAbilities.Dismay;

import controller.enums.InGameAbilityType;
import controller.manager.Spawner;
import controller.manager.loading.SkippedByJson;
import constants.Constants;
import controller.manager.GameState;
import model.ModelData;
import model.inGameAbilities.InGameAbility;
import model.objectModel.fighters.EpsilonModel;
import utils.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dismay extends InGameAbility {

    private int timePassed;
    private EpsilonProtectorModel protectorModel;
    private EpsilonModel epsilon;
    @SkippedByJson
    private Timer timer;

    public Dismay(EpsilonModel epsilon){
        this.epsilon = epsilon;
        type = InGameAbilityType.dismay;
        xpCost = 120;
        initTimer();
        initProtector();
    }

    private void initProtector() {
        protectorModel = new EpsilonProtectorModel(
                epsilon,
                Helper.RandomStringGenerator(Constants.ID_SIZE)
        );
    }

    private void initTimer() {
        timer = new Timer(Constants.IN_GAME_ABILITY_TIMER_REFRESH_RATE, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GameState.isPause())
                    return;
                timePassed += Constants.IN_GAME_ABILITY_TIMER_REFRESH_RATE;
                if (timePassed >= Constants.DISMAY_DURATION){
                    isAvailable = true;
                    isActive = false;
                    timePassed = 0;
                    protectorModel.die();
                    timer.stop();
                }
            }
        });
    }

    @Override
    public void performAbility() {
        Spawner.spawnProtector(protectorModel);
        isActive = true;
        isAvailable = false;
        timer.start();
    }

    @Override
    public void setUp() {
        initTimer();
        this.epsilon =ModelData.getEpsilon();
        protectorModel.setEpsilon(epsilon);
        if (timePassed <= Constants.DISMAY_DURATION && isActive) {
            Spawner.spawnProtector(protectorModel);
            timer.start();
        }
    }
}
