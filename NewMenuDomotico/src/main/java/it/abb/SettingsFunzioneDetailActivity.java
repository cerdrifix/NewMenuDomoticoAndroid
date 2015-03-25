package it.abb;

import it.abb.R;
import it.abb.R.drawable;
import it.abb.R.id;
import it.abb.R.layout;
import it.abb.entities.Funzione;
import it.abb.sql.FunzioniDataSource;
import android.app.Activity;
import android.graphics.Color;
//import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsFunzioneDetailActivity extends Activity {
	
	Funzione funzione;
	EditText tName;
	ToggleButton bVisibility;
	ImageButton saveButton, backButton;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());

        setContentView(R.layout.settings_funzione_dettaglio);	
        
    	tName = ((EditText)findViewById(R.id.settings_funzione_nome));
    	bVisibility = ((ToggleButton)findViewById(R.id.settings_funzione_visibility_toggle_button));
        
        Bundle b = this.getIntent().getExtras();
        funzione = b.getParcelable(Constants.FUNZIONE);
        
        tName.setText(funzione.getDescrizione());
        bVisibility.setChecked(funzione.isVisible());

        saveButton = (ImageButton)findViewById(R.id.saveButton);
        backButton = (ImageButton)findViewById(R.id.backButton);

       	Button salvaInPageButton = (Button) findViewById(R.id.salva_in_page);
//       	salvaInPageButton.getBackground().setColorFilter(0xFF34AD24, PorterDuff.Mode.MULTIPLY);
       	salvaInPageButton.setTextColor(Color.WHITE);
        
       	backButton.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_back);
       	backButton.setOnTouchListener(new MyOnTouchListener());
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
		
        saveButton.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_save);
        saveButton.setOnTouchListener(new MyOnTouchListener());
		saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String d = tName.getText().toString(); 
            	if(d.length() == 0) {
            		Toast.makeText(v.getContext(), "Errore. E' obbligatorio specificare una descrizione", Toast.LENGTH_SHORT).show();
            		tName.setText(funzione.getDescrizione());
            		return;
            	}
            	
            	FunzioniDataSource ds = new FunzioniDataSource(v.getContext());
            	ds.updateFunzione(funzione.getId(), d, bVisibility.isChecked());

            	setResult(Activity.RESULT_OK);
				Toast.makeText(getApplicationContext(), "Salvataggio completato", Toast.LENGTH_SHORT).show();
            	finish();
            }
		});
		
		salvaInPageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String d = tName.getText().toString(); 
            	if(d.length() == 0) {
            		Toast.makeText(v.getContext(), "Errore. E' obbligatorio specificare una descrizione", Toast.LENGTH_SHORT).show();
            		tName.setText(funzione.getDescrizione());
            		return;
            	}
            	
            	FunzioniDataSource ds = new FunzioniDataSource(v.getContext());
            	ds.updateFunzione(funzione.getId(), d, bVisibility.isChecked());

            	setResult(Activity.RESULT_OK);
				Toast.makeText(getApplicationContext(), "Salvataggio completato", Toast.LENGTH_SHORT).show();
            	finish();
            }
		});
		
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		MyOnTouchListener.resetTouch(backButton);
		MyOnTouchListener.resetTouch(saveButton);
	}
}
