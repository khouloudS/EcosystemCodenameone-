/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pointCollect.services;

import com.codename1.components.ToastBar;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.maps.Coord;
import com.codename1.processing.Result;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.events.ActionListener;
import com.pointCollect.entities.Markers;
import com.pointCollect.tests.MyApplication;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Khouloud
 */
public class ServiceAddress {
 
 public ArrayList<Markers> parseListTaskJson(String json) {

        ArrayList<Markers> listTasks = new ArrayList<>();

        try {
            JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

            /*
                On doit convertir notre réponse texte en CharArray à fin de
            permettre au JSONParser de la lire et la manipuler d'ou vient 
            l'utilité de new CharArrayReader(json.toCharArray())
            
            La méthode parse json retourne une MAP<String,Object> ou String est 
            la clé principale de notre résultat.
            Dans notre cas la clé principale n'est pas définie cela ne veux pas
            dire qu'elle est manquante mais plutôt gardée à la valeur par defaut
            qui est root.
            En fait c'est la clé de l'objet qui englobe la totalité des objets 
                    c'est la clé définissant le tableau de tâches.
            */
            Map<String, Object> tasks = j.parseJSON(new CharArrayReader(json.toCharArray()));
                       
            
            /* Ici on récupère l'objet contenant notre liste dans une liste 
            d'objets json List<MAP<String,Object>> ou chaque Map est une tâche                
            */
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasks.get("root");

            //Parcourir la liste des tâches Json
            for (Map<String, Object> obj : list) {
                //Création des tâches et récupération de leurs données
                Markers e = new Markers();

                 float id = Float.parseFloat(obj.get("id").toString());

                e.setId((int) id);
                //   e.setEtat(obj.get("state").toString());
                e.setName(obj.get("name").toString());
                e.setAddress(obj.get("address").toString());
                e.setIng(Double.parseDouble(obj.get("lng").toString()));
                e.setLat(Double.parseDouble(obj.get("lat").toString()));
                System.out.println(e);
                listTasks.add(e);

            }

        } catch (IOException ex) {
        }
        
        /*
            A ce niveau on a pu récupérer une liste des tâches à partir
        de la base de données à travers un service web
        
        */
        System.out.println(listTasks);
        return listTasks;

    }
    
    
    ArrayList<Markers> listTasks = new ArrayList<>();
    
    public ArrayList<Markers> getList2(){       
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://127.0.0.1:8000/markersJsonApi");  
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ServiceAddress ser = new ServiceAddress();
                listTasks = ser.parseListTaskJson(new String(con.getResponseData()));
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listTasks;
    }


    JSONObject obj = new JSONObject();

   

    private void parseTestData(JSONObject employee) {
        //Get employee object within list
        JSONObject testDataObject = (JSONObject) employee.get("markers");

        String description = (String) testDataObject.get("address");

        System.err.println("kkkkkkk" + description);

    }

    
    public class GoogleReverseGeocoderService extends ConnectionRequest {
    private final static String BASEURL = "http://127.0.0.1:8000/addressNameJsonApi";
    private final static String PARAM_LATLNG="latlng";
    public GoogleReverseGeocoderService(int id) {
           setUrl(BASEURL+"/"+id);
           setContentType("application/json");
           addRequestHeader("Accept", "application/json");
           setPost(false);
     }
}
public void search (int id) throws IOException
{
    
ConnectionRequest request = new GoogleReverseGeocoderService( id) {
      protected void readResponse(InputStream input) throws IOException {
           Result result = Result.fromContent(input, Result.JSON); // ... expressions here
           System.err.println("sss"+result);
           
                //   String f = new String(result.toString());
                   String f= String.valueOf(result);
                ArrayList<Markers> listTasks = new ArrayList<>();

                try {
                    JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

                    Map<String, Object> tasks = j.parseJSON(new CharArrayReader(f.toCharArray()));

                    List<Map<String, Object>> list = (List<Map<String, Object>>) tasks.get("root");

                    //Parcourir la liste des tâches Json
                    for (Map<String, Object> obj : list) {
                        //Création des tâches et récupération de leurs données
                        Markers e = new Markers();

                        float id = Float.parseFloat(obj.get("id").toString());

                        e.setId((int) id);
                        //   e.setEtat(obj.get("state").toString());
                        e.setName(obj.get("name").toString());
                        e.setAddress(obj.get("address").toString());
                        e.setIng(Double.parseDouble(obj.get("lng").toString()));
                        e.setLat(Double.parseDouble(obj.get("lat").toString()));
                        System.out.println(e);

                        listTasks.add(e);

                        
                       
                    }

                } catch (IOException ex) {
                }
       }
};
NetworkManager.getInstance().addToQueue(request); 
}


    
}
