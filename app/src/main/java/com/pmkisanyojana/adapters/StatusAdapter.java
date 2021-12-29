package com.pmkisanyojana.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.pmkisanyojana.R;
import com.pmkisanyojana.models.ApiInterface;
import com.pmkisanyojana.models.ApiWebServices;
import com.pmkisanyojana.models.MessageModel;
import com.pmkisanyojana.models.StatusModel;
import com.pmkisanyojana.models.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    List<StatusModel> statusModelList = new ArrayList<>();
    StatusClickListener listener;
    Map<String, String> map = new HashMap<>();
    ApiInterface apiInterface = ApiWebServices.getApiInterface();

    public StatusAdapter(StatusClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load("https://gedgetsworld.in/PM_Kisan_Yojana/User_Status_Images/" + statusModelList.get(position).getImage()).into(holder.circleImageView);
        holder.name.setText(statusModelList.get(position).getProfileName());
        String time = TimeUtils.getTimeAgo(Long.valueOf(statusModelList.get(position).getTime()));
        holder.time.setText(time);
        if (time.equals("just now") || time.equals("a minute ago") || time.equals("an hour ago")) {
            Log.d("timeString", time);
        } else {
            int timeD = Integer.parseInt(time.replaceAll("[^0-9]", ""));
            if (timeD >= 24) {
                map.put("statusId", statusModelList.get(position).getId());
                map.put("statusImg", "User_Status_Images/" + statusModelList.get(position).getImage());
                deleteMyStatus(map, holder.itemView.getContext());
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onStatusClicked(statusModelList.get(position)));


    }

    @Override
    public int getItemCount() {
        return statusModelList.size();
    }

    public void updateStatusList(List<StatusModel> statusModelLis) {
        statusModelList.clear();
        statusModelList.addAll(statusModelLis);
        notifyDataSetChanged();
    }

    private void deleteMyStatus(Map<String, String> map, Context context) {
        Call<MessageModel> call = apiInterface.deleteMyStatus(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {
                    Log.d("deleteStatus", response.body().getMessage());
                } else {
                    Log.d("StatusError", response.body().getMessage());

                }

            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("deleteStatusError", t.getMessage());


            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        CircularStatusView circularStatusView;
        TextView name, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.user_status_img);
            circularStatusView = itemView.findViewById(R.id.circular_status_view);
            name = itemView.findViewById(R.id.txt_status_title);
            time = itemView.findViewById(R.id.txt_click_to_add);
        }
    }
}
