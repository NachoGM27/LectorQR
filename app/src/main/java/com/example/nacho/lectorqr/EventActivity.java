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
import android.view.KeyEvent;
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

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
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
    String linea2;
    String linea3;
    String linea4;

    private String text;


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

        if(evento.getCampoExtra1() != null && !evento.getCampoExtra1().equals("")){
            setLinea2(evento.getCampoExtra1() + ": ");
        }else{
            setLinea2("");
        }
        if(evento.getCampoExtra2() != null && !evento.getCampoExtra2().equals("")){
            setLinea3(evento.getCampoExtra2() + ": ");
        }else{
            setLinea3("");
        }
        if(evento.getCampoExtra3() != null && !evento.getCampoExtra3().equals("")){
            setLinea4(evento.getCampoExtra3() + ": ");
        }else{
            setLinea4("");
        }

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
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if(elementoSeleccionado){
            previousView.setBackgroundColor(Color.parseColor("#EAEAEA"));
            elementoSeleccionado = false;
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {

                    Bundle bundle = data.getExtras();

                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    recogerInfo(barcode);

                }
            }else
                Log.e("EventActivity:", String.format(getString(R.string.barcode_error_format) + CommonStatusCodes.getStatusCodeString(resultCode)));
        }else super.onActivityResult(requestCode, resultCode, data);
    }

    private void recogerInfo(Barcode barcode) {

        //Cogemos la url que nos da el codigo qr
        final String url = barcode.displayValue;

        new Thread(new Runnable() {
            @Override
            public void run() {

                Integer codigo = 0;
                final StringBuilder resultNombreUsuario = new StringBuilder();
                final StringBuilder resultCampo1 = new StringBuilder();
                final StringBuilder resultCampo2 = new StringBuilder();
                final StringBuilder resultCampo3 = new StringBuilder();

                //Comprobamos que nos podemos conectar a ella
                codigo = getStatusConnectionCode(url);

                if(codigo == 200){
                    Document doc = null;
                    try {
                        doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").timeout(100000).get();
                        Element nombreUsuario = doc.getElementById("usuario_valor");

                        if(nombreUsuario.val().trim() == ""){
                            resultNombreUsuario.append("Nombre de usuario vacio");
                        }else{
                            resultNombreUsuario.append(nombreUsuario.val());
                        }

                        Element campo1;
                        if(evento.getCampoExtra1() != null && !evento.getCampoExtra1().equals("")){
                            campo1 = doc.getElementById(evento.getCampoExtra1());
                            resultCampo1.append(campo1.val());
                        }else{
                            resultCampo1.append("");
                        }
                        Element campo2;
                        if(evento.getCampoExtra2() != null && !evento.getCampoExtra2().equals("")){
                            campo2 = doc.getElementById(evento.getCampoExtra2());
                            resultCampo2.append(campo2.val());
                        }else{
                            resultCampo2.append("");
                        }
                        Element campo3;
                        if(evento.getCampoExtra3() != null && !evento.getCampoExtra3().equals("")){
                            campo3 = doc.getElementById(evento.getCampoExtra3());
                            resultCampo3.append(campo3.val());
                        }else{
                            resultCampo3.append("");
                        }
                    } catch (IOException ex) {
                        Log.e("HTMLError", "Excepción al obtener el HTML de la página" + ex.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Dialog popUp = popUp(resultNombreUsuario.toString(), resultCampo1 != null ? resultCampo1.toString() : null, resultCampo2 != null ? resultCampo2.toString() : null, resultCampo3 != null ? resultCampo3.toString() : null);
                            popUp.setCanceledOnTouchOutside(false);
                            popUp.show();
                        }
                    });

                }else{
                    Log.e("codigoError:","Codigo de error:" + codigo);
                }

            }
        }).start();
    }

    private Integer getStatusConnectionCode(String url) {

        Connection conn = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        try {
            Response resp = conn.execute();
            if (resp.statusCode() != 200) {
                System.out.println("Error: "+resp.statusCode());
                return resp.statusCode();
            }else{
                System.out.println(Thread.currentThread().getName()+" is downloading "+ url);
                return resp.statusCode();
            }
        }catch(IOException e) {
            System.out.println(e.getStackTrace());
            System.out.println(Thread.currentThread().getName()+"No puedo conectar con  "+ url + e);
            System.out.println("No se puede conectar");
            return -1;
        }


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
                deletePopUp.setTitle("¿Seguro que quieres borrar este alumno de este evento?").setMessage("Alumno " + alumno.getNombre())
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
        menu.findItem(R.id.save).setVisible(false);
        if(elementoSeleccionado){
            menu.findItem(R.id.delete).setVisible(true);
        } else{
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public Dialog popUp(String nombre, String campo1, String campo2, String campo3){
            final Alumno alumno = new Alumno(nombre, campo1, campo2, campo3, evento.getId());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.pop_up_title)
                    .setMessage(linea1 + nombre + "\n" +
                                ((campo1 != null && !campo1.equals("")) ? (linea2 + campo1 + "\n") : "" + "\n") +
                                ((campo2 != null && !campo2.equals("")) ? (linea3 + campo2 + "\n") : "" + "\n") +
                                ((campo3 != null && !campo3.equals("")) ? (linea4 + campo3 + "\n") : "" + "\n"))
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

    public String getLinea2() {
        return linea2;
    }

    public void setLinea2(String linea2) {
        this.linea2 = linea2;
    }

    public String getLinea3() {
        return linea3;
    }

    public void setLinea3(String linea3) {
        this.linea3 = linea3;
    }

    public String getLinea4() {
        return linea4;
    }

    public void setLinea4(String linea4) {
        this.linea4 = linea4;
    }
}


