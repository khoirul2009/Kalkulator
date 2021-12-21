package com.khoirul.kalkulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Date;
import com.khoirul.kalkulator.adapter.ResAdapter;
import com.khoirul.kalkulator.model.model;


import java.sql.Time;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText angka1, angka2;
    Button hitung;
    RadioButton tambah, kali, bagi, kurang;
    RadioGroup operasi;
    TextView hasil;
    String tOperasi;
    private List<model> list = new ArrayList<>();
    private ResAdapter resAdapter;
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.reyclerView);
        angka1 = (EditText) findViewById(R.id.angka1);
        angka2 = (EditText) findViewById(R.id.angka2);
        hitung = (Button) findViewById(R.id.hitung);
        tambah = (RadioButton) findViewById(R.id.tambah);
        kali = (RadioButton) findViewById(R.id.kali);
        bagi = (RadioButton) findViewById(R.id.bagi);
        kurang = (RadioButton) findViewById(R.id.kurang);
        operasi = (RadioGroup) findViewById(R.id.operasi);
        hasil = (TextView) findViewById(R.id.hasil);

        resAdapter = new ResAdapter(getApplicationContext(), list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(resAdapter);
        resAdapter.setDialog(new ResAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogitem = {"Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                deleteData(list.get(pos).getId());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });




        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if((angka1.getText().length()>0) && angka2.getText().length()>0) {
                   int select = operasi.getCheckedRadioButtonId();

                   if (select == tambah.getId()) {
                       double n1 = Double.parseDouble(angka1.getText().toString());
                       double n2 = Double.parseDouble(angka2.getText().toString());

                       double n = n1 + n2;
                       hasil.setText(String.format("%.2f", n));
                       tOperasi = "+";

                   } else if (select == kurang.getId()){
                       double n1 = Double.parseDouble(angka1.getText().toString());
                       double n2 = Double.parseDouble(angka2.getText().toString());

                       double n = n1 - n2;
                       hasil.setText(String.format("%.2f", n));
                       tOperasi = "-";
                   } else if (select== kali.getId()){
                       double n1 = Double.parseDouble(angka1.getText().toString());
                       double n2 = Double.parseDouble(angka2.getText().toString());

                       double n = n1 * n2;
                       hasil.setText(String.format("%.2f", n));
                       tOperasi = "x";

                   } else if (select==bagi.getId()) {
                       double n1 = Double.parseDouble(angka1.getText().toString());
                       double n2 = Double.parseDouble(angka2.getText().toString());

                       double n = n1 / n2;
                       hasil.setText(String.format("%.2f", n));
                       tOperasi = "/";

                   }

                   else {
                       Toast toast = Toast.makeText(MainActivity.this, "Pilih Operasi Hitung", Toast.LENGTH_LONG);
                       toast.show();
                   }
               } else {
                   Toast toast = Toast.makeText(MainActivity.this, "Anda Belum Memasukan Angka", Toast.LENGTH_LONG);
                   toast.show();
               }
                Map<String, Object> history = new HashMap<>();
                history.put("date", Timestamp.now());
                history.put("angka1",angka1.getText().toString());
                history.put("operasi", tOperasi);
                history.put("angka2", angka2.getText().toString());
                history.put("hasil", hasil.getText().toString());
                if(angka1.getText().length()>0 && angka2.getText().length()>0) {

                    db.collection("history")
                            .add(history)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(@NonNull DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            });
                    onStart();
                }


            }



        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }
    private void getData() {
        db.collection("history")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            list.clear();
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                model data = new model(document.getString("angka1"), document.getString("operasi"), document.getString("angka2"), document.getString("hasil"));
                                data.setId(document.getId());
                                list.add(data);
                            }
                            resAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(getApplicationContext(),"Data gagal ditampilkan", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    private void deleteData(String id) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        db.collection("history").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Data gagal dihapus", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Riwayat Berhasil dihapus", Toast.LENGTH_SHORT).show();
                        }

                        getData();
                    }
                });
        onStart();
    }


}