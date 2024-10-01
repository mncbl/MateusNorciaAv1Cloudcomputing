package Ibmec.edu.br.AP1.controller;

import Ibmec.edu.br.AP1.model.Endereco;
import Ibmec.edu.br.AP1.repository.ClienteRepository;
import Ibmec.edu.br.AP1.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import Ibmec.edu.br.AP1.service.Ap1Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import Ibmec.edu.br.AP1.model.Cliente;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ap1")
public class Ap1Controller {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRository;
    @Autowired
    private Ap1Service service;
    //ve todos os clientes
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getAp1(){

        return new ResponseEntity<>(service.getallclients(), HttpStatus.OK);
    }
    //publica cliente
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> saveclient(@Valid @RequestBody Cliente ap1) throws Exception {
        try {
            // Chama a função que faz as verificações
            Cliente clienteValidado = service.criaUsuario(ap1);

            // Salva o cliente no banco de dados
            this.clienteRepository.save(clienteValidado);

            // Retorna o cliente salvo e o status CREATED
            return new ResponseEntity<>(clienteValidado, HttpStatus.CREATED);

        } catch (Exception e) {
            // Retorna um erro com a mensagem de exceção e status BAD_REQUEST
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //editacliente
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable("id") Integer id, @Valid @RequestBody Cliente novosDados) throws Exception {

        ResponseEntity<Cliente> clienteExistente = service.getCliente(id);

        if (clienteExistente.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Atualiza o cliente com os novos dados
        Cliente clienteAtualizado = service.updateCliente(id, novosDados);

        return new ResponseEntity<>(clienteAtualizado, HttpStatus.OK);
    }
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity deleteCliente(@PathVariable("id")int id){
        Optional<Cliente> optCliente = this.clienteRepository.findById(id);
        if(optCliente.isPresent()==false)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        this.clienteRepository.delete(optCliente.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // Cria endereço

    @PostMapping("/endereco/{id}")
    public ResponseEntity<Endereco> saveEndereco(@PathVariable("id") int idEnd, @Valid @RequestBody Endereco ap1enderec) throws Exception {
        // Verifica se o cliente existe
        Optional<Cliente> optCliente = this.clienteRepository.findById(idEnd);

        if (!optCliente.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Cliente cliente = optCliente.get();

        // Associar o endereço ao cliente
        ap1enderec.setCliente(cliente);

        // Adicionar o endereço à lista de endereços do cliente
        cliente.getEnderecos().add(ap1enderec);

        // Salva o cliente com o novo endereço (antes do endereço para evitar inconsistencia)
        this.clienteRepository.save(cliente);

        // Tenta criar o endereço utilizando a função que valida e salva
        try {
            Endereco enderecoSalvo = service.criaEndereco(ap1enderec);
            return new ResponseEntity<>(enderecoSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Tratar erro caso o estado ou CEP sejam inválidos
        }
    }
    // Edita endereço
    @PutMapping("/clientes/{clienteId}/enderecos/{enderecoId}")
    public ResponseEntity<Endereco> updateEndereco(
            @PathVariable("clienteId") int clienteId,
            @PathVariable("enderecoId") int enderecoId,
            @Valid @RequestBody Endereco novosDadosEndereco) {

        // Verifica se o cliente existe
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        if (!clienteOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Cliente não encontrado
        }

        // Verifica se o endereço existe
        Optional<Endereco> enderecoOpt = enderecoRository.findById(enderecoId);
        if (!enderecoOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Endereço não encontrado
        }

        // Acessa o endereço existente
        Endereco enderecoExistente = enderecoOpt.get();

        // Atualiza as informações do endereço
        enderecoExistente.setRua(novosDadosEndereco.getRua());
        enderecoExistente.setNumero(novosDadosEndereco.getNumero());
        enderecoExistente.setBairro(novosDadosEndereco.getBairro());
        enderecoExistente.setCidade(novosDadosEndereco.getCidade());
        enderecoExistente.setEstado(novosDadosEndereco.getEstado());
        enderecoExistente.setCep(novosDadosEndereco.getCep());

        // Atualiza o cliente associado
        enderecoExistente.setCliente(clienteOpt.get());

        // Salva as alterações no repositório
        enderecoRository.save(enderecoExistente);

        return new ResponseEntity<>(enderecoExistente, HttpStatus.OK); // Retorna o endereço atualizado
    }


    @DeleteMapping("/endereco/{id}")
    public ResponseEntity<Void> removeEndereco(@PathVariable("id") int id) throws Exception {
        // Busca o endereço a ser removido
        Optional<Endereco> enderecoOptional = enderecoRository.findById(id); // Usando findById do repositório

        // Verifica se o endereço existe
        if (!enderecoOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Deleta o endereço
        enderecoRository.delete(enderecoOptional.get()); // Deleta o endereço encontrado
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }





}
