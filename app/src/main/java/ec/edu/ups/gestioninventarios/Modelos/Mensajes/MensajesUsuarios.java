package ec.edu.ups.gestioninventarios.Modelos.Mensajes;

import ec.edu.ups.gestioninventarios.Modelos.Usuarios;

/**
 * Created by Andres on 11/12/2018.
 */

public class MensajesUsuarios {

    public boolean operacionExitosa;
    public String mensajeError;
    public Usuarios objetoInventarios;

    public boolean isOperacionExitosa() {
        return operacionExitosa;
    }

    public void setOperacionExitosa(boolean operacionExitosa) {
        this.operacionExitosa = operacionExitosa;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public Usuarios getObjetoInventarios() {
        return objetoInventarios;
    }

    public void setObjetoInventarios(Usuarios objetoInventarios) {
        this.objetoInventarios = objetoInventarios;
    }

}
