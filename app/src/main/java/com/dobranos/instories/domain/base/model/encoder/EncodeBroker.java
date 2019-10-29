package com.dobranos.instories.domain.base.model.encoder;

public class EncodeBroker<T>
{
    private T source;

    public T getBroker() { return source; }

    public EncodeBroker(T src)
    {
        this.source = src;
    }
}
