package view.objectViews.basicEnemyView;


import constants.Constants;
import utils.Vector;

import java.awt.*;

public class TrigorathView extends BasicEnemyView{
    public TrigorathView(Vector position, String id){
        this.position = position;
        this.id = id;
        this.image = Constants.trigorathImage;
    }
    @Override
    public void draw(Graphics2D g2d) {
        g2d.rotate(
                -theta ,
                position.getX() + Constants.SCREEN_SIZE.width ,
                position.getY() + Constants.SCREEN_SIZE.height
        );
        g2d.drawImage(
                image ,
                (int) position.x - Constants.TRIGORATH_DIMENTION.width / 2 + Constants.SCREEN_SIZE.width,
                (int) position.y - (Constants.TRIGORATH_DIMENTION.height * 2) / 3 + Constants.SCREEN_SIZE.height,
                Constants.TRIGORATH_DIMENTION.width ,Constants.TRIGORATH_DIMENTION.height ,
                null
        );
        g2d.rotate(
                theta ,
                position.getX() + Constants.SCREEN_SIZE.width ,
                position.getY() + Constants.SCREEN_SIZE.height
        );
    }
}
