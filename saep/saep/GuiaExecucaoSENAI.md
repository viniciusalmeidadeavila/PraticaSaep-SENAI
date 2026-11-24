# 🚀 Guia Definitivo Passo a Passo - Prova Prática SAEP (SENAI)

Este guia serve como seu roteiro durante a prova. Siga a ordem abaixo rigorosamente para garantir que todo o fluxo (Banco de Dados $\rightarrow$ Back-End $\rightarrow$ Front-End) funcione sem erros.

---

## 📌 Checklist Rápido de Verificação Inicial
- [ ] MySQL / XAMPP está **LIGADO** e rodando na porta 3306.
- [ ] O projeto Spring Boot foi aberto na sua IDE (VS Code, IntelliJ ou Eclipse).
- [ ] Os arquivos do Front-end (`index.html`, `style.css`, `script.js`) estão dentro da pasta:
  `src/main/resources/static/`

---

## 🗄️ PASSO 1: O Banco de Dados (MySQL Workbench)

1. Abra o **MySQL Workbench**.
2. Cole e execute o script SQL da prova (clique no ícone do **Raio** ⚡).
3. Verifique o nome do banco criado (exemplo: `clinica` ou `saep_db`).

---

## ⚙️ PASSO 2: Configurar a Conexão (`application.properties`)

Abra o arquivo `src/main/resources/application.properties` e ajuste para o nome do banco e credenciais do SENAI:

```properties
# Ajuste o nome do banco após a barra / (ex: /clinica ou /saep_db)
spring.datasource.url=jdbc:mysql://localhost:3306/clinica?createDatabaseIfNotExist=true&useTimezone=true&serverTimezone=UTC

# Credenciais do MySQL do computador do SENAI
spring.datasource.username=root
spring.datasource.password=

# Cria/Atualiza tabelas automaticamente
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Servidor rodando na porta 8080
server.port=8080
```

---

## ☕ PASSO 3: O Back-End em 3 Camadas Simplificadas

Para cada tabela da prova, você precisará de **3 arquivos básicos**:

### 1. A Entidade (`Model`)
*Caminho: `src/main/java/com/saep/saep/model/NomeDaClasse.java`*

```java
package com.saep.saep.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tutores") // Nome exato da tabela no MySQL
@Data                   // Lombok: gera Getters e Setters sozinho
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String telefone;
}
```

---

### 2. O Repositório (`Repository`)
*Caminho: `src/main/java/com/saep/saep/repository/NomeDaClasseRepository.java`*

```java
package com.saep.saep.repository;

import com.saep.saep.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    // Fica vazio! O JpaRepository já cria salvar, buscar, deletar e listar.
}
```

---

### 3. O Controlador (`Controller`)
*Caminho: `src/main/java/com/saep/saep/controller/NomeDaClasseController.java`*

```java
package com.saep.saep.controller;

import com.saep.saep.model.Tutor;
import com.saep.saep.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutores") // Rota que o JS vai chamar
@CrossOrigin(origins = "*")    // Evita erros de conexão
public class TutorController {

    @Autowired
    private TutorRepository repository;

    // LISTAR TODOS (GET)
    @GetMapping
    public List<Tutor> listar() {
        return repository.findAll();
    }

    // SALVAR OU EDITAR (POST)
    @PostMapping
    public Tutor salvar(@RequestBody Tutor tutor) {
        return repository.save(tutor);
    }

    // DELETAR POR ID (DELETE)
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
```

---

## 🌐 PASSO 4: O Front-End (Conectando tudo com JS)

### 1. HTML (`src/main/resources/static/index.html`)
Mantenha os IDs dos inputs fáceis de lembrar:

```html
<form id="meuFormulario">
    <input type="hidden" id="id">
    <input type="text" id="nome" placeholder="Nome" required>
    <input type="text" id="cpf" placeholder="CPF" required>
    <input type="text" id="telefone" placeholder="Telefone">
    <button type="submit">Salvar</button>
</form>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>CPF</th>
            <th>Telefone</th>
            <th>Ações</th>
        </tr>
    </thead>
    <tbody id="tabelaDados"></tbody>
</table>
```

---

### 2. JavaScript (`src/main/resources/static/script.js`)
Estrutura padrão de Fetch que você pode reutilizar em qualquer tela:

```javascript
// URL exatamente igual ao @RequestMapping do Java
const API_URL = "http://localhost:8080/api/tutores";

// 1. CARREGAR DADOS DA TABELA (GET)
async function carregarTabela() {
    const resposta = await fetch(API_URL);
    const lista = await resposta.json();

    const tbody = document.getElementById("tabelaDados");
    tbody.innerHTML = "";

    lista.forEach(item => {
        tbody.innerHTML += `
            <tr>
                <td>${item.id}</td>
                <td>${item.nome}</td>
                <td>${item.cpf}</td>
                <td>${item.telefone}</td>
                <td>
                    <button onclick="deletar(${item.id})">Excluir</button>
                </td>
            </tr>
        `;
    });
}

// 2. ENVIAR FORMULÁRIO (POST)
document.getElementById("meuFormulario").addEventListener("submit", async (e) => {
    e.preventDefault();

    const objetoSalvar = {
        id: document.getElementById("id").value || null,
        nome: document.getElementById("nome").value,
        cpf: document.getElementById("cpf").value,
        telefone: document.getElementById("telefone").value
    };

    await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(objetoSalvar)
    });

    document.getElementById("meuFormulario").reset();
    carregarTabela();
});

// 3. DELETAR ITEM (DELETE)
async function deletar(id) {
    if (confirm("Deseja realmente excluir?")) {
        await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        carregarTabela();
    }
}

// Inicializa a tabela assim que abre a página
carregarTabela();
```

---

## 🛠️ Resolução de Erros Comuns na Hora da Prova

| Sintoma do Erro | Causa Provável | Como Resolver |
| :--- | :--- | :--- |
| **`ServiceException / Dialect`** no Java | MySQL está desligado ou o banco especificado não existe. | Abra o Workbench/XAMPP, certifique-se de que o servidor está **Running** e crie o banco com `CREATE DATABASE nome_do_banco;`. |
| **`Access denied for user 'root'@'localhost'`** | A senha no `application.properties` está errada. | Descubra a senha do MySQL do SENAI (teste vazia, `root`, `1234` ou `senai`). |
| **Erro 404** no `fetch` do JavaScript | A URL no JS está diferente da anotação `@RequestMapping` no Controller. | Compare caractere por caractere (ex: `/api/tutores` vs `/tutores`). |
| **Campos salvando como `null`** | Os nomes das chaves no JSON do JS estão diferentes dos nomes dos atributos na classe Java. | Se na classe Java está `private String nomeTutor;`, no JS tem que ser `nomeTutor: ...`. |

---

## 🎯 Dica Final para o Sucesso Amanhã
Sempre dê o **Play** no Spring Boot e teste o endereço `http://localhost:8080/api/tutores` direto no navegador antes de mexer no HTML/JS. Se o navegador retornar `[]` ou uma lista em JSON, seu Back-End já está 100% pronto!