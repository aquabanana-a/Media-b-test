package com.dobranos.instories.presentation.base;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.dobranos.instories.domain.base.interactor.StreamProcessor;
import com.dobranos.instories.domain.base.resolver.ResolutionResolver;
import com.dobranos.instories.domain.base.resolver.renderer.TriImageRenderResolver;
import com.dobranos.instories.view.base.ui.App;

import org.jcodec.common.model.Size;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContentChooserFragmentPresenter implements BaseFragmentPresenter<ContentChooserFragmentView>, LifecycleObserver
{
    @Inject
    ResolutionResolver resResolver;

    private ContentChooserFragmentView view;

    public ContentChooserFragmentPresenter()
    {
        App.get(null).getAppComponent().inject(this);
    }

    @Override
    public void init(ContentChooserFragmentView view)
    {
        this.view = view;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate()
    {
        resResolver.getSurfaceResolution().observeForever(r ->
        {
            view.setChooserSurfaceResolution(r);
        });
    }

    public void onExportClicked()
    {
//        ExecutorService es = Executors.newFixedThreadPool(1);
//        es.execute(() ->
//        {
//            String appDirectoryName = "XYZ";
//            File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), appDirectoryName);
//
//            imageRoot.mkdirs();
//
//            TriImageRenderResolver tir = new TriImageRenderResolver(view.getActivity());
//            tir.start();
//        });

        Toast.makeText(view.getActivity(), "Please wait for 15 sec and see Pictures\\XYZ folder", Toast.LENGTH_LONG).show();
        ExecutorService es = Executors.newFixedThreadPool(1);
        es.execute(() ->
        {
            StreamProcessor p = new StreamProcessor(new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "XYZ"), "sample.mp4"), view.getActivity(), new Size(1280, 720));
            p.start();

            try
            {
                Thread.sleep(15000);
            }
            catch (InterruptedException e) { }

            p.stop();
        });
    }

    String getGalleryPath()
    {
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        if (!folder.exists())
        {
            folder.mkdir();
        }

        return folder.getAbsolutePath();
    }
}
