package com.example.bibliotecapessoal.persistencia;

import com.example.bibliotecapessoal.model.Livro;

import java.util.List;

/**
 * PERSISTÊNCIA — Interface do DAO (Data Access Object).
 *
 * Define O QUE pode ser feito com os livros no banco (o "contrato"), sem dizer COMO.
 * A implementação real fica em LivroDAO.
 *
 * Vantagem: as telas dependem só desta interface. Se um dia trocarmos o SQLite por
 * outro tipo de armazenamento (ex.: Room ou uma API), basta criar outra classe que
 * implemente ILivroDAO — as telas não precisam mudar.
 *
 * Contém as operações do CRUD: Create (salvar), Read (listarTodos/contar),
 * Update (atualizar) e Delete (excluir).
 */
public interface ILivroDAO {

    /** Insere um novo livro no banco. Retorna true se deu certo. */
    boolean salvar(Livro livro);

    /** Altera um livro já existente (identificado pelo id). Retorna true se deu certo. */
    boolean atualizar(Livro livro);

    /** Remove o livro com o id informado. Retorna true se deu certo. */
    boolean excluir(long id);

    /** Retorna todos os livros cadastrados, em ordem alfabética de título. */
    List<Livro> listarTodos();

    /**
     * Conta livros via SELECT COUNT(*). Usado na tela de Resumo.
     *
     * @param lido null = todos; true = apenas lidos; false = apenas não lidos.
     */
    int contar(Boolean lido);
}
