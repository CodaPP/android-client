package com.ialex.foodsavr.component;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by alex on 22/01/2018.
 */

public class RecyclerViewSpacingDecorator extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private final int horizontalSpaceMargin;

    public RecyclerViewSpacingDecorator(int verticalSpaceHeight, int horizontalSpaceMargin) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.horizontalSpaceMargin = horizontalSpaceMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
        outRect.left = horizontalSpaceMargin;
        outRect.right = horizontalSpaceMargin;
    }
}