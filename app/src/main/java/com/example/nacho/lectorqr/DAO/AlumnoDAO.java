package com.example.nacho.lectorqr.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nacho.lectorqr.Alumno;

import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    SQLiteDatabase data;
    List<Alumno> lista = new ArrayList<Alumno>();
    Context context;
    String nombreDB = "DBAlumnos";
    String tabla = "create table if not exists alumno(id integer primary key autoincrement, nombre text, apellidos text, dni text, expediente text, idEvento integer)";

    public AlumnoDAO(Context context){
        this.context = context;
        data = context.openOrCreateDatabase(nombreDB, Context.MODE_PRIVATE, null);
        data.execSQL(tabla);
    }

    public boolean insertar(Alumno alumno){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", alumno.getNombre());
        contentValues.put("apellidos", alumno.getApellido());
        contentValues.put("dni", alumno.getDni());
        contentValues.put("expediente", alumno.getNumeroExpediente());
        contentValues.put("idEvento", alumno.getIdEvento());

        return (data.insert("alumno", null, contentValues))>0;
    }

    public boolean eliminar(long id){
        return (data.delete("alumno", "id=" + id, null))>0;
    }

    public List<Alumno> verTodos(long id){
        lista.clear();
        Cursor cursor = data.rawQuery("select * from alumno where idEvento='" + id + "'", null);
        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                lista.add(new Alumno(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getInt(5)));
            }while (cursor.moveToNext());
        }

        return lista;
    }

}
