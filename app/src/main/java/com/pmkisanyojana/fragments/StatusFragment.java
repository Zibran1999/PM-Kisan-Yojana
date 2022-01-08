package com.pmkisanyojana.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.ShowStatusActivity;
import com.pmkisanyojana.activities.YojanaDataActivity;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.adapters.StatusAdapter;
import com.pmkisanyojana.adapters.StatusClickListener;
import com.pmkisanyojana.databinding.FragmentStatusBinding;
import com.pmkisanyojana.models.ApiInterface;
import com.pmkisanyojana.models.ApiWebServices;
import com.pmkisanyojana.models.MessageModel;
import com.pmkisanyojana.models.ModelFactory;
import com.pmkisanyojana.models.MyStatusModel;
import com.pmkisanyojana.models.ProfileModel;
import com.pmkisanyojana.models.StatusModel;
import com.pmkisanyojana.models.TimeUtils;
import com.pmkisanyojana.utils.CommonMethod;
import com.pmkisanyojana.utils.Prevalent;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment implements StatusClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int STORAGE_REQUEST = 200;
    private static final int CAMERA_REQUEST = 100;
    public static Uri imageuri;
    public static Dialog uploadProfileDialog, addStatusDialog;
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
    String mParam1, mParam2, id;
    CircularStatusView circularStatusView;

    ActivityResultLauncher<String> launcher;
    ImageView imageView;
    TextView statusTime;
    String userImage, timeSt, img, myStatusId;

    CircleImageView userProfileImage;

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
        Log.d("encodedImage", encodedImage);

    }

    public static String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        createAccoutnBtn = binding.createAcBtn;
        circularStatusView = binding.circularStatusView;
        imageView = binding.imageView3;
        statusTime = binding.txtClickToAdd;
        userProfileImage = binding.userStatusImg;
        apiInterface = ApiWebServices.getApiInterface();

        Paper.init(requireActivity());
        id = Paper.book().read(Prevalent.userId);


        //****Loading Dialog****/
        loadingDialog = new Dialog(requireActivity());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/
        uploadProfileDialog(root.getContext());

        // allowing permissions of gallery and camera
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        createAccoutnBtn.setOnClickListener(v -> {
            uploadProfileDialog.show();
        });

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                try {
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(result);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    encodedImage = imageStore(bitmap);
                    showStatusBeforeUpload(result, id);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


        if (id != null) {
            map.put("id", id);
            setStatusProfile();
            showAllStatus();
            binding.uploadStatusLayout.setVisibility(View.VISIBLE);
            binding.createAcBtn.setVisibility(View.GONE);
        } else {
            binding.uploadStatusLayout.setVisibility(View.GONE);
            binding.createAcBtn.setVisibility(View.VISIBLE);
        }
        binding.uploadStatusLayout.setOnClickListener(v -> {
            launcher.launch("image/*");
        });

        return binding.getRoot();
    }

    private void showStatusBeforeUpload(Uri result, String id) {

        addStatusDialog = new Dialog(requireActivity());
        addStatusDialog.setContentView(R.layout.show_status_layout);
        addStatusDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addStatusDialog.setCancelable(true);
        addStatusDialog.show();

        ImageView showImage = addStatusDialog.findViewById(R.id.setStatusImg);
        ImageView uploadBtn = addStatusDialog.findViewById(R.id.upload_status_btn);
        ImageView cancelBtn = addStatusDialog.findViewById(R.id.cancel_button);

        Glide.with(requireActivity()).load(result).into(showImage);
        cancelBtn.setOnClickListener(v -> addStatusDialog.dismiss());
        uploadBtn.setOnClickListener(v -> {
            binding.txtClickToAdd.setText("Sending...");
            uploadStatus(encodedImage, id);

        });

    }

    private void showAllStatus() {
        List<StatusModel> statusModelLis = new ArrayList<>();
        RecyclerView recyclerView = binding.recyclerview;
        StatusAdapter statusAdapter = new StatusAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(statusAdapter);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        pageViewModel.fetchStatus().observe(requireActivity(), statusModelList -> {
            if (!statusModelList.getData().isEmpty()) {

                statusModelLis.clear();
                binding.textView6.setVisibility(View.VISIBLE);
                binding.recentUpdateLayout.setVisibility(View.VISIBLE);
                statusModelLis.addAll(statusModelList.getData());
                for (StatusModel s : statusModelLis) {
                    if (id.equals(s.getProfileID())) {
                        statusModelLis.remove(s);
                        break;
                    }
                }
                Collections.reverse(statusModelLis);
                statusAdapter.updateStatusList(statusModelLis);
                CommonMethod.getBannerAds(requireActivity(), binding.adViewStatus);

            } else {
                binding.recentUpdateLayout.setVisibility(View.GONE);

            }
        });
    }


    private void uploadStatus(String encodedImage, String id) {
        loadingDialog.show();
        map.put("time", String.valueOf(System.currentTimeMillis()));
        map.put("id", id);
        map.put("statusImg", encodedImage);
        Call<MessageModel> call = apiInterface.uploadStatus(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {
                    addStatusDialog.dismiss();
                    loadingDialog.dismiss();


                    map.put("userId", id);
                    pageViewModel = new ViewModelProvider(requireActivity(), new ModelFactory(requireActivity().getApplication(), map)).get(PageViewModel.class);
                    pageViewModel.fetchMyStatus().observe(requireActivity(), statusModelList -> {
                        if (!statusModelList.getData().isEmpty()) {
                            imageView.setVisibility(View.GONE);
                            circularStatusView.setVisibility(View.VISIBLE);
                            for (MyStatusModel m : statusModelList.getData()) {
                                timeSt = String.valueOf(TimeUtils.getTimeAgo(Long.valueOf(m.getTime())));
                                statusTime.setText(timeSt);
                                img = m.getImage();
                                myStatusId = m.getId();
                            }

                            CommonMethod.schedule(myStatusId, img);

                        }
                    });


                    setMyStatus();
                    Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(requireActivity(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());


            }
        });

        Log.d("encodedStatusImage", encodedImage);

    }

    /* E0CF1AD5FB08*/

    private void setMyStatus() {
        userImage = Paper.book().read(Prevalent.userImage);
        String userName = Paper.book().read(Prevalent.userName);
        map.put("userId", id);
        Log.d("id", id);
        pageViewModel = new ViewModelProvider(this, new ModelFactory(requireActivity().getApplication(), map)).get(PageViewModel.class);
        pageViewModel.fetchMyStatus().observe(requireActivity(), statusModelList -> {
            if (!statusModelList.getData().isEmpty()) {
                Log.d("statusList", statusModelList.getData().toString());
                imageView.setVisibility(View.GONE);
                circularStatusView.setVisibility(View.VISIBLE);
                for (MyStatusModel m : statusModelList.getData()) {
                    timeSt = String.valueOf(TimeUtils.getTimeAgo(Long.valueOf(m.getTime())));
                    statusTime.setText(timeSt);
                    img = m.getImage();
                    myStatusId = m.getId();
                    Glide.with(requireActivity()).load("https://gedgetsworld.in/PM_Kisan_Yojana/User_Status_Images/" + m.getImage()).into(userProfileImage);
                }

                binding.imageView4.setVisibility(View.VISIBLE);
                binding.menuClick.setOnClickListener(v -> {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
                    builder.setTitle("Delete")
                            .setIcon(R.drawable.ic_baseline_report_problem_24)
                            .setMessage("Do You Really Want To Delete Your status?")
                            .setNeutralButton("NO", (dialog, which) -> {
                            });
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        map.put("statusId", myStatusId);
                        map.put("statusImg", "User_Status_Images/" + img);
                        deleteMyStatus(map, userImage);
                    });
                    builder.show();
                });

                binding.txtClickToAdd.setClickable(false);
                binding.uploadStatusLayout.setOnClickListener(v -> {
                    Intent intent = new Intent(requireActivity(), ShowStatusActivity.class);
                    intent.putExtra("status", "MyStatus");
                    intent.putExtra("userImage", userImage);
                    intent.putExtra("userImage", userImage);
                    intent.putExtra("userName", userName);
                    intent.putExtra("statusImage", img);
                    intent.putExtra("time", timeSt);
                    intent.putExtra("statusId", myStatusId);
                    startActivity(intent);

                });

                binding.imageView4.setVisibility(View.VISIBLE);
                binding.imageView4.setOnClickListener(v -> {


                });

            }
        });


    }


    private void deleteMyStatus(Map<String, String> map, String userImage) {
        Call<MessageModel> call = apiInterface.deleteMyStatus(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {
                    Log.d("deleteStatus", response.body().getMessage());
                    imageView.setVisibility(View.VISIBLE);

                    CommonMethod.cancelJob(CommonMethod.jobId);
                    binding.txtClickToAdd.setText("Click to add today's image");
                    Glide.with(requireActivity()).load(
                            "https://gedgetsworld.in/PM_Kisan_Yojana/User_Profile_Images/"
                                    + userImage).into(userProfileImage);

                    binding.circularStatusView.setVisibility(View.GONE);
                    binding.imageView4.setVisibility(View.GONE);
                    binding.uploadStatusLayout.setOnClickListener(v -> {
                        launcher.launch("image/*");
                    });

                } else {
                    Log.d("StatusError", response.body().getMessage());

                }

            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("deleteStatusError", t.getMessage());


            }
        });

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void uploadProfileDialog(Context context) {
        uploadProfileDialog = new Dialog(context);
        uploadProfileDialog.setContentView(R.layout.create_profile_layout);
        uploadProfileDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        uploadProfileDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.item_bg));
        uploadProfileDialog.setCancelable(true);

        chooseImg = uploadProfileDialog.findViewById(R.id.choose_img);
        userName = uploadProfileDialog.findViewById(R.id.enter_name);
        uploadProfileBtn = uploadProfileDialog.findViewById(R.id.upload_profile_btn);

        uploadProfileBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String uName = userName.getText().toString().trim();
            UUID uuid = UUID.randomUUID();
            String randomId = String.valueOf(uuid);

            if (TextUtils.isEmpty(encodedImage)) {
                Toast.makeText(requireActivity(), "Please Select an Image", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(uName)) {
                userName.setError("required field");
                loadingDialog.dismiss();
            } else {
                Paper.book().write(Prevalent.userId, randomId);
                Paper.book().write(Prevalent.userName, uName);
                Log.d("prevalent", Paper.book().read(Prevalent.userName)
                        + " " + Paper.book().read(Prevalent.userId));


                map.put("id", randomId);
                map.put("img", encodedImage);
                map.put("userName", uName);
                uploadProfile(map);
            }
        });

        chooseImg.setOnClickListener(v -> {
            showImagePicDialog();
        });
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
                    setStatusProfile();
                    binding.createAcBtn.setVisibility(View.GONE);
                    binding.uploadStatusLayout.setVisibility(View.VISIBLE);


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

    public void setStatusProfile() {
        id = Paper.book().read(Prevalent.userId);
        map.put("id", id);
        pageViewModel = new ViewModelProvider(this, new ModelFactory(requireActivity().getApplication(), map)).get(PageViewModel.class);
        pageViewModel.getUserData().observe(requireActivity(), profileModelList -> {
            if (!profileModelList.getData().isEmpty()) {
                for (ProfileModel pm : profileModelList.getData()) {
                    //txtUserName.setText(pm.getUserName().toString().trim());
                    Glide.with(this).load(
                            "https://gedgetsworld.in/PM_Kisan_Yojana/User_Profile_Images/"
                                    + pm.getUserImage()).into(userProfileImage);

                    binding.circularStatusView.setVisibility(View.GONE);

                    Paper.book().write(Prevalent.userImage, pm.getUserImage());
                    Paper.book().write(Prevalent.userName, pm.getUserName());

                }
                setMyStatus();
                showAllStatus();
            }
        });
    }

    private void showImagePicDialog() {
        String[] options = { "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Pick Image From");
        builder.setItems(options, (dialog, which) -> {
             if (which == 0) {
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

    // Requesting camera permission
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }


    // Here we will pick image from gallery or camera
    private void pickFromGallery() {
        CropImage.activity().start(requireActivity());
    }


    @Override
    public void onStatusClicked(StatusModel statusModel) {
        Intent intent = new Intent(requireActivity(), ShowStatusActivity.class);
        intent.putExtra("status", "allStatus");
        intent.putExtra("userImage", statusModel.getProfileImage());
        intent.putExtra("userName", statusModel.getProfileName());
        intent.putExtra("statusImage", statusModel.getImage());
        intent.putExtra("time", String.valueOf(TimeUtils.getTimeAgo(Long.valueOf(statusModel.getTime()))));

        startActivity(intent);
        id = Paper.book().read(Prevalent.userId);
        map.put("userId", id);
        map.put("statusId", statusModel.getId());
        map.put("statusTime", String.valueOf(System.currentTimeMillis()));

        uploadSeenBy(map);
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, statusModel.getProfileName());
        firebaseAnalytics.logEvent("Status_click_Event", bundle);


    }

    private void uploadSeenBy(Map<String, String> map) {

        Call<MessageModel> call = apiInterface.uploadSeenBy(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Log.d("statusSeenBy", response.body().getMessage());
                } else {
                    Log.d("statusSeenBy", response.body().getError());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Log.d("statusSeenByError", t.getMessage());

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        id = Paper.book().read(Prevalent.userId);
        if (id != null) {
            map.put("id", id);
            showAllStatus();
            setMyStatus();
            binding.uploadStatusLayout.setVisibility(View.VISIBLE);
            binding.createAcBtn.setVisibility(View.GONE);
        } else {
            binding.uploadStatusLayout.setVisibility(View.GONE);
            binding.createAcBtn.setVisibility(View.VISIBLE);
        }
    }
}