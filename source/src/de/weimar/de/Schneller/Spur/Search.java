package de.weimar.de.Schneller.Spur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.weimar.de.Schneller.Spur.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Search extends Activity {

	private JSONArray jsonarray;
	private EditText searchbox;
	private ListView productsearchlist;
	Button search;
	public String searchstring;  // string of the search box
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);   // sets the content of first from first.xml
        
        search = (Button) findViewById(R.id.search_btn);
        this.productsearchlist = (ListView) this.findViewById(R.id.search_product_list);
        
        search.setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View v) {
        		
        		searchbox = (EditText) findViewById(R.id.search_box_input);
        		
            	// search box input stored to string
            	searchstring = searchbox.getText().toString();
            	
            	Toast.makeText(getBaseContext(),searchstring, Toast.LENGTH_SHORT).show();
            	
            	
            	new GetAllProductList().execute(new ApiConnector());
        	}
        	
        });
        
	// for click of each item
    this.productsearchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		try
    		{
    			// Get the item clicked
    			JSONObject productClicked = jsonarray.getJSONObject(position);
    			
    			// send Product ID.
    			Intent show_product_detail = new Intent(getApplicationContext(),Product_Detail.class);
    			show_product_detail.putExtra("Pid", productClicked.getInt("Pid"));

    			startActivity(show_product_detail);	
    			
    		}
    		catch(JSONException e)
    		{
    			e.printStackTrace();
    		}
    		
    		
    	}
    	
	});
	
	
	}
	
	private class GetAllProductList extends AsyncTask<ApiConnector,Long,JSONArray>
    {
        @Override
       
        protected JSONArray doInBackground(ApiConnector... params) {
        
            // it is executed on Background thread

             return params[0].getseachItems(searchstring);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

        	setListAdapter(jsonArray);
        	
        }
    }

	
	  public void setListAdapter (JSONArray jsonArray) {
	    	this.jsonarray = jsonArray;
	    	this.productsearchlist.setAdapter(new Get_All_Product_list_View(jsonArray,this));
	    }
	

    // my option menu
    @Override
	public boolean onCreateOptionsMenu (Menu menu){
		
		MenuInflater inflater = getMenuInflater();
		
		
		// Get user session
		String userid = null;

		SharedPreferences usersession = getSharedPreferences("usersession", 0); 
		userid = usersession.getString("userid",null);
		
		//if guest
		
		if (userid == null) {    
			inflater.inflate(R.menu.public_menu, menu);
			return true;
	
		}else   // if logged in user
		{
			inflater.inflate(R.menu.user_menu, menu);
			return true;
		}
	
		
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	switch(item.getItemId()) {
		
    	case R.id.login:
    		Intent loginintent = new Intent(Search.this,Login.class); // 
			startActivity(loginintent);
			break;
			
    	case R.id.register:
    		Intent registerintent = new Intent(Search.this,register.class); // 
			startActivity(registerintent);
			break;
			
    	case R.id.search_box:
			Intent searchintent = new Intent(Search.this,Search.class); // 
			startActivity(searchintent);
			break;
		
    	case R.id.profile:
    		Intent profileintent = new Intent(Search.this,profile.class); // 
    		startActivity(profileintent);
    		break;
			
    	case R.id.help:
			Intent helpintent = new Intent(Search.this,Help.class); // 
			startActivity(helpintent);
			break;
    		
    	case R.id.sell:
			Intent sellintent = new Intent(Search.this,sell.class); // 
			startActivity(sellintent);
			break;
		
    	case R.id.Main:
			Intent mainmenuintent = new Intent(Search.this,Products.class); // 
			startActivity(mainmenuintent);
			break;

    	case R.id.purchases_Menu:
			Intent purchaseintent = new Intent(Search.this,Purchases.class); // 
			startActivity(purchaseintent);
			break;
			
    	case R.id.Request_Menu:
			Intent requestsintent = new Intent(Search.this,requests.class); // 
			startActivity(requestsintent);
			break;
			
    	case R.id.logout:
			
			// create a shared preference 
			SharedPreferences usersession = getSharedPreferences("usersession", 0);
			  
		    // Edit the shared preference
			SharedPreferences.Editor spedit = usersession.edit();
			    
			// Cleans the userid string to null
			spedit.putString("userid",null );

			  // Commits the changes and closes the editor
			  spedit.commit();
			  
			//Takes the user to the login screen and displays log out message  
			Intent logoutintent = new Intent(Search.this,Products.class); // 
			startActivity(logoutintent);   // starts the BuySell activity  
			Toast.makeText(getBaseContext(),"You are logged Out", Toast.LENGTH_SHORT).show();
			
			break;

			
    	}
    	
    
    	return true;
    }
	
}
