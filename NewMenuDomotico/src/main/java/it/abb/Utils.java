package it.abb;

import it.abb.entities.Impostazione;
import it.abb.sql.ImpostazioniDataSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {
	
	public static HashMap<String, String> getLanguagesDataSource(Context context) {
		HashMap<String, String> languagesDataSource = new HashMap<String, String>();
		languagesDataSource.put("SYS", context.getString(R.string.lingua_di_sistema));
		languagesDataSource.put("it", context.getString(R.string.italiano));
		languagesDataSource.put("en", context.getString(R.string.inglese));
		languagesDataSource.put("de", context.getString(R.string.deutsch));
		
		return languagesDataSource;
	}
	
	public static void changeLanguage(Context context, String language) {
		Resources res = context.getResources();
	    // Change locale settings in the app.
	    DisplayMetrics dm = res.getDisplayMetrics();
	    android.content.res.Configuration conf = res.getConfiguration();
	    conf.locale = new Locale(language.toLowerCase());
	    res.updateConfiguration(conf, dm);
	}
	
	public static void changeLanguage(Context context) {
		try {
			String s = getLanguageSetting(context).getValore();
			if(s != null && !s.equalsIgnoreCase("SYS")) {
				changeLanguage(context, s);
			}
		}
		catch(Exception ex) {
			Log.e("Utils->changeLanguage()", ex.getLocalizedMessage());
		}
	}
	
	public static Impostazione getLanguageSetting(Context context) {
		return getDatasource(context).getLanguageSetting();
	}
	
	private static ImpostazioniDataSource getDatasource(Context context) {
		ImpostazioniDataSource ds = new ImpostazioniDataSource(context);
		ds.open();
		return ds;
	}
	
	@SuppressWarnings("unchecked")
	public static String getCurrentLanguage(Context context) {
		String s = getLanguageSetting(context).getValore();
		if(s == null || s.equalsIgnoreCase("SYS"))
			return Locale.getDefault().getLanguage();
		
		if(!getLanguagesDataSource(context).keySet().contains(s))
			return "en";
		
		return s;
	}
}
