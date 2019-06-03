package cc.ibooker.richtext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 系统屏幕的一些操作
 * create by 邹峰立 on 2016-8-14
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取dialog宽度
     */
    public static int getDialogW(Context aty) {
        DisplayMetrics dm;
        dm = aty.getResources().getDisplayMetrics();
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth() - 100;
        return dm.widthPixels - 100;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW(Context aty) {
        DisplayMetrics dm;
        dm = aty.getResources().getDisplayMetrics();
        // int w = aty.getWindowManager().getDefaultDisplay().getWidth();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH(Context aty) {
//        DisplayMetrics dm = new DisplayMetrics();
//        dm = aty.getResources().getDisplayMetrics();
//        int h = dm.heightPixels;
//        // int h = aty.getWindowManager().getDefaultDisplay().getHeight();
        WindowManager wm = (WindowManager) aty.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     */
    @SuppressLint("PrivateApi")
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public static int getSizeofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        int size = 0;
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return size;
        }
        int totalHeight = 0;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.setLayoutParams(layoutParams);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                    .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }

        size = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        return size;
    }

    @SuppressLint("NewApi")
    public static int getSizeofGridView(GridView gridView) {
        ListAdapter mAdapter = gridView.getAdapter();
        int size = 0;
        if (mAdapter == null || mAdapter.getCount() == 0) {
            return size;
        }
        int totalHeight = 0;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < (mAdapter.getCount() + 1) / 2; i++) {
            View mView = mAdapter.getView(i, null, gridView);
            mView.setLayoutParams(layoutParams);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                    .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }

        size = totalHeight + (gridView.getVerticalSpacing() * ((mAdapter.getCount() - 1) / 2));
        return size;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context 上下文对象
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display;
        if (manager != null) {
            display = manager.getDefaultDisplay();
            return display.getWidth();
        }
        return 0;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context 上下文对象
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display;
        if (manager != null) {
            display = manager.getDefaultDisplay();
            return display.getHeight();
        }
        return 0;
    }

    /**
     * 获取屏幕中控件顶部位置的高度--即控件顶部的Y点
     */
    public static int getScreenViewTopHeight(View view) {
        return view.getTop();
    }

    /**
     * 获取屏幕中控件底部位置的高度--即控件底部的Y点
     */
    public static int getScreenViewBottomHeight(View view) {
        return view.getBottom();
    }

    /**
     * 获取屏幕中控件左侧的位置--即控件左侧的X点
     */
    public static int getScreenViewLeftHeight(View view) {
        return view.getLeft();
    }

    /**
     * 获取屏幕中控件右侧的位置--即控件右侧的X点
     */
    public static int getScreenViewRightHeight(View view) {
        return view.getRight();
    }

}