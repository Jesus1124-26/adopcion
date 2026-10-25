package com.example.adopcion.model;

/**
 * Una persona que quiere adoptar.
 * Se guarda en adoptantes.txt, una por línea:
 * id|documento|nombre|telefono|email|tipoVivienda|otrasMascotas|experiencia|prefEspecie|prefTamano
 */
public class Adoptante {

    public static final String[] TIPOS_VIVIENDA = {"Apartamento", "Casa sin patio", "Casa con patio"};
    public static final String[] SI_NO = {"Sí", "No"};
    // "Cualquiera" significa que la persona no tiene preferencia
    public static final String[] PREFERENCIAS_ESPECIE = {"Perro", "Gato", "Otro", "Cualquiera"};
    public static final String[] PREFERENCIAS_TAMANO = {"Pequeño", "Mediano", "Grande", "Cualquiera"};

    public int id;
    public String documento;
    public String nombre;
    public String telefono;
    public String email;
    public String tipoVivienda;
    public String otrasMascotas;        // "Sí" o "No"
    public String experiencia;          // "Sí" o "No": si ya ha cuidado mascotas antes
    public String preferenciaEspecie;
    public String preferenciaTamano;

    public Adoptante() {
    }

    public Adoptante(int id, String documento, String nombre, String telefono, String email,
        String tipoVivienda, String otrasMascotas, String experiencia,
        String preferenciaEspecie, String preferenciaTamano) {
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.tipoVivienda = tipoVivienda;
        this.otrasMascotas = otrasMascotas;
        this.experiencia = experiencia;
        this.preferenciaEspecie = preferenciaEspecie;
        this.preferenciaTamano = preferenciaTamano;
    }
}