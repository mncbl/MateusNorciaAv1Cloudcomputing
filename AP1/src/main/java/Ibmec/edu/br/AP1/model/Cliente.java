package Ibmec.edu.br.AP1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data // Gera os getters, setters, toString, equals e hashCode
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Campo nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter no minimo 3 caracteres e no maximo 100")
    @Column
    private String name;

    @Column
    @NotBlank(message = "Campo email é obrigatório")
    @Email(message = "Campo email não está no formato correto")
    private String email;

    @Column
    @NotBlank(message = "Campo CPF é obrigatório")
    @CPF(message = "Coloque um CPF válido")
    private String cpf;

    @Column
    @NotNull(message = "Campo data é obrigatório")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column
    @Pattern(regexp = "^\\(\\d{2}\\)\\d{9}$", message = "Número de telefone inválido. O formato deve ser (XX)XXXXXXXXX.")
    private String telefone;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnoreProperties("cliente") // Ignora o cliente no lado Endereco
    private List<Endereco> enderecos = new ArrayList<>();
}
