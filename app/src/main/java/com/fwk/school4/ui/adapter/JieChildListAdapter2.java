package com.fwk.school4.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fwk.school4.MyApp;
import com.fwk.school4.R;
import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.ChildBean;
import com.fwk.school4.model.StaBean;
import com.fwk.school4.utils.LogUtils;
import com.fwk.school4.utils.SharedPreferencesUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by fanwenke on 16/12/23.
 */

public class JieChildListAdapter2 extends BaseRecyclerAdapter {
    private Context context;
    private Map<String, List<ChildBean.RerurnValueBean>> map;
    private SharedPreferencesUtils sp = new SharedPreferencesUtils();
    //    private SharedPreferencesUtils2 spData = new SharedPreferencesUtils2();
    private List<StaBean> staBeen;
    private LinearLayoutManager manager;
    private ChildRecyAdapter adapter;
    private List<ChildBean.RerurnValueBean> list;
    private int selStationID;
    private boolean bool;
    private int itmePosition = 0;

    public void getData(int selStationID) {
        this.selStationID = selStationID;
        map = (Map<String, List<ChildBean.RerurnValueBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        staBeen = (List<StaBean>) sp.queryForSharedToObject(Keyword.SELECTSTA);
    }


    public JieChildListAdapter2(int selStationID) {
        this.selStationID = selStationID;
        map = (Map<String, List<ChildBean.RerurnValueBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        staBeen = (List<StaBean>) sp.queryForSharedToObject(Keyword.SELECTSTA);
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolde(LayoutInflater.from(MyApp.getContext()).inflate(R.layout.recyadapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, final int position) {
        if (holder instanceof ViewHolde) {
            ViewHolde viewHolde = (ViewHolde) holder;
            viewHolde.tv.setText(staBeen.get(position).getName());
            list = map.get(staBeen.get(position).getStrid());
            manager = new LinearLayoutManager(context);
            viewHolde.rv.setHasFixedSize(true);
            viewHolde.rv.setLayoutManager(manager);
            if (list != null && list.size() != 0) {
                adapter = new ChildRecyAdapter(list);
                viewHolde.rv.setAdapter(adapter);
                adapter.setBool(bool);
                adapter.setOnItemListener(new OnItemListener() {
                    @Override
                    public void setOnItemListener(int itme, ClickableViewHolder holder) {
                        onItemAdapterListener.setOnItemListener(staBeen.get(position), itme);
                    }
                });
            }

            Drawable drawable1 = context.getResources().getDrawable(R.mipmap.station_1);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            viewHolde.tv.setCompoundDrawables(drawable1, null, null, null);
            viewHolde.tv.setTextColor(context.getResources().getColor(R.color.darker_gray));
            viewHolde.tishi.setVisibility(View.GONE);
            if (staBeen.get(position).getId() == selStationID) {
                adapter.setColorl(true);
                Drawable drawable = context.getResources().getDrawable(R.mipmap.station);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                viewHolde.tv.setCompoundDrawables(drawable, null, null, null);
                viewHolde.tv.setTextColor(context.getResources().getColor(R.color.black));
                viewHolde.tishi.setVisibility(View.VISIBLE);
                viewHolde.cardView.setCardElevation(context.getResources().getDimension(R.dimen.cardview));
                itmePosition = position;
            }
            if (position < itmePosition) {
                viewHolde.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.bg));
            }
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return staBeen.size();
    }


    public class ViewHolde extends ClickableViewHolder {

        private TextView tv, tishi;
        private RecyclerView rv;
        private CardView cardView;

        public ViewHolde(View itemView) {
            super(itemView);
            tv = $(R.id.tv_station);
            tishi = $(R.id.tv_daozhantishi);
            rv = $(R.id.rv_childlist);
            cardView = $(R.id.cardview);
        }
    }

    public interface OnItemAdapterListener {
        void setOnItemListener(StaBean staid, int position);
    }

    private OnItemAdapterListener onItemAdapterListener;

    public void setOnItemAdapterListener(OnItemAdapterListener onItemAdapterListener) {
        this.onItemAdapterListener = onItemAdapterListener;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
        notifyDataSetChanged();
    }
}
