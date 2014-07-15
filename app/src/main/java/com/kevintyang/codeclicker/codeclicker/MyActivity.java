package com.kevintyang.codeclicker.codeclicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MyActivity extends Activity {

    //private variables to avoid creating additional objects
    private ImageView mCodeButton;
    private ImageView mUpgradeButton;
    private ImageView mSellButton;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;


    final static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar. Eventually I need to the learn the 4.4 call to get Immersion Mode (no On Screen Navs
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.fragment_code);

        //new shit
        setContentView(R.layout.activity_my);

        //Sets screen orientation to Portrait. We are also changing orientation in the xml, I might move it to the manifest . .
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


       // tick.start();
        //starts counting the code per second/money per second

        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        handler.postDelayed(runnable, 300);

    }

    Runnable runnable = new Runnable() {

        public int timeCheck = 0;

        @Override
        public void run() {

            if(timeCheck >=990) {
                CodeCounters.addCodePerSecondValue();
                MoneyCounters.addMoneyPerSecondValue();
                timeCheck = 0;
            }
            timeCheck = timeCheck + 300;
            updateCodeTextView();
            updateSellTextView();
            handler.postDelayed(this, 300);


        }
    };


    //ViewPagerStuff
    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    } */


  /* Placeholder containing a simple view
    public static class PlaceholderFragment extends Fragment {
        // fragment representing placeholder
        private static final String ARG_SECTION_NUMBER = "section_number";

       // returns new instance of this fragment
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            return rootView;
        }
    }*/

    //end viewpager stuff

  

    @SuppressLint("NewApi")
    protected void onResume(){
        super.onResume();


        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 300);
        // Check what version of android they are running.
        // If they are running Kit Kat . . .
        if (Build.VERSION.SDK_INT >= 19) {

            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            this.findViewById(android.R.id.content).setSystemUiVisibility(flags);
        }

        Log.d("Immersive Mode Activity", "onResume Called!");
    }


    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);

    }


    public void onPause(){
        super.onPause();
        handler.removeCallbacks(runnable);

    }

    public void updateCodeTextView() {
        TextView textView = (TextView) findViewById(R.id.codeTextView);
        textView.setText(CodeCounters.getCurrentStr());

        TextView codeTextView =(TextView) findViewById(R.id.codeTextViewSellScreen);
        codeTextView.setText(CodeCounters.getCurrentStr());
    }

    public void updateSellTextView(){
        TextView textView = (TextView) findViewById(R.id.cashTextView);
        textView.setText(MoneyCounters.getCurrentStr());

        TextView sellTextview = (TextView) findViewById(R.id.cashTextViewSellScreen);
        sellTextview.setText(MoneyCounters.getCurrentStr());
    }

    private void sendMessage() {
        Intent intent = new Intent(MyActivity.this, UpgradeScreen.class);
        startActivity(intent);

    }

    public void animateUpgradeButton() {
        mUpgradeButton = (ImageView)findViewById(R.id.upgradesButton);
        mUpgradeButton.setImageResource(R.drawable.upgrade_click);
        AnimationDrawable buttonPress = (AnimationDrawable) mUpgradeButton.getDrawable();
        if(buttonPress.isRunning()) {
            buttonPress.stop();
        }
        buttonPress.start();
    }
    
    public void upgradeClick(View v){
        animateUpgradeButton();
        sendMessage();
    }



    public void codeClick(View v){
        animateCodeButton();
        CodeCounters.codeClick();
        updateCodeTextView();
        Vibrator z = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        z.vibrate(30);
    }


    public void sellClick(View v){
        animateSellButton();
        MoneyCounters.sellClick();
        updateSellTextView();
        Vibrator g = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        g.vibrate(30);
    }



    public void animateCodeButton() {
        mCodeButton = (ImageView)findViewById(R.id.keyboardButton);
        mCodeButton.setImageResource(R.drawable.keyboard_click);
        AnimationDrawable buttonPress = (AnimationDrawable) mCodeButton.getDrawable();
        if(buttonPress.isRunning()) {
            buttonPress.stop();
        }
        buttonPress.start();
    }

    public void animateSellButton() {
        mSellButton = (ImageView)findViewById(R.id.sellButton);
        mSellButton.setImageResource(R.drawable.briefcase_click);
        AnimationDrawable buttonPress = (AnimationDrawable) mSellButton.getDrawable();
        if(buttonPress.isRunning()) {
            buttonPress.stop();
        }
        buttonPress.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private Runnable resetImmersive = new Runnable(){

        @SuppressLint("NewApi")
        @Override
        public void run() {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }

    };

    private Handler mHandler = new Handler();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (Build.VERSION.SDK_INT >= 19){
            if (keyCode == KeyEvent.KEYCODE_BACK){
                finish();
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                mHandler.postDelayed(resetImmersive, 450);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
