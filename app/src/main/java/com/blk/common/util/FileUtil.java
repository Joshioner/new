/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.blk.common.util;

import android.content.Context;

import java.io.File;
//文件的操作工具类
public class FileUtil {
    private static boolean savpplication;

    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    public static File getPersonPhoto(Context context) {
        File file = new File(context.getFilesDir(), "personPhoto.jpg");
        return file;
    }
    public static File getMemberPhoto(Context context,String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return file;
    }

    public static boolean getSavpplication() {
        return savpplication;
    }
}
