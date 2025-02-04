package model.objectModel.fighters.finalBoss.abilities.vomit;

import constants.Constants;
import controller.manager.GameState;
import model.ModelData;
import model.logics.collision.Collision;
import model.objectModel.ObjectModel;
import model.objectModel.frameModel.FrameModel;

import java.util.ArrayList;

public class VomitThread extends Thread{
    private ArrayList<ObjectModel> models;
    private Vomit vomit;
    private double time;
    private FrameModel epsilonFrame;

    public VomitThread(Vomit vomit ,FrameModel epsilonFrame){
        this.vomit = vomit;
        this.epsilonFrame = epsilonFrame;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 1000;
        double ns = 1000000000 / amountOfTicks;
        double deltaModel = 0;
        while (!GameState.isOver() && !isInterrupted()) {
            if (GameState.isPause()){
                lastTime = System.nanoTime();
                continue;
            }
            long now = System.nanoTime();
            deltaModel += (now - lastTime) / ns;
            lastTime = now;
            if (deltaModel >= Constants.SQUEEZE_THREAD_REFRESH_RATE) {
                update();
                deltaModel = 0;
                time += Constants.SQUEEZE_THREAD_REFRESH_RATE;
            }
        }
    }

    private void update() {
        updateVariables();
        fireIf();
    }

    private void updateVariables() {
        synchronized (ModelData.getModels()){
            models = (ArrayList<ObjectModel>) ModelData.getModels().clone();
        }
    }

    private void fireIf() {
        if (time % 1000 == 0){
            vomit.addEffect(epsilonFrame);
        }
        if (time >= Constants.VOMIT_DURATION_TIME){
            vomit.endAbility();
        }
    }

    public void dealDamage(BossAoeEffectModel effect) {
        updateVariables();

        ArrayList<ObjectModel> collidedModels = new ArrayList<>();

        for (ObjectModel model : models){
            if (Collision.IsColliding(model ,effect)){
                collidedModels.add(model);
            }
        }

        for (ObjectModel model : collidedModels){
            model.setHP(model.getHP() - Constants.VOMIT_AOE_DAMAGE);
        }

    }
}
