package com.admin.shopkeeper.ui.activity.activityOfBoss.basicSets;

import com.admin.shopkeeper.base.IBaseView;
import com.admin.shopkeeper.entity.BasicSetBean;

/**
 * Created by Administrator on 2017/7/9 0009.
 */

public interface IBasicSetsView extends IBaseView {

    void error(String failed);

    void success(BasicSetBean bean);

    void success(String msg);

    void imagesuccess(String name);
}
