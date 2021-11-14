package com.pmkisanyojanaadmin.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.activities.ShowItemActivity;
import com.pmkisanyojanaadmin.model.ApiInterface;
import com.pmkisanyojanaadmin.model.ApiWebServices;
import com.pmkisanyojanaadmin.model.MessageModel;
import com.pmkisanyojanaadmin.model.YojanaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YojanaAdapter extends RecyclerView.Adapter<YojanaAdapter.ViewHolder> {

    YojanaInterface yojanaInterface;
    List<YojanaModel> yojanaModelList = new ArrayList<>();
    String adapterName;
    ApiInterface apiInterface;
    ShowItemActivity showItemActivity;


    public YojanaAdapter(ShowItemActivity showItemActivity, String adapterName, YojanaInterface yojanaInterface) {
        this.showItemActivity = showItemActivity;
        this.yojanaInterface = yojanaInterface;
        this.adapterName = adapterName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(showItemActivity).inflate(R.layout.yojna_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        holder.yojanaTitle.setText(yojanaModelList.get(position).getTitle());
        Glide.with(showItemActivity).load("https://gedgetsworld.in/PM_Kisan_Yojana/Kisan_Yojana_Images/" + yojanaModelList.get(position).getImage()).apply(requestOptions).into(holder.yojanaImage);
        boolean isCheck = Boolean.parseBoolean(yojanaModelList.get(position).getPinned());
        if (isCheck) {
            holder.switchMaterial.setChecked(true);
        } else {
            holder.switchMaterial.setChecked(false);
        }
        if (adapterName.equals("others")) {
            holder.switchMaterial.setVisibility(View.GONE);
        } else {
            holder.switchMaterial.setVisibility(View.VISIBLE);

            holder.switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String pinStatus;
                if (isChecked) {
                    pinStatus = "Item Pinned";

                } else {
                    pinStatus = "Item not Pin";
                }
                uploadPinStatus(pinStatus, isChecked, yojanaModelList.get(position).getId());
            });
            holder.itemView.setOnClickListener(v -> yojanaInterface.onItemClicked(yojanaModelList.get(position)));

        }
    }

    private void uploadPinStatus(String pinStatus, boolean pinnedItem, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("pinStatus", String.valueOf(pinnedItem));
        map.put("id", id);
        apiInterface = ApiWebServices.getApiInterface();
        Call<MessageModel> call = apiInterface.uploadPinStatus(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getMessage().equals("Data Uploaded")) {
                        Toast.makeText(showItemActivity, pinStatus, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });


    }


    @Override
    public int getItemCount() {
        return yojanaModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateYojanaList(List<YojanaModel> yojanaModels) {
        yojanaModelList.clear();
        yojanaModelList.addAll(yojanaModels);
        notifyDataSetChanged();
    }


    public interface YojanaInterface {

        void onItemClicked(YojanaModel yojanaModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SwitchMaterial switchMaterial;
        TextView yojanaTitle;
        ImageView yojanaImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            yojanaTitle = itemView.findViewById(R.id.yojanaTitle);
            yojanaImage = itemView.findViewById(R.id.yojnaImage);
            switchMaterial = itemView.findViewById(R.id.pin_switch);


        }


    }
}
