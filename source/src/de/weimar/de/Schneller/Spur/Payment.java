package de.weimar.de.Schneller.Spur;


import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import de.weimar.de.Schneller.Spur.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Payment extends Activity {

	// 1: Create HTTPCLient as the form container
		HttpClient httpclient;
		
		// 2: Use HTTP Post method
		HttpPost httppost;
		
		// 3: Create an array list for the input data to be sent
		ArrayList<NameValuePair> nameValuePairs;
		
		// 4: Create a HTTP Response and HTTP Entity
		HttpResponse response;
		HttpEntity	entity;
	
	ImageView cash;
	TextView pname,pprice;  // text views
	String productname,price,userid; // for the product_detail page
	int p_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.payment);   // sets the content of first from first.xm
			
	        pname = (TextView)findViewById(R.id.pname);
	        pprice = (TextView)findViewById(R.id.pprice);
	       
	        p_id = getIntent().getIntExtra("pid", 0);    // get product id
	        productname = getIntent().getStringExtra("pname");
	        price = getIntent().getStringExtra("pprice");
	        
	      

			SharedPreferences usersession = getSharedPreferences("usersession", 0); 
			userid = usersession.getString("userid",null);   // user ID saved
	        
	        
	        pname.setText(productname);   	// sets the value to text view
	        pprice.setText(price); 			// sets the value to text view
	        
	        
	        cash = (ImageView) findViewById(R.id.cash_method_select);
	        
	        cash.setOnClickListener(new OnClickListener() {
	        	
	        	@Override
				public void onClick(View v) {

	        					
	        					// for network strict mode of android
	        	        		if (android.os.Build.VERSION.SDK_INT > 9) {
	        	        			StrictMode.ThreadPolicy policy = 
	        	        				new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        	        			StrictMode.setThreadPolicy(policy);
	        	        		}
	        					
	        					
	        					   // create new default HTTPClient
	        			    	  httpclient = new DefaultHttpClient();
	        			 
	        			    	  // Create new HTTP POST with URL of our .php file
	        			    	  httppost = new HttpPost("http://141.54.152.162/buysell/purchase.php");

	        			    	  try {
	        			    		  
	        			    		  // Checks Network Connectivity Start
	        			    		    SocketAddress sockaddr = new InetSocketAddress("141.54.152.162", 80);
	        			    		    // Create an unbound socket
	        			    		    Socket sock = new Socket();

	        			    		    // This method will block no more than timeoutMs.
	        			    		    // If the timeout occurs, SocketTimeoutException is thrown.
	        			    		    int timeoutMs = 2000;   // 2 seconds
	        			    		    sock.connect(sockaddr, timeoutMs);
	        			    		    sock.close();
	        			    		  // Checks Network Connectivity End
	        			    		  
	        			    		  
	        			    		    
	        			    		 // Create array to place the above variable for sending to server scripting file.
	        			    			  final ArrayList<NameValuePair> purchasepairs;
	        			    			  purchasepairs = new ArrayList<NameValuePair>();
	        			    			  
	        			    			  // Fills the Array with the registration form data.
	        			    			  
	        			    			  String pid = Integer.toString(p_id);
	        			    			  purchasepairs.add(new BasicNameValuePair("pid",pid));
	        			    			  purchasepairs.add(new BasicNameValuePair("pname", productname));
	        			    			  purchasepairs.add(new BasicNameValuePair("userid", userid));
	        			
	        			    		    
	        			    		    
	        			    		  // Add array list to http post
	        			    		  httppost.setEntity(new UrlEncodedFormEntity(purchasepairs));
	        			    		  
	        			    		  // Assign executed form container to response 		    		  
	        			    		  response = httpclient.execute(httppost);
	        			    		  
	        			    		  // Check status code, need to check status code 200
	        			    		  if (response.getStatusLine().getStatusCode() == 200) {
	        			    			  // assign response entity to http entity
	        			    			  
	        			    			  entity = response.getEntity();		    			  
	        			    			  // check if entity is not null
	        			    			  if (entity !=null) {
	        			    				  //Display a Toast sent by server
	        		    					  Toast.makeText(getBaseContext(),"Product Request Sent to Owner !!", Toast.LENGTH_LONG).show();
	        		    					  Intent cashintent = new Intent(Payment.this,Purchases.class); // 
	        		    				      startActivity(cashintent); 
	        		    				
	        			    		            }
	        			    	
	        			    			  }	   
	        			    		  
	        			    	  }catch(Exception e) {
	        			    		  e.printStackTrace();
	        			    		  // Display Toast message when connection error.
	        			    		  Toast.makeText(getBaseContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
	        			    	  }
	        			    	  }
	        });
}    
  
// Convert stream to strings

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
    		Intent loginintent = new Intent(Payment.this,Login.class); // 
			startActivity(loginintent);
			break;
			
    	case R.id.register:
    		Intent registerintent = new Intent(Payment.this,register.class); // 
			startActivity(registerintent);
			break;
			
    	case R.id.search_box:
			Intent searchintent = new Intent(Payment.this,Search.class); // 
			startActivity(searchintent);
			break;
			
    	case R.id.profile:
			Intent profileintent = new Intent(Payment.this,profile.class); // 
			startActivity(profileintent);
			break;
			
    	case R.id.help:
			Intent helpintent = new Intent(Payment.this,Help.class); // 
			startActivity(helpintent);
			break;
			
    	case R.id.sell:
			Intent sellintent = new Intent(Payment.this,sell.class); // 
			startActivity(sellintent);
			break;
			
    	case R.id.Main:
			Intent mainmenuintent = new Intent(Payment.this,Products.class); // 
			startActivity(mainmenuintent);
			break;
		
			
    	case R.id.purchases_Menu:
			Intent purchaseintent = new Intent(Payment.this,Purchases.class); // 
			startActivity(purchaseintent);
			break;
			
    	case R.id.Request_Menu:
			Intent requestsintent = new Intent(Payment.this,requests.class); // 
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
			Intent logoutintent = new Intent(Payment.this,Products.class); // 
			startActivity(logoutintent);   // starts the BuySell activity  
			Toast.makeText(getBaseContext(),"You are logged Out", Toast.LENGTH_SHORT).show();
			
			break;

			
    	}
    
    	return true;
    }
	
}
		
			