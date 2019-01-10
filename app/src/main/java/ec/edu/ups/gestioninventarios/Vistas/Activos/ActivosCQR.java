package ec.edu.ups.gestioninventarios.Vistas.Activos;

/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar el layout activity_activos_cqr.xml y sus componentes
 *  Esta clase incluye la barra de navegación y las opciones unicamente para gestión de activos
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

public class ActivosCQR extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias a los componentes de interfaz de usuario.
    private Intent myIntent;
    SurfaceView surfaceView;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int permisosCamara = 1001;
    String nick=ActivosPrincipal.nickUsuario;
    String correo=ActivosPrincipal.correoUsuario;
    private String palabra = "";
    private String palabraAnterior = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activos_cqr);
        // Variable para instanciar la barra horizontal de opciones de navegación
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Variable para instanciar la barra horizontal de navegación
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Metodo para setear el Usuario y Correo en la pantalla
        View v = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView)v.findViewById(R.id.NombreUsuarioLogin);
        txtUsuario.setText(nick);
        TextView txtCorreo = (TextView)v.findViewById(R.id.CorreoUsuarioLogin);
        txtCorreo.setText(correo);

        abrirCQR();
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
        getMenuInflater().inflate(R.menu.activos_principal, menu);
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

        if (id == R.id.cqr_camera) {
            this.myIntent = new Intent(ActivosCQR.this, ActivosCQR.class);
            startActivity(myIntent);
            // Handle the camera action
        }else if (id == R.id.cqr_inicio) {
            this.myIntent = new Intent(ActivosCQR.this, ActivosPrincipal.class);
            myIntent.putExtra("OPERACION","INICIO");
            startActivity(myIntent);

        } else if (id == R.id.cerrar_sesion) {
            AutenticarUsuario fin=new AutenticarUsuario();
            fin.registrarLogs(nick,"Logout");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
            startActivity(intent); finish();;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Método para abrir la cámara y escanear el código QR
    public void abrirCQR() {
        //Instancia de la ventana de cámara
        surfaceView = (SurfaceView) findViewById(R.id.camaraActivos);
        //Instancia del método de consulta para la cámara
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        //Instancia de el método para abrir la cámara posterior
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1024, 1024)
                .setAutoFocusEnabled(true)
                .build();
        
        //Instancia de la vista previa de la cámara antes de escanear el código qr
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //Método para comprobar si el usuario otorgo permisos de la cámara
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ActivosCQR.this,
                            new String[]{Manifest.permission.CAMERA}, permisosCamara);
                    return;
                }
                try {
                    //Instancia del inicio de la cámara
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
                    palabra=cqr.valueAt(0).displayValue;
                    Log.e("Pal ant ant ",palabraAnterior);
                    if(!palabra.equals(palabraAnterior)){
                        palabraAnterior=palabra;
                        Log.e("Pal ant ",palabraAnterior);
                        String parteIdentificacion=instanciaActivo(palabraAnterior);


                        if (parteIdentificacion.equals("ACT")){
                            String nick=ActivosPrincipal.nickUsuario;
                            MetodosActivos activos=new MetodosActivos();
                            MensajesActivos mensaje=activos.datosActivosCQR(palabraAnterior,nick);

                            if(mensaje.isOperacionExitosa()){
                                this.myIntent = new Intent(ActivosCQR.this,ActivosDetalle.class);
                                myIntent.putExtra("ID_ACTIVO", mensaje.getObjetoInventarios().getIdActivo());
                                myIntent.putExtra("ID_LABORATORIO", mensaje.getObjetoInventarios().getIdLaboratorio());
                                myIntent.putExtra("NOMBRE_LABORATORIO",mensaje.getObjetoInventarios().getNombreLaboratorio());
                                myIntent.putExtra("NOMBRE_MARCA",mensaje.getObjetoInventarios().getNombreMarca());
                                myIntent.putExtra("NOMBRE_ACTIVO",mensaje.getObjetoInventarios().getNombreActivo());
                                myIntent.putExtra("MODELO_ACTIVO",mensaje.getObjetoInventarios().getModeloActivo());
                                myIntent.putExtra("SERIAL_ACTIVO", mensaje.getObjetoInventarios().getSerialActivo());
                                myIntent.putExtra("FECHA_ACTIVO", mensaje.getObjetoInventarios().getFechaIngresoActivo());
                                myIntent.putExtra("CUSTODIO_ACTIVO", mensaje.getObjetoInventarios().getResponsableActivo());
                                myIntent.putExtra("ESTADO_ACTIVO", mensaje.getObjetoInventarios().getEstadoActivo());
                                myIntent.putExtra("CODIGOUPS_ACTIVO", mensaje.getObjetoInventarios().getCodigoUpsActivo());
                                myIntent.putExtra("CQR_ACTIVO", mensaje.getObjetoInventarios().getCadenaCQR());
                                myIntent.putExtra("ID_CQR_ACTIVO", palabraAnterior);
                                startActivity(myIntent);

                            }else {
                                this.myIntent = new Intent(ActivosCQR.this, ActivosPrincipal.class);
                                myIntent.putExtra("OPERACION","NO_CQR");
                                startActivity(myIntent);
                                /*Toast toast =
                                        Toast.makeText(getApplicationContext(),
                                                "Se ha Producido un error", Toast.LENGTH_LONG);
                                toast.show();*/
                            }

                        }else if(parteIdentificacion.equals("ACC")){
                            String nick=ActivosPrincipal.nickUsuario;
                            MetodosAccesorios accesorios=new MetodosAccesorios();
                            MensajesAccesorios mensaje=accesorios.datosAccesoriosCQR(palabraAnterior,nick);

                            if(mensaje.isOperacionExitosa()){
                                this.myIntent = new Intent(ActivosCQR.this,ActivosAccDetalle.class);
                                myIntent.putExtra("ID_ACCESORIO", mensaje.getObjetoInventarios().getIdAccesorio());
                                myIntent.putExtra("NOMBRE_TIPO_ACC",mensaje.getObjetoInventarios().getNombreTipoAccesorio());
                                myIntent.putExtra("NOMBRE_ACTIVO_ACC",mensaje.getObjetoInventarios().getNombreDetalleActivo());
                                myIntent.putExtra("NOMBRE_ACCESORIO",mensaje.getObjetoInventarios().getNombreAccesorio());
                                myIntent.putExtra("MODELO_ACCESORIO",mensaje.getObjetoInventarios().getModeloAccesorio());
                                myIntent.putExtra("SERIAL_ACCESORIO", mensaje.getObjetoInventarios().getSerialAccesorio());
                                myIntent.putExtra("ESTADO_ACCESORIO", mensaje.getObjetoInventarios().getEstadoAccesorio());
                                myIntent.putExtra("CQR_ACCESORIO", mensaje.getObjetoInventarios().getCadenaAccesorioCQR());
                                myIntent.putExtra("ID_CQR_ACCESORIO", palabraAnterior);
                                startActivity(myIntent);

                            }else {
                                this.myIntent = new Intent(ActivosCQR.this, ActivosPrincipal.class);
                                myIntent.putExtra("OPERACION","NO_CQR");
                                startActivity(myIntent);
                                /*Toast toast =
                                        Toast.makeText(getApplicationContext(),
                                                "Se ha Producido un error", Toast.LENGTH_LONG);
                                toast.show();*/
                            }

                        }else{
                            this.myIntent = new Intent(ActivosCQR.this, ActivosPrincipal.class);
                            myIntent.putExtra("OPERACION","NO_CQR");
                            startActivity(myIntent);
                        }

                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        // limpiamos el token
                                        palabraAnterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();

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
        String parteIdentificacion="";
        String[] parts = elemento.split("\\.");
        if (parts.length > 1){
            Log.e("CQR: ", Arrays.toString(parts));
            parteIdentificacion = parts[1];
            Log.e("CQR: ", parteIdentificacion);
        }

        return parteIdentificacion;
    }


}
