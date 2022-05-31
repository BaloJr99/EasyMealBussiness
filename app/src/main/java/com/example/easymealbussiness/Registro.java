package com.example.easymealbussiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    Spinner spSuscripcion;
    ArrayList<String> lista = new ArrayList<>();
    TextView tvFecha;
    EditText etNombre, etApellidoP, etApellidoM, etUsuario, etContrasenia,  etTitulo;

    String URL = "http://192.168.0.9/easymeal/crudUsuario.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        spSuscripcion = findViewById(R.id.spSuscripcion);
        tvFecha = findViewById(R.id.etFechaNacimiento);
        etNombre = findViewById(R.id.etNombre);
        etApellidoP = findViewById(R.id.etApellidop);
        etApellidoM = findViewById(R.id.etApellidom);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);
        etTitulo = findViewById(R.id.etTitulo);
        spSuscripcion = findViewById(R.id.spSuscripcion);

        lista.add("Seleccione...");
        lista.add("Basico $50 Anual");
        lista.add("Medio $100 Anual");
        lista.add("Avanzado $200 Anual");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lista);

        spSuscripcion.setAdapter(adapter);
    }


    public void ClickFecha(View view) {
        int dia, mes, ano;
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);

        @SuppressLint("SetTextI18n") DatePickerDialog date = new DatePickerDialog(this, (datePicker, anio, mes1, dia1) -> tvFecha.setText(anio +"-"+ (mes1 + 1) +"-"+dia1), ano, mes, dia);
        date.show();
    }

    public void ClickCancelar(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void ClickRegistrarU(View view) {
        if(!camposVacios()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                if(response.equals("INSERTADO EXITOSAMENTE")){
                    Toast.makeText(Registro.this, response, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registro.this, Login.class);
                    intent.putExtra("usuario", etUsuario.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(Registro.this, "USUARIO YA REGISTRADO", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(Registro.this, error.toString(), Toast.LENGTH_SHORT).show()){
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("idUsuario", "");
                    parametros.put("username", etUsuario.getText().toString());
                    parametros.put("clave", etContrasenia.getText().toString());
                    parametros.put("nombre", etNombre.getText().toString());
                    parametros.put("apellidoPaterno", etApellidoP.getText().toString());
                    parametros.put("apellidoMaterno", etApellidoM.getText().toString());
                    parametros.put("fechaNacimiento", tvFecha.getText().toString());
                    parametros.put("titulo", etTitulo.getText().toString());
                    parametros.put("suscripcion", spSuscripcion.getSelectedItem().toString());
                    if(spSuscripcion.getSelectedItemPosition() == 1){
                        parametros.put("clientes", "5");
                    }else if(spSuscripcion.getSelectedItemPosition() == 2){
                        parametros.put("clientes", "15");
                    } else{
                        parametros.put("clientes", "30");
                    }
                    Calendar c = Calendar.getInstance();
                    int dia = c.get(Calendar.DAY_OF_MONTH);
                    int mes = c.get(Calendar.MONTH) + 1;
                    int anio = c.get(Calendar.YEAR) + 1;
                    parametros.put("fechaVencimiento", anio + "-" + mes + "-" + dia);
                    parametros.put("accion", "insertando");

                    return parametros;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean camposVacios(){
        boolean flag = false;
        if(etUsuario.getText().toString().trim().isEmpty()||
            etApellidoP.getText().toString().trim().isEmpty()||
            etApellidoM.getText().toString().trim().isEmpty()||
            etContrasenia.getText().toString().trim().isEmpty()||
            etTitulo.getText().toString().trim().isEmpty()||
            etNombre.getText().toString().trim().isEmpty()||
            tvFecha.getText().toString().trim().equals("--/--/--")||
            spSuscripcion.getSelectedItemPosition() == 0){
            flag = true;
        }
        return flag;
    }
}