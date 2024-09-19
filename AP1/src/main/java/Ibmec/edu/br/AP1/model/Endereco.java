package Ibmec.edu.br.AP1.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public class Endereco {
    @NotBlank(message = "Campo rua é obrigatório")
    @Size(min = 3, max = 255, message = "Campo rua deve ter no minimo 3 caracteres e no maximo 255")
    private String rua;

    @NotBlank(message = "Campo numero é obrigatório")
    private String numero;

    @NotBlank(message = "Campo bairro é obrigatório ")
    @Size(min = 3, max = 100, message = "Campo rua deve ter no minimo 3 caracteres e no maximo 100")
    private String bairro;

    @NotBlank(message = "Campo cidade é obrigatório ")
    @Size(min = 3, max = 100, message = "Campo cidade deve ter no minimo 3 caracteres e no maximo 100")
    private String cidade;

    @NotBlank(message = "Campo estado é obrigatório ")
    private String estado;

    @NotNull(message = "Campo CEP é Obrigatório")
    @Pattern(regexp = "^d{5}-d{3}$")
    private int cep;
}
