package com.bottlerocket.shreyasmp.bottlerocket;


/*
* Memory cache class will limit memory usage with image loading.
*  unused images will be removed if not shown in content view
 */
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by shreyasmp on 5/4/16.
 */

//  setting a max memory size in bytes
//  using less heap size available
public class MemoryCache {

    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
    private long size = 0;
    private long limit = 1000000;

    public MemoryCache() {
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        limit = new_limit;
        Log.d(TAG, "Memory cache used to : " + limit / 1024. / 1024. + "MB");
    }

    public Bitmap get(String id) {
        try {
            if(!cache.containsKey(id))
                return null;
            return cache.get(id);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap) {
        try {
            if(cache.containsKey(id))
                size -= getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        } catch(Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        Log.i(TAG, "cache size: " +size+ " length: " + cache.size());
        if(size > limit) {
            // Least recently accessed item will be first one iterated
            Iterator<Map.Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if(size <= limit)
                    break;
            }
            Log.i(TAG, "Clean cache, New size " +cache.size());
        }
    }

    public void clear() {
        try {
            cache.clear();
            size = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
