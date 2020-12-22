package com.example.nacho.lectorqr;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nacho.lectorqr.DAO.AlumnoDAO;
import com.example.nacho.lectorqr.DAO.DAOEventoAdaptador;
import com.example.nacho.lectorqr.DAO.EventoDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EventoDAO evento_dao;
    private AlumnoDAO alumno_dao;
    private Evento evento;
    private DAOEventoAdaptador adaptador;
    private ArrayList<Evento> listaEventos;
    private boolean elementoSeleccionado = false;
    private View previousView ;
    private int posicionSeleccionado ;
    private long tiempoParaSalir;
    private Toast backToast;
    private String nombreEvento;
    private StringBuilder content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        evento_dao = new EventoDAO(this);
        alumno_dao = new AlumnoDAO(this);
        listaEventos = evento_dao.verTodos();

        adaptador = new DAOEventoAdaptador(this, listaEventos, evento_dao, alumno_dao);
        final ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                evento = listaEventos.get(position);

                Intent intent = new Intent(MainActivity.this, EventActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("evento", evento);
                intent.putExtras(bundle);

                startActivityForResult(intent, 0);
                finish();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(previousView != null) {
                    previousView.setBackgroundColor(Color.TRANSPARENT);
                }
                elementoSeleccionado = true;
                view.setBackgroundColor(Color.parseColor("#cecece"));
                previousView = view;
                posicionSeleccionado = position;
                return true;
            }
        });

        FloatingActionButton newEvent = findViewById(R.id.newEvent);
        newEvent.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(final View v) {

                final String[] name = new String[1]; //name es un array ya que al modificarse dentro de un eventListener necesita ser final y un String no se podria modificar
                final EditText input = new EditText(v.getContext());
                final TextView masCampos = new TextView(v.getContext());
                final EditText inputExtra1 = new EditText(v.getContext());
                final String[] campo1 = new String[1];
                final EditText inputExtra2 = new EditText(v.getContext());
                final String[] campo2 = new String[1];
                final EditText inputExtra3 = new EditText(v.getContext());
                final String[] campo3 = new String[1];

                Context context = v.getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50,10,50,10);

                input.setHint("Nombre del evento");
                input.setLayoutParams(params);
                layout.addView(input);
                masCampos.setTypeface(null, Typeface.BOLD);
                masCampos.setText("Id de otros campos a añadir al extraer los datos (No es necesario rellenarlos)");
                masCampos.setLayoutParams(params);
                layout.addView(masCampos);
                inputExtra1.setHint("Ejemplo: usuario_valor");
                inputExtra1.setLayoutParams(params);
                layout.addView(inputExtra1);
                inputExtra2.setHint("Ejemplo: usuario_valor");
                inputExtra2.setLayoutParams(params);
                layout.addView(inputExtra2);
                inputExtra3.setHint("Ejemplo: usuario_valor");
                inputExtra3.setLayoutParams(params);
                layout.addView(inputExtra3);


                AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                dialogo.setTitle("Nuevo evento").setView(layout);
                dialogo.setCancelable(false);

                dialogo.setPositiveButton(R.string.guardar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            name[0] = input.getText().toString();
                            evento = new Evento(name[0]);

                            campo1[0] = (inputExtra1.getText() != null && !inputExtra1.getText().toString().equals("")) ? inputExtra1.getText().toString() : null;
                            campo2[0] = (inputExtra2.getText() != null && !inputExtra1.getText().toString().equals("")) ? inputExtra2.getText().toString() : null;
                            campo3[0] = (inputExtra3.getText() != null && !inputExtra1.getText().toString().equals("")) ? inputExtra3.getText().toString() : null;

                            evento.setCampoExtra1(campo1[0]);
                            evento.setCampoExtra2(campo2[0]);
                            evento.setCampoExtra3(campo3[0]);

                            evento.setId(evento_dao.insertar(evento));

                            Intent intent = new Intent(MainActivity.this, EventActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("evento", evento);
                            intent.putExtras(bundle);

                            startActivityForResult(intent, 0);

                            dialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplication(), "ERROR", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialogo.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                dialogo.show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(previousView != null) {
            elementoSeleccionado = false;
            previousView.setBackgroundColor(Color.TRANSPARENT);
            previousView = null;
            posicionSeleccionado = -1;
        }else {
            if (tiempoParaSalir + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                MainActivity.this.finish();
                System.exit(0);
            } else {
                backToast = Toast.makeText(getBaseContext(), "Pulsa atrás otra vez para salir", Toast.LENGTH_SHORT);
                backToast.show();
            }

            tiempoParaSalir = System.currentTimeMillis();
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // no quiero que haga nada
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:
                evento = listaEventos.get(posicionSeleccionado);
                elementoSeleccionado = false;
                previousView.setBackgroundColor(Color.TRANSPARENT);
                previousView = null;
                posicionSeleccionado = -1;

                AlertDialog.Builder deletePopUp = new AlertDialog.Builder(this);
                deletePopUp.setTitle("¿Estás seguro de que quieres borrar este evento?")
                        .setCancelable(false).setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Alumno> listaAlumnos = alumno_dao.verTodos(evento.getId());
                        for (Alumno a: listaAlumnos){
                            alumno_dao.eliminar(a.getId());
                        }
                        evento_dao.eliminar(evento.getId());
                        listaEventos=evento_dao.verTodos();
                        adaptador.notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                deletePopUp.show();
                return true;
            case R.id.save:
                evento = listaEventos.get(posicionSeleccionado);
                elementoSeleccionado = false;
                previousView.setBackgroundColor(Color.TRANSPARENT);
                previousView = null;
                posicionSeleccionado = -1;

                List<Alumno> listaAlumnos = alumno_dao.verTodos(evento.getId());
                content = new StringBuilder();
                for(Alumno aux: listaAlumnos){
                    content.append(aux.getNombre() + "\n");
                }

                nombreEvento = evento.getNombre();
                Boolean guardado = saveText();

                if(guardado) {
                    for(Alumno aux: listaAlumnos){
                        alumno_dao.eliminar(aux.getId());
                    }
                    evento_dao.eliminar(evento.getId());
                    listaEventos = evento_dao.verTodos();
                    adaptador.notifyDataSetChanged();
                }
                return true;
            case android.R.id.home:
                elementoSeleccionado = false;
                previousView.setBackgroundColor(Color.TRANSPARENT);
                previousView = null;
                posicionSeleccionado = -1;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        invalidateOptionsMenu();
        if(elementoSeleccionado){
            menu.findItem(R.id.delete).setVisible(true);
            menu.findItem(R.id.save).setVisible(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else{
            menu.findItem(R.id.delete).setVisible(false);
            menu.findItem(R.id.save).setVisible(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean saveText(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

               return startDownloading();
            } else {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1000);

                return false;
            }
        }else{
            return startDownloading();
        }
    }

    private Boolean startDownloading() {
        String fileName = nombreEvento + ".txt";
        //crear el archivo
        File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

        //escribir el archivo
        try {
            if (!directorio.exists()) {
                directorio.mkdir();
            }
            File file = new File(directorio, fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.toString().getBytes());
            fos.close();

            DownloadManager downloadManager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
            downloadManager.addCompletedDownload(file.getName(), "Descargando archivo...", true, "text/plain", file.getAbsolutePath(),file.length(),true);

            Toast.makeText(this, "Evento guardado en " + directorio.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1000:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startDownloading();
                }else{
                    Toast.makeText(this, "Permiso denegado...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
