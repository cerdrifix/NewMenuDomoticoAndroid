package it.abb.sql;

import java.util.ArrayList;
import java.util.List;

import it.abb.entities.Contatto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RubricaDataSource {
	
	private SQLiteDatabase database;
	private DomusSQLiteHelper dbHelper;
	
	public RubricaDataSource(Context context) {
		dbHelper = new DomusSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		database.execSQL(Contatto.DATABASE_CREATE);
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public List<Contatto> getContatti() {
		return getContatti("");
	}

	public List<Contatto> getContatti(String nome) {
		
		if(database == null)
			this.open();
		
		List<Contatto> lista = new ArrayList<Contatto>();
		Cursor cursor;
		
		if(nome.length() == 0) {
			cursor = database.query(Contatto.TABLE, Contatto.allColumns, null, null, null, null, null);
		}
		else {
			cursor = database.query(Contatto.TABLE, Contatto.allColumns, Contatto.COLUMN_NOME+" LIKE ?", new String[] { nome + "%" }, null, null, null);
		}
				
		try {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				Contatto x = new Contatto();
				x.setId(cursor.getInt(0));
				x.setNome(cursor.getString(1));
				x.setTelefono(cursor.getString(2));
				x.setLocal(true);
				lista.add(x);
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
	
	public void updateContatto(long id, String nome, String telefono ) {
		ContentValues values = new ContentValues();
		values.put(Contatto.COLUMN_NOME, nome);
		values.put(Contatto.COLUMN_TELEFONO, telefono);
		
		if(database == null)
			this.open();
		try {
			database.update(Contatto.TABLE, values, String.format("%s = '%s'", Contatto.COLUMN_ID, id), null);
		}
		catch(SQLException e) {
			Log.d("SQLEX", e.getLocalizedMessage());
		}
		finally {
			database.close();
		}
	}
	
	public void insertContatto(String nome, String telefono ) {
		ContentValues values = new ContentValues();
		values.put(Contatto.COLUMN_NOME, nome);
		values.put(Contatto.COLUMN_TELEFONO, telefono);
		
		if(database == null)
			this.open();
		try {
			database.insert(Contatto.TABLE, null, values);
		}
		catch(SQLException e) {
			Log.d("SQLEX", e.getLocalizedMessage());
		}
		finally {
			database.close();
		}
	}
	
	public void deleteContatto(long id) {
		if(database == null)
			this.open();
		try {
			database.delete(Contatto.TABLE, String.format("%s=?", Contatto.COLUMN_ID), new String[] { Long.toString(id) });
		}
		catch(SQLException e) {
			Log.d("SQLEX", e.getLocalizedMessage());
		}
		finally {
			database.close();
		}
	}
}
