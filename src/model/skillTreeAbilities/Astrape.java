package model.skillTreeAbilities;

import controller.enums.SkillTreeAbilityType;
import data.Constants;
import model.GameState;
import model.ModelData;
import model.objectModel.fighters.EpsilonModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Astrape extends SkillTreeAbility{

    public Astrape(){
        unlockXpCost = 1000;
        type = SkillTreeAbilityType.astrape;
        initTimer();
    }

    private void initTimer() {
        coolDownTimer = new Timer(Constants.SKILL_TREE_ABILITY_TIMER_REFRESH_RATE, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GameState.isPause())
                    return;
                coolDownTimePassed += Constants.SKILL_TREE_ABILITY_TIMER_REFRESH_RATE;
                if (coolDownTimePassed >= inGameCoolDownTime){
                    canCast = true;
                    coolDownTimePassed = 0;
                    coolDownTimer.stop();
                }
            }
        });
    }

    @Override
    protected void cast() {
        canCast = false;
        EpsilonModel epsilon = (EpsilonModel) ModelData.getModels().getFirst();
        epsilon.setEpsilonDamageOnCollision(epsilon.getEpsilonDamageOnCollision() + 2);
        coolDownTimer.start();
    }


}
