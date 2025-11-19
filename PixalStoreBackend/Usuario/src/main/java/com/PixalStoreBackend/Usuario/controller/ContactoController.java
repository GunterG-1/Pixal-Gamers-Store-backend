package com.PixalStoreBackend.Usuario.controller;

import com.PixalStoreBackend.Usuario.model.Contactos;
import com.PixalStoreBackend.Usuario.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {

    @Autowired
    private ContactoService service;

    
    @PostMapping("/crear")
    public ResponseEntity<Contactos> crearContacto(@RequestBody Contactos contacto) {
        return ResponseEntity.ok(service.crearContacto(contacto));
    }
}
