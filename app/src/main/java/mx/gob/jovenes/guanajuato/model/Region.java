package mx.gob.jovenes.guanajuato.model;

/**
 * Created by Juan José Estrada Valtierra on 25/04/17.
 */

public class Region extends RealmObject{
    @PrimaryKey
    private int id;
    private String nombre;
    private String direccion;
    private String responsable;
    private String descripcion;
    private double latitud;
    private double longitud;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;




}
