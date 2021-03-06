package com.base.application.baseapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.base.application.baseapplication.jncax.animation.Animation3DActivity;
import com.base.application.baseapplication.jncax.animation.Animation3DActivity2;
import com.base.application.baseapplication.jncax.animation.AnimationActivity;
import com.base.application.baseapplication.jncax.animation.LayoutAnimationsActivity;
import com.base.application.baseapplication.jncax.blockview.BlockViewActivity;
import com.base.application.baseapplication.jncax.coordinatorlayout.CoordinatorLayoutActivity;
import com.base.application.baseapplication.jncax.coordinatorlayout.DrawerLayoutWithNestedSrcollViewActivity;
import com.base.application.baseapplication.jncax.coordinatorlayout.ScrollViewPagerActivity;
import com.base.application.baseapplication.jncax.draw.DrawActivity;
import com.base.application.baseapplication.jncax.expandtable.ExpandTableActivity;
import com.base.application.baseapplication.jncax.foldingmenu.FoldingActivity;
import com.base.application.baseapplication.jncax.pullscroll.PullSrcollActivity;
import com.base.application.baseapplication.jncax.qrcode.QRCodeActivity;
import com.base.application.baseapplication.jncax.smarttab.FiveActivity;
import com.base.application.baseapplication.net.message.NameValuePair;
import com.base.application.baseapplication.net.model.BasicNetError;
import com.base.application.baseapplication.net.model.BasicNetResult;
import com.base.application.baseapplication.net.model.NetResult;
import com.base.application.baseapplication.net.task.BasicJsonTask;
import com.base.application.baseapplication.net.task.BasicNetTask;
import com.base.application.baseapplication.utils.LogUtils;
import com.base.application.baseapplication.utils.ToastUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class MainActivity extends BasicActivity implements View.OnClickListener
{

    private TextView mBtnBlockView;

    private TextView mBtnFoldingMenu;

    private TextView mBtnExpandTable;

    private TextView mBtnQRCode;

    private TextView mBtnDraw;

    private TextView mBtnAnimation;

    private TextView mBntLayoutAnimation;

    private TextView mBtn3DAnimation;

    private TextView mBtn3DAnimation2;

    private TextView mBtnCoordinatorLayout;

    private TextView mBtnSmartTabLayout;

    private TextView mBtnTablayoutViewpager;

    private TextView mBtnNestedScrollView;

    private TextView mBtnPullScroll;

    // Used to load the 'native-lib' library on application startup.
    static
    {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native int maxFromJNI(int a,int b);

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acjn_activity_main);
        initView();
        setOnclickListener();
        ToastUtils.showMessage(maxFromJNI(10,34)+"");

    }

    private void initView()
    {
        mBtnBlockView = (TextView) findViewById(R.id.btn_block_view);
        mBtnFoldingMenu = (TextView) findViewById(R.id.btn_folding_menu);
        mBtnExpandTable = (TextView) findViewById(R.id.btn_expandtable);
        mBtnQRCode = (TextView) findViewById(R.id.btn_qr_code);
        mBtnDraw = (TextView) findViewById(R.id.btn_draw_paint);
        mBtnAnimation = (TextView) findViewById(R.id.btn_animation);
        mBntLayoutAnimation = (TextView) findViewById(R.id.btn_animation_layout);
        mBtn3DAnimation = (TextView) findViewById(R.id.btn_animation_3d_layout);
        mBtn3DAnimation2 = (TextView) findViewById(R.id.btn_animation_3d_layout2);
        mBtnCoordinatorLayout = (TextView) findViewById(R.id.btn_coordinatorlayout);
        mBtnSmartTabLayout = (TextView) findViewById(R.id.btn_smarttablayout);
        mBtnTablayoutViewpager = (TextView) findViewById(R.id.btn_tablayout_with_viewpager);
        mBtnNestedScrollView = (TextView) findViewById(R.id.btn_tablayout_with_nestedscrollview);
        mBtnPullScroll = (TextView) findViewById(R.id.btn_pull_scroll);
    }

    private void setOnclickListener()
    {
        mBtnBlockView.setOnClickListener(this);
        mBtnFoldingMenu.setOnClickListener(this);
        mBtnExpandTable.setOnClickListener(this);
        mBtnQRCode.setOnClickListener(this);
        mBtnDraw.setOnClickListener(this);
        mBtnAnimation.setOnClickListener(this);
        mBntLayoutAnimation.setOnClickListener(this);
        mBtn3DAnimation.setOnClickListener(this);
        mBtn3DAnimation2.setOnClickListener(this);
        mBtnCoordinatorLayout.setOnClickListener(this);
        mBtnSmartTabLayout.setOnClickListener(this);
        mBtnTablayoutViewpager.setOnClickListener(this);
        mBtnNestedScrollView.setOnClickListener(this);
        mBtnPullScroll.setOnClickListener(this);
    }

    private void testNetWork()
    {
        showLoadingView(true);
        TestTask testTask = new TestTask();
        testTask.setId(1000);
        testTask.setOnResultListener(listener);
        testTask.execute();
    }

    BasicNetTask.OnResultListener listener = new BasicNetTask.OnResultListener()
    {
        @Override
        public <T> void onResult(BasicNetTask<T> task, NetResult result)
        {
            if(task.getId() == 1000)
            {
                hideLoadingView();
                String jsonObject = (String) result.getData();
                ToastUtils.showMessage(jsonObject);
            }
        }
    };

    class TestTask extends BasicJsonTask
    {

        @Override
        public int getMethod()
        {
            return METHOD_GET;
        }

        @Override
        public String getUrl()
        {
            return "http://api.m.jd.com/client.action?functionId=skuDyInfo&clientVersion=5.6.0&build=40550&client=android&d_brand=Xiaomi&d_model=MI3W&osVersion=6.0.1&screen=1920*1080&partner=test&uuid=864690024391602-d4970b68063d&area=12_904_905_52652&networkType=wifi&st=1482406393812&sign=d8ad8f1f7faf5cdf17d63211734b43ed&sv=101";
        }

        @Override
        public List<NameValuePair> getRequestBody()
        {
            return null;
        }

        @Override
        public NetResult onNetResponse(JSONObject response)
        {
            return new BasicNetResult(true, response.optString("echo"));
        }

        @Override
        public NetResult onNetErrorResponse(BasicNetError error)
        {
            return new BasicNetResult(false);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btn_block_view:
                disposeBlockViewClickListener();
                break;
            case R.id.btn_folding_menu:
                disposeFoldingMenuClickListener();
                break;
            case R.id.btn_expandtable:
                disposeExpandTableClickListener();
                break;
            case R.id.btn_qr_code:
                disposeQRCodeClickListener();
                break;
            case R.id.btn_draw_paint:
                disposeDrawClickListener();
                break;
            case R.id.btn_animation:
                disposeAnimationClickListener();
                break;
            case R.id.btn_animation_layout:
                disposeLayoutAnimationClickListener();
                break;
            case R.id.btn_animation_3d_layout:
                dispose3DAnimationClickListener();
                break;
            case R.id.btn_animation_3d_layout2:
                dispose3D2AnimationClickListener();
                break;
            case R.id.btn_coordinatorlayout:
                disposeCoordinatorLayoutClickListener();
                break;
            case R.id.btn_smarttablayout:
                disposeSmartTabLayoutClickListener();
                break;
            case R.id.btn_tablayout_with_viewpager:
                disposeTabLayoutClickListener();
                break;
            case R.id.btn_tablayout_with_nestedscrollview:
                disposeNestedClickListener();
                break;
            case R.id.btn_pull_scroll:
                disposePullScrollClickListener();
                break;
        }
        //        testNetWork();
//        LogUtils.e("android.os.Build.MODEL : " + android.os.Build.MODEL);
//        LogUtils.e("Locale.getDefault().getLanguage() : " + Locale.getDefault().getLanguage());
//        LogUtils.e("Locale.getAvailableLocales() : " + Locale.getAvailableLocales().length);
//        LogUtils.e("android.os.Build.VERSION.RELEASE : " +  android.os.Build.VERSION.RELEASE);
//        LogUtils.e("android.os.Build.BRAND : " + android.os.Build.BRAND);
//        Locale[] locales = Locale.getAvailableLocales();
//        int length = locales.length;
//        for(int i = 0; i < length; i++)
//        {
//            LogUtils.e("Locale.getAvailableLocales() " + i + " : " + locales[i]);
//        }
//
//        TelephonyManager tm = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
//        LogUtils.e("tm.getDeviceId() : " + tm.getDeviceId());
    }

    private void dispose3D2AnimationClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, Animation3DActivity2.class);
        startActivity(intent);
    }

    private void disposePullScrollClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, PullSrcollActivity.class);
        startActivity(intent);
    }

    private void dispose3DAnimationClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, Animation3DActivity.class);
        startActivity(intent);
    }

    private void disposeLayoutAnimationClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, LayoutAnimationsActivity.class);
        startActivity(intent);
    }

    private void disposeAnimationClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, AnimationActivity.class);
        startActivity(intent);
    }

    private void disposeBlockViewClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, BlockViewActivity.class);
        startActivity(intent);
    }

    private void disposeFoldingMenuClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, FoldingActivity.class);
        startActivity(intent);
    }

    private void disposeExpandTableClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, ExpandTableActivity.class);
        startActivity(intent);
    }

    private void disposeQRCodeClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, QRCodeActivity.class);
        startActivity(intent);
    }

    private void disposeDrawClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, DrawActivity.class);
        startActivity(intent);
    }

    private void disposeCoordinatorLayoutClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, CoordinatorLayoutActivity.class);
        startActivity(intent);
    }

    private void disposeSmartTabLayoutClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, FiveActivity.class);
        startActivity(intent);
    }

    private void disposeTabLayoutClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, ScrollViewPagerActivity.class);
        startActivity(intent);
    }

    private void disposeNestedClickListener()
    {
        Intent intent = new Intent();
        intent.setClass(this, DrawerLayoutWithNestedSrcollViewActivity.class);
        startActivity(intent);
    }
}