package com.example.clean_architecture.repository;

import com.example.clean_architecture.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository {
    List<Producto> listarProductos();
    Optional<Producto> findById(Long id);

    List<Producto> findAll();
}
