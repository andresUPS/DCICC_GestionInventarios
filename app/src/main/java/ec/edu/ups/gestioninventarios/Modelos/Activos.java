package ec.edu.ups.gestioninventarios.Modelos;

/**
 * Created by Andres on 24/12/2018.
 */

public class Activos {

    public int IdActivo;
    public String NombreCategoriaActivo;
    public int IdTipoActivo;
    public int IdLaboratorio;
    public String NombreMarca;
    public String NombreLaboratorio;
    public String ResponsableActivo;
    public String NombreActivo;
    public String EstadoActivo;
    public String ModeloActivo;
    public String SerialActivo;
    public String FechaIngresoActivo;
    public String CodigoUpsActivo;
    public String cadenaCQR;

    public String getNombreMarca() {
        return NombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        NombreMarca = nombreMarca;
    }

    public String getCadenaCQR() {
        return cadenaCQR;
    }

    public void setCadenaCQR(String cadenaCQR) {
        this.cadenaCQR = cadenaCQR;
    }

    public int getIdActivo() {
        return IdActivo;
    }

    public void setIdActivo(int idActivo) {
        IdActivo = idActivo;
    }

    public String getNombreCategoriaActivo() {
        return NombreCategoriaActivo;
    }

    public void setNombreCategoriaActivo(String nombreCategoriaActivo) {
        NombreCategoriaActivo = nombreCategoriaActivo;
    }

    public int getIdTipoActivo() {
        return IdTipoActivo;
    }

    public void setIdTipoActivo(int idTipoActivo) {
        IdTipoActivo = idTipoActivo;
    }

    public int getIdLaboratorio() {
        return IdLaboratorio;
    }

    public void setIdLaboratorio(int idLaboratorio) {
        IdLaboratorio = idLaboratorio;
    }

    public String getNombreLaboratorio() {
        return NombreLaboratorio;
    }

    public void setNombreLaboratorio(String nombreLaboratorio) {
        NombreLaboratorio = nombreLaboratorio;
    }

    public String getResponsableActivo() {
        return ResponsableActivo;
    }

    public void setResponsableActivo(String responsableActivo) {
        ResponsableActivo = responsableActivo;
    }

    public String getNombreActivo() {
        return NombreActivo;
    }

    public void setNombreActivo(String nombreActivo) {
        NombreActivo = nombreActivo;
    }

    public String getEstadoActivo() {
        return EstadoActivo;
    }

    public void setEstadoActivo(String estadoActivo) {
        EstadoActivo = estadoActivo;
    }

    public String getModeloActivo() {
        return ModeloActivo;
    }

    public void setModeloActivo(String modeloActivo) {
        ModeloActivo = modeloActivo;
    }

    public String getSerialActivo() {
        return SerialActivo;
    }

    public void setSerialActivo(String serialActivo) {
        SerialActivo = serialActivo;
    }

    public String getFechaIngresoActivo() {
        return FechaIngresoActivo;
    }

    public void setFechaIngresoActivo(String fechaIngresoActivo) {
        FechaIngresoActivo = fechaIngresoActivo;
    }

    public String getCodigoUpsActivo() {
        return CodigoUpsActivo;
    }

    public void setCodigoUpsActivo(String codigoUpsActivo) {
        CodigoUpsActivo = codigoUpsActivo;
    }
}
