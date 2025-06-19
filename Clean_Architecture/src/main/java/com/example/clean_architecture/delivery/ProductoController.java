package com.example.clean_architecture.delivery;

import com.example.clean_architecture.application.ListarProductosUseCase;
import com.example.clean_architecture.application.ProductoService;
import com.example.clean_architecture.model.Producto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;
    private final ListarProductosUseCase listarProductosUseCase;

    public ProductoController(ProductoService productoService,  ListarProductosUseCase listarProductosUseCase) {
        this.productoService = productoService;
        this.listarProductosUseCase = listarProductosUseCase;
    }

    @GetMapping
    public List<Producto> obtenerProductos() {
        return productoService.listarProductos();
    }


    @GetMapping("/{id}")
    public Producto obtenerProducto(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }
}
