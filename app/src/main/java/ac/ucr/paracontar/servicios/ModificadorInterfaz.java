package ac.ucr.paracontar.servicios;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ucr.paracontar.R;

import java.util.Objects;

public class ModificadorInterfaz extends AppCompatActivity {
    Context contexto;
    Window ventana;
    public  ModificadorInterfaz(Context nuevoContexto, Window nuevaVentana){
        this.contexto = nuevoContexto;
        this.ventana = nuevaVentana;
    }

    /**
     * Cambia el color de la barra de status (barra de arriba)
     * */
    public void setColorStatusBar() {
        this.ventana.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        this.ventana.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        this.ventana.setStatusBarColor(ContextCompat.getColor(contexto, R.color.primary_verde));
    }
}
