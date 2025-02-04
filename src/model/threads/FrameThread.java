
package model.threads;

import constants.Constants;
import controller.manager.GameState;
import model.ModelData;
import model.logics.collision.Collision;
import model.objectModel.fighters.EpsilonModel;
import model.objectModel.frameModel.FrameModel;
import model.objectModel.ObjectModel;
import utils.FrameHelper.NullLocalFrameHandler;
import utils.FrameHelper.FrameCalculationHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class FrameThread extends Thread{

    private ArrayList<FrameModel> frames;
    private ArrayList<ObjectModel> models;
    private HashMap<ObjectModel ,FrameModel> localFrames;
    private HashMap<ObjectModel ,FrameModel> previousLocals;

    public FrameThread(){
        frames = new ArrayList<>();
        models = new ArrayList<>();
        previousLocals = new HashMap<>();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 1000;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (!GameState.isOver()){
            if (GameState.isPause()) {
                lastTime = System.nanoTime();
                continue;
            }
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= Constants.FRAME_THREAD_REFRESH_RATE) {
                updateFrames();
                delta = 0;
            }
        }
    }

    private void updateFrames() {
        synchronized (ModelData.getModels()) {
            localFrames = (HashMap<ObjectModel, FrameModel>) ModelData.getLocalFrames().clone();
            models = (ArrayList<ObjectModel>) ModelData.getModels().clone();
            frames = (ArrayList<FrameModel>) ModelData.getFrames().clone();
        }
        defineLocalFrames();
        synchronized (ModelData.getModels()) {
            localFrames = (HashMap<ObjectModel, FrameModel>) ModelData.getLocalFrames().clone();
        }
        resetDisables();
        setDisablesForSolidObjects();
        checkSolidObjectBounds();
        resize(frames);
        updatePreviousLocals();
        if (!frames.isEmpty())
            framePressure(frames.getFirst());
    }

    private void resetDisables() {
        for (FrameModel frameModel : frames){
            frameModel.setCanTopResize(true);
            frameModel.setCanBottomResize(true);
            frameModel.setCanLeftResize(true);
            frameModel.setCanRightResize(true);
        }
    }

    private void updatePreviousLocals() {
        previousLocals = (HashMap<ObjectModel, FrameModel>) localFrames.clone();
    }

    private void setDisablesForSolidObjects() {
        for (ObjectModel model : models){
            if (!model.isSolid())
                continue;
            ArrayList<FrameModel> localFrames = defineFrame(model);
            if (localFrames.size() >= 2){
                for (int i = 0; i < localFrames.size() ;i++){
                    for (int j = 0; j < localFrames.size() ;j++){
                        if (i==j)
                            continue;
                        FrameCalculationHelper.setFrameDisables(
                                localFrames.get(i),
                                localFrames.get(j)
                        );
                    }
                }
            }
        }
    }

    private void checkSolidObjectBounds() {
        for (ObjectModel model : models){
            FrameModel frame = localFrames.get(model);
            if (frame == null && model.isSolid())
                new NullLocalFrameHandler(model,models ,frames ,previousLocals).handle();
            if (model instanceof EpsilonModel) {
                new NullLocalFrameHandler(model ,models ,frames ,localFrames).epsilonHandler();
            }
        }
    }

    private void defineLocalFrames() {

        ///////concurrent
        HashMap<ObjectModel ,FrameModel> newLocals = new HashMap<>();
        for (ObjectModel model : models){
            ArrayList<FrameModel> modelFrames = defineFrame(model);
            if (modelFrames.isEmpty()){
                newLocals.put(model ,null);
            }
            else if (modelFrames.size() == 1){
                newLocals.put(model ,modelFrames.getFirst());
            }
            else {
                if (modelFrames.contains(localFrames.get(model))){
                    newLocals.put(model ,localFrames.get(model));
                }
                else {
                    newLocals.put(model ,modelFrames.getFirst());
                }
            }
        }
        ModelData.setLocalFrames(newLocals);
    }

    private void resize(ArrayList<FrameModel> frameModels) {
        if (GameState.isDizzy())
            return;
        for (FrameModel frame : frameModels){
            if (!frame.isIsometric()){
                frame.resize();
            }
        }
    }
    private void framePressure(FrameModel frame) {
        if (!frame.isResizing()){
            frame.setUpDownV(-GameState.getShrinkageVelocity(),-GameState.getShrinkageVelocity());
            frame.setLeftRightV(-GameState.getShrinkageVelocity(),-GameState.getShrinkageVelocity());
        }
    }


    private ArrayList<FrameModel> defineFrame(ObjectModel model){
        ArrayList<FrameModel> frames = new ArrayList<>();
        ArrayList<FrameModel> dataFrames = ModelData.getFrames();
        for (int i = 0 ;i < dataFrames.size() ;i++){
            if (Collision.isInFrame(dataFrames.get(i) ,model.getPosition())){
                frames.add(dataFrames.get(i));
            }
        }
        return frames;
    }

}
