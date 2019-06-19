package cc.ibooker.richtext;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 图片加载缓存类
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class BitmapCacheUtil {
    private LruCache<String, Bitmap> mLruCache;
    private static BitmapCacheUtil imageCacheUtil;

    public static BitmapCacheUtil getInstanse() {
        if (imageCacheUtil == null) {
            synchronized (BitmapCacheUtil.class) {
                if (imageCacheUtil == null)
                    imageCacheUtil = new BitmapCacheUtil();
            }
        }
        return imageCacheUtil;
    }

    // 初始化mLruCache
    public BitmapCacheUtil init() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();// 获取系统分配给应用的总内存大小
        int mCacheSize = maxMemory / 8;// 设置图片内存缓存占用八分之一
        mLruCache = new LruCache<String, Bitmap>(mCacheSize) {
            //必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value == null ? 0 : value.getByteCount();
            }
        };
        return this;
    }

    // 获取图片Bitmap
    public Bitmap getBitmapByPath(String imgPath) {
        return mLruCache.get(imgPath);
    }

    // 保存图片Bitmap
    public void putBitmapByPath(String imgPath, Bitmap bitmap) {
        mLruCache.put(imgPath, bitmap);
    }
}
