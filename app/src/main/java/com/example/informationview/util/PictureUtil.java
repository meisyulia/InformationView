package com.example.informationview.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.informationview.R;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

//需要添加依赖：implementation 'com.squareup.picasso:picasso:2.71828'
public class PictureUtil {
    public static void loadPicture(Context context, String imageUrl, ImageView imageView){
        Drawable placeholder = context.getResources().getDrawable(R.drawable.placeholder);
        Picasso picasso = new Picasso.Builder(context)
                .build();
        picasso.load(imageUrl).placeholder(placeholder)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) // 禁用内存缓存
                .networkPolicy(NetworkPolicy.NO_CACHE) // 禁用网络缓存
                .into(imageView);
    }
}
