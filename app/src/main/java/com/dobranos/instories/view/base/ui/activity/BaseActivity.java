package com.dobranos.instories.view.base.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.dobranos.instories.view.base.ui.App;
import com.dobranos.instories.view.injection.ui.AppComponent;

public abstract class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setupComponent(App.get(this).getAppComponent());
    }

    protected abstract void setupComponent(AppComponent appComponent);
}