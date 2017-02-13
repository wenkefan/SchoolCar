package com.fwk.school4.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fwk.school4.R;
import com.fwk.school4.model.ChildBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanwenke on 2017/2/10.
 */

public class ResidueAdapter extends BaseRecyclerAdapter {
    private Context context;
    private List<ChildBean.RerurnValueBean> list = new ArrayList<>();

    public ResidueAdapter(List<ChildBean.RerurnValueBean> list) {
        this.list = list;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ResidueViewHolde(LayoutInflater.from(parent.getContext()).inflate(R.layout.residue_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ResidueAdapter.ResidueViewHolde) {
            ResidueViewHolde holde = (ResidueViewHolde) holder;
            holde.tv.setText(list.get(position).getChildName());
            holde.claNa.setText(list.get(position).getClassName());
            holde.tv.setTextColor(context.getResources().getColor(R.color.darker_gray));
            holde.claNa.setTextColor(context.getResources().getColor(R.color.darker_gray));
            switch (list.get(position).getSelectid()) {
                case 0:
                    holde.iv.setBackgroundResource(R.mipmap.bianji);
                    break;
//                case 1:
//                    holde.iv.setBackgroundResource(R.mipmap.shangche);
//                    break;
//                case 2:
//                    holde.iv.setBackgroundResource(R.mipmap.bingjia);
//                    break;
//                case 3:
//                    holde.iv.setBackgroundResource(R.mipmap.shijia);
//                    break;
//                case 4:
//                    holde.iv.setBackgroundResource(R.mipmap.jiazhangjiesong);
//                    break;
                case 5:
                    holde.iv.setBackgroundResource(R.mipmap.xiache);
                    break;
            }

            if (list.get(position).getSelectid() == 0) {
                holde.claNa.setTextColor(context.getResources().getColor(R.color.black));
                holde.tv.setTextColor(context.getResources().getColor(R.color.black));
            }
        }
        super.onBindViewHolder(holder, position);
    }

    public void getData(List<ChildBean.RerurnValueBean> residueList){
        this.list = residueList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ResidueViewHolde extends ClickableViewHolder {

        private TextView tv, claNa;
        private ImageView iv;

        public ResidueViewHolde(View itemView) {
            super(itemView);
            tv = $(R.id.tv_child_name);
            iv = $(R.id.iv_type_select);
            claNa = $(R.id.tv_child_class_name);
        }
    }
}
