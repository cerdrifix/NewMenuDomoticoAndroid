package it.abb;

import it.abb.R;
import it.abb.R.id;
import it.abb.R.layout;
import it.abb.entities.Contatto;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class PhoneAddressBookActivity extends ListActivity {
	
	ListView list = null;
	ImageButton searchButton = null;
	EditText searchText = null;
	ContattiAdapter adapter = null;
	boolean iu = false;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.phone_address_book);
        
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
        
        
        searchText = (EditText)this.findViewById(R.id.searchText);
        searchButton = (ImageButton)this.findViewById(R.id.search);
        
        searchText.addTextChangedListener(new TextWatcher() {

        	  
        	@Override
        	public void afterTextChanged(Editable s) { 
        		adapter = new ContattiAdapter(searchText.getContext(), R.layout.contacts_row, loadPhoneContacts());
                setListAdapter(adapter);
        	}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
        
        refreshList();
    }
	
	private List<Contatto> loadPhoneContacts() {
		
		Cursor people;
		String  nameString;
		
		searchText = (EditText)this.findViewById(R.id.searchText);
		nameString = searchText.getText().toString().trim();
//        if((nameString = searchText.getText().toString().trim()).length() > 0) {
//        	nameString += "%";
//        	people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//        }
//        else {
        	nameString += "%";
        	people = getContentResolver().query(
    				ContactsContract.Contacts.CONTENT_URI, 
    				null, 
    				ContactsContract.Contacts.DISPLAY_NAME+" LIKE ?", 
    				new String[] { nameString }, 
    				null
			);
//        }
		
		List<Contatto> list = new ArrayList<Contatto>();
		
		while(people.moveToNext()) {
		   String contactId = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID)); 
		   String nome = people.getString(people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		   String numero = "";
		   
		   int hasPhone = people.getInt(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
		   if (hasPhone > 0) { 
		      // You know it has a number so now query it like this
		      Cursor phones = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null); 
		      while (phones.moveToNext()) { 
		         numero = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));                 
		      } 
		      phones.close(); 
		   }
		   
		   list.add(new Contatto(nome, numero, false));
		}

		people.close();
		

		TextView t = (TextView)findViewById(android.R.id.empty);
		if(list.isEmpty() && nameString.length() == 0) {
//			t.setText("");
		}
		else {
			t.setText("Nessun contatto trovato.");
		}
		
		return list;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.FLAG_CONTACT_DETAILS) {
			if (resultCode == RESULT_OK) {
				switch(data.getIntExtra("action", 0)) {
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

	private void refreshList() {
		setListAdapter(new ContattiAdapter(this, R.layout.contacts_row, loadPhoneContacts()));
	}
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	refreshList();
    }
}
