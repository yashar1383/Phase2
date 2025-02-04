package model.objectModel.fighters.miniBossEnemies.barricadosModel;

import controller.enums.ModelType;
import constants.Constants;
import model.objectModel.frameModel.FrameModel;
import model.objectModel.frameModel.FrameModelBuilder;
import utils.Math;
import utils.Vector;

public class BarricadosSecondModel extends BarricadosModel{

    public BarricadosSecondModel(Vector position ,String id){
        this.position = position;
        this.HP = 10000000;
        this.id = id;
        this.vulnerableToEpsilonBullet = false;
        this.vulnerableToEpsilonMelee = false;
        isMotionless = true;
        type = ModelType.barricadosTheSecond;
        initVertices();
        initFrame();
    }

    private void initFrame() {
        FrameModelBuilder builder = new FrameModelBuilder(
                Math.VectorAdd(
                        position,
                        new Vector(
                                -Constants.BARRICADOS_DIMENSION.width / 2d,
                                -Constants.BARRICADOS_DIMENSION.height / 2d
                        )
                ),
                Constants.BARRICADOS_DIMENSION,
                id
        );
        builder.setSolid(true);
        builder.setIsometric(true);
        frameModel = builder.create();
    }

    private FrameModel frameModel;

    @Override
    public void die() {

    }

    public FrameModel getFrameModel() {
        return frameModel;
    }

    public void setFrameModel(FrameModel frameModel) {
        this.frameModel = frameModel;
    }
}
