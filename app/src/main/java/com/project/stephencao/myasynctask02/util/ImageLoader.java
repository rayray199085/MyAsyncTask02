package com.project.stephencao.myasynctask02.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;
import com.project.stephencao.myasynctask02.R;
import com.project.stephencao.myasynctask02.adapter.MyNewsAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ImageLoader {
    //    private ImageView mImageView;
//    private String mUrl ;
    private LruCache<String, Bitmap> mLruCache;
    private ListView mListView;
    private Set<ImageAsyncTask> mTasks;

    //    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (mImageView.getTag().equals(mUrl)) {
//                mImageView.setImageBitmap((Bitmap) msg.obj);
//            }
//
//        }
//    };
    public void loadImages(int start, int end) {
        for (int i = start; i < end; i++) {
            String url = MyNewsAdapter.URLS[i];
            Bitmap bitmap = withdrawBitmapFromCache(url);
            if (bitmap == null) {
                ImageAsyncTask asyncTask = new ImageAsyncTask(url);
                asyncTask.execute(url);
                mTasks.add(asyncTask);
            } else {
                ImageView imageView = mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }


    public ImageLoader(ListView listView) {
        mListView = listView;
        mTasks = new HashSet<>();
        //get the maximum available memory value
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // return the size of bitmap
                return value.getByteCount();
            }
        };
    }

    public void storeBitmap2Cache(String url, Bitmap bitmap) {
        if (withdrawBitmapFromCache(url) == null) {
            mLruCache.put(url, bitmap);
        }

    }

    public Bitmap withdrawBitmapFromCache(String url) {
        return mLruCache.get(url);
    }

//    public void displayImageWithThread(ImageView imageView, final String url) {
//        mImageView = imageView;
//        mUrl = url;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = getBitmapFromUrl(url);
//                Message message = Message.obtain();
//                message.obj = bitmap;
//                mHandler.sendMessage(message);
//            }
//        }).start();
//    }

    public void displayImageWithAsyncTask(ImageView imageView, String url) {
        Bitmap bitmap = withdrawBitmapFromCache(url);
        if (bitmap == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }


    private Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

    /**
     * cancel(true)
     * if the thread executing this task should be interrupted;
     * cancel(false)
     * otherwise, in-progress tasks are allowed
     * to complete.
     * some views might not have enough time to load the images, but the user starts to scroll the view again, then
     * mTasks is not empty at this moment.
     */
    public void cancelAllTasks() {
        if (mTasks != null) {
            for (ImageAsyncTask task : mTasks) {
                task.cancel(false);
            }
        }
    }

    class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private String mUrl;

        public ImageAsyncTask(String mUrl) {
            this.mUrl = mUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = getBitmapFromUrl(url);
            if (bitmap != null) {
                storeBitmap2Cache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = mListView.findViewWithTag(mUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            mTasks.remove(this);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
