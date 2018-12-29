package ec.edu.ups.gestioninventarios.Controladores;

/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar los campos en el listview y mostrar información de tickets
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import ec.edu.ups.gestioninventarios.R;


public class ListViewAdapter extends BaseAdapter {
    // Declariación de Variables.
    Context context;
    String[] estados;
    String[] fechaAperturas;
    String[] responsables;
    String[] comentarios;
    LayoutInflater inflater;

    //Método para traer los datos de los tickets
    public ListViewAdapter(Context context,String[] estado, String[] fechaApertura, String[] responsable, String[] comentario) {
        this.context = context;
        this.estados = estado;
        this.fechaAperturas = fechaApertura;
        this.responsables = responsable;
        this.comentarios = comentario;
    }

    //Método para obtener la longitud del array
    @Override
    public int getCount() {
        return estados.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Declaración de Variables
        EditText FilaEstado;
        EditText  FilaFechaApertura;
        EditText  FilaResponsable;
        EditText  FilaComentario;
        LayoutInflater inflater;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listitem_ticketing_consulta, parent, false);

        // Localiza los TextViews en listitem_ticketing_consulta.xml
        FilaEstado = (EditText) itemView.findViewById(R.id.estado);
        FilaFechaApertura = (EditText) itemView.findViewById(R.id.fechaApertura);
        FilaResponsable = (EditText) itemView.findViewById(R.id.responsable);
        FilaComentario = (EditText) itemView.findViewById(R.id.comentario);

        // Captura la posición y agrega valores a los TextViews
        FilaEstado.setText(estados[position]);
        FilaFechaApertura.setText(fechaAperturas[position]);
        FilaResponsable.setText(responsables[position]);
        FilaComentario.setText(comentarios[position]);

       /* txtDetalle.setText(detalles[position]);
        imgImg.setImageResource(imagenes[position]);*/
        return itemView;
    }
}

