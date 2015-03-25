package it.abb;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerItemAdapter extends ArrayAdapter<SpinnerItem> {
	
	private int layoutResourceId;

    private static class ViewHolder {
        TextView item_id;
        TextView item_description;
    }
    
	public SpinnerItemAdapter(Context context, int layoutResourceId, ArrayList<SpinnerItem> items) {
	       super(context, layoutResourceId, items);
	       this.layoutResourceId = layoutResourceId;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	       SpinnerItem item = getItem(position);  
	       // Check if an existing view is being reused, otherwise inflate the view
	       ViewHolder viewHolder; // view lookup cache stored in tag
	       if (convertView == null) {
	          viewHolder = new ViewHolder();
	          LayoutInflater inflater = LayoutInflater.from(getContext());
	          convertView = inflater.inflate(layoutResourceId, parent, false);
//	          viewHolder.item_id = (TextView) convertView.findViewById(R.id.item_id);
//	          viewHolder.item_description = (TextView) convertView.findViewById(R.id.item_description);
	          convertView.setTag(viewHolder);
	       } else {
	           viewHolder = (ViewHolder) convertView.getTag();
	       }
	       // Populate the data into the template view using the data object
	       viewHolder.item_id.setText(item.getId());
	       viewHolder.item_description.setText(item.getDescription());
	       // Return the completed view to render on screen
	       return convertView;
	   }
}
