package Ibmec.edu.br.AP1.service;

import Ibmec.edu.br.AP1.model.Cliente;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class Ap1Service {
    private static List<Cliente> Ap1s = new ArrayList<>();

    public List<Cliente> getallclients(){
        return Ap1Service.Ap1s;
    }
    public Cliente getCliente(String cpf) {
        return findCliente(cpf);
    }

    public Cliente criaUsuario(Cliente client) throws Exception {
        LocalDate hoje = LocalDate.now(); // pega a data atual
        LocalDate idadeLimite = hoje.minus(Period.ofYears(18)); // calculo Para verificar se é maior de 18 anos
        LocalDate idadeMaxima = hoje.minus(Period.ofYears(100));// calculo para verificar < 100 anos


        //esta fora do loop pois no primeiro POST ela estava sendo ignorada
        //verifica idade > 18
        if (client.getDate().isAfter(idadeLimite)) {
            throw  new Exception("O cliente deve ter 18 anos");

        }
        // verifica se idade do cliente é valida
        if (client.getDate().isBefore(idadeMaxima)){
            throw  new Exception("O cliente tem idade invalida");
        }

        for (Cliente clients : Ap1s){
            //Verifica se o email é unico
            if (clients.getEmail().equals(client.getEmail())){
                throw new Exception ("Email já existe, tente um novo");

            }
            //Verifica se o CPF é unico
            if (clients.getCpf().equals(client.getCpf())){
                throw new Exception("Cpf já existe, tente um novo");

            }

        }
        Ap1Service.Ap1s.add(client);
        return client;
    }


    public Cliente updateCliente(String cpf, Cliente newCliente) {
        Cliente ClienteSerAtualizado = findCliente(cpf);

        if (ClienteSerAtualizado == null)
            return null;


        //Atualiza o item
        Ap1s.remove(ClienteSerAtualizado);

        ClienteSerAtualizado.setName(newCliente.getName());
        ClienteSerAtualizado.setEmail(newCliente.getEmail());
        ClienteSerAtualizado.setCpf(newCliente.getCpf());
        ClienteSerAtualizado.setDate(newCliente.getDate());
        ClienteSerAtualizado.setTelefone(newCliente.getTelefone());

        Ap1s.add(ClienteSerAtualizado);

        return ClienteSerAtualizado;

    }
    private Cliente findCliente(String cpf) {
        Cliente response = null;

        for (Cliente clients : Ap1s) {
            if (clients.getCpf().equals(cpf)) {
                response = clients;
                break;
            }
        }
        return response;
    }
}
