package ac.ucr.paracontar.entidades;

import android.os.Parcel;
import android.os.Parcelable;

public class Carta implements Parcelable {

    String categoria;
    int cantidad;
    String nombre;
    int imagen_id;

    public Carta(String categoria, int cantidad, String nombre, int imagen_id) {
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.imagen_id = imagen_id;
    }


// ----------------- Metodos autogenerados ------------------

    protected Carta(Parcel in) {
        categoria = in.readString();
        cantidad = in.readInt();
        nombre = in.readString();
        imagen_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoria);
        dest.writeInt(cantidad);
        dest.writeString(nombre);
        dest.writeInt(imagen_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Carta> CREATOR = new Creator<Carta>() {
        @Override
        public Carta createFromParcel(Parcel in) {
            return new Carta(in);
        }

        @Override
        public Carta[] newArray(int size) {
            return new Carta[size];
        }
    };

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }



    public int getImagen_id() {
        return imagen_id;
    }

    public void setImagen_id(int imagen_id) {
        this.imagen_id = imagen_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
