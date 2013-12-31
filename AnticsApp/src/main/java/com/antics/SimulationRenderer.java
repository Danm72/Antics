package com.antics;

/**
 * This Class is used for drawing, it is used as the main game loop.
 * The Variable are initialised in 'OnScreenCreated'
 * The 'onDrawFrame' is the game loop, this runs several times a second
 * The draw frame draws the map, draws the characters and calls the action method on each sprite.
 */

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import com.antics.data.GameManager;
import com.antics.geometry.Point3D;
import com.antics.objects.CircleVector;
import com.antics.objects.Hexagon;
import com.antics.objects.SquareVector;
import com.antics.programs.ColorShaderProgram;
import com.antics.programs.TextureShaderProgram;
import com.antics.util.ConfigFile;
import com.antics.util.FrameRateConfig;
import com.antics.util.MatrixHelper;
import com.antics.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;

public class SimulationRenderer extends Activity implements Renderer {
    private final GameManager gameManager = new GameManager();
    private final Context context;
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    //    @Inject protected EventManager eventManager;
    //	@InjectResource(R.drawable.grassground)
    int grassBackGroundImage = R.drawable.grassground;
    //	@InjectResource(R.drawable.antsprites)
    int antSpritesImage = R.drawable.antsprites;
    //	@InjectResource(R.drawable.foreground)
    int tileBackGroundImage = R.drawable.tilebackground;
    //	@InjectResource(R.drawable.mainbackground)
    int backGroundImage = R.drawable.mainbackground;
    //	@InjectResource(R.drawable.tilebackground)
    int foreGroundImage = R.drawable.foreground;
    //	@InjectResource(R.drawable.termite)
    int TERMITEImage = R.drawable.termite;

    private String TAG = "ASRENDERER";
    private Map<String, Point3D> colourMap = createMapOfColours();
    private SquareVector background;
    private Hexagon hexagon;
    private SquareVector squareVector;
    private CircleVector circleVector;
    private SquareVector[] squareVectors = new SquareVector[6];
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int grassBackGround;
    private int backGroundTexture;
    private int mainBackGroundPicture;
    private int foreGroundTexture;
    private int grassTexture;
    private int antsprites;
    private int TERMITETexture;
    private int DIGGER = 0;
    private int GATHERER = 1;
    private int QUEEN = 2;
    private int SOLDIER = 3;
    private int GRASS = 4;
    private int TERMITE = 5;
    private int ant = 0;
    private int resource = 1;

    public SimulationRenderer(Context context) {
        this.context = context;
        FrameRateConfig.startTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // this creates the textures,
        // this must be done on first run and every time the screen loses focus.

        initialiseTextures();

        // if the program has not run yet
        gameManager.populateMap();
        for (int i = 0; i < 5; i++) {
            squareVectors[i] = new SquareVector(.05f / ConfigFile.scale,
                    0.02f / ConfigFile.scale, 4, i);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // this creates the camera if the screen is changed, which its not.
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 30, (float) width
                / (float) height, 1f, 10f);

		/*
         * setIdentityM(modelMatrix, 0); translateM(modelMatrix, 0, 0f, 0f,
		 * -2f);
		 */
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        // this is used to slow the drawing and actions down to visible amounts
        // this is essentially FPS,frames per section
        FrameRateConfig.endTime = System.currentTimeMillis();
        FrameRateConfig.dt = FrameRateConfig.endTime
                - FrameRateConfig.startTime;

        if (FrameRateConfig.dt < ConfigFile.drawSpeed)
            try {
                Thread.sleep(ConfigFile.drawSpeed - FrameRateConfig.dt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        FrameRateConfig.startTime = System.currentTimeMillis();

        glClear(GL_COLOR_BUFFER_BIT);

        // this multiples the matricies together to create a camera effect.
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        // Background is put in firstly
        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectionMatrix,
                mainBackGroundPicture);
        background.bindData(textureProgram);
        background.draw();
        // END Background

        drawHexagons(); // the tile and squareVectors are drawn depending on their
        // tile array values

        // the QUEEN has the first action

        gameManager.newTurn();

        // change the camera depending on control changes.
        setLookAtM(viewMatrix, 0, ConfigFile.axisX, ConfigFile.axisY,
                ConfigFile.axisZ, ConfigFile.axisX, 0f,
                ConfigFile.axisZ - .05f, 0f, 1f, 0f);

    }

    private Map<String, Point3D> createMapOfColours() {
        Map<String, Point3D> temporaryMap = new HashMap<String, Point3D>();
        temporaryMap.put("Red", new Point3D(1.0f, 0.0f, 0.0f));
        temporaryMap.put("Blue", new Point3D(0.0f, 0f, 1f));
        temporaryMap.put("Pink", new Point3D(1.0f, 0f, 1f));
        temporaryMap.put("Green", new Point3D(0.0f, 1f, 0f));
        temporaryMap.put("Orange", new Point3D(1.0f, .5f, 0f));

        return Collections.unmodifiableMap(temporaryMap);
    }

    private void initialiseTextures() {
        glClearColor(0f, 0f, 0f, 0f);

        background = new SquareVector(3.5f, 0.02f, 4, 4);
        squareVector = new SquareVector(.05f / ConfigFile.scale, 0.02f / ConfigFile.scale,
                4, 4);
        hexagon = new Hexagon(0.06455f / ConfigFile.scale, 0.0f, 6);
        circleVector = new CircleVector(.02f / ConfigFile.scale, .02f / ConfigFile.scale,
                32);

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);

        backGroundTexture = TextureHelper.loadTexture(context,
                tileBackGroundImage);

        mainBackGroundPicture = TextureHelper.loadTexture(context,
                backGroundImage);
        foreGroundTexture = TextureHelper.loadTexture(context, foreGroundImage);
        grassTexture = TextureHelper
                .loadTexture(context, ConfigFile.flowers[0]);
        antsprites = TextureHelper.loadTexture(context, antSpritesImage);
        grassBackGround = TextureHelper.loadTexture(context,
                grassBackGroundImage);
        TERMITETexture = TextureHelper.loadTexture(context, TERMITEImage);
    }

    private void drawHexagons() {
        for (int i = 0; i < ConfigFile.HEIGHT; i++) {
            for (int j = 0; j < ConfigFile.WIDTH; j++) {

                if (ConfigFile.board.tiles[i][j] != null) {
                    Point3D temp = ConfigFile.board.tiles[i][j].getLocation3D();
                    // if(!ASEngine.board.tiles[i][j].isHive()){

                    positionObjectInSceneHex(temp.x, temp.y, temp.z);
                    // if(i % 2 == 0)
                    textureProgram.useProgram();
                    textureProgram
                            .setUniforms(
                                    modelViewProjectionMatrix,
                                    chooseBackGroundGraphic(ConfigFile.board.tiles[i][j]
                                            .currentBackground()));
                    hexagon.bindData(textureProgram);
                    hexagon.draw();
                    if (ConfigFile.axisY > 3f
                            && ConfigFile.board.tiles[i][j].currentForeground() != -1)
                        zoomedOut(i, j, temp);

                        // if there is an object in the foreground
                    else if (chooseForeGroundGraphic(ConfigFile.board.tiles[i][j]
                            .currentForeground()) != -1) { // if its an item

                        positionObjectInScene(temp.x, temp.y, temp.z);
                        textureProgram
                                .setUniforms(
                                        modelViewProjectionMatrix,
                                        chooseForeGroundGraphic(ConfigFile.board.tiles[i][j]
                                                .currentForeground()));

                        /**
                         * choose sprite if the object is an ant 0 DIGGERArray,
                         * 1 GATHERERArray, 2 QUEENArray, 3 soldierArray, 4
                         * fullArray aka graphics;
                         */
                        if (ConfigFile.board.tiles[i][j].currentForeground() == ant) {
                            if (ConfigFile.board.tiles[i][j].recentAnt() == GATHERER) { // GATHERER
                                squareVectors[1].bindData(textureProgram);
                                squareVectors[1].draw();

                            } else if (ConfigFile.board.tiles[i][j].recentAnt() == DIGGER) {// DIGGER
                                squareVectors[0].bindData(textureProgram);
                                squareVectors[0].draw();

                            } else if (ConfigFile.board.tiles[i][j].recentAnt() == QUEEN) {// DIGGER
                                squareVectors[2].bindData(textureProgram);
                                squareVectors[2].draw();
                                Log.d(TAG, "Draw QUEEN");

                            } else if (ConfigFile.board.tiles[i][j].recentAnt() == TERMITE) {// DIGGER
                                textureProgram.setUniforms(
                                        modelViewProjectionMatrix,
                                        TERMITETexture);
                                squareVectors[4].bindData(textureProgram);
                                squareVectors[4].draw();
                            }

                        }
                        // if the object isn't an ant (resource etc..)
                        else {
                            squareVectors[4].bindData(textureProgram);
                            squareVectors[4].draw();
                        }
                    }

                } else
                    break; // loop;
            }
        }
    }

    private void drawColour(String colour) {
        Point3D colourValues = colourMap.get(colour);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix, colourValues.x,
                colourValues.y, colourValues.z);
        circleVector.bindData(colorProgram);
        circleVector.draw();

    }

    private int chooseBackGroundGraphic(int number) {
        switch (number) {
            case (0):
                return foreGroundTexture; // switched
            case (1):
                return backGroundTexture; // switched
            case (2):
                return grassBackGround;
            default:
                return -1;
        }
    }

    private void zoomedOut(int i, int j, Point3D p) {

        positionObjectInScene(p.x, p.y, p.z);

        if (ConfigFile.board.tiles[i][j].currentForeground() == 1)
            drawColour("Red");
        else if (ConfigFile.board.tiles[i][j].recentAnt() == DIGGER)
            drawColour("Blue");
        else if (ConfigFile.board.tiles[i][j].recentAnt() == GATHERER)
            drawColour("Green");
        else if (ConfigFile.board.tiles[i][j].recentAnt() == QUEEN)
            drawColour("Pink");
        else if (ConfigFile.board.tiles[i][j].recentAnt() == TERMITE)
            drawColour("Orange");

    }

    private int chooseForeGroundGraphic(int number) {
        switch (number) {
            case (0):
                return antsprites;
            case (1):
                return grassTexture;
            default:
                return -1;
        }
    }

    private void positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        translateM(modelMatrix, 0, -0.6f, 4f, -0f);
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, 135f, 0f, 1f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0,
                modelMatrix, 0);
    }

    // The mallets and the tile are positioned on the same plane as the table.
    private void positionObjectInSceneHex(float x, float y, float z) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);

        rotateM(modelMatrix, 0, 90f, 0f, 1f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0,
                modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix, 0);
        // rotateM(modelMatrix, 0, 45f, 0f, 0f, 0f);

        translateM(modelMatrix, 0, x, y, z);
        rotateM(modelMatrix, 0, 135f, 0f, 1f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0,
                modelMatrix, 0);
    }

}
