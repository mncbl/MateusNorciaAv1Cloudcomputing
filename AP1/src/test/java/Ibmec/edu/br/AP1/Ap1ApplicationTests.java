package Ibmec.edu.br.AP1;

import Ibmec.edu.br.AP1.service.*;
import Ibmec.edu.br.AP1.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class Ap1ApplicationTests {
	private Ap1Service ap1Service;

	@BeforeEach
	public void setUp() {
		ap1Service = new Ap1Service(); // Inicializa o serviço antes de cada teste
	}

	@Test
	public void testClienteMenorDe18() {
		Cliente cliente = new Cliente(1,"Teste", "teste@gmail.com", "12345678901", LocalDate.parse("2005-12-12"), "12345678901");
		Exception exception = assertThrows(Exception.class, () -> ap1Service.criaUsuario(cliente));
		assertEquals("O cliente deve ter 18 anos ou mais", exception.getMessage());
	}

	@Test
	public void testClienteAcimaDe100() {
		Cliente cliente = new Cliente(1,"Teste", "teste@gmail.com", "12345678901", LocalDate.parse("1920-12-12"), "12345678901");
		Exception exception = assertThrows(Exception.class, () -> ap1Service.criaUsuario(cliente));
		assertEquals("O cliente tem idade inválida", exception.getMessage());
	}

	@Test
	public void testEmailDuplicado() throws Exception {
		Cliente cliente1 = new Cliente("Teste1", "teste@gmail.com", "12345678901", LocalDate.parse("2000-12-12"), "12345678901");
		ap1Service.criaUsuario(cliente1);

		Cliente cliente2 = new Cliente("", "teste@gmail.com", "98765432101", LocalDate.parse("1990-12-12"), "98765432101");
		Exception exception = assertThrows(Exception.class, () -> ap1Service.criaUsuario(cliente2));
		assertEquals("Email já existe, tente um novo", exception.getMessage());
	}

	@Test
	public void testCpfDuplicado() throws Exception {
		Cliente cliente1 = new Cliente("Teste1", "teste1@gmail.com", "12345678901", LocalDate.parse("2000-12-12"), "12345678901");
		ap1Service.criaUsuario(cliente1);

		Cliente cliente2 = new Cliente("Teste2", "teste2@gmail.com", "12345678901", LocalDate.parse("1990-12-12"), "98765432101");
		Exception exception = assertThrows(Exception.class, () -> ap1Service.criaUsuario(cliente2));
		assertEquals("CPF já existe, tente um novo", exception.getMessage());
	}
//	@Test esse ja esta garantido pela @CPF DO MODEL
// 	public void testCpfinvalido() throws Exception {
//		Cliente cliente1 = new Cliente("Teste1", "teste1@gmail.com", "111.111.111-11", LocalDate.parse("2000-12-12"), "12345678901");
//		Exception exception = assertThrows(Exception.class, () -> ap1Service.criaUsuario(cliente1));
//		assertEquals("coloque um cpf valido", exception.getMessage());
//
//
//	}

	@Test
	public void testaCriacaoUsuarioComSucesso() throws Exception {
		Cliente cliente = new Cliente("MAasdasdteus", "novomateus@gmail.com", "46224302820", LocalDate.parse("2000-12-12"), "12345678902");
		Cliente resultado = ap1Service.criaUsuario(cliente);

		assertEquals(cliente.getName(), resultado.getName());
		assertEquals(cliente.getEmail(), resultado.getEmail());
		assertEquals(cliente.getCpf(), resultado.getCpf());
		assertEquals(cliente.getDate(), resultado.getDate());
		assertEquals(cliente.getTelefone(), resultado.getTelefone());
	}

	@Test
	public void  NomeMenorQue3Carac() throws Exception {
		Cliente cliente = new Cliente("No", "mateasdassadasdasdadadase@gmail.com", "12345678902", LocalDate.parse("2000-12-12"), "12345678902");
		Cliente resultado = ap1Service.criaUsuario(cliente);
		Exception exception = assertThrows(Exception.class, () -> ap1Service.criaUsuario(cliente));
		assertExceptions("nome menor que 3 caracteres");


	}
}



