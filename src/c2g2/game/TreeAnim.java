package c2g2.game;

import c2g2.engine.GameEngine;
import c2g2.engine.IGameLogic;
 
public class TreeAnim {
 
    public static void main(String[] args) {
        try {
            boolean vSync = true;
	    System.out.println("Loading tree animation...");
            IGameLogic gameLogic = new HelloAnim();
            GameEngine gameEng = new GameEngine("COMSW4160-PA1", 960, 540, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
