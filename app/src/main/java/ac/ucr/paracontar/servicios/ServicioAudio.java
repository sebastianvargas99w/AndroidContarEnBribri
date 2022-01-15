package ac.ucr.paracontar.servicios;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

public class ServicioAudio {

    Context contexto;
    boolean estaMuteado;

    public ServicioAudio(Context contexto) {
        this.contexto = contexto;
        estaMuteado = false;
    }

    /**
     * enlaces de audios
     * https://freesound.org/people/Gronkjaer/sounds/554053/
     * https://freesound.org/people/Bertrof/sounds/351565/
     */
    /**
     * Metodo recuperado del fraseodiccionario broran.
     * Sonara el audio que se pase como parametro, los audios
     * estan amacenados en la carpeta de res/raw. Si el juego esta
     * en silencio entonces no sonara nada.
     * @param recursoID El ID del audio a reproducir
     */
    public void reproducir(int recursoID)
    {
        if (!this.estaMuteado) {
            final MediaPlayer player = MediaPlayer.create(this.contexto, recursoID);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    player.release();
                }
            });

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }

    /**
     * Cambia si el juego esta en silencio o no. Cambia el estado de la variable
     * al estado contrario.
     * */
    public void setEstaMuteado() {
        estaMuteado = !estaMuteado;
    }

    /**
     * Devuelve la variable que indica si se deben reproducir audios
     * @return estaMuteado
     */
    public boolean getEstadoMuteado() {
        return estaMuteado;
    }

}
