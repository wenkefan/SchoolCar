package com.fwk.school4.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fwk.school4.R;
import com.fwk.school4.model.ChildBean;

import java.util.List;

/**
 * Created by fanwenke on 2017/3/2.
 */

public class FinishAdapter extends BaseRecyclerAdapter {

    private List<ChildBean.RerurnValueBean> chilList;

    public FinishAdapter(List<ChildBean.RerurnValueBean> chilList) {
        this.chilList = chilList;
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new FinishAdapter.ViewHolde(LayoutInflater.from(parent.getContext()).inflate(R.layout.childrecyadapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if (holder instanceof ViewHolde){
            ViewHolde holde = (ViewHolde) holder;
            holde.tv.setText(chilList.get(position).getChildName());
            holde.claNa.setText(chilList.get(position).getClassName());
        }
        super.onBindViewHolder(holder, position);


    }

    @Override
    public int getItemCount() {
        return chilList.size();
    }

    public class ViewHolde extends ClickableViewHolder {

        private TextView tv, claNa;
        private ImageView iv;

        public ViewHolde(View itemView) {
            super(itemView);
            tv = $(R.id.tv_child_name);
            iv = $(R.id.iv_type_select);
            claNa = $(R.id.tv_child_class_name);

        }
    }
}
