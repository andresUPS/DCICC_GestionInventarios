package ec.edu.ups.gestioninventarios.Modelos.Mensajes;

import ec.edu.ups.gestioninventarios.Modelos.Tickets;

/**
 * Created by Andres on 27/12/2018.
 */

public class MensajesTickets {
    public boolean operacionExitosa;
    public String mensajeError;
    public Tickets objetoInventarios;

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

    public Tickets getObjetoInventarios() {
        return objetoInventarios;
    }

    public void setObjetoInventarios(Tickets objetoInventarios) {
        this.objetoInventarios = objetoInventarios;
    }
}
