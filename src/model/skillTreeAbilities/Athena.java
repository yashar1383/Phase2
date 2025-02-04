package model.skillTreeAbilities;

import constants.Constants;
import controller.configs.Configs;
import controller.enums.SkillTreeAbilityType;
import controller.manager.GameState;
import model.ModelData;
import model.objectModel.fighters.EpsilonModel;

public class Athena extends SkillTreeAbility{

    public Athena(){
        isBought = Configs.SkillTreeConfigs.athenaBought;
        unlockXpCost = Constants.ATHENA_UNLOCK_COST;
        type = SkillTreeAbilityType.athena;
        initTimer();
    }



    @Override
    protected void cast() {
        canCast = false;
        GameState.setShrinkageVelocity(GameState.getShrinkageVelocity() - 0.2 * GameState.getShrinkageVelocity());
        coolDownTimer.start();
    }

    @Override
    public void setUp() {
        super.setUp();
    }
}
