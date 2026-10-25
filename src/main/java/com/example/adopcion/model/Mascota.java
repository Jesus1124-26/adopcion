package com.example.adopcion.model;

/**
 * Una mascota del refugio.
 * Se guarda en mascotas.txt, una por línea:
 * id|nombre|especie|raza|edad|sexo|tamano|estadoSalud|fotoUrl|estado
 */
public class Mascota {

    // Estados de salud
    public static final String SANO = "Sano";
    public static final String EN_TRATAMIENTO = "En tratamiento";
    public static final String URGENTE = "Urgente";

    // Estados de adopción
    public static final String DISPONIBLE = "Disponible";
    public static final String EN_PROCESO = "En proceso";
    public static final String ADOPTADA = "Adoptada";

    // Opciones para los ComboBox de los formularios
    public static final String[] ESPECIES = {"Perro", "Gato", "Otro"};
    public static final String[] SEXOS = {"Macho", "Hembra"};
    public static final String[] TAMANOS = {"Pequeño", "Mediano", "Grande"};
    public static final String[] ESTADOS_SALUD = {SANO, EN_TRATAMIENTO, URGENTE};
    public static final String[] ESTADOS = {DISPONIBLE, EN_PROCESO, ADOPTADA};

    public int id;
    public String nombre;
    public String especie;
    public String raza;
    public int edad;            // en años (0 = menos de un año)
    public String sexo;
    public String tamano;
    public String estadoSalud;
    public String fotoUrl;
    public String estado;

    public Mascota() {
    }

    public Mascota(int id, String nombre, String especie, String raza, int edad, String sexo,
        String tamano, String estadoSalud, String fotoUrl, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.sexo = sexo;
        this.tamano = tamano;
        this.estadoSalud = estadoSalud;
        this.fotoUrl = fotoUrl;
        this.estado = estado;
    }

    // Regla de atención urgente: la mascota la necesita si su estado de salud es "Urgente".
    // Está escrita en un solo lugar para que todas las pantallas usen la misma regla.
    public boolean necesitaAtencionUrgente() {
        return estadoSalud.equals(URGENTE);
    }
}