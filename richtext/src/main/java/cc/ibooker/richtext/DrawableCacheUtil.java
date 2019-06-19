package cc.ibooker.richtext;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.LruCache;

/**
 * 图片加载缓存类
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class DrawableCacheUtil {
    private LruCache<String, Drawable> mLruCache;
    private static DrawableCacheUtil imageCacheUtil;

    public static DrawableCacheUtil getInstanse() {
        if (imageCacheUtil == null) {
            synchronized (DrawableCacheUtil.class) {
                if (imageCacheUtil == null)
                    imageCacheUtil = new DrawableCacheUtil();
            }
        }
        return imageCacheUtil;
    }

    // 初始化mLruCache
    public DrawableCacheUtil init() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();// 获取系统分配给应用的总内存大小
        int mCacheSize = maxMemory / 8;// 设置图片内存缓存占用八分之一
        mLruCache = new LruCache<String, Drawable>(mCacheSize) {
            //必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Drawable value) {
                if (value instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) value).getBitmap();
                    return bitmap == null ? 0 : bitmap.getByteCount();
                }
                return super.sizeOf(key, value);
            }
        };
        return this;
    }

    // 获取图片Drawable
    public Drawable getDrawableByPath(String imgPath) {
        return mLruCache.get(imgPath);
    }

    // 保存图片Drawable
    public void putDrawableByPath(String imgPath, Drawable drawable) {
        mLruCache.put(imgPath, drawable);
    }
}
