package com.khoirul.kalkulator.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoirul.kalkulator.R;
import com.khoirul.kalkulator.model.model;

import java.util.List;

public class ResAdapter extends RecyclerView.Adapter<ResAdapter.ViewHolder> {

    private Context context;
    private List<model> list;
    private Dialog dialog;
    public ResAdapter(Context context, List<model> list) {
        this.context = context;
        this.list = list;
    }
    public interface Dialog {
        void onClick(int pos);
    }
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history,parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.a1.setText(list.get(position).getAngka1());
        holder.op.setText(list.get(position).getOperasi());
        holder.a2.setText(list.get(position).getAngka2());
        holder.hasill.setText(list.get(position).getHasil());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView a1, op, a2, hasill;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            a1 = itemView.findViewById(R.id.a1);
            op = itemView.findViewById(R.id.op);
            a2 = itemView.findViewById(R.id.a2);
            hasill = itemView.findViewById(R.id.hasill);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (dialog != null) {
                        dialog.onClick(getLayoutPosition());
                    }
                    return false;
                }
            });
        }
    }
}
