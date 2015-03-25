package it.abb.async;

import it.abb.entities.Contatto;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class PhoneAddressBookLoadingTask extends AsyncTask<String, Void, List<Contatto>>{
	
	List<Contatto> lista = new ArrayList<Contatto>();
	ProgressDialog progressDialog;
	
	public PhoneAddressBookLoadingTask(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Contatto> doInBackground(
            String... params) {
        return doProcess(params[0]);
    }

    @Override
    protected void onPostExecute(List<Contatto> result) {
        progressDialog.dismiss();
        super.onPostExecute(result);
    }

    private List<Contatto> doProcess(String choice) {
    	List<Contatto> arrayList = new ArrayList<Contatto>();
        // do processing here
        return arrayList;
    }
}
