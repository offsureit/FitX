package src.kioskfitnessapplication.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import src.kioskfitnessapplication.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class RestApiUtil {

    public static final String API_URL="http://offsureitapps.com/fitlogistix";


    public static User login(String userName, String password){
        try {
        DefaultHttpClient httpClient = new DefaultHttpClient();
            StringBuilder API_METHOD_URL=new StringBuilder(API_URL);
            API_METHOD_URL.append("/user/login");
        HttpPost postRequest = new HttpPost(API_METHOD_URL.toString());
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("username", userName));
            urlParameters.add(new BasicNameValuePair("password", password));
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(postRequest);
            int responseCode = response.getStatusLine().getStatusCode();
            System.out.println("\nSending 'POST' request to URL : " + API_METHOD_URL);

            System.out.println("Response Code : " + responseCode);
            if(responseCode== HttpStatus.SC_OK) {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                Gson gson = new Gson();
                System.out.println(result.toString());
                JsonObject responseData = gson.fromJson(result.toString(), JsonObject.class);
               return  buildResponse(responseData, userName);
            }
        }  catch (IOException e) {

            e.printStackTrace();

        }
        return null;

    }
    public static boolean createUserLicense(int userId, String macAddress){
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            StringBuilder API_METHOD_URL=new StringBuilder(API_URL);
            API_METHOD_URL.append("/user_license_detail/create");
            HttpPost postRequest = new HttpPost(API_METHOD_URL.toString());
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("user_id", String.valueOf(userId)));
            urlParameters.add(new BasicNameValuePair("user_mac_address", macAddress));
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(postRequest);
            int responseCode = response.getStatusLine().getStatusCode();
            System.out.println("\nSending 'POST' request to URL : " + API_METHOD_URL);

            System.out.println("Response Code : " + responseCode);
            if(responseCode== HttpStatus.SC_OK) {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                Gson gson = new Gson();
                System.out.println(result.toString());
                JsonObject responseData = gson.fromJson(result.toString(), JsonObject.class);
                return  responseData.get("success").getAsBoolean();
            }
        }  catch (IOException e) {

            e.printStackTrace();

        }
        return false;

    }
    private static User buildResponse(JsonObject responseData,String userName){
        if(responseData.get("data").getAsJsonObject().get("login_status").getAsString().equalsIgnoreCase("false"))
            return null;
        else {
            User user = new User();
            user.setName(userName);
            user.setUser_id(responseData.get("data").getAsJsonObject().get("user_id").getAsInt());
            user.setLicenseCount(responseData.get("data").getAsJsonObject().get("license_count").getAsInt());
            user.setLoginStatus(responseData.get("data").getAsJsonObject().get("login_status").getAsString());
            user.setLicense_uses_count(responseData.get("data").getAsJsonObject().get("license_uses_count").getAsInt());
            user.setMacAddress(convert(responseData.get("data").getAsJsonObject().get("user_mac_addresses").getAsJsonArray()));
            return user;
        }
    }

    private static ArrayList<String> convert(JsonArray jArr)
    {
        ArrayList<String> list = new ArrayList<>();
        try {
            for (int i=0, l=jArr.size(); i<l; i++){
                list.add(jArr.get(i).getAsString());
            }
        } catch (JsonIOException ignored) {}
        return list;
    }


}
