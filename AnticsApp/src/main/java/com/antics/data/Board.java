package com.antics.data;

import android.util.Log;
import com.antics.ant.Ant;
import com.antics.geometry.Point3D;
import com.antics.objects.Hexagon;
import com.antics.util.ConfigFile;

public class Board {

    public int builder, gatherer, soldier, ants, dead = 0;
    public Tile[][] tiles;
    public int height, width;
    public int NEST_X = 25;
    public int NEST_Y = 25;

    public Board(int height, int width) {
        tiles = new Tile[height][width];
        this.height = height;
        this.width = width;
    }

    public void countAnts() {
        builder = 0;
        gatherer = 0;
        soldier = 0;

        if (ConfigFile.ants != null) {
            for (Ant a : ConfigFile.ants) {
                if (a.antType() == 0)
                    builder += 1;
                else if (a.antType() == 1)
                    gatherer += 1;
            }
        }
    }

    public boolean inBounds(int testX, int testY) {
        return testY >= 0 && testY < width && testX >= 0
                && testX < height
                && tiles[testX][testY] != null;
    }

    public void createMap(float xRange, float yRange, Hexagon hexagon) {
        int row = 0;
        int x = 0;
        int y = 0;
        // Draw the tile
        for (float j = -(xRange); j < xRange; j += .05f) { // use
            // of
            // switch
            // to
            // stagger
            // hexagons
            switch (row) {
                case (0):
                    for (float i = -yRange; i < yRange; i += .05f) {
                        hexagon.setLocation(new Point3D(i, (hexagon.height / 2f), j));
                        tiles[x][y] = new Tile(
                                hexagon.getLocation(), x, y);

                        /**
                         * if (positionX == 0) {// create surface hexagon.setLocation(new
                         * hexagon.setLocation(new Point3D(i - .050255f /
                         * ASEngine.scale, (hexagon.height / 2f), -xRange - (xRange
                         * / 10)));
                         *
                         * ASEngine.board.surfaceTiles[0][positionY] = new Tile(
                         * hexagon.getLocation3D(), 0, positionY);
                         *
                         * hexagon.setLocation(new Point3D(i, (hexagon.height / 2f),
                         * -xRange - (xRange / 10) * 2));
                         *
                         * ASEngine.board.surfaceTiles[1][positionY] = new Tile(
                         * hexagon.getLocation3D(), 1, positionY); }
                         **/

                        y++;
                    }
                    row = 1;
                    break;
                case (1):
                    for (float i = -yRange; i < yRange; i += .1f / ConfigFile.scale) {
                        hexagon.setLocation(new Point3D(
                                i + .050255f / ConfigFile.scale,
                                (hexagon.height / 2f), j));
                        tiles[x][y] = new Tile(
                                hexagon.getLocation(), x, y);
                        y++;

                    }
                    row = 0;
                    break;
            }
            y = 0;
            x++;

        }
        Log.d("Board", "final values" + x);
    }

}
