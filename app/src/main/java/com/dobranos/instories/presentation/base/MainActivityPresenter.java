package com.dobranos.instories.presentation.base;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.dobranos.instories.domain.base.interactor.StreamProcessor;
import com.dobranos.instories.domain.base.repository.MemoryRepository;
import com.dobranos.instories.domain.base.resolver.ResolutionResolver;
import com.dobranos.instories.domain.base.resolver.renderer.TriImageRenderResolver;
import com.dobranos.instories.presentation.base.model.ImageFilePath;
import com.dobranos.instories.view.base.ui.App;

import org.jcodec.common.model.Size;

import javax.inject.Inject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;

public class MainActivityPresenter implements LifecycleObserver
{
    private static final int SELECT_REQUEST_CODE = 100;

    @Inject MemoryRepository memRepo;
    @Inject ResolutionResolver resResolver;

    private MainActivityView view;

    public MainActivityPresenter(MainActivityView view)
    {
        this.view = view;
        App.get(null).getAppComponent().inject(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate()
    {

    }

    public void setAvailableScreenSizePx(Point value)
    {
        resResolver.resolve(value);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (memRepo.containsId(requestCode - SELECT_REQUEST_CODE) && resultCode == RESULT_OK)
        {
            ClipData cp = data.getClipData();
            //cp.getItemAt(0).getUri()


            Uri uri = data.getData();
            try
            {
                Drawable content = null;
                if (uri.getPath().contains("/video/"))
                {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource((Activity) view, uri);
                    content = new BitmapDrawable(((Activity) view).getResources(), retriever.getFrameAtTime(0));
                }
                else if (uri.getPath().contains("/images/"))
                {
                    InputStream inputStream = ((Activity) view).getContentResolver().openInputStream(uri);
                    content = Drawable.createFromStream(inputStream, uri.toString());

                    ArrayList<File> fs = new ArrayList<>();
                    if (cp != null)
                        for (int i = 0; i < cp.getItemCount(); i++)
                            fs.add(new File(ImageFilePath.getPath((Activity)view, cp.getItemAt(i).getUri())));
                    else
                    {
                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
//                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
//                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
//                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
//                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
//                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
//                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
//                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
//                        fs.add(new File(ImageFilePath.getPath((Activity) view, uri)));
                    }

//                    ExecutorService es = Executors.newFixedThreadPool(1);
//                    es.execute(() ->
//                    {
//                        String appDirectoryName = "XYZ";
//                        File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_PICTURES), appDirectoryName);
//
//                        imageRoot.mkdirs();
//
//                        TriImageRenderResolver tir = new TriImageRenderResolver((Activity)view);
//                        tir.start();
//
////                        StreamProcessor p = new StreamProcessor(new File(imageRoot, "mda.mp4"), (Activity) view, new Size(1280, 720));
////                        p.start();
////
////                        try
////                        {
////                            Thread.sleep(15000);
////                        }
////                        catch (InterruptedException e) { }
////
////                        p.stop();
//                    });

                    //hardwareMakeVideo(fs, getGalleryPath(), "testo", 1280, 720, 2000000);
                }
                memRepo.addContent(requestCode - SELECT_REQUEST_CODE, content);
            }
            catch (FileNotFoundException e) { }
        }
    }

}