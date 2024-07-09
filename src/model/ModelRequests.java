package model;

import model.objectModel.ObjectModel;
import model.objectModel.effects.EffectModel;
import model.objectModel.fighters.AbstractEnemy;
import model.objectModel.frameModel.FrameModel;

import java.util.ArrayList;

public class ModelRequests {

    public static ArrayList<String> removeObjectModelReq = new ArrayList<>();
    private static ArrayList<String> removeFrameModelReq = new ArrayList<>();
    private static ArrayList<String> removeEffectModelReq = new ArrayList<>();
    private static ArrayList<String> removeAbstractEnemyReq = new ArrayList<>();
    private static ArrayList<AbstractEnemy> addedAbstractEnemy = new ArrayList<>();
    private static ArrayList<EffectModel> addedEffectModel = new ArrayList<>();
    private static ArrayList<ObjectModel> addedObjectModel = new ArrayList<>();
    private static ArrayList<FrameModel> addedFrameModel = new ArrayList<>();

    public static void checkRequests(){
        checkObjects();
        checkFrames();
        checkEffects();
        checkAbstractEnemies();
    }

    private static void checkAbstractEnemies() {
        for (int i = 0 ;i < addedAbstractEnemy.size() ;i++){
            ModelData.addAbstractEnemy(addedAbstractEnemy.get(i));
            addedAbstractEnemy.remove(i);
            i--;
        }
        for (int i = 0 ;i < removeAbstractEnemyReq.size() ;i++){
            ModelData.removeAbstractEnemy(removeAbstractEnemyReq.get(i));
            removeAbstractEnemyReq.remove(i);
            i--;
        }
    }

    private static void checkEffects() {
        for (int i = 0 ;i < addedEffectModel.size() ;i++){
            ModelData.addEffect(addedEffectModel.get(i));
            addedEffectModel.remove(i);
            i--;
        }
        for (int i = 0 ;i < removeEffectModelReq.size() ;i++){
            ModelData.removeEffect(removeEffectModelReq.get(i));
            removeEffectModelReq.remove(i);
            i--;
        }
    }

    private static void checkObjects() {
        for (int i = 0; i <addedObjectModel.size() ;i++){
            ModelData.addModel(addedObjectModel.get(i));
            addedObjectModel.remove(i);
            i--;
        }
        for (int i = 0; i < removeObjectModelReq.size() ;i++){
            ModelData.removeModel(removeObjectModelReq.get(i));
            removeObjectModelReq.remove(i);
            i--;
        }
    }

    private static void checkFrames() {
        for (int i = 0; i <addedFrameModel.size() ;i++){
            ModelData.addFrame(addedFrameModel.get(i));
            addedFrameModel.remove(i);
            i--;
        }
        for (int i = 0 ;i < removeFrameModelReq.size() ;i++){
            ModelData.removeFrame(removeFrameModelReq.get(i));
            removeFrameModelReq.remove(i);
            i--;
        }
    }


    public synchronized static void addObjectModel(ObjectModel objectModel){
        addedObjectModel.add(objectModel);
    }

    public synchronized static void addFrameModel(FrameModel frameModel){
        addedFrameModel.add(frameModel);
    }

    public synchronized static void removeObjectModel(String id){
        removeObjectModelReq.add(id);
    }

    public synchronized static void removeFrameModel(String id){
        removeFrameModelReq.add(id);
    }

    public synchronized static void addEffectModel(EffectModel effectModel){
        addedEffectModel.add(effectModel);
    }

    public synchronized static void removeEffectModel(String id){
        removeEffectModelReq.add(id);
    }

    public synchronized static void addAbstractEnemy(AbstractEnemy abstractEnemy){
        addedAbstractEnemy.add(abstractEnemy);
    }

    public synchronized static void removeAbstractEnemy(String id){
        removeAbstractEnemyReq.add(id);
    }


}
