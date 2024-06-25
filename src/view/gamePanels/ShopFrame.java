package view.gamePanels;


import data.Constants;

import javax.swing.*;

public class ShopFrame extends JFrame {
    EndGamePanel endGame;
    public ShopFrame(){
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(Constants.GAME_WIDTH + Constants.barD.width ,Constants.GAME_HEIGHT + Constants.barD.height);
        this.setLocationRelativeTo(null);

        endGame = new EndGamePanel();

        this.add(endGame);
        this.setVisible(true);
    }

}
