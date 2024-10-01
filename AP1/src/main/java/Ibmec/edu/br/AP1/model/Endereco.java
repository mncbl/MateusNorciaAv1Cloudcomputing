package Ibmec.edu.br.AP1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Campo rua é obrigatório")
    @Size(min = 3, max = 255, message = "Campo rua deve ter no minimo 3 caracteres e no maximo 255")
    @Column
    private String rua;

    @NotBlank(message = "Campo número é obrigatório")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Formato inválido para o número")
    private String numero;

    @NotBlank(message = "Campo bairro é obrigatório")
    @Size(min = 3, max = 100, message = "Campo bairro deve ter no minimo 3 caracteres e no maximo 100")
    @Column
    private String bairro;

    @NotBlank(message = "Campo cidade é obrigatório")
    @Size(min = 3, max = 100, message = "Campo cidade deve ter no minimo 3 caracteres e no maximo 100")
    @Column
    private String cidade;

    @NotBlank(message = "Campo estado é obrigatório")
    @Column
    private String estado;

    @NotNull(message = "Campo CEP é obrigatório")
    @Pattern(regexp = "^\\d{8}$", message = "O CEP deve ter 8 dígitos, sem ponto ou traço")
    @Column
    private String cep;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference // Evita a serialização recursiva de Cliente
    private Cliente cliente;
}
