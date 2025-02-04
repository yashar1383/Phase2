package model.skillTreeAbilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.enums.SkillTreeAbilityType;
import controller.manager.GameState;
import model.ModelData;
import model.skillTreeAbilities.Cerberus.Cerberus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
        abilities.add(new Athena());
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
}
