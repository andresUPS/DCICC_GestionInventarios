package ec.edu.ups.gestioninventarios;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;

import ec.edu.ups.gestioninventarios.Modelos.Mensajes.MensajesTickets;
import ec.edu.ups.gestioninventarios.Vistas.Ticketing.TicketingCQR;
import ec.edu.ups.gestioninventarios.WebService.MetodosTickets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Andres on 2/1/2019.
 */

public class IngresoTicket {
    @Test
    public void addition_isCorrect() throws Exception {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        Log.e("token ticket","valio");
        DataOutputStream os=new DataOutputStream(null);
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
        writer.write("");
        writer.flush();
        writer.close();
        JSONObject jsonObjectEnvio=new JSONObject();

        assertEquals(5, 2 + 2);
    }

    @Test
    public void IngresoNuevoTicket() throws Exception{
        MetodosTickets tickets=new MetodosTickets();
        MensajesTickets mensajesTickets=new MensajesTickets();
        mensajesTickets=tickets.ingresarTicketActAcc("docente1",34,120,0, "No se puede ingresar a la sesión de windows","ALTA");
        assertTrue(mensajesTickets.isOperacionExitosa());
        assertTrue(mensajesTickets.getMensajeError()=="");
    }

    @Test
    public void comprobarTicket() throws Exception{
        TicketingCQR comprobar=new TicketingCQR();
        String valorActivo=comprobar.instanciaActivo("DCICC.ACY.CQR42");
        assertTrue(valorActivo.equals("ACT"));
        String valorAccesorio=comprobar.instanciaActivo("DCICC.ACM.CQR32");
        assertTrue(valorAccesorio.equals("ACC"));
                assertTrue(!valorActivo.equals("ACT") || !valorAccesorio.equals("ACC"));
    }
}
