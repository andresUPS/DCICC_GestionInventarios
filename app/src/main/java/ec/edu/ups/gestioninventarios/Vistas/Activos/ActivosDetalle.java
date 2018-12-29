package ec.edu.ups.gestioninventarios.Vistas.Activos;

/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar el layout activity_activos_detalle.xml y sus componentes
 *  Esta clase incluye la barra de navegación y las opciones unicamente para gestión de activos
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ec.edu.ups.gestioninventarios.LoginActivity;
import ec.edu.ups.gestioninventarios.Modelos.Laboratorios;
import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesActivos;
import ec.edu.ups.gestioninventarios.R;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;
import ec.edu.ups.gestioninventarios.WebService.MetodosActivos;

public class ActivosDetalle extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias a los componentes de interfaz de usuario.
    private Intent myIntent;
    private ImageView imageView;
    private TextView idCqr,estado,laboratorio;
    private AutoCompleteTextView nombreActivo;
    private EditText modelo,serial,marca,fecha,codigoups,responsable;
    private Spinner estadosCmb,laboratoriosCmb;
    private String listaLaboratorios[]=null;
    ArrayList<Laboratorios> listaLabs;
    String nick=ActivosPrincipal.nickUsuario;
    String correo=ActivosPrincipal.correoUsuario;
    private int idActivoModificar;
    private String imagenActivoCQR;
    private String idImagenCqr;
    private boolean esDeBaja;
    private  String [] nombres;
    private String nombreActivoActual;
    MetodosActivos activo=new MetodosActivos();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activos_detalle);
        // Variable para instanciar la barra horizontal de opciones de navegación
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Variable para instanciar la barra horizontal de navegación
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Metodo para setear el Usuario y Correo en la pantalla
        View v = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView)v.findViewById(R.id.NombreUsuarioLogin);
        txtUsuario.setText(nick);
        TextView txtCorreo = (TextView)v.findViewById(R.id.CorreoUsuarioLogin);
        txtCorreo.setText(correo);

        //Seccion para obtener los campos de texto
        nombreActivo=(AutoCompleteTextView) findViewById(R.id.NombreActivoTxt);
        marca=(EditText) findViewById(R.id.MarcaActivoTxt);
        modelo=(EditText) findViewById(R.id.ModeloActivoTxt);
        serial=(EditText) findViewById(R.id.SerialActivoTxt);
        fecha=(EditText) findViewById(R.id.FechaAdquisicionActivoTxt);
        responsable=(EditText) findViewById(R.id.CustodioActivoTxt);
        codigoups=(EditText) findViewById(R.id.CodUPSActivoTxt);

        estado=(TextView) findViewById(R.id.EstadoActivoLbl);
        laboratorio=(TextView) findViewById(R.id.LaboratorioActivoLbl);

        imageView=(ImageView)findViewById(R.id.imagenCQRActivo);
        idCqr=(TextView)findViewById(R.id.idQRActivo);

        estadosCmb=(Spinner)findViewById(R.id.EstadoActivoCmb);
        laboratoriosCmb=(Spinner)findViewById(R.id.LaboratorioActivoCmb);

        Intent myIntent = getIntent();
        imagenActivoCQR=myIntent.getExtras().getString("CQR_ACTIVO");
        idImagenCqr=myIntent.getExtras().getString("ID_CQR_ACTIVO");
        //Método para convertir la imagen del cqr
        byte [] encodebyte= Base64.decode(imagenActivoCQR,Base64.DEFAULT);
        Bitmap imagen=BitmapFactory.decodeByteArray(encodebyte,0,encodebyte.length);
        imageView.setImageBitmap(imagen);
        idCqr.setText(idImagenCqr);

        idActivoModificar=myIntent.getExtras().getInt("ID_ACTIVO");
        nombreActivo.setText(myIntent.getExtras().getString("NOMBRE_ACTIVO"));
        nombreActivoActual=myIntent.getExtras().getString("NOMBRE_ACTIVO");
        marca.setText(myIntent.getExtras().getString("NOMBRE_MARCA"));
        modelo.setText(myIntent.getExtras().getString("MODELO_ACTIVO"));
        serial.setText(myIntent.getExtras().getString("SERIAL_ACTIVO"));
        fecha.setText(myIntent.getExtras().getString("FECHA_ACTIVO"));
        responsable.setText(myIntent.getExtras().getString("CUSTODIO_ACTIVO"));
        codigoups.setText(myIntent.getExtras().getString("CODIGOUPS_ACTIVO"));
        estado.setText(myIntent.getExtras().getString("ESTADO_ACTIVO"));
        laboratorio.setText(myIntent.getExtras().getString("NOMBRE_LABORATORIO"));
        String labActual=myIntent.getExtras().getString("NOMBRE_LABORATORIO");
        String EstadoActual=myIntent.getExtras().getString("ESTADO_ACTIVO");
        llenarSpinnerEstados(EstadoActual);
        llenarSpinnerLaboratorios(labActual);
        llenarAutocomplete();


    }

    /*
    *  Método para realizar la acción de despliegue de la barra de navegación lateral izquierda
    * */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    *  Método para instanciar las opciones en la barra de navegación lateral izquierda
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activos_detalle, menu);
        return true;
    }

    /*
    *  Método para realizar la acción del botón cerrar Sesión
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_guardar_activo) {
            String nombreNuevo=nombreActivo.getText().toString();
            String modeloNuevo=modelo.getText().toString();
            String serialNuevo=serial.getText().toString();
            String fechaNuevo=fecha.getText().toString();
            String codigoNuevo=codigoups.getText().toString();
            String estadoNuevo=estadosCmb.getSelectedItem().toString();

            int posFInal=laboratoriosCmb.getSelectedItemPosition();
            //Log.e("Posicion lab", String.valueOf(posFInal));
            int idLabFinal=listaLabs.get(posFInal).getIdLaboratorio();

            if(comprobarCampos()){
                Log.e("id lab", String.valueOf(idLabFinal));
                MensajesActivos mensaje=
                        activo.actualizarActivo(nick,idActivoModificar,idLabFinal,nombreNuevo,modeloNuevo,serialNuevo,fechaNuevo,codigoNuevo,estadoNuevo,esDeBaja);

                if(mensaje.isOperacionExitosa()){
                    this.myIntent = new Intent(ActivosDetalle.this, ActivosPrincipal.class);
                    myIntent.putExtra("OPERACION","ACTUALIZACION_ACTIVO");
                    startActivity(myIntent);
                    Toast toast =
                            Toast.makeText(getApplicationContext(),
                                    "Actualización de Activo Exitosa", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    this.myIntent = new Intent(ActivosDetalle.this, ActivosPrincipal.class);
                    myIntent.putExtra("OPERACION","ACTUALIZACION_ACTIVO");
                    startActivity(myIntent);
                    Toast toast =
                            Toast.makeText(getApplicationContext(),
                                    "Error de actualización", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

        } else if (id == R.id.action_cancelar_activo) {
            this.myIntent = new Intent(ActivosDetalle.this, ActivosPrincipal.class);
            myIntent.putExtra("OPERACION","ACTUALIZACION_ACTIVO");
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    *  Método para instanciar la acciones de las opciones de la barra de navegación lateral
    * */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.cqr_camera) {
            this.myIntent = new Intent(ActivosDetalle.this, ActivosCQR.class);
            startActivity(myIntent);
            // Handle the camera action
        }  else if (id == R.id.cqr_inicio) {
            this.myIntent = new Intent(ActivosDetalle.this, ActivosPrincipal.class);
            myIntent.putExtra("OPERACION","INICIO");
            startActivity(myIntent);

        } else if (id == R.id.cerrar_sesion) {
            //finishAffinity();
            AutenticarUsuario fin=new AutenticarUsuario();
            fin.registrarLogs(nick,"Logout");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
            startActivity(intent); finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void llenarSpinnerEstados(String EstadoActual){
        //Método para llenar el spinner de estados
        int posicionEstados=0;
        if(EstadoActual.equals("OPERATIVO")){
            posicionEstados=0;
        }else if(EstadoActual.equals("NO OPERATIVO")){
            posicionEstados=1;
        }else{
            posicionEstados=2;
        }

        if(EstadoActual.equals("DE BAJA")){
            esDeBaja=true;
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Estados_de_baja, android.R.layout.simple_spinner_dropdown_item);
            estadosCmb.setAdapter(adapter);
        }else{
            esDeBaja=false;
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Estados, android.R.layout.simple_spinner_dropdown_item);
            estadosCmb.setAdapter(adapter);
            estadosCmb.setSelection(posicionEstados);
        }
    }

    public void llenarSpinnerLaboratorios(String labActual){
        listaLabs=activo.laboratoriosHabilitados(nick);
        listaLaboratorios=new String[listaLabs.size()];
        int posicion=0;
        for (int i = 0; i < listaLabs.size(); i++) {
            if(labActual.equals(listaLabs.get(i).getNombreLaboratorio())){
                posicion=i;
            }
            listaLaboratorios[i]=listaLabs.get(i).getNombreLaboratorio();
        }
        ArrayAdapter spinnerArrayAdapterLabs = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, listaLaboratorios);
        laboratoriosCmb.setAdapter(spinnerArrayAdapterLabs);
        laboratoriosCmb.setSelection(posicion);
    }

    private boolean comprobarCampos() {
        // Reiniciar errores
        nombreActivo.setError(null);
        modelo.setError(null);
        serial.setError(null);

        // Variables para obtener los valores de los cuadros de texto nick de usuario y contraseña.
        String nombreAct = nombreActivo.getText().toString();
        String modeloAct = modelo.getText().toString();
        String serialAct = serial.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Método para verificar si el nombre de activo esta Vacio.
        if (TextUtils.isEmpty(nombreAct)) {
            nombreActivo.setError(getString(R.string.error_field_required));
            focusView = nombreActivo;
            cancel = true;
        }

        if(comprobarNombreRepetido()){
            nombreActivo.setText("");
            nombreActivo.setError("El nombre de Activo ya existe");
            focusView = nombreActivo;
            cancel = true;
        }
        // Método para verificar si el nick de usuario es menor a 16 caracteres.
        if (TextUtils.isEmpty(modeloAct)) {
            modelo.setText("N/A");
            modelo.setError(getString(R.string.error_field_required));
            focusView = modelo;
            cancel = true;
        }
        // Método para verificar si el nick de usuario es menor a 16 caracteres.
        if (TextUtils.isEmpty(serialAct)) {
            serial.setText("N/A");
            serial.setError(getString(R.string.error_field_required));
            focusView = serial;
            cancel = true;
        }

        if (cancel) {
            // Función para mostrar los mensajes de error en las cajas de texto
            focusView.requestFocus();
            return false;
        } else {
            //Método para mostrar el spinner de progreso en la consulta de usuarios
            return true;
        }
    }

    public void llenarAutocomplete(){
        nombres=activo.obtenerNombresActivos(nick);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,nombres);
        nombreActivo.setAdapter(adapter);
    }

    public boolean comprobarNombreRepetido(){
        boolean comprobar=false;
        String nombreActNuevo=nombreActivo.getText().toString();
        if(!nombreActivoActual.equals(nombreActNuevo)){
            for (int i = 0; i < nombres.length ; i++) {
                if(nombres[i].equals(nombreActNuevo)){
                    comprobar=true;
                }
            }
        }
        return comprobar;
    }


}

