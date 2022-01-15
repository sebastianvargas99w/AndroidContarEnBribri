package ac.ucr.paracontar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ucr.paracontar.R;

import ac.ucr.paracontar.entidades.Carta;
import ac.ucr.paracontar.servicios.ManejadorDeAnimaciones;
import ac.ucr.paracontar.servicios.ManejadorDeReglas;
import ac.ucr.paracontar.servicios.ServicioAudio;
import ac.ucr.paracontar.submenu.AyudaActivity;


public class JuegoActivity extends AppCompatActivity {
    
    final int AUDIO_INCORRECTO = R.raw.incorrecto1;
    final int AUDIO_DEVOLVER = R.raw.incorrecto2;
    final int TAMANO_MANO = 7;

    final String PARCELABLE_NAME = "modo_de_juego";
    final String GAME_MODE_1 = "mazo_ordenado";
    final String GAME_MODE_2 = "mazo_desordenado";
    final int SIN_SELECCIONAR = -1; // numero que representa que no hay cartas seleccionadas en la mano
    final int MAXIMO_MANO = 7; // maximo de cantidad de cartas en la mano
    final int ESPACIO_INVALIDO = -1;
    final int PRIMER_ESPACIO_NULO = 0;
    final int MAX_CANTIDAD_INTENTOS = 4;
    int intentos_fallidos;
    int cantidad_cartas_inicial;
    public boolean esModoOrdenado;
    private Button boton_comer;
    ServicioAudio servicioAudio;
    ManejadorDeReglas manejadorDeReglas;
    ManejadorDeAnimaciones manejadorDeAnimaciones;

    /**
     * matriz representativa de id de las cartas
     * _______
     * |1|3|5|
     * |2|4|6|
     * -------
     * */
    // Los numeros corresponden a las posiciones del siguiente vector.
    // Comienza en 1 para que los índicen coincidan con los números de la interfaz y el primer elemento es nulo.
    private ImageView[] manoImagenes; //arreglo con las imagenes de la mano
    private int cartaSeleccionada = SIN_SELECCIONAR; // numero que representa qué carta está seleccionada
    private int cartaSeleccionadaComer = SIN_SELECCIONAR; // numero que representa qué carta está seleccionada

    private ImageButton boton_ayuda;
    private ImageButton boton_volumen; // botón para silenciar o habilitar audios
    private ImageButton boton_cerrar;

    private TextView textoIntentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        getDetallesDelJuego();
        // Inicializa servicio de Manejador De Reglas, donde llevara la logica
        manejadorDeReglas = new ManejadorDeReglas(esModoOrdenado);
        manejadorDeAnimaciones = new ManejadorDeAnimaciones();
        // Agarra la mano y saca esas cartas del mazo
        manoImagenes = new ImageView[MAXIMO_MANO + 1]; //inicialización de variable
        this.obtenerElementosInterfaz();
        this.llenarManoImagenes();
        this.establecerEscuchadores();
        // Inicializa el servicio para reproducir audio
        servicioAudio = new ServicioAudio(JuegoActivity.this);
        // Setea las imagenes de las cartas en mano
        this.setearImagenesMano();
        setColorStatusBar();
        textoIntentos = findViewById(R.id.textViewIntentos);
        //Revisa el modo de juego para verificar si requiere o no comer. Si es ordenado lo ocultara.
        establecerSeccionInferiorDerecha();
        intentos_fallidos = 0;
        // -1 por la primera posicion nula
        cantidad_cartas_inicial = manejadorDeReglas.getMazo().size() - 1;
        actualizarContadorCartas();
    }

    /**
     * usa los ids para obtener los elementos de la interfaz
     */
    private void obtenerElementosInterfaz() {
        boton_ayuda = (ImageButton) findViewById(R.id.boton_ayuda);
        boton_volumen = (ImageButton) findViewById(R.id.boton_volumen);
        boton_cerrar = (ImageButton) findViewById(R.id.boton_cerrar);
        boton_comer = (Button) findViewById(R.id.boton_comer);
    }

    /**
     * Obtiene las imagenes de la interfaz para referenciarlas en la actividad
     * */
    private void llenarManoImagenes() {
        manoImagenes[0] = null;
        manoImagenes[1] = (ImageView) findViewById(R.id.carta_1);
        manoImagenes[2] = (ImageView) findViewById(R.id.carta_2);
        manoImagenes[3] = (ImageView) findViewById(R.id.carta_3);
        manoImagenes[4] = (ImageView) findViewById(R.id.carta_4);
        manoImagenes[5] = (ImageView) findViewById(R.id.carta_5);
        manoImagenes[6] = (ImageView) findViewById(R.id.carta_6);
        manoImagenes[7] = (ImageView) findViewById(R.id.carta_7);
    }

    /**
     * Metodo que itera sobre las cartas que estan en la mano del jugador, y despliega
     * en pantalla la imagen asociada a esa carta.
     * */
    private void setearImagenesMano(){
        for(int it = 1; it <= MAXIMO_MANO; ++it){
            // recupera la imagen de la carta en mano y la setea en el imageview
            if(manejadorDeReglas.getCartaMano(it) != null) {
                manoImagenes[it].setImageResource(
                        manejadorDeReglas.getCartaMano(it).getImagen_id());
            } else {
                ocultarCarta(it);
            }
        }
    }

    /**
     * Le va aponer escuchadores a todos los accionables de la interfaz
     * */
    private void establecerEscuchadores() {

        boton_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAyuda();
            }
        });

        boton_volumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionVolumen();
            }
        });

        boton_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarActividad();
            }
        });

        boton_comer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comerCarta();
            }
        });
        for(int cartaActual = 1; cartaActual < manoImagenes.length; cartaActual++) {
            int copiaCarta_actual = cartaActual;
            // listener para seleccionar carta para jugar, click normal
            manoImagenes[cartaActual].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seleccionarCarta(copiaCarta_actual);
                }
            });
            if ( !manejadorDeReglas.getModoOrdenado() ) {
                // listener para seleccionar carta para comer, click dejando estripado
                manoImagenes[cartaActual].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        seleccionarCartaComer(copiaCarta_actual);
                        return true;
                    }
                });
            }
        }
    }

    /**
     * Selecionar una carta para que sea la que se va a reemplazar por otra
     * del mazo. Solo en modo mazo desordenado.
     **/
    public void seleccionarCartaComer(int numeroCarta) {
        //Se quita carta seleccionada para emparejar para evitar conflictos
        if(numeroCarta == this.cartaSeleccionada) {
            manoImagenes[numeroCarta].setBackgroundResource(R.drawable.estilo_cartas);
            this.cartaSeleccionada = SIN_SELECCIONAR;
        }

        mostrarMensaje("Carta seleccionada para intercambiar.");
        if(manejadorDeReglas.cartaSeleccionada(numeroCarta)) {
            //Si no hay cartas seleccionadas
            if (this.cartaSeleccionadaComer == SIN_SELECCIONAR) {
                //seleccionar la nueva carta
                manoImagenes[numeroCarta].setBackgroundResource(R.drawable.estilo_carta_seleccionada_comer);
                this.cartaSeleccionadaComer = numeroCarta;
            } else { //Si hay cartas seleccionadas
                //Si seleccioné una carta que ya estaba seleccionada quitar selección
                if (this.cartaSeleccionadaComer == numeroCarta) {
                    //Quitar selección de la carta
                    manoImagenes[numeroCarta].setBackgroundResource(R.drawable.estilo_cartas);
                    this.cartaSeleccionadaComer = SIN_SELECCIONAR;
                }
            }
        }
    }

    /**
    * Se encarga de la lógica, cuando se selecciona una carta. Revisa si fue la primera
    * o la segunda carta en ser seleccionada. Si es la segunda, considera el caso de
    * si se pudiera emparejar o no.
    * */
    private void seleccionarCarta(int numeroCarta) {
        //Se quita carta seleccionada para comer para evitar conflictos
        if(numeroCarta == this.cartaSeleccionadaComer) {
            manoImagenes[numeroCarta].setBackgroundResource(R.drawable.estilo_cartas);
            this.cartaSeleccionadaComer = SIN_SELECCIONAR;
        }

        if(manejadorDeReglas.cartaSeleccionada(numeroCarta)) {
            //Si no hay cartas seleccionadas
            if (cartaSeleccionada == SIN_SELECCIONAR) {
                //seleccionar la nueva carta
                manoImagenes[numeroCarta].setBackgroundResource(R.drawable.estilo_carta_seleccionada);
                cartaSeleccionada = numeroCarta;
            } else { //Si hay cartas seleccionadas
                //Si seleccioné una carta que ya estaba seleccionada quitar selección
                if (cartaSeleccionada == numeroCarta) {
                    //Quitar selección de la carta
                    manoImagenes[numeroCarta].setBackgroundResource(R.drawable.estilo_cartas);
                    cartaSeleccionada = SIN_SELECCIONAR;
                } else {// Si ocupo emparejar cartas
                    emparejarCartasSeleccionadas(numeroCarta);
                }
            }
        }
    }

    /**
     * Reproduce el audio que corresponde al número de la imagen
     * */
    private void reproducirAudioEmparejamiento(int numeroCarta) {
        String nombreAudio = "audio_numero";//nombre base del audio
        //nombre compuesto del audio success + cantidad de la carta + categoria
        nombreAudio = nombreAudio + manejadorDeReglas.getCartaMano(numeroCarta).getCantidad() +"_"+manejadorDeReglas.getCartaMano(numeroCarta).getCategoria();
        try {
            int idAudio = getResources().getIdentifier(nombreAudio,"raw",getPackageName());
            servicioAudio.reproducir(idAudio);
            /*
            mostrarMensaje(
                    manejadorDeReglas.getCartaMano(numeroCarta).getCantidad() + " " +
                    manejadorDeReglas.getCartaMano(numeroCarta).getCategoria());
             */
        }
        catch (Exception e){
            mostrarMensaje("Error al reproducir el audio."
                    /*+
                    manejadorDeReglas.getCartaMano(numeroCarta).getCantidad() + " " +
                    manejadorDeReglas.getCartaMano(numeroCarta).getCategoria()*/
            );
        }

    }

    /**
     * Metodo que revisa si se pueden emparejar las 2 cartas seleccionadas.
     * Si se pudiera, entonces empareja y repone las cartas automaticamente.
     * Si no se pudiera, revisa si debe haber castigo para revolver las cartas
     * o solo continuar con el juego.
     * @param numeroCarta = indice de la ultima carta seleccionada. El otro indice
     *                    esta en la variable global cartaSeleccionada.
     **/
    private void emparejarCartasSeleccionadas(int numeroCarta) {
        Carta carta1 = manejadorDeReglas.getCartaMano(numeroCarta);
        Carta carta2 = manejadorDeReglas.getCartaMano(this.cartaSeleccionada);
        // si se pudo emparejar las cartas porque tienen la misma categoría y cantidad
        if (manejadorDeReglas.emparejarCartas(carta1,carta2)) {//si son pareja
            // Reproduce el sonido de que si emparejo correctamente
            this.reproducirAudioEmparejamiento(numeroCarta);
            this.intentos_fallidos = 0;
            String intentos = "Intentos: 4/4";
            textoIntentos.setTextColor(getResources().getColor(R.color.black));
            textoIntentos.setText(intentos);
            reemplazarCartaMano(this.cartaSeleccionada,false, true);
            reemplazarCartaMano(numeroCarta,false, true);
            // Revisa si el juego ya terminó
            if (manejadorDeReglas.revisarCondicionVictoria()){
                mostrarModal("Fin del juego",
                "¡Felicidades por terminar el juego! Presione cerrar para volver al menú principal.");
            }
            actualizarContadorCartas();
        } else {
            //si el usuario emparejo carta incorrectamente, porque no tienen la misma categoría y cantidad
            this.intentos_fallidos ++;
            String intentos = "Intentos: "+ (MAX_CANTIDAD_INTENTOS - this.intentos_fallidos) + "/4";
            textoIntentos.setText(intentos);
            //Se cambia el color del texto, porque es cercano a 0y se catigarán los errores
            if((MAX_CANTIDAD_INTENTOS - this.intentos_fallidos) <= 1) {
                textoIntentos.setTextColor(getResources().getColor(R.color.orange_text));
            }
            if (intentos_fallidos == MAX_CANTIDAD_INTENTOS) {//catigar emparejamiento

                manejadorDeReglas.castigarEmparejamiento();
                setearImagenesMano(); //muestra la nueva mano;
                this.mostrarMensaje("Se acabaron los intentos y se revolvió la mano.");

                //se reinician los intentos
                this.intentos_fallidos = 0; //reinicia contador
                intentos = "Intentos: "+(MAX_CANTIDAD_INTENTOS - this.intentos_fallidos) + "/4";
                textoIntentos.setTextColor(getResources().getColor(R.color.black));
                textoIntentos.setText(intentos);
                servicioAudio.reproducir(AUDIO_DEVOLVER);
            }
            else{
                servicioAudio.reproducir(AUDIO_INCORRECTO);
            }
        }
        //quita selección
        manoImagenes[cartaSeleccionada].setBackgroundResource(R.drawable.estilo_cartas);
        this.cartaSeleccionada = SIN_SELECCIONAR;
    }

    /**
     * Metodo para ir a la pagina de ayuda
     **/
    private void irAyuda() {
        Intent intent = new Intent(this, AyudaActivity.class);
        startActivity(intent);
    }

    /**
     * Habilita o deshabilita los audios, cambia el icono de la interfaz para mostrarle al usuario
     * si se van a reproducir audios.
     * */
    private void accionVolumen() {
        servicioAudio.setEstaMuteado();
        if(servicioAudio.getEstadoMuteado()) {
            boton_volumen.setImageResource(R.drawable.icono_volumen_desactivado);
        }
        else {
            boton_volumen.setImageResource(R.drawable.icono_volumen);
        }
    }

    /**
     * Vuelve al menú principal, simula el botón de ir atrás
     * */
    private void cerrarActividad() {
        onBackPressed();
    }

    /**
     * Dialog de confirmación al salir si se deja estripado el
     * botón del celular para ir atrás.
     * */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.titulo_confirmacion)
                .setMessage(R.string.contenido_confirmacion)
                .setPositiveButton(R.string.respuesta_positiva, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }})
                .setNegativeButton(R.string.respuesta_negativa, null).show();
    }

    /**
     * Metodo que elimina una carta de la mano y la oculta visualmente.
     **/
    private void removerCartaMano(int posicion) {
        ocultarCarta(posicion);
        manejadorDeReglas.agregarCartaMano(posicion, null);
    }

    /**
     * Metodo para mostrar un modal con un titulo y mensaje que se dan
     * por parametro. Puede ser reusable en todo el app.
     */
    public void mostrarModal(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(JuegoActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        JuegoActivity.this.finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.show();
    }


    /**
     * Metodo que eliminara visualmente la opcion de comer si el modo
     * de juego es ordenado. Esto debido a que el modo de juego no
     * requiere que el jugador coma cartas.
     * */
    private void establecerSeccionInferiorDerecha() {
        if (this.esModoOrdenado){
            boton_comer.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Oculta una carta al hacerla invisible.
     * */
    private void ocultarCarta(int numeroCarta) {
        manejadorDeAnimaciones.animarOcultarCarta(manoImagenes[numeroCarta]);
        manoImagenes[numeroCarta].setVisibility(View.INVISIBLE);
    }

    /**
     * Come una carta nueva y la agrega en la mano a la posicion
     * que viene por parametro.
     * */
    private void reemplazarCartaMano(int posicion, boolean animarCambio, boolean animarEmparejamiento) {
        Carta nuevaCarta = manejadorDeReglas.comer();
        if(nuevaCarta != null){
            agregarCartaMano(nuevaCarta, posicion);
            if(animarCambio){
                manejadorDeAnimaciones.animarCambiarCarta(manoImagenes[posicion], nuevaCarta.getImagen_id());
            }
            else if(animarEmparejamiento) {
                    manejadorDeAnimaciones.animarOcultarCarta(manoImagenes[posicion]);
                    manoImagenes[posicion].setImageResource(nuevaCarta.getImagen_id());
                manejadorDeAnimaciones.animarMostrarCarta(manoImagenes[posicion]);
            }
            else {
                manoImagenes[posicion].setImageResource(nuevaCarta.getImagen_id());
            }
        }
        else {
            removerCartaMano(posicion);
        }
    }

    /**
     * Lógica de comer carta. El mazo se actualiza eliminando la carta
     * que fue seleccionada.
     * */
    private void comerCarta() {
        int espacioMano = buscarEspacioMano();
        if ( espacioMano != ESPACIO_INVALIDO
                && espacioMano != PRIMER_ESPACIO_NULO) {
            reemplazarCartaMano(espacioMano,true, false);
        } else if (this.cartaSeleccionadaComer != SIN_SELECCIONAR) {
            if (!manejadorDeReglas.getMazo().isEmpty()){
                Carta reemplazo = manejadorDeReglas.getCartaMano(this.cartaSeleccionadaComer);
                // se realiza el intercambio.
                reemplazarCartaMano(this.cartaSeleccionadaComer,true, false);
                //Se agrega la carta intercambiada al final y revuelve mazo.
                manejadorDeReglas.agregarCartaMazo(reemplazo);
                manoImagenes[this.cartaSeleccionadaComer].setBackgroundResource(R.drawable.estilo_cartas);
                this.cartaSeleccionadaComer = SIN_SELECCIONAR;
            } else {
                mostrarMensaje("El mazo está vacío.");
            }
        }
        else {
            mostrarMensaje("Por favor seleccione una carta para intercambiar.");
        }
    }

    /**
     * Agrega una carta nueva (que es sacada del mazo) a la mano.
     * */
    private void agregarCartaMano(Carta nuevaCarta, int posicion) {
        if (nuevaCarta != null) {
            manejadorDeReglas.agregarCartaMano(posicion, nuevaCarta);

            // Actualiza la interfaz al desplegar imagen de la carta nueva
            //se comenta esta línea para poner las animaciones, esta línea se considera desactualizada
            //porque  ya no existe el escenario cuando se agrega una carta a un espacio vacío
            //manoImagenes[posicion].setImageResource(nuevaCarta.getImagen_id());
            //mostrarCarta(posicion);
        } else {
            removerCartaMano(posicion);
        }
    }

    /**
     * Busca un espacio vacio en la mano (posicion nula) para colocar
     * una carta ahi.
     * TODO considerar usar esto en nuevo modo de juego ordenado
     *  */
    private int buscarEspacioMano () {
        int espacioMano = ESPACIO_INVALIDO;
        boolean espacioEncontrado = false;
        int posicionCarta = 1;
        while(!espacioEncontrado && (posicionCarta < manoImagenes.length) ) {
            if(manoImagenes[posicionCarta].getVisibility() == View.INVISIBLE &&
                   manejadorDeReglas.getCartaMano(posicionCarta) == null) {
                espacioEncontrado = true;
                espacioMano = posicionCarta;
            }
            posicionCarta ++;
        }
        return espacioMano;
    }

    /**
     * Recupera el modo de juego que fue seleccionado por le usuario en el MenuActivity.
     * */
    private void getDetallesDelJuego() {
        Intent intent = getIntent();
        String modo_juego = intent.getStringExtra(PARCELABLE_NAME);
        this.esModoOrdenado = modo_juego.equals(GAME_MODE_1);
    }

    /**
     * Despliega un mensaje que se desvanece
     */
    public void mostrarMensaje(String mensaje)
    {
        Context contexto = getApplicationContext();
        Toast toast = Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT);
        toast.show();
    }

    @SuppressLint("SetTextI18n")
    public void actualizarContadorCartas(){
        if (!manejadorDeReglas.getModoOrdenado()) {
            // -1 por la primera posicion nula
            int cantidad_cartas = manejadorDeReglas.getMazo().size() - 1;
            boton_comer.setText("Comer\n" + cantidad_cartas + "/" + Integer.toString(cantidad_cartas_inicial + TAMANO_MANO));
        }
    }

    /**
     * Cambia el color de la barra de arriba del status del app.
     **/
    public void setColorStatusBar() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.primary_amarillo));
    }
}