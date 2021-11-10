package com.pmkisanyojana.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pmkisanyojana.R;
import com.pmkisanyojana.models.YojanaModel;
import com.pmkisanyojana.models.YojanaTypeModel;

import java.util.ArrayList;
import java.util.List;

public class YojanaTypeAdapter extends RecyclerView.Adapter implements YojanaAdapter.YojanaInterface {

    List<YojanaTypeModel> yojanaTypeModels = new ArrayList<>();
    Context context;

    public YojanaTypeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (yojanaTypeModels.get(position).getType()) {
            case 0:
                return YojanaTypeModel.PinnedItem;
            case 1:
                return YojanaTypeModel.OtherItem;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case YojanaTypeModel.PinnedItem:
                View view = LayoutInflater.from(context).inflate(R.layout.yojana_rv_layout, parent, false);
                return new PinnedItems(view);
            case YojanaTypeModel.OtherItem:
                View otherIiew = LayoutInflater.from(context).inflate(R.layout.yojana_rv_layout, parent, false);
                return new OtherItems(otherIiew);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (yojanaTypeModels.get(position).getType()) {
            case YojanaTypeModel.PinnedItem:
                List<YojanaModel> yojanaModelList = yojanaTypeModels.get(position).getPinnedYojanaList();

                YojanaAdapter yojanaAdapter = new YojanaAdapter(context, this);
                yojanaAdapter.updateYojanaList(yojanaModelList);
//                for (YojanaModel m:yojanaModelList) {
//                    ((PinnedItems) holder).yojanaTitle.setText(m.getTitle());
//                    RequestOptions requestOptions = new RequestOptions()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL);
//                    Glide.with(context).load("https://gedgetsworld.in/PM_Kisan_Yojana/Kisan_Yojana_Images/" + m.getImage()).apply(requestOptions).into(((PinnedItems) holder).yojanaImage);
//                    //holder.itemView.setOnClickListener(v -> yojanaInterface.onItemClicked(yojanaModelList.get(position)));
//
//
//                }
                break;
            case YojanaTypeModel.OtherItem:
                List<YojanaModel> yojanaModels = yojanaTypeModels.get(position).getYojanaModelList();

                YojanaAdapter yojanaAdap = new YojanaAdapter(context, this);
                yojanaAdap.updateYojanaList(yojanaModels);
                ((OtherItems)holder).YojanaRv.setAdapter(yojanaAdap);
//                for (YojanaModel m:yojanaModels) {
//                    ((OtherItems) holder).yojanaTitle.setText(m.getTitle());
//                    RequestOptions requestOptions = new RequestOptions()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL);
//                    Glide.with(context).load("https://gedgetsworld.in/PM_Kisan_Yojana/Kisan_Yojana_Images/" + m.getImage()).apply(requestOptions).into(((OtherItems) holder).yojanaImage);
//                    //holder.itemView.setOnClickListener(v -> yojanaInterface.onItemClicked(yojanaModelList.get(position)));
//
//
//                }
                break;
            default:
        }

    }

    @Override
    public int getItemCount() {
        return yojanaTypeModels.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateYojanaList(List<YojanaTypeModel> yojanaTypeModelList) {
        yojanaTypeModels.clear();
        yojanaTypeModels.addAll(yojanaTypeModelList);
//        Collections.reverse(yojanaTypeModels);
        notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(YojanaModel yojanaModel) {

    }

    public static class PinnedItems extends RecyclerView.ViewHolder {
      RecyclerView YojanaRv;
        public PinnedItems(@NonNull View itemView) {
            super(itemView);
            YojanaRv  =itemView.findViewById(R.id.yojanaRv);
        }
    }

    public static class OtherItems extends RecyclerView.ViewHolder {
        RecyclerView YojanaRv;


        public OtherItems(@NonNull View itemView) {
            super(itemView);
            YojanaRv  =itemView.findViewById(R.id.yojanaRv);

        }
    }
}
