package ec.edu.ups.gestioninventarios.Vistas.Ticketing;

/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar el layout activity_ticketing_cqr.xml y sus componentes
 *  Esta clase incluye la barra de navegación y las opciones unicamente para ticketing
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Arrays;

import ec.edu.ups.gestioninventarios.LoginActivity;
import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesAccesorios;
import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesActivos;
import ec.edu.ups.gestioninventarios.R;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;
import ec.edu.ups.gestioninventarios.WebService.MetodosAccesorios;
import ec.edu.ups.gestioninventarios.WebService.MetodosActivos;

public class TicketingCQR extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias a los componentes de interfaz de usuario.
    private Intent myIntent;
    SurfaceView surfaceView;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int permisosCamara = 1001;

    String nick=TicketingPrincipal.nickUsuarioTicket;
    String correo=TicketingPrincipal.correoUsuarioTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing_cqr);
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

        abrirCamaraTickets();

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
        getMenuInflater().inflate(R.menu.ticketing_principal, menu);
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
        if (id == R.id.action_settings) {
            AutenticarUsuario fin=new AutenticarUsuario();
            fin.registrarLogs(nick,"Logout");
            finishAffinity();
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
            this.myIntent = new Intent(TicketingCQR.this, TicketingCQR.class);
            startActivity(myIntent);
        } else if (id == R.id.ticketing_inicio) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingCQR.this, TicketingPrincipal.class);
            myIntent.putExtra("OPERACION_TICK","INICIO");
            startActivity(myIntent);

        } else if (id == R.id.ticketing_general) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingCQR.this, TicketingDetalleGene.class);
            startActivity(myIntent);

        } else if (id == R.id.consultar_tickets) {
            this.myIntent = new Intent(TicketingCQR.this, TicketingConsulta.class);
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


    //Método para abrir la cámara y escanear el código QR
    public void abrirCamaraTickets() {
        //Instancia de la ventana de cámara
        surfaceView = (SurfaceView) findViewById(R.id.camaraTicketing);
        //Instancia del método de consulta para la cámara
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        //Instancia de el método para abrir la cámara posterior
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(2048, 1024)
                .setAutoFocusEnabled(true)
                .build();

        //Instancia de la vista previa de la cámara antes de escanear el código qr
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {


                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TicketingCQR.this,
                            new String[]{Manifest.permission.CAMERA}, permisosCamara);
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        //Instancia del método de la detección de código QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            Intent myIntent;

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> cqr=detections.getDetectedItems();
                //Método para obtener el texto del scanner
                if (cqr.size() !=0){
                    String elemento=cqr.valueAt(0).displayValue;
                    String parteIdentificacion=instanciaActivo(elemento);
                    if (parteIdentificacion.equals("ACT")){

                        MetodosActivos activos=new MetodosActivos();
                        MensajesActivos mensaje=activos.datosActivosCQR(elemento,nick);

                        if(mensaje.isOperacionExitosa()){
                            this.myIntent = new Intent(TicketingCQR.this,TicketingDetalleCQR.class);
                            myIntent.putExtra("ID_ACTIVO_TICKET", mensaje.getObjetoInventarios().getIdActivo());
                            myIntent.putExtra("ID_ACCEROSIO_TICKET", 0);
                            myIntent.putExtra("NOMBRE_LABORATORIO_TICKET",mensaje.getObjetoInventarios().getNombreLaboratorio());
                            myIntent.putExtra("NOMBRE_ACT_ACC_TICKET",mensaje.getObjetoInventarios().getNombreActivo());
                            myIntent.putExtra("MODELO_ACT_ACC_TICKET",mensaje.getObjetoInventarios().getModeloActivo());
                            myIntent.putExtra("SERIAL_ACT_ACC_TICKET", mensaje.getObjetoInventarios().getSerialActivo());
                            myIntent.putExtra("ESTADO_ACT_ACC_TICKET", mensaje.getObjetoInventarios().getEstadoActivo());
                            myIntent.putExtra("CQR_ACT_ACC_TICKET", mensaje.getObjetoInventarios().getCadenaCQR());
                            myIntent.putExtra("ID_CQR_ACT_ACC_TICKET", elemento);
                            startActivity(myIntent);

                        }else {
                            Log.e("NO VALIO","NO VALIO");
                            /*Toast toast =
                                    Toast.makeText(getApplicationContext(),
                                            "Se ha Producido un error", Toast.LENGTH_LONG);
                            toast.show();*/
                        }

                    }else if(parteIdentificacion.equals("ACC")){
                        MetodosAccesorios accesorios=new MetodosAccesorios();
                        MensajesAccesorios mensaje=accesorios.datosAccesoriosCQR(elemento,nick);

                        if(mensaje.isOperacionExitosa()){
                            this.myIntent = new Intent(TicketingCQR.this,TicketingDetalleCQR.class);
                            myIntent.putExtra("ID_ACCEROSIO_TICKET", mensaje.getObjetoInventarios().getIdAccesorio());
                            myIntent.putExtra("ID_ACTIVO_TICKET", 0);
                            myIntent.putExtra("NOMBRE_LABORATORIO_TICKET","");
                            myIntent.putExtra("NOMBRE_ACT_ACC_TICKET",mensaje.getObjetoInventarios().getNombreAccesorio());
                            myIntent.putExtra("MODELO_ACT_ACC_TICKET",mensaje.getObjetoInventarios().getModeloAccesorio());
                            myIntent.putExtra("SERIAL_ACT_ACC_TICKET", mensaje.getObjetoInventarios().getSerialAccesorio());
                            myIntent.putExtra("ESTADO_ACT_ACC_TICKET", mensaje.getObjetoInventarios().getEstadoAccesorio());
                            myIntent.putExtra("CQR_ACT_ACC_TICKET", mensaje.getObjetoInventarios().getCadenaAccesorioCQR());
                            myIntent.putExtra("ID_CQR_ACT_ACC_TICKET", elemento);
                            startActivity(myIntent);

                        }else {
                            /*Toast toast =
                                    Toast.makeText(getApplicationContext(),
                                            "Se ha Producido un error", Toast.LENGTH_LONG);
                            toast.show();*/
                        }

                    }else{
                        this.myIntent = new Intent(TicketingCQR.this, TicketingPrincipal.class);
                        myIntent.putExtra("OPERACION_TICK","NO_CQR");
                        startActivity(myIntent);
                    }
                }
            }
        });

    }

    //Método para obtener el valor del permiso de la cámara
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case permisosCamara: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public String instanciaActivo(String elemento){
        Log.e("elemento ",elemento);
        String[] parts = elemento.split("\\.");
        Log.e("CQR: ", Arrays.toString(parts));
        String parteIdentificacion = parts[1];
        Log.e("CQR: ", parteIdentificacion);
        return parteIdentificacion;
    }

}
