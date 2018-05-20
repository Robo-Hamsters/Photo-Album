package Repo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.*;


public  class LocationParser {

    private String country;
    private String city;
    private double latitude;
    private double longitude;
    private JSONObject responce;

    public void retriveLocation()
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

                for(int i = 0; i<results.length(); i++)
                {
                    if(results.getJSONObject(i).getJSONArray("types").getString(0).equals("country"))
                    {
                        country = results.getJSONObject(i).getString("formatted_address");
                    }

                    if(results.getJSONObject(i).getJSONArray("types").getString(0).equals("locality"))
                    {
                        city = results.getJSONObject(i).getJSONArray("address_components").getJSONObject(0).getString("long_name");
                    }
                }

                //country = results.getJSONObject(results.length()-1).getString("formatted_address");
                //city = results.getJSONObject(3).getJSONArray("address_components").getJSONObject(0).getString("long_name");
            }
        } catch (FileNotFoundException e) {
        } catch (IOException | JSONException e) {
        }
    }

    public String getCity() {
        return city;
    }

    public LocationParser(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        retriveLocation();
    }

    public String getCountry() {
        return country;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public JSONObject getResponce() {
        return responce;
    }

}