package it.abb;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class RubricaActivity extends TabActivity {
	
	TabHost tabHost = null;
	Intent phoneIntent = null;
	Intent localIntent = null;
			
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.wrapper_rubrica);
	    setContentView(R.layout.rubrica);
	    
        FontHelper.setAppFont( (ViewGroup) findViewById(android.R.id.content).getRootView());

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
	    txtTab.setText(getString(R.string.local_addressbook));
	    txtTab.setPadding(8, 12, 8, 12);
	    txtTab.setTextColor(Color.GRAY);
	    txtTab.setTextSize(14);
//	    txtTab.setShadowLayer(2, 0, 0, Color.parseColor("#aaaaaa"));
	    txtTab.setGravity(Gravity.BOTTOM | Gravity.CENTER);
	    txtTab.setBackgroundColor(Color.TRANSPARENT);
//	    txtTab.setBackgroundDrawable(R.drawable.custom_shape_1);
//	    txtTab.setHighlightColor(Color.BLUE);
	    txtTab.setOnTouchListener(new MyOnTouchListener());
	    
	    localIntent = new Intent().setClass(this, LocalAddressBookActivity.class).putExtra("isUsable", iu);
	    spec = tabHost.newTabSpec("localAddressBook").setIndicator(txtTab)
	                  .setContent(localIntent);
	    tabHost.addTab(spec);
	    

	    txtTab = new TextView(this);
	    txtTab.setText(getString(R.string.phone_addressbook));
	    txtTab.setPadding(8, 12, 8, 12);
	    txtTab.setTextColor(Color.GRAY);
	    txtTab.setTextSize(14);
//	    txtTab.setShadowLayer(2, 0, 0, Color.parseColor("#aaaaaa"));
	    txtTab.setGravity(Gravity.BOTTOM | Gravity.CENTER);
	    txtTab.setBackgroundColor(Color.TRANSPARENT);
//	    txtTab.setHighlightColor(Color.BLUE);
	    txtTab.setOnTouchListener(new MyOnTouchListener());
	    
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
//	    tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_shape_1));
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_shape_3));
	    ((TextView)tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())).setTextColor(Color.WHITE);
		((TextView)tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())).setShadowLayer(2, 0, 0, Color.parseColor("#333333"));
	}
	
	public void switchTab(int tab, String text){
        Intent i = (tab == 0) ? localIntent : phoneIntent;
        
        i.putExtra("testo", text);
        tabHost.setCurrentTab(tab);
	}
	
}
