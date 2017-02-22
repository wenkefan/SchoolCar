package com.fwk.school4.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fwk.school4.R;
import com.fwk.school4.model.ChildBean;
import com.fwk.school4.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanwenke on 16/12/26.
 */

public class ChildRecyAdapter extends BaseRecyclerAdapter {
    private Context context;
    private List<ChildBean.RerurnValueBean> list = new ArrayList<>();
    private boolean color;
    private boolean bool;

    public ChildRecyAdapter(List<ChildBean.RerurnValueBean> list) {
        this.list = list;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ChildViewHolde(LayoutInflater.from(parent.getContext()).inflate(R.layout.childrecyadapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ChildViewHolde) {
            ChildViewHolde holde = (ChildViewHolde) holder;
            holde.tv.setText(list.get(position).getChildName());
            holde.claNa.setText(list.get(position).getClassName());
            holde.tv.setTextColor(context.getResources().getColor(R.color.darker_gray));
            holde.claNa.setTextColor(context.getResources().getColor(R.color.darker_gray));
            switch (list.get(position).getSelectid()) {
                case 0:
                    holde.iv.setBackgroundResource(R.mipmap.bianji);
                    break;
                case 1:
                    holde.iv.setBackgroundResource(R.mipmap.shangche);
                    break;
                case 2:
                    holde.iv.setBackgroundResource(R.mipmap.bingjia);
                    break;
                case 3:
                    holde.iv.setBackgroundResource(R.mipmap.shijia);
                    break;
                case 4:
                    holde.iv.setBackgroundResource(R.mipmap.jiazhangjiesong);
                    break;
                case 5:
                    holde.iv.setBackgroundResource(R.mipmap.xiache);
                    break;
            }

            if (color){
                if (list.get(position).getSelectid() == 0) {
                    holde.claNa.setTextColor(context.getResources().getColor(R.color.black));
                    holde.tv.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
            if (bool){
                if (list.get(position).getIsDU() == 2 && list.get(position).getSelectid() == 0){
                    holde.claNa.setTextColor(context.getResources().getColor(R.color.black));
                    holde.tv.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setColorl(boolean b) {
        color = b;
    }

    public class ChildViewHolde extends ClickableViewHolder {

        private TextView tv,claNa;
        private ImageView iv;

        public ChildViewHolde(View itemView) {
            super(itemView);
            tv = $(R.id.tv_child_name);
            iv = $(R.id.iv_type_select);
            claNa = $(R.id.tv_child_class_name);
        }
    }
    public void setBool(boolean bool){
        this.bool = bool;
        notifyDataSetChanged();
    }
}
