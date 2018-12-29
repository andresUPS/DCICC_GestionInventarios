package ec.edu.ups.gestioninventarios.Vistas.Activos;

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

import ec.edu.ups.gestioninventarios.LoginActivity;
import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesAccesorios;
import ec.edu.ups.gestioninventarios.R;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;
import ec.edu.ups.gestioninventarios.WebService.MetodosAccesorios;

public class ActivosAccDetalle extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Intent myIntent;
    private ImageView imageView;
    private AutoCompleteTextView nombreAcc;
    private EditText tipoAcc,nombreActivo,serial,modelo;
    private TextView estado,idCqr;
    private Spinner estadosAccCmb;
    String imagenActivoCQR,idImagenCqr;
    int idAccesorioModificar;
    MetodosAccesorios accesorios=new MetodosAccesorios();
    String nick=ActivosPrincipal.nickUsuario;
    String correo=ActivosPrincipal.correoUsuario;
    private boolean esDeBaja;
    private String [] nombres;
    private String nombreAccesorioActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activos_acc_detalle);

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
        nombreAcc=(AutoCompleteTextView) findViewById(R.id.NombreAccesorioTxt);
        tipoAcc=(EditText) findViewById(R.id.TipoAccesorioTxt);
        nombreActivo=(EditText) findViewById(R.id.ActivoAccesorioTxt);
        modelo=(EditText) findViewById(R.id.ModeloAccesorioTxt);
        serial=(EditText) findViewById(R.id.SerialAccesorioTxt);

        estado=(TextView) findViewById(R.id.EstadoAccesorioLbl);

        imageView=(ImageView)findViewById(R.id.imagenCQRAccesorio);
        idCqr=(TextView)findViewById(R.id.idQRAccesorio);

        estadosAccCmb=(Spinner)findViewById(R.id.EstadoAccesorioCmb);

        Intent myIntent = getIntent();
        imagenActivoCQR=myIntent.getExtras().getString("CQR_ACCESORIO");
        idImagenCqr=myIntent.getExtras().getString("ID_CQR_ACCESORIO");
        //Método para convertir la imagen del cqr
        byte [] encodebyte= Base64.decode(imagenActivoCQR,Base64.DEFAULT);
        Bitmap imagen= BitmapFactory.decodeByteArray(encodebyte,0,encodebyte.length);


        imageView.setImageBitmap(imagen);
        idCqr.setText(idImagenCqr);

        idAccesorioModificar=myIntent.getExtras().getInt("ID_ACCESORIO");
        nombreAcc.setText(myIntent.getExtras().getString("NOMBRE_ACCESORIO"));
        nombreAccesorioActual=myIntent.getExtras().getString("NOMBRE_ACCESORIO");
        tipoAcc.setText(myIntent.getExtras().getString("NOMBRE_TIPO_ACC"));
        nombreActivo.setText(myIntent.getExtras().getString("NOMBRE_ACTIVO_ACC"));
        nombreAcc.setText(myIntent.getExtras().getString("NOMBRE_ACCESORIO"));
        modelo.setText(myIntent.getExtras().getString("MODELO_ACCESORIO"));
        serial.setText(myIntent.getExtras().getString("SERIAL_ACCESORIO"));
        estado.setText(myIntent.getExtras().getString("ESTADO_ACCESORIO"));
        String EstadoActual=myIntent.getExtras().getString("ESTADO_ACCESORIO");
        llenarSpinnerEstados(EstadoActual);
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
            String nombreNuevo=nombreAcc.getText().toString();
            String modeloNuevo=modelo.getText().toString();
            String serialNuevo=serial.getText().toString();
            String estadoNuevo=estadosAccCmb.getSelectedItem().toString();
            if(comprobarCampos()){
                MensajesAccesorios mensaje=
                        accesorios.actualizarAccesorios(nick,idAccesorioModificar,nombreNuevo,modeloNuevo,serialNuevo,estadoNuevo,esDeBaja);

                if(mensaje.isOperacionExitosa()){
                    this.myIntent = new Intent(ActivosAccDetalle.this, ActivosPrincipal.class);
                    myIntent.putExtra("OPERACION","ACTUALIZACION_ACCESORIO");
                    startActivity(myIntent);
                    Toast toast =
                            Toast.makeText(getApplicationContext(),
                                    "Actualización de Accesorio Exitosa", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    this.myIntent = new Intent(ActivosAccDetalle.this, ActivosPrincipal.class);
                    myIntent.putExtra("OPERACION","ACTUALIZACION_ACCESORIO");
                    startActivity(myIntent);
                    Toast toast =
                            Toast.makeText(getApplicationContext(),
                                    "Error de actualización", Toast.LENGTH_LONG);
                    toast.show();
                }

            }



        } else if (id == R.id.action_cancelar_activo) {
            this.myIntent = new Intent(ActivosAccDetalle.this, ActivosPrincipal.class);
            myIntent.putExtra("OPERACION","ACTUALIZACION_ACCCESORIO");
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
            this.myIntent = new Intent(ActivosAccDetalle.this, ActivosCQR.class);
            startActivity(myIntent);
            // Handle the camera action
        } else if (id == R.id.cqr_inicio) {
            this.myIntent = new Intent(ActivosAccDetalle.this, ActivosPrincipal.class);
            myIntent.putExtra("OPERACION","INICIO");
            startActivity(myIntent);

        } else if (id == R.id.cerrar_sesion) {
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
            estadosAccCmb.setAdapter(adapter);
        }else{
            esDeBaja=false;
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Estados, android.R.layout.simple_spinner_dropdown_item);
            estadosAccCmb.setAdapter(adapter);
            estadosAccCmb.setSelection(posicionEstados);
        }
    }

    private boolean comprobarCampos() {
        // Reiniciar errores
        nombreAcc.setError(null);
        modelo.setError(null);
        serial.setError(null);

        // Variables para obtener los valores de los cuadros de texto nick de usuario y contraseña.
        String nombre = nombreAcc.getText().toString();
        String modeloAcc = modelo.getText().toString();
        String serialAcc = serial.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Método para verificar si el nombre de activo esta Vacio.
        if (TextUtils.isEmpty(nombre)) {
            nombreAcc.setError(getString(R.string.error_field_required));
            focusView = nombreAcc;
            cancel = true;
        }
        if(comprobarNombreRepetido()){
            nombreAcc.setText("");
            nombreAcc.setError("El nombre de Accesorio ya existe");
            focusView = nombreAcc;
            cancel = true;
        }
        // Método para verificar si el nick de usuario es menor a 16 caracteres.
        if (TextUtils.isEmpty(modeloAcc)) {
            modelo.setText("N/A");
            modelo.setError(getString(R.string.error_field_required));
            focusView = modelo;
            cancel = true;
        }
        // Método para verificar si el nick de usuario es menor a 16 caracteres.
        if (TextUtils.isEmpty(serialAcc)) {
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
        nombres=accesorios.obtenerNombresAccesorios(nick);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,nombres);
        nombreAcc.setAdapter(adapter);

    }

    public boolean comprobarNombreRepetido(){
        boolean comprobar=false;
        String nombreAccNuevo=nombreAcc.getText().toString();
        if(!nombreAccesorioActual.equals(nombreAccNuevo)){
            for (int i = 0; i < nombres.length ; i++) {
                if(nombres[i].equals(nombreAccNuevo)){
                    comprobar=true;
                }
            }
        }
        return comprobar;
    }



}
