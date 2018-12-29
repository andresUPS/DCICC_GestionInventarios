package ec.edu.ups.gestioninventarios.Modelos.Mensajes;

import ec.edu.ups.gestioninventarios.Modelos.Activos;

/**
 * Created by Andres on 26/12/2018.
 */

public class MensajesActivos {
    public boolean operacionExitosa;
    public String mensajeError;
    public Activos objetoInventarios;

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

    public Activos getObjetoInventarios() {
        return objetoInventarios;
    }

    public void setObjetoInventarios(Activos objetoInventarios) {
        this.objetoInventarios = objetoInventarios;
    }
}
