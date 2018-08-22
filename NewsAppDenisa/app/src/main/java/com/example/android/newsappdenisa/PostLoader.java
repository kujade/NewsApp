package com.example.android.newsappdenisa;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;


public class PostLoader extends AsyncTaskLoader<List<Post>> {
    private String mWebUrl = "";

    public PostLoader(Context context, String url) {
        super( context );
        mWebUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //Background thread
    @Override
    public List<Post> loadInBackground() {
        if (mWebUrl == null) {
            return null;
        }
        List<Post> posts = (List<Post>) QueryUtils.fetchPostData( mWebUrl );
        return posts;
    }
}
