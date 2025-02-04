package model.objectModel.fighters.normalEnemies.omenoctModel;

import controller.Controller;
import controller.enums.ModelType;
import controller.manager.Spawner;
import controller.manager.loading.SkippedByJson;
import constants.Constants;
import model.interfaces.*;
import model.objectModel.frameModel.FrameLocations;
import model.objectModel.fighters.normalEnemies.NormalEnemyModel;
import utils.Math;
import utils.Vector;

import javax.swing.*;
import java.util.ArrayList;

public class OmenoctModel extends NormalEnemyModel implements Ability , MoveAble, FrameAttacher ,IsPolygon ,HasVertices {

    /////////////////////// Fix the Ability interface with Navigator interface


    private ArrayList<Vector> vertices = new ArrayList<>();
    private FrameLocations frameLocation;
    private FrameLocations willAttachTo;
    private Vector destination;
    @SkippedByJson
    private Timer shooter;
    private final OmenoctNavigater navigater;

    public OmenoctModel(Vector position ,String id){
        this.position = position;
        this.velocity = new Vector(0 ,0);
        this.acceleration = new Vector(0 ,0);
        this.id = id;
        this.HP = 20;
        type = ModelType.omenoct;
        vulnerableToEpsilonBullet = true;
        vulnerableToEpsilonMelee = true;
        hasMeleeAttack = true;
        meleeAttack = Constants.OMENOCT_MELEE_ATTACK;
        omega = Constants.ENEMY_ROTATION_SPEED;
        navigater = new OmenoctNavigater(position);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        initVertices();
    }

    private void initVertices() {
        vertices = new ArrayList<>();
        Vector initVector = new Vector(
                java.lang.Math.cos(java.lang.Math.PI / 8),
                java.lang.Math.sin(java.lang.Math.PI / 8)
        );
        initVector = Math.VectorWithSize(initVector ,Constants.OMENOCT_RADIOS);
        for (int i = 0 ;i < 8 ;i++){
            vertices.add(
                    Math.VectorAdd(
                            position,
                            initVector
                    )
            );
            initVector = Math.RotateByTheta(initVector ,new Vector() ,java.lang.Math.PI / 4);
        }
    }


    @Override
    public void ability() {
        navigater.reset(position);
        navigater.navigateFrame();
        destination = navigater.getDestination();
        willAttachTo = navigater.getWillAttachTo();
        setNavigationVelocity();
        checkAttached();
        setOmenoctShooterPosition();
    }

    private void setOmenoctShooterPosition() {
        if (shooter != null) {
            ((OmenoctShooter)shooter.getActionListeners()[0]).setPosition(position);
        }
    }

    private void setNavigationVelocity() {
        assert destination!= null;
        velocity = Math.VectorWithSize(
                Math.VectorAdd(
                        Math.ScalarInVector(-1 ,position),
                        destination
                )
                ,Constants.OMENOCT_NAVIGATE_VELOCITY
        );
    }

    private void checkAttached() {
        if (willAttachTo == null)
            return;
        if (Math.VectorSize(Math.VectorAdd(
                Math.ScalarInVector(-1 ,position),
                destination
        )) <= Constants.OMENOCT_NAVIGATE_VELOCITY * Constants.UPS){
            position = destination.clone();
            velocity = new Vector();
        }
        if (destination.Equals(position)){
            frameLocation = willAttachTo;
            setUpShooter();
        }
    }

    private void setUpShooter() {
        if (shooter != null){
            shooter.start();
        }
        else {
            shooter = new Timer(1000, new OmenoctShooter(position ,this));
            shooter.start();
        }
    }


    @Override
    public boolean hasAbility() {
        return true;
    }

//    @Override
//    public ArrayList<Vector> getVertices() {
//        return vertices;
//    }

    @Override
    public FrameLocations getAttachedLocation() {
        return frameLocation;
    }

    @Override
    public void damage() {
        HP = HP - Constants.OMENOCT_FRAME_DAMAGE;
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
        UpdateVertices(xMoved ,yMoved ,thetaMoved);
    }

    @Override
    public void die() {
        super.die();
        if (shooter != null) {
            shooter.stop();
        }
        Spawner.addCollectives(position ,8 ,4);
    }

    @Override
    public void UpdateVertices(double xMoved, double yMoved, double theta) {
        for (int i = 0 ;i < vertices.size() ;i++){
            vertices.set(i ,new Vector(vertices.get(i).getX() + xMoved ,vertices.get(i).getY() + yMoved));
            vertices.set(i , Math.RotateByTheta(vertices.get(i) ,position ,theta));
        }
    }

    @Override
    public ArrayList<Vector> getVertices() {
        return vertices;
    }

    public Timer getShooter() {
        return shooter;
    }
}
