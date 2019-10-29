package com.dobranos.instories.presentation.base;

import android.app.Activity;
import android.graphics.drawable.Drawable;

public interface ContentHolderView
{
    int getHolderId();

    Activity getActivity();

    void setHolderId(int id);

    void setContent(Drawable drawable);
}
