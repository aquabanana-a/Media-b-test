package com.dobranos.instories.view.injection.ui.activity;

import com.dobranos.instories.view.base.ui.activity.MainActivity;
import com.dobranos.instories.view.base.ui.fragment.ContentChooserFragment;
import com.dobranos.instories.view.injection.ui.AppComponent;
import dagger.Component;

@ActivityScope
@Component(
    dependencies = AppComponent.class,
    modules = MainActivityModule.class
)
public interface MainActivityComponent
{
    void inject(MainActivity value);
    void inject(ContentChooserFragment value);


}