package it.abb.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract.Directory;
import android.util.Log;

public class DomusSQLiteHelper extends SQLiteOpenHelper {
	private static String DB_PATH; // = "data/data/it.abb/Menu Domotico/";
	private static final String ASSET_DB = "MenuDomotico.db";
	private static final int DB_VERSION = 210;
	private static final String DB_NAME = "MenuDomotico."+DB_VERSION+".db";
	
	private SQLiteDatabase domusDb;
	private final Context domusContext;
	
	public DomusSQLiteHelper(Context context) {
		
		super(context, DB_NAME, null, DB_VERSION);
		
		this.domusContext = context;

        DB_PATH = "/data/data/" + context.getApplicationContext().getPackageName() + "/databases/";
        
    	createDataBase();
	}
    private static final String TAG = "DbHelper";

    @Override
    public void onOpen(SQLiteDatabase db) {
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public void createDataBase() {
        if(!checkDataBase()){
        	this.getReadableDatabase();
        	
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            Log.d(TAG, "db exists");
        } catch(SQLiteException e){
             //database does't exist yet.
            Log.d(TAG, "db doesn't exist");
         }

         if(checkDB != null){
             checkDB.close();
         }

         return checkDB != null;
    }

    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = domusContext.getAssets().open(ASSET_DB);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        
        File dir = new File(DB_PATH);
        if(!dir.exists()) {
        	dir.mkdirs();
        }

        File file = new File(outFileName);
        if(!file.exists()) {
        	file.createNewFile();
        }
        
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close(); 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME + "." + DB_VERSION + ".db";
    	domusDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }

    @Override
	public synchronized void close() {
 
    	    if(domusDb != null)
    		    domusDb.close();
 
    	    super.close();
	}
}
