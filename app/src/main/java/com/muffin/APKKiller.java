/*
 * Edited by t.me/@Rindaman5555
 */

package com.muffin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings("all")
public class APKKiller {
    public static final String MUFFIN = "libmuffin";

    public static String libmuffin() {
        String arch = System.getProperty("os.arch");
        if (arch != null && (arch.contains("aarch64") || arch.contains("x86_64"))) {
            return "libmuffin64.so";
        } else {
            return "libmuffin32.so";
        }
    }

    public static void loadLibFromAssets(Context context) throws RuntimeException, IOException {

        if (context == null) {
            return;
        }

        String dummyLib = libmuffin();

        AssetManager assetManager = context.getAssets();
        String[] list = assetManager.list(MUFFIN);
        
        if (list == null) {
            throw new RuntimeException("Asset list is null");
        }

        String listToString = Arrays.toString(list);

        if (!listToString.contains(dummyLib)) {
            throw new RuntimeException("Unable to locate file");
        } else {
            try (InputStream is = assetManager.open(MUFFIN.concat("/").concat(dummyLib));
                 OutputStream os = new FileOutputStream(new File(context.getDataDir().getAbsolutePath(), dummyLib))) {
                transfer(is, os);
                doLoad(new File(context.getDataDir().getAbsolutePath(), dummyLib));
            } catch (Exception exception) {
                Log.e("EXR", "loadLibFromAssets: ", exception);
                throw new RuntimeException("Crappy crap happened");
            }
        }
    }

    public static void Starting(Context context) {
        try {
            loadLibFromAssets(context);
            Start(context);
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static  native void Start(Context context);

    public static native Object processInvoke(Method method, Object[] args);

    private static final InvocationHandler myInvocationHandler = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return processInvoke(method, args);
        }
    };

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    private static void doLoad(File outFile) {
        try {
            System.load(outFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void transfer(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8192];
        int n;
        while ((n = is.read(buffer)) != -1) {
            os.write(buffer, 0, n);
        }
    }
}
