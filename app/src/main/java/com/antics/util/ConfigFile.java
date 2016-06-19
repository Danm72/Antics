package com.antics.util;

import android.content.Context;
import android.view.Display;

import com.antics.R;
import com.antics.StatisticsFragment.SampleAdapter;
import com.antics.ant.Ant;
import com.antics.data.Board;

import java.util.LinkedList;

public class ConfigFile {
    public static final int scale = 2;
    public static final int GAME_THREAD_DELAY = 0;
    public static final int BYTES_PER_FLOAT = 4;
    public static final int[] flowers = {R.drawable.flower,
            R.drawable.flower2, R.drawable.reed, R.drawable.grass2};
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final Board board = new Board(HEIGHT, WIDTH);
    // Nest co-ordinates
    public static final int NEST_X = 25;
    public static final int NEST_Y = 25;
    public static SampleAdapter adapter = null;
    public static long drawSpeed = 800;
    public static Context context;
    public static LinkedList<Ant> ants = new LinkedList<Ant>(); // House all the
    // ants
    public static float axisX = -.25f;
    public static float axisY = 3f;
    public static float axisZ = .05f;
    // public static HashMap<String,String> tileArray = new
    // HashMap<String,String>(1000);
    // 2d array of tiles, tiles contain items like dirt, resource,ant
    public static Display display;// = wm.getDefaultDisplay();
    public static boolean rendererCreated = false;

    public synchronized static SampleAdapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(SampleAdapter adapter) {
        ConfigFile.adapter = adapter;
    }

    public static Boolean onExit() {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // >:D
    public static void resetAntLocation() {
        // Place ants back at spawn
        for (int i = 1; i < ants.size(); i++) {
            ants.get(i).reset();
            /*
			 * ants.get(i).die(); ants.remove(i); i--;
			 */
        }

        // Remove pheromones
        for (int i = 0; i < board.tiles.length; i++) {
            for (int j = 0; j < board.tiles[i].length; j++) {
                if (board.tiles[i][j] != null)
                    board.tiles[i][j].reset();
            }
        }

    }

    // Check if within bounds of world
    public static boolean inBounds(int testX, int testY) {
        return testY >= 0 && testY < ConfigFile.WIDTH && testX >= 0
                && testX < ConfigFile.HEIGHT
                && ConfigFile.board.tiles[testX][testY] != null;
    }

}
