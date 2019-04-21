package com.example.nacho.lectorqr.DAO;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nacho.lectorqr.Alumno;
import com.example.nacho.lectorqr.R;

import java.util.List;

public class DAOAlumnoAdaptador extends BaseAdapter {

    List<Alumno> lista;
    AlumnoDAO dao;
    Alumno alumno;
    Activity activity;

    public DAOAlumnoAdaptador(List<Alumno> lista, Activity activity, AlumnoDAO dao){
        this.lista = lista;
        this.activity = activity;
        this.dao = dao;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Alumno getItem(int position) {
        alumno = lista.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        alumno = lista.get(position);
        return alumno.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        View result = convertView;
        if(v==null){
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.row_item, null);
        }
        alumno = lista.get(position);
        TextView nombre = v.findViewById(R.id.nombre);
        TextView apellidos = v.findViewById(R.id.apellidos);
        TextView expediente = v.findViewById(R.id.expediente);
        TextView dni = v.findViewById(R.id.dni);

        nombre.setText(alumno.getNombre());
        apellidos.setText(alumno.getApellido());
        expediente.setText(alumno.getNumeroExpediente());
        dni.setText(alumno.getDni());

        return v;
    }
}
