package com.admin.shopkeeper.ui.activity.activityOfBoss.memberVolumeAnalysis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.admin.shopkeeper.App;
import com.admin.shopkeeper.R;
import com.admin.shopkeeper.adapter.MemberVolumeAnalysisAdapter;
import com.admin.shopkeeper.adapter.RechargeDetailAdapter;
import com.admin.shopkeeper.base.BaseActivity;
import com.admin.shopkeeper.dialog.CollectionSelectDialog;
import com.admin.shopkeeper.dialog.SingleSelectDialog;
import com.admin.shopkeeper.entity.ChainBean;
import com.admin.shopkeeper.entity.MemberVolumeAnalysisBean;
import com.admin.shopkeeper.entity.RechargeDetailTableBean;
import com.admin.shopkeeper.utils.Tools;
import com.admin.shopkeeper.utils.UIUtils;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.bean.DateType;
import com.gyf.barlibrary.ImmersionBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberVolumeAnalysisActivity extends BaseActivity<MemberVolumeAnalysisPresenter> implements IMemberVolumeAnalysisView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private PopupWindow laheiPop;
    List<MemberVolumeAnalysisBean> data;
    private PopupWindow popupWindow;
    private String titleStr;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_weixin)
    TextView tvWeixin;
    @BindView(R.id.tv_diannei)
    TextView tvDiannei;
    @BindView(R.id.tv_zengsong)
    TextView tvGive;
    @BindView(R.id.ll_total)
    LinearLayout totalLL;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    MemberVolumeAnalysisAdapter adapter;

    List<ChainBean> chainBeens = new ArrayList<>();
    String shopId;

    @Override
    protected void initPresenter() {
        presenter = new MemberVolumeAnalysisPresenter(this, this);
        presenter.init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_member_volume_analysis;
    }

    @Override
    public void initView() {

        ImmersionBar.with(this)
                .statusBarColor(R.color.bosscolorPrimaryDark, 0.4f)
                .titleBar(toolbar, true)
                .init();
        toolbar.setTitle("会员量分析");

        toolbar.setNavigationIcon(R.mipmap.navigation_icon_repeat);
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .marginResId(R.dimen._1sdp, R.dimen._1sdp)
                .color(getResources().getColor(R.color.item_line_color))
                .build());
        adapter = new MemberVolumeAnalysisAdapter(R.layout.item_member_volume_analysis);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {

        });
        String startDate = Tools.formatLastMonthDate("yyyy-MM");
        String endDate = Tools.formatNowDate("yyyy-MM");

        shopId = App.INSTANCE().getShopID();
        chainBeens = App.INSTANCE().getChainBeans();

        presenter.getData(startDate, endDate, shopId);

        tvDate.setText(startDate + "至" + endDate);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode == RESULT_OK) {
            presenter.getCouponInfo();
        }*/
        if (laheiPop != null && laheiPop.isShowing()) {
            laheiPop.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                showSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Date startDate;
    Date endDate;

    private void showSearch() {
        List<String> types = new ArrayList<>();
        types.add("营业时间");
        types.add("自定义时间");

        View laheiView = LayoutInflater.from(this).inflate(R.layout.pop_select_1, null);
        popupWindow = new PopupWindow(laheiView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        TextView etStartTime = (TextView) laheiView.findViewById(R.id.pop_starttime);
        TextView etEndTime = (TextView) laheiView.findViewById(R.id.pop_endtime);
        TextView tvTimeType = (TextView) laheiView.findViewById(R.id.pop_time_typw);
        TextView tvShop = (TextView) laheiView.findViewById(R.id.tv_shop);

        for (ChainBean chainBean : chainBeens) {
            if (shopId.toLowerCase().equals(chainBean.getMerchantId())) {
                tvShop.setText(chainBean.getNames());
            }
        }

        tvShop.setOnClickListener(v -> {
            if(chainBeens.size() == 0){
                showToast("获取门店失败");
                return;
            }

            String selectText = tvShop.getText().toString().trim();

            CollectionSelectDialog.Builder builder = new CollectionSelectDialog.Builder(this, R.style.OrderDialogStyle);
            builder.setTitle("选择门店");
            builder.setReasons(chainBeens);
            builder.setSelect(selectText);
            builder.setSingleSelect(true);
            builder.setButtonClick((text, value) -> {
                tvShop.setText(text);
                shopId = value;
            });
            builder.creater().show();
        });


        tvTimeType.setOnClickListener(v -> {
            SingleSelectDialog.Builder builder = new SingleSelectDialog.Builder(this, R.style.OrderDialogStyle);
            builder.setTitle("选择时间");
            builder.setReasons(types);
            builder.setButtonClick(new SingleSelectDialog.OnButtonClick() {

                @Override
                public void onOk(String text, int position) {
                    tvTimeType.setText(text);
                }

                @Override
                public void onCancel() {

                }
            });
            builder.creater().show();
        });

        etStartTime.setOnClickListener(v -> {
            String typestr = tvTimeType.getText().toString();

            DatePickDialog dialog = new DatePickDialog(this);
            dialog.setYearLimt(10);
            dialog.setTitle("选择时间");
            dialog.setType(typestr.equals("营业时间") ? DateType.TYPE_YMD : DateType.TYPE_ALL);
            dialog.setMessageFormat("yyyy-MM-dd HH:mm:ss");
            dialog.setOnChangeLisener(null);
            dialog.setOnSureLisener(date -> {
                startDate = date;
                etStartTime.setText(new SimpleDateFormat(typestr.equals("营业时间") ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss").format(date));
            });
            dialog.show();
        });

        etEndTime.setOnClickListener(v -> {
            String typestr = tvTimeType.getText().toString();
            DatePickDialog dialog = new DatePickDialog(this);
            dialog.setYearLimt(10);
            dialog.setTitle("选择时间");
            dialog.setType(typestr.equals("营业时间") ? DateType.TYPE_YMD : DateType.TYPE_ALL);
            dialog.setMessageFormat("yyyy-MM-dd HH:mm:ss");
            dialog.setOnChangeLisener(null);
            dialog.setOnSureLisener(date -> {
                endDate = date;
                etEndTime.setText(new SimpleDateFormat(typestr.equals("营业时间") ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss").format(date));
            });
            dialog.show();
        });

        laheiView.findViewById(R.id.pop_cancel).setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        laheiView.findViewById(R.id.pop_ok).setOnClickListener(v -> {

            String typestr = tvTimeType.getText().toString();

            if (startDate == null) {
                showToast("请选择开始时间");
                return;
            }
            if (endDate == null) {
                showToast("请选择结束时间");
                return;
            }

            if (Tools.comparaDate(startDate, endDate)) {
                showToast("筛选时间出错");
                return;
            }

            if (Tools.checkDate(startDate, endDate)) {
                showToast("筛选时间不能大于一个月");
                return;
            }

            /*presenter.getData(Tools.formatNowDate("yyyy-MM-dd", startDate),
                    Tools.formatNowDate("yyyy-MM-dd", endDate),
                    Tools.formatNowDate("HH:mm:ss", startDate),
                    Tools.formatNowDate("HH:mm:ss", endDate), typestr.equals("营业时间") ? 0 : 1);*/


            tvDate.setText(Tools.formatNowDate("yyyy-MM-dd", startDate) + "至" + Tools.formatNowDate("yyyy-MM-dd", endDate));
            popupWindow.dismiss();
        });

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));
        popupWindow.setOnDismissListener(() -> {
            backgroundAlpha(1f);
        });
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.5f);
    }

    @OnClick(R.id.tv_total)
    public void totalClick() {
        if (totalLL.getVisibility() != View.VISIBLE) {
            totalLL.setVisibility(View.VISIBLE);
            UIUtils.setDrawableRight(tvTotal, R.mipmap.list_arrow_up);
        } else {
            totalLL.setVisibility(View.GONE);
            UIUtils.setDrawableRight(tvTotal, R.mipmap.list_arrow_down);
        }
    }

    int defaultType = 0;

    /*@OnClick(R.id.coupon_manage_num)
    public void setDefaultClick() {
        defaultType++;

        if (data == null) {
            return;
        }
        if (defaultType % 3 == 1) {
            UIUtils.setDrawableRight(tvNum, R.mipmap.sort_a_z);
            List<CouponManageBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (Integer.parseInt(o1.getCounts()) > Integer.parseInt(o2.getCounts())) {
                    return 1;
                } else if (Integer.parseInt(o1.getCounts()) == Integer.parseInt(o2.getCounts())) {
                    return 0;
                } else {
                    return -1;
                }
            });
            adapter.setDatas(newData);
        } else if (defaultType % 3 == 2) {
            UIUtils.setDrawableRight(tvNum, R.mipmap.sort_z_a);
            List<CouponManageBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (Integer.parseInt(o1.getCounts()) > Integer.parseInt(o2.getCounts())) {
                    return -1;
                } else if (Integer.parseInt(o1.getCounts()) > Integer.parseInt(o2.getCounts())) {
                    return 0;
                } else {
                    return 1;
                }
            });
            adapter.setDatas(newData);
        } else {
            UIUtils.setDrawableRight(tvNum, R.mipmap.sort_default);
            adapter.setDatas(data);
        }
        UIUtils.setDrawableRight(tvCouponMoney, R.mipmap.sort_default);
        UIUtils.setDrawableRight(tvNeedMoney, R.mipmap.sort_default);
    }

    int nameType = 0;

    @OnClick(R.id.coupon_money)
    public void setNameClick() {
        nameType++;
        if (data == null) {
            return;
        }
        if (nameType % 3 == 1) {
            UIUtils.setDrawableRight(tvCouponMoney, R.mipmap.sort_a_z);
            List<CouponManageBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getPrice() > o2.getPrice()) {
                    return 1;
                } else if (o1.getPrice() == o2.getPrice()) {
                    return 0;
                } else {
                    return -1;
                }
            });
            adapter.setDatas(newData);
        } else if (nameType % 3 == 2) {
            UIUtils.setDrawableRight(tvCouponMoney, R.mipmap.sort_z_a);
            List<CouponManageBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getPrice() > o2.getPrice()) {
                    return -1;
                } else if (o1.getPrice() == o2.getPrice()) {
                    return 0;
                } else {
                    return 1;
                }
            });
            adapter.setDatas(newData);
        } else {
            UIUtils.setDrawableRight(tvCouponMoney, R.mipmap.sort_default);
        }
        UIUtils.setDrawableRight(tvNum, R.mipmap.sort_default);
        UIUtils.setDrawableRight(tvNeedMoney, R.mipmap.sort_default);
    }

    int stateType = 0;

    @OnClick(R.id.coupon_manage_need_money)
    public void setStateClick() {
        stateType++;
        if (data == null) {
            return;
        }
        if (stateType % 3 == 1) {
            UIUtils.setDrawableRight(tvNeedMoney, R.mipmap.sort_a_z);
            List<CouponManageBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getSumPrice() > o2.getSumPrice()) {
                    return 1;
                } else if (o1.getSumPrice() == o2.getSumPrice()) {
                    return 0;
                } else {
                    return -1;
                }
            });
            adapter.setDatas(newData);
        } else if (stateType % 3 == 2) {
            UIUtils.setDrawableRight(tvNeedMoney, R.mipmap.sort_z_a);
            List<CouponManageBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getSumPrice() > o2.getSumPrice()) {
                    return -1;
                } else if (o1.getSumPrice() == o2.getSumPrice()) {
                    return 0;
                } else {
                    return 1;
                }
            });
            adapter.setDatas(newData);
        } else {
            UIUtils.setDrawableRight(tvNeedMoney, R.mipmap.sort_default);
        }
        UIUtils.setDrawableRight(tvCouponMoney, R.mipmap.sort_default);
        UIUtils.setDrawableRight(tvNum, R.mipmap.sort_default);
    }*/

    @Override
    public void error(String msg) {
        showFailToast(msg);
    }

    @Override
    public void success(String msg) {
        showSuccessToast(msg);
    }

    @Override
    public void success(List<MemberVolumeAnalysisBean> memberVolumeAnalysisBeanList) {
        this.data = memberVolumeAnalysisBeanList;

        adapter.setNewData(memberVolumeAnalysisBeanList);
        int add = 0;
        int weChat = 0;
        int lineOfDown = 0;
        for (MemberVolumeAnalysisBean bean : memberVolumeAnalysisBeanList) {
            add += Integer.parseInt(bean.getTotal());
            weChat += Double.parseDouble(bean.getWeChat());
            lineOfDown += Double.parseDouble(bean.getLineOfDown());
        }

        tvWeixin.setText(String.valueOf(add));
        tvDiannei.setText(String.valueOf(weChat));
        tvGive.setText(String.valueOf(lineOfDown));
    }

}
