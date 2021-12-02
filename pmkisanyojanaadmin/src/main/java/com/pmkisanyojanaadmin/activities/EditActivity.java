package com.pmkisanyojanaadmin.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.model.ApiInterface;
import com.pmkisanyojanaadmin.model.ApiWebServices;
import com.pmkisanyojanaadmin.model.MessageModel;
import com.pmkisanyojanaadmin.model.ModelFactory;
import com.pmkisanyojanaadmin.model.PageViewModel;
import com.pmkisanyojanaadmin.model.PreviewModel;
import com.pmkisanyojanaadmin.model.YojanaViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    Button uploadDataBtn, uploadNewsBtn2, editExternalBtn, editInternalBtn, dismiss2, cancelPreview;
    ImageView cardNewsImage;
    TextInputLayout editTextInputLayout, textInputLayout;
    EditText cardNewsTitle, editData, yojanaLink;
    Dialog editDialog, editDialog2, loadingDialog;
    TextView dialogTitle;
    Call<MessageModel> call;
    String id, title, image, image2, encodedImage, getTitle, getLink, intentId, link, yojanaPreview, hindiPreviewId, englishPreviewId;
    Uri uri;
    Bitmap bitmap;
    ApiInterface apiInterface;
    Map<String, String> map = new HashMap<>();
    PageViewModel pageViewModel;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editExternalBtn = findViewById(R.id.edit_external_btn);
        editInternalBtn = findViewById(R.id.edit_internal_btn);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        apiInterface = ApiWebServices.getApiInterface();

        //****Loading Dialog****/
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/

        intentId = getIntent().getStringExtra("intentId");

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("image");
        image2 = getIntent().getStringExtra("image2");
        link = getIntent().getStringExtra("link");
        encodedImage = image2;

        editExternalBtn.setOnClickListener(v -> {
            showUpdateNewsDialog();
        });
        editInternalBtn.setOnClickListener(v -> {
            showPreview(intentId);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showUpdateNewsDialog() {
        editDialog2 = new Dialog(EditActivity.this);
        editDialog2.setContentView(R.layout.edit_news_card);
        editDialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editDialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        editDialog2.setCancelable(true);
        editDialog2.show();
        cardNewsImage = editDialog2.findViewById(R.id.selectImage);
        cardNewsTitle = editDialog2.findViewById(R.id.item_title);
        editTextInputLayout = editDialog2.findViewById(R.id.edit_textInputLayout2);
        yojanaLink = editDialog2.findViewById(R.id.edit_yojana_link);
        dismiss2 = editDialog2.findViewById(R.id.cancel_btn);
        uploadNewsBtn2 = editDialog2.findViewById(R.id.upload_btn2);

        dismiss2.setOnClickListener(v -> {
            editDialog2.dismiss();
            encodedImage = "";
        });
        if (intentId.equals("news")) {
            editTextInputLayout.setVisibility(View.GONE);
        }
        yojanaLink.setText(link);

        Glide.with(EditActivity.this).load(image).into(cardNewsImage);
        cardNewsTitle.setText(title);
        cardNewsImage.setOnClickListener(v -> {
            FileChooser(100);
        });
        uploadNewsBtn2.setOnClickListener(v -> {
            loadingDialog.show();
            getTitle = cardNewsTitle.getText().toString().trim();
            getLink = yojanaLink.getText().toString().trim();
            if (encodedImage.length() <= 100) {

                map.put("id", id);
                map.put("img", encodedImage);
                map.put("title", getTitle);
                map.put("deleteImg", image2);
                map.put("imgKey", "0");

                if (intentId.equals("news")) {
                    updateNewsData(map);
                } else {
                    map.put("link", getLink);
                    updateYojanaData(map);
                }
            }
            if (encodedImage.length() > 100) {

                map.put("id", id);
                map.put("img", encodedImage);
                map.put("title", getTitle);
                map.put("deleteImg", image2);
                map.put("imgKey", "1");

                if (intentId.equals("news")) {
                    updateNewsData(map);
                } else {
                    map.put("link", getLink);
                    updateYojanaData(map);
                }
            }
        });

    }

    private void updateNewsData(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updateNews(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    editDialog2.dismiss();

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

    private void updateYojanaData(Map<String, String> map) {
        if (intentId.equals("yojana")) {
            call = apiInterface.updateYojana(map);
        } else if (intentId.equals("others")) {
            call = apiInterface.updateOthers(map);
        }
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    editDialog2.dismiss();

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

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    public void showPreview(String intentId) {
        editDialog = new Dialog(EditActivity.this);
        editDialog.setContentView(R.layout.yojna_item_layout);
        editDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        editDialog.setCancelable(true);
        editDialog.show();
        dialogTitle = editDialog.findViewById(R.id.dialog_title2);
        textInputLayout = editDialog.findViewById(R.id.textInputLayout);
        textInputLayout.setVisibility(View.GONE);
        editData = editDialog.findViewById(R.id.yojana_data);
        cancelPreview = editDialog.findViewById(R.id.cancel_yojana_btn);
        uploadDataBtn = editDialog.findViewById(R.id.upload_yojana_btn);
        cancelPreview.setOnClickListener(v -> {
            editDialog.dismiss();
        });
        Log.d("preivew id", id);

        dialogTitle.setText(intentId);


        Map<String, String> mapId = new HashMap<>();
        mapId.put("previewId", id.trim());

        MaterialButtonToggleGroup materialButtonToggleGroup = editDialog.findViewById(R.id.materialButtonToggleGroup);
        materialButtonToggleGroup.setVisibility(View.GONE);
        Button hindi, english;
        hindi = editDialog.findViewById(R.id.hindiPreview);
        english = editDialog.findViewById(R.id.englishPreview);

        if (intentId.equals("news")) {
            materialButtonToggleGroup.setVisibility(View.GONE);
        }

        YojanaViewModel viewModel = new ViewModelProvider(this, new ModelFactory(this.getApplication(), mapId)).get(YojanaViewModel.class);
        viewModel.getPreviewData().observe(this, yojanaPreviewModelList -> {

            if (!yojanaPreviewModelList.getData().isEmpty()) {

                String hindiString = null;
                String englishString = null;
                for (PreviewModel ypm : yojanaPreviewModelList.getData()) {

                    if (ypm.getPreviewId().equals(id)) {

                        Log.d("fffff", ypm.getDesc().toString());
                        hindi.setBackgroundColor(Color.parseColor("#0C61F1"));
                        hindi.setTextColor(Color.WHITE);

                        String replaceString = ypm.getDesc().replaceAll("<.*?>", "");
                        String removeNumeric = replaceString.replaceAll("[0-9]", "");

                        for (char c : removeNumeric.trim().toCharArray()) {
                            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.DEVANAGARI) {
                                hindiString = ypm.getDesc();
                                Log.d("hindi", hindiString);
                                hindiPreviewId = ypm.getId();
                                break;
                            } else {

                                if (englishString == null) {
                                    englishString = ypm.getDesc();
                                    Log.d("english", englishString);
                                    materialButtonToggleGroup.setVisibility(View.VISIBLE);
                                    englishPreviewId = ypm.getId();

                                }

                            }

                        }
                    }
                }
                String finalEnglishString = englishString;
                String finalHindiString = hindiString;
                if (finalHindiString != null) {
                    editData.setText(finalHindiString);
                    uploadDataBtn.setOnClickListener(v -> updatePreview(hindiPreviewId, editData.getText().toString().trim()));

                } else {
                    editData.setText(finalEnglishString);
                    materialButtonToggleGroup.setVisibility(View.GONE);
                    uploadDataBtn.setOnClickListener(v -> updatePreview(englishPreviewId, editData.getText().toString().trim()));

                }

                english.setBackgroundColor(0);
                english.setTextColor(Color.BLACK);
                materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

                    switch (checkedId) {
                        case R.id.hindiPreview:
                            english.setBackgroundColor(0);
                            english.setTextColor(Color.BLACK);
                            hindi.setBackgroundColor(Color.parseColor("#0C61F1"));
                            hindi.setTextColor(Color.WHITE);
                            editData.setText(finalHindiString);
                            uploadDataBtn.setOnClickListener(v -> updatePreview(hindiPreviewId, editData.getText().toString().trim()));
                            break;
                        case R.id.englishPreview:
                            english.setBackgroundColor(Color.parseColor("#0C61F1"));
                            english.setTextColor(Color.WHITE);
                            hindi.setBackgroundColor(0);
                            hindi.setTextColor(Color.BLACK);
                            editData.setText(finalEnglishString);
                            uploadDataBtn.setOnClickListener(v -> updatePreview(englishPreviewId, editData.getText().toString().trim()));
                            break;
                        default:
                    }
                });
            }
        });

    }

    private void updatePreview(String previewId, String s) {
        loadingDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("previewId", id);
        map.put("id", previewId);
        map.put("desc", s);

        Call<MessageModel> call = apiInterface.updatePreview(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    editDialog.dismiss();
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
                cardNewsImage.setImageBitmap(bitmap);
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


}