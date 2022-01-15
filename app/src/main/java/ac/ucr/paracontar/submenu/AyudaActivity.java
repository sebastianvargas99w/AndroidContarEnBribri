package ac.ucr.paracontar.submenu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableRow;
import android.widget.TextView;

import com.ucr.paracontar.R;

import java.util.Objects;

public class AyudaActivity extends AppCompatActivity {

    boolean instruccionesEstaVisible = false;
    boolean ayudaEstaVisible = false;
    boolean tablasEstaVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        setHeaderListener();
        setColorStatusBar();
    }

    public void setHeaderListener(){
        /* Header de las instrucciones del juego */
        TableRow header = findViewById(R.id.tableRowHeaderInstructions);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instruccionesEstaVisible) {
                    findViewById(R.id.content_instructions).setVisibility(View.GONE);
                    findViewById(R.id.add_class_time_image_up).setVisibility(View.GONE);
                    findViewById(R.id.add_class_time_image).setVisibility(View.VISIBLE);
                    instruccionesEstaVisible = false;
                } else {
                    findViewById(R.id.content_instructions).setVisibility(View.VISIBLE);
                    findViewById(R.id.add_class_time_image_up).setVisibility(View.VISIBLE);
                    findViewById(R.id.add_class_time_image).setVisibility(View.GONE);
                    instruccionesEstaVisible = true;
                }
            }
        });

        /* Header de la opcion de ayuda del juego */
        TableRow header_ayuda = findViewById(R.id.tableRowHeaderHelp);
        header_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ayudaEstaVisible) {
                    findViewById(R.id.content_help).setVisibility(View.GONE);
                    findViewById(R.id.add_class_time_image_up_2).setVisibility(View.GONE);
                    findViewById(R.id.add_class_time_image_2).setVisibility(View.VISIBLE);
                    ayudaEstaVisible = false;
                } else {
                    findViewById(R.id.content_help).setVisibility(View.VISIBLE);
                    findViewById(R.id.add_class_time_image_up_2).setVisibility(View.VISIBLE);
                    findViewById(R.id.add_class_time_image_2).setVisibility(View.GONE);
                    ayudaEstaVisible = true;
                }
            }
        });

        /* Header de la opcion de imagenes de tablas del juego */
        TableRow header_tablas = findViewById(R.id.tableRowHeaderImages);
        header_tablas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tablasEstaVisible) {
                    findViewById(R.id.content_images).setVisibility(View.GONE);
                    findViewById(R.id.flecha_abajo).setVisibility(View.GONE);
                    findViewById(R.id.flecha_arriba).setVisibility(View.VISIBLE);
                    tablasEstaVisible = false;
                } else {
                    findViewById(R.id.content_images).setVisibility(View.VISIBLE);
                    findViewById(R.id.flecha_abajo).setVisibility(View.VISIBLE);
                    findViewById(R.id.flecha_arriba).setVisibility(View.GONE);
                    tablasEstaVisible = true;
                }
            }
        });
    }

    public void setColorStatusBar() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.primary_amarillo));
        // Cambia el color de la barra de arriba (primary color)
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                new ColorDrawable(this.getResources().getColor(R.color.primary_amarillo)));
    }
}