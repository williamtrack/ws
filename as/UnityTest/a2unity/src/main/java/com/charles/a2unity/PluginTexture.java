package com.charles.a2unity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES10;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//  Created by Charles on 2021/3/7.
//  mail: zingon@163.com
public class PluginTexture {
    private volatile EGLContext mSharedEglContext;
    private volatile EGLConfig mSharedEglConfig;

    private String TAG="will";
    private EGLDisplay mEGLDisplay;
    private EGLConfig mEglConfig;
    private EGLContext mEglContext;
    private EGLSurface mEglSurface;

    // 创建单线程池，用于处理OpenGL纹理
    private final ExecutorService mRenderThread = Executors.newSingleThreadExecutor();
    private int mTextureID = 0;
    private int mTextureWidth = 0;
    private int mTextureHeight = 0;

    public int getPluginTextureWidth() {
        return mTextureWidth;
    }

    public int getPluginTextureHeight() {
        return mTextureHeight;
    }

    public int getPluginTextureID() {
        return mTextureID;
    }

    public PluginTexture() {
    }


    private void glLogE(String msg) {
        Log.e(TAG, msg + ", err=" + GLES10.glGetError());
    }

    private void initOpenGL() {
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            glLogE("eglGetDisplay failed");
            return;
        }

        int[] version = new int[2];
        if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            mEGLDisplay = null;
            glLogE("eglInitialize failed");
            return;
        }

//        int[] eglConfigAttribList = new int[]{
//                EGL14.EGL_RED_SIZE, 8,
//                EGL14.EGL_GREEN_SIZE, 8,
//                EGL14.EGL_BLUE_SIZE, 8,
//                EGL14.EGL_ALPHA_SIZE, 8,
//                EGL14.EGL_NONE
//        };
//        int[] numEglConfigs = new int[1];
//        EGLConfig[] eglConfigs = new EGLConfig[1];
//        if (!EGL14.eglChooseConfig(mEGLDisplay, eglConfigAttribList, 0, eglConfigs, 0,
//                eglConfigs.length, numEglConfigs, 0)) {
//            glLogE("eglGetConfigs failed");
//            return;
//        }
//        mEglConfig = eglConfigs[0];

        int[] eglContextAttribList = new int[]{
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };

        //Java线程的EGLContext时，将Unity线程的EGLContext和EGLConfig作为参数传递给eglCreateContext，
        // 从而实现两个线程共享EGLContext
        mEglContext = EGL14.eglCreateContext(mEGLDisplay, mSharedEglConfig, mSharedEglContext, eglContextAttribList, 0);
        if (mEglContext == EGL14.EGL_NO_CONTEXT) {
            glLogE("eglCreateContext failed");
            return;
        }

        int[] surfaceAttribList = {
                EGL14.EGL_WIDTH, 64,
                EGL14.EGL_HEIGHT, 64,
                EGL14.EGL_NONE
        };
        // Java线程不进行实际绘制，因此创建PbufferSurface而非WindowSurface
        mEglSurface = EGL14.eglCreatePbufferSurface(mEGLDisplay, mSharedEglConfig, surfaceAttribList, 0);
        if (mEglSurface == EGL14.EGL_NO_SURFACE) {
            glLogE("eglCreatePbufferSurface failed");
            return;
        }

        if (!EGL14.eglMakeCurrent(mEGLDisplay, mEglSurface, mEglSurface, mEglContext)) {
            glLogE("eglMakeCurrent failed");
            return;
        }
        GLES20.glFlush();
    }

    public void setupOpenGL() {
        // 注意：该调用一定是从Unity绘制线程发起
        // 获取Unity绘制线程的EGLContext
        mSharedEglContext = EGL14.eglGetCurrentContext();
        if (mSharedEglContext == EGL14.EGL_NO_CONTEXT) {
            glLogE("eglGetCurrentContext failed");
            return;
        }
        EGLDisplay sharedEglDisplay = EGL14.eglGetCurrentDisplay();
        if (sharedEglDisplay == EGL14.EGL_NO_DISPLAY) {
            glLogE("sharedEglDisplay failed");
            return;
        }
        // 获取Unity绘制线程的EGLConfig
        int[] numEglConfigs = new int[1];
        EGLConfig[] eglConfigs = new EGLConfig[1];
        if (!EGL14.eglGetConfigs(sharedEglDisplay, eglConfigs, 0, eglConfigs.length, numEglConfigs, 0)) {
            glLogE("eglGetConfigs failed");
            return;
        }
        mSharedEglConfig = eglConfigs[0];
        mRenderThread.execute(new Runnable() {
            @Override
            public void run() {
                // 初始化OpenGL环境
                initOpenGL();

                // 生成OpenGL纹理ID
                int textures[] = new int[1];
                GLES20.glGenTextures(1, textures, 0);
                if (textures[0] == 0) {
                    glLogE("glGenTextures failed");
                    return;
                }

                mTextureID = textures[0];
                mTextureWidth = 640;
                mTextureHeight = 360;
                Log.d("will", "render"+mTextureID);
            }
        });
    }

    public void updateTexture() {
        Log.d("will", "update");
        mRenderThread.execute(new Runnable() {
            @Override
            public void run() {
                String imageFilePath = "/sdcard/BOE/test.bmp";
                final Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
                GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                bitmap.recycle();
            }
        });
    }

    public void destroy() {
        mRenderThread.shutdownNow();
    }
}
