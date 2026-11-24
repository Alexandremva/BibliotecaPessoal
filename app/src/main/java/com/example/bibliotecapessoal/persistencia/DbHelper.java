package com.example.bibliotecapessoal.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * PERSISTÊNCIA — Criação e versionamento do banco de dados.
 *
 * O Android já vem com o banco SQLite embutido. A classe SQLiteOpenHelper (que esta
 * classe estende) cuida de:
 *  - criar o arquivo do banco na primeira vez que o app usa (chamando onCreate);
 *  - atualizar a estrutura quando a versão do banco muda (chamando onUpgrade).
 *
 * Aqui também ficam os nomes da tabela e das colunas como constantes, para que o
 * resto do código use sempre os mesmos nomes e não haja erro de digitação.
 */
public class DbHelper extends SQLiteOpenHelper {

    // Nome do arquivo do banco salvo no celular
    public static final String NOME_BANCO = "biblioteca.db";

    // Versão da estrutura do banco. Ao mudar a estrutura (ex.: nova coluna), aumenta-se
    // este número e o Android chama onUpgrade automaticamente nos celulares que tinham a versão antiga.
    public static final int VERSAO_BANCO = 2;

    // Nome da tabela e das colunas
    public static final String TABELA_LIVROS = "livros";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_TITULO = "titulo";
    public static final String COLUNA_AUTOR = "autor";
    public static final String COLUNA_ANO = "ano";
    public static final String COLUNA_LIDO = "lido";
    public static final String COLUNA_NOTA = "nota";

    /*
     * Comando SQL que cria a tabela "livros":
     *  - id:     número inteiro, chave primária, gerado automaticamente (AUTOINCREMENT)
     *  - titulo: texto obrigatório (NOT NULL)
     *  - autor:  texto obrigatório
     *  - ano:    inteiro obrigatório
     *  - lido:   inteiro obrigatório (SQLite não tem boolean, então usamos 1 = lido e 0 = não lido)
     *  - nota:   inteiro OPCIONAL (pode ser NULL quando o livro não tem nota)
     */
    private static final String SQL_CRIAR_TABELA =
            "CREATE TABLE " + TABELA_LIVROS + " ("
                    + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUNA_TITULO + " TEXT NOT NULL, "
                    + COLUNA_AUTOR + " TEXT NOT NULL, "
                    + COLUNA_ANO + " INTEGER NOT NULL, "
                    + COLUNA_LIDO + " INTEGER NOT NULL, "
                    + COLUNA_NOTA + " INTEGER)";

    /**
     * Construtor. Usa o contexto da APLICAÇÃO (e não o da tela) para evitar vazamento
     * de memória caso a tela seja fechada enquanto o banco ainda está referenciado.
     */
    public DbHelper(Context context) {
        super(context.getApplicationContext(), NOME_BANCO, null, VERSAO_BANCO);
    }

    /**
     * Chamado automaticamente UMA única vez: quando o banco ainda não existe no celular.
     * Cria a tabela de livros.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CRIAR_TABELA);
    }

    /**
     * Chamado automaticamente quando o app é atualizado e VERSAO_BANCO aumentou.
     * Na versão 1 a tabela não tinha a coluna "nota"; na versão 2 ela foi adicionada.
     * Assim o usuário não perde os livros já cadastrados ao atualizar o app.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE livros ADD COLUMN nota INTEGER");
        }
    }
}
