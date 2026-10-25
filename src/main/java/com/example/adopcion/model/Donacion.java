package com.example.adopcion.model;

/**
 * Una donación recibida por el refugio.
 * Se guarda en donaciones.txt, una por línea:
 * id|donante|tipo|valor|fecha|descripcion
 *
 * valor está en pesos. Si la donación no es dinero (alimento, medicamentos),
 * es el valor aproximado de lo donado.
 */
public class Donacion {

    public static final String[] TIPOS = {"Dinero", "Alimento", "Medicamentos", "Otro"};

    public int id;
    public String donante;
    public String tipo;
    public double valor;
    public String fecha;         // año-mes-día
    public String descripcion;

    public Donacion() {
    }

    public Donacion(int id, String donante, String tipo, double valor, String fecha, String descripcion) {
        this.id = id;
        this.donante = donante;
        this.tipo = tipo;
        this.valor = valor;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }
}