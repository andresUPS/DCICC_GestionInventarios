package ec.edu.ups.gestioninventarios.Modelos;

/**
 * Created by Andres on 11/12/2018.
 */

public class Usuarios {
    public int IdUsuario;
    public String NombreRol;
    public int IdRol;
    public String NombresUsuario;
    public String NickUsuario;
    public String PasswordUsuario;
    public String CorreoUsuario;
    public String TelefonoUsuario;
    public String TelefonoCelUsuario;
    public String DireccionUsuario;
    public boolean HabilitadoUsuario;

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNombreRol() {
        return NombreRol;
    }

    public void setNombreRol(String nombreRol) {
        NombreRol = nombreRol;
    }

    public int getIdRol() {
        return IdRol;
    }

    public void setIdRol(int idRol) {
        IdRol = idRol;
    }

    public String getNombresUsuario() {
        return NombresUsuario;
    }

    public void setNombresUsuario(String nombresUsuario) {
        NombresUsuario = nombresUsuario;
    }

    public String getNickUsuario() {
        return NickUsuario;
    }

    public void setNickUsuario(String nickUsuario) {
        NickUsuario = nickUsuario;
    }

    public String getPasswordUsuario() {
        return PasswordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        PasswordUsuario = passwordUsuario;
    }

    public String getCorreoUsuario() {
        return CorreoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        CorreoUsuario = correoUsuario;
    }

    public String getTelefonoUsuario() {
        return TelefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        TelefonoUsuario = telefonoUsuario;
    }

    public String getTelefonoCelUsuario() {
        return TelefonoCelUsuario;
    }

    public void setTelefonoCelUsuario(String telefonoCelUsuario) {
        TelefonoCelUsuario = telefonoCelUsuario;
    }

    public String getDireccionUsuario() {
        return DireccionUsuario;
    }

    public void setDireccionUsuario(String direccionUsuario) {
        DireccionUsuario = direccionUsuario;
    }

    public boolean isHabilitadoUsuario() {
        return HabilitadoUsuario;
    }

    public void setHabilitadoUsuario(boolean habilitadoUsuario) {
        HabilitadoUsuario = habilitadoUsuario;
    }
}
