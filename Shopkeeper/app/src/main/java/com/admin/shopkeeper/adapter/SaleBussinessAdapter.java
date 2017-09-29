package com.admin.shopkeeper.adapter;

import android.support.annotation.LayoutRes;

import com.admin.shopkeeper.R;
import com.admin.shopkeeper.entity.FoodBussinessBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/8/29.
 */

public class SaleBussinessAdapter extends BaseQuickAdapter<FoodBussinessBean, BaseViewHolder> {


    public SaleBussinessAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FoodBussinessBean item) {
        helper.setText(R.id.item_name, item.getProductName() + "");
        helper.setText(R.id.item_count, item.getCounts() + "");
        helper.setVisible(R.id.item_price, false);
    }

}
