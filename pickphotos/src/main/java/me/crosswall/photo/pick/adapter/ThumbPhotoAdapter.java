package me.crosswall.photo.pick.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import java.util.ArrayList;

import me.crosswall.photo.pick.PickConfig;
import me.crosswall.photo.pick.model.ImageInfo;
import me.crosswall.photo.pick.widget.ThumbPhotoView;


/**
 * Created by yuweichen on 15/12/9.
 */
public class ThumbPhotoAdapter extends RecyclerView.Adapter<ThumbPhotoAdapter.ThumbHolder>{

    private Activity context;
    private ArrayList<ImageInfo> imageInfos = new ArrayList<>();
    private int width;
    private Toolbar toolbar;
    private ArrayList<String> selectedImages = new ArrayList<>();
    private int maxPickSize;
    private int pickMode;
    public ThumbPhotoAdapter(Activity context, int spanCount, int maxPickSize, int pickMode, Toolbar toolbar){
        this.context = context;
        this.width   = context.getResources().getDisplayMetrics().widthPixels / spanCount;
        this.maxPickSize = maxPickSize;
        this.pickMode = pickMode;
        this.toolbar  = toolbar;
        if(pickMode== PickConfig.MODE_SINGLE_PICK){
            toolbar.setTitle("选择图片");
        }else{
            setTitle(selectedImages.size());
        }

    }

    public void addData(ArrayList<ImageInfo> imageInfos){
        this.imageInfos.addAll(imageInfos);
        notifyDataSetChanged();
    }

    public void clearAdapter(){
        this.imageInfos.clear();
        notifyDataSetChanged();
    }

    @Override
    public ThumbHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThumbHolder(new ThumbPhotoView(context));
    }

    @Override
    public void onBindViewHolder(ThumbHolder holder, int position) {
        holder.setData(getItem(position),position);
    }

    @Override
    public int getItemCount() {
        return this.imageInfos.size();
    }

    public ArrayList<String> getSelectedImages(){
        return selectedImages;
    }

    public ImageInfo getItem(int position){
        return this.imageInfos.get(position);
    }


    class ThumbHolder extends RecyclerView.ViewHolder{

        public ThumbPhotoView thumbPhotoView;

        public ThumbHolder(View itemView) {
            super(itemView);
            thumbPhotoView = (ThumbPhotoView) itemView;
        }

        public void setData(final ImageInfo imageInfo,final int position){

            thumbPhotoView.setLayoutParams(new FrameLayout.LayoutParams(width,width));
            thumbPhotoView.loadData(imageInfo.folderPath,pickMode);

            if(selectedImages.contains(imageInfo.folderPath)){
                thumbPhotoView.showSelected(true);
            }else{
                thumbPhotoView.showSelected(false);
            }

            thumbPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(pickMode == PickConfig.MODE_SINGLE_PICK){
                        selectedImages.clear();
                        selectedImages.add(imageInfo.folderPath);
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST,selectedImages);
                        context.setResult(context.RESULT_OK,intent);
                        context.finish();
                    }else{
                        if(selectedImages.contains(imageInfo.folderPath)){
                            selectedImages.remove(imageInfo.folderPath);
                            thumbPhotoView.showSelected(false);
                        }else{
                            if(selectedImages.size()==maxPickSize){
                                return;
                            }else{
                                selectedImages.add(imageInfo.folderPath);
                                thumbPhotoView.showSelected(true);
                            }
                        }
                        setTitle(selectedImages.size());
                    }


                }
            });
        }
    }

    public void setTitle(int selectCount){
        toolbar.setTitle(selectCount + "/" +maxPickSize);
    }

}
