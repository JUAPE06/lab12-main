package com.ucv.lab12.service;

import com.ucv.lab12.model.Videojuego;
import java.util.List;

public interface IVideojuegoService {
    List<Videojuego> listar();
    List<Videojuego> buscar(String nombre, String genero, String distribuidor);
    void crear(Videojuego videojuego);
    void actualizar(Videojuego videojuego);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(Videojuego videojuego);
}
