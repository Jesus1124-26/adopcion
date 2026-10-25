package com.example.adopcion.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.adopcion.model.Adoptante;
import com.example.adopcion.model.Donacion;
import com.example.adopcion.model.Mascota;
import com.example.adopcion.model.RegistroMedico;
import com.example.adopcion.model.Solicitud;

/**
 * Lee y guarda todos los datos del refugio en archivos de texto.
 *
 * Cada tipo de dato tiene su propio .txt en src/main/resources/data/, con una
 * línea por registro y los campos separados por |. Las vistas nunca abren los
 * archivos directamente: siempre pasan por esta clase.
 *
 * Si un archivo no existe, se crea con datos de ejemplo. Si una línea está mal
 * escrita, se salta y se avisa en la consola, sin que la aplicación se caiga.
 */
public class ArchivoUtil {

    private static final String CARPETA = "src/main/resources/data";
    private static final String MASCOTAS = CARPETA + "/mascotas.txt";
    private static final String ADOPTANTES = CARPETA + "/adoptantes.txt";
    private static final String SOLICITUDES = CARPETA + "/solicitudes.txt";
    private static final String HISTORIAL = CARPETA + "/historial_medico.txt";
    private static final String DONACIONES = CARPETA + "/donaciones.txt";

    // ===================== Funciones generales =====================

    // Lee todas las líneas no vacías de un archivo.
    // Si el archivo no existe, primero lo crea con los datos de ejemplo.
    private static List<String> leerLineas(String ruta, String[] datosEjemplo) {
        List<String> lineas = new ArrayList<>();
        try {
            File carpeta = new File(CARPETA);
            if (!carpeta.exists()) carpeta.mkdirs();

            File archivo = new File(ruta);
            if (!archivo.exists()) crearArchivoEjemplo(ruta, datosEjemplo);

            try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (!linea.trim().isEmpty()) {
                        lineas.add(linea);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + ruta + ": " + e.getMessage());
        }
        return lineas;
    }

    // Reescribe el archivo completo con las líneas recibidas
    // (leer todo, cambiar en memoria, reescribir todo). Devuelve true si se pudo guardar.
    private static boolean escribirLineas(String ruta, List<String> lineas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar " + ruta + ": " + e.getMessage());
            return false;
        }
    }

    // Crea un archivo con los datos de ejemplo (solo la primera vez que corre la app)
    private static void crearArchivoEjemplo(String ruta, String[] datosEjemplo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (String linea : datosEjemplo) {
                bw.write(linea);
                bw.newLine();
            }
        }
        System.out.println("Se creó " + ruta + " con datos de ejemplo.");
    }

    // Separa una línea por | y revisa que tenga la cantidad de campos esperada.
    // El -1 hace que split conserve los campos vacíos del final:
    // sin él, "5|2|Pendiente||" daría 3 campos en vez de 5.
    private static String[] separarCampos(String linea, int cantidadCampos) {
        String[] datos = linea.split("\\|", -1);
        if (datos.length != cantidadCampos) {
            throw new IllegalArgumentException("se esperaban " + cantidadCampos + " campos y hay " + datos.length);
        }
        return datos;
    }

    // ===================== Mascotas =====================
    // id|nombre|especie|raza|edad|sexo|tamano|estadoSalud|fotoUrl|estado

    public static List<Mascota> leerMascotas() {
        List<Mascota> mascotas = new ArrayList<>();
        for (String linea : leerLineas(MASCOTAS, MASCOTAS_EJEMPLO)) {
            try {
                String[] d = separarCampos(linea, 10);
                mascotas.add(new Mascota(Integer.parseInt(d[0].trim()), d[1], d[2], d[3],
                        Integer.parseInt(d[4].trim()), d[5], d[6], d[7], d[8], d[9]));
            } catch (Exception e) {
                System.err.println("Línea ignorada en mascotas.txt (" + e.getMessage() + "): " + linea);
            }
        }
        return mascotas;
    }

    public static boolean guardarMascotas(List<Mascota> mascotas) {
        List<String> lineas = new ArrayList<>();
        for (Mascota m : mascotas) {
            lineas.add(m.id + "|" + m.nombre + "|" + m.especie + "|" + m.raza + "|" + m.edad + "|"
                    + m.sexo + "|" + m.tamano + "|" + m.estadoSalud + "|" + m.fotoUrl + "|" + m.estado);
        }
        return escribirLineas(MASCOTAS, lineas);
    }
 
    // ===================== Adoptantes =====================
    // id|documento|nombre|telefono|email|tipoVivienda|otrasMascotas|experiencia|prefEspecie|prefTamano
 
    public static List<Adoptante> leerAdoptantes() {
        List<Adoptante> adoptantes = new ArrayList<>();
        for (String linea : leerLineas(ADOPTANTES, ADOPTANTES_EJEMPLO)) {
            try {
                String[] d = separarCampos(linea, 10);
                adoptantes.add(new Adoptante(Integer.parseInt(d[0].trim()), d[1], d[2], d[3], d[4],
                        d[5], d[6], d[7], d[8], d[9]));
            } catch (Exception e) {
                System.err.println("Línea ignorada en adoptantes.txt (" + e.getMessage() + "): " + linea);
            }
        }
        return adoptantes;
    }
 
    public static boolean guardarAdoptantes(List<Adoptante> adoptantes) {
        List<String> lineas = new ArrayList<>();
        for (Adoptante a : adoptantes) {
            lineas.add(a.id + "|" + a.documento + "|" + a.nombre + "|" + a.telefono + "|" + a.email + "|"
                    + a.tipoVivienda + "|" + a.otrasMascotas + "|" + a.experiencia + "|"
                    + a.preferenciaEspecie + "|" + a.preferenciaTamano);
        }
        return escribirLineas(ADOPTANTES, lineas);
    }
 
    // ===================== Solicitudes =====================
    // id|idMascota|idAdoptante|fechaSolicitud|estado|observacion|fechaRespuesta
 
    public static List<Solicitud> leerSolicitudes() {
        List<Solicitud> solicitudes = new ArrayList<>();
        for (String linea : leerLineas(SOLICITUDES, SOLICITUDES_EJEMPLO)) {
            try {
                String[] d = separarCampos(linea, 7);
                solicitudes.add(new Solicitud(Integer.parseInt(d[0].trim()), Integer.parseInt(d[1].trim()),
                        Integer.parseInt(d[2].trim()), d[3], d[4], d[5], d[6]));
            } catch (Exception e) {
                System.err.println("Línea ignorada en solicitudes.txt (" + e.getMessage() + "): " + linea);
            }
        }
        return solicitudes;
    }
 
    public static boolean guardarSolicitudes(List<Solicitud> solicitudes) {
        List<String> lineas = new ArrayList<>();
        for (Solicitud s : solicitudes) {
            lineas.add(s.id + "|" + s.idMascota + "|" + s.idAdoptante + "|" + s.fechaSolicitud + "|"
                    + s.estado + "|" + s.observacion + "|" + s.fechaRespuesta);
        }
        return escribirLineas(SOLICITUDES, lineas);
    }
 
    // ===================== Historial médico =====================
    // id|idMascota|tipo|fecha|descripcion
 
    public static List<RegistroMedico> leerHistorial() {
        List<RegistroMedico> registros = new ArrayList<>();
        for (String linea : leerLineas(HISTORIAL, HISTORIAL_EJEMPLO)) {
            try {
                String[] d = separarCampos(linea, 5);
                registros.add(new RegistroMedico(Integer.parseInt(d[0].trim()), Integer.parseInt(d[1].trim()),
                        d[2], d[3], d[4]));
            } catch (Exception e) {
                System.err.println("Línea ignorada en historial_medico.txt (" + e.getMessage() + "): " + linea);
            }
        }
        return registros;
    }
 
    public static boolean guardarHistorial(List<RegistroMedico> registros) {
        List<String> lineas = new ArrayList<>();
        for (RegistroMedico r : registros) {
            lineas.add(r.id + "|" + r.idMascota + "|" + r.tipo + "|" + r.fecha + "|" + r.descripcion);
        }
        return escribirLineas(HISTORIAL, lineas);
    }

    // ===================== Donaciones =====================
    // id|donante|tipo|valor|fecha|descripcion

    public static List<Donacion> leerDonaciones() {
        List<Donacion> donaciones = new ArrayList<>();
        for (String linea : leerLineas(DONACIONES, DONACIONES_EJEMPLO)) {
            try {
                String[] d = separarCampos(linea, 6);
                donaciones.add(new Donacion(Integer.parseInt(d[0].trim()), d[1], d[2],
                        Double.parseDouble(d[3].trim()), d[4], d[5]));
            } catch (Exception e) {
                System.err.println("Línea ignorada en donaciones.txt (" + e.getMessage() + "): " + linea);
            }
        }
        return donaciones;
    }

    public static boolean guardarDonaciones(List<Donacion> donaciones) {
        List<String> lineas = new ArrayList<>();
        for (Donacion d : donaciones) {
            lineas.add(d.id + "|" + d.donante + "|" + d.tipo + "|" + d.valor + "|" + d.fecha + "|" + d.descripcion);
        }
        return escribirLineas(DONACIONES, lineas);
    }

    // ===================== Datos de ejemplo =====================
    // Se escriben en los .txt solo si el archivo todavía no existe.

    private static final String[] MASCOTAS_EJEMPLO = {
        "1|Luna|Perro|Golden Retriever|3|Hembra|Grande|Sano|https://images.dog.ceo/breeds/retriever-golden/n02099601_6194.jpg|Disponible",
        "2|Rocky|Perro|Beagle|5|Macho|Mediano|En tratamiento|https://images.dog.ceo/breeds/beagle/n02088364_5282.jpg|En proceso",
        "3|Toby|Perro|Basset Hound|8|Macho|Mediano|Urgente|https://images.dog.ceo/breeds/hound-basset/n02088238_9446.jpg|Disponible",
        "4|Nieve|Perro|Samoyedo|2|Hembra|Grande|Sano|https://images.dog.ceo/breeds/samoyed/n02111889_15038.jpg|Adoptada",
        "5|Chispa|Perro|Fox Terrier|1|Hembra|Pequeño|Sano|https://images.dog.ceo/breeds/terrier-fox/n02095314_2196.jpg|Disponible",
        "6|Michi|Gato|Criollo|2|Macho|Pequeño|Sano|https://cdn2.thecatapi.com/images/jj.jpg|Disponible",
        "7|Pelusa|Gato|Criollo|4|Hembra|Pequeño|Sano|https://cdn2.thecatapi.com/images/be0.jpg|Adoptada",
        "8|Simón|Gato|Criollo|1|Macho|Pequeño|Urgente|https://cdn2.thecatapi.com/images/bin.jpg|Disponible"
    };

    private static final String[] ADOPTANTES_EJEMPLO = {
        "1|1012345678|Laura Martínez|3001234567|laura.martinez@correo.com|Apartamento|No|Sí|Gato|Pequeño",
        "2|52987654|Carlos Ruiz|3109876543|carlos.ruiz@correo.com|Casa con patio|Sí|Sí|Perro|Grande",
        "3|1098765432|Ana Gómez|3205557788|ana.gomez@correo.com|Casa sin patio|No|No|Perro|Mediano",
        "4|80123456|Pedro Salas|3152223344|pedro.salas@correo.com|Apartamento|Sí|No|Cualquiera|Cualquiera"
    };

    private static final String[] SOLICITUDES_EJEMPLO = {
        "1|4|2|2026-09-02|Aprobada|Casa con patio y experiencia con perros grandes|2026-09-10",
        "2|4|3|2026-09-05|Rechazada|Rechazada automáticamente: la mascota fue adoptada|2026-09-10",
        "3|7|1|2026-10-01|Aprobada|Tiene experiencia cuidando gatos|2026-10-08",
        "4|1|4|2026-10-15|Rechazada|Vive en apartamento y la mascota necesita espacio|2026-10-17",
        "5|2|3|2026-10-18|Pendiente||",
        "6|2|4|2026-10-20|Pendiente||"
    };

    private static final String[] HISTORIAL_EJEMPLO = {
        "1|1|Vacuna|2026-03-10|Rabia",
        "2|1|Consulta|2026-08-20|Control general, sin novedades",
        "3|2|Consulta|2026-10-12|Otitis, tratamiento con gotas por 10 días",
        "4|3|Consulta|2026-10-22|Cojera en pata trasera, requiere radiografía urgente",
        "5|3|Vacuna|2025-05-01|Múltiple (parvovirus y moquillo)",
        "6|6|Vacuna|2026-09-15|Triple felina",
        "7|7|Consulta|2026-09-28|Infección respiratoria leve, ya recuperada",
        "8|8|Consulta|2026-10-21|Desnutrición grave, rescatado de la calle"
    };

    private static final String[] DONACIONES_EJEMPLO = {
        "1|Fundación Huellitas|Dinero|500000|2026-09-05|Donación mensual",
        "2|Anónimo|Alimento|120000|2026-09-18|Dos bultos de concentrado para perro",
        "3|María López|Dinero|50000|2026-10-02|",
        "4|Veterinaria San Roque|Medicamentos|200000|2026-10-10|Antiparasitarios y vacunas",
        "5|Anónimo|Dinero|30000|2026-10-20|"
    };
}