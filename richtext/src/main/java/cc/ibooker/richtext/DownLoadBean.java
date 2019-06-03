package cc.ibooker.richtext;

import android.graphics.drawable.Drawable;

/**
 * 下载图片相关Bean文件
 */
public class DownLoadBean {
    private Drawable drawable;
    private String imagePath;
    private boolean isOpenImgCache;
    private Exception e;
    private DownLoadImage.ImageCallBack callBack;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isOpenImgCache() {
        return isOpenImgCache;
    }

    public void setOpenImgCache(boolean openImgCache) {
        isOpenImgCache = openImgCache;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public DownLoadImage.ImageCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(DownLoadImage.ImageCallBack callBack) {
        this.callBack = callBack;
    }
}