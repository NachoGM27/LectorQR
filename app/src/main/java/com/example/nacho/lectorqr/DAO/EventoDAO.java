package com.example.nacho.lectorqr.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nacho.lectorqr.Evento;

import java.util.ArrayList;

public class EventoDAO {

    SQLiteDatabase data;
    ArrayList<Evento> lista = new ArrayList<Evento>();
    Evento evento;
    Context context;
    String nombreDB = "DBEventos";
    String tabla = "create table if not exists evento(id integer primary key autoincrement not null, nombre text)";

    public EventoDAO(Context context){

        this.context = context;
        data = context.openOrCreateDatabase(nombreDB, Context.MODE_PRIVATE, null);
        data.execSQL(tabla);
    }

    public boolean insertar(Evento evento){

        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre", evento.getNombre());

        return (data.insert("evento", null, contenedor))>0;
    }

    public boolean eliminar(long id){
        return (data.delete("evento", "id="+id, null))>0;
    }

    public ArrayList<Evento> verTodos(){

        lista.clear();
        Cursor cursor = data.rawQuery("select * from evento", null);
        if(cursor != null && cursor.getCount() > 0){

            cursor.moveToFirst();
            do {
                lista.add(new Evento(cursor.getInt(0), cursor.getString(1)));
            }while(cursor.moveToNext());
        }

        return lista;
    }

    public Evento verUno(int id){

        Cursor cursor = data.rawQuery("select * from evento", null);
        cursor.moveToPosition(id);
        evento = new Evento(cursor.getInt(0), cursor.getString(1));

        return evento;
    }

}
