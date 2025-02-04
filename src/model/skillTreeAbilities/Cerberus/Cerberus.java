package model.skillTreeAbilities.Cerberus;

import controller.configs.Configs;
import controller.enums.ModelType;
import controller.enums.SkillTreeAbilityType;
import controller.manager.Spawner;
import constants.Constants;
import model.ModelData;
import model.objectModel.fighters.EpsilonModel;
import model.skillTreeAbilities.SkillTreeAbility;
import utils.Math;
import utils.Vector;

public class Cerberus extends SkillTreeAbility {

    public Cerberus(){
        isBought = Configs.SkillTreeConfigs.cerberusBought;
        unlockXpCost = Constants.CERBERUS_UNLOCK_COST;
        type = SkillTreeAbilityType.cerberus;
        initTimer();
    }

    @Override
    protected void cast() {
        canCast = false;
        EpsilonModel epsilon = (EpsilonModel) ModelData.getModels().getFirst();

        Vector direction = new Vector(0 ,1);
        direction = Math.VectorWithSize(
                direction,
                Constants.CERBERUS_RADIOS + Constants.EPSILON_DIMENSION.height / 2d
        );
        for (int i = 0; i < 3 ;i++) {
            Spawner.spawnObject(
                    Math.VectorAdd(
                            epsilon.getPosition(),
                            direction
                    ),
                    ModelType.cerberus
            );
            direction = Math.RotateByTheta(direction , new Vector(),java.lang.Math.PI / 3 * 2);
        }
        coolDownTimer.start();
    }
}
