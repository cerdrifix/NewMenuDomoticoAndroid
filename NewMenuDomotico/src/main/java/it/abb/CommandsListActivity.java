package it.abb;

import it.abb.R;
import it.abb.R.layout;
import it.abb.entities.Comando;
import it.abb.entities.Funzione;
import it.abb.sql.ComandiDataSource;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CommandsListActivity extends ListActivity {

	private ComandiDataSource datasource;
	private Funzione funzione;
	
	public void setFunzione(Funzione funzione) {
		this.funzione = funzione;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        datasource = new ComandiDataSource(this);
        datasource.open();
        
        Bundle b = this.getIntent().getExtras();
        funzione = b.getParcelable(Constants.FUNZIONE);
        
        List<Comando> values = datasource.getComandiPerFunzione(funzione);
        
        /*
        ArrayAdapter<Comando> adapter = new ArrayAdapter<Comando>(
        		this, 
        		R.layout.table_row, 
        		values
		);
        */
        ComandoAdapter adapter = new ComandoAdapter(this, R.layout.table_row, values);
        setListAdapter(adapter);
        
        ListView lv = getListView();
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        		Comando c = (Comando)arg0.getItemAtPosition(position);
        		Intent i = new Intent(getApplicationContext(), CommandsListActivity.class);
        		Bundle b = new Bundle();
        		b.putParcelable(Constants.COMANDO, c);
        		i.putExtras(b);
        		
//        		Intent i = new Intent(this, CommandsListActivity.class);
        		startActivity(i);
        	  }
		});
    }

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
}
