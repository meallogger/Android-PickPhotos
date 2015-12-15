package me.crosswall.photo.pick.data;


import android.content.Context;
import java.util.List;

import me.crosswall.photo.pick.model.PhotoDirectory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuweichen on 15/12/8.
 */
public class PhotoObserver {

    public static Observable<List<PhotoDirectory>> getPhotos(final Context context){
        return Observable.create(new Observable.OnSubscribe<List<PhotoDirectory>>() {
            @Override
            public void call(Subscriber<? super List<PhotoDirectory>> subscriber) {
                List<PhotoDirectory> photos = PhotoData.getPhotos(context,true);
                subscriber.onNext(photos);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
