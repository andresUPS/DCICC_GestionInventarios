package ec.edu.ups.gestioninventarios.WebService;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesUsuarios;
import ec.edu.ups.gestioninventarios.Modelos.Usuarios;

/**
 * Created by Andres on 11/12/2018.
 */

public class AutenticarUsuario {

    //public static final String conexion="http://192.168.0.8/DCICC.WebServiceInventarios/";
    public static final String conexion="http://172.17.42.129/DCICC.WebServiceInventarios/";

    public MensajesUsuarios loginInicio(String nick,String password){
        String METODO_WS="AccesoServicio/AutenticarUsuario";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        MensajesUsuarios mensajesUsuarios = new MensajesUsuarios();
        try {
            url= new URL(conexion+METODO_WS);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(20000);
            conn.connect();
            //Métodos para enviar Datos al Web Service
            JSONObject jsonObjectEnvio=new JSONObject();
            jsonObjectEnvio.put("NickUsuario",nick);
            jsonObjectEnvio.put("PasswordUsuario",password);
            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(jsonObjectEnvio.toString());
            writer.flush();
            writer.close();

            Log.e("SALIDA; ", String.valueOf(conn.getResponseCode()));
            Log.e("MEN; ", String.valueOf(conn.getResponseMessage()));
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
                //Método para setear el valor de operacion exitosa y el mensaje de error
                mensajesUsuarios.setOperacionExitosa(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")));
                mensajesUsuarios.setMensajeError(jsonObjectRetorno.optString("mensajeError"));
                if(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")) && jsonObjectRetorno.optString("objetoInventarios").contains("idUsuario")){
                    //Método para llenar el modelo de Usuarios
                    Usuarios usuario=new Usuarios();
                    JSONObject jsonUsuario=new JSONObject(jsonObjectRetorno.optString("objetoInventarios"));
                    usuario.setIdUsuario(Integer.parseInt(jsonUsuario.optString("idUsuario")));
                    usuario.setNombreRol(jsonUsuario.optString("nombreRol"));
                    usuario.setIdRol(Integer.parseInt(jsonUsuario.optString("idRol")));
                    usuario.setNombresUsuario(jsonUsuario.optString("nombresUsuario"));
                    usuario.setNickUsuario(jsonUsuario.optString("nickUsuario"));
                    usuario.setPasswordUsuario(jsonUsuario.optString("passwordUsuario"));
                    usuario.setCorreoUsuario(jsonUsuario.optString("correoUsuario"));
                    usuario.setTelefonoUsuario(jsonUsuario.optString("telefonoUsuario"));
                    usuario.setTelefonoCelUsuario(jsonUsuario.optString("telefonoCelUsuario"));
                    usuario.setDireccionUsuario(jsonUsuario.optString("direccionUsuario"));
                    usuario.setHabilitadoUsuario(Boolean.parseBoolean(jsonUsuario.optString("habilitadoUsuario")));
                    mensajesUsuarios.setObjetoInventarios(usuario);

                }else{
                    mensajesUsuarios.setOperacionExitosa(false);
                    mensajesUsuarios.setObjetoInventarios(null);
                }
                return  mensajesUsuarios;
            }else{
                mensajesUsuarios.setOperacionExitosa(false);
                Log.e("VACIO ; ","NO VALIOOO");
                return mensajesUsuarios;
            }

        } catch (Exception e) {
            conn.disconnect();
            e.printStackTrace();
            mensajesUsuarios.setOperacionExitosa(false);
            Log.e("ERROR 1:  ", e.getMessage());
            return mensajesUsuarios;
        }
        finally {
            conn.disconnect();
        }
    }

    public String ObtenerTokenTransacciones(String nick){
        String METODO_WS="Token/ObtenerTokenTransacciones";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        try {
            url= new URL(conexion+METODO_WS);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            //Métodos para enviar Datos al Web Service
            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write("\""+nick+"\"");
            writer.flush();
            writer.close();

            Log.e("SALIDA; ", String.valueOf(conn.getResponseCode()));
            Log.e("MEN; ", String.valueOf(conn.getResponseMessage()));
            if(Integer.parseInt(String.valueOf(conn.getResponseCode()))== 200){
                //Métodos para leer los datos provenientes del Web Service
                BufferedReader in= new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputline;
                StringBuffer response=new StringBuffer();
                //String json="";
                while((inputline=in.readLine())!=null){
                    response.append(inputline);
                }
                json="Bearer "+response.toString();
                Log.e("OP ; ",json);

            }else{

                json=null;
            }
            conn.disconnect();
          return json;

        } catch (Exception e) {
            conn.disconnect();
            e.printStackTrace();
            Log.e("ERROR:  ", e.getMessage());
            return null;
        }
    }


    public String getLocalIpAddress() {
        String ip ="";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ip = (inetAddress.getHostAddress());
                        Log.e("ip",ip);

                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ip","no valio");
        }
        return ip;
    }

    public void registrarLogs(String nick,String Operacion){
        //Método para obtener la fecha actual
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentDateandTime = sdf.format(currentTime);
        Log.e("SALIDA LOG; ", nick+"+++"+Operacion);
        //Método para el web service de registrar login
        String METODO_WS="Logs/RegistrarLog";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;

        try {
            String token=ObtenerTokenTransacciones(nick);
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
            jsonObjectEnvio.put("IdUsuario",nick);
            jsonObjectEnvio.put("FechaLogs",currentDateandTime);
            jsonObjectEnvio.put("IpLogs",getLocalIpAddress());
            jsonObjectEnvio.put("OperacionLogs",Operacion);
            if (Operacion.equals("Login")){
                jsonObjectEnvio.put("TablaLogs","Acceso a base de datos.");
            }else{
                jsonObjectEnvio.put("TablaLogs","Finalización de sesión con la base de datos.");
            }

            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(jsonObjectEnvio.toString());
            writer.flush();
            writer.close();

            Log.e("SALIDA; ", String.valueOf(conn.getResponseCode()));
            Log.e("MEN; ", String.valueOf(conn.getResponseMessage()));
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
                Log.e("MENSAJE LOG ; ",jsonObjectRetorno.optString("operacionExitosa"));
                //Método para setear el valor de operacion exitosa y el mensaje de error

            }else{
                Log.e("MENSAJE LOG ; ","NO VALIOOO");
                //return null;
            }
            conn.disconnect();
        } catch (Exception e) {
            conn.disconnect();
            e.printStackTrace();
            Log.e("ERROR 1:  ", e.getMessage());
        }
    }


    public int [] datosPie(String nick){
        String METODO_WS="Dashboard/ObtenerDashboard";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        try {
            String token=ObtenerTokenTransacciones(nick);
            url= new URL(conexion+METODO_WS);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization",token);

            conn.setDoInput(true);
            conn.connect();

            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write("\""+nick+"\"");
            writer.flush();
            writer.close();

            Log.e("SALIDA; ", String.valueOf(conn.getResponseCode()));
            Log.e("MEN; ", String.valueOf(conn.getResponseMessage()));
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
                Log.e("datos tick",json);
                //Método para obtener los valores del web service
                JSONObject jsonObjectRetorno = null;
                jsonObjectRetorno=new JSONObject(json);
                //Método para setear el valor de operacion exitosa y el mensaje de error
                if(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa"))  && jsonObjectRetorno.optString("objetoInventarios").contains("activosOperativosCont")){
                    //Método para llenar el modelo de Usuarios
                    JSONObject jsonUsuario=new JSONObject(jsonObjectRetorno.optString("objetoInventarios"));
                    int [] datosTickets=
                            new int[]{Integer.parseInt(jsonUsuario.optString("activosOperativosCont")),
                                    Integer.parseInt(jsonUsuario.optString("activosNoOperativosCont")),
                                    Integer.parseInt(jsonUsuario.optString("activosDeBajaCont"))};;

                    Log.e("ticketsAbiertos", String.valueOf(datosTickets[2]));
                    return datosTickets;
                }else{
                    int [] datosTickets = new int[]{0,0,0};
                    return datosTickets;
                }

            }else{
                Log.e("VACIO ; ","NO VALIOOO");
                int [] datosTickets = new int[]{0,0,0};
                return datosTickets;
            }

        } catch (Exception e) {
            conn.disconnect();
            e.printStackTrace();
            Log.e("ERROR 1:  ", e.getMessage());
            int [] datosTickets = new int[]{0,0,0};
            return datosTickets;
        }finally {
            conn.disconnect();
        }
    }

    public boolean[] rolesActivosTickets(String nick,String nombreRol){
        String METODO_WS="Roles/ObtenerRolesPermisos";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        try {
            String token=ObtenerTokenTransacciones(nick);
            url= new URL(conexion+METODO_WS);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization",token);

            conn.setDoInput(true);
            conn.connect();

            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            os.writeBytes("\""+nombreRol+"\"");
            os.flush();
            os.close();

            Log.e("SALIDA; ", String.valueOf(conn.getResponseCode()));
            Log.e("MEN; ", String.valueOf(conn.getResponseMessage()));
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
                Log.e("datos roles",json);
                //Método para obtener los valores del web service
                JSONObject jsonObjectRetorno = null;
                jsonObjectRetorno=new JSONObject(json);
                //Método para setear el valor de operacion exitosa y el mensaje de error
                if(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")) && jsonObjectRetorno.optString("objetoInventarios").contains("permisoActivos")){
                    //Método para llenar el modelo de Usuarios
                    JSONObject jsonUsuario=new JSONObject(jsonObjectRetorno.optString("objetoInventarios"));
                    boolean [] datosRoles=
                            new boolean[]{Boolean.parseBoolean(jsonUsuario.optString("permisoActivos"))};;

                    Log.e("Permiso rol: ", String.valueOf(datosRoles[0]));
                    return datosRoles;
                }else{
                    boolean [] datosRoles = new boolean[]{false};
                    return datosRoles;
                }

            }else{
                Log.e("VACIO ; ","NO VALIOOO");
                boolean [] datosRoles = new boolean[]{false};
                return datosRoles;
            }

        } catch (Exception e) {
            conn.disconnect();
            e.printStackTrace();
            Log.e("ERROR 1:  ", e.getMessage());
            boolean [] datosRoles = new boolean[]{false};
            return datosRoles;
        }finally {
            conn.disconnect();
        }
    }


}
