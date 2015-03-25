package it.abb.dialogs;

import it.abb.R;
import it.abb.enums.ContactsDialogType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ContactsDialog extends DialogFragment {
	
	public interface ContactsDialogListener {
		public void onContactSaved(DialogFragment dialog, String name, String phone);
	}
	
	static ContactsDialogListener listener;
	public ContactsDialogType type;
	
	public static ContactsDialog newInstance(Activity activity, ContactsDialogType type) {
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events with it
            listener = (ContactsDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ContactsDialogListener");
        }
        ContactsDialog frag = new ContactsDialog();
        frag.type = type;
        return frag;
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    final View v = inflater.inflate(R.layout.dialog_local_contact, null);
	    

	    TextView title = (TextView)v.findViewById(R.id.local_contact_form_title);
	    title.setText("Test");
	    
	    builder.setView(v)
	    // Add action buttons
	           .setPositiveButton(R.string.salva, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {

	           	    	String name = ((EditText)v.findViewById(R.id.local_contact_form_name)).getText().toString();
	           	    	String phone = ((EditText)v.findViewById(R.id.local_contact_form_phone)).getText().toString();
	           	    	listener.onContactSaved(ContactsDialog.this, name, phone);
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   ContactsDialog.this.getDialog().cancel();
	               }
	           });
	    
	    
	    return builder.create();
    }
}
