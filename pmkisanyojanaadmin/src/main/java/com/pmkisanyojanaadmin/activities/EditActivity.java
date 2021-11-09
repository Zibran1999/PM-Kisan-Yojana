package com.pmkisanyojanaadmin.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.adapter.NewsAdapter;
import com.pmkisanyojanaadmin.model.ApiInterface;
import com.pmkisanyojanaadmin.model.ApiWebServices;
import com.pmkisanyojanaadmin.model.MessageModel;
import com.pmkisanyojanaadmin.model.NewsModel;
import com.pmkisanyojanaadmin.model.PageViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity implements NewsAdapter.NewsInterface {

    Button uploadNewsBtn, uploadNewsBtn2, editExternalBtn, editInternalBtn, dismiss2;
    ImageButton dismiss;
    ImageView newsImageView, cardNewsImage;
    EditText newsTitle, cardNewsTitle, newsDesc;
    Dialog editDialog, editDialog2, loadingDialog;
    List<NewsModel> newsModelList = new ArrayList<>();
    NewsAdapter newsAdapter;
    String id, title, image,image2, encodedImage, url, getTitle;
    Uri uri;
    Bitmap bitmap;
    ApiInterface apiInterface;
    Map<String, String> map = new HashMap<>();
    private PageViewModel pageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editExternalBtn = findViewById(R.id.edit_external_btn);
        editInternalBtn = findViewById(R.id.edit_internal_btn);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        setNewsData(this);
        apiInterface = ApiWebServices.getApiInterface();

        //****Loading Dialog****/
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("image");
        image2 = getIntent().getStringExtra("image2");
        url = getIntent().getStringExtra("url");
        Log.d("fffffff", id);
        Log.d("fffffff", title);
        Log.d("fffffff", image);
        editExternalBtn.setOnClickListener(v -> {
            showExternalEdit(this);
        });
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void editNews() {
        editDialog = new Dialog(EditActivity.this);
        editDialog.setContentView(R.layout.news_layout);
        editDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        editDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        editDialog.setCancelable(true);
        editDialog.show();
        newsImageView = editDialog.findViewById(R.id.news_imageView);
        newsTitle = editDialog.findViewById(R.id.news_title);
        newsDesc = editDialog.findViewById(R.id.news_description);
        uploadNewsBtn = editDialog.findViewById(R.id.upload_news_btn);
        dismiss = editDialog.findViewById(R.id.newsDismissBtn);
        dismiss.setOnClickListener(v -> {
            editDialog.dismiss();
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showExternalEdit(Context context) {
        editDialog2 = new Dialog(EditActivity.this);
        editDialog2.setContentView(R.layout.edit_news_card);
        editDialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editDialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        editDialog2.setCancelable(true);
        editDialog2.show();
        cardNewsImage = editDialog2.findViewById(R.id.selectImage);
        cardNewsTitle = editDialog2.findViewById(R.id.item_title);
        dismiss2 = editDialog2.findViewById(R.id.cancel_btn);
        uploadNewsBtn2 = editDialog2.findViewById(R.id.upload_btn2);

        dismiss2.setOnClickListener(v -> editDialog2.dismiss());
        Glide.with(EditActivity.this).load(image).into(cardNewsImage);
        cardNewsTitle.setText(title);
        cardNewsImage.setOnClickListener(v -> {
            FileChooser(100);
        });
        uploadNewsBtn2.setOnClickListener(v -> {
            loadingDialog.show();
            getTitle = cardNewsTitle.getText().toString().trim();
            if (encodedImage.length() <= 100) {
                map.put("id", id);
                map.put("img", encodedImage);
                map.put("title", getTitle);
                map.put("deleteImg", image2);
                map.put("imgKey", "0");
                map.put("url", url);
                uploadNewsData(map);
            }if (encodedImage.length() > 100) {
                map.put("id", id);
                map.put("img", encodedImage);
                map.put("title", getTitle);
                map.put("deleteImg", image);
                map.put("imgKey", "1");
                map.put("url", url);
                uploadNewsData(map);
            }
        });


    }
    private void uploadNewsData(Map<String, String> map) {
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

    private void setNewsData(Context context) {

    }

    @Override
    public void onItemClicked(NewsModel newsModel) {

    }
}