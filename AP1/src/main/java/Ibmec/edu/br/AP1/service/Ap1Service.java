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

    public Cliente criaUsuario(Cliente client) throws Exception {
        LocalDate hoje = LocalDate.now();
        LocalDate idadeLimite = hoje.minus(Period.ofYears(18));
        LocalDate idadeMaxima = hoje.plus(Period.ofYears(100));

        for (Cliente clients : Ap1s){
            if (clients.getEmail().equals(client.getEmail())){
                throw new Exception ("Email já existe, tente um novo");

            }
            if (clients.getCpf().equals(client.getCpf())){
                throw new Exception("Cpf já existe, tente um novo");

            }
            if (client.getDate().isAfter(idadeLimite)) {
                throw  new Exception("O cliente deve ter 18 anos");

            }
            if (client.getDate().isBefore(idadeMaxima)){
                throw  new Exception("O cliente tem idade invalida");
            }
        }
        Ap1Service.Ap1s.add(client);
        return client;
    }


}
