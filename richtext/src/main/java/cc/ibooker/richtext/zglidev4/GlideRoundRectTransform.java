//package cc.ibooker.richtext.zglidev4;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapShader;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.support.annotation.NonNull;
//
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
//import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
//import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
//
//import java.io.UnsupportedEncodingException;
//import java.security.MessageDigest;
//
//public class GlideRoundRectTransform extends BitmapTransformation {
//    private final String ID = "cc.ibooker.zglide.GlideRoundRectTransform";
//    private float radius;
//
//    public GlideRoundRectTransform(float radius) {
//        this.radius = radius;
//    }
//
//    @Override
//    public Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
//        return roundRect(pool, TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight));
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        return o instanceof GlideCircleTransform;
//    }
//
//    @Override
//    public int hashCode() {
//        return ID.hashCode();
//    }
//
//    @Override
//    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
//        try {
//            messageDigest.update(ID.getBytes(STRING_CHARSET_NAME));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private Bitmap roundRect(BitmapPool pool, Bitmap source) {
//        if (source == null) return null;
//        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(result);
//        Paint paint = new Paint();
//        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
//        paint.setAntiAlias(true);
//
//        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
//        canvas.drawRoundRect(rectF, radius, radius, paint);
//
//        return result;
//    }
//
//}
