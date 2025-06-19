package com.example.ordenservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orden")
public class OrdenController {

    @Autowired
    private final OrdenRepository repository;
    private final ProductoClient productoClient;

    @Autowired
    public OrdenController(
        OrdenRepository repository, ProductoClient productoClient
    )
    {
        this.repository = repository;
        this.productoClient = productoClient;
    }

    @GetMapping
    public List<Orden> obtenerTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Orden obtenerPorId(@PathVariable int id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Orden crear(@RequestBody RequestOrden ModelOrden) {
        RequestOrden ModelRequestOrden = new RequestOrden();

        if (ModelOrden == null || ModelOrden.getOrden() == null) {
            throw new IllegalArgumentException("El campo 'orden' no puede ser null.");
        }else {
            Orden ordenGuardada = repository.save(ModelOrden.getOrden());
            ModelRequestOrden.setOrden(ordenGuardada);

            List<ProductoModel> lst = new ArrayList<>();

            for (ProductoModel item : ModelRequestOrden.getLstProductos())
            {
                ProductoModel producto = productoClient.obtenerProducto(item.getIdProducto());
                producto.setCantidad(item.getCantidad());
                producto.setPrecio(item.getPrecio());
                lst.add(producto);
            }

            ModelRequestOrden.setLstProductos(lst);

            eliminar(ordenGuardada.getIdOrden());
        }

        return ModelOrden.getOrden();
    }

    @PutMapping("/{idOrden}")
    public Orden actualizarOrden(@PathVariable int idOrden, @RequestBody Orden nuevaOrden) {
        return repository.findById(idOrden)
                .map(ordenExistente -> {
                    ordenExistente.setIdUsuario(nuevaOrden.getIdUsuario());
                    ordenExistente.setEstado(nuevaOrden.getEstado());
                    ordenExistente.setTotal(nuevaOrden.getTotal());
                    ordenExistente.setFechaCreacion(nuevaOrden.getFechaCreacion());

                    Orden ordenActualizada = repository.save(ordenExistente);
                    return ordenActualizada;
                })
                .orElseThrow(() -> new RuntimeException("Orden con ID " + idOrden + " no encontrada"));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
