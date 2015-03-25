package it.abb;

import java.util.List;

import it.abb.entities.Funzione;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsFunzioneAdapter extends ArrayAdapter<Funzione> {
	Context context; 
    int layoutResourceId;    
    List<Funzione> data = null;
    
    public SettingsFunzioneAdapter(Context context, int layoutResourceId, List<Funzione> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = convertView;
        if (v == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            v = inflater.inflate(layoutResourceId, null);
        }
        Funzione f = data.get(position);
        
        if (f != null) {

        	TextView tt = (TextView) v.findViewById(R.id.function_name);
			tt.setText(f.getDescrizione());
			          
			String uri = "drawable/ic_" + f.getImage();
			int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
			tt.setCompoundDrawablesWithIntrinsicBounds(imageResource, 0, 0, 0);
        	tt.setTag(R.id.TAG_ONLINE_ID, f);
			tt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent i = null;
	        		Funzione f = (Funzione)v.getTag(R.id.TAG_ONLINE_ID);
	        		if(f.getCommand() == null || f.getCommand().equals("")) {
	        			i = new Intent(context, SettingsFunzioneActivity.class);
	        		}
	        		else {
	        			return;
	        		}
	        		Bundle b = new Bundle();
	        		b.putParcelable(Constants.FUNZIONE, f);
	        		
	        		i.putExtras(b);
	        		((Activity)context).startActivity(i);

				}
			});
            
            ImageButton b = (ImageButton)v.findViewById(R.id.function_edit_button);
        	b.setTag(R.id.TAG_ONLINE_ID, f);
        	b.setTag(R.id.TAG_IMAGE_ID, R.drawable.ic_edit_functions);
            b.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
	        		Funzione f = (Funzione)v.getTag(R.id.TAG_ONLINE_ID);
					Intent i = new Intent(context, SettingsFunzioneDetailActivity.class);
					i.putExtra(Constants.FUNZIONE, f);
					((Activity) context).startActivityForResult(i, Constants.FLAG_FUNZIONE_DETAILS);
				}
			});
        }
        return v;
    }
}
