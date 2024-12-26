package com.sansu.libraryfilter.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
