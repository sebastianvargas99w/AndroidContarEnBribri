package ac.ucr.paracontar.entidades;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ucr.paracontar.R;
import java.util.ArrayList;

public class Mazo implements Parcelable {

    private final int CANTIDAD_CARTAS_POR_MANO = 7;
    private ArrayList<Carta> cartas;
    // Ptr a la siguiente carta a comer en el mazo

    public Mazo() {
        this.cartas = new ArrayList<>();
        this.crearMazo();
        this.barajar();
    }

    /**
     * Cambia el orden del arreglo cartas
     * pendiente: si se agrega una arreglo como parámetro entoces las cartas de ese arreglo
     * se pasan al mazo o arreglo "cartas"
     */
    @SafeVarargs
    public final void barajar(ArrayList<Carta>... parametroMano) {
        //agregar cartas de la mano a las cartas del mazo
        if(parametroMano.length > 0) {
            ArrayList<Carta> mano = parametroMano[0];
            for(int cartaActual = 1; cartaActual < mano.size(); cartaActual++) {
                if(mano.get(cartaActual) != null) {
                    cartas.add(mano.get(cartaActual));
                }
            }
        }
        ArrayList<Carta> nuevasCartas = new ArrayList<>(cartas.size());
        nuevasCartas.add(null);
        while (cartas.size() > 1) {
            int aleatorio = generarNumeroAleatorio(cartas.size());
            nuevasCartas.add(cartas.remove(aleatorio));
        }
        cartas = nuevasCartas;
    }

    /**
     * el maximo es exclusivo y el mínimo inclusido
     */
    public int generarNumeroAleatorio(int maximo) {
        int MINIMO = 1;
        return (int) (Math.random() * (maximo - (MINIMO + 1) + 1) + (MINIMO + 1)) - 1;
    }


    /**
     * Al llamar el metodo, se seleccionan las cartas del mazo, se sacan del mazo
     * (porque llaman al metodo de comer() ) y se retornan para que el jugador las
     * maneje en JuegoActivity.
     * **/
    public ArrayList<Carta> agarrarMano() {
        ArrayList<Carta> mano = new ArrayList<>();
        mano.add(null);
        for(int it = 1; it <= CANTIDAD_CARTAS_POR_MANO; ++it){
            mano.add(this.comer());
        }
        //Log.d("ERROR", "El mazo tiene ahora " + cartas.size() + " cartas.");
        return mano;
    }

    /**
     * Al llamar el metodo, se seleccionan las cartas del mazo, se sacan del mazo
     * (porque llaman al metodo de comer() ) y se retornan para que el jugador las
     * maneje en JuegoActivity. La diferencia con el otro metodo es que se asegura
     * que al menos la ultima carta a agregar tenga pareja en la mano
     * **/
    public ArrayList<Carta> agarrarManoModoOrdenado() {
        ArrayList<Carta> mano = new ArrayList<>();
        mano.add(null);
        // Itera sobre la cantidad menos uno
        for(int it = 1; it < CANTIDAD_CARTAS_POR_MANO; ++it){
            mano.add(this.comer());
        }
        //La ultima carta que va a comer, asegurara que tenga pareja
        mano.add(this.comerModoOrdenado(mano));
        return mano;
    }


    /**
     * Devuelve un arreglo con nuevas cartas para asignarlo a la mano.
     * Ocupa una mano(arreglo con cartas) para tomar en cuenta los espacios reservados con nulos
     * y los mantiene nulos
     */
    public ArrayList<Carta> reemplazarMano(ArrayList<Carta> manoActual) {
        ArrayList<Carta> nuevaMano = new ArrayList<>();
        nuevaMano.add(null); // agrega nulo para reservar el primer espacio del arreglo.
        for (int cartaActual = 1; cartaActual < manoActual.size(); ++cartaActual) {
            //Si la mano contiene nulo en esa posición.
            if(manoActual.get(cartaActual) != null) {
                nuevaMano.add(this.comer());
            }
            else {
                nuevaMano.add(null);
            }
        }
        return nuevaMano;
    }

    /**
     * Devuelve un arreglo con nuevas cartas para asignarlo a la mano.
     * Ocupa una mano(arreglo con cartas) para tomar en cuenta los espacios reservados con nulos
     * y los mantiene nulos
     */
    public ArrayList<Carta> reemplazarManoModoOrdenado(ArrayList<Carta> manoActual) {
        ArrayList<Carta> nuevaMano = new ArrayList<>();
        nuevaMano.add(null); // agrega nulo para reservar el primer espacio del arreglo.
        for (int cartaActual = 1; cartaActual < manoActual.size(); ++cartaActual) {
            //Si la mano contiene nulo en esa posición.
            if(manoActual.get(cartaActual) != null) {
                // En modo ordenado debe asegurar que al menos una carta tiene pareja
                if(cartaActual == manoActual.size() - 1) {
                    nuevaMano.add(this.comerModoOrdenado(nuevaMano));
                } else {
                    nuevaMano.add(this.comer());
                }
            }
            else {
                nuevaMano.add(null);
            }
        }
        return nuevaMano;
    }

    /**
     * Metodo para comer una carta del mazo. Cada vez que se come, la carta
     * seleccionada sera borrada del mazo.
     **/
    public Carta comer() {
        Carta aComer = null;
        if(cartas.size() > 1) {
            aComer = cartas.remove(cartas.size() - 1);
        }
        else {
            Log.d("ERROR", "El mazo esta vacio.");
            return null;
        }
        return aComer;
    }

    /**
     * Metodo para comer una carta del mazo en el modo ordenado.
     * Cada vez que se come, la carta seleccionada sera borrada del mazo.
     * La diferencia con el metodo comer, recae en que este metodo asegura
     * que siempre al menos una carta tendrá pareja en la mano
     **/
    public Carta comerModoOrdenado( ArrayList<Carta> mano) {
        for(int cartaMazoIt = 1; cartaMazoIt < this.cartas.size(); ++cartaMazoIt) {
            Carta cartaMazo = this.cartas.get(cartaMazoIt);
            // Se itera sobre la mano para asegurar pareja
            for(int manoIt = 1; manoIt < mano.size(); ++manoIt) {
                Carta manoCarta = mano.get(manoIt);
                // Si la carta tentativa a sacar si tiene pareja, entonces se retorna
                if ( manoCarta != null && cartaMazo != null &&
                        cartaMazo.getCategoria().equals(manoCarta.getCategoria())
                        && cartaMazo.getCantidad() == manoCarta.getCantidad()) {
                    // misma carta que cartaMazo, pero se usa remove para eliminarla del mazo.
                    return this.cartas.remove(cartaMazoIt);
                }
            }
        }
        if(cartas.size() > 1){
            // Si no esta vacio, da cualquier carta.
            return this.cartas.remove(1);
        }
        // Ya no hay suficientes cartas para asegurar pareja
        return null;
    }

    /**
     * Agrega nueva carta al mazo debido a un intercambio. Se remueve el mazo despues
     **/
    public void agregarCarta(Carta carta){
        this.cartas.add(carta);
        this.barajar();
    }

    public void crearMazoTest() {
        /** Cateogria Redonda **/
        cartas.add(null);
        // Cantidad 2
        cartas.add(new Carta("redonda", 2, "dos piedras", R.drawable.dos_piedras_grandes));
        cartas.add(new Carta("redonda", 2, "dos caracoles", R.drawable.dos_caracoles_grandes));
        cartas.add(new Carta("redonda", 2, "dos casas cónicas", R.drawable.dos_casas_grandes));
        cartas.add(new Carta("redonda", 2, "dos guacales", R.drawable.dos_guacales_grandes));
        cartas.add(new Carta("redonda", 2, "dos conejos", R.drawable.dos_conejos_grandes));
        cartas.add(new Carta("redonda", 2, "dos guatusas", R.drawable.dos_guatusas_grandes));
        cartas.add(new Carta("redonda",1, "un cangrejo",R.drawable.un_cangrejo_grande));
        cartas.add(new Carta("redonda",1, "un ayote",R.drawable.un_ayote_grande));
    }

    /**
     * Metodo que crea un mazo al iniciar la partida. El mazo contiene todas las cartas del juego y
     * luego es cuando se sacan las primeras cartas para que sean la mano del jugador. La primera carta
     * del mazo es nula para seguir un estandar de manejo de cartas en arrays.
     **/

    //las que tienen comentarios vacíos al final están revisadas
    public void crearMazo(){
        /** Cateogria Redonda **/
        cartas.add(null);
        // Cantidad 1
        cartas.add(new Carta("redonda",1, "un cangrejo",R.drawable.un_cangrejo_grande));//
        cartas.add(new Carta("redonda",1, "un ayote",R.drawable.un_ayote_grande));//
        cartas.add(new Carta("redonda",1, "un chayote",R.drawable.un_chayote_grande));//
        cartas.add(new Carta("redonda",1, "una canasta",R.drawable.una_canasta_grande));//
        cartas.add(new Carta("redonda",1, "una mazorca",R.drawable.una_mazorca_grande));//
        cartas.add(new Carta("redonda",1, "una bola de fútbol",R.drawable.una_bola_futbol_grande));//
        // Cantidad 2
        cartas.add(new Carta("redonda", 2, "dos piedras", R.drawable.dos_piedras_grandes));//
        cartas.add(new Carta("redonda", 2, "dos caracoles", R.drawable.dos_caracoles_grandes));//
        cartas.add(new Carta("redonda", 2,"dos casas cónicas", R.drawable.dos_casas_grandes));//
        cartas.add(new Carta("redonda", 2, "dos guacales", R.drawable.dos_guacales_grandes));//
        cartas.add(new Carta("redonda", 2, "dos conejos", R.drawable.dos_conejos_grandes));//
        cartas.add(new Carta("redonda", 2, "dos guatusas", R.drawable.dos_guatusas_grandes));//
        // Cantidad 3
        cartas.add(new Carta("redonda",3, "tres huevos",R.drawable.tres_huevos_grandes));//
        cartas.add(new Carta("redonda",3, "tres pejibayes",R.drawable.tres_pejibayes_grandes));//
        cartas.add(new Carta("redonda",3, "tres aguacates",R.drawable.tres_aguacates_grandes));//
        cartas.add(new Carta("redonda",3, "tres ollas",R.drawable.tres_ollas_grandes));//
        cartas.add(new Carta("redonda",3, "tres guanábanas",R.drawable.tres_guanabanas_grandes));//
        cartas.add(new Carta("redonda",3, "tres zapotes",R.drawable.tres_zapotes_grandes));//

        /** Categoria Alargada **/
        // Cantidad 1
        cartas.add(new Carta("alargada",1, "una iguana",R.drawable.una_iguana_grande));//
        cartas.add(new Carta("alargada",1, "un mono",R.drawable.un_mono_grande));//
        cartas.add(new Carta("alargada",1, "una lanza",R.drawable.una_lanza_grande));//
        cartas.add(new Carta("alargada",1, "un camino",R.drawable.un_camino_grande));//
        cartas.add(new Carta("alargada",1, "una culebra",R.drawable.una_culebra_grande));//
        cartas.add(new Carta("alargada",1, "un río",R.drawable.un_rio_grande));//
        // Cantidad 2
        cartas.add(new Carta("alargada",2, "dos mecates",R.drawable.dos_mecates_grandes));//
        cartas.add(new Carta("alargada",2, "dos jaguares",R.drawable.dos_jaguares_grandes));//
        cartas.add(new Carta("alargada",2, "dos venados",R.drawable.dos_venados_grandes));//
        cartas.add(new Carta("alargada",2, "dos hachas",R.drawable.dos_hachas_grandes));//
        cartas.add(new Carta("alargada",2, "dos lagartos",R.drawable.dos_lagartos_grandes));//
        cartas.add(new Carta("alargada",2, "dos chanchos",R.drawable.dos_chanchos_grandes));//
        // Cantidad 3
        cartas.add(new Carta("alargada",3, "tres cuchillos",R.drawable.tres_cuchillos_grandes));//
        cartas.add(new Carta("alargada",3, "tres dantas",R.drawable.tres_dantas_grandes));//
        cartas.add(new Carta("alargada",3, "tres tambores",R.drawable.tres_tambores_grandes));//
        cartas.add(new Carta("alargada",3, "tres cerbatanas",R.drawable.tres_cerbanatas_grandes));//
        cartas.add(new Carta("alargada",3, "tres  casas rectangulares",R.drawable.tres_casas_rectangulares_grandes));//
        cartas.add(new Carta("alargada",3, "tres palos",R.drawable.tres_palos_grandes));//

        /** Categoria Plana **/
        // Cantidad 1
        cartas.add(new Carta("plana",1, "una cama",R.drawable.una_cama_grande));//
        cartas.add(new Carta("plana",1, "un tamal",R.drawable.un_tamal_grande));//
        cartas.add(new Carta("plana",1, "una mosca",R.drawable.una_mosca_grande));//
        cartas.add(new Carta("plana",1, "una araña",R.drawable.una_arana_grande));//
        cartas.add(new Carta("plana",1, "una hamaca",R.drawable.una_hamaca_grande));
        cartas.add(new Carta("plana",1, "un asiento",R.drawable.un_asiento_grande));//
        // Cantidad 2
        cartas.add(new Carta("plana",2, "dos kae",R.drawable.dos_kae_grandes));//
        cartas.add(new Carta("plana",2, "dos libros",R.drawable.dos_libros));//
        cartas.add(new Carta("plana",2, "dos gallinas",R.drawable.dos_gallinas_grandes));//
        cartas.add(new Carta("plana",2, "dos mariposas",R.drawable.dos_mariposas_grandes));//
        cartas.add(new Carta("plana",2, "dos abejas",R.drawable.dos_abejas_grandes));//
        cartas.add(new Carta("plana",2, "dos camisas",R.drawable.dos_camisas_grandes));//
        // Cantidad 3
        cartas.add(new Carta("plana",3, "tres quetzales",R.drawable.tres_quetzales_grandes));//
        cartas.add(new Carta("plana",3, "tres zompopas",R.drawable.tres_zompopas_grandes));//
        cartas.add(new Carta("plana",3, "tres peces",R.drawable.tres_peces_grandes));//
        cartas.add(new Carta("plana",3, "tres cucarachas",R.drawable.tres_cucarachas_grandes));//
        cartas.add(new Carta("plana",3, "tres pantalones",R.drawable.tres_pantalones_grandes));//
        cartas.add(new Carta("plana",3, "tres bolsos",R.drawable.tres_bolsos));//
    }

// ----------------- Metodos autogenerados ------------------

    protected Mazo(Parcel in) {
        cartas = in.createTypedArrayList(Carta.CREATOR);
    }

    /**
     * Para hacer pruebas
     */
    public ArrayList<Carta> obtenerCartas() {
        return cartas;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cartas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mazo> CREATOR = new Creator<Mazo>() {
        @Override
        public Mazo createFromParcel(Parcel in) {
            return new Mazo(in);
        }

        @Override
        public Mazo[] newArray(int size) {
            return new Mazo[size];
        }
    };

}
