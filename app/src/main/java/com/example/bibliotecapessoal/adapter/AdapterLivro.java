package com.example.bibliotecapessoal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecapessoal.R;
import com.example.bibliotecapessoal.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class AdapterLivro extends RecyclerView.Adapter<AdapterLivro.LivroViewHolder> {

    public interface OnLivroClickListener {
        void onLivroClick(int position);

        void onLivroLongClick(int position);
    }

    private final List<Livro> livros = new ArrayList<>();
    private final OnLivroClickListener listener;

    public AdapterLivro(List<Livro> livrosIniciais, OnLivroClickListener listener) {
        this.listener = listener;
        if (livrosIniciais != null) {
            this.livros.addAll(livrosIniciais);
        }
    }

    public void atualizarLista(List<Livro> lista) {
        livros.clear();
        if (lista != null) {
            livros.addAll(lista);
        }
        notifyDataSetChanged();
    }

    public Livro getLivro(int position) {
        return livros.get(position);
    }

    @NonNull
    @Override
    public LivroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livro, parent, false);
        return new LivroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivroViewHolder holder, int position) {
        Livro livro = livros.get(position);
        Context context = holder.itemView.getContext();

        holder.tvTitulo.setText(livro.getTitulo());
        holder.tvAutor.setText(context.getString(R.string.formato_autor, livro.getAutor()));
        holder.tvAno.setText(context.getString(R.string.formato_ano, livro.getAno()));

        if (livro.getNota() != null) {
            holder.tvNota.setText(context.getString(R.string.formato_nota, livro.getNota()));
        } else {
            holder.tvNota.setText(R.string.sem_nota);
        }

        int corStatus;
        if (livro.isLido()) {
            holder.tvStatus.setText(R.string.status_lido);
            corStatus = ContextCompat.getColor(context, R.color.status_lido);
        } else {
            holder.tvStatus.setText(R.string.status_nao_lido);
            corStatus = ContextCompat.getColor(context, R.color.status_nao_lido);
        }
        holder.tvStatus.setTextColor(corStatus);
        holder.viewIndicador.setBackgroundColor(corStatus);

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (listener != null && pos != RecyclerView.NO_POSITION) {
                listener.onLivroClick(pos);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (listener != null && pos != RecyclerView.NO_POSITION) {
                listener.onLivroLongClick(pos);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    static class LivroViewHolder extends RecyclerView.ViewHolder {
        final View viewIndicador;
        final TextView tvTitulo;
        final TextView tvAutor;
        final TextView tvAno;
        final TextView tvNota;
        final TextView tvStatus;

        LivroViewHolder(@NonNull View itemView) {
            super(itemView);
            viewIndicador = itemView.findViewById(R.id.viewIndicador);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvAno = itemView.findViewById(R.id.tvAno);
            tvNota = itemView.findViewById(R.id.tvNota);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
