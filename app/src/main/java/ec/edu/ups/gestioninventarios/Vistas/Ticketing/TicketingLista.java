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
import android.widget.Toast;

import java.util.ArrayList;

import ec.edu.ups.gestioninventarios.Controladores.ListViewAdapter;
import ec.edu.ups.gestioninventarios.LoginActivity;
import ec.edu.ups.gestioninventarios.Modelos.Tickets;
import ec.edu.ups.gestioninventarios.R;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;
import ec.edu.ups.gestioninventarios.WebService.MetodosTickets;

public class TicketingLista extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias a los componentes de interfaz de usuario.
    private Intent myIntent;
    int idUsuarioActual=TicketingPrincipal.idUsuarioTicket;
    String nick=TicketingPrincipal.nickUsuarioTicket;
    String correo=TicketingPrincipal.correoUsuarioTicket;
    ListViewAdapter adapter;
    private ListView lstCounts;
    private String operacionTicket;
    int valorConsulta;

    ArrayList<Tickets> ticketsUsuario=new ArrayList<Tickets>();
    MetodosTickets tickets=new MetodosTickets();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketing_lista);
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
        valorConsulta=myIntentObtener.getExtras().getInt("VALOR_CONSULTA");

        ticketsUsuario=tickets.ticketsReportados(idUsuarioActual,nick);

        if(ticketsUsuario==null || ticketsUsuario.size()==0){
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            "No existen tickets Reportados", Toast.LENGTH_LONG);
            toast.show();
        }else{
            if(operacionTicket.equals("ABIERTOS")){
                //int tamanoLista=ticketsUsuario.size();
                String[] estados=new String[valorConsulta];
                String[] fechaAperturas=new String[valorConsulta];
                String[] actLabAcc=new String[valorConsulta];
                String[] responsables=new String[valorConsulta];
                String[] comentarios=new String[valorConsulta];
                int cont=0;
                for (int i = 0; i < ticketsUsuario.size() ; i++) {
                    if(ticketsUsuario.get(i).getEstadoTicket().equals("ABIERTO")){
                        estados[cont]=ticketsUsuario.get(i).getEstadoTicket();
                        fechaAperturas[cont]=ticketsUsuario.get(i).getFechaAperturaTicket();
                        responsables[cont]="";
                        comentarios[cont]=ticketsUsuario.get(i).getDescripcionTicket();
                        if(ticketsUsuario.get(i).getIdLaboratorio()!=0){
                            actLabAcc[cont]="Laboratorio: "+ticketsUsuario.get(i).getNombreLaboratorio();
                        }else if(ticketsUsuario.get(i).getIdDetalleActivo()!=0){
                            actLabAcc[cont]="Activo: "+ticketsUsuario.get(i).getNombreDetalleActivo();
                        }else{
                            actLabAcc[cont]="Accesorio: "+ticketsUsuario.get(i).getNombreAccesorio();
                        }
                        cont++;
                    }


                }
                adapter=new ListViewAdapter(this,estados,fechaAperturas,responsables,comentarios,actLabAcc);
                lstCounts.setAdapter(adapter);
            } else if(operacionTicket.equals("ENPROCESO")){
                //int valorConsulta=ticketsUsuario.size();
                String[] estados=new String[valorConsulta];
                String[] fechaAperturas=new String[valorConsulta];
                String[] actLabAcc=new String[valorConsulta];
                String[] responsables=new String[valorConsulta];
                String[] comentarios=new String[valorConsulta];
                int cont=0;
                for (int i = 0; i < ticketsUsuario.size() ; i++) {
                   if(ticketsUsuario.get(i).getEstadoTicket().equals("EN PROCESO")){
                        estados[cont]=ticketsUsuario.get(i).getEstadoTicket();
                        fechaAperturas[cont]=ticketsUsuario.get(i).getFechaEnProcesoTicket();
                        responsables[cont]=ticketsUsuario.get(i).getNombreUsuarioResponsable();
                        comentarios[cont]=ticketsUsuario.get(i).getComentarioEnProcesoTicket();
                       if(ticketsUsuario.get(i).getIdLaboratorio()!=0){
                           actLabAcc[cont]="Laboratorio: "+ticketsUsuario.get(i).getNombreLaboratorio();
                       }else if(ticketsUsuario.get(i).getIdDetalleActivo()!=0){
                           actLabAcc[cont]="Activo: "+ticketsUsuario.get(i).getNombreDetalleActivo();
                       }else{
                           actLabAcc[cont]="Accesorio: "+ticketsUsuario.get(i).getNombreAccesorio();
                       }
                       cont++;
                    }




                }
                adapter=new ListViewAdapter(this,estados,fechaAperturas,responsables,comentarios,actLabAcc);
                lstCounts.setAdapter(adapter);
            }else if(operacionTicket.equals("ENESPERA")){
                //int valorConsulta=ticketsUsuario.size();
                String[] estados=new String[valorConsulta];
                String[] fechaAperturas=new String[valorConsulta];
                String[] actLabAcc=new String[valorConsulta];
                String[] responsables=new String[valorConsulta];
                String[] comentarios=new String[valorConsulta];
                int cont=0;
                for (int i = 0; i < ticketsUsuario.size() ; i++) {
                    if(ticketsUsuario.get(i).getEstadoTicket().equals("EN ESPERA")){
                        estados[cont]=ticketsUsuario.get(i).getEstadoTicket();
                        fechaAperturas[cont]=ticketsUsuario.get(i).getFechaEnEsperaTicket();
                        responsables[cont]=ticketsUsuario.get(i).getNombreUsuarioResponsable();
                        comentarios[cont]=ticketsUsuario.get(i).getComentarioEnEsperaTicket();
                        if(ticketsUsuario.get(i).getIdLaboratorio()!=0){
                            actLabAcc[cont]="Laboratorio: "+ticketsUsuario.get(i).getNombreLaboratorio();
                        }else if(ticketsUsuario.get(i).getIdDetalleActivo()!=0){
                            actLabAcc[cont]="Activo: "+ticketsUsuario.get(i).getNombreDetalleActivo();
                        }else{
                            actLabAcc[cont]="Accesorio: "+ticketsUsuario.get(i).getNombreAccesorio();
                        }
                        cont++;
                    }


                }
                adapter=new ListViewAdapter(this,estados,fechaAperturas,responsables,comentarios,actLabAcc);
                lstCounts.setAdapter(adapter);
            }
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
            this.myIntent = new Intent(TicketingLista.this, TicketingCQR.class);
            startActivity(myIntent);
        } else if (id == R.id.ticketing_inicio) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingLista.this, TicketingPrincipal.class);
            myIntent.putExtra("OPERACION_TICK","INICIO");
            startActivity(myIntent);

        }else if (id == R.id.ticketing_general) {
            //Instancia a la vista que se genera al presionar el botón
            this.myIntent = new Intent(TicketingLista.this, TicketingDetalleGene.class);
            startActivity(myIntent);

        } else if (id == R.id.consultar_tickets) {
            this.myIntent = new Intent(TicketingLista.this, TicketingConsulta.class);
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

