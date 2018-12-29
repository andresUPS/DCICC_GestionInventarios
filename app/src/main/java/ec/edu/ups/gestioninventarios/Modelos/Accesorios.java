package ec.edu.ups.gestioninventarios.Modelos;

/**
 * Created by Andres on 26/12/2018.
 */

public class Accesorios {
    public int IdAccesorio;
    public int IdTipoAccesorio;
    public String NombreTipoAccesorio;
    public int IdDetalleActivo;
    public String NombreDetalleActivo;
    public String NombreAccesorio;
    public String EstadoAccesorio;
    public String SerialAccesorio;
    public String ModeloAccesorio;
    public String cadenaAccesorioCQR;

    public int getIdAccesorio() {
        return IdAccesorio;
    }

    public void setIdAccesorio(int idAccesorio) {
        IdAccesorio = idAccesorio;
    }

    public int getIdTipoAccesorio() {
        return IdTipoAccesorio;
    }

    public void setIdTipoAccesorio(int idTipoAccesorio) {
        IdTipoAccesorio = idTipoAccesorio;
    }

    public String getNombreTipoAccesorio() {
        return NombreTipoAccesorio;
    }

    public void setNombreTipoAccesorio(String nombreTipoAccesorio) {
        NombreTipoAccesorio = nombreTipoAccesorio;
    }

    public int getIdDetalleActivo() {
        return IdDetalleActivo;
    }

    public void setIdDetalleActivo(int idDetalleActivo) {
        IdDetalleActivo = idDetalleActivo;
    }

    public String getNombreDetalleActivo() {
        return NombreDetalleActivo;
    }

    public void setNombreDetalleActivo(String nombreDetalleActivo) {
        NombreDetalleActivo = nombreDetalleActivo;
    }

    public String getNombreAccesorio() {
        return NombreAccesorio;
    }

    public void setNombreAccesorio(String nombreAccesorio) {
        NombreAccesorio = nombreAccesorio;
    }

    public String getEstadoAccesorio() {
        return EstadoAccesorio;
    }

    public void setEstadoAccesorio(String estadoAccesorio) {
        EstadoAccesorio = estadoAccesorio;
    }

    public String getSerialAccesorio() {
        return SerialAccesorio;
    }

    public void setSerialAccesorio(String serialAccesorio) {
        SerialAccesorio = serialAccesorio;
    }

    public String getModeloAccesorio() {
        return ModeloAccesorio;
    }

    public void setModeloAccesorio(String modeloAccesorio) {
        ModeloAccesorio = modeloAccesorio;
    }

    public String getCadenaAccesorioCQR() {
        return cadenaAccesorioCQR;
    }

    public void setCadenaAccesorioCQR(String cadenaAccesorioCQR) {
        this.cadenaAccesorioCQR = cadenaAccesorioCQR;
    }
}
