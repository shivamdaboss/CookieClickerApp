package com.example.shivamthelordalmighty.cookieclickerapp;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    TextView countView;
    TextView grannyPriceView;
    TextView cookiePriceView;
    AtomicInteger score;
    ConstraintLayout l;
    ImageView i;
    int grandmaCount;
    int grannyPrice;
    int grannyCPS;
    ImageView grannyMenu;
    TextView grannyView;

    ImageView cookieMachine;
    TextView cookieView;
    int cookieMachineCount;
    int cookiePrice;
    int cookieCPS;

    TextView CPSView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        grannyPriceView = findViewById(R.id.grannyPriceView);
        cookiePriceView = findViewById(R.id.cookiePriceView);
        CPSView = findViewById(R.id.cps);

        final TranslateAnimation slideIn = new TranslateAnimation(700.0f, 0.0f,0.0f, 0.0f);
        slideIn.setDuration(600);


        score = new AtomicInteger(0);
        i = findViewById(R.id.cookie);
        countView = findViewById(R.id.CookieCount);
        l = findViewById(R.id.yo);
        grandmaCount = 0;
        grannyPrice = 100;
        grannyCPS = 10;
        cookieMachineCount = 0;
        cookiePrice = 200;
        cookieCPS = 20;

        grannyMenu = findViewById(R.id.grannyMenu);
        grannyView = findViewById(R.id.GrannyTextView);
        grannyMenu.setImageResource(R.drawable.grandma);
        cookieMachine = findViewById(R.id.cookieMachineImage);
        cookieView = findViewById(R.id.cookieText);
        cookieMachine.setImageResource(R.drawable.factory);

        CPSView.setText("CPS: " + (grandmaCount * grannyCPS + cookieMachineCount * cookieCPS));

        cookiePriceView.setText("Price: " + cookiePrice);
        grannyPriceView.setText("Price: " + grannyPrice);

        grannyMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grandmaCount++;
                scoreUpdate(-grannyPrice);
                grannyPrice += 10;
                grannyPriceView.setText("Price: " + grannyPrice);
                Upgrade x = new Upgrade(new ImageView(v.getContext()), R.drawable.grandma);
                ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                x.getBody().setLayoutParams(p);
                x.getBody().setScaleX(0.33f);
                x.getBody().setScaleY(0.33f);
                x.getBody().setElevation(2f);

                l.addView(x.getBody());

                ConstraintSet cs = new ConstraintSet();
                cs.clone(l);
                cs.connect(x.getBody().getId(),cs.TOP, i.getId(), cs.TOP );
                cs.connect(x.getBody().getId(),cs.BOTTOM, l.getId(), cs.BOTTOM );
                cs.connect(x.getBody().getId(),cs.RIGHT, l.getId(), cs.RIGHT );
                cs.connect(x.getBody().getId(),cs.LEFT, l.getId(), cs.LEFT );
                cs.setVerticalBias(x.getBody().getId(), .5f);
                cs.setHorizontalBias(x.getBody().getId(), (float) ((Math.random())));
                cs.applyTo(l);

                x.getBody().startAnimation(slideIn);
                CPSView.setText("CPS: " + (grandmaCount * grannyCPS + cookieMachineCount * cookieCPS));

            }
        });


        cookieMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cookieMachineCount++;
                scoreUpdate(-cookiePrice);
                cookiePrice += 10;
                cookiePriceView.setText("Price: " + cookiePrice);
                Upgrade x = new Upgrade(new ImageView(v.getContext()), R.drawable.factory);
                ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                x.getBody().setLayoutParams(p);
                x.getBody().setScaleX(0.33f);
                x.getBody().setScaleY(0.33f);

                l.addView(x.getBody());

                ConstraintSet cs = new ConstraintSet();
                cs.clone(l);
                cs.connect(x.getBody().getId(),cs.TOP, i.getId(), cs.BOTTOM );
                cs.connect(x.getBody().getId(),cs.BOTTOM, l.getId(), cs.BOTTOM );
                cs.connect(x.getBody().getId(),cs.RIGHT, l.getId(), cs.RIGHT );
                cs.connect(x.getBody().getId(),cs.LEFT, l.getId(), cs.LEFT );
                cs.setVerticalBias(x.getBody().getId(), 10f);
                cs.setHorizontalBias(x.getBody().getId(), (float) ((Math.random())));
                cs.applyTo(l);

                x.getBody().startAnimation(slideIn);
                CPSView.setText("CPS: " + (grandmaCount * grannyCPS + cookieMachineCount * cookieCPS));
            }
        });

        ScoreThread t = new ScoreThread();
        t.start();


        Log.d("TAG", "CHECKPOINT 2");
        //anims
        final ScaleAnimation cookieEnlargeAnim = new ScaleAnimation(1f, 1.1f, 1f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        cookieEnlargeAnim.setDuration(50);
        final ScaleAnimation cookieShrinkAnim  = new ScaleAnimation(1.1f, 1f, 1.1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, .5f);
        cookieShrinkAnim.setDuration(50);

        final TranslateAnimation plusOne = new TranslateAnimation(0.0f, 0.0f,0.0f, 200.0f);
        plusOne.setDuration(200);

        i.setClickable(true);
        i.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scoreUpdate(1);
                countView.setText("Cookies: " + score);
                i.startAnimation(cookieEnlargeAnim);
                i.startAnimation(cookieShrinkAnim);

                plusOne();

            }
        });

        Log.d("TAG", "CHECKPOINT 3");
    }

    public void plusOne(){
        TextView t = new TextView(this);
        t.setId(View.generateViewId());
        t.setText("+1");
        t.setTextColor(Color.RED);
        t.setElevation(2f);
        t.setTextSize(40f);

        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        t.setLayoutParams(p);

        l.addView(t);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(l);
        cs.connect(t.getId(),cs.TOP, l.getId(), cs.TOP );
        cs.connect(t.getId(),cs.BOTTOM, l.getId(), cs.BOTTOM );
        cs.connect(t.getId(),cs.RIGHT, l.getId(), cs.RIGHT );
        cs.connect(t.getId(),cs.LEFT, l.getId(), cs.LEFT );
        cs.setVerticalBias(t.getId(), 0.4f);
        cs.setHorizontalBias(t.getId(), (float) ((Math.random() * 0.5) + 0.25));
        cs.applyTo(l);


        final TranslateAnimation moving = new TranslateAnimation(0.0f, 0.0f,0.0f, -200.0f);
        moving.setDuration(1000);
        AnimationSet animSet = new AnimationSet(true);
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        AlphaAnimation fadeOut = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(500);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(500);
        animSet.addAnimation(moving);
        animSet.addAnimation(fadeOut);

        t.startAnimation(moving);
        t.setVisibility(View.INVISIBLE);
    }



    public void scoreUpdate(int income){
        score.getAndAdd(income);
        if(score.get() >= grannyPrice){
            grannyMenu.setClickable(true);
            grannyMenu.setAlpha(255);
            grannyView.setAlpha(1);
            grannyPriceView.setAlpha(1);
        }
        else{
            grannyMenu.setAlpha(150);
            grannyView.setAlpha(.5f);
            grannyPriceView.setAlpha(.5f);
            grannyMenu.setClickable(false);
        }

        if(score.get() >= cookiePrice){

            cookieMachine.setClickable(true);

            cookieMachine.setAlpha(255);
            cookieView.setAlpha(1f);
            cookiePriceView.setAlpha(1f);
        }
        else{

            cookieMachine.setClickable(false);
            cookieMachine.setAlpha(150);
            cookieView.setAlpha(.5f);
            cookiePriceView.setAlpha(.5f);
        }



    }

    public class Upgrade  {

        private ImageView body;

        public Upgrade(ImageView a, int id) {
            body = a;
            body.setId(View.generateViewId());
            body.setImageResource(id);
        }

        public ImageView getBody() {
            return body;
        }

    }

    public class ScoreThread extends Thread{
        ScoreThread(){

        }

        @Override
        public void run() {
            while(true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scoreUpdate(grandmaCount * grannyCPS);
                        scoreUpdate( cookieMachineCount * cookieCPS);

                        countView.setText("Cookies: " + score.get());
                    }
                });
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
