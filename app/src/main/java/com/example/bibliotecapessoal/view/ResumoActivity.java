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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, 0);
            return insets;
        });

        dao = new LivroDAO(this);

        tvTotal = findViewById(R.id.tvTotal);
        tvLidos = findViewById(R.id.tvLidos);
        tvNaoLidos = findViewById(R.id.tvNaoLidos);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavegacaoMenu.configurar(this, bottomNav, R.id.nav_resumo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarResumo();
    }

    private void atualizarResumo() {
        tvTotal.setText(String.valueOf(dao.contar(null)));
        tvLidos.setText(String.valueOf(dao.contar(true)));
        tvNaoLidos.setText(String.valueOf(dao.contar(false)));
    }
}
