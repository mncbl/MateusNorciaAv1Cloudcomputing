package Ibmec.edu.br.AP1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Ibmec.edu.br.AP1.model.Cliente;
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}
