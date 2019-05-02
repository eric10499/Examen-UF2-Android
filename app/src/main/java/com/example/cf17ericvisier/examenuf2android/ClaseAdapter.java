package com.example.cf17ericvisier.examenuf2android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClaseAdapter extends RecyclerView.Adapter<ClaseAdapter.ClaseViewHolder> implements View.OnClickListener {

    List<Incidencias> incidenciasList = new ArrayList<>();
    Context context;

    private View.OnClickListener listener;



    public ClaseAdapter(List<Incidencias> incidencias) {

        this.incidenciasList = incidencias;

    }




    @Override

    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View view = layoutInflater.inflate(R.layout.recycler_items, viewGroup, false);

        view.setOnClickListener(this);


        return new ClaseViewHolder(view);



    }



    @Override

    public void onBindViewHolder(@NonNull final ClaseViewHolder claseViewHolder, int i) {



        Incidencias m = incidenciasList.get(i);

        claseViewHolder.descripcion.setText(m.getDescripcion());

        claseViewHolder.aula.setText(m.getAula());

       claseViewHolder.cruz.animate().alpha(0).translationXBy(3000).setDuration((long) 0.1);
        Log.i("Imagen", m.getUrlImagen());
        MiHilo miHilo = new MiHilo(claseViewHolder.foto_incidencia, context);
        miHilo.execute(m.getUrlImagen());


        claseViewHolder.resuelto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(claseViewHolder.resuelto.isChecked()){
                    claseViewHolder.cruz.animate().alpha(1).translationXBy(-3000).setDuration(1000);
                    Log.i("check", "true");
                } else {
                    claseViewHolder.cruz.animate().alpha(0).translationXBy(3000);

                }
            }
        });


      //  claseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View v) {
               // claseViewHolder.cruz.animate().alpha(1).translationX(60).setDuration(2000);
           // }
       // });

    }



    @Override
    public int getItemCount() {

        return incidenciasList.size();

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }


    class ClaseViewHolder extends RecyclerView.ViewHolder{



        TextView descripcion, aula_label, aula;
        ImageView foto_incidencia, cruz;
        CheckBox resuelto;



        public ClaseViewHolder(@NonNull View itemView) {

            super(itemView);


            cruz = itemView.findViewById(R.id.cruz);
            descripcion = itemView.findViewById(R.id.descripcion_recycler);

            aula_label = itemView.findViewById(R.id.aula_label);

            aula = itemView.findViewById(R.id.aula);


            foto_incidencia = itemView.findViewById(R.id.imagen_incidencia);

            resuelto = itemView.findViewById(R.id.resolt);


        }

    }
}