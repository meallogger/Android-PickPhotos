package me.crosswall.photo.pick.data.normal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.crosswall.photo.pick.R;
import me.crosswall.photo.pick.model.PhotoDirectory;
import me.crosswall.photo.pick.util.BitmapUtil;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by yuweichen on 15/12/8.
 */
public class PhotoData {

    private static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
    };

    private final static String IMAGE_JPEG = "image/jpeg";
    private final static String IMAGE_PNG = "image/png";
    private final static String IMAGE_GIF = "image/gif";
    private final static String SORT = MediaStore.Images.Media.DATE_ADDED + " DESC";
    private final static Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


    public static List<PhotoDirectory> getPhotos(Context context, boolean checkImageStatus, boolean showGif) {

        ContentResolver resolver = context.getContentResolver();

        String selection = MIME_TYPE + "=? or " + MIME_TYPE + "=? " + (showGif ? ("or " + MIME_TYPE + "=?") : "");
        String selectionArgs[];
        if (showGif) {
            selectionArgs = new String[]{IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF};
        } else {
            selectionArgs = new String[]{IMAGE_JPEG, IMAGE_PNG};
        }


        Cursor data = resolver.query(IMAGE_URI, IMAGE_PROJECTION,
                selection, selectionArgs, SORT);

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
                if (!BitmapUtil.checkImgCorrupted(path)) {
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
        directories.add(0, photoDirectoryAll);

        return directories;
    }
}

