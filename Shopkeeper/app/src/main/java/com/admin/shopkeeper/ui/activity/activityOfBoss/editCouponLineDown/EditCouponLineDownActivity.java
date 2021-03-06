package com.admin.shopkeeper.ui.activity.activityOfBoss.editCouponLineDown;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.admin.shopkeeper.App;
import com.admin.shopkeeper.R;
import com.admin.shopkeeper.base.BaseActivity;
import com.admin.shopkeeper.dialog.CollectionSelectDialog;
import com.admin.shopkeeper.dialog.SingleSelectDialog;
import com.admin.shopkeeper.entity.ChainBean;
import com.admin.shopkeeper.entity.CouponLineDownBean;
import com.admin.shopkeeper.entity.FoodBean;
import com.admin.shopkeeper.entity.MansongBean;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.bean.DateType;
import com.gyf.barlibrary.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EditCouponLineDownActivity extends BaseActivity<EditCouponLineDownPresenter> implements IEditCouponLineDownView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.item_name_coupon_line)
    EditText etName;
    @BindView(R.id.item_nums_coupon_line)
    EditText etNums;
    @BindView(R.id.item_max_use_coupon_line)
    EditText etMaxUse;
    @BindView(R.id.item_money_free_coupon_line)
    TextView etMoney;
    @BindView(R.id.item_shop_coupon_line)
    TextView tvShop;
    @BindView(R.id.item_enable_coupon_line)
    RadioGroup rgEnable;
    private FoodBean foodBean;
    private CouponLineDownBean bean;
    List<ChainBean> chainBeens = new ArrayList<>();
    private String shopId;

    @Override
    protected void initPresenter() {
        presenter = new EditCouponLineDownPresenter(this, this);
        presenter.init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_coupon_line_down;
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.bosscolorPrimaryDark, 0.4f)
                .titleBar(toolbar, true)
                .init();
        toolbar.setNavigationIcon(R.mipmap.navigation_icon_repeat);
        toolbar.setTitle("添加线下券");
        setSupportActionBar(toolbar);

        chainBeens = App.INSTANCE().getChainBeans();
        shopId = App.INSTANCE().getShopID();
        bean = (CouponLineDownBean) getIntent().getSerializableExtra("bean");
        if (bean == null) {
            toolbar.setTitle("添加线下券");
            ((RadioButton) rgEnable.getChildAt(0)).setChecked(true);
        } else {
            toolbar.setTitle("修改线下券");

            etName.setText(bean.getName());
            etNums.setText(String.valueOf(bean.getCounts()));
            etMoney.setText(String.valueOf(bean.getPice()));
            etMaxUse.setText(String.valueOf(bean.getMaxUseCount()));
            tvShop.setText(bean.getShopId());
            /*if (TextUtils.isEmpty(bean.getApply())) {
                tvShop.setText("预定");
            } else if (bean.getApply().equals("1")) {
                tvShop.setText("预定");
            } else if (bean.getApply().equals("3")) {
                tvShop.setText("外卖");
            } else if (bean.getApply().equals("4")) {
                tvShop.setText("快餐");
            } else if (bean.getApply().equals("5")) {
                tvShop.setText("扫码点餐");
            }*/

            if (bean.getState().equals("1")) {
                ((RadioButton) rgEnable.getChildAt(0)).setChecked(true);
            } else {
                ((RadioButton) rgEnable.getChildAt(1)).setChecked(true);
            }
        }
    }
    @OnClick(R.id.item_shop_coupon_line)
    public void shopClick(){
        if (chainBeens.size() == 0) {
            showToast("获取门店失败");
            return;
        }

        String selectText = tvShop.getText().toString().trim();

        CollectionSelectDialog.Builder builder = new CollectionSelectDialog.Builder(this, R.style.OrderDialogStyle);
        builder.setTitle("选择门店");
        builder.setReasons(chainBeens);
        builder.setSelect(selectText);
        builder.setButtonClick((text, value) -> {
            tvShop.setText(text);
            shopId = value;
        });
        builder.creater().show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_submit:
                submit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        String nameStr = etName.getText().toString();
        String numStr = etNums.getText().toString();
        String moneyStr = etMoney.getText().toString();
        String maxUse = etMaxUse.getText().toString();

        if (TextUtils.isEmpty(nameStr)) {
            showToast("请输入线下券名称");
            return;
        }

        if (TextUtils.isEmpty(numStr)) {
            showToast("请输入线下券数量");
            return;
        }
        if (TextUtils.isEmpty(moneyStr)) {
            showToast("请输入赠送金额");
            return;
        }

        if (TextUtils.isEmpty(maxUse)) {
            showToast("请输入最大使用量");
            return;
        }


        int enable = ((RadioButton) rgEnable.getChildAt(0)).isChecked() ? 1 : 0;


        for (ChainBean chainBean : chainBeens) {
            if (shopId.toLowerCase().equals(chainBean.getMerchantId())) {
                tvShop.setText(chainBean.getNames());
            }
        }

        String typeStr = tvShop.getText().toString();
        if (TextUtils.isEmpty(typeStr)) {
            showToast("请选择适用商家");
            return;
        }
//        presenter.commit(bean == null ? "" : bean.getGuid(), nameStr, tiaojianStr, moneyStr, startStr, entStr, type, enable);
        presenter.commit(nameStr, Integer.parseInt(numStr), Integer.parseInt(maxUse), moneyStr, typeStr, enable);
    }


    @Override
    public void error(String msg) {
        showFailToast(msg);
    }

    @Override
    public void success(String msg) {
        showSuccessToast(msg);
        setResult(RESULT_OK);
        finish();
    }
}
