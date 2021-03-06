package com.admin.shopkeeper.adapter;

import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.admin.shopkeeper.R;
import com.admin.shopkeeper.entity.KouWei;
import com.admin.shopkeeper.entity.Season;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by admin on 2017/6/30.
 */

public class MaterialAdapter extends BaseMultiItemQuickAdapter<Season, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MaterialAdapter(List<Season> data) {
        super(data);
        addItemType(KouWei.NORMAL, R.layout.item_order_food_dialog);
        addItemType(KouWei.EDIT, R.layout.item_kouwei_edit);
    }

    @Override
    protected void convert(BaseViewHolder helper, Season item) {

        helper.setText(R.id.textView, item.getName() + " ￥" + item.getPrice());
        if (item.getItemType() == KouWei.EDIT) {
            AppCompatEditText editText = helper.getView(R.id.textView);
            helper.getView(R.id.textView).setSelected(item.isSelected());
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    item.setSelected(!TextUtils.isEmpty(s));
                    item.setName(TextUtils.isEmpty(s) ? "" : s.toString());
                    helper.getView(R.id.textView).setSelected(item.isSelected());
                }
            });
        } else if (item.getItemType() == KouWei.NORMAL) {
            helper.getView(R.id.textView).setSelected(item.isSelected());
        }
    }
}
