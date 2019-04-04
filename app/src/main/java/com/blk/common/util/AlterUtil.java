package com.blk.common.util;

import android.content.Context;
import android.widget.Toast;

public class AlterUtil {
    public static void alterTextLong(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void alterTextShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
