package com.admin.shopkeeper.ui.activity.activityOfBoss.foodedit;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.admin.shopkeeper.App;
import com.admin.shopkeeper.Config;
import com.admin.shopkeeper.base.BasePresenter;
import com.admin.shopkeeper.db.AppDbHelper;
import com.admin.shopkeeper.db.DbHelper;
import com.admin.shopkeeper.entity.BussinessBean;
import com.admin.shopkeeper.entity.MenuTypeEntity;
import com.admin.shopkeeper.entity.OrderDetailFood;
import com.admin.shopkeeper.entity.PrintBean;
import com.admin.shopkeeper.helper.RetrofitHelper;
import com.admin.shopkeeper.model.StringModel;
import com.admin.shopkeeper.ui.activity.activityOfBoss.edit.IEditView;
import com.admin.shopkeeper.utils.DeviceUtils;
import com.admin.shopkeeper.utils.DialogUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/24.
 */

public class FoodEditPresenter extends BasePresenter<IFoodEditView> {

    public FoodEditPresenter(Context context, IFoodEditView iView) {
        super(context, iView);
    }

    public void submit(String type, String productId, String productName, String id,
                       String pinyin, String unit, String minunit, String productTypeId,
                       String productTypeName, double price, String productfile,String printId, int state,
                       String remark, String tasteID, int productCount, int warCount,
                       int chuCaiType, double memberPice, int salesType, int canDiscount, int protuctShuXing,int accordIng) {
        DialogUtils.showDialog(context, "数据提交中");
        RetrofitHelper.getInstance()
                .getApi()
                .addFood("2", type, productId, App.INSTANCE().getShopID(), productName,
                        id, pinyin, unit, minunit, productTypeId, productTypeName,
                        price, productfile,printId, state, remark, tasteID, productCount,
                        warCount, chuCaiType, memberPice, salesType, canDiscount,protuctShuXing ,accordIng)
                .compose(getActivityLifecycleProvider().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringModel -> {
                    DialogUtils.hintDialog();
                    if (stringModel.getCode().equals("1")) {
                        if (TextUtils.isEmpty(productId)) {
                            iView.success("添加成功");
                        } else {
                            iView.success("修改成功");
                        }
                    } else {
                        iView.error("提交失败");
                    }

                }, throwable -> {
                    DialogUtils.hintDialog();
                    iView.error("提交失败");
                });
    }

    public void editFood(String imagePath) {


        File file = new File(imagePath);
        if (!file.exists()) {
            iView.error("提交失败");
            return;
        }

        List<MultipartBody.Part> images = new ArrayList<>();
        HashMap<String, RequestBody> map = new HashMap<>();

        String path = DeviceUtils.getimage(imagePath, 500);
        File imageFile = new File(path);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("FileData", imageFile.getName(), requestFile);
        images.add(body);

        map.put("RESTAURANTID", getValue(App.INSTANCE().getShopID()));
        map.put("type", getValue("1"));

        RetrofitHelper.getInstance()
                .getApi()
                .uploadImage(map, images)
                .compose(getActivityLifecycleProvider().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringModel -> {
                    DialogUtils.hintDialog();
                    if (stringModel.getCode().equals("1")) {
                        iView.imagesuccess(imageFile.getName());
                    } else {
                        iView.error("提交失败");
                    }
                }, throwable -> {
                    DialogUtils.hintDialog();
                    iView.error("提交失败");
                });
    }

    private RequestBody getValue(String value) {
        return RequestBody.create(
                MediaType.parse("text/plain"), value);
    }

    public void getFoodType() {
        AppDbHelper.INSTANCE()
                .getMenuTypes(App.INSTANCE().getShopID())
                .compose(getFragmentLifecycleProvider().<List<MenuTypeEntity>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(menuTypeEntities -> {
                    if (menuTypeEntities.size() > 0) {
                        iView.getFoodSuccess(menuTypeEntities);
                    }
                }, throwable -> {

                });
    }

    public void getPrintData() {
        RetrofitHelper.getInstance()
                .getApi()
                .getPrint("1", App.INSTANCE().getShopID())
                .compose(getActivityLifecycleProvider().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringModel -> {
                    DialogUtils.hintDialog();
                    if (stringModel.getCode().equals("1")) {
                        if (stringModel.getResult().equals("0")) {
                        } else {
                            PrintBean[] printBeens = new Gson().fromJson(stringModel.getResult(), PrintBean[].class);
                            iView.getPrintSuccess(Arrays.asList(printBeens));
                        }
                    } else {
                        iView.error("获取打印机失败");
                    }
                }, throwable -> {
                    iView.error("获取打印机失败");
                });
    }
}
