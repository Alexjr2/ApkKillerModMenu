/*
 * Edited by Laughing Muffin
 */

package com.muffin;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@SuppressWarnings("all")
public class APKKiller {

    static {
        String libraryPath = "assets/so/libmuffin.so";
        URL resourceUrl = getClass().getClassLoader().getResource(libraryPath);
        String libraryAbsolutePath = resourceUrl.getPath();
        System.load(libraryAbsolutePath);
    }

    public static native void Start(Context context);

    public static native Object processInvoke(Method method, Object[] args);

    private static InvocationHandler myInvocationHandler = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return processInvoke(method, args);
        }
    };
}
