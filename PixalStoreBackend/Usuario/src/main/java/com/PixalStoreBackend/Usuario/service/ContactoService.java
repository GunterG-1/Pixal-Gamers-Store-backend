package com.PixalStoreBackend.Usuario.service;

import com.PixalStoreBackend.Usuario.model.Contactos;
import com.PixalStoreBackend.Usuario.repository.ContactoRepository;
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

    