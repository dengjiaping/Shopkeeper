package com.admin.shopkeeper.ui.activity.activityOfBoss.free;

import android.content.Context;

import com.admin.shopkeeper.App;
import com.admin.shopkeeper.base.BasePresenter;
import com.admin.shopkeeper.entity.ChainBean;
import com.admin.shopkeeper.entity.FreeBean;
import com.admin.shopkeeper.entity.HandoverBean;
import com.admin.shopkeeper.helper.RetrofitHelper;
import com.admin.shopkeeper.ui.activity.activityOfBoss.jion.IJionView;
import com.admin.shopkeeper.utils.DialogUtils;
import com.google.gson.Gson;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/24.
 */

public class FreePresenter extends BasePresenter<IFreeView> {

    public FreePresenter(Context context, IFreeView iView) {
        super(context, iView);
    }

    public void getData(int page, String startDate, String endDate, String startTime, String endTime, int selectType, String shopId) {
        DialogUtils.showDialog(context, "数据加载中");
        RetrofitHelper.getInstance()
                .getApi()
                .getJion("3", 20, page, "ASC", startDate, endDate, startTime, endTime, shopId, selectType)
                .compose(getActivityLifecycleProvider().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringModel -> {
                    DialogUtils.hintDialog();
                    if (stringModel.getCode().equals("1")) {
                        FreeBean[] beens = new Gson().fromJson(stringModel.getResult(), FreeBean[].class);
                        iView.success(Arrays.asList(beens));
                    } else {
                        iView.error("加载失败");
                    }
                }, throwable -> {
                    DialogUtils.hintDialog();
                    iView.error("加载失败");
                });
    }
}
