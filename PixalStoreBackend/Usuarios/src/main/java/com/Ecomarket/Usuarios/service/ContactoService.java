package com.Ecomarket.Usuarios.service;

import com.Ecomarket.Usuarios.model.Contactos;
import com.Ecomarket.Usuarios.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    // --------- CONTACTOS ---------
    public List<Contactos> listarContactos() {
        return contactoRepository.findAll();
    }

    public Contactos crearContacto(Contactos contacto) {
        return contactoRepository.save(contacto);
    }

   
}

    