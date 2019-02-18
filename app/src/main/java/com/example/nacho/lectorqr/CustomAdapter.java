package com.example.nacho.lectorqr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Alumno>{

    private List<Alumno> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView nombre;
        TextView apellidos;
        TextView dni;
        TextView expediente;
    }

    public CustomAdapter(List<Alumno> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Alumno alumno = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.nombre = convertView.findViewById(R.id.nombre);
            viewHolder.apellidos = convertView.findViewById(R.id.apellidos);
            viewHolder.dni = convertView.findViewById(R.id.dni);
            viewHolder.expediente = convertView.findViewById(R.id.expediente);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.left_from_right : R.anim.no_anim);
        result.startAnimation(animation);
        Log.d("POSICION", "Posicion: " + position + " -- Ultima posicion: " + lastPosition);
        if (lastPosition < position) lastPosition = position;

        viewHolder.nombre.setText(alumno.getNombre());
        viewHolder.apellidos.setText(alumno.getApellido());
        viewHolder.dni.setText(alumno.getDni());
        viewHolder.expediente.setText(alumno.getNumeroExpediente());
        // Return the completed view to render on screen
        return convertView;
    }
}