package com.pmkisanyojanaadmin.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.databinding.ActivityMainBinding;
import com.pmkisanyojanaadmin.model.AdsModel;
import com.pmkisanyojanaadmin.model.AdsModelList;
import com.pmkisanyojanaadmin.model.ApiInterface;
import com.pmkisanyojanaadmin.model.ApiWebServices;
import com.pmkisanyojanaadmin.model.ImgModel;
import com.pmkisanyojanaadmin.model.MessageModel;
import com.pmkisanyojanaadmin.model.NewsModel;
import com.pmkisanyojanaadmin.model.NewsModelList;
import com.pmkisanyojanaadmin.model.PMAdsModel;
import com.pmkisanyojanaadmin.model.PMAdsModelList;
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
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Button cancelBtn, uploadBtn,
            cancelYoajanBtn, uploadYojanaBtn, editAndDeleteBtn, adIdCancelBtn, adIdUploadBtn;
    Dialog uploadDialog, adYojanaDialog, addQuizDialog, adsUpdateDialog,uploadImagesDialog;
    RadioButton immediateBtn, scheduleBtn;
    RadioGroup radioGroup;
    LinearLayout scheduleLayout;
    TextView dialogTitle, dialogTitle2, adIdTitleTxt;
    TextView setDate, setTime;
    ImageView selectImage,chooseBannerImage;
    EditText selectTitle, yojanaData, yojanaLink, bannerIdTxt, interstitialIdTxt, nativeIdTxt, openAppTxt,imageURlTxt;
    AppCompatAutoCompleteTextView appCompatAutoCompleteTextView;
    List<YojanaModel> yojanaModelList = new ArrayList<>();
    List<NewsModel> newsModelList = new ArrayList<>();
    List<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String getYojanaName, previewId, randomId, adIdTitle;
    ApiInterface apiInterface;
    String encodedImage;
    Uri uri;
    Bitmap bitmap;
    TextInputLayout textInputLayout;
    String selectTime = "", selectDate = "";
    Map<String, String> map = new HashMap<>();
    Dialog loadingDialog;
    Button uploadQuizQuestionBtn,uploadBannerImageBtn;
    TextView question, op1, op2, op3, op4, ans;
    AdsModel adsModel;
    PMAdsModel pmAdsModel;
    ActivityResultLauncher<String> launcher;
    String img, url;



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                if (chooseBannerImage != null) {
                    Glide.with(this).load(result).into(chooseBannerImage);
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(result);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    encodedImage = imageStore(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        apiInterface = ApiWebServices.getApiInterface();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);
        binding.yojanaBtn.setOnClickListener(v -> {
            showYojanaUploadDialog(this, "Upload Yojana");
        });
        binding.newsBtn.setOnClickListener(v -> {
            showYojanaUploadDialog(this, "Upload News");
        });
        binding.addYojanaBtn.setOnClickListener(v -> {
            arrayList.clear();
            addPreviewData(this, "Yojana");
        });
//        binding.addNewsBtn.setOnClickListener(v -> {
//            arrayList.clear();
//            addPreviewData(this, "News");
//        });
        binding.addOthersBtn.setOnClickListener(v -> {
            addPreviewData(this, "Others");
        });
        binding.editDeleteBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EditAndDeleteActivity.class));
        });
        binding.uploadOtherData.setOnClickListener(v -> {
            showYojanaUploadDialog(this, "Upload Others");
        });

        binding.uploadQuiz.setOnClickListener(v -> showUploadQuizQuestionDialog());

        binding.updateAdId.setOnClickListener(v -> {
            showUpdatePMAdsDialog();
        });
        binding.updateAdId2.setOnClickListener(v -> {
            showUpdateAdsDialog("PM Kisan Yojana List");
        });
        updateImagesDialog();

        binding.uploadImg.setOnClickListener(v -> {
            uploadImagesDialog.show();

        });
    }

    public void updateImagesDialog() {
        uploadImagesDialog = new Dialog(this);
        uploadImagesDialog.setContentView(R.layout.upload_image_dialog);
        uploadImagesDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        uploadImagesDialog.getWindow().setBackgroundDrawable(
                ContextCompat.getDrawable(this, R.drawable.item_bg));
        uploadImagesDialog.setCancelable(false);

        Call<ImgModel> call = apiInterface.fetchImg();
        call.enqueue(new Callback<ImgModel>() {
            @Override
            public void onResponse(@NonNull Call<ImgModel> call, @NonNull Response<ImgModel> response) {

                if (response.body()!= null){

                    img= response.body().getImg();
                    url= response.body().getUrl();
                    encodedImage = img;
                    Glide.with(MainActivity.this).load("https://gedgetsworld.in/PM_Kisan_Yojana/Images/" + img).into(chooseBannerImage);
                    imageURlTxt.setText(url);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImgModel> call, @NonNull Throwable t) {

            }
        });
        chooseBannerImage = uploadImagesDialog.findViewById(R.id.choose_banner_image);
        imageURlTxt = uploadImagesDialog.findViewById(R.id.image_url);
        uploadBannerImageBtn = uploadImagesDialog.findViewById(R.id.upload_image_btn);
        cancelBtn = uploadImagesDialog.findViewById(R.id.image_cancel);
        cancelBtn.setOnClickListener(v -> {
            uploadImagesDialog.dismiss();
            encodedImage = "";
        });


        chooseBannerImage.setOnClickListener(v -> {
            launcher.launch("image/*");
        });

        uploadBannerImageBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String mUrl = imageURlTxt.getText().toString().trim();
            if (encodedImage == null) {
                Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(mUrl)) {
                imageURlTxt.setError("Image Url Required");
                loadingDialog.dismiss();
            } else {
                if (encodedImage.length() <= 100) {
                    map.put("img", encodedImage);
                    map.put("deleteImg", img);
                    map.put("url", mUrl);
                    map.put("imgKey", "0");
                    updateBannerImage(map);

                } else {
                    map.put("img", encodedImage);
                    map.put("deleteImg", img);
                    map.put("url", mUrl);
                    map.put("imgKey", "1");
                    updateBannerImage(map);
                }

            }
        });

    }
    private void updateBannerImage(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updateImg(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    uploadImagesDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }
    private void showUpdatePMAdsDialog() {
        adsUpdateDialog = new Dialog(this);
        adsUpdateDialog.setContentView(R.layout.ad_id_layout);
        adsUpdateDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        adsUpdateDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.item_bg));
        adsUpdateDialog.setCancelable(false);
        adsUpdateDialog.show();

        bannerIdTxt = adsUpdateDialog.findViewById(R.id.banner_id);
        interstitialIdTxt = adsUpdateDialog.findViewById(R.id.interstitial_id);
        nativeIdTxt = adsUpdateDialog.findViewById(R.id.native_id);
        adIdTitleTxt = adsUpdateDialog.findViewById(R.id.ad_id_title);
        openAppTxt = adsUpdateDialog.findViewById(R.id.openapp_id);
        adIdUploadBtn = adsUpdateDialog.findViewById(R.id.upload_ids);
        adIdCancelBtn = adsUpdateDialog.findViewById(R.id.cancel_id);
        TextInputLayout textInputLayout = adsUpdateDialog.findViewById(R.id.textInputLayout1);
        textInputLayout.setVisibility(View.VISIBLE);

        adIdCancelBtn.setOnClickListener(v -> {
            adsUpdateDialog.dismiss();
        });

        apiInterface = ApiWebServices.getApiInterface();
        Call<PMAdsModelList> call = apiInterface.fetchAdsPm("PM Kisan Yojana");
        call.enqueue(new Callback<PMAdsModelList>() {
            @Override
            public void onResponse(@NonNull Call<PMAdsModelList> call, @NonNull Response<PMAdsModelList> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getData() != null) {
                        for (PMAdsModel ads : response.body().getData()) {
                            pmAdsModel = ads;
                            adIdTitle = ads.getId();
                            adIdTitleTxt.setText(adIdTitle);
                            bannerIdTxt.setText(ads.getBanner());
                            interstitialIdTxt.setText(ads.getInterstitial());
                            nativeIdTxt.setText(ads.getNativeADs());
                            openAppTxt.setText(ads.getAppOpen());

                            Log.d("ads", pmAdsModel.getBanner());

                        }
                    }
                } else {
                    Log.d("adsError", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PMAdsModelList> call, @NonNull Throwable t) {
                Log.d("adsError", t.getMessage());
            }
        });


        adIdUploadBtn.setOnClickListener(v -> {
            String bnId = bannerIdTxt.getText().toString().trim();
            String interId = interstitialIdTxt.getText().toString().trim();
            String nativeId = nativeIdTxt.getText().toString().trim();
            String appOpen = openAppTxt.getText().toString().trim();
            if (bnId.equals(pmAdsModel.getBanner())
                    && interId.equals(pmAdsModel.getInterstitial())
                    && nativeId.equals(pmAdsModel.getNativeADs())
                    && appOpen.equals(pmAdsModel.getAppOpen())) {

                Toast.makeText(this, "No changes made in Ids", Toast.LENGTH_SHORT).show();

            } else {
                loadingDialog.show();
                map.put("id", adIdTitle);
                map.put("banner_id", bnId);
                map.put("inter_id", interId);
                map.put("native_id", nativeId);
                map.put("open_app", appOpen);
                updatePMAdIds(map);
            }
        });
    }

    private void updatePMAdIds(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updatePMAdIds(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
                adsUpdateDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void showUpdateAdsDialog(String key) {
        adsUpdateDialog = new Dialog(this);
        adsUpdateDialog.setContentView(R.layout.ad_id_layout);
        adsUpdateDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        adsUpdateDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.item_bg));
        adsUpdateDialog.setCancelable(false);
        adsUpdateDialog.show();
        bannerIdTxt = adsUpdateDialog.findViewById(R.id.banner_id);
        interstitialIdTxt = adsUpdateDialog.findViewById(R.id.interstitial_id);
        nativeIdTxt = adsUpdateDialog.findViewById(R.id.native_id);
        adIdTitleTxt = adsUpdateDialog.findViewById(R.id.ad_id_title);
        adIdUploadBtn = adsUpdateDialog.findViewById(R.id.upload_ids);
        adIdCancelBtn = adsUpdateDialog.findViewById(R.id.cancel_id);

        adIdCancelBtn.setOnClickListener(v -> {
            adsUpdateDialog.dismiss();
        });

        apiInterface = ApiWebServices.getApiInterface();
        Call<AdsModelList> call = apiInterface.fetchAds(key);
        call.enqueue(new Callback<AdsModelList>() {
            @Override
            public void onResponse(@NonNull Call<AdsModelList> call, @NonNull Response<AdsModelList> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getData() != null) {
                        for (AdsModel ads : response.body().getData()) {
                            adsModel = ads;
                            adIdTitle = ads.getId();
                            adIdTitleTxt.setText(adIdTitle);
                            bannerIdTxt.setText(ads.getBanner());
                            interstitialIdTxt.setText(ads.getInterstitial());
                            nativeIdTxt.setText(ads.getNativeADs());
                        }
                    }
                } else {
                    Log.d("adsError", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AdsModelList> call, @NonNull Throwable t) {
                Log.d("adsError", t.getMessage());
            }
        });


        adIdUploadBtn.setOnClickListener(v -> {
            String bnId = bannerIdTxt.getText().toString().trim();
            String interId = interstitialIdTxt.getText().toString().trim();
            String nativeId = nativeIdTxt.getText().toString().trim();
            if (bnId.equals(adsModel.getBanner())
                    && interId.equals(adsModel.getInterstitial())
                    && nativeId.equals(adsModel.getNativeADs())) {

                Toast.makeText(this, "No changes made in Ids", Toast.LENGTH_SHORT).show();

            } else {
                loadingDialog.show();
                map.put("id", adIdTitle);
                map.put("banner_id", bnId);
                map.put("inter_id", interId);
                map.put("native_id", nativeId);
                updateAdIds(map);
            }
        });
    }

    private void updateAdIds(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updateAdIds(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
                adsUpdateDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void showUploadQuizQuestionDialog() {

        addQuizDialog = new Dialog(this);
        addQuizDialog.setContentView(R.layout.quiz_layout);
        addQuizDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        addQuizDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.item_bg));
        addQuizDialog.setCancelable(false);
        addQuizDialog.show();


        question = addQuizDialog.findViewById(R.id.question);
        op1 = addQuizDialog.findViewById(R.id.option);
        op2 = addQuizDialog.findViewById(R.id.option2);
        op3 = addQuizDialog.findViewById(R.id.option3);
        op4 = addQuizDialog.findViewById(R.id.option4);
        ans = addQuizDialog.findViewById(R.id.answer);
        uploadQuizQuestionBtn = addQuizDialog.findViewById(R.id.upload_quiz);

        addQuizDialog.findViewById(R.id.cancel).setOnClickListener(v -> addQuizDialog.dismiss());
        uploadQuizQuestionBtn.setOnClickListener(v -> {

            String ques, opt1, opt2, opt3, opt4, answer;

            ques = question.getText().toString().trim();
            opt1 = op1.getText().toString().trim();
            opt2 = op2.getText().toString().trim();
            opt3 = op3.getText().toString().trim();
            opt4 = op4.getText().toString().trim();
            answer = ans.getText().toString().trim();
            if (TextUtils.isEmpty(ques)) {
                question.setError("field required");
            } else if (TextUtils.isEmpty(opt1)) {
                op1.setError("field required");
            } else if (TextUtils.isEmpty(opt2)) {
                op2.setError("field required");
            } else if (TextUtils.isEmpty(opt3)) {
                op3.setError("field required");
            } else if (TextUtils.isEmpty(opt4)) {
                op4.setError("field required");
            } else if (TextUtils.isEmpty(answer)) {
                ans.setError("field required");
            } else {

                Map<String, String> map = new HashMap<>();
                map.put("ques", ques);
                map.put("op1", opt1);
                map.put("op2", opt2);
                map.put("op3", opt3);
                map.put("op4", opt4);
                map.put("ans", answer);
                uploadQuiz(map);
            }
        });

    }

    private void uploadQuiz(Map<String, String> map) {
        loadingDialog.show();
        Call<MessageModel> call = apiInterface.uploadQuizQuestions(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    addQuizDialog.dismiss();

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


    private void showYojanaUploadDialog(Context context, String title) {
        uploadDialog = new Dialog(context);
        uploadDialog.setContentView(R.layout.upload_dialog);
        uploadDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        uploadDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.item_bg));
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

        radioGroup.setVisibility(View.GONE);
        if (title.equals("Upload Yojana")) {
            textInputLayout.setVisibility(View.VISIBLE);

        } else if (title.equals("Upload News")) {
            textInputLayout.setVisibility(View.VISIBLE);
            textInputLayout.setHint("Enter Description");
            yojanaLink.setHeight(450);
        } else if (title.equals("Upload Others")) {
            textInputLayout.setVisibility(View.VISIBLE);

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

            assert currentDate != null;
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
                    UUID uuid = UUID.randomUUID();
                    randomId = String.valueOf(uuid);
                    map.put("id", randomId);
                    map.put("img", encodedImage);
                    map.put("title", sTitle);
                    map.put("time", time);
                    map.put("date", date);
                    map.put("url", url);
                    map.put("pinned", String.valueOf(false));
                    uploadYojana(map);
                }
            } else if (title.equals("Upload News")) {

                String sTitle = selectTitle.getText().toString().trim();
                String desc = yojanaLink.getText().toString();
                String date = setDate.getText().toString();
                String time = setTime.getText().toString();

                if (TextUtils.isEmpty(encodedImage)) {
                    loadingDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Please select an Image!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sTitle)) {
                    loadingDialog.dismiss();

                    selectTitle.setError("required field");
                } else {
                    UUID uuid = UUID.randomUUID();
                    randomId = String.valueOf(uuid);
                    map.put("id", randomId);
                    map.put("img", encodedImage);
                    map.put("title", sTitle);
                    map.put("desc", desc);
                    map.put("time", time);
                    map.put("date", date);
                    uploadNewsData(map);
                }
            } else if (title.equals("Upload Others")) {
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
                    UUID uuid = UUID.randomUUID();
                    randomId = String.valueOf(uuid);
                    map.put("id", randomId);
                    map.put("img", encodedImage);
                    map.put("title", sTitle);
                    map.put("time", time);
                    map.put("date", date);
                    map.put("url", url);
                    map.put("pinned", String.valueOf(false));
                    uploadOthers(map);
                }
            }
        });

    }

    private void uploadOthers(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.uploadOthers(map);
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


    @SuppressLint("UseCompatLoadingForDrawables")
    public void addPreviewData(Context context, String title) {
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

        if (title.equals("Yojana")) {
            arrayList.clear();
            yojanaModelList.clear();
            fetchYojanaDetails();
            uploadYojanaBtn.setOnClickListener(v -> {
                loadingDialog.show();
                String desc = yojanaData.getText().toString();
                if (TextUtils.isEmpty(getYojanaName)) {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please select a Category!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(desc)) {
                    yojanaData.setError("field required!");
                    loadingDialog.dismiss();
                } else {
                    for (YojanaModel m : yojanaModelList) {
                        if (m.getTitle().equals(getYojanaName)) {
                            previewId = m.getId();
                            break;
                        }
                    }
                    map.put("previewId", previewId);
                    map.put("desc", desc);
                    uploadPreview(map);
                }
            });
        }
//        else if (title.equals("News")) {
//            arrayList.clear();
//            newsModelList.clear();
//            fetchNewsDetails();
//            uploadYojanaBtn.setOnClickListener(v -> {
//                loadingDialog.show();
//                String desc = yojanaData.getText().toString();
//                if (TextUtils.isEmpty(getYojanaName)) {
//                    loadingDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Please select a Category!", Toast.LENGTH_SHORT).show();
//                } else if (TextUtils.isEmpty(desc)) {
//                    yojanaData.setError("field required!");
//                    loadingDialog.dismiss();
//                } else {
//                    for (NewsModel m : newsModelList) {
//                        if (m.getTitle().equals(getYojanaName)) {
//                            previewId = m.getId();
//                            break;
//                        }
//                    }
//                    map.put("previewId", previewId);
//                    map.put("desc", desc);
//                    uploadPreview(map);
//                }
//            });
//        }
        else if (title.equals("Others")) {
            arrayList.clear();
            yojanaModelList.clear();
            fetchOthersDetails();
            uploadYojanaBtn.setOnClickListener(v -> {
                loadingDialog.show();
                String desc = yojanaData.getText().toString();
                if (TextUtils.isEmpty(getYojanaName)) {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please select a Category!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(desc)) {
                    yojanaData.setError("field required!");
                    loadingDialog.dismiss();
                } else {
                    for (YojanaModel m : yojanaModelList) {
                        if (m.getTitle().equals(getYojanaName)) {
                            previewId = m.getId();
                            break;
                        }
                    }
                    map.put("previewId", previewId);
                    map.put("desc", desc);
                    uploadPreview(map);
                }
            });
        }


    }

    private void uploadPreview(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.uploadPreviewData(map);
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

    public void fetchOthersDetails() {
        Call<YojanaModelList> call = apiInterface.getAllOthers();
        call.enqueue(new Callback<YojanaModelList>() {
            @Override
            public void onResponse(@NonNull Call<YojanaModelList> call, @NonNull Response<YojanaModelList> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;

                    for (YojanaModel yjm : response.body().getData()) {
                        arrayList.add(yjm.getTitle());
                        yojanaModelList.addAll(response.body().getData());
                    }
                    Log.d("ffffffff", response.body().getData().toString());
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