package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entidades.Usuario;
import com.example.myapplication.Persistencia.UsuarioDAO;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {
    private Button btnVerUsuarios;
    private Button btnCerrarSesion;
    private Button btnChatGrupal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnVerUsuarios = findViewById(R.id.btnVerUsuarios);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnChatGrupal = findViewById(R.id.btnChatGrupal);

        btnVerUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, VerUsuariosActivity.class);
                startActivity(intent);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });

        btnChatGrupal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(this, MainActivity.class);
                returnGrupal();
            }
        });
    }

    private void returnLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void returnGrupal(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();

        if(UsuarioDAO.getInstancia().isUsuarioLogeado()){
            //el usuario esta logeado y hacemos algo

        }else{
            returnLogin();
        }
    }
}
