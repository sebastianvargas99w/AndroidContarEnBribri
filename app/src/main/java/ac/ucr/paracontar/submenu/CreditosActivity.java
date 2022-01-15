package ac.ucr.paracontar.submenu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.ucr.paracontar.R;
import ac.ucr.paracontar.servicios.ModificadorInterfaz;

import java.util.Objects;

public class CreditosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ModificadorInterfaz modificadorInterfaz = new ModificadorInterfaz(this.getApplicationContext(), this.getWindow());
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(
                new ColorDrawable(this.getResources().getColor(R.color.primary_verde)));
        modificadorInterfaz.setColorStatusBar();

        setContentView(R.layout.activity_creditos);
    }
}