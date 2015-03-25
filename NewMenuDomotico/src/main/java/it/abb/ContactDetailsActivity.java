package it.abb;

import it.abb.R;
import it.abb.R.id;
import it.abb.R.layout;
import it.abb.entities.Contatto;
import it.abb.sql.RubricaDataSource;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
//import android.graphics.PorterDuff;

public class ContactDetailsActivity extends Activity {
	
	Contatto contatto;
	EditText tName;
	EditText tPhone;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.contact_details);

        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());
        
        Bundle b = this.getIntent().getExtras();
        boolean iu = true;
        if(b != null) {
        	contatto = b.getParcelable(Constants.CONTATTO);
        	iu = b.getBoolean("isUsable");
        }
        
    	tName = ((EditText)findViewById(R.id.name));
    	tPhone = ((EditText)findViewById(R.id.phone));

        Button bUse = ((Button)findViewById(R.id.button_use));
//        bUse.getBackground().setColorFilter(0xFF2487AD, PorterDuff.Mode.MULTIPLY);
        bUse.setOnClickListener(onUse());
        Button bSave = (Button)findViewById(R.id.button_save);
//        bSave.getBackground().setColorFilter(0xFF34AD24, PorterDuff.Mode.MULTIPLY);
        bSave.setOnClickListener(onSave());
        Button bDelete = (Button)findViewById(R.id.button_delete);
//        bDelete.getBackground().setColorFilter(0xFFAD2525, PorterDuff.Mode.MULTIPLY);
//        bDelete.setVisibility(contatto == null ? View.GONE : View.VISIBLE);
        bDelete.setOnClickListener(onDelete());
        
        Button bCopy = (Button)findViewById(R.id.button_copy);
//        bCopy.getBackground().setColorFilter(0xFF245BAD, PorterDuff.Mode.MULTIPLY);
//        bCopy.setVisibility(contatto == null ? View.GONE : View.VISIBLE);
        bCopy.setOnClickListener(onCopy());
        
        if(contatto != null) {
        	
        	if(contatto.isLocal()) {
            	bDelete.setVisibility(View.VISIBLE);
            	bSave.setVisibility(View.VISIBLE);
            	bCopy.setVisibility(View.GONE);
        	}
        	else {
            	bDelete.setVisibility(View.GONE);
            	bSave.setVisibility(View.GONE);
            	bCopy.setVisibility(View.VISIBLE);
        	}
        	bUse.setVisibility(iu ? View.VISIBLE : View.GONE);
        	
        	
        	tName.setText(contatto.getNome());
        	tPhone.setText(contatto.getTelefono());
        	
        }
        else {
        	bDelete.setVisibility(View.GONE);
        	bSave.setVisibility(View.VISIBLE);
        	bUse.setVisibility(View.GONE);
        	bCopy.setVisibility(View.GONE);
        }
        
	}
	
	private View.OnClickListener onUse() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent resultData = new Intent();
				resultData.putExtra("action", Constants.FLAG_CONTACT_ACTION_USE);
				resultData.putExtra("contact", contatto);
				setResult(Activity.RESULT_OK, resultData);
				finish();
			}
		};
	}
	
	private View.OnClickListener onSave() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RubricaDataSource ds = new RubricaDataSource(ContactDetailsActivity.this.getApplicationContext());
				if(contatto != null) {
					ds.updateContatto(contatto.getId(), tName.getText().toString(), tPhone.getText().toString());
				}
				else {
					ds.insertContatto(tName.getText().toString(), tPhone.getText().toString());
				}
				Intent resultData = new Intent();
				resultData.putExtra("action", Constants.FLAG_CONTACT_ACTION_SAVE);
				setResult(Activity.RESULT_OK, resultData);
				finish();
			}
		};
	}
	
	private View.OnClickListener onDelete() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RubricaDataSource ds = new RubricaDataSource(ContactDetailsActivity.this.getApplicationContext());
				ds.deleteContatto(contatto.getId());
				Intent resultData = new Intent();
				resultData.putExtra("action", Constants.FLAG_CONTACT_ACTION_DELETE);
				setResult(Activity.RESULT_OK, resultData);
				finish();
			}
		};
	}
	
	private View.OnClickListener onCopy() {
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RubricaDataSource ds = new RubricaDataSource(ContactDetailsActivity.this.getApplicationContext());
				ds.insertContatto(tName.getText().toString(), tPhone.getText().toString());
			
				Intent resultData = new Intent();
				resultData.putExtra("action", Constants.FLAG_CONTACT_ACTION_SAVE);
				setResult(Activity.RESULT_OK, resultData);
				finish();
			}
		};
	}
	
}
