package it.abb;

import java.util.List;

import it.abb.entities.Comando;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ComandoAdapter extends ArrayAdapter<Comando> {
	Context context; 
    int layoutResourceId;    
    List<Comando> data = null;
    
    public ComandoAdapter(Context context, int layoutResourceId, List<Comando> data) {
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
        
        Comando f = data.get(position);
        if (f != null) {
            TextView tt = (TextView) v.findViewById(R.id.label);
            if (tt != null) {
                  tt.setText(f.getDescrizione());
            }
        }
        return v;
    }
}
