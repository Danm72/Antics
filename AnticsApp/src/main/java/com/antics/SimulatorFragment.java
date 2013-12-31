package com.antics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.antics.util.ConfigFile;
import com.antics.util.Music;
import com.antics.util.MusicConfig;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity
public class SimulatorFragment extends SherlockFragmentActivity {
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    String[] mPlanetTitles;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @ViewById(R.id.seekBar1)
    SeekBar seek;
    @ViewById(R.id.upArrow)
    ImageButton up;
    @ViewById(R.id.downArrow)
    ImageButton down;
    @ViewById(R.id.leftArrow)
    ImageButton left;
    @ViewById(R.id.rightArrow)
    ImageButton right;
    @ViewById(R.id.homebutton1)
    ImageButton home;
    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @ViewById(R.id.left_drawer)
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;


    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;
    private int MUSIC_GAME = 1;
    private Boolean continueMusic = false;
    private String TAG = "GameFrag";

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
//        if (menu.isMenuShowing()) {
//            menu.showContent();
//        } else {
//            super.onBackPressed();
//        }
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


    @AfterViews
    void initialiseViews() {

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_item, mPlanetTitles));

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
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
