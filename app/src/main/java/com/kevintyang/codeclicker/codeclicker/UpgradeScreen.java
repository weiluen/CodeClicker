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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by jameshuang on 7/11/14.
 */
public class UpgradeScreen extends Activity{
    private ImageView mExitButton;


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //Remove title bar. Eventually I need to the learn the 4.4 call to get Immersion Mode (no On Screen Navs
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.fragment_code);


        //Sets screen orientation to Portrait. We are also changing orientation in the xml, I might move it to the manifest . .
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.screen_upgrade);


    }

    private void sendMessage() {
        Intent intent = new Intent(UpgradeScreen.this, MyActivity.class);
        startActivity(intent);
    }

    public void animateUpgradeButton() {
        mExitButton = (ImageView)findViewById(R.id.exit_button);
        mExitButton.setImageResource(R.drawable.exit_click);
        AnimationDrawable buttonPress = (AnimationDrawable) mExitButton.getDrawable();
        if(buttonPress.isRunning()) {
            buttonPress.stop();
        }
        buttonPress.start();
    }

    public void exitClick(View v) {
        animateUpgradeButton();
        sendMessage();
    }

    protected void onResume(){
        super.onResume();

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

    public void upgradeCCPunchCard(View v){
        CapacityUpgrades.buyPunchCard();
        vibrate();
    }

    public void upgradeMCPockets(View v) {
        CapacityUpgrades.buyPockets();
        vibrate();

    }

    public void codeClickUpgrade(View v){
        UpgradeObjects.buyCodeClickUpgrades();
        vibrate();

    }

    public void moneyClickUpgrade(View v){
        UpgradeObjects.buyMoneyClickUpgrades();
        vibrate();
    }

    public void vibrate(){
        final Vibrator z = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        z.vibrate(20);
    }
}
