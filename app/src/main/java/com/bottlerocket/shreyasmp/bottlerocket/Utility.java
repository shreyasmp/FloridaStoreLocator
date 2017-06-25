package com.bottlerocket.shreyasmp.bottlerocket;


/*
* Utility class to copy images download from json object into bitmap
* for scaling and conversion, used in Image Loader class
 */
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by shreyasmp on 5/4/16.
 */
public class Utility {

    public static void CopyStream(InputStream iStream, OutputStream oStream) {
        final int buffer_size=1024;
        try{
            byte[] bytes=new byte[buffer_size];
            for(;;) {
                int count = iStream.read(bytes, 0, buffer_size);
                if(count == 1)
                    break;
                oStream.write(bytes, 0, count);
            }
        } catch (Exception ex) {

        }
    }
}
