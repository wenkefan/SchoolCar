package com.fwk.school4.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fwk.school4.R;
import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.ChildBean;
import com.fwk.school4.model.StationModeBean;
import com.fwk.school4.ui.adapter.BaseRecyclerAdapter;
import com.fwk.school4.ui.adapter.ShangCheRecyclerAdapter;
import com.fwk.school4.utils.SharedPreferencesUtils;
import com.fwk.school4.weight.CenterItemDialog;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fanwenke on 16/12/19.
 */

public class ShangcheActivity extends BaseActivity implements View.OnClickListener, CenterItemDialog.OnItemClickListener, BaseRecyclerAdapter.OnItemListener {
    @InjectView(R.id.title_tv)
    TextView title;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_ask_for_leave_status)
    TextView tvAskForLeaveStatus;
    @InjectView(R.id.tv_father_name)
    TextView tvFatherName;
    @InjectView(R.id.tv_father_phone)
    TextView tvFatherPhone;
    @InjectView(R.id.tv_mother_name)
    TextView tvMotherName;
    @InjectView(R.id.tv_mother_phone)
    TextView tvMotherPhone;
    @InjectView(R.id.tv_select_station)
    TextView tvSelectStation;
    @InjectView(R.id.rv_select_type)
    RecyclerView rv;
    @InjectView(R.id.ll_zhuangtai)
    LinearLayout zhuangtai;
    @InjectView(R.id.btn_confirm)
    Button quding;
    @InjectView(R.id.tv_mother_phone)
    TextView mother_phone;
    @InjectView(R.id.tv_father_phone)
    TextView father_phone;
    private ChildBean.RerurnValueBean bean;
    private SharedPreferencesUtils sp = new SharedPreferencesUtils();
//    private SharedPreferencesUtils2 spData = new SharedPreferencesUtils2();
//    private boolean isZhuangtai = false;
    private CenterItemDialog dialog = null;
    private List<StationModeBean> stationModeBeen;
    private LinearLayoutManager manager;
    private ShangCheRecyclerAdapter adapter;
    private boolean jumpPosition;

    public ShangcheActivity() {

        stationModeBeen = (List<StationModeBean>) sp.queryForSharedToObject(Keyword.STATIONIDLIST);
    }

    @Override
    public int getLayoutId() {
        return R.layout.schoolcar_dialog;
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        jumpPosition = intent.getBooleanExtra(Keyword.JUMPPOSITION,false );
        bean = (ChildBean.RerurnValueBean) intent.getSerializableExtra("bean");
        manager = new LinearLayoutManager(this);
        initView();
        setGetOnBusData();
    }

    private void initView() {
        title.setText(getResources().getString(R.string.select));
        tvName.setText(bean.getChildName());
        tvFatherName.setText(bean.getFatherName());
        tvFatherPhone.setText(bean.getFatherPhone());
        tvMotherName.setText(bean.getMotherName());
        tvMotherPhone.setText(bean.getMotherPhone());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(manager);
        adapter = new ShangCheRecyclerAdapter(bean.getSelectid()-1);
        rv.setAdapter(adapter);
        adapter.setOnItemListener(this);
        if (!jumpPosition){
            zhuangtai.setVisibility(View.GONE);
            quding.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btn_confirm, R.id.tv_ask_for_leave_status, R.id.tv_select_station, R.id.btn_fanhui, R.id.tv_father_phone, R.id.tv_mother_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                    Intent intent = new Intent();
                    intent.putExtra(Keyword.SP_SELECT_ID, SelectState);
                    setResult(RESULT_OK, intent);
                    finish();
                break;
            case R.id.btn_fanhui:
                finish();
                break;
            case R.id.tv_ask_for_leave_status:
                dialog.show();
                break;
            case R.id.tv_select_station:
                break;
            case R.id.tv_father_phone:
                //"android.intent.action.CALL"为隐式Intent跳转到拨打电话的activity
                String num1 = (String) father_phone.getText();
                Intent photoIntent1 =new Intent("android.intent.action.CALL", Uri.parse("tel:"+num1));
                startActivity(photoIntent1);
                break;
            case R.id.tv_mother_phone:
                String num2 = (String) mother_phone.getText();
                Intent photoIntent2 =new Intent("android.intent.action.CALL", Uri.parse("tel:"+num2));
                startActivity(photoIntent2);
                break;
        }
    }

    /**
     * 请假状态
     */
    private String[] askForLeaveStatus = null;

    private void setGetOnBusData() {
        if (true) {

            askForLeaveStatus = new String[]{"已上车（已刷卡、未带卡）", "病假", "事假", "家长接送"};

        } else if (false) {

            askForLeaveStatus = new String[]{"已下车（已刷卡、未带卡）"};

        }

        tvAskForLeaveStatus.setOnClickListener(this);
        dialog = new CenterItemDialog(this);
        dialog.setTitle(R.string.manual_operation_dialog_title_choise_children_status);
        dialog.setOnItemClickListener(this);
        dialog.setItems(askForLeaveStatus);

    }

    @Override
    public void onItemClick(int requestCode, int position) {
        if (requestCode == 1) {


        } else {
            tvAskForLeaveStatus.setText(askForLeaveStatus[position]);
            switch (position) {
                case 0:
                    //手动上下车
                    SelectState = ChildState1;
                    break;
                case 1:
                    //病假
                    SelectState = ChildState2;
                    break;
                case 2:
                    //事假
                    SelectState = ChildState3;
                    break;
                case 3:
                    //家长接送
                    SelectState = ChildState4;
                    break;
                default:
                    break;
            }
        }
    }

    private static final int ChildState1 = 1;
    private static final int ChildState2 = 2;
    private static final int ChildState3 = 3;
    private static final int ChildState4 = 4;
    private int SelectState = 1;


    @Override
    public void setOnItemListener(int position, BaseRecyclerAdapter.ClickableViewHolder holder) {
        SelectState = (position + 1);
        adapter.setItme(position);
        adapter.notifyDataSetChanged();
    }
}
