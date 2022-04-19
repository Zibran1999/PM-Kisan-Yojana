package com.pmkisanyojana.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ironsource.mediationsdk.IronSource;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.adapters.CodesAdapter;
import com.pmkisanyojana.databinding.FragmentStatusBinding;
import com.pmkisanyojana.models.ApiInterface;
import com.pmkisanyojana.models.ApiWebServices;
import com.pmkisanyojana.models.CodesModel;
import com.pmkisanyojana.models.ContestCodeModel;
import com.pmkisanyojana.models.MessageModel;
import com.pmkisanyojana.utils.CommonMethod;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusFragment extends Fragment {


    public static String encodedImage;
    public static CircleImageView userImageView;
    static Bitmap bitmap;
    FragmentStatusBinding binding;
    Button addYojanaBtn;
    Dialog addYojanaDialog, dialog;
    String[] storagePermission;
    PageViewModel pageViewModel;
    List<CodesModel> codesModelList = new ArrayList<>();
    RecyclerView recyclerView;


    public static String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] imageBytes = stream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static void setImage(Uri uri, Context context) {

        if (uri != null) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                userImageView.setImageBitmap(bitmap);
                encodedImage = imageStore(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        addYojanaBtn = binding.addYojanaBtn;
        recyclerView = binding.yojanaContestRecyclerView;
        dialog = CommonMethod.getDialog(requireActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setHasFixedSize(true);
        CommonMethod.getBannerAds(requireActivity(), binding.adViewTop);
        CommonMethod.getBannerAds(requireActivity(), binding.adViewBottom);
        showYojanaData();


        addYojanaBtn.setOnClickListener(v -> AddYojanaDialog(requireContext()));
        return binding.getRoot();
    }

    private void AddYojanaDialog(Context context) {
        addYojanaDialog = new Dialog(context);
        addYojanaDialog.setContentView(R.layout.add_contest_layout);
        addYojanaDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addYojanaDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.item_bg));
        addYojanaDialog.setCancelable(false);
        addYojanaDialog.show();

        EditText userName, yojanaName, yojanaAmount;
        Button cancelBtn, addYojanaBtn;

        userName = addYojanaDialog.findViewById(R.id.user_nameEDT);
        yojanaName = addYojanaDialog.findViewById(R.id.yojana_name);
        yojanaAmount = addYojanaDialog.findViewById(R.id.yojana_amount);
        cancelBtn = addYojanaDialog.findViewById(R.id.cancel_yojanaBtn);
        addYojanaBtn = addYojanaDialog.findViewById(R.id.add_yojanaBtn);

        userImageView = addYojanaDialog.findViewById(R.id.profileImg);
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        userImageView.setOnClickListener(v -> pickFromGallery());
        cancelBtn.setOnClickListener(v -> {
            addYojanaDialog.dismiss();
        });

        addYojanaBtn.setOnClickListener(v -> {
            String uName, yoName, yoAmount;
            uName = userName.getText().toString().trim();
            yoName = yojanaName.getText().toString().trim();
            yoAmount = yojanaAmount.getText().toString().trim();

            if (encodedImage == null) {
                Toast.makeText(requireActivity(), "Please select an image.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(uName)) {
                userName.setError("Field are required");
            } else if (TextUtils.isEmpty(yoName)) {
                yojanaName.setError("Field are required");
            } else if (TextUtils.isEmpty(yoAmount)) {
                yojanaAmount.setError("Field are required");
            } else {
                dialog.show();
                uploadYojanaData(context, encodedImage, uName, yoName, yoAmount);
            }


        });

    }

    private void uploadYojanaData(Context context, String encodedImage, String uName, String yoName, String yoAmount) {
        Map<String, String> map = new HashMap<>();
        map.put("userName", uName);
        map.put("yojanaName", yoName);
        map.put("yojanaAmount", yoAmount);
        map.put("img", encodedImage);

        ApiInterface apiInterface = ApiWebServices.getApiInterface();
        Call<MessageModel> call = apiInterface.getContestRes(map, "https://gedgetsworld.in/PM_Kisan_Yojana/add_yojana_data.php");
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    showYojanaData();
                    assert response.body() != null;
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    addYojanaDialog.dismiss();
                } else {
                    assert response.body() != null;
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {

            }
        });
    }

    private void showYojanaData() {
        pageViewModel = new ViewModelProvider(requireActivity()).get(PageViewModel.class);
        pageViewModel.getContestData().observe(requireActivity(), new Observer<ContestCodeModel>() {
            @Override
            public void onChanged(ContestCodeModel contestCodeModel) {
                if (!contestCodeModel.getData().isEmpty()) {
                    codesModelList.clear();
                    codesModelList.addAll(contestCodeModel.getData());
                    Collections.reverse(codesModelList);
                    CodesAdapter codesAdapter = new CodesAdapter(requireActivity());
                    recyclerView.setAdapter(codesAdapter);
                    codesAdapter.updateCodeModelList(codesModelList);

                }
            }
        });
    }


    private void pickFromGallery() {
        CropImage.activity().start(requireActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(requireActivity());
    }
}