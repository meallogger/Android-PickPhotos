package me.crosswall.photo.pick.presenters;

import android.content.Context;
import java.util.List;

import me.crosswall.photo.pick.data.PhotoObserver;
import me.crosswall.photo.pick.model.PhotoDirectory;
import me.crosswall.photo.pick.views.PhotoView;
import rx.Subscriber;


/**
 * Created by yuweichen on 15/12/9.
 */
public class PhotoPresenterImpl extends SafePresenter<PhotoView> {

    public Context context;
    public PhotoPresenterImpl(Context context, PhotoView photoView) {
        super(photoView);
        this.context = context;
    }

    @Override
    public void initialized(Object... objects) {
        boolean showGif = (boolean) objects[0];
        PhotoObserver.getPhotos(context,showGif).subscribe(safeSubscriber(albumSubcriber));
    }


    Subscriber<List<PhotoDirectory>> albumSubcriber = new Subscriber<List<PhotoDirectory>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            PhotoView photoView = getView();
            if(photoView!=null){
                photoView.showException(e.getMessage());
                //selectPhotoByCategory(0); //默认全部
            }
        }

        @Override
        public void onNext(List<PhotoDirectory> albumInfos) {
           PhotoView photoView = getView();
           if(photoView!=null){
               photoView.showAlbumView(albumInfos);
               //selectPhotoByCategory(0); //默认全部
           }
        }
    };

}
