package ac.ucr.paracontar.servicios;

import android.util.Log;

import ac.ucr.paracontar.entidades.Carta;
import ac.ucr.paracontar.entidades.Mazo;

import java.util.ArrayList;

public class ManejadorDeReglas {

    final int MAZO_VACIO = 1;
    boolean esModoOrdenado;
    Mazo mazo;
    ArrayList<Carta> mano; //arreglo con las imagenes de la mano

    /* ---------- Business logic methods  ---------- */

    /**
     * Revisa si se cumplen las condiciones de categoria y cantidad para
     * poder emparejar dos cartas.
     **/
    public boolean emparejarCartas(Carta primeraCarta, Carta segundaCarta) {
        boolean sonPareja = false;
        if (primeraCarta.getCategoria().equals(segundaCarta.getCategoria()) &&
            primeraCarta.getCantidad() == segundaCarta.getCantidad()) {
            sonPareja = true;
        }
        return sonPareja;
    }

    /**
     * Cuando se falla mas de una cantidad de veces definida, se castiga al usuario.
     * Este metodo revuelve las cartas de la mano con nuevas cartas del mazo.
     * Es decir, se le cambia la mano al usuario.
     **/
    public void castigarEmparejamiento() {
        ArrayList<Carta> copiaMano = copiarArreglo(mano); //copia la referencia de las cartas
        mazo.barajar(copiaMano); // devuelve la mano vieja al mazo y lo revuelve
        if(getModoOrdenado()) {
            mano = mazo.reemplazarManoModoOrdenado(mano); // saca las cartas del mazo
        } else {
            mano = mazo.reemplazarMano(mano); // saca las cartas del mazo
        }
    }

    /**
     * Metodo que saca una nueva carta del mazo y la pone en la mano
     **/
    public Carta comer(){
        if (getModoOrdenado()) {
            Carta carta = mazo.comerModoOrdenado(getMano());
            return carta;
        } else {
            return mazo.comer();
        }
    }

    /**
     * Agrega una nueva carta a la mano.
     * Tambien puede ser que elimina, si la nuevaCarta es nula.
     **/
    public void agregarCartaMano(int posicion, Carta nuevaCarta){
        mano.set(posicion, nuevaCarta);
    }

    /**
     * Agrega una nueva carta al mazo.
     **/
    public void agregarCartaMazo(Carta nuevaCarta){ mazo.agregarCarta(nuevaCarta); }

    /**
     * Revisa que la carta exista y sea valida.
     **/
    public boolean cartaSeleccionada(int numeroCarta){ return mano.get(numeroCarta) != null; }

    /**
     * Copia la mano para crear una nueva.
     **/
    private ArrayList<Carta> copiarArreglo(ArrayList<Carta> arreglo) {
        ArrayList<Carta> nuevoArreglo = new ArrayList<>(arreglo.size());
        for(int posicion = 0; posicion < arreglo.size() ;posicion++) {
            nuevoArreglo.add(arreglo.get(posicion));
        }
        return nuevoArreglo;
    }

    /**
     * Revisa si la mano tiene todas sus posiciones nulas. En ese caso,
     * la mano esta vacia.
     * */
    public boolean manoEstaVacia(){
        int contador = 0;
        for(int posicion = 0; posicion < mano.size() ;posicion++) {
            if(mano.get(posicion) == null) contador++;
        }
        return contador == mano.size();
    }

    /**
     * Revisa si la cantidad de cartas del mazo es 1 (posicion nula,
     * es decir, mazo vacio) y si la mano esta vacia, En ese caso,
     * se ha acabado el juego.
     * */
    public boolean revisarCondicionVictoria() {
        // Igual a 1 porque siempre tendra la primera pos nula
        return mazo.obtenerCartas().size() == MAZO_VACIO &&
                manoEstaVacia();
    }

    /** ---------- Constructor y Getters  ---------- **/
    public ManejadorDeReglas(boolean esModoOrdenado) {
        this.esModoOrdenado = esModoOrdenado;
        // Inicializa el mazo para el juego
        mazo = new Mazo();
        mano = this.agarrarMano();
    }

    /**
     * Retorna la carta de la mano ubicada en el indice
     * que se proporciona por parametro.
     **/
    public Carta getCartaMano(int indiceCarta){ return mano.get(indiceCarta); }

    public boolean getModoOrdenado() {
        return esModoOrdenado;
    }

    /**
     * Crea la mano del jugador. Saca las cartas del mazo.
     **/
    public ArrayList<Carta> agarrarMano(){
        if (getModoOrdenado()){
            return mazo.agarrarManoModoOrdenado();
        }
        Log.d("ERROR", "El mazo al final del algoritmo tiene " + getMazo().size() + " cartas.");
        return mazo.agarrarMano();
    }

    public ArrayList<Carta> getMano(){ return mano; }

    public ArrayList<Carta> getMazo(){ return mazo.obtenerCartas(); }

}
