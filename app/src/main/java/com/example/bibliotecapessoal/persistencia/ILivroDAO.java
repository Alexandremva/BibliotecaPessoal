package com.example.bibliotecapessoal.persistencia;

import com.example.bibliotecapessoal.model.Livro;

import java.util.List;

public interface ILivroDAO {

    boolean salvar(Livro livro);

    boolean atualizar(Livro livro);

    boolean excluir(long id);

    List<Livro> listarTodos();

    /**
     * Conta livros via SELECT COUNT(*).
     *
     * @param lido null = todos; true = apenas lidos; false = apenas não lidos.
     */
    int contar(Boolean lido);
}
