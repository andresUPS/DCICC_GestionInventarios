package ec.edu.ups.gestioninventarios.Vistas.Ticketing;

/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar el layout activity_ticketing_consulta.xml y sus componentes
 *  Esta clase incluye la barra de navegación y las opciones unicamente para ticketing
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ec.edu.ups.gestioninventarios.Controladores.ListViewAdapter;
import ec.edu.ups.gestioninventarios.LoginActivity;
import ec.edu.ups.gestioninventarios.Modelos.Tickets;
import ec.edu.ups.gestioninventarios.R;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;
import ec.edu.ups.gestioninventarios.WebService.MetodosTickets;

public class TicketingConsulta extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias a los componentes de interfaz de usuario.
    private Intent myIntent;
    int idUsuarioActual=TicketingPrincipal.idUsuarioTicket;
    String nick=TicketingPrincipal.nickUsuarioTicket;
    String correo=TicketingPrincipal.correoUsuarioTicket;
    ListViewAdapter adapter;
    private ListView lstCounts;
    private String operacionTicket;

    ArrayList<Tickets> ticketsUsuario=new ArrayList<Tickets>();
    MetodosTickets tickets=new MetodosTickets();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing_consulta);
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

        lstCounts = (ListView)findViewById(R.id.ConsultaTicketLst);

        Intent myIntentObtener = getIntent();
        operacionTicket=myIntentObtener.getExtras().getString("OPERACION_CONSULTA");

        ticketsUsuario=tickets.ticketsReportados(idUsuarioActual,nick);

         if(operacionTicket.equals("TOTALES")){
            int tamanoLista=ticketsUsuario.size();
            String[] estados=new String[tamanoLista];
            String[] fechaAperturas=new String[tamanoLista];
            String[] responsables=new String[tamanoLista];
            String[] comentarios=new String[tamanoLista];
            for (int i = 0; i < ticketsUsuario.size() ; i++) {
                if(ticketsUsuario.get(i).getEstadoTicket().equals("ABIERTO")){
                    estados[i]=ticketsUsuario.get(i).getEstadoTicket();
                    fechaAperturas[i]=ticketsUsuario.get(i).getFechaAperturaTicket();
                    responsables[i]="";
                    comentarios[i]=ticketsUsuario.get(i).getDescripcionTicket();
                }else if(ticketsUsuario.get(i).getEstadoTicket().equals("EN PROCESO")){
                    estados[i]=ticketsUsuario.get(i).getEstadoTicket();
                    fechaAperturas[i]=ticketsUsuario.get(i).getFechaEnProcesoTicket();
                    responsables[i]=ticketsUsuario.get(i).getNombreUsuarioResponsable();
                    comentarios[i]=ticketsUsuario.get(i).getComentarioEnProcesoTicket();
                }else if(ticketsUsuario.get(i).getEstadoTicket().equals("EN ESPERA")){
                    estados[i]=ticketsUsuario.get(i).getEstadoTicket();
                    fechaAperturas[i]=ticketsUsuario.get(i).getFechaEnEsperaTicket();
                    responsables[i]=ticketsUsuario.get(i).getNombreUsuarioResponsable();
                    comentarios[i]=ticketsUsuario.get(i).getComentarioEnEsperaTicket();
                }else{
                    estados[i]=ticketsUsuario.get(i).getEstadoTicket();
                    fechaAperturas[i]=ticketsUsuario.get(i).getFechaResueltoTicket();
                    responsables[i]=ticketsUsuario.get(i).getNombreUsuarioResponsable();
                    comentarios[i]=ticketsUsuario.get(i).getComentarioResueltoTicket();
                }
            }
            adapter=new ListViewAdapter(this,estados,fechaAperturas,responsables,comentarios);
            lstCounts.setAdapter(adapter);
        }


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
            this.myIntent = new Intent(TicketingConsulta.this, TicketingCQR.class);
            startActivity(myIntent);
        } else if (id == R.id.ticketing_inicio) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingConsulta.this, TicketingPrincipal.class);
            myIntent.putExtra("OPERACION_TICK","INICIO");
            startActivity(myIntent);

        }else if (id == R.id.ticketing_general) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingConsulta.this, TicketingDetalleGene.class);
            startActivity(myIntent);

        } else if (id == R.id.consultar_tickets) {
            this.myIntent = new Intent(TicketingConsulta.this, TicketingConsulta.class);
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
}
