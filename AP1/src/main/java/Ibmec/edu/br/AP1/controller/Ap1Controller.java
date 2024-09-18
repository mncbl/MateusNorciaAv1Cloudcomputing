package Ibmec.edu.br.AP1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import Ibmec.edu.br.AP1.service.Ap1Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import Ibmec.edu.br.AP1.model.Cliente;

import java.util.List;

@RestController
@RequestMapping("/ap1")
public class Ap1Controller {
    @Autowired
    private Ap1Service service;

    @GetMapping
    public ResponseEntity<List<Cliente>> getAp1(){
        return new ResponseEntity(service.getallclients(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cliente> saveclient(@Valid @RequestBody Cliente ap1) throws Exception {
        Cliente response = service.createAp1(ap1);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
