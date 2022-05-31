package com.example.easymealbussiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button tv_registrar;
    EditText usuario,pass;
    Button login;


    String URL = "http://192.168.0.9/easymeal/crudUsuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        tv_registrar= findViewById(R.id.tv_registrar);
        login = findViewById(R.id.login);
        usuario = findViewById(R.id.usuario);
        pass = findViewById(R.id.password);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usuario.setText(bundle.get("usuario").toString());
        }
        recuperarPreferencia();

    }

    public void RegistrarUsuario(View view) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

    public void validarUsuario(View view) {
        accessoBase();
    }

    private void accessoBase(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if(!response.trim().isEmpty()){
                guardarPreferencia();
                Intent intent = new Intent(this, MenuPrincipal.class);
                intent.putExtra("idUsuario", Integer.valueOf(response));
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(Login.this, "FALLO LA CONEXION AL SERVIDOR", Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("username", usuario.getText().toString());
                parametros.put("contrasenia", pass.getText().toString());
                parametros.put("accion", "login");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarPreferencia(){
        SharedPreferences preferences = getSharedPreferences("preferenciaLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario", usuario.getText().toString());
        editor.putString("contrasenia", pass.getText().toString());
        editor.commit();
    }

    private void recuperarPreferencia(){
        SharedPreferences preferences = getSharedPreferences("preferenciaLogin", Context.MODE_PRIVATE);
        usuario.setText(preferences.getString("usuario", ""));
        pass.setText(preferences.getString("contrasenia", ""));
    }
}