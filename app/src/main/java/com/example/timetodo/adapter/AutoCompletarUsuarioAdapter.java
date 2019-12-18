package com.example.timetodo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.timetodo.R;
import com.example.timetodo.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class AutoCompletarUsuarioAdapter extends ArrayAdapter<Usuario> {
    private List<Usuario> usuarioListaCheia;

    public AutoCompletarUsuarioAdapter(@NonNull Context context, @NonNull List<Usuario> usuarioLista) {
        super(context, 0, usuarioLista);
        usuarioListaCheia = new ArrayList<>(usuarioLista);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return usuarioFiltro;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.usuario_autocompletar_linha, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.textViewNome);
        TextView textViewEmail = convertView.findViewById(R.id.textViewEmail);
       // ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);

        Usuario usuarioItem = getItem(position);
        Log.d("autoComplete", "getView: "+usuarioItem.getNome()+"  ");

        if (usuarioItem != null) {
            textViewName.setText(usuarioItem.getNome());
            textViewEmail.setText(usuarioItem.getEmail());
           // imageViewFlag.setImageResource(usuarioItem.getFlagImage());
        }

        return convertView;
    }

    private Filter usuarioFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Usuario> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(usuarioListaCheia);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Usuario item : usuarioListaCheia) {
                    if (item.getEmail().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Usuario) resultValue).getEmail();
        }
    };
}
