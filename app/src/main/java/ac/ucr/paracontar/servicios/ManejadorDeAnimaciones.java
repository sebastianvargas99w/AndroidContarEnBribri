package ac.ucr.paracontar.servicios;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.ucr.paracontar.R;

public class ManejadorDeAnimaciones {
    public ManejadorDeAnimaciones (){}

    /**
     * Hace una aminación para desaparecer una carta
     * */
    public void animarOcultarCarta(ImageView imagenCarta){

        Animation fadeOut = new AlphaAnimation(1, 0); //se crea objeto con la cofiguracion de la animacion
        // Hace que la animación empiece lento y que aumente su velocidad conforme pasa el tiempo
        fadeOut.setInterpolator(new AccelerateInterpolator());

        fadeOut.setStartOffset(1000); //pone retardo para que empiece empiece la animación
        fadeOut.setDuration(1000);



        AnimationSet animation = new AnimationSet(false); //crea un conjunto de animaciones para un elemento.
        animation.addAnimation(fadeOut);
        imagenCarta.setAnimation(animation);
        imagenCarta.startAnimation(animation);
    }

    /**
     * Hace una aminación para desaparecer una carta
     * ver documentación interna de animarOcultarCarta para más detalles
     * */
    public void animarMostrarCarta(ImageView imagenCarta){

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setStartOffset(0);
        fadeIn.setDuration(1000);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        imagenCarta.setAnimation(animation);
        imagenCarta.startAnimation(animation);
    }

    /**
     * Animación para simular que se voltea una carta, el primer parámetro indica el elemento de la interfaz
     * que se va a cambiar y el segundo el id de la imagen que se va a poner en su lugar.
     *
     * resumen: inicia la primera parte de la animacion, pone la imagen de la parte de atrás de la cartya y
     * luego hace la segunda parte de la animación
     *
     * la tercera y cuarta parte son similares a la primera y segunda, pero en estas partes se pone la imagen
     * final en lugar de la parte de atrás de la carta
     */
    public void animarCambiarCarta(ImageView imagenCarta, int idNuevaImagen) {
        final int DURACION = 1000;
        final float ACELERACION = 1f;

        ObjectAnimator animacionParte1; //rota la imagen 90 grados
        animacionParte1 = ObjectAnimator.ofFloat(imagenCarta, "scaleX", 1f, 0f);
        animacionParte1.setDuration(DURACION);
        animacionParte1.setInterpolator(new DecelerateInterpolator(ACELERACION));

        ObjectAnimator animacionParte2; //rota la imagen 90 grados
        animacionParte2 = ObjectAnimator.ofFloat(imagenCarta, "scaleX", 0f, 1f);
        animacionParte2.setDuration(DURACION);
        animacionParte2.setInterpolator(new AccelerateInterpolator(ACELERACION));

        ObjectAnimator animacionParte3; //rota la imagen 90 grados
        animacionParte3 = ObjectAnimator.ofFloat(imagenCarta, "scaleX", 1f, 0f);
        animacionParte3.setDuration(DURACION);
        animacionParte3.setInterpolator(new DecelerateInterpolator(ACELERACION));

        ObjectAnimator animacionParte4; //rota la imagen 90 grados
        animacionParte4 = ObjectAnimator.ofFloat(imagenCarta, "scaleX", 0f, 1f);
        animacionParte4.setDuration(DURACION);
        animacionParte4.setInterpolator(new AccelerateInterpolator(ACELERACION));
        
        // escucha que se ejecuta cuando la animación termina
        animacionParte1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animacion) {
                super.onAnimationEnd(animacion);
                imagenCarta.setImageResource(R.drawable.atras_carta);
                animacionParte2.start();
            }
        });

        // escucha que se ejecuta cuando la animación termina
        animacionParte2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animacion) {
                super.onAnimationEnd(animacion);
                animacionParte3.start();
            }
        });

        // escucha que se ejecuta cuando la animación termina
        animacionParte3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animacion) {
                super.onAnimationEnd(animacion);
                imagenCarta.setImageResource(idNuevaImagen);
                animacionParte4.start();
            }
        });
        // inicia la primera parte de la animación, la cuál va a llamar a las otras partes
        animacionParte1.start();
    }

}
