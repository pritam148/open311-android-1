package gov.in.bloomington.open311.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class GeoreporterAPI {
	public static boolean isConnected(Activity a) {
		HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;

        //get server URL from shared preferences
        SharedPreferences pref = a.getSharedPreferences("server",0);
		JSONObject server;
		boolean is_connected = false;
		try {
			server = new JSONObject(pref.getString("selectedServer", ""));
			String server_url = server.getString("url");
			HttpPost post1 = new HttpPost(server_url);
		    
		    response = client.execute(post1);

			if (response!=null) {

	        	is_connected = true;;
	        }
	        else {

	        	is_connected = false;
	        }
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			is_connected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			is_connected = false;
		}
        
        return is_connected;
	}
	
	public static JSONArray getServices(Activity a) {
		SharedPreferences pref = a.getSharedPreferences("server",0);
		JSONArray services_list = null;
		JSONObject server;
		try {
			server = new JSONObject(pref.getString("selectedServer", ""));
			String server_url = server.getString("url");
			
			HttpClient client = new DefaultHttpClient();
	        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
	        HttpResponse response;
	        HttpGet get = new HttpGet(server_url+"/services.json");
            response = client.execute(get);
            /*Checking response */
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                String str = GeoreporterUtils.convertStreamToString(in);
                services_list = new JSONArray(str);
            }
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (ClientProtocolException e) {
        	// TODO Auto-generated catch block
        }
        catch(Exception e){
        	// TODO Auto-generated catch block
        }
		return services_list;
	}
	
	public static JSONObject getServiceAttribute(Activity a, String service_code) {
		SharedPreferences pref = a.getSharedPreferences("server",0);
		JSONObject services_attribute = null;
		JSONObject server;
		try {
			server = new JSONObject(pref.getString("selectedServer", ""));
			String server_url = server.getString("url");
			HttpClient client = new DefaultHttpClient();
	        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
	        HttpResponse response;
	        
	        HttpGet get = new HttpGet(server_url+"/services/"+service_code+".json");
            response = client.execute(get);
            /*Checking response */
            if(response!=null){
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                String str = GeoreporterUtils.convertStreamToString(in);
                services_attribute = new JSONObject(str);
            }
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClientProtocolException e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
        }
        catch(Exception e){
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
		return services_attribute;
	}
	
	public static JSONArray sendReport(Activity a, String jurisdiction_id, String service_code, Double latitude,Double longitude, boolean hasattribute, List<NameValuePair> attribute, String email, String device_id, String first_name, String last_name, String phone, String description) {
		SharedPreferences pref = a.getSharedPreferences("server",0);
		JSONArray reply = new JSONArray(); 
		HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;
		
		try {
			
			JSONObject server = new JSONObject(pref.getString("selectedServer", ""));
			String server_url = server.getString("url");
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	        pairs.add(new BasicNameValuePair("jurisdiction_id", jurisdiction_id));
	        pairs.add(new BasicNameValuePair("service_code", service_code));
	        pairs.add(new BasicNameValuePair("lat", latitude+""));
	        pairs.add(new BasicNameValuePair("long", longitude+""));
	        if (hasattribute)
	        	pairs.add(new BasicNameValuePair("attribute", attribute.toString()));
	        pairs.add(new BasicNameValuePair("email", email));
	        pairs.add(new BasicNameValuePair("device_id", device_id+""));
	        pairs.add(new BasicNameValuePair("first_name", first_name));
	        pairs.add(new BasicNameValuePair("last_name", last_name));
	        pairs.add(new BasicNameValuePair("phone", phone));
	        pairs.add(new BasicNameValuePair("description", description));
	        
			
	        HttpPost post1 = new HttpPost(server_url+"/requests.json");
            post1.setEntity(new UrlEncodedFormEntity(pairs));
            
            response = client.execute(post1);
	        
            if(response!=null){
            	Log.d("georeporter api", "1 response not null");
                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                String str = GeoreporterUtils.convertStreamToString(in);
                Log.d("georeporter api", "1 "+str);
                reply = new JSONArray(str);
            }
            else 
            	Log.d("georeporter api", "2 response null");
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return reply;
	}
	
}

