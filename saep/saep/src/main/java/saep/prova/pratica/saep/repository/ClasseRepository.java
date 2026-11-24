package saep.prova.pratica.saep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import saep.prova.pratica.saep.model.Classe;

public interface ClasseRepository extends JpaRepository<Classe, Long> {
    // Não precisa escrever nada aqui dentro!
}
