package it.abb.sql;

import java.util.ArrayList;
import java.util.List;

import it.abb.entities.Impostazione;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ImpostazioniDataSource {
	
	private SQLiteDatabase database;
	private DomusSQLiteHelper dbHelper;
	
	public ImpostazioniDataSource(Context context) {
		dbHelper = new DomusSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}

	public List<Impostazione> getImpostazioni() {
		List<Impostazione> lista = new ArrayList<Impostazione>();
		
		Cursor cursor = database.query(Impostazione.TABLE, Impostazione.allColumns, null, null, null, null, null);
				
		try {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Impostazione x = new Impostazione();
				x.setId(cursor.getInt(0));
				x.setCodice(cursor.getString(1));
				x.setDescrizione(cursor.getString(2));
				x.setValore(cursor.getString(3));
				x.setOpzione1(cursor.getInt(4));
				lista.add(x);
				cursor.moveToNext();
			}
		}
		catch(SQLException e) {
			Log.d("SQLEX", e.getLocalizedMessage());
		}
		finally {
			cursor.close();
		}
		
		return lista;
	}
	
	public Impostazione getLanguageSetting() {
		return getSetting("LAN");
	}
	
	public Impostazione getPinSetting() {
		return getSetting("PIN");
	}
	
	public Impostazione getPhoneSetting() {
		return getSetting("TEL");
	}
	
	public Impostazione getMaintenanceSetting() {
		return getSetting("MAN");
	}
	
	public Impostazione getSetting(String codice) {
//		Cursor cursor = database.query(
//				Impostazione.TABLE, 
//				Impostazione.allColumns, 
//				Impostazione.COLUMN_CODICE + " = '?s'", new String[] { codice }, null, null, null);

		Cursor cursor = database.query(Impostazione.TABLE, Impostazione.allColumns, Impostazione.COLUMN_CODICE + " = '" + codice + "'", null, null, null, null);
		
		if(cursor.getCount() <= 0)
			return null;

		cursor.moveToFirst();
		Impostazione x = new Impostazione();
		x.setId(cursor.getInt(0));
		x.setCodice(cursor.getString(1));
		x.setDescrizione(cursor.getString(2));
		x.setValore(cursor.getString(3));
		x.setOpzione1(cursor.getInt(4));
		
		return x;
	}
	
	public void updateSetting(String codice, String valore, int opzione1 ) {
		ContentValues values = new ContentValues();
		values.put(Impostazione.COLUMN_VALORE, valore);
		values.put(Impostazione.COLUMN_OPZIONE1, opzione1);
		
		database.update(Impostazione.TABLE, values, String.format("%s = '%s'", Impostazione.COLUMN_CODICE, codice), null);
	}
}
