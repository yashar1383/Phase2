package model.objectModel.fighters.finalBoss.abilities.projectile;

import constants.Constants;
import model.ModelData;
import model.animations.DashAnimation;
import model.objectModel.fighters.EpsilonModel;
import model.objectModel.fighters.finalBoss.Boss;
import model.objectModel.fighters.finalBoss.abilities.Ability;
import model.objectModel.fighters.finalBoss.bossHelper.BossHelperModel;
import model.objectModel.fighters.finalBoss.bossHelper.HeadModel;
import utils.Math;
import utils.Vector;

public class Projectile extends Ability {

    private Boss boss;
    private EpsilonModel epsilon;
    private ProjectileThread thread;

    public Projectile(Boss boss){
        this.boss = boss;
        synchronized (ModelData.getModels()) {
            this.epsilon = (EpsilonModel) ModelData.getModels().getFirst();
        }
        thread = new ProjectileThread(this ,epsilon);
    }


    @Override
    protected void setUp() {
        ownHelper(boss.getHead());
        boss.getHead().setHovering(false);
        boss.getLeftHand().setVulnerableToEpsilonBullet(true);
        boss.getLeftHand().setVulnerableToEpsilonMelee(true);
        boss.getRightHand().setVulnerableToEpsilonBullet(true);
        boss.getRightHand().setVulnerableToEpsilonMelee(true);
    }

    @Override
    protected void unsetUp() {
        disownHelper(boss.getHead());
        boss.getHead().setHovering(false);
        boss.getLeftHand().setVulnerableToEpsilonBullet(false);
        boss.getLeftHand().setVulnerableToEpsilonMelee(false);
        boss.getRightHand().setVulnerableToEpsilonBullet(false);
        boss.getRightHand().setVulnerableToEpsilonMelee(false);
    }

    @Override
    public void activate() {
        super.activate();
        bossHeadAnimation();
        thread.setOrigin(epsilon.getPosition().clone());
        thread.start();
    }
    @Override
    protected void endAbility() {
        endAnimation();
        try {
            Thread.sleep(Constants.ABILITY_UNSETUP_DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread.interrupt();
        unsetUp();
    }

    private void bossHeadAnimation() {
        HeadModel headModel = boss.getHead();
        Vector destination = new Vector(
                epsilon.getPosition().x,
                epsilon.getPosition().y - Constants.PROJECTILE_NAVIGATE_RADIOS
        );
        Vector distance = Math.VectorAdd(
                destination,
                Math.ScalarInVector(-1 ,headModel.getPosition())
        );
        new DashAnimation(
                headModel,
                distance,
                Constants.ABILITY_SETUP_DELAY,
                Math.VectorSize(distance),
                0,
                false
        ).StartAnimation();
    }


    private void endAnimation() {
        Vector headDirection = Math.VectorAdd(
                new Vector(Constants.SCREEN_SIZE.width / 2d ,0),
                Math.ScalarInVector(-1 ,boss.getHead().getPosition())
        );
        helperAnimation(boss.getHead() ,headDirection);
    }

    private void helperAnimation(BossHelperModel helper, Vector direction) {
        new DashAnimation(
                helper,
                direction,
                1000,
                Math.VectorSize(direction),
                0,
                false
        ).StartAnimation();
    }


    public Boss getBoss() {
        return boss;
    }

}
