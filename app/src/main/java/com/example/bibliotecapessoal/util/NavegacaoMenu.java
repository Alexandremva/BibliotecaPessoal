package com.example.bibliotecapessoal.util;

import android.app.Activity;
import android.content.Intent;

import com.example.bibliotecapessoal.R;
import com.example.bibliotecapessoal.view.CadastroLivroActivity;
import com.example.bibliotecapessoal.view.ListaLivrosActivity;
import com.example.bibliotecapessoal.view.ResumoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class NavegacaoMenu {

    private NavegacaoMenu() {
    }

    /**
     * Configura o BottomNavigationView da Activity atual.
     * O item atual é marcado ANTES de registrar o listener para evitar loop infinito.
     */
    public static void configurar(final Activity activity,
                                  BottomNavigationView nav,
                                  final int itemAtual) {
        nav.setSelectedItemId(itemAtual);

        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == itemAtual) {
                return true;
            }

            Class<?> destino = destinoPara(itemId);
            if (destino == null) {
                return false;
            }

            irPara(activity, destino);
            return true;
        });
    }

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

    public static void irPara(Activity activity, Class<?> destino) {
        irPara(activity, new Intent(activity, destino));
    }

    public static void irPara(Activity activity, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }
}
