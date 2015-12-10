package me.crosswall.photo.pick.presenters;

import android.content.Context;

import java.util.ArrayList;

import me.crosswall.photo.pick.data.PhotoObserver;
import me.crosswall.photo.pick.model.AlbumInfo;
import me.crosswall.photo.pick.model.ImageInfo;
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
    public void initialized() {
        PhotoObserver.getAlbumInfoList(context).subscribe(safeSubscriber(albumSubcriber));
    }

    public void selectPhotoByCategory(long id){
        PhotoObserver.getImageInfoList(context,id).subscribe(safeSubscriber(photoSubcriber));
    }

    Subscriber<ArrayList<AlbumInfo>> albumSubcriber = new Subscriber<ArrayList<AlbumInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(ArrayList<AlbumInfo> albumInfos) {
           PhotoView photoView = getView();
           if(photoView!=null){
               photoView.showAlbumView(albumInfos);
               selectPhotoByCategory(0); //默认全部
           }
        }
    };


    Subscriber<ArrayList<ImageInfo>> photoSubcriber = new Subscriber<ArrayList<ImageInfo>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(ArrayList<ImageInfo> imageInfos) {
            PhotoView photoView = getView();
            if(photoView!=null){
                photoView.showPhotoView(imageInfos);
            }
        }
    };


}
