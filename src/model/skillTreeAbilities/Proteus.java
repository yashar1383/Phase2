package model.skillTreeAbilities;

import constants.Constants;
import controller.configs.Configs;
import controller.enums.SkillTreeAbilityType;
import model.ModelData;
import model.objectModel.fighters.EpsilonModel;

public class Proteus extends SkillTreeAbility{

    private EpsilonModel epsilonModel;

    public Proteus(){
        isBought = Configs.SkillTreeConfigs.proteusBought;
        unlockXpCost = Constants.PROTEUS_UNLOCK_COST;
        type = SkillTreeAbilityType.proteus;
        initTimer();
        initEpsilon();
    }

    private void initEpsilon() {
        epsilonModel = ModelData.getEpsilon();
    }


    @Override
    protected void cast() {
        canCast = false;
        epsilonModel.addVertex();
        coolDownTimer.start();
    }

    @Override
    public void setUp() {
        super.setUp();
        epsilonModel = ModelData.getEpsilon();
    }
}
