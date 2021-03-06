package com.admin.shopkeeper.ui.activity.activityOfBoss.foodbussiness;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.admin.shopkeeper.R;
import com.admin.shopkeeper.adapter.FoodBussinessAdapter;
import com.admin.shopkeeper.base.BaseActivity;
import com.admin.shopkeeper.entity.DeskBussinessBean;
import com.admin.shopkeeper.entity.FoodBussinessBean;
import com.admin.shopkeeper.utils.DeviceUtils;
import com.admin.shopkeeper.utils.Tools;
import com.admin.shopkeeper.utils.UIUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FoodBussinessActivity extends BaseActivity<FoodBussinessPresenter> implements IFoodBussinessView ,
        DatePicker.OnDateChangedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.desk_count)
    TextView tvCount;
    @BindView(R.id.desk_money)
    TextView tvMoney;

    private FoodBussinessAdapter adapter;
    private PopupWindow popupWindow;

    @Override
    protected void initPresenter() {
        presenter = new FoodBussinessPresenter(this, this);
        presenter.init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_food_bussiness;
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.bosscolorPrimaryDark, 0.4f)
                .titleBar(toolbar, true)
                .init();
        date = new StringBuffer();

        toolbar.setTitle("菜品销售情况");
        toolbar.setNavigationIcon(R.mipmap.navigation_icon_repeat);
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .marginResId(R.dimen._30sdp, R.dimen._1sdp)
                .color(getResources().getColor(R.color.item_line_color))
                .build());
        adapter = new FoodBussinessAdapter(R.layout.item_foodbussiness);
        recyclerView.setAdapter(adapter);

        presenter.getData(Tools.formatNowDate("yyyy-MM-dd"), Tools.formatNowDate("yyyy-MM-dd"));
    }

    @OnClick(R.id.desk_select)
    public void selectClick() {
        View laheiView = LayoutInflater.from(this).inflate(R.layout.pop_select, null);
        popupWindow = new PopupWindow(laheiView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        TextView etStartTime = (TextView) laheiView.findViewById(R.id.pop_starttime);
        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etStartTime);
            }
        });
        TextView etEndTime = (TextView) laheiView.findViewById(R.id.pop_endtime);
        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etEndTime);
            }
        });
        laheiView.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData("1999-01-01", Tools.formatNowDate("yyyy-MM-dd"));
                popupWindow.dismiss();
            }
        });

        laheiView.findViewById(R.id.pop_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTime = etStartTime.getText().toString();
                String entTime = etEndTime.getText().toString();
                presenter.getData(startTime, entTime);
            }
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

    int countType = 0;

    @OnClick(R.id.desk_count)
    public void countClick() {
        if (data == null || data.size() == 0) {
            return;
        }
        countType++;
        if (countType % 3 == 1) {
            UIUtils.setDrawableRight(tvCount, R.mipmap.sort_a_z);

            List<FoodBussinessBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getCounts() > o2.getCounts()) {
                    return 1;
                } else if (o1.getCounts() == o2.getCounts()) {
                    return 0;
                } else {
                    return -1;
                }
            });
            adapter.setNewData(newData);
        } else if (countType % 3 == 2) {
            UIUtils.setDrawableRight(tvCount, R.mipmap.sort_z_a);

            List<FoodBussinessBean> newData = new ArrayList<>();
            newData.addAll(data);
            Collections.sort(newData, (o1, o2) -> {
                if (o1.getCounts() > o2.getCounts()) {
                    return -1;
                } else if (o1.getCounts() == o2.getCounts()) {
                    return 0;
                } else {
                    return 1;
                }
            });
            adapter.setNewData(newData);
        } else {
            UIUtils.setDrawableRight(tvCount, R.mipmap.sort_default);
            adapter.setNewData(data);
        }
        UIUtils.setDrawableRight(tvMoney, R.mipmap.sort_default);
    }

    int moneyType = 0;

    @OnClick(R.id.desk_money)
    public void moneyClick() {
        if (data == null || data.size() == 0) {
            return;
        }
        moneyType++;
        if (moneyType % 3 == 1) {
            UIUtils.setDrawableRight(tvMoney, R.mipmap.sort_a_z);

            List<FoodBussinessBean> newData = new ArrayList<>();
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
            adapter.setNewData(newData);
        } else if (moneyType % 3 == 2) {
            UIUtils.setDrawableRight(tvMoney, R.mipmap.sort_z_a);

            List<FoodBussinessBean> newData = new ArrayList<>();
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
            adapter.setNewData(newData);
        } else {
            UIUtils.setDrawableRight(tvMoney, R.mipmap.sort_default);
            adapter.setNewData(data);
        }
        UIUtils.setDrawableRight(tvCount, R.mipmap.sort_default);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void error(String msg) {
        showFailToast(msg);
    }

    List<FoodBussinessBean> data;

    @Override
    public void success(List<FoodBussinessBean> data) {
        this.data = data;
        adapter.setNewData(data);
        if (popupWindow != null) {
            popupWindow.dismiss();
        }

        UIUtils.setDrawableRight(tvCount, R.mipmap.sort_default);
        UIUtils.setDrawableRight(tvMoney, R.mipmap.sort_default);
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }
    private StringBuffer date;
    private int year, month, day;
    public void selectDate(TextView tv) {
        initDateTime();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date.length() > 0) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                tv.setText(date.append(String.valueOf(year)).append("-").append(String.valueOf(month)).append("-").append(day));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month - 1, day, this);
    }

    /**
     * 日期改变的监听事件
     *
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear + 1;
        this.day = dayOfMonth;
    }
}
