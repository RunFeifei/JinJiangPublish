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
import com.uestc.jinjiang.publish.bean.FileTypeEnum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PengFeifei
 * @Description TODO
 * @date 2021/6/5
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.RecyclerHolder> {
    private Context mContext;
    private OnItemClickListener clickListener;
    private View.OnClickListener clickMoreListener;
    private final List<FileDisplayInfo> dataList = new ArrayList<>();

    public ListAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }

    public void addData(FileDisplayInfo data) {
        if (null == data) {
            return;
        }
        this.dataList.add(data);
        notifyDataSetChanged();
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
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener == null) {
                    return;
                }
                clickListener.onItemClickListener(v, dataList.get(recyclerHolder.getAdapterPosition()), recyclerHolder.getAdapterPosition());
            }
        });
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        FileDisplayInfo fileDisplayInfo = dataList.get(position);
        holder.textDesc.setText(fileDisplayInfo.getFileDesc());
        holder.textTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fileDisplayInfo.getFileTime()));
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickMoreListener != null) {
                    clickMoreListener.onClick(holder.imgMore);
                }
            }
        });
        int ic = R.drawable.icon_file_unknown;
        ic = fileDisplayInfo.getFileType().equals(FileTypeEnum.FILE_TYPE_PDF.getCode()) ? R.drawable.ic_pdf : ic;
        ic = fileDisplayInfo.getFileType().equals(FileTypeEnum.FILE_TYPE_PPT.getCode()) ? R.drawable.ic_ppt : ic;
        ic = fileDisplayInfo.getFileType().equals(FileTypeEnum.FILE_TYPE_DOC.getCode()) ? R.drawable.ic_doc : ic;
        ic = fileDisplayInfo.getFileType().equals(FileTypeEnum.FILE_TYPE_VIDEO.getCode()) ? R.drawable.ic_video : ic;
        holder.imgFile.setImageResource(ic);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView textDesc;
        ImageView imgFile;
        TextView textTime;
        View imgMore;

        private RecyclerHolder(View itemView) {
            super(itemView);
            textDesc = (TextView) itemView.findViewById(R.id.textDesc);
            textTime = (TextView) itemView.findViewById(R.id.textTime);
            imgFile = (ImageView) itemView.findViewById(R.id.imgFile);
            imgMore = itemView.findViewById(R.id.imgMore);
        }
    }

    public OnItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public View.OnClickListener getClickMoreListener() {
        return clickMoreListener;
    }

    public void setClickMoreListener(View.OnClickListener clickMoreListener) {
        this.clickMoreListener = clickMoreListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, FileDisplayInfo fileDisplayInfo, int position);
    }
}
