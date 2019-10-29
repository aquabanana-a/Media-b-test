package com.dobranos.instories.data.base.repository;

import android.graphics.drawable.Drawable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.dobranos.instories.data.base.model.Content;
import com.dobranos.instories.domain.base.repository.MemoryRepository;

import java.util.HashMap;
import java.util.Map;

public class MemoryRepositoryImpl implements MemoryRepository
{
    private Map<Long, LiveData<Drawable>> contents = new HashMap<>();
    private Object contentsLo = new Object();

    public LiveData<Drawable> addContent(long id, Drawable content)
    {
        synchronized (contentsLo)
        {
            MutableLiveData<Drawable> ld = (MutableLiveData<Drawable>) contents.get(id);
            if (ld == null)
                contents.put(id, ld = new MutableLiveData<>());
            ld.postValue(content);
            return ld;
        }
    }

    public void removeContent(long id)
    {
        synchronized (contentsLo)
        {
            MutableLiveData<Drawable> ld = (MutableLiveData<Drawable>) contents.get(id);
            if (ld != null)
                ld.postValue(null);
        }
    }

    public LiveData<Drawable> getContent(long id)
    {
        synchronized (contentsLo)
        {
            return contents.get(id);
        }
    }

    public boolean containsId(long id)
    {
        synchronized (contentsLo)
        {
            return contents.containsKey(id);
        }
    }
}