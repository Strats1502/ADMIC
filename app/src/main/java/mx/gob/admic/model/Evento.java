package mx.gob.admic.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by code on 12/05/16.
 */
public class Evento extends RealmObject {
    @PrimaryKey
    private int idEvento;
    private String titulo;
    private String areaResponsable;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String rutaImagen;
    private int idTipoEvento;
    private String direccion;
    private double latitud;
    private double longitud;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private boolean estaRegistrado;
    private Area area;
    private RealmList<DocumentoEvento> documentos;

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(int idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setEstaRegistrado(boolean estaRegistrado) {
        this.estaRegistrado = estaRegistrado;
    }

    public boolean getEstaRegistrado() {
        return this.estaRegistrado;
    }

    public String getAreaResponsable() {
        return areaResponsable;
    }

    public void setAreaResponsable(String areaResponsable) {
        this.areaResponsable = areaResponsable;
    }


    public boolean isEstaRegistrado() {
        return estaRegistrado;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public RealmList<DocumentoEvento> getDocumentosEvento() {
        return documentos;
    }

    public void setDocumentosEvento(RealmList<DocumentoEvento> documentos) {
        this.documentos = documentos;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public RealmList<DocumentoEvento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(RealmList<DocumentoEvento> documentos) {
        this.documentos = documentos;
    }
}
