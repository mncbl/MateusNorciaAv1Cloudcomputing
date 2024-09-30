package Ibmec.edu.br.AP1.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Generated;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CPF;




@Data
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private int id;

    @NotBlank(message = "Campo rua é obrigatório")
    @Size(min = 3, max = 255, message = "Campo rua deve ter no minimo 3 caracteres e no maximo 255")
    @Column
    private String rua;

    @NotBlank(message = "Campo numero é obrigatório")
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String numero;

    @NotBlank(message = "Campo bairro é obrigatório ")
    @Column
    @Size(min = 3, max = 100, message = "Campo bairro deve ter no minimo 3 caracteres e no maximo 100")
    private String bairro;

    @NotBlank(message = "Campo cidade é obrigatório ")
    @Column
    @Size(min = 3, max = 100, message = "Campo cidade deve ter no minimo 3 caracteres e no maximo 100")
    private String cidade;

    @NotBlank(message = "Campo estado é obrigatório ")
    @Column
    private String estado;

    @NotNull(message = "Campo CEP é Obrigatório")
    @Pattern(regexp = "^\\d{8}$", message = "O CEP deve ter 8 digitos, SEM NENHUM PONTO OU TRAÇO")
    @Column
    private String cep;


    @NotBlank(message = "Campo cpf do responsavel é obrigatório")
    @CPF(message = "coloque um cpf valido")
    @Column
    private String cpf_do_responsavel;
}
