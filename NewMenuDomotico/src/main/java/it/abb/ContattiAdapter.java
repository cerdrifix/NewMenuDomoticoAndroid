package it.abb;

import java.util.List;

import it.abb.entities.Contatto;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ContattiAdapter extends ArrayAdapter<Contatto> {
	Context context; 
    int layoutResourceId;    
    List<Contatto> data = null;
    LayoutInflater inflater;
    
    public ContattiAdapter(Context context, int layoutResourceId, List<Contatto> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        inflater = ((Activity)context).getLayoutInflater();
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
    	ContactViewHolder viewHolder;
        if (v == null) {
            v = inflater.inflate(layoutResourceId, null);
            viewHolder = new ContactViewHolder();
            viewHolder.nome = (TextView) v.findViewById(R.id.contact_name);
            viewHolder.telefono = (TextView) v.findViewById(R.id.contact_phone);
            v.setTag(viewHolder);
        }
        else {
        	viewHolder = (ContactViewHolder)v.getTag();
        }
        
        Contatto f = data.get(position);
        if (f != null) {
        	viewHolder.nome.setText(f.getNome());
        	viewHolder.telefono.setText(f.getTelefono());
        }
        return v;
    }
    

	static class ContactViewHolder {
		TextView nome;
		TextView telefono;
	}
}
