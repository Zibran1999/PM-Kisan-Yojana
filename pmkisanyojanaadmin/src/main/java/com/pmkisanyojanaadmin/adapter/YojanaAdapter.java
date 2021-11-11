package com.pmkisanyojanaadmin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.model.YojanaModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YojanaAdapter extends RecyclerView.Adapter<YojanaAdapter.ViewHolder> {

    Context context;
    YojanaInterface yojanaInterface;
    List<YojanaModel> yojanaModelList = new ArrayList<>();
    boolean isEnable = false;
    boolean isSelectAll = false;
    ArrayList<YojanaModel> selectList = new ArrayList<>();


    public YojanaAdapter(Context context, YojanaInterface yojanaInterface) {
        this.context = context;
        this.yojanaInterface = yojanaInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.yojna_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.yojanaTitle.setText(yojanaModelList.get(position).getTitle());
        Glide.with(context).load("https://gedgetsworld.in/PM_Kisan_Yojana/Kisan_Yojana_Images/" + yojanaModelList.get(position).getImage()).into(holder.yojanaImage);

        holder.itemView.setOnClickListener(v -> yojanaInterface.onItemClicked(yojanaModelList.get(position)));

//        holder.itemView.setOnLongClickListener(v -> {
//            yojanaInterface.onItemClicked(yojanaModelList.get(position));
//            if (!isEnable) {
//
//                ActionMode.Callback callback = new ActionMode.Callback() {
//                    @Override
//                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                        MenuInflater menuInflater = mode.getMenuInflater();
//                        menuInflater.inflate(R.menu.menu, menu);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//
//                        isEnable = true;
//                        ClickItem(holder);
//
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                        int id = item.getItemId();
//                        switch (id) {
//                            case R.id.delete:
//                                for (YojanaModel m : selectList) {
//                                    yojanaModelList.remove(m);
//                                }
//                                mode.finish();
//                                break;
//                            case R.id.menu_selectAll:
//                                if (selectList.size() == yojanaModelList.size()) {
//                                    isSelectAll = false;
//                                    selectList.clear();
//                                } else {
//                                    isSelectAll = true;
//                                    selectList.clear();
//                                    selectList.addAll(yojanaModelList);
//                                }
//                                notifyDataSetChanged();
//                                break;
//                        }
//                        return true;
//                    }
//
//                    @Override
//                    public void onDestroyActionMode(ActionMode mode) {
//                        isEnable = false;
//                        isSelectAll = false;
//                        selectList.clear();
//                        notifyDataSetChanged();
//
//                    }
//                };
//                ((AppCompatActivity)context).startActionMode(callback);
//
//            }else {
//                ClickItem(holder);
//            }
//
//            return true;
//        });
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
        Collections.reverse(yojanaModelList);
        notifyDataSetChanged();
    }

    public interface YojanaInterface {

        void onItemClicked(YojanaModel yojanaModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView yojanaTitle;
        ImageView yojanaImage, checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            yojanaTitle = itemView.findViewById(R.id.yojanaTitle);
            yojanaImage = itemView.findViewById(R.id.yojnaImage);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
