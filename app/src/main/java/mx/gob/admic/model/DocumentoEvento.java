package mx.gob.admic.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by codigus on 24/11/2017.
 */

public class DocumentoEvento extends RealmObject {
    @PrimaryKey
    private int id;
    private int idEvento;
    private String titulo;
    private String rutaDocumento;
    private int idFormato;
    private Formato formato;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    public int getIdFormato() {
        return idFormato;
    }

    public void setIdFormato(int idFormato) {
        this.idFormato = idFormato;
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

    public Formato getFormato() {
        return formato;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }
}
