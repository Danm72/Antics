package com.antics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import com.antics.util.ConfigFile;
import com.antics.util.Music;
import com.antics.util.MusicConfig;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class SimulatorFragment extends RoboFragmentActivity {
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    @InjectView(R.id.seekBar1)
    SeekBar seek;
    @InjectView(R.id.upArrow)
    ImageButton up;
    @InjectView(R.id.downArrow)
    ImageButton down;
    @InjectView(R.id.leftArrow)
    ImageButton left;
    @InjectView(R.id.rightArrow)
    ImageButton right;
    @InjectView(R.id.homebutton1)
    ImageButton home;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;
    private int MUSIC_GAME = 1;
    private Boolean continueMusic = false;
    private String TAG = "GameFrag";
    private SlidingMenu menu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setTitle(R.string.attach);
        setContentView(R.layout.fragment_test);
        if (!MusicConfig.muteMusic)
            Music.start(this, MUSIC_GAME);


        seek.setMax(1000);
        seek.setProgress(500);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean arg2) {
                // TODO Auto-generated method stub
                if (10f - (progress / 100) >= 1.2f
                        && 10f - (progress / 100) <= 9f) {
                    ConfigFile.axisY = 10f - progress / 100;
                } else if (progress >= 900) {
                    ConfigFile.axisY = 1.2f;
                } else if (progress <= 100) {
                    ConfigFile.axisY = 9.5f;
                }


            }
        });

        up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigFile.axisZ -= .1f;
            }
        });

        down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfigFile.axisZ += .1f;
            }
        });

        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfigFile.axisX = 0f;
                ConfigFile.axisY = 7f;
                ConfigFile.axisZ = .05f;
            }
        });

        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfigFile.axisX -= .25f;
            }
        });

        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigFile.axisX += .25f;
            }
        });

        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new StatisticsFragment()).commit();

        if (ConfigFile.rendererCreated == false) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            // FrameLayout f = (Rel) findViewById(R.id.instructions);
            dialogBuilder.setCancelable(false).setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            LayoutInflater inflater = getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.instructions,
                    (ViewGroup) getCurrentFocus());

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setView(dialoglayout);
            // show it
            alertDialog.show();
            /*
			Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
			dialog.setContentView(R.layout.instructions); 
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			*/
        }
        // TextView detail = (TextView) findViewById(R.id.row_detail);
        // detail.setText(ASEngine.tiles[0][0].food()+"");
    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!continueMusic) {
            Music.pause();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        continueMusic = false;
        Music.start(this, Music.MUSIC_MENU);
    }

}
