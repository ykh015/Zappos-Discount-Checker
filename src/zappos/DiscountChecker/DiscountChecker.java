package zappos.DiscountChecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.zappos.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class DiscountChecker extends Activity {
	private static final String PRODUCT_ID = "pid";
	private static final String STYLE_ID = "sid";
	ArrayList<String> pid = new ArrayList<String>();
	ArrayList<String> sid = new ArrayList<String>();
	private ProgressDialog spinner;

	// For Json Parsing
	class Super {
		String statusCode;
		List<Product> product;
	}

	class Product {
		String brandId;
		String brandName;
		String productId;
		String productName;
		String defaultProductUrl;
		String defaultImageUrl;
		List<Style> styles;
	}

	class Style {
		String originalPrice;
		String percentOff;
		String styleId;
		String imageUrl;
		String price;
		String productUrl;
		String color;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		getProductinDatabase(); // Get all the favorite products from the
								// database
		spinner = new ProgressDialog(this);
		if (isOnline())
			new distTask().execute("");
		else {
			Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG)
					.show();
			Toast.makeText(this, "Turn on Wifi or Data Plan", Toast.LENGTH_LONG)
					.show();

		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private class distTask extends AsyncTask<String, Void, String> {
		private boolean error401;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				// Go through the Product Ids stored in the database (favorite
				// Products). Send a Http request with a particular product id.
				// Parse the result using gson.
				// Get the Styles associated with a particular product. When The
				// Product Id and Style Id match ... Check for the discount. If
				// it is greater than or equal to 20%,
				// send a notification to the user which is shown in the
				// Notifications bar.
				for (int i = 0; i < pid.size(); i++) {
					String json;
					try {
						json = readUrl("http://api.zappos.com/Product/"
								+ pid.get(i)
								+ "?includes=[%22styles%22]&key=a73121520492f88dc3d33daf2103d7574f1a3166");
						Gson gson = new Gson(); // Parse JSON
						Super p = gson.fromJson(json, Super.class);
						if (p.statusCode.equals("200")) { // 200 implies success
							for (Product product : p.product) {
								for (Style i_style : product.styles) {
									if (i_style.styleId.equals(sid.get(i))) {
										int discount = Integer
												// Get the integer part from the
												// discount string which is like
												// 19%
												.parseInt(i_style.percentOff
														.substring(
																0,
																i_style.percentOff
																		.length() - 1));
										if (discount >= 20) { // Set the
																// Notification
																// when the
																// discount is
																// greater than
																// or equal to
																// 20%
											setNotification(
													product.productName,
													product.brandName,
													i_style.styleId,
													i_style.price,
													product.productId,
													i_style.originalPrice,
													i_style.percentOff,
													i_style.imageUrl, i);
										}
									}
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						error401 = true;
						e.printStackTrace();
					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				error401 = true;
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPreExecute() {

			spinner.setMessage("Checking discount on Favourite items ...");
			spinner.show();

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			spinner.dismiss();
			// After the Notifications have been sent, The new activity is
			// called
			Intent i = new Intent(DiscountChecker.this, Category.class);
			if (!error401) {
				DiscountChecker.this.startActivity(i);
				finish();
			} else {
				Toast.makeText(DiscountChecker.this,
						"Error in processing HTTP request. Check API Key",
						Toast.LENGTH_LONG).show();

			}
		}

	}

	private void setNotification(String pname, String bname, String sid,
			String price, String pid, String oprice, String discount,
			String imageUrl, int count) {

		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		// Setting parameters for the notification.
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.notification)
				.setContentTitle(discount + " on " + pname + " !!")
				.setContentText("  Brand: " + bname).setSound(alarmSound);

		// Setting extras for the intent
		Intent resultIntent = new Intent(this, DiscountProduct.class);
		resultIntent.putExtra("ProductName", pname);
		resultIntent.putExtra("BrandName", bname);
		resultIntent.putExtra("StyleId", sid);
		resultIntent.putExtra("ProductId", pid);
		resultIntent.putExtra("Price", price);
		resultIntent.putExtra("OriginalPrice", oprice);
		resultIntent.putExtra("Discount", discount);
		resultIntent.putExtra("Image", imageUrl);

		resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
				count, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setAutoCancel(true);
		int mNotificationId = count; // Different Id for each notification
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());

	}

	private void getProductinDatabase() {
		// TODO Auto-generated method stub

		DatabaseHandler db = new DatabaseHandler(this);
		Cursor c = db.getProduct();
		while (c.moveToNext()) {
			// Adding all the favorite products in the arraylist
			pid.add(c.getString(c.getColumnIndexOrThrow(PRODUCT_ID))); // ProductId
																		// array
			sid.add(c.getString(c.getColumnIndexOrThrow(STYLE_ID))); // StringId
																		// array
		}

	}

	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}

	}

}
