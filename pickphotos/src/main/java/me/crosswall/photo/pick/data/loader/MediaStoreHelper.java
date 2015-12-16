package me.crosswall.photo.pick.data.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import me.crosswall.photo.pick.PickConfig;
import me.crosswall.photo.pick.R;
import me.crosswall.photo.pick.model.PhotoDirectory;
import me.crosswall.photo.pick.util.BitmapUtil;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;


/**
 * see https://github.com/donglua/PhotoPicker/blob/master/PhotoPicker/src/main/java/me/iwf/photopicker/utils/MediaStoreHelper.java
 */
public class MediaStoreHelper {

    public final static int INDEX_ALL_PHOTOS = 0;


    public static void getPhotoDirs(AppCompatActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, args.getBoolean(PickConfig.EXTRA_CHECK_IMAGE), resultCallback));
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private PhotosResultCallback resultCallback;
        private boolean checkImageStatus;

        public PhotoDirLoaderCallbacks(Context context, boolean checkImageStatus, PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
            this.checkImageStatus = checkImageStatus;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context, args.getBoolean(PickConfig.EXTRA_SHOW_GIF, false));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;
            List<PhotoDirectory> directories = new ArrayList<>();
            PhotoDirectory photoDirectoryAll = new PhotoDirectory();
            photoDirectoryAll.setName(context.getString(R.string.all_photo));
            photoDirectoryAll.setId("ALL");

            while (data.moveToNext()) {

                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));

                if (checkImageStatus) {
                    if (!BitmapUtil.checkImgCorrupted(path)){
                        PhotoDirectory photoDirectory = new PhotoDirectory();
                        photoDirectory.setId(bucketId);
                        photoDirectory.setName(name);

                        if (!directories.contains(photoDirectory)) {
                            photoDirectory.setCoverPath(path);
                            photoDirectory.addPhoto(imageId, path);
                            photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                            directories.add(photoDirectory);
                        } else {
                            directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
                        }

                        photoDirectoryAll.addPhoto(imageId, path);
                    }
                } else {

                    PhotoDirectory photoDirectory = new PhotoDirectory();
                    photoDirectory.setId(bucketId);
                    photoDirectory.setName(name);

                    if (!directories.contains(photoDirectory)) {
                        photoDirectory.setCoverPath(path);
                        photoDirectory.addPhoto(imageId, path);
                        photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                        directories.add(photoDirectory);
                    } else {
                        directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
                    }

                    photoDirectoryAll.addPhoto(imageId, path);
                }

            }
            if (photoDirectoryAll.getPhotoPaths().size() > 0) {
                photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
            }
            directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }

}
