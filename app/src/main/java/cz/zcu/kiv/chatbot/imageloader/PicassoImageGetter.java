package cz.zcu.kiv.chatbot.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Create instance of {@link android.text.Html.ImageGetter} and download image from view
 * that holds HTML which contains $lt;img&gt; tag to load.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-27-04
 */
public class PicassoImageGetter implements Html.ImageGetter {

    /**
     * Class tag for logger.
     */
    private static final String TAG = PicassoImageGetter.class.getSimpleName();

    /**
     * Reference to text view that holds HTML which contains $lt;img&gt; tag to load.
     */
    private final TextView mTextView;

    /**
     * Initialize text view variable.
     *
     * @param view - view that holds HTML which contains $lt;img&gt; tag to load
     */
    public PicassoImageGetter(TextView view) {
        mTextView = view;
    }

    /**
     * Download image from the internet based on given source.
     * @param source - absolute URL with image location
     * @return drawable image
     */
    @Override
    public Drawable getDrawable(String source) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }

        final Uri uri = Uri.parse(source);

        if (uri.isRelative()) {
            return null;
        }

        final URLDrawable urlDrawable = new URLDrawable(mTextView.getResources(), null);
        LoadFromUriAsyncTask task = new LoadFromUriAsyncTask(mTextView, urlDrawable);

        task.execute(uri);

        return urlDrawable;
    }

    /**
     * Asynchronous image loading from URI.
     */
    private static class LoadFromUriAsyncTask extends AsyncTask<Uri, Void, Bitmap> {
        private final WeakReference<TextView> mTextViewRef;
        private final URLDrawable mUrlDrawable;
        private final Picasso mImageUtils;

        LoadFromUriAsyncTask(TextView textView, URLDrawable urlDrawable) {
            mImageUtils = Picasso.with(textView.getContext());
            mTextViewRef = new WeakReference<>(textView);
            mUrlDrawable = urlDrawable;
        }

        /**
         * Asynchronous image download.
         * @param params - image URI
         * @return - image's bitmap
         */
        @Override
        protected Bitmap doInBackground(Uri... params) {
            try {
                Log.d(TAG, "Async image loading from: " + params[0]);
                return mImageUtils.load(params[0]).get();
            } catch (IOException e) {
                Log.e(TAG, "Cannot load image from: " + params[0]);
                return null;
            }
        }

        /**
         * Redraw textView with downloaded image.
         * @param result - downloaded image
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                return;
            }

            if (mTextViewRef.get() == null) {
                return;
            }

            TextView textView = mTextViewRef.get();

            // change the reference of the current mDrawable to the result from the HTTP call
            mUrlDrawable.mDrawable = new BitmapDrawable(textView.getResources(), result);

            // set bound to scale image to fit width
            int width = textView.getWidth();
            int height = Math.round(1.0f * width *
                    mUrlDrawable.mDrawable.getIntrinsicHeight() /
                    mUrlDrawable.mDrawable.getIntrinsicWidth());
            mUrlDrawable.setBounds(0, 0, width, height);
            mUrlDrawable.mDrawable.setBounds(0, 0, width, height);

            // force redrawing bitmap by setting text
            textView.setText(textView.getText());
        }

    }

    /**
     * Drawable bitmap for image from the internet.
     */
    private static class URLDrawable extends BitmapDrawable {

        private Drawable mDrawable;

        URLDrawable(Resources res, Bitmap bitmap) {
            super(res, bitmap);
        }

        @Override
        public void draw(Canvas canvas) {
            if(mDrawable != null) {
                mDrawable.draw(canvas);
            }
        }

    }
}