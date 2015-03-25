package it.abb.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Contatto implements Parcelable {
	
	public static String TABLE = "rubrica";
	public static String COLUMN_ID = "_id";
	public static String COLUMN_NOME = "nome";
	public static String COLUMN_TELEFONO = "telefono";
	public static String[] allColumns = {
		Contatto.COLUMN_ID,
		Contatto.COLUMN_NOME,
		Contatto.COLUMN_TELEFONO
	};
	public static String DATABASE_CREATE = 
		    "CREATE TABLE IF NOT EXISTS " + TABLE + " ( "
		    + COLUMN_ID + " integer PRIMARY KEY Autoincrement, "
		    + COLUMN_NOME + " varchar NOT NULL,"
		    + COLUMN_TELEFONO + " varchar NOT NULL );";
	
	private long id;
	private String nome;
	private String telefono;
	private boolean local;
	
	public Contatto() {
		
	}

	public Contatto(String nome, String telefono) {
		this.nome = nome;
		this.telefono = telefono;
		this.local = true;
	}
	
	public Contatto(String nome, String telefono, boolean isLocal) {
		this.nome = nome;
		this.telefono = telefono;
		this.local = isLocal;
	}
	
	public Contatto(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(nome);
		dest.writeString(telefono);
		dest.writeLong(local ? 1 : 0);
	}
	
	public void readFromParcel(Parcel in) {
		id = in.readLong();
		nome = in.readString();
		telefono = in.readString();
		local = in.readInt() == 0 ? false: true;
	}

    public static final Parcelable.Creator<Contatto> CREATOR =
    	new Parcelable.Creator<Contatto>() {
            public Contatto createFromParcel(Parcel in) {
                return new Contatto(in);
            }
 
            public Contatto[] newArray(int size) {
                return new Contatto[size];
            }
        };

	@Override
	public String toString() {
		return nome;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public boolean isLocal() {
		return local;
	}
	
	public void setLocal(boolean isLocal) {
		this.local = isLocal;
	}
}
