package com.sansu.libraryfilter.principal;

import com.sansu.libraryfilter.model.Datos;
import com.sansu.libraryfilter.model.DatosLibros;
import com.sansu.libraryfilter.service.ConsumoAPI;
import com.sansu.libraryfilter.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    public void muestraElMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        // Top 10 libros descargados
        System.out.println("Top 10 libros más descargados");
        datos.resultados().stream().sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed()).limit(10).map(l -> l.titulo().toUpperCase()).forEach(System.out::println);

        // Búsqueda de libros por nombre
        System.out.println("Ingrese el nombre del libro deseado: ");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?search="+tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream().filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase())).findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado: " + libroBuscado.get());
        }else {
            System.out.println("Libro no encontrado");
        }

        // Estadísticas
        DoubleSummaryStatistics est = datos.resultados().stream().filter(d -> d.numeroDeDescargas() > 0).collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        System.out.println("\nCantidad media de descargas: " + est.getAverage() +"\nCantidad máxima de descargas: " + est.getMax() +"\nCantidad mínima de descargas: "+ est.getMin() +"\nCantidad de registros evaluados: " + est.getCount());


    }
}
