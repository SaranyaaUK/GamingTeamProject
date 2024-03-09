package gamelogic;

import structures.GameState;
import structures.basic.*;
import utils.TilesGenerator;

import static gamelogic.ScoreForUnitAction.*;

public class Values {

    public static double getUnitValue(Unit unit){



        return 1;
    }

    public static double getUnitEffectValue(Unit unit){
        return 0;
    }

    public static double getDeathWatchValue(Unit unit){


        return 0;
    }


    // unit amount, bounty provoke
    public static double getProvokeValue(Unit unit,Tile tile){


        return 0;
    }

    public static double getAvatarDistanceValue(Unit unit, int avatar1Distance, int avatar2Distance){



        return 25-avatar1Distance-avatar2Distance;
    }

    public static double getFutureAttackedScore(Unit unit, Tile tile){


        return 0;
    };

    public static double getDamageScore(Unit targetUnit, int ATK){
        double score=0;
        double lethalScore=0;
        double hpLossScore=0;
        double deathWatchScore=0;

        
        double myUnitModifier = getUnitValue(targetUnit) * getScoreForPosition(targetUnit, TilesGenerator.getUnitTile(targetUnit));

        int targetHP=targetUnit.getHealth();
        boolean lethal= (ATK>=targetHP);
        boolean isAvatar= (targetUnit== GameState.getInstance().getHumanPlayer().getAvatar()|| targetUnit==GameState.getInstance().getAIPlayer().getAvatar());
        int hpLoss=Math.min(ATK, targetHP);

        if(isAvatar){
            if(lethal){lethalScore=9999;}
            hpLossScore=hpLoss*(25.0/targetHP);

        }else{
            if(lethal){
                lethalScore= BOUNTY.REMOVAL_PER_UNIT_SCORE;}
                hpLossScore=hpLoss*BOUNTY.DAMAGE_PER_UNIT_SCORE();

        }

        Tile[][] Tiles = GameState.getInstance().getGrid().getBoardTiles();
        for (Tile[] tiles : Tiles) {
            for (Tile tile : tiles) {
                deathWatchScore += ((lethal) ? DeathWatchValue.getUnitDeathWatchValue(tile.getUnit()) : 0);
            }
        }

        score=lethalScore+hpLossScore;




        return 0.03*score * myUnitModifier;

    }

}
