package com.madhavesh.gohel.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Context context;
    String[] items;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lstview);
        runtimepermission();
    }

        public void runtimepermission()
        {
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        display();
                        //    doStuff();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }


    public ArrayList<File> findsong(File  file)
    {
        ArrayList<File> arrayList =new ArrayList<>();

        File[] files = file.listFiles();

        for(File singlefile : files)
        {
            if(singlefile.isDirectory() )
            {
                arrayList.addAll(findsong(singlefile));
            }
            else
            {
                if(singlefile.getName().endsWith(".mp3") )
                {
                    arrayList.add(singlefile);
                }
            }

        }

        return  arrayList   ;
    }

    public  void  display()
    {
        final ArrayList<File> mySOng = findsong(getExternalStorageDirectory());

        items =new String[mySOng.size()];

        for(int i=0;i<mySOng.size();i++)
        {
            items[i] = mySOng.get(i).getName().toString().replace(".mp3","");

        }
        ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(this,R.layout.simple,R.id.txt,items);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


               String SongName = listView.getItemAtPosition(i).toString();

               startActivity(new Intent(getApplicationContext(),MusicPlayer.class)
                       .putExtra("songs",mySOng).putExtra("songName",SongName)
                       .putExtra("position",i));

            }
        });

    }


}