package com.dobranos.instories.view.base.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import com.dobranos.instories.R;
import com.dobranos.instories.domain.base.mapper.BitmapOperationsMapper;
import com.dobranos.instories.domain.base.mapper.YuvMapper;
import com.dobranos.instories.presentation.base.MainActivityPresenter;
import com.dobranos.instories.presentation.base.MainActivityView;
import com.dobranos.instories.view.base.ui.fragment.ContentChooserFragment;
import com.dobranos.instories.view.injection.ui.AppComponent;
import com.dobranos.instories.view.injection.ui.activity.DaggerMainActivityComponent;
import com.dobranos.instories.view.injection.ui.activity.MainActivityComponent;
import com.dobranos.instories.view.injection.ui.activity.MainActivityModule;

import java.nio.ByteBuffer;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainActivityView
{
    @Inject
    MainActivityPresenter presenter;

    private ViewGroup vRoot;

    //region setup injection

    private MainActivityComponent mainActivityComponent;
    @Override public MainActivityComponent getComponent() { return mainActivityComponent; }
    @Override protected void setupComponent(AppComponent appComponent)
    {
        mainActivityComponent = DaggerMainActivityComponent.builder()
            .appComponent(appComponent)
            .mainActivityModule(new MainActivityModule(this))
            .build();
        mainActivityComponent.inject(this);

        getLifecycle().addObserver(presenter);
    }

    //endregion

    private FragmentManager fragmentManager;

    protected boolean shouldAskPermissions() { return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1); }
    @TargetApi(23) protected void askPermissions()
    {
        String[] permissions = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS =
    {
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    };
    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (context != null && permissions != null)
            for (String permission : permissions)
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
        return true;
    }

//    @Override
//    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults)
//    {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION)
//        {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                // Permission granted.
//            }
//            else
//            {
//                // User refused to grant permission.
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vRoot = findViewById(R.id.v_root);

        fragmentManager = getSupportFragmentManager();
        ContentChooserFragment chooserFragment = (ContentChooserFragment) fragmentManager.findFragmentByTag("ChooserFragment");
        if (chooserFragment == null)
            chooserFragment = new ContentChooserFragment();

        if (savedInstanceState == null)
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, chooserFragment, "ChooserFragment")
                .commit();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (!hasPermissions(this, PERMISSIONS))
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }

    @Override
    public void onBackPressed()
    {
        if (fragmentManager.getBackStackEntryCount() <= 0)
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        presenter.setAvailableScreenSizePx(new Point(vRoot.getWidth(), vRoot.getHeight()));
    }
}