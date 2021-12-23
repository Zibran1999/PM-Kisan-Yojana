package com.pmkisanyojana.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.pmkisanyojana.R;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class ShowStatusActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {


    private static final int PROGRESS_COUNT = 1;
    private final long duration = 500L;
    long pressTime = 0L;
    long limit = 500L;
    TextView UserName, Time, seenBy;
    CircleImageView circleImageView;
    String userImage, userName, statusImage, time;
    BottomSheetBehavior sheetBehavior;
    MaterialCardView seenByCard;
    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private int counter = 0;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_status);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(3000L);
        // or
        // storiesProgressView.setStoriesCountWithDurations(durations);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();

        Paper.init(this);
        image = (ImageView) findViewById(R.id.image);
        UserName = findViewById(R.id.textView);
        Time = findViewById(R.id.textView2);
        circleImageView = findViewById(R.id.circleImageView);
        seenBy = findViewById(R.id.seenBy);
        seenByCard = findViewById(R.id.seenByCard);
        sheetBehavior =BottomSheetBehavior.from(seenByCard);

        seenBy.setOnClickListener(v -> {
            seenByCard.setVisibility(View.VISIBLE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            pressTime = System.currentTimeMillis();
            storiesProgressView.pause();
        });


        userImage = getIntent().getStringExtra("userImage");
        userName = getIntent().getStringExtra("userName");
        statusImage = getIntent().getStringExtra("statusImage");
        time = getIntent().getStringExtra("time");

        UserName.setText(userName);
        Time.setText(time);
        Log.d("lddfdfd", userName + " " + time + " " + userImage + " " + statusImage);
        Glide.with(this).load("https://gedgetsworld.in/PM_Kisan_Yojana/User_Profile_Images/" + userImage).into(circleImageView);

        Glide.with(this).load("https://gedgetsworld.in/PM_Kisan_Yojana/User_Status_Images/" + statusImage).into(image);


        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onNext() {
//        image.setImageResource(resources[++counter]);
    }

    @Override
    public void onPrev() {
//        if ((counter - 1) < 0) return;
//        image.setImageResource(resources[--counter]);
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }


}