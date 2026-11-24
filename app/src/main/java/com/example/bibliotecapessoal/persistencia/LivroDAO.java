package com.example.bibliotecapessoal.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.bibliotecapessoal.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class LivroDAO implements ILivroDAO {

    private final DbHelper dbHelper;

    public LivroDAO(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    private ContentValues toContentValues(Livro livro) {
        ContentValues valores = new ContentValues();
        valores.put(DbHelper.COLUNA_TITULO, livro.getTitulo());
        valores.put(DbHelper.COLUNA_AUTOR, livro.getAutor());
        valores.put(DbHelper.COLUNA_ANO, livro.getAno());
        valores.put(DbHelper.COLUNA_LIDO, livro.isLido() ? 1 : 0);
        if (livro.getNota() == null) {
            valores.putNull(DbHelper.COLUNA_NOTA);
        } else {
            valores.put(DbHelper.COLUNA_NOTA, livro.getNota());
        }
        return valores;
    }

    @Override
    public boolean salvar(Livro livro) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long idGerado = db.insert(DbHelper.TABELA_LIVROS, null, toContentValues(livro));
            if (idGerado != -1) {
                livro.setId(idGerado);
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

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

    @Override
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT " + DbHelper.COLUNA_ID + ", "
                + DbHelper.COLUNA_TITULO + ", "
                + DbHelper.COLUNA_AUTOR + ", "
                + DbHelper.COLUNA_ANO + ", "
                + DbHelper.COLUNA_LIDO + ", "
                + DbHelper.COLUNA_NOTA
                + " FROM " + DbHelper.TABELA_LIVROS
                + " ORDER BY " + DbHelper.COLUNA_TITULO + " ASC";

        try (Cursor cursor = db.rawQuery(sql, null)) {
            int idxId = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_ID);
            int idxTitulo = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_TITULO);
            int idxAutor = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_AUTOR);
            int idxAno = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_ANO);
            int idxLido = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_LIDO);
            int idxNota = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_NOTA);

            while (cursor.moveToNext()) {
                Livro livro = new Livro(
                        cursor.getLong(idxId),
                        cursor.getString(idxTitulo),
                        cursor.getString(idxAutor),
                        cursor.getInt(idxAno),
                        cursor.getInt(idxLido) == 1,
                        cursor.isNull(idxNota) ? null : cursor.getInt(idxNota));
                livros.add(livro);
            }
        }
        return livros;
    }

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
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        }
    }
}
