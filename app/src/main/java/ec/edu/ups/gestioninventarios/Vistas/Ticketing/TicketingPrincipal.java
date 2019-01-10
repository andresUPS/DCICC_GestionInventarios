package ec.edu.ups.gestioninventarios.Vistas.Ticketing;

/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar el layout activity_ticketing_principal.xml y sus componentes
 *  Esta clase incluye la barra de navegación y las opciones unicamente para ticketing
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ec.edu.ups.gestioninventarios.LoginActivity;
import ec.edu.ups.gestioninventarios.Modelos.Tickets;
import ec.edu.ups.gestioninventarios.R;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;
import ec.edu.ups.gestioninventarios.WebService.MetodosTickets;

public class TicketingPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias a los componentes de interfaz de usuario.
    private Intent myIntent;
    private Button Reportados,EnProceso,EnEspera,Resueltos;
    public static int idUsuarioTicket=0;
    public static int idRolTicket=0;
    public static String nickUsuarioTicket=null;
    public static String nombresUsuarioTicket=null;
    public static String correoUsuarioTicket=null;
    public static String rolUsuarioTicket=null;

    private String operacion;
    PieChart pieChart;
    public static int abiertos=0,enProceso=0,enEspera=0,resueltos=0;
    ArrayList<Tickets> ticketsUsuario=new ArrayList<Tickets>();
    MetodosTickets tickets=new MetodosTickets();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing_principal);
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

        Intent myIntentObtener = getIntent();
        operacion=myIntentObtener.getExtras().getString("OPERACION_TICK");
        Log.e("operacion tic:",operacion);
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
        TextView txtUsuario = (TextView)v.findViewById(R.id.UsuarioTickets);
        txtUsuario.setText(nickUsuarioTicket);
        TextView txtCorreo = (TextView)v.findViewById(R.id.CorreoTickets);
        txtCorreo.setText(correoUsuarioTicket);
        contarTickets();




        Log.e("Usuario ",nickUsuarioTicket);
        Log.e("Correo ",correoUsuarioTicket);

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
            fin.registrarLogs(nickUsuarioTicket,"Logout");
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
            this.myIntent = new Intent(TicketingPrincipal.this, TicketingCQR.class);
            startActivity(myIntent);
        } else if (id == R.id.ticketing_inicio) {


        } else if (id == R.id.ticketing_general) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingPrincipal.this, TicketingDetalleGene.class);
            startActivity(myIntent);

        }  else if (id == R.id.consultar_tickets) {
            this.myIntent = new Intent(TicketingPrincipal.this, TicketingConsulta.class);
            myIntent.putExtra("OPERACION_CONSULTA", "TOTALES");
            startActivity(myIntent);

        } else if (id == R.id.cerrar_sesion_tickets) {
            AutenticarUsuario fin=new AutenticarUsuario();
            fin.registrarLogs(nickUsuarioTicket,"Logout");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
            startActivity(intent); finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setearValoresSesion(Intent myIntentObtener){
        idUsuarioTicket=myIntentObtener.getExtras().getInt("ID_USUARIO_TICK");
        nickUsuarioTicket=myIntentObtener.getExtras().getString("NICK_USUARIO_TICK");
        nombresUsuarioTicket=myIntentObtener.getExtras().getString("NOMBRES_USUARIO_TICK");
        correoUsuarioTicket=myIntentObtener.getExtras().getString("CORREO_USUARIO_TICK");
        idRolTicket=myIntentObtener.getExtras().getInt("ID_ROL_TICK");
        rolUsuarioTicket=myIntentObtener.getExtras().getString("NOMBRE_ROL_TICK");
    }

    public void contarTickets(){
        ticketsUsuario=tickets.ticketsReportados(idUsuarioTicket,nickUsuarioTicket);

        if (ticketsUsuario !=null){
            if(ticketsUsuario.size() !=0){
                for (int i = 0; i < ticketsUsuario.size() ; i++) {
                    if(ticketsUsuario.get(i).getEstadoTicket().equals("ABIERTO")){
                        abiertos+=1;
                    }else if(ticketsUsuario.get(i).getEstadoTicket().equals("EN PROCESO")){
                        enProceso+=1;
                    }else if(ticketsUsuario.get(i).getEstadoTicket().equals("EN ESPERA")){
                        enEspera+=1;
                    }else{
                        resueltos+=1;
                    }
                }
            }
        }

        int [] valores=new int[]{abiertos,enProceso,enEspera,resueltos};
        Log.e("valor; ", String.valueOf(valores[0]));
        pieChart = (PieChart) findViewById(R.id.pieChartTickets);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();

        yvalues.add(new PieEntry(valores[0], "ABIERTOS", 0));
        yvalues.add(new PieEntry(valores[1], "EN PROCESO", 1));
        yvalues.add(new PieEntry(valores[2], "EN ESPERA", 2));
        yvalues.add(new PieEntry(valores[3], "DE BAJA", 3));

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
