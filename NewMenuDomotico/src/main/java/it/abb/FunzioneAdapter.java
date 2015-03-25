package it.abb;

import java.util.List;

import it.abb.entities.Funzione;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FunzioneAdapter extends ArrayAdapter<Funzione> {
	Context context; 
    int layoutResourceId;    
    List<Funzione> data = null;
    
    public FunzioneAdapter(Context context, int layoutResourceId, List<Funzione> data) {
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
            TextView tt = (TextView) v.findViewById(R.id.label);
            if (tt != null) {
                  tt.setText(f.getDescrizione());
                  
                  
                  String uri = "drawable/ic_" + f.getImage();

                  // int imageResource = R.drawable.icon;
                  int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

                  tt.setCompoundDrawablesWithIntrinsicBounds(imageResource, 0, 0, 0);
            }
        }
        return v;
    }
}
