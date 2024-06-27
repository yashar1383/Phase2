package model.objectModel.normalEnemies.archmireModel;

import data.Constants;
import model.ModelData;
import model.interfaces.Ability;
import model.interfaces.HasVertices;
import model.interfaces.IsPolygon;
import model.interfaces.MoveAble;
import model.logics.aoe.OverTimeAOE;
import model.objectModel.EpsilonModel;
import model.objectModel.normalEnemies.NormalEnemyModel;
import utils.Math;
import utils.Vector;

import java.util.ArrayList;

public class ArchmireModel extends NormalEnemyModel implements MoveAble , IsPolygon , Ability ,HasVertices {
    private OverTimeAOE aoe;
    private ArchmireThread thread;
    private ArrayList<Vector> vertices = new ArrayList<>();

    public ArchmireModel(Vector position ,String id){
        aoe = new OverTimeAOE();
        thread = new ArchmireThread(this);
        this.position = position;
        this.velocity = new Vector(0 ,0);
        this.acceleration = new Vector(0 ,0);
        this.id = id;
        this.HP = 20;
        setHovering(true);
        omega = Constants.ENEMY_ROTATION_SPEED;
        initVertices();
        thread.setPriority(3);
        thread.start();
    }

    private void initVertices() {
        vertices = new ArrayList<>();
        vertices.add(new Vector(
                position.x ,
                position.y - (java.lang.Math.sqrt(3) * Constants.ARCHMIRE_DIMENSION.width / 3d))
        );
        vertices.add(new Vector(
                position.x - Constants.ARCHMIRE_DIMENSION.width / 2d ,
                position.y + (java.lang.Math.sqrt(3) * Constants.ARCHMIRE_DIMENSION.width / 6d))
        );
        vertices.add(new Vector(
                position.x + Constants.ARCHMIRE_DIMENSION.width / 2d ,
                position.y + (java.lang.Math.sqrt(3) * Constants.ARCHMIRE_DIMENSION.width / 6d))
        );
    }


    @Override
    protected void meleeAttack(EpsilonModel epsilon) {
        //////////////nothing
    }

    @Override
    public void die() {

    }

    public OverTimeAOE getAOE(){
        return aoe;
    }

    @Override
    public void move() {
        velocity = Math.VectorAdd(velocity ,Math.ScalarInVector(Constants.UPS ,acceleration));
        double xMoved = ((2 * velocity.x - acceleration.x * Constants.UPS) / 2) * Constants.UPS;
        double yMoved = ((2 * velocity.y - acceleration.y * Constants.UPS) / 2) * Constants.UPS;
        setPosition(position.x + xMoved ,position.y + yMoved);


        omega += alpha * Constants.UPS;
        double thetaMoved = ((2 * omega - alpha * Constants.UPS) / 2) * Constants.UPS;
        theta = theta + thetaMoved;
        if (this instanceof HasVertices)
            ((HasVertices) this).UpdateVertices(xMoved ,yMoved ,thetaMoved);
    }

    @Override
    public ArrayList<Vector> getVertices() {
        return vertices;
    }

    @Override
    public void ability() {
        synchronized (ModelData.getModels()) {
            Vector epsilonPosition = ModelData.getModels().getFirst().getPosition();
            velocity = Math.VectorWithSize(Math.VectorAdd(
                    epsilonPosition,
                    Math.ScalarInVector(-1 ,position)
            ),Constants.ARCHMIRE_VELOCITY);
        }
    }

    @Override
    public boolean hasAbility() {
        return true;
    }

    @Override
    public void UpdateVertices(double xMoved, double yMoved, double theta) {
        for (int i = 0 ;i < vertices.size() ;i++){
            vertices.set(i ,new Vector(vertices.get(i).getX() + xMoved ,vertices.get(i).getY() + yMoved));
            vertices.set(i , Math.RotateByTheta(vertices.get(i) ,position ,theta));
        }
    }
}
