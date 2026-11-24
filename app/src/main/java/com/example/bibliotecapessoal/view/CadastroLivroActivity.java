package com.example.bibliotecapessoal.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bibliotecapessoal.R;
import com.example.bibliotecapessoal.model.Livro;
import com.example.bibliotecapessoal.persistencia.ILivroDAO;
import com.example.bibliotecapessoal.persistencia.LivroDAO;
import com.example.bibliotecapessoal.util.NavegacaoMenu;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class CadastroLivroActivity extends AppCompatActivity {

    public static final String EXTRA_LIVRO = "extra_livro";

    private static final int ANO_MINIMO = 1450;
    private static final int NOTA_MINIMA = 0;
    private static final int NOTA_MAXIMA = 10;

    private EditText etTitulo;
    private EditText etAutor;
    private EditText etAno;
    private EditText etNota;
    private CheckBox cbLido;

    private ILivroDAO dao;
    private Livro livroEmEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_livro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, 0);
            return insets;
        });

        dao = new LivroDAO(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        etTitulo = findViewById(R.id.etTitulo);
        etAutor = findViewById(R.id.etAutor);
        etAno = findViewById(R.id.etAno);
        etNota = findViewById(R.id.etNota);
        cbLido = findViewById(R.id.cbLido);
        Button btnSalvar = findViewById(R.id.btnSalvar);

        livroEmEdicao = (Livro) getIntent().getSerializableExtra(EXTRA_LIVRO);
        if (livroEmEdicao != null) {
            etTitulo.setText(livroEmEdicao.getTitulo());
            etAutor.setText(livroEmEdicao.getAutor());
            etAno.setText(String.valueOf(livroEmEdicao.getAno()));
            if (livroEmEdicao.getNota() != null) {
                etNota.setText(String.valueOf(livroEmEdicao.getNota()));
            }
            cbLido.setChecked(livroEmEdicao.isLido());
            btnSalvar.setText(R.string.btn_atualizar);
            toolbar.setTitle(R.string.titulo_edicao);
        }

        btnSalvar.setOnClickListener(v -> salvar());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        NavegacaoMenu.configurar(this, bottomNav, R.id.nav_cadastro);
    }

    private void salvar() {
        String titulo = etTitulo.getText().toString().trim();
        String autor = etAutor.getText().toString().trim();
        String anoTexto = etAno.getText().toString().trim();
        String notaTexto = etNota.getText().toString().trim();

        boolean valido = true;

        if (titulo.isEmpty()) {
            etTitulo.setError(getString(R.string.erro_titulo_obrigatorio));
            valido = false;
        }

        if (autor.isEmpty()) {
            etAutor.setError(getString(R.string.erro_autor_obrigatorio));
            valido = false;
        }

        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        int ano = 0;
        if (anoTexto.isEmpty()) {
            etAno.setError(getString(R.string.erro_ano_obrigatorio));
            valido = false;
        } else {
            try {
                ano = Integer.parseInt(anoTexto);
                if (ano < ANO_MINIMO || ano > anoAtual) {
                    etAno.setError(getString(R.string.erro_ano_intervalo, ANO_MINIMO, anoAtual));
                    valido = false;
                }
            } catch (NumberFormatException e) {
                etAno.setError(getString(R.string.erro_ano_intervalo, ANO_MINIMO, anoAtual));
                valido = false;
            }
        }

        Integer nota = null;
        if (!notaTexto.isEmpty()) {
            try {
                int notaInformada = Integer.parseInt(notaTexto);
                if (notaInformada < NOTA_MINIMA || notaInformada > NOTA_MAXIMA) {
                    etNota.setError(getString(R.string.erro_nota_intervalo, NOTA_MINIMA, NOTA_MAXIMA));
                    valido = false;
                } else {
                    nota = notaInformada;
                }
            } catch (NumberFormatException e) {
                etNota.setError(getString(R.string.erro_nota_intervalo, NOTA_MINIMA, NOTA_MAXIMA));
                valido = false;
            }
        }

        if (!valido) {
            return;
        }

        boolean lido = cbLido.isChecked();

        if (livroEmEdicao != null) {
            livroEmEdicao.setTitulo(titulo);
            livroEmEdicao.setAutor(autor);
            livroEmEdicao.setAno(ano);
            livroEmEdicao.setLido(lido);
            livroEmEdicao.setNota(nota);

            if (dao.atualizar(livroEmEdicao)) {
                Toast.makeText(this, R.string.msg_atualizado_sucesso, Toast.LENGTH_SHORT).show();
                NavegacaoMenu.irPara(this, ListaLivrosActivity.class);
            } else {
                Toast.makeText(this, R.string.msg_erro_atualizar, Toast.LENGTH_SHORT).show();
            }
        } else {
            Livro novoLivro = new Livro(0, titulo, autor, ano, lido, nota);

            if (dao.salvar(novoLivro)) {
                Toast.makeText(this, R.string.msg_salvo_sucesso, Toast.LENGTH_SHORT).show();
                NavegacaoMenu.irPara(this, ListaLivrosActivity.class);
            } else {
                Toast.makeText(this, R.string.msg_erro_salvar, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
