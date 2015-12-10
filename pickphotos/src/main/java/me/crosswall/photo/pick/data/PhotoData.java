package me.crosswall.photo.pick.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


import java.util.ArrayList;

import me.crosswall.photo.pick.model.AlbumInfo;
import me.crosswall.photo.pick.model.ImageInfo;

/**
 * Created by yuweichen on 15/12/8.
 */
public class PhotoData {

    public static ArrayList<AlbumInfo> getAlbumList(Context context) {

        ContentResolver resolver = context.getContentResolver();

        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID};
        String orderBy = MediaStore.Images.Media.BUCKET_ID;

        Cursor albumCursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, orderBy);

        int bucketColumnId = albumCursor
                .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

        int bucketColumn = albumCursor
                .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        long previouSid = 0;
        ArrayList<AlbumInfo> albumInfos = new ArrayList<>();
        AlbumInfo albumInfo = new AlbumInfo();
        albumInfo.bucketId = 0;
        albumInfo.bucketName = "所有图片";
        albumInfo.photoCount = 0;
        albumInfos.add(albumInfo);
        int photoCount = 0;
        while (albumCursor.moveToNext()) {

            photoCount++;
            if (albumCursor.isLast()) {
                albumInfos.get(0).photoCount = photoCount;
            }

            long bucketId = albumCursor.getInt(bucketColumnId);
            if (previouSid != bucketId) {
                AlbumInfo album = new AlbumInfo();
                album.bucketId = bucketId;
                album.bucketName = albumCursor.getString(bucketColumn);
                album.photoCount++;
                albumInfos.add(album);
                previouSid = bucketId;
            } else {
                if (albumInfos.size() > 0) {
                    albumInfos.get(albumInfos.size() - 1).photoCount++;
                }
            }

        }

        if (albumCursor != null) {
            albumCursor.close();
        }

        if (photoCount == 0) {
            albumInfos.clear();
        }

        if (albumInfos.size() > 0) {
            for (int i = 0; i < albumInfos.size(); i++) {
                String thumbPath = getMediaThumbnailPath(context, albumInfos.get(i).bucketId);
                albumInfos.get(i).thumbPath = thumbPath;
            }
        }

        return albumInfos;
    }

    public static String getMediaThumbnailPath(Context context, long id) {
        String path = "";
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketId = String.valueOf(id);
        String sort = MediaStore.Images.Thumbnails._ID + " DESC";
        String[] selectionArgs = {bucketId};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor;
        if (!bucketId.equals("0")) {
            cursor = context.getContentResolver().query(images, null,
                    selection, selectionArgs, sort);
        } else {
            cursor = context.getContentResolver().query(images, null,
                    null, null, sort);
        }


        if (cursor.moveToNext()) {
            selection = MediaStore.Images.Media._ID + " = ?";
            String photoID = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            selectionArgs = new String[]{photoID};

            images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
            Cursor pathCursor = context.getContentResolver().query(images, null,
                    selection, selectionArgs, sort);
            if (pathCursor != null && pathCursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            } else
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            pathCursor.close();
        }

        cursor.close();
        return path;
    }

    public static ArrayList<ImageInfo> getMediaThumbnailsPathByCategroy(Context context, long id) {
        String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        String bucketId = String.valueOf(id);
        String sort = MediaStore.Images.Media._ID + " DESC";
        String[] selectionArgs = {bucketId};

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor;
        if (!bucketId.equals("0")) {
            cursor = context.getContentResolver().query(images, null,
                    selection, selectionArgs, sort);
        } else {
            cursor = context.getContentResolver().query(images, null,
                    null, null, sort);
        }
        int pathColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

        ArrayList<ImageInfo> imageInfos = new ArrayList<>();

        while (cursor.moveToNext()) {
            String path = cursor.getString(pathColumn);
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.folderPath = path;
            imageInfo.selectOrder = -1;
            imageInfos.add(imageInfo);
        }

        cursor.close();

        return imageInfos;
    }


}
