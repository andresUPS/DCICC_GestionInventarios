package ec.edu.ups.gestioninventarios;


/**
 *          Aplicación para la Gestión de Activos y ticketing para Soporte Técnico
 *  Autores: Andres Chisaguano - Joel Ludeña
 *  Descripción: Clase para instanciar el layout activity_login.xml y sus componentes
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesUsuarios;
import ec.edu.ups.gestioninventarios.Vistas.Activos.ActivosPrincipal;
import ec.edu.ups.gestioninventarios.Vistas.Ticketing.TicketingPrincipal;
import ec.edu.ups.gestioninventarios.WebService.AutenticarUsuario;

public class LoginActivity extends AppCompatActivity {

    // Referencias a los componentes de interfaz de usuario.
    private AutoCompleteTextView nickUsuario;
    private EditText contrasenaUsuario;
    private View layoutProgreso;
    private View layoutLogin;
    private Intent myIntent;
    private String nombreRolActual;
    //ObtenerToken login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        /*Instrumentation.start(AgentConfiguration.builder()
                .withContext(getApplicationContext())
                .withAppKey("AD-AAB-AAN-HCK")
                .build());*/
        // Métodos para llamar almacenar los valores de los componentes de la interfaz
        nickUsuario = (AutoCompleteTextView) findViewById(R.id.email);
        contrasenaUsuario = (EditText) findViewById(R.id.password);
        //Metodo para comprobar la longitud de la contraseña
        contrasenaUsuario.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    comprobarCampos();
                    return true;
                }
                return false;
            }
        });
        //Método para realizar acciones al presionar el botón de login
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //LLamada al método de comprobación de nick de usuario y contraseña
                comprobarCampos();
            }
        });
        //Métodos para obtener las vistas de progreso y formulario de login
        layoutLogin = findViewById(R.id.login_form);
        layoutProgreso = findViewById(R.id.login_progress);
    }


    /**
     * Método para la validación del usuario y contraseña
     * Incluye el método para determinar el usuario y mostrar su respectiva vista
     */
    private void comprobarCampos() {
        // Reiniciar errores
        nickUsuario.setError(null);
        contrasenaUsuario.setError(null);

        // Variables para obtener los valores de los cuadros de texto nick de usuario y contraseña.
        String nick = nickUsuario.getText().toString();
        String password = contrasenaUsuario.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Método para verificar si la contraseña es mayor a 6 caracteres.
        if (!TextUtils.isEmpty(password) && !longitudPassword(password)) {
            contrasenaUsuario.setError(getString(R.string.error_invalid_password));
            focusView = contrasenaUsuario;
            cancel = true;
        }
        // Método para verificar si el nick de usuario es menor a 16 caracteres.
        if (TextUtils.isEmpty(nick)) {
            nickUsuario.setError(getString(R.string.error_field_required));
            focusView = nickUsuario;
            cancel = true;
        } else if (!longitudNick(nick)) {
            nickUsuario.setError(getString(R.string.error_invalid_email));
            focusView = nickUsuario;
            cancel = true;
        }

        if (cancel) {
            // Función para mostrar los mensajes de error en las cajas de texto
            focusView.requestFocus();
        } else {
            //Método para mostrar el spinner de progreso en la consulta de usuarios
            showProgress(true);
                AutenticarUsuario autenticacion=new AutenticarUsuario();
                MensajesUsuarios mensaje= autenticacion.loginInicio(nick,password);
                if(mensaje.isOperacionExitosa()){
                    autenticacion.registrarLogs(nick,"Login");
                    Log.e("ID=USUARIO", String.valueOf(mensaje.getObjetoInventarios().getIdUsuario()));
                    Log.e("NOM=USUARIO", mensaje.getObjetoInventarios().getNombreRol());

                    boolean[] valoresRol=autenticacion.rolesActivosTickets(nick,mensaje.getObjetoInventarios().getNombreRol());

                    if(mensaje.getObjetoInventarios().getNombreRol().equals("administrador")){
                        this.myIntent = new Intent(LoginActivity.this, ActivosPrincipal.class);
                        myIntent.putExtra("ID_USUARIO",mensaje.getObjetoInventarios().getIdUsuario());
                        myIntent.putExtra("NICK_USUARIO", nick);
                        myIntent.putExtra("CORREO_USUARIO",mensaje.getObjetoInventarios().getCorreoUsuario());
                        myIntent.putExtra("ID_ROL",mensaje.getObjetoInventarios().getIdRol());
                        myIntent.putExtra("NOMBRE_ROL",mensaje.getObjetoInventarios().getNombreRol());
                        myIntent.putExtra("OPERACION","LOGIN");
                        startActivity(myIntent);
                    }else if(mensaje.getObjetoInventarios().getNombreRol().equals("pasante")){
                        this.myIntent = new Intent(LoginActivity.this, ActivosPrincipal.class);
                        myIntent.putExtra("ID_USUARIO",mensaje.getObjetoInventarios().getIdUsuario());
                        myIntent.putExtra("NICK_USUARIO", nick);
                        myIntent.putExtra("CORREO_USUARIO",mensaje.getObjetoInventarios().getCorreoUsuario());
                        myIntent.putExtra("ID_ROL",mensaje.getObjetoInventarios().getIdRol());
                        myIntent.putExtra("NOMBRE_ROL",mensaje.getObjetoInventarios().getNombreRol());
                        myIntent.putExtra("OPERACION","LOGIN");
                        startActivity(myIntent);
                    } else if(mensaje.getObjetoInventarios().getNombreRol().equals("invitado") || mensaje.getObjetoInventarios().getNombreRol().equals("reporteria")){
                        showProgress(false);
                        Toast toast =
                                Toast.makeText(getApplicationContext(),
                                        "No cuenta con permisos para acceder al sistema", Toast.LENGTH_LONG);
                        toast.show();
                    }else if(mensaje.getObjetoInventarios().getNombreRol().equals("docente")){
                        this.myIntent = new Intent(LoginActivity.this, TicketingPrincipal.class);
                        myIntent.putExtra("ID_USUARIO_TICK",mensaje.getObjetoInventarios().getIdUsuario());
                        myIntent.putExtra("NICK_USUARIO_TICK", nick);
                        myIntent.putExtra("NOMBRES_USUARIO_TICK", mensaje.getObjetoInventarios().getNombresUsuario());
                        myIntent.putExtra("CORREO_USUARIO_TICK",mensaje.getObjetoInventarios().getCorreoUsuario());
                        myIntent.putExtra("ID_ROL_TICK",mensaje.getObjetoInventarios().getIdRol());
                        myIntent.putExtra("NOMBRE_ROL_TICK",mensaje.getObjetoInventarios().getNombreRol());
                        myIntent.putExtra("OPERACION_TICK","LOGIN");
                        startActivity(myIntent);
                    }else if(mensaje.getObjetoInventarios().getNombreRol().equals("generador_tickets")){
                        this.myIntent = new Intent(LoginActivity.this, TicketingPrincipal.class);
                        myIntent.putExtra("ID_USUARIO_TICK",mensaje.getObjetoInventarios().getIdUsuario());
                        myIntent.putExtra("NICK_USUARIO_TICK", nick);
                        myIntent.putExtra("NOMBRES_USUARIO_TICK", mensaje.getObjetoInventarios().getNombresUsuario());
                        myIntent.putExtra("CORREO_USUARIO_TICK",mensaje.getObjetoInventarios().getCorreoUsuario());
                        myIntent.putExtra("ID_ROL_TICK",mensaje.getObjetoInventarios().getIdRol());
                        myIntent.putExtra("NOMBRE_ROL_TICK",mensaje.getObjetoInventarios().getNombreRol());
                        myIntent.putExtra("OPERACION_TICK","LOGIN");
                        startActivity(myIntent);
                    }
                    else{
                        if(valoresRol[0]){
                            this.myIntent = new Intent(LoginActivity.this, ActivosPrincipal.class);
                            myIntent.putExtra("ID_USUARIO",mensaje.getObjetoInventarios().getIdUsuario());
                            myIntent.putExtra("NICK_USUARIO", nick);
                            myIntent.putExtra("CORREO_USUARIO",mensaje.getObjetoInventarios().getCorreoUsuario());
                            myIntent.putExtra("ID_ROL",mensaje.getObjetoInventarios().getIdRol());
                            myIntent.putExtra("NOMBRE_ROL",mensaje.getObjetoInventarios().getNombreRol());
                            myIntent.putExtra("OPERACION","LOGIN");
                            startActivity(myIntent);
                        }else{
                            showProgress(false);
                            Toast toast =
                                    Toast.makeText(getApplicationContext(),
                                            "No cuenta con permisos para acceder al sistema", Toast.LENGTH_LONG);
                            toast.show();
                        }

                    }
                }else{
                    showProgress(false);
                    Toast toast =
                            Toast.makeText(getApplicationContext(),
                                    "Usuario o Contraseña incorrectas", Toast.LENGTH_LONG);
                    toast.show();
                }
        }
    }

    /**
     * Método para la validación de la longitud del nick de usuario
     */
    private boolean longitudNick(String email) {
        return email.length() < 16;
    }

    /**
     * Método para la validación de la longitud del la contraseña de usuario
     */
    private boolean longitudPassword(String password) {
        return password.length() > 6;
    }

    /**
     * Método para mostrar el spinner de progreso al realizar una consulta.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        //  La API HONEYCOMB_MR2 es la que contiene los metódos para llamar al spinner de progreso
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            layoutLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            layoutLogin.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layoutLogin.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            layoutProgreso.setVisibility(show ? View.VISIBLE : View.GONE);
            layoutProgreso.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    layoutProgreso.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // Función para detener el spinner si las validaciones son incorrectas
            layoutProgreso.setVisibility(show ? View.VISIBLE : View.GONE);
            layoutLogin.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

