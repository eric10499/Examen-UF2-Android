package com.example.cf17ericvisier.examenuf2android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ListaFragmentFragment.OnFragmentInteractionListener, AddIncidenciaFragment.OnFragmentInteractionListener {

    Toolbar toolbar;
    MediaPlayer mediaPlayer;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseStorage cloud = FirebaseStorage.getInstance();
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/jdamusica-1ed6a.appspot.com/o/Los%20Vengadores%20Bso%20Main%20Tittle.mp3?alt=media&token=a01c40d0-2a99-4193-95d8-592ad153be91");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            mediaPlayer.prepare(); // might take long! (for buffering, etc)

        } catch (IOException e) {

            e.printStackTrace();

        }

        mediaPlayer.start();

        FragmentManager fragmentManager = getSupportFragmentManager();
        ListaFragmentFragment listaFragmentFragment = new ListaFragmentFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, listaFragmentFragment, "listaFragment").commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pause:
                mediaPlayer.pause();
                return true;
            case R.id.add_incidencia:
                FragmentManager fragmentManager = getSupportFragmentManager();
                AddIncidenciaFragment addIncidenciaFragment = new AddIncidenciaFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, addIncidenciaFragment, "addFragment").commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setearEnFirebase(final String descripcion, final String aula) {
        final StorageReference filePath = cloud.getReference().child(uri.getLastPathSegment());
        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("url", uri.toString());
                Incidencias incidencias = new Incidencias(descripcion, aula, uri.toString());
                db.getInstance().getReference().child("Incidencias").push().setValue(incidencias);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

    }

    @Override
    public void cargar_galeria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 10);
    }

    @Override
    public void goToListaFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListaFragmentFragment listaFragmentFragment = new ListaFragmentFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, listaFragmentFragment, "listaFragment").commit();

    }

    @Override
    public void añadirIncidencia() {
        FirebaseDatabase.getInstance().getReference().child("Incidencias").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String urlImagen = dataSnapshot.child("urlImagen").getValue(String.class);
                String descripcion = dataSnapshot.child("descripcion").getValue(String.class);
                String aula = dataSnapshot.child("aula").getValue(String.class);



                ListaFragmentFragment listaFragment = (ListaFragmentFragment) getSupportFragmentManager().findFragmentByTag("listaFragment");

                listaFragment.addIncidenciaToList(new Incidencias(descripcion, aula, urlImagen));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;



        if(requestCode == 10 && resultCode == RESULT_OK) {

            uri = data.getData();

            //STORAGE SUBIR
            StorageReference filePath = cloud.getReference().child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Imagen subida a firebase Storage", Toast.LENGTH_SHORT).show();
                }
            });
            //


            try {



                bitmap = MediaStore.Images.Media

                        .getBitmap(getContentResolver(), uri);





            } catch (Exception e) {

                e.printStackTrace();

            }

        }





        if(bitmap != null){

            AddIncidenciaFragment fragment = (AddIncidenciaFragment) getSupportFragmentManager().findFragmentByTag("addFragment");

            fragment.setFoto(bitmap);



        }
    }
}
