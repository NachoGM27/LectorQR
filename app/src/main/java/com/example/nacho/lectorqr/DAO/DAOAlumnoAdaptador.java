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
        TextView campoExtra1 = v.findViewById(R.id.campoExtra1);
        TextView campoExtra2 = v.findViewById(R.id.campoExtra2);
        TextView campoExtra3 = v.findViewById(R.id.campoExtra3);

        nombre.setText(alumno.getNombre());
        campoExtra1.setText(alumno.getCampoExtra1());
        campoExtra2.setText(alumno.getCampoExtra2());
        campoExtra3.setText(alumno.getCampoExtra3());

        return v;
    }
}
