package com.ucv.lab12.repository;

import com.ucv.lab12.model.Videojuego;
import java.util.List;

public interface IVideojuegoRepository {
    List<Videojuego> findAll();
    List<Videojuego> findByFilters(String nombre, String genero, String distribuidor);
    void save(Videojuego videojuego);
    void update(Videojuego videojuego);
    void delete(int id);
    void deleteAll(List<Integer> ids);
}
