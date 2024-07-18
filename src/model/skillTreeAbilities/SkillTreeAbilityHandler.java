package model.skillTreeAbilities;

import controller.enums.SkillTreeAbilityType;
import controller.manager.GameState;
import model.ModelData;
import model.skillTreeAbilities.Cerberus.Cerberus;

import java.util.ArrayList;

public class SkillTreeAbilityHandler {

    public synchronized static void initAbilities(){
        ArrayList<SkillTreeAbility> abilities = ModelData.getSkillTreeAbilities();
        abilities.add(new Ares());
        abilities.add(new Astrape());
        abilities.add(new Cerberus());
        abilities.add(new Aceso());
        abilities.add(new Melapmus());
        abilities.add(new Chiron());
        abilities.add(new Proteus());
        abilities.add(new Empusa());
        abilities.add(new Dolus());
        ModelData.setSkillTreeAbilities(abilities);
    }

    public static SkillTreeAbility getAbility(SkillTreeAbilityType type) {
        ArrayList<SkillTreeAbility> abilities = ModelData.getSkillTreeAbilities();
        for (SkillTreeAbility ability : abilities){
            if (ability.getType().equals(type))
                return ability;
        }
        return null;
    }

    public static void activateSkillTreeAbility(SkillTreeAbilityType type) {
        SkillTreeAbility skillTreeAbility = getAbility(type);
        skillTreeAbility.cast();
    }

    public static void addSkillTree(SkillTreeAbility skillTreeAbility) {
        ArrayList<SkillTreeAbility> skillTreeAbilities = ModelData.getSkillTreeAbilities();
        skillTreeAbilities.add(skillTreeAbility);
        ModelData.setSkillTreeAbilities(skillTreeAbilities);
    }

    public static void buyIf(SkillTreeAbilityType skillTreeAbilityType) {
        SkillTreeAbility skillTreeAbility = getAbility(skillTreeAbilityType);
        if (GameState.getXp() > skillTreeAbility.getUnlockXpCost()) {
            GameState.setXp(GameState.getXp() - skillTreeAbility.getUnlockXpCost());
            skillTreeAbility.setBought(true);
        }
    }
}
