package com.example.bibliotecapessoal.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecapessoal.R;
import com.example.bibliotecapessoal.adapter.AdapterLivro;
import com.example.bibliotecapessoal.model.Livro;
import com.example.bibliotecapessoal.persistencia.ILivroDAO;
import com.example.bibliotecapessoal.persistencia.LivroDAO;
import com.example.bibliotecapessoal.util.NavegacaoMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * VIEW — Tela "Meus Livros" (tela INICIAL do app, definida no AndroidManifest.xml).
 *
 * Responsabilidades:
 *  - Mostrar todos os livros cadastrados numa lista rolável (RecyclerView);
 *  - Mostrar uma mensagem quando não houver nenhum livro;
 *  - Toque simples num livro → abre a tela de cadastro em modo EDIÇÃO;
 *  - Toque longo num livro → pergunta se deseja EXCLUIR.
 *
 * Implementa AdapterLivro.OnLivroClickListener para receber os cliques vindos do Adapter.
 */
public class ListaLivrosActivity extends AppCompatActivity
        implements AdapterLivro.OnLivroClickListener {

    private ILivroDAO dao;              // Acesso ao banco (declarado pela interface)
    private AdapterLivro adapter;       // Liga a lista de livros à RecyclerView
    private TextView tvListaVazia;      // Mensagem "Nenhum livro cadastrado ainda"

    /**
     * onCreate: primeiro método do ciclo de vida, executado quando a tela é criada.
     * Aqui montamos a tela e inicializamos os componentes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Faz o app desenhar "de ponta a ponta", inclusive atrás da barra de status
        EdgeToEdge.enable(this);
        // Liga esta classe Java ao layout XML da tela
        setContentView(R.layout.activity_lista_livros);

        // Como o app desenha atrás das barras do sistema, adiciona um espaçamento (padding)
        // do tamanho dessas barras para o conteúdo não ficar escondido embaixo delas.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, 0);
            return insets;
        });

        dao = new LivroDAO(this);

        // Configura a RecyclerView: LinearLayoutManager = itens em lista vertical
        tvListaVazia = findViewById(R.id.tvListaVazia);
        RecyclerView rvLivros = findViewById(R.id.rvLivros);
        rvLivros.setLayoutManager(new LinearLayoutManager(this));
        // Começa vazio (null); "this" = esta tela recebe os cliques
        adapter = new AdapterLivro(null, this);
        rvLivros.setAdapter(adapter);

        // Configura o menu inferior, destacando o item "Livros"
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavegacaoMenu.configurar(this, bottomNav, R.id.nav_lista);
    }

    /**
     * onResume: executado toda vez que a tela fica visível (inclusive ao voltar de outra).
     * Recarregar aqui garante que a lista esteja sempre atualizada.
     */
    @Override
    protected void onResume() {
        super.onResume();
        carregarLivros();
    }

    /** Busca os livros no banco, entrega ao Adapter e mostra/esconde a mensagem de lista vazia. */
    private void carregarLivros() {
        List<Livro> livros = dao.listarTodos();
        adapter.atualizarLista(livros);
        tvListaVazia.setVisibility(livros.isEmpty() ? View.VISIBLE : View.GONE);
    }

    /**
     * Toque simples num livro: abre a tela de cadastro enviando o livro no Intent
     * (por isso Livro é Serializable). A tela de cadastro percebe que recebeu um livro
     * e entra em modo de edição.
     */
    @Override
    public void onLivroClick(int position) {
        Livro livro = adapter.getLivro(position);
        Intent intent = new Intent(this, CadastroLivroActivity.class);
        intent.putExtra(CadastroLivroActivity.EXTRA_LIVRO, livro);
        NavegacaoMenu.irPara(this, intent);
    }

    /**
     * Toque longo num livro: mostra uma caixa de diálogo de confirmação.
     * Se o usuário confirmar, exclui do banco, mostra um aviso (Toast) e recarrega a lista.
     */
    @Override
    public void onLivroLongClick(int position) {
        Livro livro = adapter.getLivro(position);
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialogo_excluir_titulo)
                .setMessage(getString(R.string.dialogo_excluir_mensagem, livro.getTitulo()))
                .setPositiveButton(R.string.dialogo_btn_sim, (dialog, which) -> {
                    if (dao.excluir(livro.getId())) {
                        Toast.makeText(this, R.string.msg_excluido_sucesso, Toast.LENGTH_SHORT).show();
                        carregarLivros();
                    } else {
                        Toast.makeText(this, R.string.msg_erro_excluir, Toast.LENGTH_SHORT).show();
                    }
                })
                // "Cancelar" apenas fecha o diálogo (null = nenhuma ação extra)
                .setNegativeButton(R.string.dialogo_btn_nao, null)
                .show();
    }
}
