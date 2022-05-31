package com.example.easymealbussiness;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.easymealbussiness.mensajeria.AdapterMensaje;
import com.example.easymealbussiness.mensajeria.Mensaje;
import com.example.easymealbussiness.mensajeria.MensajeEnviar;
import com.example.easymealbussiness.mensajeria.MensajeRecibir;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mensajes extends AppCompatActivity {

    DrawerLayout dl;
    TextView tvNombre;
    RecyclerView tvChat;
    private String nombre;

    EditText etMensaje;

    int id;
    int idCliente = 1;

    private AdapterMensaje adapterMensaje;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        dl = findViewById(R.id.drawer_mensajes);
        tvChat = findViewById(R.id.tvChat);
        tvNombre = findViewById(R.id.tvNombre);
        etMensaje = findViewById(R.id.etMensaje);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");

        adapterMensaje = new AdapterMensaje(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        tvChat.setLayoutManager(l);
        tvChat.setAdapter(adapterMensaje);

        adapterMensaje.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollBar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MensajeRecibir m = snapshot.getValue(MensajeRecibir.class);
                adapterMensaje.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getInt("idUsuario");
            llenarUsuario();
        }

        buscarDatos();
    }

    private void setScrollBar(){
        tvChat.scrollToPosition(adapterMensaje.getItemCount() - 1);
    }

    private void buscarDatos(){

        String URL = "http://192.168.0.9/easymeal/crudUsuario.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if(!response.trim().isEmpty()){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    nombre = jsonObject.getString("nombre");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "OCURRIO UN ERROR AL BUSCAR SUS DATOS", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idUsuario", String.valueOf(id));
                parametros.put("accion", "buscandoDatos");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void llenarUsuario() {
        String URL = "http://192.168.0.9/easymeal/obtenerUsuario.php";
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if(!response.trim().isEmpty()){

                tvNombre.setText(response);

            }else{
                Toast.makeText(Mensajes.this, "Ocurrio un error al obtener datos del cliente", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(Mensajes.this, error.toString(), Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idCliente", String.valueOf(idCliente));
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ClickEmoji(View view) {

    }

    public void ClickSend(View view) {
        databaseReference.push().setValue(new MensajeEnviar(etMensaje.getText().toString(), nombre, "1", ServerValue.TIMESTAMP));
        etMensaje.setText("");
    }

    public void ClickMenu(View view) {
        //Abrimos drawer
        MenuPrincipal.openDrawer(dl);
    }

    public void ClickLogo(View view) {
        //Cerramos el drawer
        MenuPrincipal.closeDrawer(dl);
    }
    public void ClickInicio(View view) {
        //Recreamos actividad
        MenuPrincipal.redirectActivity(this, MenuPrincipal.class);
    }

    public void ClickSalir(View view) {
        //Cerramos app
        MenuPrincipal.logout(this);
    }

    public void ClickHorario(View v) {

    }

    public void ClickChats(View v) {
        recreate();
    }

    public void ClickUsuario(View v) {
        MenuPrincipal.redirectActivity(this, MenuUsuario.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Cerramos drawer
        MenuPrincipal.closeDrawer(dl);
    }
}