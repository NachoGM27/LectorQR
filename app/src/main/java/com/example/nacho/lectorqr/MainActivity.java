package com.example.nacho.lectorqr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nacho.lectorqr.DAO.AlumnoDAO;
import com.example.nacho.lectorqr.DAO.DAOAdaptador;
import com.example.nacho.lectorqr.DAO.EventoDAO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EventoDAO evento_dao;
    private AlumnoDAO alumno_dao;
    private Evento evento;
    private DAOAdaptador adaptador;
    private ArrayList<Evento> listaEventos;
    private boolean elementoSeleccionado = false;
    private View previousView ;
    private int posicionSeleccionado ;


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

        adaptador = new DAOAdaptador(this, listaEventos, evento_dao, alumno_dao);
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
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(previousView != null) {
                    previousView.setBackgroundColor(Color.parseColor("#EAEAEA"));
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

                final String[] name = new String[1];
                final EditText input = new EditText(v.getContext());

                input.setHint("Nombre del evento");

                AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
                dialogo.setTitle("Nuevo evento").setView(input);
                dialogo.setCancelable(false);

                dialogo.setPositiveButton(R.string.guardar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            name[0] = input.getText().toString();
                            evento = new Evento(name[0]);

                            evento_dao.insertar(evento);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:
                evento = listaEventos.get(posicionSeleccionado);
                elementoSeleccionado = false;
                previousView.setBackgroundColor(Color.parseColor("#EAEAEA"));

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
                previousView.setBackgroundColor(Color.parseColor("#EAEAEA"));

                List<Alumno> listaAlumnos = alumno_dao.verTodos(evento.getId());
                String content = "";
                for(Alumno aux: listaAlumnos){
                    content.concat(aux.getNombre() + ", ");
                    //alumno_dao.eliminar(aux.getId());
                }
               // saveText(evento.getNombre(), content);

                /*evento_dao.eliminar(evento.getId());
                listaEventos=evento_dao.verTodos();
                adaptador.notifyDataSetChanged();*/
                return true;
            case android.R.id.home:
                elementoSeleccionado = false;
                previousView.setBackgroundColor(Color.parseColor("#EAEAEA"));
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

    /*public void saveText(String nombreEvento, String content){
        String fileName = nombreEvento + ".txt";

        //crear el archivo
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

        //escribir el archivo
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(this, "Evento guardado", Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }*/

}
