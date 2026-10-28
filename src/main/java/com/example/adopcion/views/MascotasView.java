package com.example.adopcion.views;

import com.example.adopcion.model.Mascota;
import com.example.adopcion.util.ArchivoUtil;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Catálogo de mascotas: una tabla (Grid) con todas las mascotas del refugio,
 * una búsqueda por nombre o raza y filtros por especie y por estado.
 *
 * Investigado en la documentación de Vaadin: Grid y ValueChangeMode
 * https://vaadin.com/docs/latest/components/grid
 */
@Route(value = "mascotas", layout = MainLayout.class)
@PageTitle("Mascotas - Refugio")
public class MascotasView extends VerticalLayout {

    public List<Mascota> mascotas;

    public Grid<Mascota> grid = new Grid<>();
    public TextField busqueda = new TextField("Buscar");
    public ComboBox<String> filtroEspecie = new ComboBox<>("Especie");
    public ComboBox<String> filtroEstado = new ComboBox<>("Estado");
    public Paragraph contador = new Paragraph();

    public MascotasView() {
        setSizeFull();

        H2 titulo = new H2("Catálogo de mascotas");

        // Búsqueda: con EAGER la tabla se filtra con cada letra que se escribe,
        // no solo al presionar Enter o salir del campo
        busqueda.setPlaceholder("Nombre o raza...");
        busqueda.setClearButtonVisible(true);
        busqueda.setValueChangeMode(ValueChangeMode.EAGER);
        busqueda.addValueChangeListener(e -> actualizarTabla());

        // Filtros: si el ComboBox queda vacío, se muestran todas
        filtroEspecie.setItems(Mascota.ESPECIES);
        filtroEspecie.setPlaceholder("Todas");
        filtroEspecie.setClearButtonVisible(true);
        filtroEspecie.addValueChangeListener(e -> actualizarTabla());

        filtroEstado.setItems(Mascota.ESTADOS);
        filtroEstado.setPlaceholder("Todos");
        filtroEstado.setClearButtonVisible(true);
        filtroEstado.addValueChangeListener(e -> actualizarTabla());

        // FormLayout acomoda los tres campos en fila en el computador
        // y uno debajo del otro en el celular
        FormLayout filtros = new FormLayout();
        filtros.add(busqueda, filtroEspecie, filtroEstado);

        // Columnas de la tabla: cada lambda dice qué dato de la mascota mostrar
        grid.addColumn(m -> m.nombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(m -> m.especie).setHeader("Especie").setAutoWidth(true);
        grid.addColumn(m -> m.raza).setHeader("Raza").setAutoWidth(true);
        grid.addColumn(m -> textoEdad(m.edad)).setHeader("Edad").setAutoWidth(true);
        grid.addColumn(m -> m.sexo).setHeader("Sexo").setAutoWidth(true);
        grid.addColumn(m -> m.estadoSalud).setHeader("Salud").setAutoWidth(true);
        grid.addColumn(m -> m.estado).setHeader("Estado").setAutoWidth(true);

        add(titulo, filtros, contador, grid);

        mascotas = ArchivoUtil.leerMascotas();
        actualizarTabla();
    }

    // Vuelve a llenar la tabla solo con las mascotas que cumplen la búsqueda y los filtros
    public void actualizarTabla() {
        String texto = busqueda.getValue().trim().toLowerCase();
        String especie = filtroEspecie.getValue();   // null si no se eligió nada
        String estado = filtroEstado.getValue();

        List<Mascota> filtradas = new ArrayList<>();
        for (Mascota m : mascotas) {
            boolean coincideTexto = texto.isEmpty()
                    || m.nombre.toLowerCase().contains(texto)
                    || m.raza.toLowerCase().contains(texto);
            boolean coincideEspecie = especie == null || m.especie.equals(especie);
            boolean coincideEstado = estado == null || m.estado.equals(estado);

            if (coincideTexto && coincideEspecie && coincideEstado) {
                filtradas.add(m);
            }
        }

        grid.setItems(filtradas);
        contador.setText("Mostrando " + filtradas.size() + " de " + mascotas.size() + " mascotas");
    }

    // Convierte la edad en un texto fácil de leer
    public String textoEdad(int edad) {
        if (edad == 0) return "Menos de 1 año";
        if (edad == 1) return "1 año";
        return edad + " años";
    }
}
