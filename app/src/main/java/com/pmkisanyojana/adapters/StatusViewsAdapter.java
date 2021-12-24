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
import com.pmkisanyojana.models.StatusViewModel;
import com.pmkisanyojana.models.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusViewsAdapter extends RecyclerView.Adapter<StatusViewsAdapter.ViewHolder> {

    List<StatusViewModel> statusViewModelList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load("https://gedgetsworld.in/PM_Kisan_Yojana/User_Profile_Images/" + statusViewModelList.get(position).getProfileImage()).into(holder.circleImageView);
        holder.name.setText(statusViewModelList.get(position).getProfileName());
        String time = TimeUtils.getTimeAgo(Long.valueOf(statusViewModelList.get(position).getStatusTime()));
        holder.time.setText(time);


    }

    @Override
    public int getItemCount() {
        return statusViewModelList.size();
    }

    public void updateStatusViewsList(List<StatusViewModel> statusModelLis) {
        statusViewModelList.clear();
        statusViewModelList.addAll(statusModelLis);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        CircularStatusView circularStatusView;
        TextView name, time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.user_status_img);
            circularStatusView = itemView.findViewById(R.id.circular_status_view);
            name = itemView.findViewById(R.id.txt_status_title);
            time = itemView.findViewById(R.id.txt_click_to_add);
            circularStatusView.setVisibility(View.GONE);
        }
    }
}
