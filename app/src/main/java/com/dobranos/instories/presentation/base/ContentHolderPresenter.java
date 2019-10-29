package com.dobranos.instories.presentation.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.dobranos.instories.domain.base.repository.MemoryRepository;
import com.dobranos.instories.view.base.ui.App;

import javax.inject.Inject;

public class ContentHolderPresenter
{
    private static final int SELECT_REQUEST_CODE = 100;

    @Inject
    MemoryRepository memRepo;

    private ContentHolderView view;

//    @Inject
//    MainActivityContract.Presenter mainPresenter;

    private int id;
    public ContentHolderPresenter setId(int value) { id = value; return this; }

    public ContentHolderPresenter(ContentHolderView view)
    {
        this.view = view;
        App.get(null).getAppComponent().inject(this);
    }

    public void onBrowseContentClicked()
    {
        if(!memRepo.containsId(view.getHolderId()))
            memRepo.addContent(view.getHolderId(), null).observeForever(content ->
            {
                view.setContent(content);
            });

//        view.getActivity().startActivityForResult(
//            Intent.createChooser(new Intent().setType("video/*, image/*").setAction(Intent.ACTION_GET_CONTENT), "Выбери файл"),
//            view.getHolderId() + SELECT_REQUEST_CODE);

        final CharSequence[] options = {"Images", "Videos", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivity());
        builder.setTitle("Select From...");
        builder.setItems(options, (dialog, item) ->
        {
            if (options[item].equals("Images"))
            {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                view.getActivity().startActivityForResult(intent, view.getHolderId() + SELECT_REQUEST_CODE);
            }
            else if (options[item].equals("Videos"))
            {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                view.getActivity().startActivityForResult(intent, view.getHolderId() + SELECT_REQUEST_CODE);
            }
            else if (options[item].equals("Cancel"))
            {
                dialog.dismiss();
            }
            dialog.dismiss();
        });
        builder.show();
    }

    public void onCloseContentClicked()
    {
        memRepo.removeContent(view.getHolderId());
    }
}
