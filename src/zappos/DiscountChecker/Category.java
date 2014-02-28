package zappos.DiscountChecker;

import java.util.ArrayList;

import com.example.zappos.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Category extends Activity implements OnItemClickListener{
	View myView;
	ListView l;

	ArrayList<String> slist = new ArrayList<String>();

	int[] images={R.drawable.boots,R.drawable.kettle,R.drawable.lipstick,R.drawable.shoes,R.drawable.jeans};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		l = (ListView)findViewById(R.id.listView1);
		
		//Adding Categories in the ArrayList
		slist.add("Boots");
		slist.add("Kettle");
		slist.add("LipStick");
		slist.add("Shoes");
		slist.add("Jeans");
		MyAdapter adapter = new MyAdapter(Category.this, slist,images);
		l.setAdapter(adapter);
		l.setOnItemClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		//Open the Product Activity for the particular category
		Intent openSingleTrip = new Intent(this, Product_List.class);
		openSingleTrip.putExtra("FileName", slist.get(arg2));
		Category.this.startActivity(openSingleTrip);

	}
	
	//Custom Adapter
	class MyAdapter extends ArrayAdapter<String>{
		
		Context context;
		ArrayList<String> names;
		int[] images;

		 MyAdapter(Context c,ArrayList<String> names,int[] images){
			super(c,R.layout.single_row,R.id.tvlarge,names);
			this.context=c;
			this.names=names;
			this.images=images;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			View row=inflater.inflate(R.layout.single_row, parent,false);
			if(position%2==0)
		        row.setBackgroundColor(Color.rgb(136, 179, 149));

			ImageView myImage=(ImageView) row.findViewById(R.id.imageView1);
			ImageView arrow=(ImageView) row.findViewById(R.id.imageView2);

			TextView myTitle = (TextView) row.findViewById(R.id.tvlarge);
			TextView myDescription = (TextView) row.findViewById(R.id.tvsmall);
			
			myImage.setImageResource(images[position]);
			myTitle.setText("   "+names.get(position));
			myDescription.setText("   ");
			arrow.setImageResource(R.drawable.arrow);
			return row;
		}
		 
	}
	public void onBackPressed() {
		   this.finish();
		   return;
		}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
