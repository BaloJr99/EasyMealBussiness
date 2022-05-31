package com.example.easymealbussiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuPrincipal extends AppCompatActivity {

    DrawerLayout dl;
    static int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        dl = findViewById(R.id.drawer_menu);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            idUsuario = bundle.getInt("idUsuario");
        }
    }

    public void ClickMenu(View view) {
        //Abrimos drawer
        openDrawer(dl);
    }

    public static void openDrawer(DrawerLayout drawer) {
        //Abrimos el drawer layout
        drawer.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //Cerramos el drawer
        closeDrawer(dl);
    }

    public static void closeDrawer(DrawerLayout drawer) {
        //Cerramos el layout si esta abierto
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //Cuando este abierto cerramos el drawer
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickInicio(View view) {
        //Recreamos actividad
        recreate();
    }

    public void ClickSalir(View view) {
        //Cerramos app
        logout(this);
    }

    public void ClickHorario(View v) {

    }

    public void ClickChats(View v) {
        redirectActivity(this, Mensajes.class);
    }

    public void ClickUsuario(View v) {
        redirectActivity(this, MenuUsuario.class);
    }

    public static void logout(Activity activity) {
        //Inicializamos mensaje de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Establecemos el titulo
        builder.setTitle("Logout");
        //Establecemos el mensaje
        builder.setMessage("Estas seguro de que desea salir?");
        //Boton de seguro
        builder.setPositiveButton("YES", (dialogInterface, i) -> {
            Intent intent = new Intent(activity, Login.class);
            activity.startActivity(intent);
        });

        //Boton negativo

        builder.setNegativeButton("NO", (dialogInterface, i) -> {
            //Hacemos caso omiso al dialogo
            dialogInterface.dismiss();
        });

        //Mostramos el dialogo
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Inicializamos el intento
        Intent intent = new Intent(activity, aClass);
        //Creamos bandera
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Empezamos activity
        intent.putExtra("idUsuario", idUsuario);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Cerramos drawer
        closeDrawer(dl);
    }
}