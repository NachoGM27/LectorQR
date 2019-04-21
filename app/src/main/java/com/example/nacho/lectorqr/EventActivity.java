package com.example.nacho.lectorqr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.nacho.lectorqr.DAO.AlumnoDAO;
import com.example.nacho.lectorqr.DAO.DAOAlumnoAdaptador;
import com.example.nacho.lectorqr.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

public class EventActivity extends AppCompatActivity {

    private int BARCODE_READER_REQUEST_CODE = 1;

    private Evento evento;
    private Bundle datosRecogidos;
    private List<Alumno> lista;
    private AlumnoDAO dao;
    private DAOAlumnoAdaptador adaptadorDAO;

    ListView listView;

    String linea1 ="Nombre: ";
    String linea2 ="Apellidos: ";
    String linea3 ="DNI: ";
    String linea4 ="Expediente: ";
    String linea5 ="Titulación: ";


    private boolean elementoSeleccionado = false;
    private View previousView ;
    private int posicionSeleccionado ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Titulo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        datosRecogidos = getIntent().getExtras();
        evento = (Evento) datosRecogidos.getSerializable("evento");
        toolbar.setTitle(evento.getNombre());

        dao = new AlumnoDAO(this);
        lista = dao.verTodos(evento.getId());

        adaptadorDAO = new DAOAlumnoAdaptador(lista, this, dao);

        listView = findViewById(R.id.listView);
        listView.setAdapter(adaptadorDAO);
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


        ImageButton btn_scan_qrcode = findViewById(R.id.btn_scan_qrcode);
        btn_scan_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    String nombre = barcode.displayValue;
                    String apellidos = barcode.displayValue;
                    String dni = barcode.displayValue;
                    String expediente = barcode.displayValue;
                    String titulacion = barcode.displayValue;

                    Dialog popUp = popUp(nombre, apellidos, dni, expediente, titulacion);
                    popUp.setCanceledOnTouchOutside(false);
                    popUp.show();
                }
            }else
                Log.e("EventActivity:", String.format(getString(R.string.barcode_error_format) + CommonStatusCodes.getStatusCodeString(resultCode)));
        }else super.onActivityResult(requestCode, resultCode, data);
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
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.delete:
                final Alumno alumno = lista.get(posicionSeleccionado);
                elementoSeleccionado = false;
                previousView.setBackgroundColor(Color.parseColor("#EAEAEA"));

                AlertDialog.Builder deletePopUp = new AlertDialog.Builder(this);
                deletePopUp.setTitle("¿Seguro que quieres borrar este alumno de este evento?").setMessage("Alumno " + alumno.getNombre() + " " + alumno.getApellido() + " con dni " + alumno.getDni())
                        .setCancelable(false).setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.eliminar(alumno.getId());
                        lista=dao.verTodos(evento.getId());
                        adaptadorDAO.notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                deletePopUp.show();
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
        } else{
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public Dialog popUp(String nombre, String apellidos, String dni, String expediente, String titulacion){
            final Alumno alumno = new Alumno(nombre, apellidos, "99999999A", "9999", evento.getId());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.pop_up_title)
                    .setMessage(linea1 + nombre + "\n" + linea2 + apellidos  + "\n" + linea3 + dni + "\n" + linea4 + expediente + "\n" + linea5 + titulacion )
                    .setPositiveButton(R.string.guardar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dao.insertar(alumno);

                            lista = dao.verTodos(evento.getId());
                            adaptadorDAO.notifyDataSetChanged();

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
    }

}


