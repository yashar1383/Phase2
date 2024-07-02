package model.collision;


import controller.configs.Configs;
import data.Constants;
import model.GameState;
import model.interfaces.CollisionDetector;
import model.interfaces.HasVertices;
import model.interfaces.IsPolygon;
import model.logics.Impact;
import model.objectModel.CollectiveModel;
import model.objectModel.fighters.EnemyModel;
import model.objectModel.fighters.EpsilonModel;
import model.objectModel.ObjectModel;
import model.objectModel.fighters.basicEnemies.SquarantineModel;
import model.objectModel.fighters.basicEnemies.TrigorathModel;
import model.objectModel.frameModel.FrameModel;
import model.objectModel.projectiles.BulletModel;
import model.objectModel.projectiles.EpsilonBulletModel;
import utils.Math;
import utils.Vector;

public class CollisionHandler {
    ObjectModel model1;
    ObjectModel model2;
    Vector collisionPoint;
    public CollisionHandler(ObjectModel model1 ,ObjectModel model2){
        this.model1 = model1;
        this.model2 = model2;
        this.collisionPoint = Collision.FindCollisionPoint(model1 ,model2);
    }

    public void handle() {
        ///////////epsilon and anotherModel
        if (model1 instanceof EpsilonModel || model2 instanceof EpsilonModel){
            if (model1 instanceof EpsilonModel){
                epsilonHandler((EpsilonModel) model1 ,model2);
            }
            else {
                epsilonHandler((EpsilonModel) model2 ,model1);
            }
            return;
        }

        /////////enemy and enemy
        if (model1 instanceof EnemyModel && model2 instanceof EnemyModel){
            enemyHandler((EnemyModel) model1 ,(EnemyModel)model2);
            return;
        }

        ///////////enemy and epsilonBullet
        if (model1 instanceof EnemyModel && model2 instanceof EpsilonBulletModel) {
            BulletToEnemyHandler((EnemyModel) model1, (EpsilonBulletModel) model2);
            return;
        }
        if (model2 instanceof EnemyModel && model1 instanceof EpsilonBulletModel){
            BulletToEnemyHandler((EnemyModel) model2 ,(EpsilonBulletModel) model1);
            return;
        }

    }

    private void BulletToEnemyHandler(EnemyModel enemy, EpsilonBulletModel epsilonBullet) {
        enemy.setHP(enemy.getHP() - epsilonBullet.getDamage());
        epsilonBullet.die();
    }

    private void enemyHandler(EnemyModel enemy1, EnemyModel enemy2) {
        if (enemy1.isHovering() || enemy2.isHovering())
            return;
        pullOutObject(enemy1 ,enemy2);
        new Impact(collisionPoint).MakeImpact();
        if (enemy1 instanceof CollisionDetector)
            ((CollisionDetector) enemy1).detect();
        if (enemy2 instanceof CollisionDetector)
            ((CollisionDetector) enemy2).detect();
    }

    private void epsilonHandler(EpsilonModel epsilon ,ObjectModel object) {
        if (object.isHovering()){
            epsilon.meleeAttack((EnemyModel) object);
            return;
        }
        if (object instanceof EnemyModel){
            ((EnemyModel) object).meleeAttack(epsilon);
            epsilon.meleeAttack((EnemyModel) object);
            pullOutObject(epsilon ,object);
            new Impact(collisionPoint).MakeImpact();
        }
        if (object instanceof BulletModel){
            epsilon.setHP(epsilon.getHP() - ((BulletModel) object).getDamage());
            object.die();
        }
        if (object instanceof CollisionDetector){
            ((CollisionDetector) object).detect();
        }
    }



    public void EpsilonEnemy(EpsilonModel epsilon , EnemyModel enemy){
        for (int i = 0 ;i < ((IsPolygon)enemy).getVertices().size() ;i++){
            if (Collision.IsInCircle(epsilon ,((IsPolygon)enemy).getVertices().get(i))){
                if (enemy instanceof TrigorathModel)
                    GameState.setHp(GameState.getHp() - Constants.TRIGORATH_DAMAGE);
                else if (enemy instanceof SquarantineModel)
                    GameState.setHp(GameState.getHp() - Constants.SQURANTINE_DAMAGE);
                break;
            }
        }
        for (int i = 0; i < EpsilonModel.getVertices().size() ; i++){
            if (Collision.IsColliding(EpsilonModel.getVertices().get(i) ,enemy)){
                enemy.setHP(enemy.getHP() - Constants.MELEI_ATTACK - Configs.EXTRA_DAMAGE);
                break;
            }
        }
        pullOutObject(epsilon ,enemy);
        new Impact(collisionPoint).MakeImpact();
    }

    public void EnemyEnemy(EnemyModel a, EnemyModel b) {
        ObjectModel attacker = a, defender = b;
        for (int i = 0; i < ((IsPolygon) b).getVertices().size(); i++) {
            if (collisionPoint.Equals(((IsPolygon) b).getVertices().get(i))) {
                attacker = b;
                defender = a;
            }
        }
        pullOutObject(attacker, defender);
        new Impact(collisionPoint).MakeImpact();
    }

    public void EnemyBullet(EnemyModel enemy, BulletModel bullet) {
        bullet.setHP(-1);
        enemy.setHP(enemy.getHP() - Constants.EPSILON_DAMAGE - Configs.EXTRA_DAMAGE);
    }


    private void pullOutObject(ObjectModel attacker, ObjectModel defender) {
        Vector attackerP = Math.VectorAdd(Math.ScalarInVector(-1, collisionPoint), attacker.getPosition());
        attackerP = Math.VectorWithSize(attackerP, 1);
        while (Collision.IsColliding(attacker, defender)) {
            attacker.setPosition(Math.VectorAdd(attackerP, attacker.getPosition()));
            if (attacker instanceof HasVertices){
                ((HasVertices) attacker).UpdateVertices(attackerP.x ,attackerP.y ,0);
            }
        }
    }


    public void EpsilonCollective(EpsilonModel epsilon, CollectiveModel collective) {
        collective.setHP(-1);
    }

}
