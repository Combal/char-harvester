package ge.combal.charharvester;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vano on 10/4/16.
 */

public class SendImageTask extends AsyncTask<String, Void, Integer> {

	private static final String HOST = "combal.ge";
	private ByteArrayOutputStream baos;
	private String label;
	private Context context;

	public SendImageTask(Context context, String label, ByteArrayOutputStream baos) {
		this.context = context;
		this.label = label;
		this.baos = baos;
	}

	@Override
	protected Integer doInBackground(String... strings) {
		try {
			String urlString = "http://%s/chars/?label=%s";
			URL url = new URL(String.format(urlString, HOST, label));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("label", label);
			conn.setConnectTimeout(5000);
			OutputStream os = conn.getOutputStream();
			baos.writeTo(os);
			conn.connect();
			return conn.getResponseCode();
		} catch (Exception e){
			e.printStackTrace();
			return 1;
		}
	}

	@Override
	protected void onPostExecute(Integer status) {
		super.onPostExecute(status);
		String msg ;
		switch (status){
			case 200:
				msg = "";
				break;
			case 1:
				msg = "problem connecting the host: " + HOST;
				break;
			default:
				msg = "server responded with status: " + status;
				break;
		}
		if(!msg.isEmpty()) {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}
}
