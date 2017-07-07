package com.base.application.baseapplication.custom.dialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.application.baseapplication.R;

/**
 * 通用加载框, 样式如下:
 * <PRE>
 *  ______________________
 * |     _                |
 * |    (_) message       |
 * |______________________|
 * </PRE>
 * 
 * @Title:
 * @Description:
 * @Author:12075179
 * @Since:2013-7-4
 * @Version:
 */
public class LoadingDialog extends BasicDialogFragment
{
    /** fragment的名字. */
    private static final String NAME = "LoadingDialog";
    
    /** 加载信息的Key **/
    protected static final String KEY_MESSAGE  = "message";
    /** 是否能够取消  **/
    protected static final String KEY_CANCELABLE = "cancelable";
    
    private DialogInterface.OnCancelListener mOnCancelListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnShowListener mOnShowListener;
    private OnBackPressedListener mOnBackPressedListener;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //初始化view
        View layout = inflater.inflate(R.layout.layout_dialog_loading, container, false);
        
        TextView tvLoadMessage = (TextView)layout.findViewById(R.id.loading_txt);
        //如果有信息,显示加载信息
        Bundle bundle = getArguments();
        String message = bundle != null?bundle.getString(KEY_MESSAGE):"";
        
        if(TextUtils.isEmpty(message)) {
            tvLoadMessage.setVisibility(View.GONE);
        } else {
            tvLoadMessage.setVisibility(View.VISIBLE);
            tvLoadMessage.setText(message);
        }
        return layout;
    }
    
    public void setOnCancelListener(DialogInterface.OnCancelListener mOnCancelListener)
    {
        this.mOnCancelListener = mOnCancelListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener mOnDismissListener)
    {
        this.mOnDismissListener = mOnDismissListener;
    }

    public void setOnShowListener(DialogInterface.OnShowListener mOnShowListener)
    {
        this.mOnShowListener = mOnShowListener;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        if(mOnCancelListener != null) {
            mOnCancelListener.onCancel(dialog);
        }
    }
    
    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
        if(mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }
    
    @Override
    public void onStart()
    {
        super.onStart();
        // 设置对话的事件
        getDialog().setOnShowListener(mOnShowListener);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (mOnBackPressedListener != null) {
                    mOnBackPressedListener.onBackPressed();
                }
                return true;
            }
        });
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * 设置返回键按下监听
     * @param listener {@link OnBackPressedListener}
     */
    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    /**
     * 返回键按下事件监听
     */
    public interface OnBackPressedListener {

        /**
         * 返回键按下的时候
         */
        void onBackPressed();
    }
    
    /**
     * Example:
     * <PRE>
     * 
     * LoadingDialog.Controller controller = new LoadingDialog.Controller()
     *                      .setMessage("message");
     * controller.show(fragmentManager);
     * </PRE>
     *@Title:
     *@Description:
     *@Author:12075179
     *@Since:2015-9-10
     *@Version:
     */
    public static class Controller {
        
        /** 存放数据的容器  **/
        private Bundle mmBundle;
        
        private DialogInterface.OnCancelListener mmOnCancelListener;
        private DialogInterface.OnDismissListener mmOnDismissListener;
        private DialogInterface.OnShowListener mmOnShowListener;
        private OnBackPressedListener mmOnBackPressedListener;
        
        /** 加载框 **/
        private LoadingDialog mmLoadingDialog;
        
        /**
         * Constructor using a context for this builder and the {@link LoadingDialog} it creates.
         */
        public Controller() {
            mmBundle = new Bundle();
        }

        /**
         * Set the message to display.<BR>
         * 
         * 如果想隐藏加载信息, 这个方法不调用即可
         * 
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Controller setMessage(CharSequence message) {
            mmBundle.putCharSequence(KEY_MESSAGE, message);
            return this;
        }
        

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Controller setCancelable(boolean cancelable) {
            mmBundle.putBoolean(KEY_CANCELABLE, cancelable);
            return this;
        }
        
        /**
         * Sets the callback that will be called if the dialog is canceled.
         *
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(DialogInterface.OnDismissListener) setOnDismissListener}.</p>
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(DialogInterface.OnDismissListener)
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Controller setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            mmOnCancelListener = onCancelListener;
            return this;
        }
        
        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Controller setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            mmOnDismissListener = onDismissListener;
            return this;
        }
        
        /**
         * Sets the callback that will be called when the dialog is showed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Controller setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            mmOnShowListener = onShowListener;
            return this;
        }

        /**
         * {@link LoadingDialog#setOnBackPressedListener(OnBackPressedListener)}
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Controller setOnBackPressedListener(OnBackPressedListener listener) {
            mmOnBackPressedListener = listener;
            return this;
        }


        /**
         * Creates a {@link LoadingDialog} with the arguments supplied to this builder.
         */
        private LoadingDialog create() {
            LoadingDialog dialog = new LoadingDialog();
            // 1,设置显示内容
            dialog.setArguments(mmBundle);
            // 2,设置操作事件
            dialog.setOnCancelListener(mmOnCancelListener);
            dialog.setOnDismissListener(mmOnDismissListener);
            dialog.setOnShowListener(mmOnShowListener);
            dialog.setOnBackPressedListener(mmOnBackPressedListener);
            
            return dialog;
        }
        
        /**
         * @return {@link #mmLoadingDialog}
         * @Author 12075179
         * @Date 2015-12-9
         */
        public LoadingDialog getLoadingDialg() {
            if(mmLoadingDialog == null) {
                mmLoadingDialog = create();
            }
            return mmLoadingDialog;
        }

        /**
         * Creates a {@link LoadingDialog} with the arguments supplied to this builder and
         * {@link DialogFragment#show(FragmentTransaction,String)}'s the dialog.
         */
        public void show(FragmentManager fm) {
            if(fm == null) {
                Log.e(NAME, "show error : fragment manager is null.");
                return;
            }
            if(mmLoadingDialog == null) {
                mmLoadingDialog = create();
            }
            // 这边需要判断这个Fragment是否已经被添加,如果添加了,直接返回
            FragmentManager selfFM = mmLoadingDialog.getFragmentManager();
            if(selfFM != null) {
                Log.e(NAME, "show : fragment already added. show it.");
                selfFM.beginTransaction().show(mmLoadingDialog).commit();
                return;
            }
            Log.d(NAME, "show loding dialog.");
            mmLoadingDialog.show(fm, mmLoadingDialog.getName());
        }
        
        /**
         * 关闭加载框
         * @Description:
         * @Author 12075179
         * @Date 2015-9-11
         */
        public void dismiss() {
            if(mmLoadingDialog == null) {
                Log.e(NAME, "dismiss error : loading dialog is null.");
                return;
            }
            if(mmLoadingDialog.getFragmentManager() == null) {
                Log.e(NAME, "dismiss error : fragment manager of loading dialog is null.");
                return;
            }
            Log.d(NAME, "dismiss loding dialog.");
            mmLoadingDialog.dismissAllowingStateLoss();
        }
    }
}
