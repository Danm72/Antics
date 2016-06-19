package com.antics.data;

import com.antics.ant.Ant;
import com.antics.ant.Builder;
import com.antics.ant.Gatherer;
import com.antics.ant.Queen;
import com.antics.ant.Termite;

import static com.antics.util.ConfigFile.NEST_X;
import static com.antics.util.ConfigFile.NEST_Y;
import static com.antics.util.ConfigFile.ants;
import static com.antics.util.ConfigFile.board;
import static com.antics.util.ConfigFile.rendererCreated;

public class GameManager {
    // this creates the textures,
    // this must be done on first run and every time the screen loses focus.


    public void populateMap() {
        // if the program has not run yet
        if (rendererCreated == false) {

            // this creates an ant hive
            for (int i = 0; i < 3; i++) {
                board.tiles[(NEST_X - 1) + i][NEST_Y - 1]
                        .setHive();
                board.tiles[(NEST_X - 1) + i][NEST_Y]
                        .setHive();
                board.tiles[(NEST_X - 1) + i][NEST_Y + 1]
                        .setHive();
            }
            // Add the QUEEN to the ant array
            ants.add(new Queen(board));
            board.ants++;

            // add ten builders to begin
            for (int i = 0; i < 10; i++) {
                ants.add(new Builder(board, NEST_X,
                        NEST_Y));
                board.ants++;
            }
            // add 15 GATHERERs to begin
            for (int i = 0; i < 15; i++) {
                ants.add(new Gatherer(board, NEST_X,
                        NEST_Y));
                board.ants++;
            }

            // add two TERMITEs
            ants.add(new Termite(board,0, 0));
            ants.add(new Termite(board,10, 10));

            // add five squareVectors, a different squareVector is used per ant to draw the
            // correct texture coordinates

            // stop this secton recurring
            rendererCreated = true;
        }
    }

    public void newTurn() {
        ants.get(0).action();
        // All other ants do their actions given they are alive
        for (int i = 1; i < ants.size(); i++) {
            if (isAlive(ants.get(i))) {
                if (ants.get(i).action()) {
                    if (ants.get(i).antType() == 0)
                        ants.set(i,
                                new Gatherer(board, ants.get(i)));
                    else
                        ants.set(i,
                                new Builder(board, ants.get(i)));
                }
            } else {
                ants.remove(i);
                i--;
            }
        }
    }

    public boolean isAlive(Ant ant) {
        //if(dead)
        //return false;
        if (ant.getHealth() > 0)
            return true;
        ant.die();
        return false;
    }
}