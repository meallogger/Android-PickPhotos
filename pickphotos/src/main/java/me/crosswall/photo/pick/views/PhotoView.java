package me.crosswall.photo.pick.views;
import java.util.List;
import me.crosswall.photo.pick.model.PhotoDirectory;

/**
 * Created by yuweichen on 15/12/9.
 */
public interface PhotoView extends BaseView {
    /**
     * showAlbumView
     * @param albumInfos
     */
    void showAlbumView(List<PhotoDirectory> albumInfos);

}
