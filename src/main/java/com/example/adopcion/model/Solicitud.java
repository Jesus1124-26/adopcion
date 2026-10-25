package com.example.adopcion.model;

/**
 * Una solicitud de adopción: un adoptante pide adoptar una mascota.
 * Se guarda en solicitudes.txt, una por línea:
 * id|idMascota|idAdoptante|fechaSolicitud|estado|observacion|fechaRespuesta
 *
 * Las fechas se escriben como año-mes-día (2026-10-25).
 * fechaRespuesta queda vacía mientras la solicitud está pendiente.
 */
public class Solicitud {

    public static final String PENDIENTE = "Pendiente";
    public static final String APROBADA = "Aprobada";
    public static final String RECHAZADA = "Rechazada";
    public static final String[] ESTADOS = {PENDIENTE, APROBADA, RECHAZADA};

    public int id;
    public int idMascota;       // id de la mascota que se quiere adoptar
    public int idAdoptante;     // id de la persona que la quiere adoptar
    public String fechaSolicitud;
    public String estado;
    public String observacion;
    public String fechaRespuesta;

    public Solicitud() {
    }

    public Solicitud(int id, int idMascota, int idAdoptante, String fechaSolicitud,
        String estado, String observacion, String fechaRespuesta) {
        this.id = id;
        this.idMascota = idMascota;
        this.idAdoptante = idAdoptante;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.observacion = observacion;
        this.fechaRespuesta = fechaRespuesta;
    }
}