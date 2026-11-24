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

public class ListaLivrosActivity extends AppCompatActivity
        implements AdapterLivro.OnLivroClickListener {

    private ILivroDAO dao;
    private AdapterLivro adapter;
    private TextView tvListaVazia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_livros);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, 0);
            return insets;
        });

        dao = new LivroDAO(this);

        tvListaVazia = findViewById(R.id.tvListaVazia);
        RecyclerView rvLivros = findViewById(R.id.rvLivros);
        rvLivros.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterLivro(null, this);
        rvLivros.setAdapter(adapter);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavegacaoMenu.configurar(this, bottomNav, R.id.nav_lista);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLivros();
    }

    private void carregarLivros() {
        List<Livro> livros = dao.listarTodos();
        adapter.atualizarLista(livros);
        tvListaVazia.setVisibility(livros.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLivroClick(int position) {
        Livro livro = adapter.getLivro(position);
        Intent intent = new Intent(this, CadastroLivroActivity.class);
        intent.putExtra(CadastroLivroActivity.EXTRA_LIVRO, livro);
        NavegacaoMenu.irPara(this, intent);
    }

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
                .setNegativeButton(R.string.dialogo_btn_nao, null)
                .show();
    }
}
