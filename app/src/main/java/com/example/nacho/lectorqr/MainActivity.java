package com.example.nacho.lectorqr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton newEvent = findViewById(R.id.newEvent);
        newEvent.setOnClickListener(new View.OnClickListener() {
    //----------------------DESDE AQUI ES MIO------------------------------------------------
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        /*Evento evento = (Evento) getIntent().getSerializableExtra("evento"); //NO FUNCIONA

        if (evento != null) {
            TextView textView = findViewById(R.id.text);
            textView.setText(evento.getLista().get(1).getNombre());
        }*/

        /*Button button = findViewById(R.id.boton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventActivity.class);
                intent.putExtra("evento", evento);
                startActivity(intent);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
