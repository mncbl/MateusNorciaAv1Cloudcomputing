package Ibmec.edu.br.AP1.model;

import jakarta.validation.constraints.*;


import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Cliente {

    @NotBlank(message = "Campo nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter no minimo 3 caracteres e no maximo 100")
    private String name;


    @NotBlank(message = "Campo email é obrigatório")
    @Email(message = "Campo email não esta no formato correto")
    private String email;

    @NotBlank(message = "Campo cpf é obrigatório")
//    @Size(min = 11, max = 11, message = "O cpf deve ter 11 digitos")
    @CPF(message = "coloque um cpf valido")
    private String cpf;

    @NotNull(message = "Campo data é obrigatório")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}


