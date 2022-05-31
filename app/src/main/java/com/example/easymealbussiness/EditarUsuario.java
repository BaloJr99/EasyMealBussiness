package com.example.easymealbussiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditarUsuario extends AppCompatActivity {

    DrawerLayout dl;
    int idUsuario;
    EditText etNombre, etApellidoP, etApellidoM, etTitulo;
    TextView tvFecha;
    String URL = "http://192.168.0.9/easymeal/crudUsuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        dl = findViewById(R.id.drawer_editaru);
        etNombre = findViewById(R.id.editnombre);
        etApellidoP = findViewById(R.id.editapellidop);
        etApellidoM = findViewById(R.id.editapellidom);
        etTitulo = findViewById(R.id.editTitulo);
        tvFecha = findViewById(R.id.editfecha);

        Bundle b = getIntent().getExtras();
        idUsuario = b.getInt("idUsuario");
        buscarDatos();
    }

    private void buscarDatos(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            if(!response.trim().isEmpty()){
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    etNombre.setText(jsonObject.getString("nombre"));
                    etApellidoP.setText(jsonObject.getString("apellidoPaterno"));
                    etApellidoM.setText(jsonObject.getString("apellidoMaterno"));
                    etTitulo.setText(jsonObject.getString("titulo"));
                    tvFecha.setText(jsonObject.getString("fechaNacimiento"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(EditarUsuario.this, "OCURRIO UN ERROR AL BUSCAR SUS DATOS", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(EditarUsuario.this, error.toString(), Toast.LENGTH_SHORT).show()){
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

    public void ClickFechaU(View view) {
        int dia, mes, ano;
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);

        @SuppressLint("SetTextI18n") DatePickerDialog date = new DatePickerDialog(this, (datePicker, anio, mes1, dia1) -> tvFecha.setText(anio +"-"+ (mes1 + 1) +"-"+dia1), ano, mes, dia);
        date.show();
    }

    public void ClickEditandoU(View view) {
        if(!camposVacios()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                if(response.equals("ACTUALIZADO EXITOSAMENTE")){
                    Toast.makeText(EditarUsuario.this, response, Toast.LENGTH_SHORT).show();
                    MenuPrincipal.redirectActivity(EditarUsuario.this, MenuUsuario.class);
                }else{
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(EditarUsuario.this, "OCURRIO UN ERROR AL BUSCAR SUS DATOS", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(EditarUsuario.this, error.toString(), Toast.LENGTH_SHORT).show()){
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("idUsuario", String.valueOf(idUsuario));
                    parametros.put("nombre", etNombre.getText().toString());
                    parametros.put("apellidoPaterno", etApellidoP.getText().toString());
                    parametros.put("apellidoMaterno", etApellidoM.getText().toString());
                    parametros.put("fechaNacimiento", tvFecha.getText().toString());
                    parametros.put("titulo", etTitulo.getText().toString());
                    parametros.put("accion", "actualizando");

                    return parametros;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private boolean camposVacios(){
        if(etNombre.getText().toString().trim().isEmpty() ||
            etApellidoP.getText().toString().trim().isEmpty() ||
            etApellidoM.getText().toString().trim().isEmpty() ||
            etTitulo.getText().toString().trim().isEmpty()){
            return true;
        }
        return false;
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

    public void ClickCancelarU(View view) {
        MenuPrincipal.redirectActivity(this, MenuUsuario.class);
    }
}