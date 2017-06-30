package com.afrid.iscan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afrid.iscan.R;

/**
 * 功能：部门选择数据Adapter
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder>{

    private Context mContext;

    public DepartmentsAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public DepartmentsAdapter.DepartmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(mContext).inflate(R.layout.rv_item_department , parent , false);
        DepartmentsViewHolder viewHolder = new DepartmentsViewHolder(item);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DepartmentsAdapter.DepartmentsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 50;
    }

    public static class DepartmentsViewHolder extends RecyclerView.ViewHolder{

        public DepartmentsViewHolder(View itemView) {
            super(itemView);
        }

    }

}
