package com.example.adopcion.views;

import java.util.ArrayList;
import java.util.List;

import com.example.adopcion.model.Mascota;
import com.example.adopcion.model.RegistroMedico;
import com.example.adopcion.model.Solicitud;
import com.example.adopcion.util.ArchivoUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Catálogo de mascotas con CRUD completo:
 * - Ver: tabla (Grid) con foto, datos y estados de colores, búsqueda y filtros.
 * - Crear y editar: formulario en un Dialog, con validación antes de guardar.
 * - Eliminar: siempre pasa primero por un diálogo de confirmación.
 *
 * Investigado en la documentación de Vaadin: Grid (addComponentColumn),
 * Image (https://vaadin.com/docs/latest/components/html-elements),
 * Badge (https://vaadin.com/docs/latest/components/badge) y ValueChangeMode.
 */
@Route(value = "mascotas", layout = MainLayout.class)
@PageTitle("Mascotas - Refugio")
public class MascotasView extends VerticalLayout {

    // Datos y tabla
    public List<Mascota> mascotas;
    public Grid<Mascota> grid = new Grid<>();

    // Búsqueda y filtros
    public TextField busqueda = new TextField("Buscar");
    public ComboBox<String> filtroEspecie = new ComboBox<>("Especie");
    public ComboBox<String> filtroEstado = new ComboBox<>("Estado");
    public Paragraph contador = new Paragraph();

    // Formulario para crear o editar (se arma una sola vez y se reutiliza)
    public Dialog dialogo = new Dialog();
    public Mascota mascotaEditando;   // null cuando se está creando una mascota nueva
    public TextField campoNombre = new TextField("Nombre");
    public ComboBox<String> campoEspecie = new ComboBox<>("Especie");
    public TextField campoRaza = new TextField("Raza");
    public TextField campoEdad = new TextField("Edad (años)");
    public RadioButtonGroup<String> campoSexo = new RadioButtonGroup<>();
    public ComboBox<String> campoTamano = new ComboBox<>("Tamaño");
    public ComboBox<String> campoSalud = new ComboBox<>("Estado de salud");
    public ComboBox<String> campoEstado = new ComboBox<>("Estado de adopción");
    public TextField campoFoto = new TextField("Foto (dirección web)");

    public MascotasView() {
        setSizeFull();

        H2 titulo = new H2("Catálogo de mascotas");
        Button btnNueva = new Button("Nueva mascota", e -> abrirFormulario(null));
        btnNueva.addThemeVariants(ButtonVariant.PRIMARY);
        HorizontalLayout encabezado = new HorizontalLayout(titulo, btnNueva);
        encabezado.setAlignItems(Alignment.CENTER);

        // Búsqueda: con EAGER la tabla se filtra con cada letra que se escribe
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

        FormLayout filtros = new FormLayout();
        filtros.add(busqueda, filtroEspecie, filtroEstado);

        // Columnas. addColumn muestra un texto; addComponentColumn muestra un componente
        // (una foto, un badge o botones) en cada fila
        grid.addComponentColumn(m -> crearFoto(m)).setHeader("Foto").setAutoWidth(true);
        grid.addColumn(m -> m.nombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(m -> m.especie).setHeader("Especie").setAutoWidth(true);
        grid.addColumn(m -> m.raza).setHeader("Raza").setAutoWidth(true);
        grid.addColumn(m -> textoEdad(m.edad)).setHeader("Edad").setAutoWidth(true);
        grid.addColumn(m -> m.sexo).setHeader("Sexo").setAutoWidth(true);
        grid.addComponentColumn(m -> crearBadgeSalud(m.estadoSalud)).setHeader("Salud").setAutoWidth(true);
        grid.addComponentColumn(m -> crearBadgeEstado(m.estado)).setHeader("Estado").setAutoWidth(true);
        grid.addComponentColumn(m -> crearBotones(m)).setHeader("Acciones").setAutoWidth(true);

        crearFormulario();

        add(encabezado, filtros, contador, grid);

        mascotas = ArchivoUtil.leerMascotas();
        actualizarTabla();
    }

    // ===================== Tabla =====================

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

    // Foto pequeña de la mascota (Image). Si no tiene foto, muestra un texto
    public Component crearFoto(Mascota m) {
        if (m.fotoUrl.trim().isEmpty()) {
            return new Span("Sin foto");
        }
        Image foto = new Image(m.fotoUrl, "Foto de " + m.nombre);
        foto.setWidth("56px");
        foto.setHeight("56px");
        foto.getStyle().set("object-fit", "cover").set("border-radius", "8px");
        return foto;
    }

    // Badge de color según el estado de salud: verde, amarillo o rojo
    public Badge crearBadgeSalud(String salud) {
        Badge badge = new Badge(salud);
        if (salud.equals(Mascota.SANO)) {
            badge.addThemeVariants(BadgeVariant.SUCCESS);
        } else if (salud.equals(Mascota.EN_TRATAMIENTO)) {
            badge.addThemeVariants(BadgeVariant.WARNING);
        } else {
            badge.addThemeVariants(BadgeVariant.ERROR, BadgeVariant.FILLED);   // Urgente: rojo y relleno
        }
        return badge;
    }

    // Badge de color según el estado de adopción
    public Badge crearBadgeEstado(String estado) {
        Badge badge = new Badge(estado);
        if (estado.equals(Mascota.DISPONIBLE)) {
            badge.addThemeVariants(BadgeVariant.SUCCESS);
        } else if (estado.equals(Mascota.EN_PROCESO)) {
            badge.addThemeVariants(BadgeVariant.WARNING);
        } else {
            badge.addThemeVariants(BadgeVariant.CONTRAST);   // Adoptada
        }
        return badge;
    }

    // Botones de cada fila. La lambda ya sabe cuál es su mascota (m)
    public HorizontalLayout crearBotones(Mascota m) {
        Button editar = new Button("Editar", e -> abrirFormulario(m));
        editar.addThemeVariants(ButtonVariant.SMALL);

        Button eliminar = new Button("Eliminar", e -> confirmarEliminar(m));
        eliminar.addThemeVariants(ButtonVariant.SMALL, ButtonVariant.ERROR);

        return new HorizontalLayout(editar, eliminar);
    }

    // ===================== Crear y editar =====================

    // Arma el diálogo del formulario una sola vez (se llama desde el constructor)
    public void crearFormulario() {
        campoEspecie.setItems(Mascota.ESPECIES);
        campoSexo.setLabel("Sexo");
        campoSexo.setItems(Mascota.SEXOS);
        campoTamano.setItems(Mascota.TAMANOS);
        campoSalud.setItems(Mascota.ESTADOS_SALUD);
        campoEstado.setItems(Mascota.ESTADOS);
        campoRaza.setPlaceholder("Si no se sabe, escribe Criollo");
        campoEdad.setPlaceholder("Ejemplo: 3");
        campoFoto.setPlaceholder("https://...");

        FormLayout formulario = new FormLayout();
        formulario.add(campoNombre, campoEspecie, campoRaza, campoEdad, campoSexo,
                campoTamano, campoSalud, campoEstado, campoFoto);
        dialogo.add(formulario);

        Button cancelar = new Button("Cancelar", e -> dialogo.close());
        Button guardar = new Button("Guardar", e -> guardarMascota());
        guardar.addThemeVariants(ButtonVariant.PRIMARY);
        dialogo.getFooter().add(cancelar, guardar);
    }

    // Abre el formulario. Si mascota es null, es para crear una nueva; si no, para editarla
    public void abrirFormulario(Mascota mascota) {
        mascotaEditando = mascota;

        if (mascota == null) {
            dialogo.setHeaderTitle("Nueva mascota");
            campoNombre.clear();
            campoEspecie.clear();
            campoRaza.clear();
            campoEdad.clear();
            campoSexo.clear();
            campoTamano.clear();
            campoSalud.setValue(Mascota.SANO);
            campoEstado.setValue(Mascota.DISPONIBLE);
            campoFoto.clear();
        } else {
            dialogo.setHeaderTitle("Editar a " + mascota.nombre);
            campoNombre.setValue(mascota.nombre);
            campoEspecie.setValue(mascota.especie);
            campoRaza.setValue(mascota.raza);
            campoEdad.setValue(String.valueOf(mascota.edad));
            campoSexo.setValue(mascota.sexo);
            campoTamano.setValue(mascota.tamano);
            campoSalud.setValue(mascota.estadoSalud);
            campoEstado.setValue(mascota.estado);
            campoFoto.setValue(mascota.fotoUrl);
        }
        dialogo.open();
    }

    // Revisa el formulario. Devuelve el mensaje del primer error, o "" si todo está bien
    public String validarFormulario() {
        if (campoNombre.getValue().trim().isEmpty()) return "Escribe el nombre de la mascota";
        if (campoEspecie.isEmpty()) return "Elige la especie";
        if (campoRaza.getValue().trim().isEmpty()) return "Escribe la raza (si no se sabe, escribe Criollo)";
        if (campoEdad.getValue().trim().isEmpty()) return "Escribe la edad";

        int edad;
        try {
            edad = Integer.parseInt(campoEdad.getValue().trim());
        } catch (NumberFormatException ex) {
            return "La edad debe ser un número entero, por ejemplo 3";
        }
        if (edad < 0 || edad > 30) return "La edad debe estar entre 0 y 30 años";

        if (campoSexo.isEmpty()) return "Elige el sexo";
        if (campoTamano.isEmpty()) return "Elige el tamaño";
        if (campoSalud.isEmpty()) return "Elige el estado de salud";
        if (campoEstado.isEmpty()) return "Elige el estado de adopción";

        String foto = campoFoto.getValue().trim();
        if (!foto.isEmpty() && !foto.startsWith("http")) {
            return "La foto debe ser una dirección web que empiece por http";
        }

        // El | separa los campos en el .txt, así que no se puede escribir en los textos
        if (campoNombre.getValue().contains("|") || campoRaza.getValue().contains("|") || foto.contains("|")) {
            return "No uses el carácter | en los textos";
        }
        return "";
    }

    // Valida, pasa los datos del formulario a la mascota y guarda el archivo
    public void guardarMascota() {
        String error = validarFormulario();
        if (!error.isEmpty()) {
            Notification.show(error);
            return;
        }

        Mascota m = mascotaEditando;
        if (m == null) {
            m = new Mascota();
            m.id = siguienteId();
            mascotas.add(m);
        }
        m.nombre = campoNombre.getValue().trim();
        m.especie = campoEspecie.getValue();
        m.raza = campoRaza.getValue().trim();
        m.edad = Integer.parseInt(campoEdad.getValue().trim());
        m.sexo = campoSexo.getValue();
        m.tamano = campoTamano.getValue();
        m.estadoSalud = campoSalud.getValue();
        m.estado = campoEstado.getValue();
        m.fotoUrl = campoFoto.getValue().trim();

        if (ArchivoUtil.guardarMascotas(mascotas)) {
            Notification.show("Se guardó " + m.nombre);
            dialogo.close();
            actualizarTabla();
        } else {
            Notification.show("No se pudo guardar el archivo de mascotas");
        }
    }

    // El id de una mascota nueva es el mayor id que exista + 1
    public int siguienteId() {
        int mayor = 0;
        for (Mascota m : mascotas) {
            if (m.id > mayor) {
                mayor = m.id;
            }
        }
        return mayor + 1;
    }

    // ===================== Eliminar =====================

    // Antes de borrar, pregunta en un diálogo y avisa qué más se va a borrar
    public void confirmarEliminar(Mascota m) {
        int registros = 0;
        for (RegistroMedico r : ArchivoUtil.leerHistorial()) {
            if (r.idMascota == m.id) registros++;
        }
        int solicitudes = 0;
        for (Solicitud s : ArchivoUtil.leerSolicitudes()) {
            if (s.idMascota == m.id) solicitudes++;
        }

        Dialog confirmacion = new Dialog();
        confirmacion.setHeaderTitle("¿Eliminar a " + m.nombre + "?");
        confirmacion.add(new Paragraph("También se borrarán sus " + registros + " registros médicos y "
                + solicitudes + " solicitudes de adopción. Esta acción no se puede deshacer."));

        Button cancelar = new Button("Cancelar", e -> confirmacion.close());
        Button eliminar = new Button("Sí, eliminar", e -> {
            eliminarMascota(m);
            confirmacion.close();
        });
        eliminar.addThemeVariants(ButtonVariant.ERROR);
        confirmacion.getFooter().add(cancelar, eliminar);
        confirmacion.open();
    }

    // Borra la mascota y, para no dejar datos sueltos, su historial médico y sus solicitudes
    public void eliminarMascota(Mascota m) {
        List<RegistroMedico> historial = new ArrayList<>();
        for (RegistroMedico r : ArchivoUtil.leerHistorial()) {
            if (r.idMascota != m.id) historial.add(r);
        }
        List<Solicitud> solicitudes = new ArrayList<>();
        for (Solicitud s : ArchivoUtil.leerSolicitudes()) {
            if (s.idMascota != m.id) solicitudes.add(s);
        }

        mascotas.remove(m);
        boolean guardado = ArchivoUtil.guardarMascotas(mascotas)
                && ArchivoUtil.guardarHistorial(historial)
                && ArchivoUtil.guardarSolicitudes(solicitudes);

        if (guardado) {
            Notification.show("Se eliminó " + m.nombre);
        } else {
            Notification.show("No se pudieron guardar los cambios");
        }
        actualizarTabla();
    }
}
