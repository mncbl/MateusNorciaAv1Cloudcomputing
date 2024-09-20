package Ibmec.edu.br.AP1.controller;

import Ibmec.edu.br.AP1.model.Endereco;
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
    //ve todos os clientes
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getAp1(){
        return new ResponseEntity(service.getallclients(), HttpStatus.OK);
    }
    //publica cliente
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> saveclient(@Valid @RequestBody Cliente ap1) throws Exception {

        Cliente response = service.criaUsuario(ap1);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    //editacliente
    @PutMapping("/clientes/{cpf}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable("cpf") String cpf, @Valid @RequestBody Cliente novosDados) {

        Cliente ClienteSerAtualizado = service.getCliente(cpf);

        if (ClienteSerAtualizado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Cliente response = service.updateCliente(cpf, novosDados);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Cria endereço
    @PostMapping("/endereco")
    public ResponseEntity<Endereco> saveEndereco(@Valid @RequestBody Endereco ap1enderec) throws Exception {

        service.verificaCep(ap1enderec.getCep());

        Endereco response = service.criaEndereco(ap1enderec);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // Edita endereço
    @PutMapping("/endereco/{id}")
    public ResponseEntity<Endereco> updateEndereco(@PathVariable("id") int id, @Valid @RequestBody Endereco novosDadosend) throws Exception{
        service.verificaCep(novosDadosend.getCep());
        Endereco EnderecoSerAtualizado = service.getEndereco(id);

        if (EnderecoSerAtualizado == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Endereco responsend = service.updateEndereco(id, novosDadosend);

        return new ResponseEntity<>(responsend,HttpStatus.OK);
    }
    //Delete Todos os clientes
    @DeleteMapping("/endereco/{cpf_do_responsavel}/{id}")
    public  ResponseEntity<Endereco> removeEndereco(@PathVariable("cpf_do_responsavel") String cpf_do_responsavel,@PathVariable("id") int id) throws Exception  {


        if (service.getEndereco(id)==null|| !service.getEndereco(id).getCpf_do_responsavel().equals(cpf_do_responsavel))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        service.deleteEndereco(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
    //Busca e Mostra Infomação dos clientes e seus respectivos enderecos
    @GetMapping("/info/{cpf_do_responsavel}")
    public ResponseEntity<List<Endereco>> imoveisPorCPF(@PathVariable("cpf_do_responsavel") String cpf_do_responsavel) {
        List<Endereco> response = service.getEnderecoCPF(cpf_do_responsavel);

        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
