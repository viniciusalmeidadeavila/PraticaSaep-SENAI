const URL = "http://localhost:8080/itens";

// 1. BUSCAR NO JAVA E MOSTRAR NA TABELA
async function carregar() {
    const resposta = await fetch(URL);
    const itens = await resposta.json();

    const lista = document.getElementById("lista");
    lista.innerHTML = "";

    itens.forEach(item => {
        lista.innerHTML += `
            <tr>
                <td>${item.id}</td>
                <td>${item.nome}</td>
                <td>R$ ${item.valor}</td>
                <td><button onclick="deletar(${item.id})">Excluir</button></td>
            </tr>
        `;
    });
}

// 2. ENVIAR DO FORMULÁRIO PARA O JAVA
document.getElementById("formulario").addEventListener("submit", async (e) => {
    e.preventDefault(); // Não deixa a página recarregar

    const novoItem = {
        nome: document.getElementById("nome").value,
        valor: document.getElementById("valor").value
    };

    await fetch(URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(novoItem)
    });

    document.getElementById("formulario").reset(); // Limpa os campos
    carregar(); // Atualiza a tabela
});

// 3. PEDIR PRO JAVA DELETAR
async function deletar(id) {
    await fetch(`${URL}/${id}`, { method: "DELETE" });
    carregar(); // Atualiza a tabela
}

// Carrega a lista assim que a página abre
carregar();