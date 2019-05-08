/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pointCollect.services;

import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.location.Location;
import com.codename1.location.LocationManager;
import com.codename1.maps.Coord;
import com.codename1.processing.Result;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.pointCollect.entities.Favoris;
import com.pointCollect.entities.Markers;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author Khouloud
 */

    
   public class ListAddressService extends ConnectionRequest {
         private static final String HTML_API_KEY = "AIzaSyDb_mKcVpUW3pv4Yq98VrDp84xXtTkfQu8";
    private final static String BASEURL = "http://127.0.0.1:8000/nearestAddress";
    private final static String favVerif = "http://127.0.0.1:8000/VerifFavoris";
    private final static String PARAM_LATLNG="latlng";
     boolean test = false;     
  
    public ListAddressService() {
         final Double lo1 = 36.89839200000002;
        final Double lo2 = 10.189732000000058;
       
        try {
            /*    Location loc = LocationManager.getLocationManager().getCurrentLocation();
             cnt.setCameraPosition(new Coord(loc.getLatitude(), loc.getLongitude()));*/

            Location location = LocationManager.getLocationManager().getCurrentLocationSync(30000);

            if (location == null) {
                try {
                    location = LocationManager.getLocationManager().getCurrentLocation();
                } catch (IOException err) {
                    Dialog.show("Location Error", "Unable to find your current location, please be sure that your GPS is turned on", "OK", null);
                    return;
                }
            }
            Double loc1 = location.getLatitude();
            Double loc2 = location.getLongitude();
            System.err.println("loc1 " + loc1 + " " + loc2);

            //  System.out.println("user location "+loc);
            //36.89839200000002//10.189732000000058   
           
        } catch (Exception ex) {
            ex.getMessage();
        }

           setUrl(BASEURL+"/"+ lo1 + "/" + lo2);
           setContentType("application/json");
           addRequestHeader("Accept", "application/json");
           setPost(false);
     }
    
    
      public ListAddressService(String url) {
        

           setUrl(BASEURL);
           setContentType("application/json");
           addRequestHeader("Accept", "application/json");
           setPost(false);
     }
      
     public ListAddressService(int add) {
        

           setUrl(favVerif+"/"+add);
           setContentType("application/json");
           addRequestHeader("Accept", "application/json");
           setPost(false);
     } 
    
    public String getDirectionsUrl(Coord origin, Coord dest) {

        //https://us1.locationiq.com/v1/matrix/driving/13.388860,52.517037;13.397634,52.529407;13.428555,52.523219?key=384b3444404038
        
        // Origin of route
        String str_origin = "origin=" + origin.getLatitude() + "," + origin.getLongitude();

        // Destination of route
        String str_dest = "destination=" + dest.getLatitude() + "," + dest.getLongitude();

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
      //  String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&key="+ HTML_API_KEY;
        String org = origin.getLatitude() + "," + origin.getLongitude();
        String dis = dest.getLatitude() + "," + dest.getLongitude();
    String url = "https://us1.locationiq.com/v1/matrix/driving/"+ org + ";" + dis + "?key=384b3444404038";
        return url;
    }
    
    

    public boolean verifAddress (int add) throws IOException
{
   
    List<Integer> lst2 = new ArrayList<>();
 
     List<Integer> listTasks = new ArrayList<>();
    ListAddressService request = new ListAddressService(add) {
      protected void readResponse(InputStream input) throws IOException {
           Result result = Result.fromContent(input, Result.JSON); // ... expressions here
          
           
                //   String f = new String(result.toString());
                   String f= String.valueOf(result);
 
  float id =0;

                try {
                    
                   
                    JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

                    Map<String, Object> tasks = j.parseJSON(new CharArrayReader(f.toCharArray()));

                    List<Map<String, Object>> list = (List<Map<String, Object>>) tasks.get("root");

                    //Parcourir la liste des tâches Json
                    for (Map<String, Object> obj : list) {
                        //Création des tâches et récupération de leurs données
                        ArrayList<Integer> e = new ArrayList<>();

                 float idAddress = Float.parseFloat(obj.get("idAddress").toString());
             
                e.add((int)idAddress);
             
              //  System.out.println(e);
                    test = true;
                   
                       listTasks.addAll(e);
                       
                        
                    }
                    
                 

                } catch (IOException ex) {
                    
                    ex.printStackTrace();
                }
                
             //  lst2.add((int)id);
               
             System.err.println("list "+ listTasks + " test "+test);
                    
                    
       }
      
};

   
    
    
    
NetworkManager.getInstance().addToQueue(request); 

return test;
          
}



   }
    



   
    

