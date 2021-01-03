package com.example.snakeandladder;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.HashMap;

public class MainActivity3 extends AppCompatActivity {
    AbsoluteLayout snake2;
    private int widthOfBlock,noOfBlock=10,widthOfScreen;

    ImageView redPos,dt,dice2;
    ObjectAnimator objectAnimator;
    ImageView users[] = new ImageView[2];
    private int currentPos0 =0, currentPos1 = 0;
    PlanePoints boardPoints[] = new PlanePoints[100];
    HashMap<Integer, Integer> boardPosition;
    static Path p0,p1;
    int blue = R.drawable.ic_blue;
    int red = R.drawable.ic_red;
    int diceView[] = {R.drawable.dice_1,R.drawable.dice_2,R.drawable.dice_3,R.drawable.dice_4,R.drawable.dice_5,R.drawable.dice_6};

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//        ca-app-pub-3940256099942544/1033173712    test ad
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                //Toast.makeText(MainActivity3.this,""+loadAdError,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(MainActivity3.this,MainActivity.class));
            }
        });


        dt = findViewById(R.id.diceTry);
        dt.setClickable(false);
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos1 =  makeNewMove(users[1],currentPos1,p1,dt);
            }
        });

        setBoardPosition();

        users[0].setX(widthOfBlock*boardPoints[currentPos0].getX());
        users[0].setY(widthOfBlock*boardPoints[currentPos0].getY());
        users[1].setX(widthOfBlock*boardPoints[currentPos1].getX());
        users[1].setY(widthOfBlock*boardPoints[currentPos1].getY());
        p0 = new Path();
        p0.moveTo(users[0].getX(),users[0].getY());
        p1 = new Path();
        p1.moveTo(users[0].getX(),users[0].getY());
        dice2 = findViewById(R.id.dice2);
        dice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos0 =  makeNewMove(users[0],currentPos0,p0,dice2);
            }
        });



    }

    private int makeNewMove(ImageView user,int currentPos,Path p,ImageView die) {
        int diceVal = (int) (Math.random()*6 + 1);

        die.setImageResource(diceView[diceVal-1]);
        if(diceVal != 1)
            if(die == dice2)
            {
                dice2.setClickable(false);
                dt.setClickable(true);
                dice2.setBackground(null);
                dt.setBackgroundResource(R.drawable.ic_blue);
            }
        else
            {
                dice2.setClickable(true);
                dice2.setBackgroundResource(R.drawable.ic_red);
                dt.setBackground(null);
                dt.setClickable(false);
            }
        if(currentPos+diceVal <= 99) {
            while (diceVal >= 1) {
                currentPos++;
                p.lineTo(widthOfBlock * boardPoints[currentPos].getX(), widthOfBlock * boardPoints[currentPos].getY());
                diceVal--;
            }
            p.lineTo(widthOfBlock * boardPoints[currentPos].getX(), widthOfBlock * boardPoints[currentPos].getY());
            if (boardPosition.containsKey(currentPos)) {
                currentPos = boardPosition.get(currentPos);
                p.lineTo(widthOfBlock * boardPoints[currentPos].getX(), widthOfBlock * boardPoints[currentPos].getY());
            }
            objectAnimator = ObjectAnimator.ofFloat(user, "x", "y", p);
            objectAnimator.setDuration(1000);
            objectAnimator.start();
            user.setX(widthOfBlock * boardPoints[currentPos].getX());
            user.setY(widthOfBlock * boardPoints[currentPos].getY());
            p.reset();
            p.moveTo(user.getX(), user.getY());
            if(currentPos==99) {
                if (user == users[0])
                    Toast.makeText(this,"Red user Wins.",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this,"Blue user Wins.",Toast.LENGTH_LONG).show();

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        }
    return currentPos;
    }

    private void setBoardPosition() {
        snake2 = findViewById(R.id.snakeAndLadder2);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen = displayMetrics.widthPixels;
        widthOfBlock = widthOfScreen/noOfBlock;
        snake2.getLayoutParams().width = widthOfScreen;
        snake2.getLayoutParams().height = widthOfScreen;
        for(int i=4;i>=0;i--)
        {
            for(int j=0;j<10;j++)
            {
                boardPoints[20*i+j] = new PlanePoints(j,2*(4-i)+1);
                boardPoints[20*i+19-j] = new PlanePoints(j,2*(4-i));
            }
        }


        users[0] = findViewById(R.id.user1);
        users[1] = findViewById(R.id.user2);
        users[0].getLayoutParams().width = widthOfBlock;
        users[0].getLayoutParams().height = widthOfBlock;
        users[0].setMaxHeight(widthOfBlock);
        users[0].setMaxWidth(widthOfBlock);
        users[0].setPadding(10,15,10,5);

        users[1].getLayoutParams().width = widthOfBlock;
        users[1].getLayoutParams().height = widthOfBlock;
        users[1].setMaxHeight(widthOfBlock);
        users[1].setMaxWidth(widthOfBlock);
        users[1].setPadding(10,15,10,5);


            boardPosition = new HashMap();
            //Ladder
            boardPosition.put(1,37);
            boardPosition.put(3,13);
            boardPosition.put(8,30);
            boardPosition.put(32,84);
            boardPosition.put(51,87);
            boardPosition.put(79,98);
            //Snake
            boardPosition.put(97,7);
            boardPosition.put(91,52);
            boardPosition.put(61,56);
            boardPosition.put(55,14);
            boardPosition.put(50,10);
    }



}