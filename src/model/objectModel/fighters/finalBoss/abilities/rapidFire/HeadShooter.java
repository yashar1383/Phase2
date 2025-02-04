package model.objectModel.fighters.finalBoss.abilities.rapidFire;

import controller.enums.ModelType;
import controller.manager.Spawner;
import constants.Constants;
import utils.Math;
import utils.Vector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeadShooter implements ActionListener {

    private double time;
    private RapidFire rapidFire;
    public HeadShooter(RapidFire rapidFire){
        this.rapidFire = rapidFire;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        double theta = 0;
        Vector direction;
        Vector spawnPosition;
        for (int i = 0; i < 16 ;i++){
            theta += java.lang.Math.PI / 8d;
            direction = new Vector(java.lang.Math.cos(theta) , java.lang.Math.sin(theta));
            spawnPosition = Math.VectorAdd(
                    Math.VectorWithSize(direction , Constants.HEAD_DIMENSION.width / 2d),
                    rapidFire.getBoss().getHead().getPosition()
            );
            Spawner.addProjectile(
                    spawnPosition,
                    direction,
                    ModelType.bossBullet
            );
        }

        time += Constants.BOSS_BULLET_DELAY_TIME;
        if (time >= Constants.RAPID_FIRE_DURATION_TIME){
            rapidFire.endAbility();
            rapidFire.getShooter().removeActionListener(this);
            rapidFire.getShooter().stop();
        }

    }
}
