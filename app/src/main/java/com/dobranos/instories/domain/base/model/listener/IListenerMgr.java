package com.dobranos.instories.domain.base.model.listener;

import java.util.List;

public interface IListenerMgr<T, L extends IListener> /* S.D. @ 2017 */
{
    T addListener(L listener);
    T removeListener(L listener);
    T removeListeners(List<L> listeners);
    T clearListeners();
}