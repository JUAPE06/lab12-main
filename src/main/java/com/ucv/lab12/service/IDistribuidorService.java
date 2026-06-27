package com.ucv.lab12.service;

import com.ucv.lab12.model.Distribuidor;
import java.util.List;

public interface IDistribuidorService {
    List<Distribuidor> listar();
    List<Distribuidor> buscar(String razonSocial, String ruc);
    void crear(Distribuidor distribuidor);
    void actualizar(Distribuidor distribuidor);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(Distribuidor distribuidor);
}
