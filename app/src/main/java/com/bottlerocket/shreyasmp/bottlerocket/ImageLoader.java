package com.bottlerocket.shreyasmp.bottlerocket;


/*
* Image Loaded class helps download display and cachec images using imageLoaded , images
* unloaded automatically if the device memory is low and is made sure size of images are
* appropriate and cached in memory.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;

/**
 * Created by shreyasmp on 5/4/16.
 */
public class ImageLoader {

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;

    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    ExecutorService executorService;

    // Handler to display images in UI Thread
    Handler handler = new Handler();

    public ImageLoader(Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    final int stub_id = R.drawable.br_img;

    public void DisplayImage(String url, ImageView imageView) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        else {
            queueLogos(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queueLogos(String url, ImageView imageView) {
        logoToLoad l = new logoToLoad(url, imageView);
        executorService.submit(new LogoLoader(l));
    }

    private Bitmap getBitmap(String url) {

        File f = fileCache.getFile(url);
        Bitmap b = decodeFile(f);
        if( b != null )
            return b;

        // Downloading images from json object urls
        try {
            Bitmap bitmap = null;
            URL imageURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setInstanceFollowRedirects(true);

            InputStream iStream = connection.getInputStream();
            OutputStream oStream = new FileOutputStream(f);

            Utility.CopyStream(iStream, oStream);
            oStream.close();
            connection.disconnect();
            bitmap = decodeFile(f);
            return bitmap;
        } catch(Throwable ex) {
            ex.printStackTrace();
            if(ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    // decoding images and scaling to reduce memory consumption
    // finding the correct scale size as a power of 2.
    // decoing the images in sample sizes

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            FileInputStream inStream = new FileInputStream(f);
            BitmapFactory.decodeStream(inStream, null, options);
            inStream.close();

            final int REQUIRED_SIZE = 70;
            int width_tmp = options.outWidth;
            int height_temp = options.outHeight;
            int scale = 1;
            while(true) {
                if(width_tmp/2 < REQUIRED_SIZE || height_temp/2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_temp /= 2;
                scale *= 2;
            }
            BitmapFactory.Options options1 = new BitmapFactory.Options();
            options1.inSampleSize = scale;
            FileInputStream inStream1 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(inStream1, null, options1);
            inStream1.close();
            return bitmap;
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Task to queue images for loading
    private class logoToLoad {
        public String url;
        public ImageView imageView;

        public logoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }


     class LogoLoader implements Runnable {
        logoToLoad logoLoad;

         LogoLoader(logoToLoad logoLoad) {
            this.logoLoad = logoLoad;
        }

        @Override
        public void run() {
            try {
                if(imageViewReused(logoLoad))
                    return;
                Bitmap bmp = getBitmap(logoLoad.url);
                memoryCache.put(logoLoad.url, bmp);
                if(imageViewReused(logoLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, logoLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(logoToLoad logoLoad) {
        String tag = imageViews.get(logoLoad.imageView);
        if(tag == null || !tag.equals(logoLoad.url))
            return true;
        return false;
    }

    // Display bitmap in UI Thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        logoToLoad logoLoad;

        public BitmapDisplayer(Bitmap b, logoToLoad l) {
            bitmap = b;
            logoLoad  = l;
        }

        public void run() {
            if(imageViewReused(logoLoad))
                return;
            if(bitmap != null)
                logoLoad.imageView.setImageBitmap(bitmap);
            else
                logoLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
