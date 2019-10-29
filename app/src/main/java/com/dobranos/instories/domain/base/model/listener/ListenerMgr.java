package com.dobranos.instories.domain.base.model.listener;

import java.util.ArrayList;
import java.util.List;

public abstract class ListenerMgr<T extends IListenerMgr, L extends IListener> implements IListenerMgr<T, L> /* S.D. @ 2016 */
{
    private final List<L> mListeners;
    private final Object mListenersLo = new Object();

    private boolean mSyncNeeded = true;
    private List<L> mCopiedListeners;

    public List<L> getListeners()
    {
        return mListeners;
    }

    public ListenerMgr()
    {
        mListeners = new ArrayList<L>();
    }

    public T addListener(L listener)
    {
        if(listener == null || mListeners.contains(listener))
            return (T)this;

        synchronized(mListenersLo)
        {
            mListeners.add(listener);
            mSyncNeeded = true;
        }
        return (T)this;
    }

    public T removeListener(L listener)
    {
        if (listener == null)
            return (T)this;

        synchronized(mListenersLo)
        {
            if(mListeners.remove(listener))
                mSyncNeeded = true;
        }
        return (T)this;
    }

    public T removeListeners(List<L> listeners)
    {
        if (listeners == null)
            return (T)this;

        synchronized(mListenersLo)
        {
            for(L listener : listeners)
            {
                if(listener == null)
                    continue;

                if(mListeners.remove(listener))
                    mSyncNeeded = true;
            }
        }
        return (T)this;
    }

    public T clearListeners()
    {
        synchronized(mListenersLo)
        {
            if (mListeners.size() == 0)
                return (T)this;

            mListeners.clear();
            mSyncNeeded = true;
        }
        return (T)this;
    }

    protected List<L> getSynchronizedListeners()
    {
        synchronized(mListenersLo)
        {
            if (mSyncNeeded == false)
                return mCopiedListeners;

            List<L> copiedListeners = new ArrayList(mListeners.size());
            for(L listener : mListeners)
                copiedListeners.add(listener);

            // Synchronize.
            mCopiedListeners = copiedListeners;
            mSyncNeeded = false;

            return copiedListeners;
        }
    }
}