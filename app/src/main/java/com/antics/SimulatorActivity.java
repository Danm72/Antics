package com.antics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;

import com.antics.util.ConfigFile;
import com.antics.util.Music;
import com.antics.util.MusicConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimulatorActivity extends AppCompatActivity {
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    String[] mPlanetTitles;
    @BindView(R.id.seekBar1)
    SeekBar seek;
    @BindView(R.id.upArrow)
    ImageButton up;
    @BindView(R.id.downArrow)
    ImageButton down;
    @BindView(R.id.leftArrow)
    ImageButton left;
    @BindView(R.id.rightArrow)
    ImageButton right;
    @BindView(R.id.homebutton1)
    ImageButton home;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.left_drawer)
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private int MUSIC_GAME = 1;
    private Boolean continueMusic = false;
    private String TAG = "GameFrag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_activity);
        ButterKnife.bind(this);

        // setTitle(R.string.attach);
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

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigFile.axisZ -= .1f;
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfigFile.axisZ += .1f;
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfigFile.axisX = 0f;
                ConfigFile.axisY = 7f;
                ConfigFile.axisZ = .05f;
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfigFile.axisX -= .25f;
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigFile.axisX += .25f;
            }
        });

        // configure the SlidingMenu
//        menu = new SlidingMenu(this);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.content_frame);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content_frame, new StatisticsFragment()).commit();

        if (ConfigFile.rendererCreated == false) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            // FrameLayout f = (Rel) findViewById(R.id.instructions);
            dialogBuilder.setCancelable(false).setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            /*View dialoglayout = inflater.inflate(R.layout.instructions,
                    (ViewGroup) getCurrentFocus());*/

            AlertDialog alertDialog = dialogBuilder.create();
//            alertDialog.setView(dialoglayout);
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

  /*  @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.game_activity, container, false);
        ButterKnife.bind(this, view);
        initialiseViews();

        return view;
    }*/


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!continueMusic) {
            Music.pause();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        continueMusic = false;
        Music.start(this, Music.MUSIC_MENU);
    }

    void initialiseViews() {

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_item, mPlanetTitles));

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }
}
