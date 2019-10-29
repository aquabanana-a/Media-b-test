package com.dobranos.instories.view.base.ui.fragment;

import androidx.fragment.app.Fragment;
import com.dobranos.instories.view.injection.model.HasComponent;

public abstract class BaseFragment extends Fragment
{
    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentType)
    {
        return componentType.cast(((HasComponent<T>) getActivity()).getComponent());
    }
}