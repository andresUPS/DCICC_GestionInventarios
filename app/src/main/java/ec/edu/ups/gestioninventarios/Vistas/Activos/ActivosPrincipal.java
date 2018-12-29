package ec.edu.ups.gestioninventarios.Vistas.Activos;

/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar el layout activity_activos_principal.xml y sus componentes
 *  Esta clase incluye la barra de navegación y las opciones unicamente para gestión de activos
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ec.edu.ups.gestioninventarios.LoginActivity;
import ec.edu.ups.gestioninventarios.R;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;


public class ActivosPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias a los componentes de interfaz de usuario.
    Intent myIntent;
    //TextView nombreUsuarioLogin;
    //TextView correoUsuarioLogin;
    public static int idUsuario=0;
    public static int idRol=0;
    public static String nickUsuario=null;
    public static String correoUsuario=null;
    public static String rolUsuario=null;
    private String operacion;
    PieChart pieChart;
    AutenticarUsuario fin=new AutenticarUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activos_principal);
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

        Intent myIntentObtener = getIntent();
        operacion=myIntentObtener.getExtras().getString("OPERACION");
        Log.e("operacion: ",operacion);
        if(operacion.equals("LOGIN")){
            setearValoresSesion(myIntentObtener);
        }else if(operacion.equals("NO_CQR")){
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            "El Código QR escaneado no se encuentra registrado ", Toast.LENGTH_LONG);
            toast.show();
        }

        //Metodo para setear el Usuario y Correo en la pantalla
        View v = navigationView.getHeaderView(0);
        TextView txtUsuario = (TextView)v.findViewById(R.id.NombreUsuarioLogin);
        txtUsuario.setText(nickUsuario);
        TextView txtCorreo = (TextView)v.findViewById(R.id.CorreoUsuarioLogin);
        txtCorreo.setText(correoUsuario);


        drawChart();

        Log.e("Usuario ",nickUsuario);
        Log.e("Correo ",correoUsuario);

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
            fin.registrarLogs(nickUsuario,"Logout");
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
            this.myIntent = new Intent(ActivosPrincipal.this, ActivosCQR.class);
            startActivity(myIntent);
            // Handle the camera action
        }else if (id == R.id.cqr_inicio) {
            //finishAffinity();

        } else if (id == R.id.cerrar_sesion) {
            //finishAffinity();
            fin.registrarLogs(nickUsuario,"Logout");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
            startActivity(intent); finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setearValoresSesion(Intent myIntentObtener){
        idUsuario=myIntentObtener.getExtras().getInt("ID_USUARIO");
        nickUsuario=myIntentObtener.getExtras().getString("NICK_USUARIO");
        correoUsuario=myIntentObtener.getExtras().getString("CORREO_USUARIO");
        idRol=myIntentObtener.getExtras().getInt("ID_ROL");
        rolUsuario=myIntentObtener.getExtras().getString("NOMBRE_ROL");

    }

    private void drawChart() {
        int [] valores=fin.datosPie(nickUsuario);
        Log.e("valor; ", String.valueOf(valores[0]));
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

        yvalues.add(new PieEntry(valores[0], "OPERATIVOS", 0));
        yvalues.add(new PieEntry(valores[1], "NO OPERATIVOS", 1));
        yvalues.add(new PieEntry(valores[2], "DE BAJA", 2));

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.DKGRAY);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.setData(data);
        pieChart.animateXY(1400, 1400);
    }

}
