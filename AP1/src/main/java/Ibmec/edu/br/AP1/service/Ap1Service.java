package Ibmec.edu.br.AP1.service;

import Ibmec.edu.br.AP1.model.Cliente;
import Ibmec.edu.br.AP1.model.Endereco;
import Ibmec.edu.br.AP1.repository.ClienteRepository;
import Ibmec.edu.br.AP1.repository.EnderecoRepository;
import jakarta.xml.ws.Response;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.*;
import java.util.Optional;

@Service
public class Ap1Service {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRository;

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
        return clienteRepository.findAll();
    }

    public ResponseEntity<Cliente> getCliente(Integer id) {
        return clienteRepository.findById(id)
                .map(cliente -> new ResponseEntity<>(cliente, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    public Endereco getEndereco(int id) {
//        return findEndereco(id);
//    }
//    public List<Endereco> getEnderecoCPF(String cpf_do_responsavel) {
//        return findEnderecosPorCpf(cpf_do_responsavel);
//    }

    public Cliente criaUsuario(Cliente client) throws Exception {
        LocalDate hoje = LocalDate.now(); // pega a data atual
        LocalDate idadeLimite = hoje.minus(Period.ofYears(18)); // verifica se é maior de 18 anos
        LocalDate idadeMaxima = hoje.minus(Period.ofYears(100)); // verifica se é menor de 100 anos

        // Verifica se a idade é maior que 18
        if (client.getDate().isAfter(idadeLimite)) {
            throw new Exception("O cliente deve ter 18 anos ou mais.");
        }

        // Verifica se a idade é menor que 100
        if (client.getDate().isBefore(idadeMaxima)) {
            throw new Exception("O cliente deve ter menos de 100 anos.");
        }

        // Verifica se o e-mail ou CPF já existem no banco de dados
        for (Cliente clients : clienteRepository.findAll()) {
            // Verifica se o e-mail é único
            if (clients.getEmail().equals(client.getEmail())) {
                throw new Exception("E-mail já existe, tente um novo.");
            }

            // Verifica se o CPF é único
            if (clients.getCpf().equals(client.getCpf())) {
                throw new Exception("CPF já existe, tente um novo.");
            }
        }

        // Retorna o cliente validado
        return client;
    }



    public Cliente updateCliente(Integer id, Cliente newCliente) throws Exception {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        if (!clienteOptional.isPresent()) {
            throw new Exception("Cliente com ID " + id + " não encontrado.");
        }

        Cliente clienteSerAtualizado = clienteOptional.get();
        LocalDate hoje = LocalDate.now();
        LocalDate idadeLimite = hoje.minus(Period.ofYears(18)); // Verifica se é maior de 18 anos
        LocalDate idadeMaxima = hoje.minus(Period.ofYears(100)); // Verifica se é menor de 100 anos

        // Verifica idade > 18
        if (newCliente.getDate().isAfter(idadeLimite)) {
            throw new Exception("O cliente deve ter 18 anos ou mais.");
        }

        // Verifica se a idade é < 100
        if (newCliente.getDate().isBefore(idadeMaxima)) {
            throw new Exception("O cliente deve ter menos de 100 anos.");
        }

        // Verifica se o e-mail e o CPF são únicos, excluindo o cliente que está sendo atualizado
        for (Cliente clients : clienteRepository.findAll()) {
            // Verifica se o e-mail é único e pertence a outro cliente
            if (clients.getId() != id && clients.getEmail().equals(newCliente.getEmail())) {
                throw new Exception("E-mail já existe, tente um novo.");
            }

            // Verifica se o CPF é único e pertence a outro cliente
            if (clients.getId() != id && clients.getCpf().equals(newCliente.getCpf())) {
                throw new Exception("CPF já existe, tente um novo.");
            }
        }


        // Atualiza as informações do cliente
        clienteSerAtualizado.setName(newCliente.getName());
        clienteSerAtualizado.setEmail(newCliente.getEmail());
        clienteSerAtualizado.setCpf(newCliente.getCpf());
        clienteSerAtualizado.setDate(newCliente.getDate());
        clienteSerAtualizado.setTelefone(newCliente.getTelefone());

        // Salva o cliente atualizado no banco de dados
        return clienteRepository.save(clienteSerAtualizado);
    }



    private Cliente findCliente(int id) {
        return clienteRepository.findById(id).orElse(null); // Retorna null se não encontrado
    }
//    // ENDEREÇOS
    public Endereco criaEndereco(Endereco enderec) throws Exception {
        // Verifica se o estado é válido
        if (estadosList == null || !estadosList.contains(enderec.getEstado())) {
            throw new IllegalArgumentException("A sigla do estado brasileiro está incorreta. Deve ser uma sigla válida em letras maiúsculas.");
        }

        // Verifica se o CEP é válido
        verificaCep(enderec.getCep());

        // Salva o endereço no repositório
        try {
            return enderecoRository.save(enderec);
        } catch (Exception e) {
            throw new Exception("Erro ao salvar o endereço: " + e.getMessage(), e);
        }
    }




    //
    public Endereco updateEndereco(int id, Endereco newEndereco) throws Exception {
        // Verifica se o endereço existe
        Endereco enderecoSerAtualizado = findEndereco(id);

        if (enderecoSerAtualizado == null) {
            throw new Exception("Endereço com ID " + id + " não encontrado.");
        }

        // Verifica se a sigla do estado é válida
        if (estadosList == null || !estadosList.contains(newEndereco.getEstado())) {
            throw new Exception("A sigla do estado brasileiro está incorreta. Coloque em letras maiúsculas.");
        }

        // Verifica se o CEP é válido
        verificaCep(newEndereco.getCep());

        // Atualiza as informações do endereço
        enderecoSerAtualizado.setRua(newEndereco.getRua());
        enderecoSerAtualizado.setNumero(newEndereco.getNumero());
        enderecoSerAtualizado.setBairro(newEndereco.getBairro());
        enderecoSerAtualizado.setCidade(newEndereco.getCidade());
        enderecoSerAtualizado.setEstado(newEndereco.getEstado());
        enderecoSerAtualizado.setCep(newEndereco.getCep());

        // Atualiza na lista de endereços (ou no repositório, se necessário)
        // Aqui você pode optar por remover o antigo e adicionar o novo, ou apenas atualizar
        enderecoRository.delete(enderecoSerAtualizado); // Remove o endereço anterior, se necessário
        enderecoRository.save(enderecoSerAtualizado); // Adiciona o endereço atualizado

        return enderecoSerAtualizado;
    }


    private Endereco findEndereco(int id) {
        for (Endereco endereco : Ap1enderecos) {
            if (endereco.getId() == id) {
                return endereco; // Retorna o endereço encontrado
            }
        }
        return null; // Retorna null se não encontrar
    }

    public void deleteEndereco(int id) throws Exception {
        Endereco enderecoSerExcluido = findEndereco(id);
        if (enderecoSerExcluido == null) {
            throw new Exception("Endereço não encontrado.");
        }
        Ap1enderecos.remove(enderecoSerExcluido); // Remove o endereço
    }


    //Verifica se CEP existe no documento
    public void verificaCep(String cep) throws Exception {
        String urlCsv = "https://raw.githubusercontent.com/mncbl/MateusNorciaAv1Cloudcomputing/refs/heads/main/AP1/validador_de_df.csv";
        boolean cepEncontrado = false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(urlCsv).openStream()))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] colunas = linha.split(",");

                if (colunas[1].trim().equals(cep)) { // Verifica se o CEP existe
                    cepEncontrado = true;
                    break;
                }
            }
        } catch (IOException e) {
            // Caso ocorra algum erro na leitura do arquivo
            throw new Exception("Erro ao ler o arquivo CSV", e);
        }

        if (!cepEncontrado) {
            throw new Exception("CEP inválido");
        }
    }

}
