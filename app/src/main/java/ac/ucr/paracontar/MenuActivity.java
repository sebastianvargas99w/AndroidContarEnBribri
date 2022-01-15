package ac.ucr.paracontar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ucr.paracontar.R;

import ac.ucr.paracontar.servicios.ModificadorInterfaz;
import ac.ucr.paracontar.submenu.AclaracionActivity;
import ac.ucr.paracontar.submenu.AyudaActivity;
import ac.ucr.paracontar.submenu.CreditosActivity;

public class MenuActivity extends AppCompatActivity {

    final String PARCELABLE_NAME = "modo_de_juego";
    final String GAME_MODE_1 = "mazo_ordenado";
    final String GAME_MODE_2 = "mazo_desordenado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ModificadorInterfaz modificadorInterfaz = new ModificadorInterfaz(this.getApplicationContext(), this.getWindow());
        modificadorInterfaz.setColorStatusBar();
        establecerListeners();
    }


    public void establecerListeners() {
        // Modo de Juego Mazo Ordenado
        Button mazo_ordenado = (Button) findViewById(R.id.btn_modo_1);
        mazo_ordenado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irJuego(GAME_MODE_1);
            }
        });

        // Modo de Juego Mazo Desordenado
        Button mazo_desordenado = (Button) findViewById(R.id.btn_modo_2);
        mazo_desordenado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irJuego(GAME_MODE_2);
            }
        });

        // Instrucciones
        Button instrucciones = (Button) findViewById(R.id.btn_instrucciones);
        instrucciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irInstrucciones();
            }
        });

        // Creditos
        Button creditos = (Button) findViewById(R.id.btn_creditos);
        creditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irCreditos();
            }
        });

        // Aclaracion
        Button aclaracion = (Button) findViewById(R.id.btn_aclaracion);
        aclaracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAclaracion();
            }
        });
    }

    public void irJuego(String modo_de_juego){
        Intent intent = new Intent(this, JuegoActivity.class);
        intent.putExtra(PARCELABLE_NAME, modo_de_juego);
        startActivity(intent);
    }

    public void irCreditos(){
        Intent intent = new Intent(this, CreditosActivity.class);
        startActivity(intent);
    }

    public void irInstrucciones(){
        Intent intent = new Intent(this, AyudaActivity.class);
        startActivity(intent);
    }

    public void irAclaracion(){
        Intent intent = new Intent(this, AclaracionActivity.class);
        startActivity(intent);
    }


    /** ----- Opciones del Menu ---- **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_credits) {

            Intent intent = new Intent(this, CreditosActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_aclaration) {
            Intent intent = new Intent(this, AclaracionActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_help) {
            Intent intent = new Intent(this, AyudaActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}