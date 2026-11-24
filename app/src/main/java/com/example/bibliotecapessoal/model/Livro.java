package com.example.bibliotecapessoal.model;

import java.io.Serializable;

/**
 * MODEL (camada de modelo / entidade).
 *
 * Representa UM livro da biblioteca. É um objeto simples (POJO) que só guarda dados:
 * não acessa banco nem tela. Cada objeto Livro corresponde a UMA linha da tabela
 * "livros" no banco SQLite (ver DbHelper).
 *
 * Implementa Serializable para poder ser enviado de uma tela (Activity) para outra
 * dentro de um Intent — é assim que a lista manda o livro clicado para a tela de edição.
 */
public class Livro implements Serializable {

    // Número de versão da serialização. Exigido pelo Java ao usar Serializable.
    private static final long serialVersionUID = 1L;

    // ---------- Atributos (cada um vira uma coluna no banco) ----------
    private long id;         // Identificador único, gerado automaticamente pelo banco
    private String titulo;   // Título do livro (obrigatório)
    private String autor;    // Autor do livro (obrigatório)
    private int ano;         // Ano de publicação (obrigatório)
    private boolean lido;    // true = já foi lido; false = ainda não lido
    private Integer nota;    // Nota de 0 a 10. É "Integer" (e não "int") para poder ser null = sem nota

    /** Construtor vazio: cria um livro sem dados (útil para preencher depois com os setters). */
    public Livro() {
    }

    /** Construtor completo: cria um livro já com todos os dados preenchidos. */
    public Livro(long id, String titulo, String autor, int ano, boolean lido, Integer nota) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.lido = lido;
        this.nota = nota;
    }

    // ---------- Getters e Setters ----------
    // Métodos de acesso: "get" lê o valor do atributo e "set" altera o valor.
    // Os atributos são private (encapsulamento), então só podem ser acessados por aqui.

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    /** Para boolean a convenção Java é "is" em vez de "get". */
    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}
