package it.abb;

import it.abb.entities.Contatto;
import it.abb.entities.Funzione;
import it.abb.entities.Impostazione;
import it.abb.sql.ImpostazioniDataSource;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResumeActivity extends Activity {
	
	private List<Impostazione> impostazioni;
	private String pin, impianto;
	private TextView customTitle;
	
	private Button bRubrica, sendInPageButton;
	
	private static ProgressDialog progress;
	
	protected ImageButton buttonBack, buttonSend;
	
	private ImpostazioniDataSource datasource;
	private BroadcastReceiver sendBroadcastReceiver;
//	private BroadcastReceiver deliveryBroadcastReceiver;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.riepilogo_comando);
		
        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());

		
		final Bundle b = this.getIntent().getExtras();
		final Funzione f = b.getParcelable(Constants.FUNZIONE);

        customTitle = (TextView)findViewById(R.id.title);
		
		setTitle(f.getDescrizione());
		
		bRubrica = (Button)findViewById(R.id.impiantoManualePulsanteRubrica);
		bRubrica.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RubricaActivity.class);
				i.putExtra("isUsable", true);
				startActivity(i);
			}
		});
		
		buttonBack = (ImageButton) findViewById(R.id.buttonBack);buttonBack.setOnTouchListener(new MyOnTouchListener());
		buttonBack.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_back);
		buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        
        buttonSend = (ImageButton) findViewById(R.id.buttonSend);
		if(f.isMaintenance()) {
			buttonSend.setVisibility(View.INVISIBLE);
		}
        buttonSend.setOnTouchListener(new MyOnTouchListener());
        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(getPin() == null || getImpianto() == null) {
                    Toast.makeText(getBaseContext(), "Attenzione, si prega di compilare tutti i campi.", Toast.LENGTH_LONG).show();
                	return;
                }
                sendMessage(getPin(), getImpianto(), f);
            }
        });
        
        sendInPageButton = (Button) findViewById(R.id.invia_in_page);
        sendInPageButton.setText(f.isMaintenance() ? R.string.next : R.string.button_send);
        sendInPageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getPin() == null || getImpianto() == null) {
                    Toast.makeText(getBaseContext(), "Attenzione, si prega di compilare tutti i campi.", Toast.LENGTH_LONG).show();
                	return;
                }
				if(f.isMaintenance()) {
            		Intent i = new Intent(getApplicationContext(), RiepilogoManutenzioneActivity.class);
            		b.putString(Constants.PIN, getPin());
            		b.putString(Constants.IMPIANTO, getImpianto());
            		i.putExtras(b);
            		startActivityForResult(i, Constants.ACTIVITY_SEND_SMS);
            	}
            	else {
	                sendMessage(getPin(), getImpianto(), f);
				}
			}
		});
        
        bRubrica.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RubricaActivity.class);
        		startActivityForResult(i, Constants.FLAG_CONTACT_RETURN_SELECTED);
			}
		});

    	RelativeLayout ilm = (RelativeLayout) findViewById(R.id.impiantoManualeContainer);
    	LinearLayout ila = (LinearLayout) findViewById(R.id.ImpiantoAutomaticoContainer);
    	RelativeLayout plm = (RelativeLayout) findViewById(R.id.PinManualeContainer);
    	LinearLayout pla = (LinearLayout) findViewById(R.id.PinAutomaticoContainer);
		
        for (Impostazione i : getImpostazioni()) {
			Boolean auto = (i.getOpzione1() == 1) ? true : false;
			
			if(i.getCodice().equals("TEL")) {
	        	ila.setVisibility(auto?View.VISIBLE:View.GONE);
	        	ilm.setVisibility(auto?View.GONE:View.VISIBLE);
				if(auto) {
					impianto = i.getValore();
				}
			}
			else if(i.getCodice().equals("PIN")){
	        	pla.setVisibility(auto?View.VISIBLE:View.GONE);
	        	plm.setVisibility(auto?View.GONE:View.VISIBLE);
				if(auto) {
					pin = i.getValore();
				}
			}
		}

        
        //---when the SMS has been sent---
        sendBroadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
            	
            	dismissLoadingDialog();
            	switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Messaggio Inviato", 
                                Toast.LENGTH_LONG).show();
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
//        deliveryBroadcastReceiver = new BroadcastReceiver(){
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//            	switch (getResultCode())
//                {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS consegnato", 
//                                Toast.LENGTH_LONG).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getBaseContext(), "SMS non consegnato", 
//                                Toast.LENGTH_LONG).show();
//                        break;                        
//                }
//            }
//        };
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		dismissLoadingDialog();
		if(requestCode == Constants.ACTIVITY_SEND_SMS) {
			if(resultCode == Activity.RESULT_OK) {
				finish();
			}
		}
		else if(resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.FLAG_CONTACT_RETURN_SELECTED) {
				Bundle b = data.getExtras();
				Contatto c = (Contatto)b.getParcelable(Constants.CONTATTO);
				this.setImpianto(c.getTelefono());
			}
		}
	}

	public void showLoadingDialog() {
	
	    if (progress == null) {
	        progress = new ProgressDialog(this);
	        progress.setTitle("");
	        progress.setMessage(getString(R.string.invio_messaggio_in_corso));
	    }
	    progress.show();
	}
	
	public void dismissLoadingDialog() {
	
	    if (progress != null && progress.isShowing()) {
	        progress.dismiss();
	        progress = null;
	    }
	}
	
	@Override
	protected void onStop()
	{
		try {
		    unregisterReceiver(sendBroadcastReceiver);
//		    unregisterReceiver(deliveryBroadcastReceiver);
		    
		    datasource.close();
		    datasource = null;
		} catch(IllegalArgumentException e) {} catch(Exception ex) {}
	    super.onStop();
	}
	
	private final void sendMessage(String pin, String phoneNumber, Funzione funzione) {

		showLoadingDialog();
		
		String SENT = "SMS_SENT";
//        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
//        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
//            new Intent(DELIVERED), 0);
        
        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
//        registerReceiver(deliveryBroadcastReceiver, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
		
        String message = funzione.getCommand();
        
        if(funzione.isNeedPin())
        	message += String.format(" %s", pin);
        
//        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        sms.sendTextMessage(phoneNumber, null, message, sentPI, null);
	}
	
	private String getPin() {
		if(pin == null) {
			TextView tv = (TextView)findViewById(R.id.PinManualeText);
			if(tv.getText().length() > 0)
				pin = tv.getText().toString();
		}
		return pin;
	}
	
	private String getImpianto() {
		if(impianto == null) {
			TextView tv = (TextView)findViewById(R.id.ImpiantoManualeText);
			if(tv.getText().length() > 0)
				impianto = tv.getText().toString();
		}
		return impianto;
	}
	
	private void setImpianto(String phoneNumber) {
		((TextView)findViewById(R.id.ImpiantoManualeText)).setText(phoneNumber);
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
	protected void onResume() {
		dismissLoadingDialog();
		super.onResume();
	}

    @Override
    public void setTitle(CharSequence title) {
        customTitle.setText(title);        
    }
    
}
