package Ibmec.edu.br.AP1.service;

import Ibmec.edu.br.AP1.model.Cliente;
import Ibmec.edu.br.AP1.model.Endereco;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class Ap1Service {
    private static List<Cliente> Ap1clientes = new ArrayList<>();
    private static List<Endereco> Ap1enderecos = new ArrayList<>();
    List<String> estadosList = new ArrayList<>() {{
        add("AC"); // Acre
        add("AL"); // Alagoas
        add("AP"); // Amapá
        add("AM"); // Amazonas
        add("BA"); // Bahia
        add("CE"); // Ceará
        add("DF"); // Distrito Federal
        add("ES"); // Espírito Santo
        add("GO"); // Goiás
        add("MA"); // Maranhão
        add("MT"); // Mato Grosso
        add("MS"); // Mato Grosso do Sul
        add("MG"); // Minas Gerais
        add("PA"); // Pará
        add("PB"); // Paraíba
        add("PR"); // Paraná
        add("PE"); // Pernambuco
        add("PI"); // Piauí
        add("RJ"); // Rio de Janeiro
        add("RN"); // Rio Grande do Norte
        add("RS"); // Rio Grande do Sul
        add("RO"); // Rondônia
        add("RR"); // Roraima
        add("SC"); // Santa Catarina
        add("SP"); // São Paulo
        add("SE"); // Sergipe
        add("TO"); // Tocantins
    }};

    public List<Cliente> getallclients() {
        return Ap1Service.Ap1clientes;
    }

    public Cliente getCliente(String cpf) {
        return findCliente(cpf);
    }

    public Endereco getEndereco(int id) {
        return findEndereco(id);
    }
    public List<Endereco> getEnderecoCPF(String cpf_do_responsavel) {
        return findEnderecosPorCpf(cpf_do_responsavel);
    }

    public Cliente criaUsuario(Cliente client) throws Exception {
        LocalDate hoje = LocalDate.now(); // pega a data atual
        LocalDate idadeLimite = hoje.minus(Period.ofYears(18)); // calculo Para verificar se é maior de 18 anos
        LocalDate idadeMaxima = hoje.minus(Period.ofYears(100));// calculo para verificar < 100 anos


        //esta fora do loop pois no primeiro POST ela estava sendo ignorada
        //verifica idade > 18
        if (client.getDate().isAfter(idadeLimite)) {
            throw new Exception("O cliente deve ter 18 anos");

        }
        // verifica se idade do cliente é valida
        if (client.getDate().isBefore(idadeMaxima)) {
            throw new Exception("O cliente tem idade invalida");
        }

        for (Cliente clients : Ap1clientes) {
            //Verifica se o email é unico
            if (clients.getEmail().equals(client.getEmail())) {
                throw new Exception("Email já existe, tente um novo");

            }
            //Verifica se o CPF é unico
            if (clients.getCpf().equals(client.getCpf())) {
                throw new Exception("Cpf já existe, tente um novo");

            }

        }
        Ap1Service.Ap1clientes.add(client);
        return client;
    }


    public Cliente updateCliente(String cpf, Cliente newCliente) {
        Cliente ClienteSerAtualizado = findCliente(cpf);

        if (ClienteSerAtualizado == null)
            return null;


        //Atualiza o item
        Ap1clientes.remove(ClienteSerAtualizado);

        ClienteSerAtualizado.setName(newCliente.getName());
        ClienteSerAtualizado.setEmail(newCliente.getEmail());
        ClienteSerAtualizado.setCpf(newCliente.getCpf());
        ClienteSerAtualizado.setDate(newCliente.getDate());
        ClienteSerAtualizado.setTelefone(newCliente.getTelefone());

        Ap1clientes.add(ClienteSerAtualizado);

        return ClienteSerAtualizado;

    }

    private Cliente findCliente(String cpf) {
        Cliente response = null;

        for (Cliente clients : Ap1clientes) {
            if (clients.getCpf().equals(cpf)) {
                response = clients;
                break;
            }
        }
        return response;
    }

    // ENDEREÇOS
    public Endereco criaEndereco(Endereco enderec) throws Exception {
        // lista para verificação do estado


        if (!estadosList.contains(enderec.getEstado())) {
            throw new Exception("A Sigla do estado Brasileiro esta incorreta, colocar em letras maiusculas");

        }
        Cliente clienteResponsavel = findCliente(enderec.getCpf_do_responsavel());
        if (clienteResponsavel == null) {
            throw new Exception("CPF do responsável não encontrado na base de clientes.");
        }


        Ap1Service.Ap1enderecos.add(enderec);
        return enderec;
    }

    public Endereco updateEndereco(int id, Endereco newEndereco) throws Exception {
        Endereco EnderecoSerAtualizado = findEndereco(id);
        Cliente clienteResponsavel = findCliente(newEndereco.getCpf_do_responsavel());

        if (EnderecoSerAtualizado == null)
            return null;


        if (!estadosList.contains(newEndereco.getEstado())) {

            throw new Exception("A Sigla do estado Brasileiro está incorreta. Coloque em letras maiúsculas.");

        }

        // Verifica se o CPF do responsável é válido

        if (clienteResponsavel == null) {

            throw  new Exception("CPF do responsável não encontrado na base de clientes.");

        }


        //Atualiza o item
        Ap1enderecos.remove(EnderecoSerAtualizado);


        EnderecoSerAtualizado.setRua(newEndereco.getRua());
        EnderecoSerAtualizado.setNumero(newEndereco.getNumero());
        EnderecoSerAtualizado.setBairro(newEndereco.getBairro());
        EnderecoSerAtualizado.setCidade(newEndereco.getCidade());
        EnderecoSerAtualizado.setEstado(newEndereco.getEstado());
        EnderecoSerAtualizado.setCep(newEndereco.getCep());
        EnderecoSerAtualizado.setCpf_do_responsavel(newEndereco.getCpf_do_responsavel());

        Ap1enderecos.add(EnderecoSerAtualizado);

        return EnderecoSerAtualizado;

    }
    private List<Endereco> findEnderecosPorCpf(String cpf_do_responsavel) {
        List<Endereco> enderecosFiltrados = new ArrayList<>();

        for (Endereco endereco : Ap1enderecos) {
            if (endereco.getCpf_do_responsavel().equals(cpf_do_responsavel)) {
                enderecosFiltrados.add(endereco);
            }
        }

        return enderecosFiltrados;
    }

    private Endereco findEndereco(int id) {
        Endereco response = null;

        for (Endereco endereco : Ap1enderecos) {
            if (endereco.getId() == id) {
                response = endereco;
                break;
            }
        }
        return response;
    }
    //Deleta Endereco
    public void deleteEndereco(int id) throws Exception{
        Endereco  EnderecoSerExcluido = findEndereco(id);

        if (EnderecoSerExcluido == null)
            throw new Exception("Endereço não encontrado");
        Ap1enderecos.remove(EnderecoSerExcluido);
    }

}
