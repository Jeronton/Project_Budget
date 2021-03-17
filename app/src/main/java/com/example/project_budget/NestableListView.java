package com.example.project_budget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NestableListView extends ListView {
    public NestableListView(Context context) {
        super(context);
    }

    public NestableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 4, MeasureSpec.AT_MOST));
    }
}