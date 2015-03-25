package it.abb;

import android.app.Activity;
import android.os.Bundle;

public class MDActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Utils.changeLanguage(getApplicationContext());
	}
	
	@Override
	protected void onRestart() {
        Utils.changeLanguage(getApplicationContext());
		super.onRestart();
	}
}
