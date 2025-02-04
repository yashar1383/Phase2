package model.objectModel.fighters.finalBoss;

import controller.manager.Spawner;
import controller.manager.loading.SkippedByJson;
import constants.Constants;
import model.objectModel.fighters.AbstractEnemy;
import model.objectModel.fighters.finalBoss.bossHelper.HandModel;
import model.objectModel.fighters.finalBoss.bossHelper.HeadModel;
import model.objectModel.fighters.finalBoss.bossHelper.PunchModel;
import utils.Helper;
import utils.Vector;

public class Boss extends AbstractEnemy {

    private HandModel leftHand;
    private HandModel rightHand;
    private HeadModel head;
    private PunchModel punch;
    private int attackPhase = 1;
    @SkippedByJson
    private BossThread bossThread;

    public Boss(String id){
        this.id = id;
        initHead();
        initHands();
        initPunch();
        bossThread = new BossThread(this);
        bossThread.start();
    }

    private void initPunch() {
        punch = new PunchModel(
                new Vector(
                        Constants.PUNCH_DIMENSION.width / 2d,
                        Constants.SCREEN_SIZE.height - Constants.PUNCH_DIMENSION.height /2d
                ),
                Helper.RandomStringGenerator(Constants.ID_SIZE)
        );
    }

    private void initHands() {
        leftHand = new HandModel(
                new Vector(
                        Constants.HAND_DIMENSION.width / 2d,
                        Constants.SCREEN_SIZE.height / 2d
                ),
                Helper.RandomStringGenerator(Constants.ID_SIZE)
        );
        rightHand = new HandModel(
                new Vector(
                        Constants.SCREEN_SIZE.width - Constants.HAND_DIMENSION.width / 2d,
                        Constants.SCREEN_SIZE.height / 2d
                ),
                Helper.RandomStringGenerator(Constants.ID_SIZE)
        );
    }

    private void initHead() {
        head = new HeadModel(
                new Vector(
                        Constants.SCREEN_SIZE.width / 2d,
                        -Constants.HEAD_DIMENSION.width
                ),
                this,
                Helper.RandomStringGenerator(Constants.ID_SIZE)
        );
    }

    public void spawnHead() {
        Spawner.spawnHead(head);
        head.setInUse(false);
    }
    public void spawnLeftHand() {
        Spawner.spawnHand(leftHand);
        leftHand.setInUse(false);
    }
    public void spawnRightHand() {
        Spawner.spawnHand(rightHand);
        rightHand.setInUse(false);
    }

    public void spawnPunch(){
        Spawner.addPunch(punch);
        punch.setInUse(false);
    }


    public HandModel getLeftHand() {
        return leftHand;
    }

    public void setLeftHand(HandModel leftHand) {
        this.leftHand = leftHand;
    }

    public HandModel getRightHand() {
        return rightHand;
    }

    public void setRightHand(HandModel rightHand) {
        this.rightHand = rightHand;
    }

    public HeadModel getHead() {
        return head;
    }

    public void setHead(HeadModel head) {
        this.head = head;
    }

    public PunchModel getPunch() {
        return punch;
    }

    public void setPunch(PunchModel punch) {
        this.punch = punch;
    }

    public int getAttackPhase() {
        return attackPhase;
    }

    public void setPhaseAttack(int phaseAttack) {
        this.attackPhase = phaseAttack;
    }
}
