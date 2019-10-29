package com.dobranos.instories.domain.base.repository;

import android.graphics.drawable.Drawable;
import androidx.lifecycle.LiveData;
import com.dobranos.instories.data.base.model.Content;

public interface MemoryRepository
{
    LiveData<Drawable> addContent(long id, Drawable content);

    void removeContent(long id);

    LiveData<Drawable> getContent(long id);

    boolean containsId(long id);
}