package com.example.bibliotecapessoal.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "biblioteca.db";
    public static final int VERSAO_BANCO = 2;

    public static final String TABELA_LIVROS = "livros";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_TITULO = "titulo";
    public static final String COLUNA_AUTOR = "autor";
    public static final String COLUNA_ANO = "ano";
    public static final String COLUNA_LIDO = "lido";
    public static final String COLUNA_NOTA = "nota";

    private static final String SQL_CRIAR_TABELA =
            "CREATE TABLE " + TABELA_LIVROS + " ("
                    + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUNA_TITULO + " TEXT NOT NULL, "
                    + COLUNA_AUTOR + " TEXT NOT NULL, "
                    + COLUNA_ANO + " INTEGER NOT NULL, "
                    + COLUNA_LIDO + " INTEGER NOT NULL, "
                    + COLUNA_NOTA + " INTEGER)";

    public DbHelper(Context context) {
        super(context.getApplicationContext(), NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CRIAR_TABELA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE livros ADD COLUMN nota INTEGER");
        }
    }
}
