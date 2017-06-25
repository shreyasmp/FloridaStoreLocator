package com.bottlerocket.shreyasmp.bottlerocket;

/*
* File cache class saves temporary images into device internal storage to prevent
* images from being download repeatedly, avoids repeated backend call image download
 */

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by shreyasmp on 5/4/16.
 */
public class FileCache {

    private File cacheDirectory;

    // Identifying the directory to save cached images on storage
    public FileCache(Context context) {
        if(android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDirectory = new File(android.os.Environment.getExternalStorageDirectory(), "BottleRocketCache");
        else
            cacheDirectory = context.getCacheDir();
        if(!cacheDirectory.exists())
            cacheDirectory.mkdirs();
    }

    public File getFile(String url) {
        String fileName = String.valueOf(url.hashCode());
        File f = new File(cacheDirectory, fileName);
        return f;
    }

    public void clear() {
        File[] files = cacheDirectory.listFiles();
        if(files == null)
            return;
        for(File f : files)
            f.delete();
    }
}
