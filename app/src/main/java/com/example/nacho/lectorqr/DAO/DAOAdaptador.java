package com.example.nacho.lectorqr.DAO;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nacho.lectorqr.Evento;
import com.example.nacho.lectorqr.R;

import java.util.ArrayList;

public class DAOAdaptador extends BaseAdapter {

    ArrayList<Evento> lista;
    EventoDAO dao;
    AlumnoDAO alumnoDAO;
    Evento evento;
    Activity activity;

    public DAOAdaptador(Activity activity, ArrayList<Evento> lista, EventoDAO dao, AlumnoDAO alumnoDAO){
        this.lista = lista;
        this.activity = activity;
        this.dao = dao;
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Evento getItem(int position) {
        evento = lista.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        evento = lista.get(position);
        return evento.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.event_item, null);
        }
        evento = lista.get(position);

        TextView nombre = v.findViewById(R.id.nombre);
        TextView participantes = v.findViewById(R.id.participantes);

        nombre.setText(evento.getNombre());
        participantes.setText(alumnoDAO.verTodos(evento.getId()).size() + " participantes");

        return v;
    }

}
