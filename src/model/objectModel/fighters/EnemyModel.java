package model.objectModel.fighters;


import controller.Controller;
import controller.manager.GameState;
import model.logics.collision.Collision;
import model.interfaces.IsCircle;
import model.interfaces.IsPolygon;
import model.objectModel.FighterModel;
import utils.Vector;

public abstract class EnemyModel extends FighterModel {

    protected boolean vulnerableToEpsilonMelee;
    protected boolean vulnerableToEpsilonBullet;

    public void meleeAttack(EpsilonModel epsilon){
        if (!hasMeleeAttack)
            return;
        if (this instanceof IsPolygon){
            for (Vector vertex : ((IsPolygon) this).getVertices()) {
                if (Collision.IsInCircle((IsCircle) epsilon, vertex)){
                    epsilon.setHP(epsilon.getHP() - meleeAttack);
                    return;
                }
            }
        }
    }

    @Override
    public void die() {
        Controller.removeObject(this);
        GameState.setEnemyCount(GameState.getEnemyCount() - 1);
        GameState.setEnemyKilled(GameState.getEnemyKilled() + 1);
    }

    public boolean isVulnerableToEpsilonMelee() {
        return vulnerableToEpsilonMelee;
    }

    public void setVulnerableToEpsilonMelee(boolean vulnerableToEpsilonMelee) {
        this.vulnerableToEpsilonMelee = vulnerableToEpsilonMelee;
    }

    public boolean isVulnerableToEpsilonBullet() {
        return vulnerableToEpsilonBullet;
    }

    public void setVulnerableToEpsilonBullet(boolean vulnerableToEpsilonBullet) {
        this.vulnerableToEpsilonBullet = vulnerableToEpsilonBullet;
    }
}
