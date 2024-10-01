package Ibmec.edu.br.AP1.service;
import Ibmec.edu.br.AP1.model.Cliente;
import Ibmec.edu.br.AP1.model.Endereco;
import Ibmec.edu.br.AP1.repository.ClienteRepository;
import Ibmec.edu.br.AP1.repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ServiceTest {

    @InjectMocks
    private Ap1Service ap1Service; // Injeção da classe a ser testada

    @Mock
    private ClienteRepository clienteRepository; // Mock do repositório de clientes

    @Mock
    private EnderecoRepository enderecoRepository; // Mock do repositório de endereços

    private Cliente cliente;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);

        // Configura cliente de exemplo
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setName("Teste Cliente");
        cliente.setEmail("teste@teste.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDate(LocalDate.of(1990, 1, 1));

        // Configura endereço de exemplo
        endereco = new Endereco();
        endereco.setId(1);
        endereco.setEstado("SP");
        endereco.setCep("22040042");
    }

    @Test
    void testCriaUsuarioValido() throws Exception {
        // Simula o comportamento do repositório para não encontrar clientes com o mesmo e-mail ou CPF
        when(clienteRepository.findAll()).thenReturn(Arrays.asList());

        // Chama o método do serviço
        Cliente clienteCriado = ap1Service.criaUsuario(cliente);

        // Verifica se o cliente foi criado corretamente
        assertNotNull(clienteCriado);
        assertEquals("Teste Cliente", clienteCriado.getName());
    }

    @Test
    void testCriaUsuarioComEmailDuplicado() {
        // Simula o comportamento do repositório para retornar um cliente com o mesmo e-mail
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

        Cliente clienteNovo = new Cliente();
        clienteNovo.setEmail("teste@teste.com");

        Exception exception = assertThrows(Exception.class, () -> {
            ap1Service.criaUsuario(clienteNovo);
        });

        // Verifica se a exceção lançada é a correta
        assertEquals("E-mail já existe, tente um novo.", exception.getMessage());
    }

    @Test
    void testGetAllClients() {
        // Simula o comportamento do repositório para retornar uma lista de clientes
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

        // Chama o método do serviço
        var clientes = ap1Service.getallclients();

        // Verifica se a lista retornada contém o cliente de exemplo
        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        assertEquals("Teste Cliente", clientes.get(0).getName());
    }

    @Test
    void testGetClienteById() {
        // Simula o comportamento do repositório para retornar o cliente com o ID fornecido
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        // Chama o método do serviço
        ResponseEntity<Cliente> response = ap1Service.getCliente(1);

        // Verifica se o cliente retornado é o correto
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Teste Cliente", response.getBody().getName());
    }



    @Test
    void testCriaEnderecoValido() throws Exception {
        // Simula o comportamento do repositório
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        // Chama o método do serviço
        Endereco enderecoCriado = ap1Service.criaEndereco(endereco);

        // Verifica se o endereço foi criado corretamente
        assertNotNull(enderecoCriado);
        assertEquals("SP", enderecoCriado.getEstado());
    }



    // Teste para estado inválido na criação do endereço
    @Test
    void testCriaEnderecoEstadoInvalido() {
        Endereco enderecoInvalido = new Endereco();
        enderecoInvalido.setEstado("XX"); // Estado inválido

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ap1Service.criaEndereco(enderecoInvalido);
        });

        String expectedMessage = "A sigla do estado brasileiro está incorreta. Deve ser uma sigla válida em letras maiúsculas.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Teste para CEP inválido na criação do endereço
    @Test
    void testCriaEnderecoCepInvalido() {
        Endereco enderecoInvalido = new Endereco();
        enderecoInvalido.setEstado("SP"); // Estado válido
        enderecoInvalido.setCep("00000000"); // CEP inválido

        Exception exception = assertThrows(Exception.class, () -> {
            ap1Service.verificaCep(enderecoInvalido.getCep());
        });

        String expectedMessage = "CEP inválido";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Teste para cliente com idade superior a 100 anos
    @Test
    void testCriaClienteComIdadeSuperiorA100() {
        Cliente clienteIdadeInvalida = new Cliente();
        clienteIdadeInvalida.setName("Idoso");
        clienteIdadeInvalida.setCpf("123.456.789-01");
        clienteIdadeInvalida.setEmail("idoso@teste.com");
        clienteIdadeInvalida.setDate(LocalDate.now().minusYears(101)); // Idade superior a 100 anos

        Exception exception = assertThrows(Exception.class, () -> {
            ap1Service.criaUsuario(clienteIdadeInvalida);
        });

        String expectedMessage = "O cliente deve ter menos de 100 anos.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Teste para cliente com idade inferior a 18 anos
    @Test
    void testCriaClienteComIdadeInferiorA18() {
        Cliente clienteIdadeInvalida = new Cliente();
        clienteIdadeInvalida.setName("Menor de idade");
        clienteIdadeInvalida.setCpf("987.654.321-01");
        clienteIdadeInvalida.setEmail("menor@teste.com");
        clienteIdadeInvalida.setDate(LocalDate.now().minusYears(17)); // Idade inferior a 18 anos

        Exception exception = assertThrows(Exception.class, () -> {
            ap1Service.criaUsuario(clienteIdadeInvalida);
        });

        String expectedMessage = "O cliente deve ter 18 anos ou mais.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
