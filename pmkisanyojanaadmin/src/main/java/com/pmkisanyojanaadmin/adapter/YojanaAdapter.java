package com.pmkisanyojanaadmin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.model.ApiInterface;
import com.pmkisanyojanaadmin.model.ApiWebServices;
import com.pmkisanyojanaadmin.model.MessageModel;
import com.pmkisanyojanaadmin.model.YojanaModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YojanaAdapter extends RecyclerView.Adapter<YojanaAdapter.ViewHolder> {

    Context context;
    YojanaInterface yojanaInterface;
    List<YojanaModel> yojanaModelList = new ArrayList<>();
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<YojanaModel> selectList = new ArrayList<>();
    String adapterName;
    ApiInterface apiInterface;


    public YojanaAdapter(Context context, String adapterName, YojanaInterface yojanaInterface) {
        this.context = context;
        this.yojanaInterface = yojanaInterface;
        this.adapterName = adapterName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.yojna_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        holder.yojanaTitle.setText(yojanaModelList.get(position).getTitle());
        Glide.with(context).load("https://gedgetsworld.in/PM_Kisan_Yojana/Kisan_Yojana_Images/" + yojanaModelList.get(position).getImage()).apply(requestOptions).into(holder.yojanaImage);
        boolean isCheck = Boolean.parseBoolean(yojanaModelList.get(position).getPinned());
        if (isCheck) {
            holder.switchMaterial.setChecked(true);
        }
        if (adapterName.equals("others")) {
            holder.switchMaterial.setVisibility(View.GONE);
        } else
            holder.switchMaterial.setVisibility(View.VISIBLE);

        holder.switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean pinnedItem = isChecked;
            String pinStatus;
            if (pinnedItem) {
                pinStatus = "Item Pinned";

            } else {
                pinStatus = "Item not Pin";


            }
            uploadPinStatus(pinStatus, pinnedItem, yojanaModelList.get(position).getId());
        });
        holder.itemView.setOnClickListener(v -> yojanaInterface.onItemClicked(yojanaModelList.get(position)));


        holder.itemView.setOnLongClickListener(v -> {
            yojanaInterface.onItemClicked(yojanaModelList.get(position));
            if (!isEnable) {

                ActionMode.Callback callback = new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater menuInflater = mode.getMenuInflater();
                        menuInflater.inflate(R.menu.menu, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                        isEnable = true;
                        ClickItem(holder);

                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.delete:
                                for (YojanaModel m : selectList) {
                                    yojanaModelList.remove(m);
                                }
                                mode.finish();
                                break;
                            case R.id.menu_selectAll:
                                if (selectList.size() == yojanaModelList.size()) {
                                    isSelectAll = false;
                                    selectList.clear();
                                } else {
                                    isSelectAll = true;
                                    selectList.clear();
                                    selectList.addAll(yojanaModelList);
                                }
                                notifyDataSetChanged();
                                break;
                        }
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        isEnable = false;
                        isSelectAll = false;
                        selectList.clear();
                        notifyDataSetChanged();

                    }
                };
                ((AppCompatActivity)context).startActionMode(callback);

            }else {
                ClickItem(holder);
            }

            return true;
        });
//
//
//        holder.itemView.setOnClickListener(v -> {
//            if ((isEnable)){
//                ClickItem(holder);
//            }else {
//                Toast.makeText(context, "You Clicked "+yojanaModelList.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if (isSelectAll){
//
//            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.itemView.setBackgroundColor(Color.LTGRAY);
//        }else {
//            holder.checkBox.setVisibility(View.GONE);
//        }
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
                        Toast.makeText(context, pinStatus, Toast.LENGTH_SHORT).show();
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

    private void ClickItem(ViewHolder holder) {
        YojanaModel m = yojanaModelList.get(holder.getAdapterPosition());
        if (holder.checkBox.getVisibility() == View.GONE) {

            holder.checkBox.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectList.add(m);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            selectList.remove(m);

        }


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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SwitchMaterial switchMaterial;
        TextView yojanaTitle;
        ImageView yojanaImage, checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            yojanaTitle = itemView.findViewById(R.id.yojanaTitle);
            yojanaImage = itemView.findViewById(R.id.yojnaImage);
            checkBox = itemView.findViewById(R.id.checkbox);
            switchMaterial = itemView.findViewById(R.id.pin_switch);
        }


    }
}
