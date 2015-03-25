package it.abb;

import it.abb.R;
import it.abb.R.id;
import it.abb.R.layout;
import it.abb.R.menu;
import it.abb.entities.Contatto;
import it.abb.sql.RubricaDataSource;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class LocalAddressBookActivity //extends FragmentActivity implements ContactsDialogListener 
extends ListActivity {
	
	ListView list = null;
	ImageButton searchButton = null;
	EditText searchText = null;
	TextView emptyList = null;
	ContattiAdapter adapter = null;
	boolean iu = false;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.local_address_book, null);
        this.setContentView(viewToLoad);
//        setContentView(R.layout.local_address_book);
        
        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());

        
        Bundle b = getParent().getIntent().getExtras();
    	iu = true;
    	if(b != null){
    		iu = b.getBoolean("isUsable");
    	}
        
        list = getListView();
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Contatto c = (Contatto)arg0.getItemAtPosition(position);
				if(!iu) {
					Bundle b = new Bundle();
					b.putParcelable(Constants.CONTATTO, c);
					Intent i = new Intent(getApplicationContext(), ContactDetailsActivity.class);
					i.putExtras(b);
					startActivityForResult(i, Constants.FLAG_CONTACT_DETAILS);
				}
				else {
					Intent data = new Intent();
					data.putExtra(Constants.CONTATTO, c);
	    		    getParent().setResult(Activity.RESULT_OK, data);
	    		    finish();
				}
        	  }
		});
        
//        emptyList = (TextView)this.findViewById(android.R.id.empty);
        
        searchText = (EditText)this.findViewById(R.id.searchText);
        searchButton = (ImageButton)this.findViewById(R.id.search);
        
        searchText.addTextChangedListener(new TextWatcher() {

        	  
        	@Override
        	public void afterTextChanged(Editable s) { 
        		refreshList();
        	}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
        
        refreshList();
    }
	
	private List<Contatto> loadAppContacts() {
		
		searchText = (EditText)this.findViewById(R.id.searchText);
		String nameString = searchText.getText().toString().trim();
		
		RubricaDataSource ds = new RubricaDataSource(this);
		
		List<Contatto> list = ds.getContatti(nameString);
				
		return list;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.FLAG_CONTACT_DETAILS) {
			if (resultCode == RESULT_OK) {
				switch(data.getIntExtra("action", 0)) {
		        	case Constants.FLAG_CONTACT_ACTION_SAVE:
		        		refreshList();
		        		break;
		        	case Constants.FLAG_CONTACT_ACTION_DELETE:
		        		refreshList();
		        		break;
		        	case Constants.FLAG_CONTACT_ACTION_USE:
		        		if (getParent() == null) {
		        		    setResult(Activity.RESULT_OK, data);
		        		    finish();
		        		} else {
		        		    getParent().setResult(Activity.RESULT_OK, data);
			        		getParent().finish();
		        		}
		        		break;
				}
			}
		}
	}
	

	/*
	public void showContactsDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = ContactsDialog.newInstance(this, ContactsDialogType.ContactsDialogTypeNew);
        
        dialog.show(getSupportFragmentManager(), "ContactsDialog");
    }
    */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_rubrica_locale, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.local_contact_new:
	        	Intent i = new Intent(getApplicationContext(), ContactDetailsActivity.class);
	        	i.putExtra("isUsable", iu);
				startActivityForResult(i, Constants.FLAG_CONTACT_DETAILS);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void refreshList() {
		setListAdapter(new ContattiAdapter(this, R.layout.contacts_row, loadAppContacts()));
	}
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	refreshList();
    }
}