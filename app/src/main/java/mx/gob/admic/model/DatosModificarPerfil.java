package mx.gob.admic.model;

/**
 * Created by codigus on 04/07/2017.
 */

public class DatosModificarPerfil {
    private String apiToken;
    private String calle;
    private String colonia;
    private String ciudad;
    private String empresa;
    private String actividad;
    private int numEmpleados;
    private String rfc;
    private int antiguedad;
    private int idNivelEstudios;
    private String rutaImagen;

    public DatosModificarPerfil(String apiToken, String rutaImagen, int idNivelEstudios, String calle, String colonia, String ciudad, String empresa, String actividad, int numEmpleados, String rfc, int antiguedad) {
        this.apiToken = apiToken;
        this.rutaImagen = rutaImagen;
        this.idNivelEstudios = idNivelEstudios;
        this.calle = calle;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.empresa = empresa;
        this.actividad = actividad;
        this.numEmpleados = numEmpleados;
        this.rfc = rfc;
        this.antiguedad = antiguedad;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public int getIdNivelEstudios() {
        return idNivelEstudios;
    }

    public void setIdNivelEstudios(int idNivelEstudios) {
        this.idNivelEstudios = idNivelEstudios;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public int getNumEmpleados() {
        return numEmpleados;
    }

    public void setNumEmpleados(int numEmpleados) {
        this.numEmpleados = numEmpleados;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }
}
