package com.pmkisanyojana.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.YojanaDataActivity;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.databinding.FragmentStatusBinding;
import com.pmkisanyojana.models.ApiInterface;
import com.pmkisanyojana.models.ApiWebServices;
import com.pmkisanyojana.models.MessageModel;
import com.pmkisanyojana.models.ModelFactory;
import com.pmkisanyojana.models.ProfileModel;
import com.pmkisanyojana.utils.Prevalent;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    public static Uri imageuri;
    public static Dialog uploadProfileDialog,addStatusDialog;
    public static ImageView chooseImg;
    static Bitmap bitmap;
    static String encodedImage;
    String[] cameraPermission;
    String[] storagePermission;
    MaterialCardView createAccoutnBtn;
    EditText userName;
    Button uploadProfileBtn;
    FragmentStatusBinding binding;
    Map<String, String> map = new HashMap<>();
    ApiInterface apiInterface;
    Dialog loadingDialog;
    PageViewModel pageViewModel;
    String mParam1,mParam2,id;
  public   static ConstraintLayout constraintLayout;


    public StatusFragment() {
        // Required empty public constructor
    }

    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setImage(Uri uri, YojanaDataActivity yojanaDataActivity) {
        imageuri = uri;

        try {
            InputStream inputStream = yojanaDataActivity.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            chooseImg.setImageBitmap(bitmap);
            encodedImage = imageStore(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("encodedImage",encodedImage);

    }
    public static void setStatusImage(Uri uri, YojanaDataActivity yojanaDataActivity) {
        imageuri = uri;

        try {
            InputStream inputStream = yojanaDataActivity.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            chooseImg.setImageBitmap(bitmap);
            encodedImage = imageStore(bitmap);
            uploadStatus(encodedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("encodedImage",encodedImage);

    }

    private static void uploadStatus(String encodedImage) {

        Log.d("encodedStatusImage" , encodedImage);

    }

    public static String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        createAccoutnBtn = binding.createAcBtn;
        apiInterface = ApiWebServices.getApiInterface();

        //****Loading Dialog****/
        loadingDialog = new Dialog(requireActivity());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(requireActivity().getDrawable(R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/

        // allowing permissions of gallery and camera
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        createAccoutnBtn.setOnClickListener(v -> {
            uploadProfileDialog(root.getContext());
        });
        Paper.init(requireActivity());

        id = Paper.book().read(Prevalent.userId);

        if (id != null) {
            Map<String, String> map = new HashMap<>();
            map.put("id", id);
            pageViewModel = new ViewModelProvider(this, new ModelFactory(requireActivity().getApplication(), map)).get(PageViewModel.class);
            setStatusProfile();
            binding.uploadStatusLayout.setVisibility(View.VISIBLE);
            binding.createAcBtn.setVisibility(View.GONE);
        }else {
            binding.uploadStatusLayout.setVisibility(View.GONE);
            binding.createAcBtn.setVisibility(View.VISIBLE);
        }
        constraintLayout = binding.uploadStatusLayout;
        binding.uploadStatusLayout.setOnClickListener(v -> pickFromGallery());

        return binding.getRoot();
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    private void uploadProfileDialog(Context context) {
        uploadProfileDialog = new Dialog(context);
        uploadProfileDialog.setContentView(R.layout.create_profile_layout);
        uploadProfileDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        uploadProfileDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.item_bg));
        uploadProfileDialog.setCancelable(true);
        uploadProfileDialog.show();

        chooseImg = uploadProfileDialog.findViewById(R.id.choose_img);
        userName = uploadProfileDialog.findViewById(R.id.enter_name);
        uploadProfileBtn = uploadProfileDialog.findViewById(R.id.upload_profile_btn);

        uploadProfileBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String uName = userName.getText().toString().trim();
            UUID uuid = UUID.randomUUID();
            String randomId = String.valueOf(uuid);

            if (TextUtils.isEmpty(encodedImage)){
                Toast.makeText(requireActivity(), "Please Select an Image", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }else  if (TextUtils.isEmpty(uName)){
                userName.setError("required field");
                loadingDialog.dismiss();
            }else {
                Paper.book().write(Prevalent.userId,randomId);
                Paper.book().write(Prevalent.userName,uName);
                Log.d("prevalent",Paper.book().read(Prevalent.userName)
                        +" "+Paper.book().read(Prevalent.userId));


                map.put("id", randomId);
                map.put("img", encodedImage);
                map.put("userName",uName);
                uploadProfile(map);
            }
        });

        chooseImg.setOnClickListener(v -> {
            showImagePicDialog();
        });
    }

    private void showAddStatusDialog(Context context) {
        addStatusDialog = new Dialog(context);
        addStatusDialog.setContentView(R.layout.add_status_layout);
        addStatusDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        addStatusDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.item_bg));
        addStatusDialog.getWindow().setGravity(Gravity.TOP|Gravity.END);
        WindowManager.LayoutParams layoutParams = addStatusDialog.getWindow().getAttributes();
       // layoutParams.x = 100; // right margin
        layoutParams.y = 170; // top margin
        addStatusDialog.getWindow().setAttributes(layoutParams);

        addStatusDialog.setCancelable(true);
        addStatusDialog.show();



    }

    private void uploadProfile(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.uploadProfile(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    uploadProfileDialog.dismiss();

                } else {
                    Toast.makeText(requireActivity(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());


            }
        });
    }

    public void setStatusProfile(){
        pageViewModel.getUserData().observe(requireActivity(), profileModelList -> {
            if (!profileModelList.getData().isEmpty()) {
                for (ProfileModel pm : profileModelList.getData()) {
                    //txtUserName.setText(pm.getUserName().toString().trim());
                    Glide.with(this).load(
                            "https://gedgetsworld.in/PM_Kisan_Yojana/User_Profile_Images/"
                                    + pm.getUserImage()).into(binding.userStatusImg);

                }
            }
        });
    }

    private void showImagePicDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickFromGallery();
                }
            } else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickFromGallery();
                }
            }
        });
        builder.create().show();
    }

    // checking storage permissions
    private Boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    // Requesting  gallery permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // Requesting camera and gallery
    // permission if not given
    //  @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case CAMERA_REQUEST: {
//                if (grantResults.length > 0) {
//                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (camera_accepted && writeStorageaccepted) {
//                        pickFromGallery();
//                    } else {
//                        Toast.makeText(requireActivity(), "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//            break;
//            case STORAGE_REQUEST: {
//                if (grantResults.length > 0) {
//                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (writeStorageaccepted) {
//                        pickFromGallery();
//                    } else {
//                        Toast.makeText(requireActivity(), "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//            break;
//        }
//    }

    // Requesting camera permission
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                uri = result.getUri();
//                chooseImg.setImageURI(uri);
//                Glide.with(this).load(uri).into(chooseImg);
//            }
//        }
//        Toast.makeText(requireActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            uri = data.getData();
//
//            // start picker to get image for cropping and then use the image in cropping activity
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(requireActivity());
//
//
//        }
//        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode==RESULT_OK) {
//                assert result != null;
//                uri = result.getUri();
//                try {
//                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
//                    bitmap = BitmapFactory.decodeStream(inputStream);
//                    chooseImg.setImageBitmap(bitmap);
//                    encodedImage = imageStore(bitmap);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }

    private void FileChooser() {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 96);
            intent.putExtra("outputY", 96);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            Log.d("vvvvv", e.getMessage());
        }
    }

    // Here we will pick image from gallery or camera
    private void pickFromGallery() {
        CropImage.activity().start(requireActivity());
    }


}