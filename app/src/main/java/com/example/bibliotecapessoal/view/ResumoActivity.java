package com.example.bibliotecapessoal.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bibliotecapessoal.R;
import com.example.bibliotecapessoal.persistencia.ILivroDAO;
import com.example.bibliotecapessoal.persistencia.LivroDAO;
import com.example.bibliotecapessoal.util.NavegacaoMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * VIEW — Tela "Resumo da Biblioteca".
 *
 * Mostra três números em cartões:
 *  - Total de livros;
 *  - Livros lidos (em verde);
 *  - Livros não lidos (em vermelho).
 * Os números são calculados direto no banco com SELECT COUNT(*) (ver LivroDAO.contar).
 */
public class ResumoActivity extends AppCompatActivity {

    private ILivroDAO dao;
    private TextView tvTotal;
    private TextView tvLidos;
    private TextView tvNaoLidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resumo);

        // Espaçamento para o conteúdo não ficar embaixo da barra de status
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, 0);
            return insets;
        });

        dao = new LivroDAO(this);

        // Componentes onde os números serão exibidos
        tvTotal = findViewById(R.id.tvTotal);
        tvLidos = findViewById(R.id.tvLidos);
        tvNaoLidos = findViewById(R.id.tvNaoLidos);

        // Menu inferior com o item "Resumo" destacado
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavegacaoMenu.configurar(this, bottomNav, R.id.nav_resumo);
    }

    /** Sempre que a tela aparece, recalcula os números para estarem atualizados. */
    @Override
    protected void onResume() {
        super.onResume();
        atualizarResumo();
    }

    /** Consulta as três contagens no banco e mostra na tela. */
    private void atualizarResumo() {
        tvTotal.setText(String.valueOf(dao.contar(null)));     // todos
        tvLidos.setText(String.valueOf(dao.contar(true)));     // só lidos
        tvNaoLidos.setText(String.valueOf(dao.contar(false))); // só não lidos
    }
}
