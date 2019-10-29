package com.dobranos.instories.view.base.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.dobranos.instories.R;
import com.dobranos.instories.presentation.base.ContentHolderPresenter;
import com.dobranos.instories.presentation.base.ContentHolderView;
import com.jsibbold.zoomage.ZoomageView;

public final class ContentHolderWidget extends FrameLayout implements ContentHolderView
{
    private ContentHolderPresenter presenter;

    private View vBrowse;
    private ImageView vClose;
    private ViewGroup vgAdd;
    private ZoomageView zivMedia;

    public ContentHolderWidget(Context context) { super(context); init(context, null); }
    public ContentHolderWidget(Context context, AttributeSet attributeSet) { super(context, attributeSet); init(context, attributeSet); }
    public ContentHolderWidget(Context context, AttributeSet attributeSet, int defStyleAttr) { super(context, attributeSet, defStyleAttr); init(context, attributeSet); }

    private void init(Context cx, AttributeSet attrs)
    {
        LayoutInflater.from(cx).inflate(R.layout.view_content_holder, this, true);

        presenter = new ContentHolderPresenter(this);

        this.vgAdd = findViewById(R.id.vg_add);

        this.vClose = findViewById(R.id.iv_close);
        this.vClose.setOnClickListener(v ->
        {
            presenter.onCloseContentClicked();
        });

        this.vBrowse = findViewById(R.id.v_browse);
        this.vBrowse.setOnClickListener(v ->
        {
//                Matrix m = zivMedia.getImageMatrix();
//                int a = 10;
            presenter.onBrowseContentClicked();
        });

        this.zivMedia = findViewById(R.id.ziv_media);
    }

    private int holderId;
    public int getHolderId() { return holderId; }
    public void setHolderId(int value) { holderId = value; }

    public void setContent(Drawable drawable)
    {
        zivMedia.setImageDrawable(drawable);

        zivMedia.setVisibility(drawable != null ? VISIBLE : GONE);
        vClose.setVisibility(drawable != null ? VISIBLE : GONE);
        vBrowse.setVisibility(drawable != null ? GONE : VISIBLE);
        vgAdd.setVisibility(drawable != null ? GONE : VISIBLE);
    }

    public Activity getActivity()
    {
        Context context = getContext();
        while (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
                return (Activity) context;
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
