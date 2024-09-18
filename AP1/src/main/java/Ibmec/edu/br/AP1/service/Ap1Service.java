package Ibmec.edu.br.AP1.service;

import Ibmec.edu.br.AP1.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Ap1Service {
    private static List<Cliente> Ap1s = new ArrayList<>();

    public List<Cliente> getallclients(){
        return Ap1Service.Ap1s;
    }

    public Cliente createAp1(Cliente client)  {
        Ap1Service.Ap1s.add(client);
        return client;
    }


}
