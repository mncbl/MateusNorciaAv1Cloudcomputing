package Ibmec.edu.br.AP1.controler;

import Ibmec.edu.br.AP1.controller.Ap1Controller;
import Ibmec.edu.br.AP1.model.Cliente;
import Ibmec.edu.br.AP1.model.Endereco;
import Ibmec.edu.br.AP1.service.Ap1Service;
import Ibmec.edu.br.AP1.repository.ClienteRepository;
import Ibmec.edu.br.AP1.repository.EnderecoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = Ap1Controller.class)
public class ControlerTest {

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private EnderecoRepository enderecoRository;

    @MockBean
    private Ap1Service service;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    // ======= Testes para Clientes =======

    // Teste para GET /ap1/clientes
    @Test
    public void testGetAllClientes() throws Exception {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1);
        cliente1.setName("Cliente Teste 1");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2);
        cliente2.setName("Cliente Teste 2");

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        // Simula o comportamento do service
        Mockito.when(service.getallclients()).thenReturn(clientes);

        mvc.perform(get("/ap1/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Cliente Teste 1")))
                .andExpect(jsonPath("$[1].name", is("Cliente Teste 2")));
    }

    // Teste para POST /ap1/clientes
    @Test
    public void testCreateCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setName("Novo Cliente");
        cliente.setEmail("novo.cliente@email.com");
        cliente.setCpf("12345678909");
        cliente.setDate(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("(12)345678901");

        // Simula o comportamento do service
        Mockito.when(service.criaUsuario(any(Cliente.class))).thenReturn(cliente);

        mvc.perform(post("/ap1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Novo Cliente")))
                .andExpect(jsonPath("$.email", is("novo.cliente@email.com")));
    }

    // Teste para PUT /ap1/clientes/{id}
    @Test
    public void testUpdateCliente() throws Exception {
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setId(1);
        clienteAtualizado.setName("Cliente Atualizado");
        clienteAtualizado.setEmail("cliente.atualizado@email.com");
        clienteAtualizado.setCpf("12345678909");
        clienteAtualizado.setDate(LocalDate.of(1990, 1, 1));
        clienteAtualizado.setTelefone("(12)345678901");

        // Simula o comportamento do service
        Mockito.when(service.getCliente(anyInt())).thenReturn(ResponseEntity.ok(clienteAtualizado));
        Mockito.when(service.updateCliente(anyInt(), any(Cliente.class))).thenReturn(clienteAtualizado);

        mvc.perform(put("/ap1/clientes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cliente Atualizado")))
                .andExpect(jsonPath("$.email", is("cliente.atualizado@email.com")));
    }

    // Teste para DELETE /ap1/clientes/{id}
    @Test
    public void testDeleteCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setName("Cliente a ser deletado");

        // Simula o comportamento do repositório
        Mockito.when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        Mockito.doNothing().when(clienteRepository).delete(cliente);

        mvc.perform(delete("/ap1/clientes/{id}", 1))
                .andExpect(status().isNoContent());
    }

    // ======= Testes para Endereços =======

    // Teste para POST /ap1/endereco/{id}
    @Test
    public void testCreateEndereco() throws Exception {
        int clienteId = 1;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setName("Cliente Teste");
        cliente.setEmail("cliente.teste@email.com");
        cliente.setCpf("12345678909");
        cliente.setDate(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("(12)345678901");

        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setRua("Avenida Brasil");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Rio de Janeiro");
        endereco.setEstado("RJ");
        endereco.setCep("12345678");
        endereco.setCliente(cliente);

        // Simula o comportamento do repositório
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        Mockito.when(service.criaEndereco(any(Endereco.class))).thenReturn(endereco);

        mvc.perform(post("/ap1/endereco/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(endereco)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rua", is("Avenida Brasil")))
                .andExpect(jsonPath("$.estado", is("RJ")))
                .andExpect(jsonPath("$.cep", is("12345678")));
    }

    // Teste para PUT /ap1/clientes/{clienteId}/enderecos/{enderecoId}
    @Test
    public void testUpdateEndereco() throws Exception {
        int clienteId = 1;
        int enderecoId = 1;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setName("Cliente Teste");
        cliente.setEmail("cliente.teste@email.com");
        cliente.setCpf("12345678909");
        cliente.setDate(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("(12)345678901");

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(enderecoId);
        enderecoExistente.setRua("Avenida Brasil");
        enderecoExistente.setNumero("123");
        enderecoExistente.setBairro("Centro");
        enderecoExistente.setCidade("Rio de Janeiro");
        enderecoExistente.setEstado("RJ");
        enderecoExistente.setCep("12345678");
        enderecoExistente.setCliente(cliente);

        Endereco novosDadosEndereco = new Endereco();
        novosDadosEndereco.setRua("Avenida Atlântica");
        novosDadosEndereco.setNumero("456");
        novosDadosEndereco.setBairro("Copacabana");
        novosDadosEndereco.setCidade("Rio de Janeiro");
        novosDadosEndereco.setEstado("RJ");
        novosDadosEndereco.setCep("87654321");

        // Simula o comportamento do repositório
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        Mockito.when(enderecoRository.findById(enderecoId)).thenReturn(Optional.of(enderecoExistente));
        Mockito.when(service.updateEndereco(anyInt(), any(Endereco.class))).thenReturn(enderecoExistente);

        mvc.perform(put("/ap1/clientes/{clienteId}/enderecos/{enderecoId}", clienteId, enderecoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(novosDadosEndereco)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rua", is("Avenida Atlântica")))
                .andExpect(jsonPath("$.bairro", is("Copacabana")))
                .andExpect(jsonPath("$.cep", is("87654321")));
    }

    // Teste para DELETE /ap1/endereco/{id}
    @Test
    public void testDeleteEndereco() throws Exception {
        int enderecoId = 1;

        Endereco endereco = new Endereco();
        endereco.setId(enderecoId);
        endereco.setRua("Avenida Brasil");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Rio de Janeiro");
        endereco.setEstado("RJ");
        endereco.setCep("12345678");

        // Simula o comportamento do repositório
        Mockito.when(enderecoRository.findById(enderecoId)).thenReturn(Optional.of(endereco));
        Mockito.doNothing().when(enderecoRository).delete(endereco);

        mvc.perform(delete("/ap1/endereco/{id}", enderecoId))
                .andExpect(status().isNoContent());
    }

}