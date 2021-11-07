package com.pmkisanyojana.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmkisanyojana.R;
import com.pmkisanyojana.models.YojanaModel;

import java.util.List;

public class YojanaAdapter extends RecyclerView.Adapter<YojanaAdapter.ViewHolder> {

    List<YojanaModel> yojnaModelList;
    Context context;

    public YojanaAdapter(List<YojanaModel> yojnaModelList, Context context) {
        this.yojnaModelList = yojnaModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.yojna_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.yojanaTitle.setText(yojnaModelList.get(position).getYojnaName());
        holder.yojanaImage.setImageResource(yojnaModelList.get(position).getYojnaImage());

    }

    @Override
    public int getItemCount() {
        return yojnaModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView yojanaTitle;
        ImageView yojanaImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            yojanaTitle = itemView.findViewById(R.id.yojanaTitle);
            yojanaImage = itemView.findViewById(R.id.yojnaImage);
        }
    }

}
