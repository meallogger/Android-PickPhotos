package me.crosswall.photo.pick.views;

/**
 * Created by yuweichen on 15/12/10.
 */
public interface BaseView {
    /**
     * show loading message
     * @param msg
     */
    void showLoading(String msg);

    /**
     * hide loading
     */
    void hideLoading();

    /**
     * show error message
     */
    void showError(String msg);

    /**
     * show exception message
     */
    void showException(String msg);
}
