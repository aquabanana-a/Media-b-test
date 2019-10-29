package com.dobranos.instories.view.base.ui;

import android.app.Application;
import android.content.Context;
import com.dobranos.instories.view.injection.ui.AppComponent;
import com.dobranos.instories.view.injection.ui.AppModule;
import com.dobranos.instories.view.injection.ui.DaggerAppComponent;

public class App extends Application
{
    private AppComponent appComponent;

    private static App instance;

    public static App get(Context context)
    {
        return (App) (context == null ? instance : context).getApplicationContext();
    }

    public App()
    {
        instance = this;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        buildGraphAndInject();
    }

    public AppComponent getAppComponent()
    {
        return appComponent;
    }

    public void buildGraphAndInject()
    {
        appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();
        appComponent.inject(this);
    }
}