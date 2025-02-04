package model.skillTreeAbilities;

import constants.Constants;
import controller.configs.Configs;
import controller.enums.SkillTreeAbilityType;
import controller.manager.loading.SkippedByJson;
import controller.manager.GameState;
import model.ModelData;
import model.objectModel.fighters.EpsilonModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Aceso extends SkillTreeAbility{
    @SkippedByJson
    private Timer healTimer;
    private EpsilonModel epsilon;
    private int healAmount;

    public Aceso(){
        isBought = Configs.SkillTreeConfigs.acesoBought;
        unlockXpCost = Constants.ACESO_UNLOCK_COST;
        type = SkillTreeAbilityType.aceso;
        initTimer();
        initEpsilon();
        initHealTimer();
    }

    private void initEpsilon() {
        epsilon = ModelData.getEpsilon();
    }

    private void initHealTimer() {
        healTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GameState.isPause())
                    return;
                epsilon.setHP(epsilon.getHP() + healAmount);
                epsilon.checkHP();
            }
        });
    }
    @Override
    protected void cast() {
        canCast = false;
        healAmount++;
        if (healAmount == 1)
            healTimer.start();
        coolDownTimer.start();
    }

    @Override
    protected void stop() {
        super.stop();
        healTimer.stop();
    }

    @Override
    public void setUp() {
        super.setUp();
        initHealTimer();
        initEpsilon();
        if (healAmount != 0) {
            healTimer.start();
        }
    }
}
