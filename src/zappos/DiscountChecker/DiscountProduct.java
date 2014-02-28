package zappos.DiscountChecker;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.zappos.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DiscountProduct extends Activity implements OnClickListener{
	String s_id,p_id;
	Button b_fav;
	ImageView image;
	Bitmap mIcon_val ;
	private static final String PRODUCT_ID = "pid";
	private static final String STYLE_ID = "sid";

	
	//For new Intents from the notifications
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		String pname = getIntent().getExtras().getString("ProductName");
		String bname = getIntent().getExtras().getString("BrandName");
		s_id = getIntent().getExtras().getString("StyleId");
		p_id = getIntent().getExtras().getString("ProductId");
		String price = getIntent().getExtras().getString("Price");
		String originalPrice = getIntent().getExtras().getString("OriginalPrice");
		String discount = getIntent().getExtras().getString("Discount");
		String imageUrl = getIntent().getExtras().getString("Image");

		TextView tv_pname = (TextView)findViewById(R.id.pname);
		TextView tv_bname = (TextView)findViewById(R.id.bname);
		TextView tv_price = (TextView)findViewById(R.id.price);
		TextView tv_oprice = (TextView)findViewById(R.id.oprice);
		TextView tv_dicount = (TextView)findViewById(R.id.discount);
		image = (ImageView) findViewById(R.id.imageView1);
		tv_pname.setText(pname);
		tv_bname.setText(bname);
		tv_price.setText(price);
		tv_oprice.setText(originalPrice);
		tv_dicount.setText(discount);
		b_fav = (Button)findViewById(R.id.button1);
		b_fav.setOnClickListener(this);
		boolean check=checkProductinDatabase();
		if(check)
			b_fav.setText("Remove as Favorite");
		else
			b_fav.setText("Add as Favorite");

		new distTask().execute(imageUrl);


	}
	
	//The first time the activity is called
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);
		
		//Getting values from the intent
		String pname = getIntent().getExtras().getString("ProductName");
		String bname = getIntent().getExtras().getString("BrandName");
		s_id = getIntent().getExtras().getString("StyleId");
		p_id = getIntent().getExtras().getString("ProductId");
		String price = getIntent().getExtras().getString("Price");
		String originalPrice = getIntent().getExtras().getString("OriginalPrice");
		String discount = getIntent().getExtras().getString("Discount");
		String imageUrl = getIntent().getExtras().getString("Image");
		
		//Initializing the textviews and imageview
		image = (ImageView) findViewById(R.id.imageView1);
		TextView tv_pname = (TextView)findViewById(R.id.pname);
		TextView tv_bname = (TextView)findViewById(R.id.bname);
		TextView tv_price = (TextView)findViewById(R.id.price);
		TextView tv_oprice = (TextView)findViewById(R.id.oprice);
		TextView tv_dicount = (TextView)findViewById(R.id.discount);
		
		//Setting text to the textviews
		tv_pname.setText(pname);
		tv_bname.setText(bname);
		tv_price.setText(price);
		tv_oprice.setText(originalPrice);
		tv_dicount.setText(discount);

		b_fav = (Button)findViewById(R.id.button1);
		b_fav.setOnClickListener(this);
		
		boolean check=checkProductinDatabase(); //Check if the product is a favorite item (i.e. It is in the database or not)
		
		if(check)
			b_fav.setText("Remove as Favorite");
		else
			b_fav.setText("Add as Favorite");

		new distTask().execute(imageUrl);

	}

	private boolean checkProductinDatabase() {
		
		DatabaseHandler db = new DatabaseHandler(this);
		ArrayList<String> pid = new ArrayList<String>();
		ArrayList<String> sid = new ArrayList<String>();
		Cursor c=db.getProduct();
		while(c.moveToNext()){
            pid.add(c.getString(c.getColumnIndexOrThrow(PRODUCT_ID)));
            sid.add(c.getString(c.getColumnIndexOrThrow(STYLE_ID)));

    }

		for(int i=0;i<pid.size();i++){
			if(pid.get(i).equals(p_id) && sid.get(i).equals(s_id)){
				return true;
			}
		}


		return false;
	}

	//Async Task
	private class distTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String imageUrl=params[0];
			URL newurl;
			try {
				newurl = new URL(imageUrl);
				 mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream()); 


			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			return null;

		}

		@Override
		protected void onPreExecute() {


		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			image.setImageBitmap(mIcon_val);

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		DatabaseHandler db = new DatabaseHandler(this);

		if(b_fav.getText().equals("Add as Favorite")){
			b_fav.setText("Remove as Favorite");
			db.addProduct(p_id,s_id);
		}
		else if(b_fav.getText().equals("Remove as Favorite")){

			b_fav.setText("Add as Favorite");
			db.deleteProduct(p_id, s_id);


		}

	}

}
