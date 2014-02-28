package zappos.DiscountChecker;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.zappos.R;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Product_List extends Activity implements OnItemClickListener {
	
	//classes for JSON parsing
	static class Super {
		String statusCode;
		String term;
		String originalTerm;
		int currentResultCount;
		int totalResultCount;
		List<Item> results;
	}

	static class Item {
		String styleId;
		String price;
		String originalPrice;
		String productUrl;
		String colorId;
		String productName;
		String brandName;
		String thumbnailImageUrl;
		String percentOff;
		String productId;
	}

	private ProgressDialog spinner;
	String name;
	ArrayList<String> pname = new ArrayList<String>();
	ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
	ArrayList<String> price = new ArrayList<String>();
	ArrayList<String> originalPrice = new ArrayList<String>();
	ArrayList<String> brandName = new ArrayList<String>();
	ArrayList<String> styleId = new ArrayList<String>();
	ArrayList<String> productId = new ArrayList<String>();
	ArrayList<String> percentOff = new ArrayList<String>();


	ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);
		spinner = new ProgressDialog(this);
		list = (ListView)findViewById(R.id.listView1);
		list.setOnItemClickListener(this);
		name = getIntent().getExtras().getString("FileName").toLowerCase(); //get the category

		new distTask().execute(""); //Create Async Task

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

	private class distTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String json = readUrl("http://api.zappos.com/Search?term="
						+ name
						+ "&key=a73121520492f88dc3d33daf2103d7574f1a3166");

				Gson gson = new Gson();
				Super p = gson.fromJson(json, Super.class);
				List<Item> i = p.results;
				for (Item a : i) {
					//Add all the products in the category in the respective ArrayLists
					pname.add(a.productName);
					price.add(a.price);
					originalPrice.add(a.originalPrice);
					brandName.add(a.brandName);
					styleId.add(a.styleId);
					percentOff.add(a.percentOff);
					productId.add(a.productId);
					URL newurl = new URL(a.thumbnailImageUrl); 
					Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream()); 
					bitmapArray.add(mIcon_val);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;

		}


		@Override
		protected void onPreExecute() {

			spinner.setMessage("Getting  ...");
			spinner.show();

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {
			spinner.dismiss();
			//Set the adapter in the list when all products in the category are processed
			MyAdapter adapter = new MyAdapter(Product_List.this, pname,bitmapArray,price);
			list.setAdapter(adapter);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, SingleProduct.class);
		i.putExtra("ProductId", productId.get(arg2));
		i.putExtra("StyleId", styleId.get(arg2));
		i.putExtra("ProductName", pname.get(arg2));
		i.putExtra("Price", price.get(arg2));
		i.putExtra("OriginalPrice", originalPrice.get(arg2));
		i.putExtra("BrandName", brandName.get(arg2));
		i.putExtra("Discount", percentOff.get(arg2));


		
		//Add the image in the extras of the intent
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmapArray.get(arg2).compress(Bitmap.CompressFormat.PNG, 100, baos); 
		byte[] b = baos.toByteArray();

	      i.putExtra("Bitmap", b);

		Product_List.this.startActivity(i);
		
	}
	
	//Custom Adapter
	class MyAdapter extends ArrayAdapter<String>{
		
		Context context;
		ArrayList<String> names;
		ArrayList<String> prices;

		ArrayList<Bitmap> images;
		 MyAdapter(Context c,ArrayList<String> names,ArrayList<Bitmap> images,ArrayList<String> prices){
			super(c,R.layout.single_row,R.id.tvlarge,names);
			this.context=c;
			this.names=names;
			this.images=images;
			this.prices=prices;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			View row=inflater.inflate(R.layout.single_row, parent,false);
			ImageView myImage=(ImageView) row.findViewById(R.id.imageView1);
			ImageView arrow=(ImageView) row.findViewById(R.id.imageView2);
			
			if(position%2==0)
		        row.setBackgroundColor(Color.rgb(136, 179, 149));

			TextView myTitle = (TextView) row.findViewById(R.id.tvlarge);
			TextView myDescription = (TextView) row.findViewById(R.id.tvsmall);
			myImage.setImageBitmap(images.get(position));
			myTitle.setText("   "+names.get(position));
			myDescription.setText("   "+"Price : "+prices.get(position));
			arrow.setImageResource(R.drawable.arrow);
			return row;
		}
		 
	}

}
