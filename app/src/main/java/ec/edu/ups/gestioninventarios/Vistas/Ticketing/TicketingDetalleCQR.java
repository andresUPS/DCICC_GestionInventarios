package ec.edu.ups.gestioninventarios.Vistas.Ticketing;

/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar el layout activity_ticketing_cqr.xml y sus componentes
 *  Esta clase incluye la barra de navegación y las opciones unicamente para ticketing
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ec.edu.ups.gestioninventarios.LoginActivity;
import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesTickets;
import ec.edu.ups.gestioninventarios.R;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;
import ec.edu.ups.gestioninventarios.WebService.MetodosTickets;

public class TicketingDetalleCQR extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias a los componentes de interfaz de usuario.
    private Intent myIntent;
    private ImageView imageView;
    private TextView idCqr;
    private EditText nombreAct,modeloAct,serialAct,labAct,estadoAct,descripcion;
    private Spinner prioridad;
    private String imagenActivoCQR,idImagenCqr;
    int idUsuarioActual=TicketingPrincipal.idUsuarioTicket;
    String nick=TicketingPrincipal.nickUsuarioTicket;
    String nombresUsuario=TicketingPrincipal.nombresUsuarioTicket;
    String correo=TicketingPrincipal.correoUsuarioTicket;
    private int idActivoActual,idAccesorioActual;

    MetodosTickets ticket=new MetodosTickets();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing_detalle_cqr);
        // Variable para instanciar la barra horizontal de opciones de navegación
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Variable para instanciar la barra horizontal de navegación
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_tick);
        navigationView.setNavigationItemSelectedListener(this);
        //Metodo para setear el Usuario y Correo en la pantalla
        View v = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView)v.findViewById(R.id.UsuarioTickets);
        txtUsuario.setText(nick);
        TextView txtCorreo = (TextView)v.findViewById(R.id.CorreoTickets);
        txtCorreo.setText(correo);

        //Métodos para instanciar los componentes de la vista
        nombreAct=(EditText) findViewById(R.id.TicketNombreActivoTxt);
        modeloAct=(EditText) findViewById(R.id.TicketModeloActivoTxt);
        serialAct=(EditText) findViewById(R.id.TicketSerialActivoTxt);
        labAct=(EditText) findViewById(R.id.TicketLabActivoTxt);
        estadoAct=(EditText) findViewById(R.id.TicketEstadoActivoTxt);
        descripcion=(EditText) findViewById(R.id.TicketDescripcionActivoTxt);

        imageView=(ImageView)findViewById(R.id.imagenCQRTicket);
        idCqr=(TextView)findViewById(R.id.idQRTicket);

        prioridad=(Spinner)findViewById(R.id.TicketPrioridadCmb);

        Intent myIntent = getIntent();
        imagenActivoCQR=myIntent.getExtras().getString("CQR_ACT_ACC_TICKET");
        idImagenCqr=myIntent.getExtras().getString("ID_CQR_ACT_ACC_TICKET");
        //Método para convertir la imagen del cqr
        byte [] encodebyte= Base64.decode(imagenActivoCQR,Base64.DEFAULT);
        Bitmap imagen= BitmapFactory.decodeByteArray(encodebyte,0,encodebyte.length);
        imageView.setImageBitmap(imagen);
        idCqr.setText(idImagenCqr);

        idActivoActual=myIntent.getExtras().getInt("ID_ACTIVO_TICKET");
        idAccesorioActual=myIntent.getExtras().getInt("ID_ACCEROSIO_TICKET");
        labAct.setText(myIntent.getExtras().getString("NOMBRE_LABORATORIO_TICKET"));
        nombreAct.setText(myIntent.getExtras().getString("NOMBRE_ACT_ACC_TICKET"));
        modeloAct.setText(myIntent.getExtras().getString("MODELO_ACT_ACC_TICKET"));
        serialAct.setText(myIntent.getExtras().getString("SERIAL_ACT_ACC_TICKET"));
        estadoAct.setText(myIntent.getExtras().getString("ESTADO_ACT_ACC_TICKET"));
        llenarSpinnerPrioridades();
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
        if (id == R.id.action_guardar_activo)  {
            String descripcionNuevo=descripcion.getText().toString();
            String prioridadNuevo=prioridad.getSelectedItem().toString();
            String nomActAcc=nombreAct.getText().toString();

            if(comprobarCampos()){

                MensajesTickets mensaje=
                        ticket.ingresarTicketActAcc(nick,idUsuarioActual,nombresUsuario,idActivoActual,idAccesorioActual,descripcionNuevo,prioridadNuevo,nomActAcc);

                if(mensaje.isOperacionExitosa()){
                    this.myIntent = new Intent(TicketingDetalleCQR.this, TicketingPrincipal.class);
                    myIntent.putExtra("OPERACION_TICK","ACTUALIZACION_ACTIVO");
                    startActivity(myIntent);
                    Toast toast =
                            Toast.makeText(getApplicationContext(),
                                    "Ticket reportado Exitosamente", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    this.myIntent = new Intent(TicketingDetalleCQR.this, TicketingPrincipal.class);
                    myIntent.putExtra("OPERACION_TICK","ACTUALIZACION_ACTIVO");
                    startActivity(myIntent);
                    Toast toast =
                            Toast.makeText(getApplicationContext(),
                                    "Error de registro", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }else if (id == R.id.action_cancelar_activo) {
            this.myIntent = new Intent(TicketingDetalleCQR.this, TicketingPrincipal.class);
            myIntent.putExtra("OPERACION_TICK","ACTUALIZACION_ACTIVO");
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

        if (id == R.id.ticketing_camera) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingDetalleCQR.this, TicketingCQR.class);
            startActivity(myIntent);
        } else if (id == R.id.ticketing_inicio) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingDetalleCQR.this, TicketingPrincipal.class);
            myIntent.putExtra("OPERACION_TICK","INICIO");
            startActivity(myIntent);

        }else if (id == R.id.ticketing_general) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingDetalleCQR.this, TicketingDetalleGene.class);
            startActivity(myIntent);

        } else if (id == R.id.consultar_tickets) {
            this.myIntent = new Intent(TicketingDetalleCQR.this, TicketingConsulta.class);
            myIntent.putExtra("OPERACION_CONSULTA", "TOTALES");
            startActivity(myIntent);

        } else if (id == R.id.cerrar_sesion_tickets) {
            AutenticarUsuario fin=new AutenticarUsuario();
            fin.registrarLogs(nick,"Logout");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
            startActivity(intent); finish();

        } /*else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void llenarSpinnerPrioridades(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Prioridad, android.R.layout.simple_spinner_dropdown_item);
        prioridad.setAdapter(adapter);
    }

    private boolean comprobarCampos() {
        // Reiniciar errores
        descripcion.setError(null);


        // Variables para obtener los valores de los cuadros de texto nick de usuario y contraseña.
        String descipcionAct = descripcion.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Método para verificar si el nombre de activo esta Vacio.
        if (TextUtils.isEmpty(descipcionAct)) {
            descripcion.setError(getString(R.string.error_field_required));
            focusView = descripcion;
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
}
