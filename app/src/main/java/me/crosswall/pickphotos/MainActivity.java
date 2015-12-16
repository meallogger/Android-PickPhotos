package me.crosswall.pickphotos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import me.crosswall.photo.pick.PickConfig;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        imageAdapter = new ImageAdapter(this);
        recyclerView.setAdapter(imageAdapter);
    }

    public void testNormalPick(View view){
        new PickConfig.Builder(this)
                .pickMode(PickConfig.MODE_MULTIP_PICK)
                .maxPickSize(30)
                .spanCount(3)
                //.showGif(true)
                .checkImage(false) //default false
                .useCursorLoader(false) //default true
                .toolbarColor(R.color.colorPrimary)
                .build();
    }

    public void testLoaderPick(View view){
        new PickConfig.Builder(this)
                .pickMode(PickConfig.MODE_MULTIP_PICK)
                .maxPickSize(30)
                .spanCount(3)
                //.showGif(true)
                .checkImage(false) //default false
                .useCursorLoader(true) //default true
                .toolbarColor(R.color.colorPrimary)
                .build();
    }

    public void testNormalCheckPick(View view){
        new PickConfig.Builder(this)
                .pickMode(PickConfig.MODE_MULTIP_PICK)
                .maxPickSize(30)
                .spanCount(3)
                //.showGif(true)
                .checkImage(true) //default false
                .useCursorLoader(false) //default true
                .toolbarColor(R.color.colorPrimary)
                .build();
    }

    public void testLoaderCheckPick(View view){
        new PickConfig.Builder(this)
                .pickMode(PickConfig.MODE_MULTIP_PICK)
                .maxPickSize(30)
                .spanCount(3)
                //.showGif(true)
                .checkImage(true) //default false
                .useCursorLoader(true) //default true
                .toolbarColor(R.color.colorPrimary)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }

        if(requestCode==PickConfig.PICK_REQUEST_CODE){
            ArrayList<String> pick = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            Toast.makeText(this,"pick size:"+pick.size(),Toast.LENGTH_SHORT).show();
            imageAdapter.clearAdapter();
            imageAdapter.addData(pick);
        }
    }

}
