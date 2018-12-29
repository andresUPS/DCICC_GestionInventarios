package ec.edu.ups.gestioninventarios.WebService;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesTickets;
import ec.edu.ups.gestioninventarios.Modelos.Tickets;

/**
 * Created by Andres on 27/12/2018.
 */

public class MetodosTickets {
    String conexion=AutenticarUsuario.conexion;

    public MensajesTickets ingresarTicketActAcc(String nick,int idUsuario, int idActivo, int idAccesorio, String descripcion, String prioridad){
        //Método para el web service de registrar login
        String METODO_WS="Tickets/RegistrarTicket";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        AutenticarUsuario usuario=new AutenticarUsuario();
        MensajesTickets mensajesTicket=new MensajesTickets();
        int lab=0;
        try {
            String token=usuario.ObtenerTokenTransacciones(nick);
            Log.e("token ticket",token);
            url= new URL(conexion+METODO_WS);
            Log.e("URL ",conexion+METODO_WS);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization",token);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Log.e("SALIDA TICK; ", idUsuario +"- "+idActivo+"- "+idAccesorio+" -"+descripcion+"- "+prioridad);
            conn.connect();
            //Métodos para enviar Datos al Web Service
            JSONObject jsonObjectEnvio=new JSONObject();
            jsonObjectEnvio.put("IdUsuario",idUsuario);
            jsonObjectEnvio.put("IdLaboratorio",0);
            jsonObjectEnvio.put("IdDetalleActivo",idActivo);
            jsonObjectEnvio.put("IdAccesorio",idAccesorio);
            jsonObjectEnvio.put("DescripcionTicket",descripcion);
            jsonObjectEnvio.put("PrioridadTicket",prioridad);



            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(jsonObjectEnvio.toString());
            writer.flush();
            writer.close();

            Log.e("SALIDA TICK; ", String.valueOf(conn.getResponseCode()));
            Log.e("MEN TICK; ", String.valueOf(conn.getResponseMessage()));
            if(Integer.parseInt(String.valueOf(conn.getResponseCode()))== 200){
                //Métodos para leer los datos provenientes del Web Service
                BufferedReader in= new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputline;
                StringBuffer response=new StringBuffer();
                //String json="";
                while((inputline=in.readLine())!=null){
                    response.append(inputline);
                }
                json=response.toString();
                Log.e("JSON TICK",json);
                //Método para obtener los valores del web service
                JSONObject jsonObjectRetorno = null;
                jsonObjectRetorno=new JSONObject(json);
                mensajesTicket.setOperacionExitosa(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")));
                mensajesTicket.setMensajeError(jsonObjectRetorno.optString("mensajeError"));
                return mensajesTicket;
            }else{
                mensajesTicket.setOperacionExitosa(false);
                Log.e("MENSAJE LOG ; ","NO VALIOOO");
                return mensajesTicket;
            }

        } catch (Exception e) {
            conn.disconnect();
            e.printStackTrace();
            Log.e("ERROR 1:  ", e.getMessage());
            return null;
        }finally {
            conn.disconnect();
        }
    }

    public MensajesTickets ingresarTicketGeneral(String nick,int idUsuario, int idLab, String descripcion, String prioridad){
        //Método para el web service de registrar login
        String METODO_WS="Tickets/RegistrarTicket";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        AutenticarUsuario usuario=new AutenticarUsuario();
        MensajesTickets mensajesTicket=new MensajesTickets();
        try {
            String token=usuario.ObtenerTokenTransacciones(nick);
            Log.e("token ticket",token);
            url= new URL(conexion+METODO_WS);
            Log.e("URL ",conexion+METODO_WS);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization",token);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.connect();
            //Métodos para enviar Datos al Web Service
            JSONObject jsonObjectEnvio=new JSONObject();
            jsonObjectEnvio.put("IdUsuario",idUsuario);
            jsonObjectEnvio.put("IdLaboratorio",idLab);
            jsonObjectEnvio.put("IdDetalleActivo",0);
            jsonObjectEnvio.put("IdAccesorio",0);
            jsonObjectEnvio.put("DescripcionTicket",descripcion);
            jsonObjectEnvio.put("PrioridadTicket",prioridad);



            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(jsonObjectEnvio.toString());
            writer.flush();
            writer.close();

            Log.e("SALIDA TICK; ", String.valueOf(conn.getResponseCode()));
            Log.e("MEN TICK; ", String.valueOf(conn.getResponseMessage()));
            if(Integer.parseInt(String.valueOf(conn.getResponseCode()))== 200){
                //Métodos para leer los datos provenientes del Web Service
                BufferedReader in= new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputline;
                StringBuffer response=new StringBuffer();
                //String json="";
                while((inputline=in.readLine())!=null){
                    response.append(inputline);
                }
                json=response.toString();
                Log.e("JSON TICK",json);
                //Método para obtener los valores del web service
                JSONObject jsonObjectRetorno = null;
                jsonObjectRetorno=new JSONObject(json);
                mensajesTicket.setOperacionExitosa(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")));
                mensajesTicket.setMensajeError(jsonObjectRetorno.optString("mensajeError"));
                return mensajesTicket;
            }else{
                mensajesTicket.setOperacionExitosa(false);
                Log.e("MENSAJE LOG ; ","NO VALIOOO");
                return mensajesTicket;
            }

        } catch (Exception e) {
            conn.disconnect();
            e.printStackTrace();
            Log.e("ERROR 1:  ", e.getMessage());
            return null;
        }finally {
            conn.disconnect();
        }
    }


    public ArrayList<Tickets> ticketsReportados(int idUsuario, String nick){
        String METODO_WS="Tickets/ObtenerTicketsPorIdUsuario";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        AutenticarUsuario usuario=new AutenticarUsuario();

        try {

            String token=usuario.ObtenerTokenTransacciones(nick);
            url= new URL(conexion+METODO_WS);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization",token);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.connect();
            //Métodos para enviar Datos al Web Service
            JSONObject jsonObjectEnvio=new JSONObject();
            jsonObjectEnvio.put("IdUsuario",idUsuario);

            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            os.writeBytes(String.valueOf(idUsuario));
            os.flush();
            os.close();

            Log.e("SALIDA LABS; ", String.valueOf(conn.getResponseCode()));
            Log.e("MEN LABS; ", String.valueOf(conn.getResponseMessage()));
            if(Integer.parseInt(String.valueOf(conn.getResponseCode()))== 200){
                //Métodos para leer los datos provenientes del Web Service
                BufferedReader in= new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputline;
                StringBuffer response=new StringBuffer();
                //String json="";
                while((inputline=in.readLine())!=null){
                    response.append(inputline);
                }
                json=response.toString();

                //Método para obtener los valores del web service
                JSONObject jsonObjectRetorno = null;
                jsonObjectRetorno=new JSONObject(json);
                JSONArray jsonArr=null;

                jsonArr=new JSONArray(jsonObjectRetorno.optString("listaObjetoInventarios"));
                ArrayList<Tickets> ticket=new ArrayList<Tickets>();
                for(int i=0;i<jsonArr.length();i++){
                    //String valor=jsonArr.getString(i);
                    JSONObject jsonObject=jsonArr.getJSONObject(i);
                    Tickets ticketsUsuario=new Tickets();
                    ticketsUsuario.setIdTicket(Integer.parseInt(jsonObject.optString("idTicket")));
                    ticketsUsuario.setNombreUsuarioResponsable(jsonObject.optString("nombreUsuarioResponsable"));
                    ticketsUsuario.setEstadoTicket(jsonObject.optString("estadoTicket"));
                    ticketsUsuario.setFechaAperturaTicket(jsonObject.optString("fechaAperturaTicket").replace('T',' '));
                    ticketsUsuario.setFechaEnProcesoTicket(jsonObject.optString("fechaEnProcesoTicket").replace('T',' '));
                    ticketsUsuario.setFechaEnEsperaTicket(jsonObject.optString("fechaEnEsperaTicket").replace('T',' '));
                    ticketsUsuario.setFechaResueltoTicket(jsonObject.optString("fechaResueltoTicket").replace('T',' '));
                    ticketsUsuario.setDescripcionTicket(jsonObject.optString("descripcionTicket"));
                    ticketsUsuario.setComentarioEnProcesoTicket(jsonObject.optString("comentarioEnProcesoTicket"));
                    ticketsUsuario.setComentarioEnEsperaTicket(jsonObject.optString("comentarioEnEsperaTicket"));
                    ticketsUsuario.setComentarioResueltoTicket(jsonObject.optString("comentarioResueltoTicket"));
                    ticketsUsuario.setNombreLaboratorio(jsonObject.optString("nombreLaboratorio"));
                    ticketsUsuario.setNombreDetalleActivo(jsonObject.optString("nombreDetalleActivo"));
                    ticketsUsuario.setNombreAccesorio(jsonObject.optString("nombreAccesorio"));
                    ticket.add(ticketsUsuario);
                }
                Log.e("Lista",ticket.get(0).getNombreLaboratorio());
                return  ticket;
            }else{
                Log.e("VACIO ; ","NO VALIOOO");
                return null;
            }

        } catch (Exception e) {
            conn.disconnect();
            e.printStackTrace();
            Log.e("ERROR LABS:  ", e.getMessage());
            return null;
        }finally {
            conn.disconnect();
        }
    }


}
