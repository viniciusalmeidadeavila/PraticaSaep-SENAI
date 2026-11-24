package saep.prova.pratica.saep.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // Cria getters e setters sozinho
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double valor;
}