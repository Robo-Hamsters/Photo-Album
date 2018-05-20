package Repo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LocationParser
{
    private JSONObject responce;
    private String country = "";
    private String city = "";
    private double latitude;
    private double longitude;

    public LocationParser(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        retriveLocation();
    }
    public JSONObject getResponce() {
        return responce;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }


    public  void retriveLocation()
    {
        try {

            URL urlObj = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&key=AIzaSyCmA2N-U9gtKDHf-Zmh1ODAtKnyJ54iJBI");
            URLConnection uc = urlObj.openConnection();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()))) {
                String inputLine = new String();
                String ln;
                do{
                    ln = in.readLine();
                    inputLine += ln;
                }while(ln != null);

                responce = new JSONObject(inputLine);
                JSONArray results = responce.getJSONArray("results");

                if(results.length()>0)
                {
                    country = results.getJSONObject(results.length()-1).getString("formatted_address");
                    city = results.getJSONObject(3).getJSONArray("address_components").getJSONObject(0).getString("long_name");
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }
}
