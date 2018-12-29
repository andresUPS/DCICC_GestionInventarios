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

import ec.edu.ups.gestioninventarios.Modelos.Activos;
import ec.edu.ups.gestioninventarios.Modelos.Laboratorios;
import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesActivos;


/**
 * Created by Andres on 24/12/2018.
 */

public class MetodosActivos {
    String conexion=AutenticarUsuario.conexion;

    public MensajesActivos datosActivosCQR(String codigo, String nick){

        String METODO_WS="Activos/ObtenerActivoPorCQR";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        AutenticarUsuario usuario=new AutenticarUsuario();
        MensajesActivos mensajesActivos=new MensajesActivos();
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
                //Método para setear el valor de operacion exitosa y el mensaje de error
                mensajesActivos.setOperacionExitosa(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")));
                mensajesActivos.setMensajeError(jsonObjectRetorno.optString("mensajeError"));
                if(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa"))){
                    //Método para llenar el modelo de Usuarios
                    Activos activo=new Activos();
                    JSONObject jsonActivo=new JSONObject(jsonObjectRetorno.optString("objetoInventarios"));
                    activo.setIdActivo(Integer.parseInt(jsonActivo.optString("idActivo")));
                    activo.setIdTipoActivo(Integer.parseInt(jsonActivo.optString("idTipoActivo")));
                    activo.setIdLaboratorio(Integer.parseInt(jsonActivo.optString("idLaboratorio")));
                    activo.setNombreActivo(jsonActivo.optString("nombreActivo"));
                    activo.setNombreCategoriaActivo(jsonActivo.optString("nombreCategoriaActivo"));
                    activo.setNombreLaboratorio(jsonActivo.optString("nombreLaboratorio"));
                    activo.setResponsableActivo(jsonActivo.optString("responsableActivo"));
                    activo.setNombreMarca(jsonActivo.optString("nombreMarca"));
                    activo.setModeloActivo(jsonActivo.optString("modeloActivo"));
                    activo.setSerialActivo(jsonActivo.optString("serialActivo"));
                    activo.setEstadoActivo(jsonActivo.optString("estadoActivo"));
                    String dateAsString = jsonActivo.optString("fechaIngresoActivo").substring(0,10);
                    activo.setFechaIngresoActivo(dateAsString);
                    activo.setCodigoUpsActivo(jsonActivo.optString("codigoUPSActivo"));
                    activo.setCadenaCQR(jsonActivo.optString("bytesCQR"));

                    mensajesActivos.setObjetoInventarios(activo);

                }else{
                    mensajesActivos.setObjetoInventarios(null);
                }
                return  mensajesActivos;
            }else{
                mensajesActivos.setOperacionExitosa(false);
                Log.e("VACIO ; ","NO VALIOOO");
                return mensajesActivos;
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


    public ArrayList<Laboratorios> laboratoriosHabilitados(String nick){
        String METODO_WS="Laboratorios/ObtenerLaboratoriosHab";
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
                ArrayList<Laboratorios> lab=new ArrayList<Laboratorios>();
                for(int i=0;i<jsonArr.length();i++){
                    //String valor=jsonArr.getString(i);
                    JSONObject jsonObject=jsonArr.getJSONObject(i);
                    Laboratorios nuevoLab=new Laboratorios();
                    nuevoLab.setIdLaboratorio(Integer.parseInt(jsonObject.optString("idLaboratorio")));
                    nuevoLab.setNombreLaboratorio(jsonObject.optString("nombreLaboratorio"));
                    lab.add(nuevoLab);
                }
                Log.e("Lista",lab.get(0).getNombreLaboratorio());
                return  lab;
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


    public MensajesActivos actualizarActivo(String nick,int idActivo,int idLab,String nombre,String modelo,String serial,String fecha,String codigoUps,String estado,boolean debaja){
        //Método para el web service de registrar login
        String METODO_WS="Activos/ActualizarActivoBasico";
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        URL url=null;
        HttpURLConnection conn=null;
        String json;
        AutenticarUsuario usuario=new AutenticarUsuario();
        MensajesActivos mensajesActivos=new MensajesActivos();
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
            jsonObjectEnvio.put("IdActivo",idActivo);
            jsonObjectEnvio.put("IdLaboratorio",idLab);
            jsonObjectEnvio.put("NombreActivo",nombre);
            jsonObjectEnvio.put("ModeloActivo",modelo);
            jsonObjectEnvio.put("SerialActivo",serial);
            jsonObjectEnvio.put("FechaIngresoActivo",fecha);
            jsonObjectEnvio.put("CodigoUpsActivo",codigoUps);
            jsonObjectEnvio.put("EstadoActivo",estado);
            jsonObjectEnvio.put("DeBaja",debaja);

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
                mensajesActivos.setOperacionExitosa(Boolean.parseBoolean(jsonObjectRetorno.optString("operacionExitosa")));
                mensajesActivos.setMensajeError(jsonObjectRetorno.optString("mensajeError"));
                return mensajesActivos;
            }else{
                mensajesActivos.setOperacionExitosa(false);
                Log.e("MENSAJE LOG ; ","NO VALIOOO");

                return mensajesActivos;
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

    public String [] obtenerNombresActivos(String nick){
        String METODO_WS="Activos/ObtenerActivosNombres";
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
                    nombresActivos[i]=jsonObject.optString("nombreActivo");
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
