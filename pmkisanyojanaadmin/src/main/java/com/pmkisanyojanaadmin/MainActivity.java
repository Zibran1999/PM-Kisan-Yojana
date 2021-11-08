package com.pmkisanyojanaadmin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.pmkisanyojanaadmin.databinding.ActivityMainBinding;
import com.pmkisanyojanaadmin.model.ApiInterface;
import com.pmkisanyojanaadmin.model.ApiWebServices;
import com.pmkisanyojanaadmin.model.YojanaModel;
import com.pmkisanyojanaadmin.model.YojanaModelList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Button yojnaBtn, newsBtn, cancelBtn, uploadBtn,addYojanaBtn,cancelYoajanBtn,uploadYojanaBtn;
    Dialog uploadDialog ,adYojanaDialog;
    RadioButton immediateBtn, scheduleBtn;
    RadioGroup radioGroup;
    LinearLayout scheduleLayout;
    TextView dialogTitle,dialogTitle2;
    TextView setDate, setTime;
    ImageView selectImage;
    EditText selectTitle,yojanaData,yojanaLink;
    AppCompatAutoCompleteTextView appCompatAutoCompleteTextView;
    List<YojanaModel> yojanaModelList;
    List<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    String getYojanaName;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        yojnaBtn = binding.yojanaBtn;
        newsBtn = binding.newsBtn;
        addYojanaBtn = binding.addYojanaBtn;
        radioGroup = findViewById(R.id.radio_group);
        immediateBtn = findViewById(R.id.immediate);
        scheduleBtn = findViewById(R.id.schedule);
        yojanaModelList = new ArrayList<>();
        arrayList = new ArrayList<>();
        apiInterface = ApiWebServices.getApiInterface();
        fetchYojanaDetails();
        arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        yojnaBtn.setOnClickListener(v -> {
            showYojanaUploadDialog(this, "Upload Yojana");
        });
        newsBtn.setOnClickListener(v -> {
            showYojanaUploadDialog(this, "Upload News");
        });
        addYojanaBtn.setOnClickListener(v -> {
            addYojanaData(this,"Add Yojana");
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    private void showYojanaUploadDialog(Context context, String title) {
        uploadDialog = new Dialog(context);
        uploadDialog.setContentView(R.layout.upload_dialog);
        uploadDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        uploadDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        uploadDialog.setCancelable(false);
        uploadDialog.show();
        dialogTitle = uploadDialog.findViewById(R.id.dialog_title);
        selectImage = uploadDialog.findViewById(R.id.selectImage);
        selectTitle = uploadDialog.findViewById(R.id.item_title);
        scheduleLayout = uploadDialog.findViewById(R.id.schedule_layout);
        setDate = uploadDialog.findViewById(R.id.select_date);
        setTime = uploadDialog.findViewById(R.id.select_time);
        cancelBtn = uploadDialog.findViewById(R.id.cancel_btn);
        uploadBtn = uploadDialog.findViewById(R.id.upload_btn);
        radioGroup = uploadDialog.findViewById(R.id.radio_group);
        cancelBtn.setOnClickListener(v -> uploadDialog.dismiss());
        dialogTitle.setText(title);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int buttonId = group.getCheckedRadioButtonId();
            switch (buttonId) {
                case R.id.immediate:
                    scheduleLayout.setVisibility(View.GONE);
                    break;
                case R.id.schedule:
                    scheduleLayout.setVisibility(View.VISIBLE);
                    break;
            }
        });

        Calendar myCalendar = Calendar.getInstance();
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        int year = myCalendar.get(Calendar.YEAR);
        setDate.setOnClickListener(v -> {
            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this, (view, year1, month1, dayOfMonth) ->
                    setDate.setText(dayOfMonth + " - " + (month1 + 1) + " - " + year1), year, month, day);
            datePickerDialog.show();
        });

        int mHour = 0, mMinute = 0;
        setTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    MainActivity.this, (TimePickerDialog.OnTimeSetListener) (view, hourOfDay, minute) -> {
                setTime.setText(hourOfDay + ":" + minute);
            }, mHour, mMinute, false);
            timePickerDialog.show();
        });
    }

    public void addYojanaData(Context context,String title){
        adYojanaDialog = new Dialog(context);
        adYojanaDialog.setContentView(R.layout.yojna_item_layout);
        adYojanaDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        adYojanaDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        adYojanaDialog.setCancelable(false);
        adYojanaDialog.show();
        dialogTitle2 = findViewById(R.id.dialog_title2);
        dialogTitle2.setText(title);
        appCompatAutoCompleteTextView = findViewById(R.id.drop_down_text);
        yojanaData = findViewById(R.id.yojana_data);
        yojanaLink = findViewById(R.id.yojana_link);
        cancelYoajanBtn = findViewById(R.id.cancel_yojana_btn);
        uploadYojanaBtn = findViewById(R.id.upload_yojana_btn);

        appCompatAutoCompleteTextView.setInputType(0);
        appCompatAutoCompleteTextView.setAdapter(arrayAdapter);
        appCompatAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            getYojanaName = arrayList.get(position);
            Toast.makeText(MainActivity.this, getYojanaName, Toast.LENGTH_SHORT).show();
        });

    }

    public void fetchYojanaDetails(){
        Call<YojanaModelList> call = apiInterface.getAllYojana();
        call.enqueue(new Callback<YojanaModelList>() {
            @Override
            public void onResponse(@NonNull Call<YojanaModelList> call, @NonNull Response<YojanaModelList> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    yojanaModelList.addAll(response.body().getData());

                    for(int i=0;i<yojanaModelList.size();i++){
                        arrayList.add(yojanaModelList.get(i).getYojanaTitle());
                    }
                }else {
                    Log.d("onResponse",response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<YojanaModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error",t.getMessage());

            }
        });
    }
}