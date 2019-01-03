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

import ec.edu.ups.gestioninventarios.Modelos.Accesorios;
import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesAccesorios;

/**
 * Created by Andres on 26/12/2018.
 */

public class MetodosAccesorios {
    String conexion=AutenticarUsuario.conexion;

    public MensajesAccesorios datosAccesoriosCQR(String codigo, String nick){

        String METODO_WS="Accesorios/ObtenerAccesorioPorCQR";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        AutenticarUsuario usuario=new AutenticarUsuario();
        MensajesAccesorios mensajesAccesorios=new MensajesAccesorios();
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

            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            os.writeBytes("\""+codigo+"\"");
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
                //Método para obtener los valores del web service
                JSONObject jsonObjectRetorno = null;
                jsonObjectRetorno=new JSONObject(json);
                Log.e("Estado Accesorio", jsonObjectRetorno.optString("operacionExitosa"));
                //Método para setear el valor de operacion exitosa y el mensaje de error
                mensajesAccesorios.setOperacionExitosa(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")));
                mensajesAccesorios.setMensajeError(jsonObjectRetorno.optString("mensajeError"));
                Log.e("Estado Accesorio", jsonObjectRetorno.optString("objetoInventarios"));

                if(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")) && jsonObjectRetorno.optString("objetoInventarios").contains("idAccesorio") ){
                    //Método para llenar el modelo de Usuarios
                    Accesorios accesorio=new Accesorios();
                    Log.e("Estado", "entro");
                    JSONObject jsonAccesorio=new JSONObject(jsonObjectRetorno.optString("objetoInventarios"));
                    accesorio.setIdAccesorio(Integer.parseInt(jsonAccesorio.optString("idAccesorio")));
                    accesorio.setNombreDetalleActivo(jsonAccesorio.optString("nombreDetalleActivo"));
                    accesorio.setNombreAccesorio(jsonAccesorio.optString("nombreAccesorio"));
                    accesorio.setNombreTipoAccesorio(jsonAccesorio.optString("nombreTipoAccesorio"));
                    accesorio.setModeloAccesorio(jsonAccesorio.optString("modeloAccesorio"));
                    accesorio.setSerialAccesorio(jsonAccesorio.optString("serialAccesorio"));
                    accesorio.setEstadoAccesorio(jsonAccesorio.optString("estadoAccesorio"));
                    accesorio.setCadenaAccesorioCQR(jsonAccesorio.optString("bytesCQR"));

                    mensajesAccesorios.setObjetoInventarios(accesorio);
                }else{
                    mensajesAccesorios.setOperacionExitosa(false);
                    mensajesAccesorios.setObjetoInventarios(null);
                }
                return  mensajesAccesorios;
            }else{
                mensajesAccesorios.setOperacionExitosa(false);
                Log.e("VACIO ; ","NO VALIOOO");
                return mensajesAccesorios;
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


    public MensajesAccesorios actualizarAccesorios(String nick, int idAccesorio, String nombre, String modelo, String serial, String estado,boolean debaja){
        //Método para el web service de registrar login
        String METODO_WS="Accesorios/ActualizarAccesorioBasico";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        AutenticarUsuario usuario=new AutenticarUsuario();
        MensajesAccesorios mensajesAccesorios=new MensajesAccesorios();
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
            jsonObjectEnvio.put("IdAccesorio",idAccesorio);
            jsonObjectEnvio.put("NombreAccesorio",nombre);
            jsonObjectEnvio.put("ModeloAccesorio",modelo);
            jsonObjectEnvio.put("SerialAccesorio",serial);
            jsonObjectEnvio.put("EstadoAccesorio",estado);
            jsonObjectEnvio.put("DeBaja",debaja);

            DataOutputStream os=new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(jsonObjectEnvio.toString());
            writer.flush();
            writer.close();

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
                mensajesAccesorios.setOperacionExitosa(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")));
                mensajesAccesorios.setMensajeError(jsonObjectRetorno.optString("mensajeError"));
                return mensajesAccesorios;
            }else{
                mensajesAccesorios.setOperacionExitosa(false);
                Log.e("MENSAJE LOG ; ","NO VALIOOO");

                return mensajesAccesorios;
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

    public String [] obtenerNombresAccesorios(String nick){
        String METODO_WS="Accesorios/ObtenerAccesoriosNombres";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        AutenticarUsuario usuario=new AutenticarUsuario();
        String[] listaLabs=new String[0];
        try {

            String token=usuario.ObtenerTokenTransacciones(nick);
            url= new URL(conexion+METODO_WS);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization",token);

            conn.setDoInput(true);
            conn.connect();

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
                String[] nombresActivos=new String[jsonArr.length()];
                for(int i=0;i<jsonArr.length();i++){
                    //String valor=jsonArr.getString(i);
                    JSONObject jsonObject=jsonArr.getJSONObject(i);
                    nombresActivos[i]=jsonObject.optString("nombreAccesorio");
                }
                Log.e("Lista",nombresActivos[0]);
                return  nombresActivos;
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
