package it.abb.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Comando implements Parcelable {

	public static String TABLE = "comandi";
	public static String COLUMN_ID = "_id";
	public static String COLUMN_IDFUNZIONE = "idFunzione";
	public static String COLUMN_DESCRIZIONE = "descrizione";
	public static String COLUMN_COMANDO = "comando";
	public static String COLUMN_NEEDPIN = "needPin";
	
	
	private int id;
	private int idFunzione;
	private String descrizione;
	private String comando;
	private boolean needPin;
	
	public Comando() {
		
	}
	
	public Comando(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(idFunzione);
		dest.writeString(descrizione);
		dest.writeString(comando);
		dest.writeInt(needPin ? 1 : 0);
	}
	
	private void readFromParcel(Parcel in) {
		id = in.readInt();
		idFunzione = in.readInt();
		descrizione = in.readString();
		comando = in.readString();
		needPin = (in.readInt() == 1);
	}

    public static final Parcelable.Creator<Comando> CREATOR =
    	new Parcelable.Creator<Comando>() {
            public Comando createFromParcel(Parcel in) {
                return new Comando(in);
            }
 
            public Comando[] newArray(int size) {
                return new Comando[size];
            }
        };

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdFunzione() {
		return idFunzione;
	}

	public void setFunzione(int idFunzione) {
		this.idFunzione = idFunzione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public boolean getNeedPin() {
		return needPin;
	}

	public void setNeedPin(boolean needPin) {
		this.needPin = needPin;
	}

	@Override
	public String toString() {
		return this.descrizione;
	}
}
