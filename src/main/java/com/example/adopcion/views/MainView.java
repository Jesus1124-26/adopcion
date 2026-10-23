package com.example.adopcion.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Vista de inicio del proyecto (Día 1).
 *
 * Por ahora solo muestra un título y una descripción, para comprobar que el
 * proyecto arranca bien. Más adelante esta vista se convierte en el catálogo
 * de mascotas (Grid con búsqueda, filtros, fotos y estados).
 */
@Route("") // "" = página de inicio, la que se ve en http://localhost:8080
@PageTitle("Refugio - Adopción de mascotas") // texto de la pestaña del navegador
public class MainView extends VerticalLayout {

    public MainView() {
        // Ajustes de la vista: centrar, dejar margen y separar los componentes
        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        H1 titulo = new H1("Refugio de mascotas");
        Paragraph descripcion = new Paragraph(
                "Sistema para gestionar mascotas, adoptantes, solicitudes de adopción, "
                + "historial médico y donaciones. Proyecto en construcción.");

        // Sin add(...) los componentes existen en Java pero no aparecen en pantalla
        add(titulo, descripcion);
    }
}