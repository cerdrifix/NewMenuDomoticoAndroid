package it.abb;

import it.abb.entities.Funzione;
import it.abb.entities.Impostazione;
import it.abb.sql.FunzioniDataSource;
import it.abb.sql.ImpostazioniDataSource;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MenuDomoticoActivity extends ListActivity {
	private FunzioniDataSource datasource;
	private ImpostazioniDataSource impostazioniDS;
	
	private TextView customTitle;
	protected ImageButton backButton, rubricaButton, settingsButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Utils.changeLanguage(getApplicationContext());
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());
        
        datasource = new FunzioniDataSource(this);
        datasource.open();
        
        customTitle = (TextView)findViewById(R.id.title);
        
        Funzione funzione = null;
        List<Funzione> values = getData();
        
        
        Bundle b = this.getIntent().getExtras();
        try {
        	funzione = b.getParcelable(Constants.FUNZIONE);
//        	values = datasource.getFunzioniFiglie(funzione, false);
        } catch(Exception ex) {
//        	values = datasource.getFunzioniPrincipali(false);
        }
        
        backButton = (ImageButton) findViewById(R.id.buttonBack);
        backButton.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_back);
        backButton.setOnTouchListener(new MyOnTouchListener());
        backButton.setVisibility((funzione == null) ? View.INVISIBLE : View.VISIBLE);
        
        if(funzione != null) {
        	setTitle(funzione.getDescrizione());
            
        	backButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }
        rubricaButton = (ImageButton) findViewById(R.id.impiantoManualePulsanteRubrica);
        rubricaButton.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_contacts);
        rubricaButton.setOnTouchListener(new MyOnTouchListener());
        rubricaButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RubricaActivity.class);
				i.putExtra("isUsable", false);
        		startActivity(i);
			}
		});
        
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_settings);
        settingsButton.setOnTouchListener(new MyOnTouchListener());
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        		startActivity(i);
            }
        });
        
        FunzioneAdapter adapter = new FunzioneAdapter(
        		this,
        		R.layout.table_row,
        		values
		);
        
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
        		if(f.getCommand() == null || f.getCommand().equals("")) {
        			i = new Intent(getApplicationContext(), MenuDomoticoActivity.class);
        		}
        		else {
        			i = new Intent(getApplicationContext(), ResumeActivity.class);
        		}
        		i.putExtras(b);
        		startActivity(i);
        	  }
		});
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        		Funzione f = (Funzione)arg0.getItemAtPosition(position);
				Intent i = new Intent(getApplicationContext(), SettingsFunzioneDetailActivity.class);
				i.putExtra(Constants.FUNZIONE, f);
				startActivityForResult(i, Constants.FLAG_FUNZIONE_DETAILS);
				
        		return true;
        	}
        	
		});
    }
    
    private List<Funzione> getData() {
        List<Funzione> values;
        try {
        	Funzione funzione = this.getIntent().getExtras().getParcelable(Constants.FUNZIONE);
        	values = datasource.getFunzioniFiglie(funzione, false);
        } catch(Exception ex) {
        	boolean maint = isMaintenanceEnabled();
//        	boolean maint = false;
        	values = datasource.getFunzioniPrincipali(false, maint);
        }
        return values;
    }

    @Override
    public void setTitle(CharSequence title) {
        customTitle.setText(title);        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		Intent i;
	    switch (item.getItemId()) {
        case R.id.menu_item_rubrica:
        	i= new Intent(getApplicationContext(), RubricaActivity.class);
			i.putExtra("isUsable", false);
    		startActivity(i);
        	return true;
        case R.id.menu_item_settings:
        	i = new Intent(getApplicationContext(), SettingsActivity.class);
    		startActivity(i);
        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
    @Override
    protected void onRestart() {
        Utils.changeLanguage(getApplicationContext());
    	super.onRestart();
		datasource.open();
    	FunzioneAdapter adapter = new FunzioneAdapter(
        		this,
        		R.layout.table_row,
        		getData()
		);
    	setListAdapter(adapter);
    	
    	MyOnTouchListener.resetTouch(backButton);
    	MyOnTouchListener.resetTouch(rubricaButton);
    	MyOnTouchListener.resetTouch(settingsButton);
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
		return getImpostazioniDatasource().getImpostazioni();
	}
    
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}