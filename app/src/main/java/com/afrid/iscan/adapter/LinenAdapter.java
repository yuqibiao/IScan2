package com.afrid.iscan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afrid.iscan.R;

/**
 * 功能：扫描后得条目对应得Adapter
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class LinenAdapter extends RecyclerView.Adapter<LinenAdapter.LinenViewHolder>{

    private Context mContext;

    public LinenAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public LinenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.rv_item_linen ,parent , false);
        LinenViewHolder viewHolder   = new LinenViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LinenViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public static class LinenViewHolder extends RecyclerView.ViewHolder{

        public LinenViewHolder(View itemView) {
            super(itemView);
        }

    }
}
