package com.Ecomarket.Usuarios.controller;

import com.Ecomarket.Usuarios.model.Contactos;
import com.Ecomarket.Usuarios.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacto")
public class Re_De_So_Controller {

    @Autowired
    private ContactoService service;

    
    @PostMapping("/crear")
    public ResponseEntity<Contactos> crearContacto(@RequestBody Contactos contacto) {
        return ResponseEntity.ok(service.crearContacto(contacto));
    }
}
