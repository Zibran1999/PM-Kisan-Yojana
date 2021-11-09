package com.pmkisanyojanaadmin.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.databinding.ActivityMainBinding;
import com.pmkisanyojanaadmin.model.ApiInterface;
import com.pmkisanyojanaadmin.model.ApiWebServices;
import com.pmkisanyojanaadmin.model.MessageModel;
import com.pmkisanyojanaadmin.model.NewsModel;
import com.pmkisanyojanaadmin.model.NewsModelList;
import com.pmkisanyojanaadmin.model.YojanaModel;
import com.pmkisanyojanaadmin.model.YojanaModelList;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Button yojnaBtn, newsBtn, cancelBtn, uploadBtn, addYojanaBtn, addNewsBtn, cancelYoajanBtn, uploadYojanaBtn,editAndDeleteBtn;
    Dialog uploadDialog, adYojanaDialog;
    RadioButton immediateBtn, scheduleBtn;
    RadioGroup radioGroup;
    LinearLayout scheduleLayout;
    TextView dialogTitle, dialogTitle2;
    TextView setDate, setTime;
    ImageView selectImage;
    EditText selectTitle, yojanaData, yojanaLink;
    AppCompatAutoCompleteTextView appCompatAutoCompleteTextView;
    List<YojanaModel> yojanaModelList = new ArrayList<>();
    List<NewsModel> newsModelList = new ArrayList<>();
    List<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String getYojanaName, yojanaId;
    ApiInterface apiInterface;
    String encodedImage;
    Uri uri;
    Bitmap bitmap;
    TextInputLayout textInputLayout;
    String selectTime = "", selectDate = "";
    Map<String, String> map = new HashMap<>();
    Dialog loadingDialog;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        yojnaBtn = binding.yojanaBtn;
        newsBtn = binding.newsBtn;
        addYojanaBtn = binding.addYojanaBtn;
        addNewsBtn = binding.addNewsBtn;
        radioGroup = findViewById(R.id.radio_group);
        immediateBtn = findViewById(R.id.immediate);
        scheduleBtn = findViewById(R.id.schedule);
        editAndDeleteBtn = binding.editDeleteBtn;

        //****Loading Dialog****/
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/

        apiInterface = ApiWebServices.getApiInterface();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);
        yojnaBtn.setOnClickListener(v -> {
            showYojanaUploadDialog(this, "Upload Yojana", "Kisan_Yojana");
        });
        newsBtn.setOnClickListener(v -> {
            showYojanaUploadDialog(this, "Upload News", "News_Data");
        });
        addYojanaBtn.setOnClickListener(v -> {
            arrayList.clear();
            fetchYojanaDetails();
            addYojanaData(this, "Add Yojana Preview");
        });
        addNewsBtn.setOnClickListener(v -> {
            arrayList.clear();
            fetchNewsDetails();
            addYojanaData(this, "Add News Preview");
        });
        editAndDeleteBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EditAndDeleteActivity.class));
        });
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    private void showYojanaUploadDialog(Context context, String title, String tableName) {
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
        textInputLayout = uploadDialog.findViewById(R.id.textInputLayout2);
        yojanaLink = uploadDialog.findViewById(R.id.yojana_link);

        if (title.equals("Upload Yojana")) {
            textInputLayout.setVisibility(View.VISIBLE);

        } else if (title.equals("Upload News")) {
            textInputLayout.setVisibility(View.GONE);

        }

        cancelBtn.setOnClickListener(v -> uploadDialog.dismiss());
        dialogTitle.setText(title);
        selectImage.setOnClickListener(v -> FileChooser(100));


        uploadBtn.setOnClickListener(v -> {
            loadingDialog.show();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            setDate.setText(formattedDate);

            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String stime = mdformat.format(c);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTimeFormate = new SimpleDateFormat("HH:mm");
            Date currentDate = null;
            try {
                currentDate = currentTimeFormate.parse(stime);
            } catch (ParseException e) {

                e.printStackTrace();
            }
            @SuppressLint("SimpleDateFormat") SimpleDateFormat CurrentfmtOut = new SimpleDateFormat("hh:mm aa");

            selectTime = CurrentfmtOut.format(currentDate);
            setTime.setText(selectTime);
            if (title.equals("Upload Yojana")) {
                String sTitle = selectTitle.getText().toString().trim();
                String url = yojanaLink.getText().toString();
                String date = setDate.getText().toString();
                String time = setTime.getText().toString();

                if (TextUtils.isEmpty(encodedImage)) {
                    loadingDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Please select an Image!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sTitle)) {
                    loadingDialog.dismiss();

                    selectTitle.setError("required field");
                } else if (TextUtils.isEmpty(url)) {
                    loadingDialog.dismiss();

                    yojanaLink.setError("required field");
                } else {
                    map.put("img", encodedImage);
                    map.put("title", sTitle);
                    map.put("time", time);
                    map.put("date", date);
                    map.put("url", url);
                    uploadYojana(map);
                }
            } else if (title.equals("Upload News")) {

                String sTitle = selectTitle.getText().toString().trim();
                String date = setDate.getText().toString();
                String time = setTime.getText().toString();

                if (TextUtils.isEmpty(encodedImage)) {
                    loadingDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Please select an Image!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sTitle)) {
                    loadingDialog.dismiss();

                    selectTitle.setError("required field");
                } else {
                    map.put("img", encodedImage);
                    map.put("title", sTitle);
                    map.put("time", time);
                    map.put("date", date);
                    uploadNewsData(map);
                }
            }
        });


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int buttonId = group.getCheckedRadioButtonId();
            switch (buttonId) {
                case R.id.immediate:
                    scheduleLayout.setVisibility(View.GONE);
                    uploadBtn.setOnClickListener(v -> {
                        loadingDialog.show();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        setDate.setText(formattedDate);

                        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        String stime = mdformat.format(c);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTimeFormate = new SimpleDateFormat("HH:mm");
                        Date currentDate = null;
                        try {
                            currentDate = currentTimeFormate.parse(stime);
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat CurrentfmtOut = new SimpleDateFormat("hh:mm aa");

                        selectTime = CurrentfmtOut.format(currentDate);
                        setTime.setText(selectTime);
                        if (title.equals("Upload Yojana")) {
                            String sTitle = selectTitle.getText().toString().trim();
                            String url = yojanaLink.getText().toString();
                            String date = setDate.getText().toString();
                            String time = setTime.getText().toString();

                            if (TextUtils.isEmpty(encodedImage)) {
                                loadingDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Please select an Image!", Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(sTitle)) {
                                loadingDialog.dismiss();

                                selectTitle.setError("required field");
                            } else if (TextUtils.isEmpty(url)) {
                                yojanaLink.setError("required field");
                                loadingDialog.dismiss();

                            } else {
                                map.put("img", encodedImage);
                                map.put("title", sTitle);
                                map.put("time", time);
                                map.put("date", date);
                                map.put("url", url);
                                uploadYojana(map);
                            }
                        } else if (title.equals("Upload News")) {

                            String sTitle = selectTitle.getText().toString().trim();
                            String date = setDate.getText().toString();
                            String time = setTime.getText().toString();

                            if (TextUtils.isEmpty(encodedImage)) {
                                loadingDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Please select an Image!", Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(sTitle)) {
                                loadingDialog.dismiss();
                                selectTitle.setError("required field");
                            } else if (TextUtils.isEmpty(selectDate)) {
                                loadingDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Please select Date!", Toast.LENGTH_SHORT).show();

                            } else if (TextUtils.isEmpty(selectTime)) {
                                loadingDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Please select Time!", Toast.LENGTH_SHORT).show();

                            } else {
                                map.put("img", encodedImage);
                                map.put("title", sTitle);
                                map.put("time", time);
                                map.put("date", date);
                                uploadNewsData(map);
                            }
                        }
                    });

                    break;
                case R.id.schedule:

                    scheduleLayout.setVisibility(View.VISIBLE);
                    setDate.setOnClickListener(v -> {
                        Calendar myCalendar = Calendar.getInstance();
                        int month = myCalendar.get(Calendar.MONTH);
                        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                        int year = myCalendar.get(Calendar.YEAR);
                        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(
                                MainActivity.this, (view, year1, month1, dayOfMonth) -> {
                            selectDate = dayOfMonth + " - " + (month1 + 1) + " - " + year1;
                            setDate.setText(selectDate);
                        }, year, month, day);

                        datePickerDialog.show();
                    });
                    setTime.setOnClickListener(v -> {
                        int mHour = 0, mMinute = 0;
                        @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(
                                MainActivity.this, (view, hourOfDay, minute) -> {
                            String time = hourOfDay + ":" + minute;

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                            Date date = null;
                            try {
                                date = fmt.parse(time);
                            } catch (ParseException e) {

                                e.printStackTrace();
                            }

                            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                            selectTime = fmtOut.format(date);
                            setTime.setText(selectTime);
                        }, mHour, mMinute, false);
                        timePickerDialog.show();
                    });

                    uploadBtn.setOnClickListener(v -> {
                        loadingDialog.show();
                        if (title.equals("Upload Yojana")) {
                            String sTitle = selectTitle.getText().toString().trim();
                            String url = yojanaLink.getText().toString();
                            String date = setDate.getText().toString();
                            String time = setTime.getText().toString();

                            if (TextUtils.isEmpty(encodedImage)) {
                                Toast.makeText(getApplicationContext(), "Please select an Image!", Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(sTitle)) {
                                selectTitle.setError("required field!");
                            } else if (TextUtils.isEmpty(url)) {
                                yojanaLink.setError("required field!");
                            } else if (TextUtils.isEmpty(selectDate)) {
                                loadingDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "Please select Date!", Toast.LENGTH_SHORT).show();

                            } else if (TextUtils.isEmpty(selectTime)) {
                                loadingDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Please select Time!", Toast.LENGTH_SHORT).show();

                            } else {
                                map.put("img", encodedImage);
                                map.put("title", sTitle);
                                map.put("time", time);
                                map.put("date", date);
                                map.put("url", url);
                                uploadYojana(map);
                            }
                        } else if (title.equals("Upload News")) {

                            String sTitle = selectTitle.getText().toString().trim();
                            String date = setDate.getText().toString();
                            String time = setTime.getText().toString();

                            if (TextUtils.isEmpty(encodedImage)) {
                                Toast.makeText(getApplicationContext(), "Please select an Image!", Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(sTitle)) {
                                selectTitle.setError("required field!");
                            } else {
                                map.put("img", encodedImage);
                                map.put("title", sTitle);
                                map.put("time", time);
                                map.put("date", date);
                                uploadNewsData(map);
                            }
                        }

                    });

                    break;
            }
        });


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void addYojanaData(Context context, String title) {
        adYojanaDialog = new Dialog(context);
        adYojanaDialog.setContentView(R.layout.yojna_item_layout);
        adYojanaDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        adYojanaDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        adYojanaDialog.setCancelable(false);
        adYojanaDialog.show();
        dialogTitle2 = adYojanaDialog.findViewById(R.id.dialog_title2);
        dialogTitle2.setText(title);
        appCompatAutoCompleteTextView = adYojanaDialog.findViewById(R.id.drop_down_text);
        yojanaData = adYojanaDialog.findViewById(R.id.yojana_data);
        cancelYoajanBtn = adYojanaDialog.findViewById(R.id.cancel_yojana_btn);
        uploadYojanaBtn = adYojanaDialog.findViewById(R.id.upload_yojana_btn);
        cancelYoajanBtn.setOnClickListener(v -> {
            adYojanaDialog.dismiss();
        });

        appCompatAutoCompleteTextView.setInputType(0);
        appCompatAutoCompleteTextView.setAdapter(arrayAdapter);
        appCompatAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            getYojanaName = arrayList.get(position);
            Toast.makeText(MainActivity.this, getYojanaName, Toast.LENGTH_SHORT).show();
        });

        uploadYojanaBtn.setOnClickListener(v -> {
            loadingDialog.show();
            if (title.equals("Add Yojana Preview")) {
                String desc = yojanaData.getText().toString();
                if (TextUtils.isEmpty(getYojanaName)) {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please select a Category!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(desc)) {
                    yojanaData.setError("field required!");
                } else {
                    for (YojanaModel m : yojanaModelList) {
                        if (m.getTitle().equals(getYojanaName)) {
                            yojanaId = m.getId();
                            break;
                        }
                    }

                    map.put("yojanaId", yojanaId);
                    map.put("desc", desc);
                    uploadYojanaPreview(map);
                }

            } else if (title.equals("Add News Preview")) {
                String desc = yojanaData.getText().toString();
                if (TextUtils.isEmpty(getYojanaName)) {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please select a Category!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(desc)) {
                    yojanaData.setError("field required!");
                } else {
                    for (NewsModel m : newsModelList) {
                        if (m.getTitle().equals(getYojanaName)) {
                            yojanaId = m.getId();
                            break;
                        }

                    }
                    map.put("newsId", yojanaId);
                    map.put("desc", desc);
                    uploadNewsPreview(map);
                }
            }

        });
    }

    private void uploadNewsPreview(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.uploadNewsPreviewData(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {


                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    adYojanaDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());


            }
        });

    }

    private void uploadYojanaPreview(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.upladYojanaPreivewData(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {


                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    adYojanaDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());


            }
        });

    }

    private void FileChooser(int i) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                selectImage.setImageBitmap(bitmap);
                encodedImage = imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void fetchYojanaDetails() {
        Call<YojanaModelList> call = apiInterface.getAllYojana();
        call.enqueue(new Callback<YojanaModelList>() {
            @Override
            public void onResponse(@NonNull Call<YojanaModelList> call, @NonNull Response<YojanaModelList> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;

                    for (YojanaModel yjm : response.body().getData()) {
                        arrayList.add(yjm.getTitle());
                        yojanaModelList.addAll(response.body().getData());
                    }
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<YojanaModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });
    }

    private void fetchNewsDetails() {
        Call<NewsModelList> call = apiInterface.getAllNews();
        call.enqueue(new Callback<NewsModelList>() {
            @Override
            public void onResponse(@NonNull Call<NewsModelList> call, @NonNull Response<NewsModelList> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;

                    for (NewsModel yjm : response.body().getData()) {
                        arrayList.add(yjm.getTitle());
                        newsModelList.addAll(response.body().getData());
                    }
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });

    }

    public void uploadYojana(Map<String, String> map) {

        Call<MessageModel> call = apiInterface.uploadYojana(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {


                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    uploadDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());


            }
        });
    }

    private void uploadNewsData(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.uploadNews(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {


                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    uploadDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());


            }
        });

    }
}