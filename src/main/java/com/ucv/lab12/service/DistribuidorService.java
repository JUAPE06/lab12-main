package com.ucv.lab12.service;

import com.ucv.lab12.model.Distribuidor;
import com.ucv.lab12.repository.IDistribuidorRepository;

import java.util.List;

public class DistribuidorService implements IDistribuidorService {

    private final IDistribuidorRepository repository;

    public DistribuidorService(IDistribuidorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Distribuidor> listar() {
        return repository.findAll();
    }

    @Override
    public List<Distribuidor> buscar(String razonSocial, String ruc) {
        return repository.findByFilters(razonSocial, ruc);
    }

    @Override
    public void crear(Distribuidor distribuidor) {
        validar(distribuidor);
        repository.save(distribuidor);
    }

    @Override
    public void actualizar(Distribuidor distribuidor) {
        validar(distribuidor);
        repository.update(distribuidor);
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
    public void validar(Distribuidor d) {
        if (d.getRazonSocial() == null || d.getRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("La Razón Social es obligatoria.");
        }
        if (d.getRazonSocial().trim().length() > 150) {
            throw new IllegalArgumentException("La Razón Social no puede superar 150 caracteres.");
        }
        if (d.getRuc() != null && d.getRuc().trim().length() > 20) {
            throw new IllegalArgumentException("El RUC no puede superar 20 caracteres.");
        }
        if (d.getTelefono() != null && d.getTelefono().trim().length() > 20) {
            throw new IllegalArgumentException("El Teléfono no puede superar 20 caracteres.");
        }
        if (d.getEmail() != null && !d.getEmail().trim().isEmpty()) {
            if (!d.getEmail().trim().matches("^[\\w.+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
                throw new IllegalArgumentException("El Email no tiene un formato válido.");
            }
            if (d.getEmail().trim().length() > 100) {
                throw new IllegalArgumentException("El Email no puede superar 100 caracteres.");
            }
        }
        if (d.getDireccion() != null && d.getDireccion().trim().length() > 200) {
            throw new IllegalArgumentException("La Dirección no puede superar 200 caracteres.");
        }
    }
}
