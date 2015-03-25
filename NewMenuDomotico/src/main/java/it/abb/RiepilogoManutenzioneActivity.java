package it.abb;

import it.abb.entities.Funzione;
import it.abb.sql.ImpostazioniDataSource;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RiepilogoManutenzioneActivity extends Activity {
	
	private String pin, impianto;
	private TextView customTitle;
	private static RadioGroup scelta, zona;
	private static LinearLayout layout_concentratore;
	private static Spinner sensoreSemplice, concentratore;
	private Button sendInPageButton;
	
	protected ImageButton buttonBack, buttonSend;
	
	private ImpostazioniDataSource datasource;
	private BroadcastReceiver sendBroadcastReceiver;
	private BroadcastReceiver deliveryBroadcastReceiver;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.riepilogo_maintenance);
		
        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());
        
        zona = (RadioGroup)findViewById(R.id.zona);
        layout_concentratore = (LinearLayout)findViewById(R.id.layout_concentratore);
        sensoreSemplice = (Spinner)findViewById(R.id.sensore);
        concentratore = (Spinner)findViewById(R.id.concentratore);
		
		final Bundle b = this.getIntent().getExtras();
		final Funzione f = b.getParcelable(Constants.FUNZIONE);

        customTitle = (TextView)findViewById(R.id.title);
        
        pin = b.getString(Constants.PIN);
        impianto = b.getString(Constants.IMPIANTO);
		
		setTitle(f.getDescrizione());
		
		buttonBack = (ImageButton) findViewById(R.id.buttonBack);buttonBack.setOnTouchListener(new MyOnTouchListener());
		buttonBack.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_back);
		buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        
        buttonSend = (ImageButton) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(pin, impianto, f);
            }
        });
        
        sendInPageButton = (Button) findViewById(R.id.invia_in_page);
        sendInPageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendMessage(pin, impianto, f);
			}
		});
		
		/* spinner concentratore */

        String[] ac = {
        		"",
        		getString(R.string.concentratore) + " 1",
        		getString(R.string.concentratore) + " 2",
        		getString(R.string.concentratore) + " 3",
        		getString(R.string.concentratore) + " 4",
        		getString(R.string.concentratore) + " 5",
        		getString(R.string.concentratore) + " 6",
        		getString(R.string.concentratore) + " 7",
        		getString(R.string.concentratore) + " 8"
        };
        
        ArrayAdapter<String> aConcentratori = new ArrayAdapter<String>(
        		getApplicationContext(), R.layout.spinner_item, ac);

        concentratore.setAdapter(aConcentratori);

		/* spinner sensore */

        String[] as = {
        		"",
	        	getString(R.string.sensore) + " 1",
	        	getString(R.string.sensore) + " 2",
	        	getString(R.string.sensore) + " 3",
	        	getString(R.string.sensore) + " 4",
	        	getString(R.string.sensore) + " 5",
	        	getString(R.string.sensore) + " 6",
	        	getString(R.string.sensore) + " 7",
	        	getString(R.string.sensore) + " 8",
	        	getString(R.string.tutti)
        };

        ArrayAdapter<String> aSensori = new ArrayAdapter<String>(
        		getApplicationContext(),R.layout.spinner_item, as);
        
        sensoreSemplice.setAdapter(aSensori);
        
        /* scelta */
        scelta = (RadioGroup)findViewById(R.id.scelta);
        scelta.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    		
    		@Override
    		public void onCheckedChanged(RadioGroup arg0, int arg1) {
    			layout_concentratore.setVisibility(hasConcentratore() ? View.VISIBLE : View.GONE); 
    		}
    	});
     

        //---when the SMS has been sent---
        sendBroadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Messaggio Inviato", 
                                Toast.LENGTH_LONG).show();

		        		if (RiepilogoManutenzioneActivity.this.getParent() == null) {
		        			RiepilogoManutenzioneActivity.this.setResult(RESULT_OK, getIntent());
		        		    finish();
		        		} else {
		        			RiepilogoManutenzioneActivity.this.getParent().setResult(Activity.RESULT_OK, null);
		        			RiepilogoManutenzioneActivity.this.getParent().finish();
		        		}
                        finish();
                        
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Errore generico", 
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Nessun servizio di rete", 
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", 
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", 
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        //---when the SMS has been delivered---
        deliveryBroadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS consegnato", 
                                Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS non consegnato", 
                                Toast.LENGTH_LONG).show();
                        break;                        
                }
            }
        };
	}
	
	
	@Override
	protected void onStop()
	{
		try {
		    unregisterReceiver(sendBroadcastReceiver);
		    unregisterReceiver(deliveryBroadcastReceiver);
		    
		    datasource.close();
		    datasource = null;
		} catch(IllegalArgumentException e) {} catch(Exception ex) {}
	    super.onStop();
	}
	
	private final void sendMessage(String pin, String phoneNumber, Funzione funzione) {

		String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        
        String concentratoreValue = parseSpinner(concentratore);
        String sensoreValue = parseSpinner(sensoreSemplice);
        
        if(( hasConcentratore() && "".equals(concentratoreValue)) || "".equals(sensoreValue)) {
        	Toast.makeText(getBaseContext(), R.string.all_choices_required, Toast.LENGTH_SHORT).show();
        	return;
        }
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
        
        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
        registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
		
        String message = funzione.getCommand();

        if(funzione.isNeedPin())
        	message += String.format(" %s", pin);
        
		int selectedId = zona.getCheckedRadioButtonId();
		RadioButton b = (RadioButton)findViewById(selectedId);
        
        String maintString = b.getText().toString();
        maintString += hasConcentratore() ? concentratoreValue : ".";
        maintString += sensoreValue;
        
        Log.d(this.getClass().getName(), "Maintenance string: " + maintString);
        
        message += String.format(" %s", maintString);
        
        sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
	}
	
	private boolean hasConcentratore() {
		int selectedId = scelta.getCheckedRadioButtonId();
		RadioButton b = (RadioButton)findViewById(selectedId);
		
		return b.getId() == R.id.radioConcentratore;
	}
	
	private String parseSpinner(Spinner spinner) {
		String item = (String)spinner.getSelectedItem();

		if(item == null) {
			return null;
		}
		
		if("".equals(item)) {
			return "";
		}
		
		if(getString(R.string.tutti).equalsIgnoreCase(item)) {
			return ".";
		}	
		
		String[] a = item.split(" "); 
		
		if(a.length != 2)
			return null;
		
		return a[1];
	}

    @Override
    public void setTitle(CharSequence title) {
        customTitle.setText(title);        
    }

}
