package com.afrid.iscan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afrid.iscan.R;
import com.afrid.iscan.bean.HotelSource;
import com.afrid.iscan.bean.SpOffice;

import java.util.List;

/**
 * 功能：部门选择数据Adapter
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder>{

    private Context mContext;
    private List<SpOffice> deptList;

    public DepartmentsAdapter(Context context){
        this.mContext = context;
    }

    public DepartmentsAdapter(Context context ,List<SpOffice> deptList){
        this.mContext = context;
        this.deptList = deptList;
    }

    @Override
    public DepartmentsAdapter.DepartmentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(mContext).inflate(R.layout.rv_item_department , parent , false);
        DepartmentsViewHolder viewHolder = new DepartmentsViewHolder(item);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DepartmentsAdapter.DepartmentsViewHolder holder, int position) {
        holder.getTv_dept_name().setText(deptList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return deptList==null ? 0 : deptList.size();
    }

    public void setDeptList(List<SpOffice> deptList) {
        this.deptList = deptList;
        notifyDataSetChanged();
    }

    public static class DepartmentsViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_dept_name;

        public DepartmentsViewHolder(View itemView) {
            super(itemView);
            tv_dept_name = (TextView) itemView.findViewById(R.id.tv_dept_name);
        }

        public TextView getTv_dept_name() {
            return tv_dept_name;
        }
    }


}
