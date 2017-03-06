package com.fwk.school4.ui;


import com.fwk.school4.R;
import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.BanciBean;
import com.fwk.school4.utils.SharedPreferencesUtils;


/**
 * Created by fanwenke on 2017/3/2.
 */

public class FinishActivity extends BaseActivity {


    private SharedPreferencesUtils sp;
    private BanciBean.RerurnValueBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.finishactivity;
    }

    @Override
    public void init() {
        sp = new SharedPreferencesUtils();
        bean = (BanciBean.RerurnValueBean) sp.queryForSharedToObject(Keyword.SELECTBANCI);
//        title.setText(bean.getBusScheduleName() + "已结束");
        initData();
//        initRecycler();
    }

    private void initData() {





//        map = (Map<String, List<ChildBean.RerurnValueBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
//        staBeen = (List<StaBean>) sp.queryForSharedToObject(Keyword.SELECTSTA);
//        childList = new ArrayList<>();
//        for (StaBean bean : staBeen){
//            if (bean.getType() == 1){
//                for (ChildBean.RerurnValueBean valueBean : map.get(bean.getStrid())){
//                    childList.add(valueBean);
//                }
//            }
//        }
    }

//    private void initRecycler(){
//        manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        adapter = new FinishAdapter(childList);
//        recyclerView.setAdapter(adapter);
//    }
    @Override
    protected void onDestroy() {
        sp.removData();
        super.onDestroy();
    }
}
