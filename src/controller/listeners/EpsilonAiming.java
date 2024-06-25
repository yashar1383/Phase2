package controller.listeners;


import controller.enums.ObjectType;
import controller.manager.Spawner;
import data.Constants;
import model.GameState;
import model.ModelData;
import model.objectModel.EpsilonModel;
import utils.Math;
import utils.Vector;
import view.soundEffects.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class EpsilonAiming implements MouseListener {

    double timer;
    static public double extraAim;
    public EpsilonAiming(){
        timer = 0;
        extraAim = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        if ((GameState.getTime() - timer) * 1000 >= Constants.AIMING_PAUSE_TIME){
            timer = GameState.getTime();
            EpsilonModel epsilon =(EpsilonModel) ModelData.getModels().getFirst();
            Vector clickedPoint = new Vector(
                    e.getX() - Constants.SCREEN_SIZE.width ,
                    e.getY() - Constants.SCREEN_SIZE.height
            );
            if (clickedPoint.Equals(epsilon.getPosition()))
                return;
            Vector direction = Math.VectorAdd(Math.ScalarInVector(-1 ,epsilon.getPosition()) ,clickedPoint);
            Vector position = Math.VectorAdd(Math.VectorWithSize(direction ,Constants.BULLET_DIAMETER / 2 + Constants.EPSILON_DIMENSION.width) ,epsilon.getPosition());
            int constant = -1;
        Spawner.addProjectile(position, direction , ObjectType.epsilonBullet);
            for (int i = 0; i < extraAim ;i++) {
                constant = constant * (-1);
                Vector direction2 = Math.RotateByTheta(direction, new Vector(0 ,0), java.lang.Math.PI / 12 * constant);
                if (i / 2 == 1)
                    constant -= 1;
                Spawner.addProjectile(position, direction , ObjectType.epsilonBullet);
            }


            try {
                Sound sound = new Sound(Constants.BulletFiredSound);
                sound.play();
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
//        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
