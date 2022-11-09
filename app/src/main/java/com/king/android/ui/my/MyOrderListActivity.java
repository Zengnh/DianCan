package com.king.android.ui.my;

import com.king.android.R;
import com.king.android.databinding.ActivityMyOrderListBinding;
import com.king.android.ui.order.OrderFragment;
import com.king.base.activity.BaseActivity;

/**
 * 我的订单
 */
public
class MyOrderListActivity extends BaseActivity<ActivityMyOrderListBinding> {
    @Override
    public void init() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout,
                        new OrderFragment().
                        setIntentData().
                        add("isActivity", "1").getFragment()
                ).commit();
    }
}
