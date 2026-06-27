package com.ucv.lab12.service;

import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.repository.IVideojuegoRepository;

import java.util.List;

public class VideojuegoService implements IVideojuegoService {

    private final IVideojuegoRepository repository;

    public VideojuegoService(IVideojuegoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Videojuego> listar() {
        return repository.findAll();
    }

    @Override
    public List<Videojuego> buscar(String nombre, String genero, String distribuidor) {
        return repository.findByFilters(nombre, genero, distribuidor);
    }

    @Override
    public void crear(Videojuego videojuego) {
        validar(videojuego);
        repository.save(videojuego);
    }

    @Override
    public void actualizar(Videojuego videojuego) {
        validar(videojuego);
        repository.update(videojuego);
    }

    @Override
    public void eliminar(int id) {
        repository.delete(id);
    }

    @Override
    public void eliminarSeleccionados(List<Integer> ids) {
        repository.deleteAll(ids);
    }

    @Override
    public void validar(Videojuego v) {
        validarObligatorio(v.getConsola(), "La Consola es obligatoria.");
        validarObligatorio(v.getNombre(), "El Nombre es obligatorio.");
        validarLongitud(v.getConsola(), "La Consola");
        validarLongitud(v.getNombre(), "El Nombre");
        validarLongitud(v.getGenero(), "El Genero");
        validarLongitud(v.getClasificacion(), "La Clasificacion");
        validarLongitud(v.getDescripcion(), "La Descripcion");

        if (v.getIdDesarrollador() <= 0) {
            throw new IllegalArgumentException("El ID de desarrollador debe ser mayor que cero.");
        }
        if (v.getIdDistribuidor() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un distribuidor.");
        }
    }

    private void validarObligatorio(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validarLongitud(String value, String fieldName) {
        if (value != null && value.trim().length() > 45) {
            throw new IllegalArgumentException(fieldName + " no puede superar 45 caracteres.");
        }
    }
}
