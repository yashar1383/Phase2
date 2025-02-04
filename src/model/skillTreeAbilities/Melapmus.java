package model.skillTreeAbilities;

import constants.Constants;
import controller.configs.Configs;
import controller.enums.SkillTreeAbilityType;
import model.ModelData;
import model.objectModel.fighters.EpsilonModel;

public class Melapmus extends SkillTreeAbility{

    private EpsilonModel epsilonModel;

    public Melapmus(){
        isBought = Configs.SkillTreeConfigs.melampusBought;
        unlockXpCost = Constants.MELAMPUS_UNLOCK_COST;
        type = SkillTreeAbilityType.melampus;
        initTimer();
        initEpsilon();
    }

    private void initEpsilon() {
        epsilonModel = ModelData.getEpsilon();
    }


    @Override
    protected void cast() {
        canCast = false;
        epsilonModel.setChanceOfSurvival(epsilonModel.getChanceOfSurvival() + 5);
        coolDownTimer.start();
    }

    @Override
    public void setUp() {
        super.setUp();
        initEpsilon();
    }
}
