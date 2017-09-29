package com.admin.shopkeeper.ui.activity.activityOfBoss.memberManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.admin.shopkeeper.Config;
import com.admin.shopkeeper.R;
import com.admin.shopkeeper.adapter.MemberManagerAdapter;
import com.admin.shopkeeper.adapter.ShopPermissionManageAdapter;
import com.admin.shopkeeper.base.BaseActivity;
import com.admin.shopkeeper.entity.DeskBussinessBean;
import com.admin.shopkeeper.entity.MemberInfoBean;
import com.admin.shopkeeper.entity.RetCauseBean;
import com.admin.shopkeeper.entity.ShopPermissionManageBean;
import com.admin.shopkeeper.ui.activity.activityOfBoss.MemberInfo.MemberInfoActivity;
import com.admin.shopkeeper.ui.activity.activityOfBoss.edit.EditActivity;
import com.admin.shopkeeper.ui.activity.activityOfBoss.returncause.ReturnCauseActivity;
import com.admin.shopkeeper.utils.UIUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberManageActivity extends BaseActivity<MemberManagePresenter> implements IMemberManageView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.staff_manage_default)
    TextView tvDefault;
    @BindView(R.id.staff_manage_name)
    TextView tvName;
    @BindView(R.id.staff_manage_state)
    TextView tvState;
    @BindView(R.id.staff_manage_select)
    TextView tvSelect;
    private MemberManagerAdapter adapter;
    private PopupWindow laheiPop;
    List<MemberInfoBean> data;

    @Override
    protected void initPresenter() {

        presenter = new MemberManagePresenter(this, this);
        presenter.init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_member_manage;
    }

    @Override
    public void initView() {

        ImmersionBar.with(this)
                .statusBarColor(R.color.bosscolorPrimaryDark, 0.4f)
                .titleBar(toolbar, true)
                .init();

        toolbar.setTitle("会员管理");
        toolbar.setNavigationIcon(R.mipmap.navigation_icon_repeat);
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .marginResId(R.dimen._1sdp, R.dimen._1sdp)
                .color(getResources().getColor(R.color.item_line_color))
                .build());
        adapter = new MemberManagerAdapter(this);
        recyclerView.setAdapter(adapter);
        presenter.getMemberInfo();
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    int defaultType = 0;

    @OnClick(R.id.staff_manage_default)
    public void setDefaultClick() {
        defaultType++;

        if (data == null) {
            return;
        }
        if (defaultType % 3 == 1) {
            UIUtils.setDrawableRight(tvDefault, R.mipmap.sort_a_z);
            List<MemberInfoBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getPosition() > o2.getPosition()) {
                    return 1;
                } else if (o1.getPosition() == o2.getPosition()) {
                    return 0;
                } else {
                    return -1;
                }
            });
            adapter.setDatas(newData);
        } else if (defaultType % 3 == 2) {
            UIUtils.setDrawableRight(tvDefault, R.mipmap.sort_z_a);
            List<MemberInfoBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getPosition() > o2.getPosition()) {
                    return -1;
                } else if (o1.getPosition() == o2.getPosition()) {
                    return 0;
                } else {
                    return 1;
                }
            });
            adapter.setDatas(newData);
        } else {
            UIUtils.setDrawableRight(tvDefault, R.mipmap.sort_default);
            adapter.setDatas(data);
        }
        UIUtils.setDrawableRight(tvName, R.mipmap.sort_default);
        UIUtils.setDrawableRight(tvState, R.mipmap.sort_default);
    }

    int nameType = 0;

    @OnClick(R.id.staff_manage_name)
    public void setNameClick() {
        nameType++;
        if (nameType % 3 == 1) {
            UIUtils.setDrawableRight(tvName, R.mipmap.sort_a_z);
            List<MemberInfoBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getPosition() > o2.getPosition()) {
                    return 1;
                } else if (o1.getPosition() == o2.getPosition()) {
                    return 0;
                } else {
                    return -1;
                }
            });
            adapter.setDatas(newData);
        } else if (nameType % 3 == 2) {
            UIUtils.setDrawableRight(tvName, R.mipmap.sort_z_a);
            List<MemberInfoBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getPosition() > o2.getPosition()) {
                    return -1;
                } else if (o1.getPosition() == o2.getPosition()) {
                    return 0;
                } else {
                    return 1;
                }
            });
            adapter.setDatas(newData);
        } else {
            UIUtils.setDrawableRight(tvName, R.mipmap.sort_default);
        }
        UIUtils.setDrawableRight(tvDefault, R.mipmap.sort_default);
        UIUtils.setDrawableRight(tvState, R.mipmap.sort_default);
    }

    int stateType = 0;

    @OnClick(R.id.staff_manage_state)
    public void setStateClick() {
        stateType++;
        if (stateType % 3 == 1) {
            UIUtils.setDrawableRight(tvState, R.mipmap.sort_a_z);
            List<MemberInfoBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (TextUtils.isEmpty(o1.getMemberCard()) && TextUtils.isEmpty(o2.getMemberCard())) {
                    return 0;
                } else if (TextUtils.isEmpty(o1.getMemberCard())) {
                    return 1;
                } else {
                    return -1;
                }
            });
            adapter.setDatas(newData);
        } else if (stateType % 3 == 2) {
            UIUtils.setDrawableRight(tvState, R.mipmap.sort_z_a);
            List<MemberInfoBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (TextUtils.isEmpty(o1.getMemberCard()) && TextUtils.isEmpty(o2.getMemberCard())) {
                    return 0;
                } else if (TextUtils.isEmpty(o1.getMemberCard())) {
                    return -1;
                } else {
                    return 1;
                }
            });
            adapter.setDatas(newData);
        } else {
            UIUtils.setDrawableRight(tvState, R.mipmap.sort_default);
        }
        UIUtils.setDrawableRight(tvName, R.mipmap.sort_default);
        UIUtils.setDrawableRight(tvDefault, R.mipmap.sort_default);
    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void success(String msg) {

    }

    @Override
    public void success(List<MemberInfoBean> memberInfoBeanList) {
        this.data = memberInfoBeanList;
        adapter.setDatas(memberInfoBeanList);

    }

    public void showDeletePop(MemberInfoBean bean) {
        View laheiView = LayoutInflater.from(this).inflate(R.layout.pop_four_item, null);
        laheiPop = new PopupWindow(laheiView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        laheiView.findViewById(R.id.pop_cancel).setOnClickListener(v -> {
            laheiPop.dismiss();
        });
        //会员消费记录
        laheiView.findViewById(R.id.pop_item1).setOnClickListener(v -> {
            gotoMemberInfoActivity(2, bean);
        });
        //会员充值记录
        laheiView.findViewById(R.id.pop_item2).setOnClickListener(v -> {
            gotoMemberInfoActivity(3, bean);
        });
        //会员积分记录
        laheiView.findViewById(R.id.pop_item3).setOnClickListener(v -> {
            gotoMemberInfoActivity(4, bean);
        });
        //会员卡券记录
        laheiView.findViewById(R.id.pop_item4).setOnClickListener(v -> {
            gotoMemberInfoActivity(5, bean);
        });
        laheiPop.setOutsideTouchable(true);
        laheiPop.setFocusable(true);
        laheiPop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));
        laheiPop.setOnDismissListener(() -> {
            backgroundAlpha(1f);
        });
        laheiPop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.5f);
    }

    private void gotoMemberInfoActivity(int type, MemberInfoBean bean) {
        Intent intent = new Intent(this, MemberInfoActivity.class);
        intent.putExtra(Config.PARAM1, type);
        intent.putExtra(Config.PARAM2, bean);
        startActivity(intent);
    }


}
