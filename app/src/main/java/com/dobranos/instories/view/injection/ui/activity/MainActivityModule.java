package com.dobranos.instories.view.injection.ui.activity;

import com.dobranos.instories.presentation.base.ContentChooserFragmentPresenter;
import com.dobranos.instories.presentation.base.MainActivityPresenter;
import com.dobranos.instories.presentation.base.ContentChooserFragmentView;
import com.dobranos.instories.presentation.base.MainActivityView;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule
{
    private MainActivityView view;

    public MainActivityModule(MainActivityView view)
    {
        this.view = view;
    }

    @Provides public MainActivityView provideView()
    {
        return view;
    }
    @Provides public MainActivityPresenter provideMainActivityPresenter()
    {
        return new MainActivityPresenter(view);
    }
    @Provides public ContentChooserFragmentPresenter provideContentChooserFragmentPresenter()
    {
        return new ContentChooserFragmentPresenter();
    }
}