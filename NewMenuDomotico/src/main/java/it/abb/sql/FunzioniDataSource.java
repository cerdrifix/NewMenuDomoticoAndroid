package it.abb.sql;

import java.util.ArrayList;
import java.util.List;

import it.abb.Utils;
import it.abb.entities.Funzione;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FunzioniDataSource {

	public String TABLE = "functions";
	public String COLUMN_ID = "_id";
	public String COLUMN_PARENT_ID = "parentId";
	public String COLUMN_DESCRIZIONE = "description";
	public String COLUMN_COMMAND = "command";
	public String COLUMN_ORDER = "fnOrder";
	public String COLUMN_NEED_PIN = "needPin";
	public String COLUMN_IMAGE = "image";
	public String COLUMN_VISIBLE = "visible";
	public String COLUMN_TYPE = "type";
	public String COLUMN_LANGUAGE = "language";
	
	private Context context = null;
	private SQLiteDatabase database;
	private DomusSQLiteHelper dbHelper;
	private String[] allColumns = {
			this.COLUMN_ID,
			this.COLUMN_PARENT_ID,
			this.COLUMN_DESCRIZIONE,
			this.COLUMN_COMMAND,
			this.COLUMN_ORDER,
			this.COLUMN_NEED_PIN,
			this.COLUMN_IMAGE,
			this.COLUMN_VISIBLE,
			this.COLUMN_TYPE,
			this.COLUMN_LANGUAGE
	};
	
	public FunzioniDataSource(Context context) {
		this.context = context;
		dbHelper = new DomusSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}

//	public List<Funzione> getAllFunzioni() {
//		List<Funzione> funzioni = new ArrayList<Funzione>();
//		
//		Cursor cursor = database.query(this.TABLE, allColumns, null, null, null, null, this.COLUMN_ORDER);
//				
//		cursor.moveToFirst();
//		while(!cursor.isAfterLast()) {
//			Funzione f = new Funzione();
//			f.setId(cursor.getLong(0));
//			f.setDescrizione(cursor.getString(1));
//			f.setImage(cursor.getString(2));
//			f.setOrder(cursor.getInt(3));
//			funzioni.add(f);
//			cursor.moveToNext();
//		}
//		
//		cursor.close();
//		return funzioni;
//	}
	
	public List<Funzione> getFunzioniPrincipali(boolean all, boolean maintenance) {
		String condition;
		if(all) {
			condition = String.format("%s = 0", this.COLUMN_PARENT_ID);
		}
		else {
			condition = String.format("%s = 0 and %s = 1", this.COLUMN_PARENT_ID, this.COLUMN_VISIBLE);
		}

		if(!maintenance) {
			condition += String.format(" and %s != 9999", this.COLUMN_TYPE);
		}
		
		condition += String.format(" and %s = '%s'", this.COLUMN_LANGUAGE, Utils.getCurrentLanguage(context));
		
		Cursor cursor = database.query(this.TABLE, allColumns, condition, null, null, null, this.COLUMN_ORDER);
			
		return parseCursor(cursor);
	}
	
	public List<Funzione> getFunzioniFiglie(Funzione f, boolean all) {
				
		String condition;
		if(all) {
			condition = String.format("%s = %d", this.COLUMN_PARENT_ID, f.getId());
		}
		else {
			condition = String.format("%s = %d and %s = 1", this.COLUMN_PARENT_ID, f.getId(), this.COLUMN_VISIBLE);
		}
		
		condition += String.format(" and %s = '%s'", this.COLUMN_LANGUAGE, Utils.getCurrentLanguage(context));
		Cursor cursor = database.query(this.TABLE, allColumns, condition, null, null, null, this.COLUMN_ORDER);
			
		return parseCursor(cursor);
	}
	
	private List<Funzione> parseCursor(Cursor cursor) {
		List<Funzione> lista = new ArrayList<Funzione>();
		
		try {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Funzione c = new Funzione();
				c.setId(cursor.getInt(0));
				c.setParentId(cursor.getInt(1));
				c.setDescrizione(cursor.getString(2));
				c.setCommand(cursor.getString(3));
				c.setOrder(cursor.getInt(4));
				c.setNeedPin(cursor.getInt(5) == 1);
				c.setImage(cursor.getString(6));
				c.setVisibility(cursor.getInt(7) == 1);
				c.setType(cursor.getInt(8));
				c.setLanguage(cursor.getString(9));
				lista.add(c);
				cursor.moveToNext();
			}
		}
		catch(SQLException e) {
			Log.d("SQLEX", e.getLocalizedMessage());
		}
		finally {
			cursor.close();
			this.close();
		}
		return lista;
	}

	
	public void updateFunzione(long id, String descrizione, boolean isVisible) {
		ContentValues values = new ContentValues();
		values.put(this.COLUMN_DESCRIZIONE, descrizione);
		values.put(this.COLUMN_VISIBLE, isVisible ? 1 : 0);
		if(database == null) {
			this.open();
		}
		database.update(this.TABLE, values, String.format("%s = %s", this.COLUMN_ID, Long.toString(id)), null);
		this.close();
	}
}
