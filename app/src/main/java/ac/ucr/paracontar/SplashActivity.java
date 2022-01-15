package ac.ucr.paracontar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ucr.paracontar.R;

import ac.ucr.paracontar.servicios.ModificadorInterfaz;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        establecerListener();
        ModificadorInterfaz modificadorInterfaz = new ModificadorInterfaz(this.getApplicationContext(), this.getWindow());
        modificadorInterfaz.setColorStatusBar();
    }

    public void establecerListener(){
        ImageView slash = (ImageView) findViewById(R.id.imageViewSplash);
        slash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irMenu();
            }
        });
    }

    public void irMenu()
    {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}