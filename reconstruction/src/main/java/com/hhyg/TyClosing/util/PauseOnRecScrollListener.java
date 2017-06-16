package com.hhyg.TyClosing.util;

import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by user on 2017/6/15.
 */

public class PauseOnRecScrollListener extends RecyclerView.OnScrollListener{
    private ImageLoader imageLoader;
    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;

    public PauseOnRecScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch(newState) {
            case 0:
                this.imageLoader.resume();
                break;
            case 1:
                if(this.pauseOnScroll) {
                    this.imageLoader.pause();
                }
                break;
            case 2:
                if(this.pauseOnFling) {
                    this.imageLoader.pause();
                }
        }
    }
}
