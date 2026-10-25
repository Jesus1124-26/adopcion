package com.example.adopcion.model;

/**
 * Un registro del historial médico de una mascota: una vacuna o una consulta.
 * Se guarda en historial_medico.txt, uno por línea:
 * id|idMascota|tipo|fecha|descripcion
 */
public class RegistroMedico {

    public static final String[] TIPOS = {"Vacuna", "Consulta"};

    public int id;
    public int idMascota;
    public String tipo;
    public String fecha;         // año-mes-día
    public String descripcion;

    public RegistroMedico() {
    }

    public RegistroMedico(int id, int idMascota, String tipo, String fecha, String descripcion) {
        this.id = id;
        this.idMascota = idMascota;
        this.tipo = tipo;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }
}