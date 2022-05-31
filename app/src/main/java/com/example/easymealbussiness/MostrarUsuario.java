package com.example.easymealbussiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MostrarUsuario extends AppCompatActivity {

    DrawerLayout dl;
    ListView listaUser;
    int idUsuario;
    String URL = "http://192.168.0.9/easymeal/crudUsuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_usuario);
        dl = findViewById(R.id.drawer_mostrar_u);
        Bundle b=getIntent().getExtras();
        listaUser = findViewById(R.id.listUsers);
        idUsuario = b.getInt("idUsuario");
        buscarDatos();
    }

    private void buscarDatos(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if(!response.trim().isEmpty()){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ArrayList<String> datos = new ArrayList<>();
                    datos.add("Nombre: " + jsonObject.getString("nombre"));
                    datos.add("Apellido Paterno: " + jsonObject.getString("apellidoPaterno"));
                    datos.add("Apellido Materno: " + jsonObject.getString("apellidoMaterno"));
                    datos.add("Fecha Nacimiento: " + jsonObject.getString("fechaNacimiento"));
                    datos.add("Titulo: " + jsonObject.getString("titulo"));
                    datos.add("Suscripcion: " + jsonObject.getString("suscripcion"));
                    datos.add("Fecha Vencimiento: " + jsonObject.getString("fechaVencimiento"));

                    ArrayAdapter<String> a = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, datos);
                    listaUser.setAdapter(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(MostrarUsuario.this, "OCURRIO UN ERROR AL BUSCAR SUS DATOS", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(MostrarUsuario.this, error.toString(), Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idUsuario", String.valueOf(idUsuario));
                parametros.put("accion", "buscandoDatos");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
        MenuPrincipal.redirectActivity(this, Mensajes.class);
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