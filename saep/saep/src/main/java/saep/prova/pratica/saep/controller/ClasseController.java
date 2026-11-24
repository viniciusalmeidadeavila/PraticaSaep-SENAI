package saep.prova.pratica.saep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import saep.prova.pratica.saep.model.Classe;
import saep.prova.pratica.saep.repository.ClasseRepository;

import java.util.List;

@RestController
@RequestMapping("/itens") // Endereço da API: http://localhost:8080/itens
@CrossOrigin("*")
public class ClasseController {

    @Autowired
    private ClasseRepository repository;

    // 1. LISTAR TODOS
    @GetMapping
    public List<Classe> listar() {
        return repository.findAll();
    }

    // 2. SALVAR (Serve tanto para cadastrar novo quanto para editar)
    @PostMapping
    public Classe salvar(@RequestBody Classe item) {
        return repository.save(item);
    }

    // 3. DELETAR POR ID
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}