package com.pmkisanyojana.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.pmkisanyojana.R;
import com.pmkisanyojana.models.StatusModel;
import com.pmkisanyojana.models.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    List<StatusModel> statusModelList = new ArrayList<>();
    StatusClickListener listener;

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
        Glide.with(holder.itemView.getContext()).load("https://gedgetsworld.in/PM_Kisan_Yojana/User_Status_Images/"+statusModelList.get(position).getImage()).into(holder.circleImageView);
        holder.name.setText(statusModelList.get(position).getProfileName());
        String time = TimeUtils.getTimeAgo(Long.valueOf(statusModelList.get(position).getTime()));
        holder.time.setText(time);

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
