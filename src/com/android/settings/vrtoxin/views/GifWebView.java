package com.android.settings.vrtoxin.views;

import android.content.Context;
import android.webkit.WebView;

public class GifWebView extends WebView {

    public GifWebView(Context context, String path) {
        super(context);        
        
        loadUrl(path);
    }
}
