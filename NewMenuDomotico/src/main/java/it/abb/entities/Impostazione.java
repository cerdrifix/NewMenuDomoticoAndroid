package it.abb.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Impostazione implements Parcelable {
	
	public static String TABLE = "impostazioni";
	public static String COLUMN_ID = "_id";
	public static String COLUMN_CODICE = "codice";
	public static String COLUMN_DESCRIZIONE = "descrizione";
	public static String COLUMN_VALORE = "valore";
	public static String COLUMN_OPZIONE1 = "opzione1";
	public static String[] allColumns = {
		Impostazione.COLUMN_ID,
		Impostazione.COLUMN_CODICE,
		Impostazione.COLUMN_DESCRIZIONE,
		Impostazione.COLUMN_VALORE,
		Impostazione.COLUMN_OPZIONE1
	};
	
	private long id;
	private String codice;
	private String descrizione;
	private String valore;
	private int opzione1;
	
	public Impostazione() {
		
	}
	
	public Impostazione(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(codice);
		dest.writeString(descrizione);
		dest.writeString(valore);
		dest.writeInt(opzione1);
	}
	
	public void readFromParcel(Parcel in) {
		id = in.readLong();
		codice = in.readString();
		descrizione = in.readString();
		valore = in.readString();
		opzione1 = in.readInt();
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public int getOpzione1() {
		return opzione1;
	}

	public void setOpzione1(int opzione1) {
		this.opzione1 = opzione1;
	}
}
