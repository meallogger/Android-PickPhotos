package me.crosswall.photo.pick.data;


import android.content.Context;


import java.util.ArrayList;

import me.crosswall.photo.pick.model.AlbumInfo;
import me.crosswall.photo.pick.model.ImageInfo;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuweichen on 15/12/8.
 */
public class PhotoObserver {

   public static Observable<ArrayList<AlbumInfo>> getAlbumInfoList(final Context context){
        return Observable.create(new Observable.OnSubscribe<ArrayList<AlbumInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<AlbumInfo>> subscriber) {
                ArrayList<AlbumInfo> albumInfos = PhotoData.getAlbumList(context);
                subscriber.onNext(albumInfos);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ArrayList<ImageInfo>> getImageInfoList(final Context context, final long id){
        return Observable.create(new Observable.OnSubscribe<ArrayList<ImageInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<ImageInfo>> subscriber) {
                ArrayList<ImageInfo> imageInfos = PhotoData.getMediaThumbnailsPathByCategroy(context,id);
                subscriber.onNext(imageInfos);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

}
