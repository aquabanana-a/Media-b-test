package com.dobranos.instories.view.injection.ui;

import com.dobranos.instories.domain.base.resolver.renderer.TriImageRenderResolver;
import com.dobranos.instories.presentation.base.ContentChooserFragmentPresenter;
import com.dobranos.instories.presentation.base.ContentHolderPresenter;
import com.dobranos.instories.presentation.base.MainActivityPresenter;
import com.dobranos.instories.view.base.ui.App;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules =
    {
        AppModule.class
    }
)
public interface AppComponent
{
    void inject(App app);

    void inject(MainActivityPresenter value);
    void inject(ContentChooserFragmentPresenter value);
    void inject(ContentHolderPresenter value);

    void inject(TriImageRenderResolver value);
}