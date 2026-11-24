package com.example.bibliotecapessoal.util;

import android.app.Activity;
import android.content.Intent;

import com.example.bibliotecapessoal.R;
import com.example.bibliotecapessoal.view.CadastroLivroActivity;
import com.example.bibliotecapessoal.view.ListaLivrosActivity;
import com.example.bibliotecapessoal.view.ResumoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * UTILITÁRIO — Centraliza a navegação pelo menu inferior (BottomNavigationView).
 *
 * As três telas (Livros, Cadastrar, Resumo) têm o mesmo menu na parte de baixo.
 * Em vez de repetir o mesmo código em cada tela, todas chamam NavegacaoMenu.configurar().
 *
 * A classe é "final" e o construtor é privado: ela só tem métodos estáticos e
 * não deve ser instanciada (padrão de classe utilitária).
 */
public final class NavegacaoMenu {

    private NavegacaoMenu() {
    }

    /**
     * Configura o BottomNavigationView da Activity atual.
     * O item atual é marcado ANTES de registrar o listener para evitar loop infinito.
     *
     * @param activity  tela que está aberta agora
     * @param nav       o menu inferior dessa tela
     * @param itemAtual id do item que representa essa tela (fica destacado)
     */
    public static void configurar(final Activity activity,
                                  BottomNavigationView nav,
                                  final int itemAtual) {
        // 1) Destaca o ícone da tela atual
        nav.setSelectedItemId(itemAtual);

        // 2) Define o que acontece quando o usuário toca em um item do menu
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            // Tocou na própria tela em que já está: não faz nada
            if (itemId == itemAtual) {
                return true;
            }

            // Descobre qual tela abrir
            Class<?> destino = destinoPara(itemId);
            if (destino == null) {
                return false;
            }

            irPara(activity, destino);
            return true;
        });
    }

    /** Traduz o id do item do menu (menu_navegacao.xml) para a classe da tela correspondente. */
    private static Class<?> destinoPara(int itemId) {
        if (itemId == R.id.nav_lista) {
            return ListaLivrosActivity.class;
        } else if (itemId == R.id.nav_cadastro) {
            return CadastroLivroActivity.class;
        } else if (itemId == R.id.nav_resumo) {
            return ResumoActivity.class;
        }
        return null;
    }

    /** Abre a tela de destino (versão simplificada: recebe só a classe). */
    public static void irPara(Activity activity, Class<?> destino) {
        irPara(activity, new Intent(activity, destino));
    }

    /**
     * Abre a tela descrita pelo Intent e FECHA a tela atual.
     *  - Sem animação de transição, para parecer que só o conteúdo trocou (como abas).
     *  - finish() fecha a tela atual, evitando acumular várias telas abertas na pilha
     *    (o botão "voltar" não fica passando por todas as telas visitadas).
     * Esta versão recebe um Intent para permitir enviar dados junto (ex.: o livro a editar).
     */
    public static void irPara(Activity activity, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }
}
