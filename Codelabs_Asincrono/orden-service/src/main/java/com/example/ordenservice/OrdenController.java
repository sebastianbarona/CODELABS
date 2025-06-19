package com.example.ordenservice;

import com.example.ordenservice.rabbitmq.RabbitMQSender;
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
    private RabbitMQSender rabbitMQSender;
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

    @PostMapping("/rb")
    public Orden crearRb(@RequestBody RequestOrden ModelOrden) {
        Orden ordenGuardada = repository.save(ModelOrden.getOrden());
        ModelOrden.setOrden(ordenGuardada);

        rabbitMQSender.enviarCreateDetalle(ModelOrden);

        return ordenGuardada;
    }

}
