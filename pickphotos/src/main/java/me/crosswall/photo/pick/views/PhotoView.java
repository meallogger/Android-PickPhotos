package me.crosswall.photo.pick.views;


import java.util.ArrayList;

import me.crosswall.photo.pick.model.AlbumInfo;
import me.crosswall.photo.pick.model.ImageInfo;

/**
 * Created by yuweichen on 15/12/9.
 */
public interface PhotoView extends BaseView {
    /**
     * showAlbumView
     * @param albumInfos
     */
    void showAlbumView(ArrayList<AlbumInfo> albumInfos);

    void showPhotoView(ArrayList<ImageInfo> imageInfos);

}
