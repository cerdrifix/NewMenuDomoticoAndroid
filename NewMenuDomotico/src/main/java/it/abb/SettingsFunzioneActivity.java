package it.abb;

import it.abb.entities.Funzione;
import it.abb.entities.Impostazione;
import it.abb.sql.FunzioniDataSource;
import it.abb.sql.ImpostazioniDataSource;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsFunzioneActivity extends ListActivity {
	private FunzioniDataSource datasource;
	private ImpostazioniDataSource impostazioniDS;
	
	private List<Impostazione> impostazioni;
	
	protected ImageButton backButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_funzione);
        
        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());

        datasource = new FunzioniDataSource(this);
        datasource.open();
               
        Funzione funzione = null;
        List<Funzione> values = getData();
        Bundle b = this.getIntent().getExtras();
        
        try {
        	funzione = b.getParcelable(Constants.FUNZIONE);
//        	values = datasource.getFunzioniFiglie(funzione, true);
        } catch(Exception ex) {
//        	values = datasource.getFunzioniPrincipali(true);
        }
        
        backButton = (ImageButton) findViewById(R.id.buttonBack);
        backButton.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_back);
        backButton.setOnTouchListener(new MyOnTouchListener());
//        backButton.setVisibility((funzione == null) ? View.INVISIBLE : View.VISIBLE);
    	backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        
        if(funzione != null) {
        	setTitle(funzione.getDescrizione());   
        }
        else {
        	setTitle("");
        }
        /*
        final ImageButton rubricaButton = (ImageButton) findViewById(R.id.impiantoManualePulsanteRubrica);
        rubricaButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RubricaActivity.class);
				i.putExtra("isUsable", false);
        		startActivity(i);
			}
		});
        
        final ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        		startActivity(i);
            }
        });
        */
        
        SettingsFunzioneAdapter adapter = new SettingsFunzioneAdapter( this, R.layout.editable_function_row, values );
        
        setListAdapter(adapter);
        
        ListView lv = getListView();
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        		Funzione f = (Funzione)arg0.getItemAtPosition(position);
        		Bundle b = new Bundle();
        		b.putParcelable(Constants.FUNZIONE, f);
        		Intent i = null;
        		if(f.getCommand() == null) {
        			i = new Intent(getApplicationContext(), SettingsFunzioneActivity.class);
        		}
        		else {
        			return;
        		}

        		i.putExtras(b);
        		startActivity(i);
        	  }
		});
    }
    
    @Override
    public void setTitle(CharSequence title) {

        ((TextView)findViewById(R.id.title)).setText(title);        
    }
    
    private List<Funzione> getData() {
        List<Funzione> values;
        try {
        	Funzione funzione = this.getIntent().getExtras().getParcelable(Constants.FUNZIONE);
        	values = datasource.getFunzioniFiglie(funzione, true);
        } catch(Exception ex) {
        	values = datasource.getFunzioniPrincipali(true, isMaintenanceEnabled());
        }
        return values;
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
		datasource.open();
		SettingsFunzioneAdapter adapter = new SettingsFunzioneAdapter(
        		this,
        		R.layout.editable_function_row,
        		getData()
		);
    	setListAdapter(adapter);
    	
    	MyOnTouchListener.resetTouch(backButton);
    	
    }
    
    
    /**
     * Recupero impostazioni
     */
   
	private boolean isMaintenanceEnabled() {
		for (Impostazione i : getImpostazioni() ) {
			if(i.getCodice().equals("MAN")) {
				return i.getOpzione1() == 1;
			}
		}
		return false;
	}
	
	private ImpostazioniDataSource getImpostazioniDatasource() {
		if(impostazioniDS == null) {
			impostazioniDS = new ImpostazioniDataSource(this);
			impostazioniDS.open();
		}
		return impostazioniDS;
	}
	
	private List<Impostazione> getImpostazioni() {
		if(impostazioni == null) {
			impostazioni = getImpostazioniDatasource().getImpostazioni();
		}
		return impostazioni;
	}
    
	@Override
	protected void onResume() {
//		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	
}