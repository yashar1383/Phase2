package model.objectModel.fighters.miniBossEnemies.barricadosModel;

import constants.Constants;
import model.interfaces.Fader;
import model.interfaces.HasVertices;
import model.interfaces.IsPolygon;
import model.objectModel.fighters.miniBossEnemies.MiniBossModel;
import utils.Math;
import utils.Vector;

import java.util.ArrayList;

public abstract class BarricadosModel extends MiniBossModel implements Fader , IsPolygon , HasVertices {

    protected double time;
    protected ArrayList<Vector> vertices;

    @Override
    public void addTime(double time) {
        this.time += time;
    }

    @Override
    public void fadeIf() {
        if (time >= Constants.BARRICADOS_DURATION_TIME){
            die();
        }
    }

    @Override
    public void UpdateVertices(double xMoved ,double yMoved ,double theta) {
        for (int i = 0 ;i < vertices.size() ;i++){
            vertices.set(i ,new Vector(vertices.get(i).getX() + xMoved ,vertices.get(i).getY() + yMoved));
            vertices.set(i , Math.RotateByTheta(vertices.get(i) ,position ,theta));
        }
    }

    @Override
    public ArrayList<Vector> getVertices() {
        return vertices;
    }

    protected void initVertices() {
        vertices = new ArrayList<>();
        vertices.add(Math.VectorAdd(
                position,
                new Vector(
                        -Constants.BARRICADOS_DIMENSION.width /2d,
                        -Constants.BARRICADOS_DIMENSION.height /2d
                )
        ));
        vertices.add(Math.VectorAdd(
                position,
                new Vector(
                        Constants.BARRICADOS_DIMENSION.width / 2d,
                        -Constants.BARRICADOS_DIMENSION.height /2d
                )
        ));
        vertices.add(Math.VectorAdd(
                position,
                new Vector(
                        Constants.BARRICADOS_DIMENSION.width /2d,
                        Constants.BARRICADOS_DIMENSION.height /2d
                )
        ));
        vertices.add(Math.VectorAdd(
                position,
                new Vector(
                        -Constants.BARRICADOS_DIMENSION.width /2d,
                        Constants.BARRICADOS_DIMENSION.height /2d
                )
        ));
    }


}
