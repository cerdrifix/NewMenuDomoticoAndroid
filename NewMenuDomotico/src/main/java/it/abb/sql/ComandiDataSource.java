package it.abb.sql;

import java.util.ArrayList;
import java.util.List;

import it.abb.entities.Comando;
import it.abb.entities.Funzione;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ComandiDataSource {	
	
	private SQLiteDatabase database;
	private DomusSQLiteHelper dbHelper;
	private String[] allColumns = {
			Comando.COLUMN_ID,
			Comando.COLUMN_IDFUNZIONE,
			Comando.COLUMN_DESCRIZIONE,
			Comando.COLUMN_COMANDO,
			Comando.COLUMN_NEEDPIN
	};
	
	public ComandiDataSource(Context context) {
		dbHelper = new DomusSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public List<Comando> getComandiPerFunzione(Funzione f) {
		List<Comando> comandi = new ArrayList<Comando>();
		
		Cursor cursor = database.query(Comando.TABLE, allColumns, String.format("%s = %d", Comando.COLUMN_IDFUNZIONE, f.getId()), null, null, null, null);
				
		try {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Comando c = new Comando();
				c.setId(cursor.getInt(0));
				c.setFunzione(cursor.getInt(1));
				c.setDescrizione(cursor.getString(2));
				c.setComando(cursor.getString(3));
				c.setNeedPin(cursor.getInt(4) == 1);
				comandi.add(c);
				cursor.moveToNext();
			}
		}
		catch(SQLException e) {
			Log.d("SQLEX", e.getLocalizedMessage());
		}
		
		cursor.close();
		return comandi;
	}
}
