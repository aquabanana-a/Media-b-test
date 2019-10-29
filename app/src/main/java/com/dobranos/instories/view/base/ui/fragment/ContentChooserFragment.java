package com.dobranos.instories.view.base.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.dobranos.instories.R;
import com.dobranos.instories.domain.base.model.Resolution;
import com.dobranos.instories.presentation.base.ContentChooserFragmentPresenter;
import com.dobranos.instories.presentation.base.ContentChooserFragmentView;
import com.dobranos.instories.view.base.ui.widget.ContentHolderWidget;
import com.dobranos.instories.view.injection.ui.activity.MainActivityComponent;

import javax.inject.Inject;

public class ContentChooserFragment extends BaseFragment implements ContentChooserFragmentView
{
    @Inject
    ContentChooserFragmentPresenter presenter;

    private ViewGroup vgSurface;
    private ContentHolderWidget chvFirst;
    private ContentHolderWidget chvSecond;
    private ContentHolderWidget chvThird;

    private ImageView ivExport;

    //region setup injection

    @Override public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(MainActivityComponent.class).inject(this);

        getLifecycle().addObserver(presenter);
    }

    // endregion

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_content_chooser, container, false);

            this.vgSurface = rootView.findViewById(R.id.vg_surface);
            this.chvFirst = rootView.findViewById(R.id.chv_first);
            this.chvSecond = rootView.findViewById(R.id.chv_second);
            this.chvThird = rootView.findViewById(R.id.chv_third);
            this.ivExport = rootView.findViewById(R.id.iv_export);
        }
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.init(this);
    }

    @Override
    public void onDestroyView()
    {
        if (rootView.getParent() != null)
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        this.chvFirst.setHolderId(1);
        this.chvSecond.setHolderId(2);
        this.chvThird.setHolderId(3);

        this.ivExport.setOnClickListener(v ->
        {
            presenter.onExportClicked();
        });
    }

    public void setChooserSurfaceResolution(Resolution value)
    {
        vgSurface.getLayoutParams().height = (int)value.getSurfaceSizePx().y;
        vgSurface.getLayoutParams().width = (int)value.getSurfaceSizePx().x;
        vgSurface.requestLayout();

        chvFirst.getLayoutParams().height = (int)value.getBigHolderSizePx().y;
        chvFirst.getLayoutParams().width = (int)value.getBigHolderSizePx().x;
        chvFirst.requestLayout();

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)chvSecond.getLayoutParams();
        lp.height = (int)value.getSmallHolderSizePx().y;
        lp.width = (int)value.getSmallHolderSizePx().x;
        lp.setMargins(0, 0, 0, (int)value.getGapSizePx().y);
        chvSecond.setLayoutParams(lp);
        chvSecond.requestLayout();

        chvThird.getLayoutParams().height = (int)value.getSmallHolderSizePx().y;
        chvThird.getLayoutParams().width = (int)value.getSmallHolderSizePx().x;
        chvThird.requestLayout();
    }
}