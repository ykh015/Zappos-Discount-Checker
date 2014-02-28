package zappos.DiscountChecker;




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "favProducts";
 
    // Contacts table name
 
    // Contacts Table Columns names
    private static final String PRODUCT_ID = "pid";
    private static final String STYLE_ID = "sid";
    private static final String FAV_PRODUCT = "fav_product";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAV_PRODUCT_TABLE = "CREATE TABLE " + FAV_PRODUCT + "("
                + PRODUCT_ID + " TEXT," + STYLE_ID + " TEXT" + ")";
        db.execSQL(CREATE_FAV_PRODUCT_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FAV_PRODUCT);
 
        // Create tables again
        onCreate(db);
    }
    
    //Add a product with Product Id = pid and Style Id = sid
    public void addProduct(String pid,String sid){
    	SQLiteDatabase db = this.getWritableDatabase();
    
    		ContentValues values = new ContentValues();
    		values.put(PRODUCT_ID, pid); // Contact Phone Number
    		values.put(STYLE_ID, sid); // Contact Name
    
    		db.insert(FAV_PRODUCT, null, values);
    		db.close(); // Closing database connection

    }
    
    //Get all the products in the database and return the cursor
    public Cursor getProduct(){
    	SQLiteDatabase db = this.getReadableDatabase();    
    	String[] projection = {
    			PRODUCT_ID,
    			STYLE_ID
    		    };
    		Cursor cursor = db.query(FAV_PRODUCT,projection, null, null, null, null,null);
			return cursor;

    }
    
    //Delete a product with given parameters
    public void deleteProduct(String p_id,String s_id) {
    		SQLiteDatabase db = this.getWritableDatabase();
    		String selection = PRODUCT_ID + " = ? and "+STYLE_ID + " = ?";
    		String[] selectionArgs = { p_id,s_id};
    		db.delete(FAV_PRODUCT, selection, selectionArgs);
    		db.close();
    	}
}
