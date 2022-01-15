package ac.ucr.paracontar.entidades;

import static org.junit.Assert.*;

import org.junit.Test;

public class MazoTest {

    @Test
    public void generarNumeroAleatorio() {
        Mazo mazo = new Mazo(); //poner cualquier modo cuando se cree el constructor con modo
        int LIMITE_SUPERIOR = mazo.obtenerCartas().size();
        int CANTIDAD_EJECUCIONES = 100000;
        for (int it = 0; it < CANTIDAD_EJECUCIONES; it++) {
            boolean sirvio = false;
            int resultado = mazo.generarNumeroAleatorio(LIMITE_SUPERIOR);
            // Log.d("resultado: ", Integer.toString(resultado));
            // Log.d("tamaño: ", Integer.toString(mazo.obtenerCartas().size()));
            if (resultado < LIMITE_SUPERIOR && resultado > 0) {
                sirvio = true;
            }
            assertTrue(sirvio);
        }
    }
}