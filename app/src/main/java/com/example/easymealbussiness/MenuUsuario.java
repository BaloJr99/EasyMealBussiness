package com.example.easymealbussiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MenuUsuario extends AppCompatActivity {

    int idUsuario;
    String URL = "http://192.168.0.9/easymeal/crudUsuario.php";
    DrawerLayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);
        dl = findViewById(R.id.drawer_usuario);
        Bundle b = getIntent().getExtras();
        idUsuario = b.getInt("idUsuario");
    }

    public void ClickEditarU(View view) {
        MenuPrincipal.redirectActivity(this, EditarUsuario.class);
    }

    public void ClickEliminarU(View view) {
        AlertDialog.Builder b= new AlertDialog.Builder(MenuUsuario.this);
        b.setMessage("¿Seguro que desea eliminar la cuenta?");
        b.setCancelable(false);
        b.setPositiveButton("SI", (dialogInterface, i) -> {
            accionBase();
        });
        b.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());
        b.show();
    }

    private void accionBase(){
        System.out.println(idUsuario);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if(response.equals("ELIMINADO EXITOSAMENTE")){
                Toast.makeText(MenuUsuario.this,"Cuenta Eliminada",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MenuUsuario.this, Login.class);
                startActivity(intent);
            }else{
                Toast.makeText(MenuUsuario.this, "OCURRIO UN ERROR AL ELIMINAR", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(MenuUsuario.this, error.toString(), Toast.LENGTH_SHORT).show()){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                System.out.println(idUsuario);
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idUsuario", String.valueOf(idUsuario));
                parametros.put("accion", "eliminando");

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ClickVerU(View view) {
        MenuPrincipal.redirectActivity(this, MostrarUsuario.class);
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
        recreate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Cerramos drawer
        MenuPrincipal.closeDrawer(dl);
    }
}