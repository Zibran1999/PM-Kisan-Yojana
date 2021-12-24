package com.pmkisanyojana.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.adapters.StatusViewsAdapter;
import com.pmkisanyojana.models.ModelFactory;
import com.pmkisanyojana.models.StatusViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String userImage, userName, statusImage, time, status;
    BottomSheetBehavior sheetBehavior;
    MaterialCardView seenByCard;
    PageViewModel pageViewModel;
    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private int counter = 0;
    List<StatusViewModel> statusViewModels = new ArrayList<>();
    RecyclerView statusViewsRV;
    StatusViewsAdapter statusViewsAdapter;
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
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    storiesProgressView.resume();

                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @SuppressLint("SetTextI18n")
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

//         storiesProgressView.setStoriesCountWithDurations(duration);

        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();

        Paper.init(this);
        image = (ImageView) findViewById(R.id.image);
        UserName = findViewById(R.id.textView);
        Time = findViewById(R.id.textView2);
        circleImageView = findViewById(R.id.circleImageView);
        seenBy = findViewById(R.id.seenBy);
        seenByCard = findViewById(R.id.seenByCard);
        sheetBehavior = BottomSheetBehavior.from(seenByCard);


        seenBy.setOnClickListener(v -> {
            seenByCard.setVisibility(View.VISIBLE);
            sheetBehavior.setPeekHeight(seenByCard.getHeight());
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            pressTime = System.currentTimeMillis();
            storiesProgressView.pause();
        });

        status = getIntent().getStringExtra("status");
        userImage = getIntent().getStringExtra("userImage");
        userName = getIntent().getStringExtra("userName");
        statusImage = getIntent().getStringExtra("statusImage");
        time = getIntent().getStringExtra("time");

        if (status.equals("MyStatus")) {
            TextView textView = findViewById(R.id.viewedTV);
            statusViewsRV = findViewById(R.id.viewedByRV);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            statusViewsRV.setLayoutManager(layoutManager);
            statusViewsAdapter = new StatusViewsAdapter();
            statusViewsRV.setAdapter(statusViewsAdapter);
            Map<String, String> map = new HashMap<>();
            map.put("statusId", getIntent().getStringExtra("statusId"));
            pageViewModel = new ViewModelProvider(this, new ModelFactory(this.getApplication(), map)).get(PageViewModel.class);
            pageViewModel.fetchStatusViews().observe(this, statusViewModelList -> {
                if (!statusViewModelList.getData().isEmpty()) {
                    statusViewModels.clear();
                    Log.d("statusView",statusViewModelList.getData().get(1).getProfileName()+" "+getIntent().getStringExtra("statusId"));
                    seenBy.setText(""+statusViewModelList.getData().size());
                    textView.setText("Viewed  "+statusViewModelList.getData().size());
                    statusViewModels.addAll(statusViewModelList.getData());
                    Collections.reverse(statusViewModels);
                    statusViewsAdapter.updateStatusViewsList(statusViewModels);


                }else {
                    seenByCard.setVisibility(View.GONE);
                }
            });

        }


        if (status.equals("allStatus")) {
            seenBy.setVisibility(View.GONE);
        }
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