package com.uestc.jinjiang.publish.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uestc.jinjiang.publish.R;
import com.uestc.jinjiang.publish.bean.FileDisplayInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/6/5
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.RecyclerHolder> {
    private Context mContext;
    private List<FileDisplayInfo> dataList = new ArrayList<>();

    public ListAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    public void setData(List<FileDisplayInfo> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.textDesc.setText(dataList.get(position).getFileDesc());
        holder.textTime.setText(dataList.get(position).getFileTime() + "");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView textDesc;
        ImageView imgFile;
        TextView textTime;

        private RecyclerHolder(View itemView) {
            super(itemView);
            textDesc = (TextView) itemView.findViewById(R.id.textDesc);
            textTime = (TextView) itemView.findViewById(R.id.textTime);
            imgFile = (ImageView) itemView.findViewById(R.id.imgFile);
        }
    }
}
