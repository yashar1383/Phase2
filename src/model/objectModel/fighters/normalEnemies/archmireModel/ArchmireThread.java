package model.objectModel.fighters.normalEnemies.archmireModel;

import controller.manager.Spawner;
import constants.Constants;
import controller.manager.GameState;
import model.ModelData;
import model.logics.collision.Collision;
import model.objectModel.ObjectModel;
import model.objectModel.effects.ArchmireAoeEffectModel;
import model.objectModel.fighters.EnemyModel;
import model.objectModel.fighters.EpsilonModel;
import utils.Helper;

import java.util.ArrayList;


public class ArchmireThread extends Thread{

    private double time;
    private ArchmireModel archmire;
    private ArrayList<String> removedAoe = new ArrayList<>();
    private ArrayList<ObjectModel> models;

    public ArchmireThread(ArchmireModel archmire){
        this.archmire = archmire;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 1000;
        double ns = 1000000000 / amountOfTicks;
        double deltaModel = 0;
        while (!GameState.isOver()) {
            if (GameState.isPause()) {
                lastTime = System.nanoTime();
                continue;
            }
            if (isInterrupted())
                return;
            long now = System.nanoTime();
            deltaModel += (now - lastTime) / ns;
            lastTime = now;
            if (deltaModel >= Constants.ARCHMIRE_THREAD_REFRESH_RATE) {
                updateAOE();
                deltaModel = 0;
                time += Constants.ARCHMIRE_THREAD_REFRESH_RATE;
            }
        }
    }

    private void updateAOE() {
        if (GameState.isDizzy())
            return;
        synchronized (ModelData.getModels()){
            models = (ArrayList<ObjectModel>) ModelData.getModels().clone();
        }
        checkRemovedAOEs();
        addEffect();
        checkDamage();
    }

    private void checkDamage() {
        if (time % 1000 != 0)
            return;

        ArrayList<ObjectModel> collidedModels = new ArrayList<>();

        for (ObjectModel model : models){
            if (isCollided(model)){
                collidedModels.add(model);
            }
        }

        for (ObjectModel model : collidedModels){
            if (model instanceof ArchmireModel)
                continue;
            if (Collision.IsColliding(model ,archmire)){
                model.setHP(model.getHP() - Constants.ARCHMIRE_DROWN_DAMAGE_PER_SECOND);
            }
            else {
                model.setHP(model.getHP() - Constants.ARCHMIRE_AOE_DAMAGE_PER_SECOND);
            }
        }

    }

    private boolean isCollided(ObjectModel model) {
        for (ArchmireAoeEffectModel effectModel : archmire.getAoeEffects()){
            if (Collision.IsColliding(effectModel ,model)) {
                if (model instanceof EnemyModel || model instanceof EpsilonModel)
                    return true;
            }
        }
        return false;
    }

    private void checkRemovedAOEs() {
        for (String id : removedAoe){
            removeAoe(id);
        }
        removedAoe = new ArrayList<>();
    }

    private void removeAoe(String id) {
        for (ArchmireAoeEffectModel effectModel : archmire.getAoeEffects()){
            if (effectModel.getId().equals(id)){
                archmire.getAoeEffects().remove(effectModel);
                return;
            }
        }
    }

    private void addEffect() {
        ArchmireAoeEffectModel effectModel = new ArchmireAoeEffectModel(
                archmire,
                Helper.RandomStringGenerator(Constants.ID_SIZE)
        );
        Spawner.addArchmireEffect(effectModel);
        archmire.getAoeEffects().add(effectModel);
    }

    public ArrayList<String> getRemovedAoe() {
        return removedAoe;
    }
}
