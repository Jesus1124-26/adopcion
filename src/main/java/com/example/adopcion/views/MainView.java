package com.example.adopcion.views;

import com.example.adopcion.model.Mascota;
import com.example.adopcion.util.ArchivoUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

/**
 * Vista de inicio: un resumen de los datos del refugio y la alerta de las
 * mascotas que necesitan atención urgente.
 */
@Route(value = "", layout = MainLayout.class) // "" = página de inicio; layout = usa el menú de MainLayout
@PageTitle("Inicio - Refugio")
public class MainView extends VerticalLayout {

    public MainView() {
        // Ajustes de la vista: centrar, dejar margen y separar los componentes
        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Bienvenido al refugio");
        Paragraph descripcion = new Paragraph(
                "Sistema para gestionar mascotas, adoptantes, solicitudes de adopción, "
                + "historial médico y donaciones.");

        // Leer los datos (la primera vez, ArchivoUtil crea los .txt con datos de ejemplo)
        List<Mascota> mascotas = ArchivoUtil.leerMascotas();
        int totalAdoptantes = ArchivoUtil.leerAdoptantes().size();
        int totalSolicitudes = ArchivoUtil.leerSolicitudes().size();
        int totalRegistros = ArchivoUtil.leerHistorial().size();
        int totalDonaciones = ArchivoUtil.leerDonaciones().size();

        // Contar las disponibles y armar la lista de las que necesitan atención urgente
        int disponibles = 0;
        int urgentes = 0;
        String nombresUrgentes = "";
        for (Mascota m : mascotas) {
            if (m.estado.equals(Mascota.DISPONIBLE)) {
                disponibles++;
            }
            if (m.necesitaAtencionUrgente()) {
                urgentes++;
                if (urgentes > 1) {
                    nombresUrgentes += ", ";
                }
                nombresUrgentes += m.nombre;
            }
        }

        H3 subtitulo = new H3("Resumen");
        Paragraph resumenMascotas = new Paragraph(
                "Mascotas: " + mascotas.size() + " (" + disponibles + " disponibles)");
        Paragraph resumenAdoptantes = new Paragraph("Adoptantes: " + totalAdoptantes);
        Paragraph resumenSolicitudes = new Paragraph("Solicitudes: " + totalSolicitudes);
        Paragraph resumenHistorial = new Paragraph("Registros médicos: " + totalRegistros);
        Paragraph resumenDonaciones = new Paragraph("Donaciones: " + totalDonaciones);
        Paragraph resumenUrgentes = new Paragraph("Ninguna mascota necesita atención urgente");
        if (urgentes > 0) {
            resumenUrgentes.setText("Atención urgente: " + urgentes + " (" + nombresUrgentes + ")");
        }

        // Botón para ir al catálogo (navigate, como en la Sesión 11)
        Button verCatalogo = new Button("Ver catálogo de mascotas",
                e -> UI.getCurrent().navigate(MascotasView.class));

        // Sin add(...) los componentes existen en Java pero no aparecen en pantalla
        add(titulo, descripcion, subtitulo, resumenMascotas, resumenAdoptantes,
                resumenSolicitudes, resumenHistorial, resumenDonaciones, resumenUrgentes, verCatalogo);
    }
}
