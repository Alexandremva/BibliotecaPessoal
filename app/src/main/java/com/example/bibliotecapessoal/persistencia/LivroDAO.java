package com.example.bibliotecapessoal.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.bibliotecapessoal.model.Livro;

import java.util.ArrayList;
import java.util.List;

/**
 * PERSISTÊNCIA — Implementação do DAO usando SQLite.
 *
 * É a ÚNICA classe do app que executa comandos no banco de dados (INSERT, UPDATE,
 * DELETE, SELECT). As telas pedem "salve este livro" e esta classe sabe como fazer isso.
 */
public class LivroDAO implements ILivroDAO {

    // Objeto que dá acesso ao banco (cria/abre o arquivo biblioteca.db)
    private final DbHelper dbHelper;

    public LivroDAO(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    /**
     * Converte um objeto Livro em ContentValues — um "mapa" coluna → valor que o
     * Android usa nos métodos insert() e update().
     * O id NÃO é colocado aqui porque é gerado pelo próprio banco.
     */
    private ContentValues toContentValues(Livro livro) {
        ContentValues valores = new ContentValues();
        valores.put(DbHelper.COLUNA_TITULO, livro.getTitulo());
        valores.put(DbHelper.COLUNA_AUTOR, livro.getAutor());
        valores.put(DbHelper.COLUNA_ANO, livro.getAno());
        // SQLite não tem tipo boolean: grava 1 para lido e 0 para não lido
        valores.put(DbHelper.COLUNA_LIDO, livro.isLido() ? 1 : 0);
        // A nota é opcional: se não houver, grava NULL no banco
        if (livro.getNota() == null) {
            valores.putNull(DbHelper.COLUNA_NOTA);
        } else {
            valores.put(DbHelper.COLUNA_NOTA, livro.getNota());
        }
        return valores;
    }

    /**
     * CREATE — Insere um novo livro (equivale a: INSERT INTO livros ...).
     * db.insert() devolve o id gerado, ou -1 se falhar. Em caso de sucesso,
     * o id é colocado de volta no objeto Livro.
     */
    @Override
    public boolean salvar(Livro livro) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase(); // abre o banco para escrita
            long idGerado = db.insert(DbHelper.TABELA_LIVROS, null, toContentValues(livro));
            if (idGerado != -1) {
                livro.setId(idGerado);
                return true;
            }
            return false;
        } catch (SQLException e) {
            // Qualquer erro de banco é tratado como falha; a tela mostra a mensagem de erro
            return false;
        }
    }

    /**
     * UPDATE — Altera um livro existente (equivale a: UPDATE livros SET ... WHERE id = ?).
     * O "?" é um parâmetro preenchido com o id: isso evita SQL Injection.
     * Retorna true se pelo menos uma linha foi alterada.
     */
    @Override
    public boolean atualizar(Livro livro) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int linhasAfetadas = db.update(
                    DbHelper.TABELA_LIVROS,
                    toContentValues(livro),
                    DbHelper.COLUNA_ID + " = ?",
                    new String[]{String.valueOf(livro.getId())});
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * DELETE — Remove um livro pelo id (equivale a: DELETE FROM livros WHERE id = ?).
     * Retorna true se pelo menos uma linha foi removida.
     */
    @Override
    public boolean excluir(long id) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int linhasAfetadas = db.delete(
                    DbHelper.TABELA_LIVROS,
                    DbHelper.COLUNA_ID + " = ?",
                    new String[]{String.valueOf(id)});
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * READ — Busca todos os livros, ordenados pelo título (A → Z).
     *
     * O resultado de um SELECT vem num Cursor, que funciona como um "ponteiro" que
     * anda linha por linha. Para cada linha, montamos um objeto Livro e o colocamos na lista.
     */
    @Override
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // abre o banco só para leitura
        String sql = "SELECT " + DbHelper.COLUNA_ID + ", "
                + DbHelper.COLUNA_TITULO + ", "
                + DbHelper.COLUNA_AUTOR + ", "
                + DbHelper.COLUNA_ANO + ", "
                + DbHelper.COLUNA_LIDO + ", "
                + DbHelper.COLUNA_NOTA
                + " FROM " + DbHelper.TABELA_LIVROS
                + " ORDER BY " + DbHelper.COLUNA_TITULO + " ASC";

        // "try-with-resources": o Cursor é fechado automaticamente ao final do bloco
        try (Cursor cursor = db.rawQuery(sql, null)) {
            // Descobre a posição (índice) de cada coluna no resultado, uma única vez
            int idxId = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_ID);
            int idxTitulo = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_TITULO);
            int idxAutor = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_AUTOR);
            int idxAno = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_ANO);
            int idxLido = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_LIDO);
            int idxNota = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_NOTA);

            // moveToNext() avança para a próxima linha; retorna false quando acabam as linhas
            while (cursor.moveToNext()) {
                Livro livro = new Livro(
                        cursor.getLong(idxId),
                        cursor.getString(idxTitulo),
                        cursor.getString(idxAutor),
                        cursor.getInt(idxAno),
                        cursor.getInt(idxLido) == 1,                          // 1 → true, 0 → false
                        cursor.isNull(idxNota) ? null : cursor.getInt(idxNota)); // NULL → sem nota
                livros.add(livro);
            }
        }
        return livros;
    }

    /**
     * READ — Conta quantos livros existem, usando SELECT COUNT(*).
     *  - lido == null  → conta todos
     *  - lido == true  → conta só os lidos      (WHERE lido = 1)
     *  - lido == false → conta só os não lidos  (WHERE lido = 0)
     * Contar direto no banco é mais eficiente do que carregar todos os livros e contar na mão.
     */
    @Override
    public int contar(Boolean lido) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql;
        String[] argumentos;

        if (lido == null) {
            sql = "SELECT COUNT(*) FROM " + DbHelper.TABELA_LIVROS;
            argumentos = null;
        } else {
            sql = "SELECT COUNT(*) FROM " + DbHelper.TABELA_LIVROS
                    + " WHERE " + DbHelper.COLUNA_LIDO + " = ?";
            argumentos = new String[]{lido ? "1" : "0"};
        }

        try (Cursor cursor = db.rawQuery(sql, argumentos)) {
            // O COUNT(*) devolve uma única linha com uma única coluna (índice 0)
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        }
    }
}
