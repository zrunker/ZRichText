package cc.ibooker.richtext;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用来获取网络图片-没有缓存
 *
 * @author 邹峰立
 */
public class DownLoadImage {
    private ArrayList<MyHandler> handlers;
    private ExecutorService executorService;
    private static DownLoadImage downLoadImage;

    public static DownLoadImage getInstance() {
        if (downLoadImage == null) {
            synchronized (DownLoadImage.class) {
                if (downLoadImage == null)
                    downLoadImage = new DownLoadImage();
            }
        }
        return downLoadImage;
    }

    // 销毁方法
    public void deStory() {
        if (handlers != null) {
            for (MyHandler handler : handlers)
                if (handler != null)
                    handler.removeCallbacksAndMessages(null);
            handlers.clear();
        }
        if (executorService != null)
            executorService.shutdownNow();
    }

    // 加载图片
    public void loadImage(final String imagePath, final boolean isOpenImgCache, final ImageCallBack callBack) {
        // 开启Handler
        final MyHandler myHandler = new MyHandler(this);
        if (handlers == null)
            handlers = new ArrayList<>();
        handlers.add(myHandler);
        // 定义下载数据对象DownLoadBean
        final DownLoadBean downLoadBean = new DownLoadBean();
        downLoadBean.setImagePath(imagePath);
        downLoadBean.setOpenImgCache(isOpenImgCache);
        downLoadBean.setCallBack(callBack);
        // 获取Drawable
        Drawable drawable = null;
        if (isOpenImgCache)
            drawable = ImageCacheUtil.getInstanse().init().getDrawableByPath(imagePath);
        if (drawable == null) {
            // 开启线程
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(imagePath);
                        Drawable drawable = Drawable.createFromStream(url.openStream(), null);
                        Message message = Message.obtain();
                        downLoadBean.setDrawable(drawable);
                        message.obj = downLoadBean;
                        message.what = 1;
                        myHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message message = Message.obtain();
                        downLoadBean.setE(e);
                        message.obj = downLoadBean;
                        message.what = 2;
                        myHandler.sendMessage(message);
                    }
                }
            });
            if (executorService == null)
                executorService = Executors.newCachedThreadPool();
            executorService.execute(thread);
        } else {
            Message message = Message.obtain();
            downLoadBean.setDrawable(drawable);
            message.obj = downLoadBean;
            message.what = 1;
            myHandler.sendMessage(message);
        }
    }

    // 自定义Handler
    private static class MyHandler extends Handler {
        private WeakReference<DownLoadImage> mWef;

        MyHandler(DownLoadImage downLoadImage) {
            mWef = new WeakReference<>(downLoadImage);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DownLoadBean downLoadBean = (DownLoadBean) msg.obj;
            ImageCallBack callback = downLoadBean.getCallBack();
            switch (msg.what) {
                case 1:
                    Drawable drawable = downLoadBean.getDrawable();
                    if (downLoadBean.isOpenImgCache())
                        ImageCacheUtil.getInstanse().init().putDrawableByPath(downLoadBean.getImagePath(), drawable);
                    if (callback != null)
                        callback.getDrawable(drawable);
                    break;
                case 2:
                    if (callback != null)
                        callback.onError(downLoadBean.getE());
                    break;
            }
            // 关闭所有通知Handler
            removeCallbacksAndMessages(null);
            mWef.get().handlers.remove(this);
        }
    }

    // 对外提供回调
    public interface ImageCallBack {
        void getDrawable(Drawable drawable);

        void onError(Exception e);
    }

}
