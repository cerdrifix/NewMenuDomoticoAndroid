package it.abb;

import it.abb.R;
import it.abb.R.drawable;
import it.abb.R.layout;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TabHost;
import android.widget.TextView;

public class RubricaActivityOld extends TabActivity {
	
	TabHost tabHost = null;
	Intent phoneIntent = null;
	Intent localIntent = null;
			
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.wrapper_rubrica);

	    tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    TextView txtTab;
	    
	    Intent ii = getIntent();
	    Bundle b = ii.getExtras();
	    boolean iu = true;
	    if(b != null) {
	    	iu = b.getBoolean("isUsable");
	    }
	    
	    
	    txtTab = new TextView(this);
	    txtTab.setText("Rubrica Locale");
	    txtTab.setPadding(8, 9, 8, 9);
	    txtTab.setTextColor(Color.GRAY);
	    txtTab.setTextSize(14);
//	    txtTab.setShadowLayer(2, 0, 0, Color.parseColor("#aaaaaa"));
	    txtTab.setGravity(Gravity.BOTTOM | Gravity.CENTER);
	    txtTab.setBackgroundColor(Color.TRANSPARENT);
//	    txtTab.setBackgroundDrawable(R.drawable.custom_shape_1);
	    txtTab.setHighlightColor(Color.BLUE);
	    
	    localIntent = new Intent().setClass(this, LocalAddressBookActivity.class).putExtra("isUsable", iu);
	    spec = tabHost.newTabSpec("localAddressBook").setIndicator(txtTab)
	                  .setContent(localIntent);
	    tabHost.addTab(spec);
	    

	    txtTab = new TextView(this);
	    txtTab.setText("Rubrica del Telefono");
	    txtTab.setPadding(8, 9, 8, 9);
	    txtTab.setTextColor(Color.GRAY);
	    txtTab.setTextSize(14);
//	    txtTab.setShadowLayer(2, 0, 0, Color.parseColor("#aaaaaa"));
	    txtTab.setGravity(Gravity.BOTTOM | Gravity.CENTER);
	    txtTab.setBackgroundColor(Color.TRANSPARENT);
	    txtTab.setHighlightColor(Color.BLUE);
	    
	    phoneIntent = new Intent().setClass(this, PhoneAddressBookActivity.class);
	    spec = tabHost.newTabSpec("phoneAddressBook").setIndicator(txtTab)
	                  .setContent(phoneIntent);
	    tabHost.addTab(spec);
	    changeTabColor();
	    
	    tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				changeTabColor();
			}
		});
	    
	    tabHost.setCurrentTab(0);
	    
	    
	}
	
	private void changeTabColor() {
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
	    {
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.TRANSPARENT); //unselected
			((TextView)tabHost.getTabWidget().getChildAt(i)).setTextColor(Color.GRAY);
			((TextView)tabHost.getTabWidget().getChildAt(i)).setShadowLayer(0, 0, 0, Color.parseColor("#ffffff"));
	    }
//		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.GRAY);
	    tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_shape_1));
	    ((TextView)tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())).setTextColor(Color.WHITE);
		((TextView)tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())).setShadowLayer(2, 0, 0, Color.parseColor("#333333"));
	}
	
	public void switchTab(int tab, String text){
        Intent i = (tab == 0) ? localIntent : phoneIntent;
        
        i.putExtra("testo", text);
        tabHost.setCurrentTab(tab);
	}
	
}
