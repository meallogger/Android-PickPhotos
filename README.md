#Android-PickPhotos

PickPhotos for Android Devices.It‘s a simple MVP demo. 

##GIF
<img src="https://github.com/crosswall/Android-PickPhotos/blob/master/art/pickphotos.gif" width="50%" height="50%">

##How to use.

####PickConfig

```code
  new PickConfig.Builder(this)
                .pickMode(PickConfig.MODE_MULTIP_PICK)
                .maxPickSize(30)
                .spanCount(3)
                .toolbarColor(R.color.colorPrimary)
                .build();
```
####Permission

```code
   <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
####AndroidManifest.xml
```code
   <activity android:name="me.crosswall.photo.pick.PickPhotosActiviy"
            android:screenOrientation="portrait"/>
```

####Receive Activity
```code
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
```

