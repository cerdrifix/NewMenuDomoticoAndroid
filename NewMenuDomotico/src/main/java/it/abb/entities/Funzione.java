package it.abb.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Funzione implements Parcelable {
	
	private long id;
	private long parentId;
	private String descrizione;
	private int order;
	private String command;
	private boolean needPin;
	private String image;
	private boolean visible;
	private int type;
	private String language;
	
	public Funzione() { 
		
	}
	
	public Funzione(Parcel in) {
		readFromParcel(in);
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(parentId);
		dest.writeString(descrizione);
		dest.writeInt(order);
		dest.writeString(command);
		dest.writeInt(needPin ? 1 : 0);
		dest.writeString(image);
		dest.writeInt(visible ? 1 : 0);
		dest.writeInt(type);
		dest.writeString(language);
	}
	
	private void readFromParcel(Parcel in) {
		id = in.readLong();
		parentId = in.readLong();
		descrizione = in.readString();
		order = in.readInt();
		command = in.readString();
		needPin = (in.readInt() == 1);
		image = in.readString();
		visible = (in.readInt() == 1);
		type = in.readInt();
		language = in.readString();
	}

    public static final Parcelable.Creator<Funzione> CREATOR =
    	new Parcelable.Creator<Funzione>() {
            public Funzione createFromParcel(Parcel in) {
                return new Funzione(in);
            }
 
            public Funzione[] newArray(int size) {
                return new Funzione[size];
            }
        };

	@Override
	public String toString() {
		return descrizione;
	}
	
	public boolean isMaintenance() {
		return this.type == 9990;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public boolean isNeedPin() {
		return needPin;
	}
	public void setNeedPin(boolean needPin) {
		this.needPin = needPin;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisibility(boolean isVisible) {
		this.visible = isVisible;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}
