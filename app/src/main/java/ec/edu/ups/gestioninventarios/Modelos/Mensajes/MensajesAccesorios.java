package ec.edu.ups.gestioninventarios.Modelos.Mensajes;

import ec.edu.ups.gestioninventarios.Modelos.Accesorios;

/**
 * Created by Andres on 26/12/2018.
 */

public class MensajesAccesorios {
    public boolean operacionExitosa;
    public String mensajeError;
    public Accesorios objetoInventarios;

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

    public Accesorios getObjetoInventarios() {
        return objetoInventarios;
    }

    public void setObjetoInventarios(Accesorios objetoInventarios) {
        this.objetoInventarios = objetoInventarios;
    }
}
