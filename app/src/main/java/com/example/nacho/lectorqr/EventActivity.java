package com.example.nacho.lectorqr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import android.widget.ListView;
import android.widget.TextView;

import com.example.nacho.lectorqr.barcode.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

public class EventActivity extends AppCompatActivity {

    private int BARCODE_READER_REQUEST_CODE = 1;

    private Evento evento;
    private List<Alumno> lista;
    private static CustomAdapter adapter;

    ListView listView;

    String linea1 ="Nombre: ";
    String linea2 ="Apellidos: ";
    String linea3 ="DNI: ";
    String linea4 ="Expediente: ";
    String linea5 ="Titulación: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Titulo");
        setSupportActionBar(toolbar);

        //-----------------DESDE AQUI ES MIO----------------------

        evento = new Evento();
        lista = evento.getLista();

        adapter = new CustomAdapter(lista, this);

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        evento.setNombre("Prueba");

        toolbar.setTitle(evento.getNombre());

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
        TextView tv_qr_code_text_content = findViewById(R.id.tv_qr_code_text_content);
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    tv_qr_code_text_content.setText(barcode.displayValue);

                    String nombre = barcode.displayValue;
                    String apellidos = barcode.displayValue;
                    String dni = barcode.displayValue;
                    String expediente = barcode.displayValue;
                    String titulacion = barcode.displayValue;

                    popUp( nombre, apellidos, dni, expediente, titulacion).show();
                } else
                    tv_qr_code_text_content.setText(R.string.no_barcode_captured);
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

    //---------------POPUP---------------------

    public Dialog popUp(String nombre, String apellidos, String dni, String expediente, String titulacion){
            final Alumno alumno = new Alumno();
            alumno.setNombre(nombre);
            alumno.setApellido(apellidos);
            alumno.setDni("99999999A");
            alumno.setNumeroExpediente("9999");
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.pop_up_title)
                    .setMessage(linea1 + nombre + "\n" + linea2 + apellidos  + "\n" + linea3 + dni + "\n" + linea4 + expediente + "\n" + linea5 + titulacion )
                    .setPositiveButton(R.string.guardar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            lista.add(alumno);
                            adapter.notifyDataSetChanged();

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


