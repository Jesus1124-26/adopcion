package com.example.adopcion.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

/**
 * Plantilla que comparten todas las vistas: una barra arriba con el título
 * y un menú lateral para moverse entre las secciones.
 *
 * Investigado en la documentación de Vaadin (App Layout y Side Navigation):
 * https://vaadin.com/docs/latest/components/app-layout
 * En el celular el menú se esconde solo y se abre con el botón de las tres rayas.
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        // Botón de tres rayas que abre y cierra el menú
        DrawerToggle botonMenu = new DrawerToggle();

        H1 titulo = new H1("Refugio de mascotas");
        titulo.getStyle().set("font-size", "1.125rem").set("margin", "0");

        // Menú lateral: cada opción lleva a una vista
        SideNav menu = new SideNav();
        menu.addItem(new SideNavItem("Inicio", MainView.class));
        menu.addItem(new SideNavItem("Mascotas", MascotasView.class));

        addToNavbar(botonMenu, titulo);
        addToDrawer(new Scroller(menu));
    }
}
