#Android-PickPhotos

PickPhotos for Android Devices.It‘s a simple MVP demo. 

##GIF
![crosswall](https://github.com/crosswall/Android-PickPhotos/blob/master/art/pickphotos.gif)


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

