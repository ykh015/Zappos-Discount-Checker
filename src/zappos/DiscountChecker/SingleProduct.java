package zappos.DiscountChecker;

import java.util.ArrayList;

import com.example.zappos.R;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleProduct extends Activity implements OnClickListener{

	private static final String PRODUCT_ID = "pid";
	private static final String STYLE_ID = "sid";
	String s_id,p_id;
	Button b_fav;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);
		
		//Get the information received from the intent
		String pname = getIntent().getExtras().getString("ProductName");
		String bname = getIntent().getExtras().getString("BrandName");
		s_id = getIntent().getExtras().getString("StyleId");
		p_id = getIntent().getExtras().getString("ProductId");
		String price = getIntent().getExtras().getString("Price");
		String originalPrice = getIntent().getExtras().getString("OriginalPrice");
		String discount = getIntent().getExtras().getString("Discount");

		//Set the image from the intent
		Bundle extras = getIntent().getExtras();
		byte[] b = extras.getByteArray("Bitmap");
		Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
		ImageView image = (ImageView) findViewById(R.id.imageView1);
		image.setImageBitmap(bmp);

		//Set the views
		TextView tv_pname = (TextView)findViewById(R.id.pname);
		TextView tv_bname = (TextView)findViewById(R.id.bname);
		TextView tv_price = (TextView)findViewById(R.id.price);
		TextView tv_oprice = (TextView)findViewById(R.id.oprice);
		TextView tv_dicount = (TextView)findViewById(R.id.discount);
		b_fav = (Button)findViewById(R.id.button1);
		b_fav.setOnClickListener(this);
		
		boolean check=checkProductinDatabase();	//Check if the product is in the database (i.e. Is it marked as favorite)
		if(check)
			b_fav.setText("Remove as Favorite");
		else
			b_fav.setText("Add as Favorite");
		tv_pname.setText(pname);
		tv_bname.setText(bname);
		tv_price.setText(price);
		tv_oprice.setText(originalPrice);
		tv_dicount.setText(discount);
		

	}

	private boolean checkProductinDatabase() {
		// TODO Auto-generated method stub
		
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
