package it.abb;

import it.abb.entities.Contatto;
import it.abb.entities.Impostazione;
import it.abb.sql.ImpostazioniDataSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
//import android.graphics.PorterDuff;

public class SettingsActivity extends Activity {
	
	private String TAG = "Settings";
	
	private List<Impostazione> impostazioni;
	ImpostazioniDataSource datasource;
	private boolean pinAuto, impiantoAuto;
	private String languageSetting = "SYS";
	private static HashMap<String, String> languagesDataSource;
	
	protected ToggleButton pinButton, impiantoButton, maintenanceButton;
	protected LinearLayout pll, ill;
	protected EditText itv, ptv;
	protected Button bRubrica, salvaInPageButton;
	protected ImageButton editFunctions, backButton, saveButton;
	
	protected Spinner languages;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		setTitle("Impostazioni");
		
		languagesDataSource = new HashMap<String, String>();
		languagesDataSource.put("SYS", getString(R.string.lingua_di_sistema));
		languagesDataSource.put("it", getString(R.string.italiano));
		languagesDataSource.put("en", getString(R.string.inglese));
		languagesDataSource.put("de", getString(R.string.deutsch));
		
        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());

		pll = (LinearLayout)findViewById(R.id.settingsPinAutomaticoContainer);
        ill = (LinearLayout)findViewById(R.id.settingsImpiantoAutomaticoContainer);
        ptv = (EditText)findViewById(R.id.settingsPinAutomatico);
        itv = (EditText)findViewById(R.id.settingsImpiantoAutomatico);
        pinButton = (ToggleButton) findViewById(R.id.settingsPinToggleButton);
        impiantoButton = (ToggleButton) findViewById(R.id.settingsImpiantoToggleButton);
       	maintenanceButton = (ToggleButton) findViewById(R.id.settingsMaintenanceToggleButton);
       	languages = (Spinner) findViewById(R.id.languages);
       	
       	// Rubrica Button
       	bRubrica = (Button)findViewById(R.id.impiantoAutomaticoPulsanteRubrica);
        bRubrica.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RubricaActivity.class);
				i.putExtra("isUsable", true);
				startActivityForResult(i, Constants.FLAG_CONTACT_DETAILS);
			}
		});
       	
        
        // Impostazioni Funzioni
       	editFunctions = (ImageButton) findViewById(R.id.editFunctionsButton);
       	editFunctions.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_edit_functions);
       	editFunctions.setOnTouchListener(new MyOnTouchListener());
       	editFunctions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsFunzioneActivity.class);
                startActivity(i);
            }
        });
		
		
       	// Back Button
       	backButton = (ImageButton) findViewById(R.id.buttonBack);
       	backButton.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_back);
       	backButton.setOnTouchListener(new MyOnTouchListener());
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
		
        // Save Button
		saveButton = (ImageButton) findViewById(R.id.saveButton);
		saveButton.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_save);
		saveButton.setOnTouchListener(new MyOnTouchListener());
       	saveButton.setOnClickListener(new SaveClickListener());
		
//		salvaInPageButton = (Button) findViewById(R.id.salva_in_page);
//		salvaInPageButton.setOnTouchListener(new MyOnTouchListener());
//       	salvaInPageButton.setTextColor(Color.WHITE);
//       	salvaInPageButton.setOnClickListener(new SaveClickListener());
	
        pinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	pinAuto = pinButton.isChecked();
                pll.setVisibility(pinAuto ? View.VISIBLE : View.GONE);
                ptv.setText("");
            }
        });
        
        impiantoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	impiantoAuto = impiantoButton.isChecked();
                ill.setVisibility(impiantoAuto ? View.VISIBLE : View.GONE);
            }
        });
        
        /**
         * 	lingue
         */
        
        String[] array = languagesDataSource.values().toArray(new String[0]);
        
        Log.v("Languages array", "Languages array: "+ Arrays.toString(array));
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        		getApplicationContext(),R.layout.spinner_item, array);
        
        languages.setAdapter(adapter);
		
        for (Impostazione i : getImpostazioni()) {
			Boolean auto = (i.getOpzione1() == 1) ? true : false;
			
			if(i.getCodice().equals("PIN")) {
				pinAuto = auto;
				pinButton.setChecked(auto);
                pll.setVisibility(pinAuto ? View.VISIBLE : View.GONE);
                ptv.setText(auto ? i.getValore() : "");
			}
			else if(i.getCodice().equals("TEL")){
				impiantoAuto = auto;
				impiantoButton.setChecked(auto);
                ill.setVisibility(impiantoAuto ? View.VISIBLE : View.GONE);
                itv.setText(auto ? i.getValore() : "");
			}
			else if(i.getCodice().equals("MAN")){
				maintenanceButton.setChecked(auto);
			}
			else if(i.getCodice().equals("LAN")) {
				if(!"".equals(i.getValore()) && i.getValore() != null){
					languageSetting = i.getValore();
				}
				else {
					languageSetting = "SYS";
				}
			}
		}

		String item = languagesDataSource.get(languageSetting);
		int position = adapter.getPosition(item);
		languages.setSelection(position);
	}
	
	class SaveClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			 salva();
		 }
	}
	
	@SuppressWarnings("unchecked")
	protected void salva() {
		try {
        	String valore;
        	int opzione1;
        	boolean autoPin, autoTel, maintEnabled;
        	
        	autoPin = pinButton.isChecked();
        	autoTel = impiantoButton.isChecked();
        	maintEnabled = maintenanceButton.isChecked();
        	
        	if(autoPin && ptv.getText().toString().equals("")) {
        		Toast.makeText(getApplicationContext(), "Inserire un valore nel campo PIN", Toast.LENGTH_LONG).show();
        		return;
        	}

        	if(autoTel && itv.getText().toString().equals("")) {
        		Toast.makeText(getApplicationContext(), "Inserire un valore nel campo Telefono", Toast.LENGTH_LONG).show();
        		return;
        	}
        	
        	valore = autoPin ? ptv.getText().toString() : "";
        	opzione1 = autoPin ? 1 : 0;
        	Log.i(TAG, "Saving PIN setting with value: " + valore);
        	getDatasource().updateSetting("PIN", valore, opzione1);
        	
        	valore = autoTel ? itv.getText().toString() : "";
        	opzione1 = autoTel ? 1 : 0;
        	Log.i(TAG, "Saving phone setting with value: " + valore);
        	getDatasource().updateSetting("TEL", valore, opzione1);
        	
        	valore = "";
        	opzione1 = maintEnabled ? 1 : 0;
        	Log.i(TAG, "Saving maintenance setting with value: " + opzione1);
        	getDatasource().updateSetting("MAN", valore, opzione1);
        	
        	ArrayAdapter<String> adapter = (ArrayAdapter<String>) languages.getAdapter();
//    		String item = languagesDataSource.get(languages.getSelectedItem());
    		int position = adapter.getPosition(languages.getSelectedItem().toString());
        	valore = languagesDataSource.keySet().toArray(new String[0])[position];
//        	valore = 
        	opzione1 = 0;
        	Log.i(TAG, "Saving language setting with value: " + valore);
        	getDatasource().updateSetting("LAN", valore, opzione1);
        	
        	Toast.makeText(getApplicationContext(), "Salvataggio completato", Toast.LENGTH_SHORT).show();
        	finish();
	 	}
	 	catch(SQLException ex) {
	        	Toast.makeText(getApplicationContext(), "Si � verificato un errore nel salvataggio dei dati", Toast.LENGTH_SHORT).show();
	 	}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.FLAG_CONTACT_DETAILS) {
				Bundle b = data.getExtras();
				Contatto c = (Contatto)b.getParcelable(Constants.CONTATTO);
				((EditText)findViewById(R.id.settingsImpiantoAutomatico)).setText(c.getTelefono());
			}
		}
	}
	
	private ImpostazioniDataSource getDatasource() {
		if(datasource == null) {
			datasource = new ImpostazioniDataSource(this);
	        datasource.open();
		}
		return datasource;
	}
	
	private List<Impostazione> getImpostazioni() {
		if(impostazioni == null) {
			impostazioni = getDatasource().getImpostazioni();
		}
		return impostazioni;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_settings, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings_menu_edit_functions:
	        	Intent i = new Intent(getApplicationContext(), SettingsFunzioneActivity.class);
				startActivity(i);
	            return true;
	        case R.id.menu_item_save:
	        	salva();
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		MyOnTouchListener.resetTouch(backButton);
		MyOnTouchListener.resetTouch(saveButton);
		MyOnTouchListener.resetTouch(editFunctions);
	}
	
	@Override
	public void onStop() {
		try {
			datasource.close();
			datasource = null;
		}
		catch(Exception e) {}
		super.onStop();
	}
}
