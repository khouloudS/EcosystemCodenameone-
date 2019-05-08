/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pointCollect.services;

import com.codename1.components.InteractionDialog;

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
import com.codename1.maps.MapComponent;
import com.codename1.maps.layers.PointLayer;
import com.codename1.maps.layers.PointsLayer;
import com.codename1.processing.Result;
import com.codename1.ui.AutoCompleteTextField;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Rectangle;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.pointCollect.entities.Markers;
import com.pointCollect.services.ServiceAddress;
import com.pointCollect.tests.MyApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.Slider;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.list.ListCellRenderer;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.google.gson.JsonParser;
import com.pointCollect.gui.addressList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;




/**
 *
 * @author Khouloud
 */
public class pointCollect {

    private static final String HTML_API_KEY = "AIzaSyDb_mKcVpUW3pv4Yq98VrDp84xXtTkfQu8";
     final MapContainer cnt2 = new MapContainer("AIzaSyDXUvonxNp0wCliQrIzCCx77RApdzgdvGM");
      
    private Form current;
    JSONObject testDataObject = null;
  Style s = new Style();
  Style st = new Style();
    private enum SideMenuMode {
        SIDE, RIGHT_SIDE {
            public String getCommandHint() {
                return SideMenuBar.COMMAND_PLACEMENT_VALUE_RIGHT;
            }
        }, BOTH_SIDES {
            boolean b;

            public String getCommandHint() {
                b = !b;
                if (b) {
                    return null;
                }
                return SideMenuBar.COMMAND_PLACEMENT_VALUE_RIGHT;
            }
        }, TOP {
            public String getCommandHint() {
                return SideMenuBar.COMMAND_PLACEMENT_VALUE_TOP;
            }
        };

        public String getCommandHint() {
            return null;
        }

        public void updateCommand(Command c) {
            String h = getCommandHint();
            if (h == null) {
                return;
            }
            c.putClientProperty(SideMenuBar.COMMAND_PLACEMENT_KEY, h);
        }
    };

    SideMenuMode mode = SideMenuMode.SIDE;

    public void init(Object context) {
        try {
            Resources theme = Resources.openLayered("/theme");
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
            Display.getInstance().setCommandBehavior(Display.COMMAND_BEHAVIOR_SIDE_NAVIGATION);
            UIManager.getInstance().getLookAndFeel().setMenuBarClass(SideMenuBar.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (current != null) {
            current.show();
            return;
        }
        
        Form hi = new Form("Nos points de collecte");
        hi.setLayout(new BorderLayout());
        buildSideMenu(hi);
        final MapContainer cnt = new MapContainer(HTML_API_KEY);
       
        Button btnMoveCamera = new Button("Nos adresse");
        btnMoveCamera.addActionListener(e -> {
          //  cnt.setCameraPosition(new Coord(-33.867, 151.206));
       addressList a = new addressList();
       a.start();
        });
      
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
        
         st.setFgColor(0x0000ff);
        st.setBgTransparency(0);
        
        FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_STORE, s, Display.getInstance().convertToPixels(0.5f));
        FontImage markerImg2 = FontImage.createMaterial(FontImage.MATERIAL_PLACE, st, Display.getInstance().convertToPixels(0.6f));

        /*  Button btnAddMarker = new Button("Add Marker");
        btnAddMarker.addActionListener(e->{

            cnt.setCameraPosition(new Coord(41.889, -87.622));
            cnt.addMarker(
                    EncodedImage.createFromImage(markerImg, false),
                    cnt.getCameraPosition(),
                    "Hi marker",
                    "Optional long description",
                     evt -> {
                             ToastBar.showMessage("You clicked the marker", FontImage.MATERIAL_PLACE);
                     }
            );

        });
         */
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://127.0.0.1:8000/markersJsonApi");
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                ServiceAddress ser = new ServiceAddress();
                //  listTasks = ser.parseListTaskJson(new String(con.getResponseData()));
                String f = new String(con.getResponseData());
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

                        cnt.setCameraPosition(new Coord(e.getLat(), e.getIng()));
                        cnt.addMarker(
                                EncodedImage.createFromImage(markerImg, false),
                                cnt.getCameraPosition(),
                                "Hi " + e.getName(),
                                "" + e.getAddress(),
                                evtt -> {
                                    ToastBar.showMessage(""+e.getAddress(), FontImage.MATERIAL_PLACE);
                                }
                        );
                    }

                } catch (IOException ex) {
                }

            }
        });

        Button btnAddPath = new Button("Add Path");
        btnAddPath.addActionListener(e -> {

         /*   cnt.addPath(
                    cnt.getCameraPosition(),
                    new Coord(36.89, 10.309), // Sydney
                    new Coord(36.8822, 10.1552) // Fiji
          
            );*/
         cnt.setShowMyLocation(true);
        });

        Button btnClearAll = new Button("Nettoyer");
        btnClearAll.addActionListener(e -> {
            cnt.clearMapLayers();
        });

        /*   cnt.addTapListener(e -> {
            TextField enterName = new TextField();
            Container wrapper = BoxLayout.encloseY(new Label("Name:"), enterName);
            InteractionDialog dlg = new InteractionDialog("Add Marker");
            dlg.getContentPane().add(wrapper);
            enterName.setDoneListener(e2 -> {
                String txt = enterName.getText();
                cnt.addMarker(
                        EncodedImage.createFromImage(markerImg, false),
                        cnt.getCoordAtPosition(e.getX(), e.getY()),
                        enterName.getText(),
                        "",
                        e3 -> {
                            ToastBar.showMessage("You clicked " + txt, FontImage.MATERIAL_PLACE);
                        }
                );
                dlg.dispose();
            });
            dlg.showPopupDialog(new Rectangle(e.getX(), e.getY(), 10, 10));
            enterName.startEditingAsync();
        });
         */
 /*  Slider jSlider = new Slider();
    
    jSlider.setMaxValue(255);
    jSlider.setMinValue(0); 
    jSlider.setProgress(50); // Set  the starting value
    jSlider.setEditable(true); // */
        final Double lo1 = 36.89839200000002;
        final Double lo2 = 10.189732000000058;
         cnt.setCameraPosition(new Coord(lo1, lo2));
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
            NetworkManager.getInstance().addToQueueAndWait(con);
        } catch (Exception ex) {
            ex.getMessage();
        }
        // cnt.setCameraPosition(new Coord(41.889, -87.622));

        
        
     
       
         cnt.setCameraPosition(new Coord(lo1,lo2));
                         cnt.addMarker(
                                EncodedImage.createFromImage(markerImg2, false),
                                cnt.getCameraPosition(),
                                "Hi je suis ici",
                                "" ,
                                evtt -> {
                                    ToastBar.showMessage("You clicked the marker", FontImage.MATERIAL_PLACE);
                                }
                        );
        
        Toolbar.setGlobalToolbar(true);

        Form h = new Form("Toolbar", new BoxLayout(BoxLayout.Y_AXIS));
        TextField searchField = new TextField("", "Distance KM",10 ,TextArea.NUMERIC); // <1>
        
        searchField.getHintLabel().setUIID("Title");
        searchField.setUIID("Title");
        searchField.getAllStyles().setAlignment(Component.LEFT);
        h.getToolbar().setTitleComponent(searchField);
        

        FontImage searchIcon = FontImage.createMaterial(FontImage.MATERIAL_SEARCH, s);

        h.getToolbar().addCommandToRightBar("", searchIcon, ee -> {
            
            verifDes(searchField);
            //  con2.setUrl("http://127.0.0.1:8000/markersJsonApi");
            String t = searchField.getText();

            int dis = Integer.valueOf(t);
            System.err.println("result " + dis);

            
            try {
                search(lo1,lo2,dis);
                cnt2.setCameraPosition(new Coord(lo1,lo2));
                cnt2.addMarker(
                                EncodedImage.createFromImage(markerImg2, false),
                                cnt2.getCameraPosition(),
                                "Hi je suis ici",
                                "" ,
                                evtt -> {
                                    ToastBar.showMessage("You clicked the marker", FontImage.MATERIAL_PLACE);
                                }
                        );
            
            } catch (IOException ex) {
                 Dialog.show("Alert", "La distance ne doit pas etre un caractère", "OK", null);
            }
           
         //   h.getContentPane().animateLayout(250);

        });

        
 
        
        Tabs t = new Tabs();
        Style ss = UIManager.getInstance().getComponentStyle("Tab");
        FontImage icon1 = FontImage.createMaterial(FontImage.MATERIAL_MAP, ss);
        FontImage icon2 = FontImage.createMaterial(FontImage.MATERIAL_SEARCH, ss);

        
        btnMoveCamera.setIcon(FontImage.createMaterial(FontImage.MATERIAL_DIRECTIONS, btnMoveCamera.getUnselectedStyle()));
        btnClearAll.setIcon(FontImage.createMaterial(FontImage.MATERIAL_CLEAR, btnClearAll.getUnselectedStyle()));
        Container root = LayeredLayout.encloseIn(
             
                BorderLayout.center(cnt),
                BorderLayout.north(
                        FlowLayout.encloseCenter(btnMoveCamera, btnClearAll)
                )
        );

        Container container2 = BoxLayout.encloseY(
                BorderLayout.north(h)
                ,BorderLayout.center(cnt2)
        );
        t.addTab("Geolocaliser", icon1, root);
        t.addTab("Chercher", icon2, container2);

        hi.add(BorderLayout.SOUTH, t);
        hi.show();

        /*  hi.add(BorderLayout.CENTER, root);
        hi.show();*/
    }

    void newHiForm(String title) {
        Form hi = new Form(title);
        hi.setName(title);

        hi.show();
    }

    void buildSideMenu(Form hi) {
        Command changeToSideMenuLeft = new Command("Point collecte") {
            public void actionPerformed(ActionEvent ev) {
                mode = pointCollect.SideMenuMode.SIDE;
                newHiForm("Point collecte");
                pointCollect h = new pointCollect();
                h.start();
               /* ServiceAddress a = new ServiceAddress();
                a.getList();*/
            }
        };
        
      //  Command dummy = new Command("Dummy 1");
    //    Command dummy2 = new Command("Dummy 2");

    /*    mode.updateCommand(dummy);
        hi.addCommand(dummy);
        mode.updateCommand(dummy2);
        hi.addCommand(dummy2);*/
        mode.updateCommand(changeToSideMenuLeft);
        hi.addCommand(changeToSideMenuLeft);
      
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }

    public void destroy() {
    }

   
   

public class GoogleReverseGeocoderService extends ConnectionRequest {
    private final static String BASEURL = "http://127.0.0.1:8000/markersDistance";
    private final static String PARAM_LATLNG="latlng";
    public GoogleReverseGeocoderService(double lo1, double lo2 , int dis) {
           setUrl(BASEURL+"/"+ lo1 + "/" + lo2 + "/" + dis);
           setContentType("application/json");
           addRequestHeader("Accept", "application/json");
           setPost(false);
     }
}
public void search (double lo1, double lo2 , int dis) throws IOException
{
      cnt2.clearMapLayers();
     FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_STORE, s, Display.getInstance().convertToPixels(0.5f));
     FontImage markerImg2 = FontImage.createMaterial(FontImage.MATERIAL_PLACE, st, Display.getInstance().convertToPixels(0.6f));
ConnectionRequest request = new GoogleReverseGeocoderService( lo1,  lo2 ,  dis) {
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

//                        float id = Float.parseFloat(obj.get("id").toString());

                       // e.setId((int) id);
                        //   e.setEtat(obj.get("state").toString());
                        e.setName(obj.get("name").toString());
                        e.setAddress(obj.get("address").toString());
                        e.setIng(Double.parseDouble(obj.get("lng").toString()));
                        e.setLat(Double.parseDouble(obj.get("lat").toString()));
                        System.out.println(e);

                        listTasks.add(e);
                         
                        cnt2.setCameraPosition(new Coord(e.getLat(), e.getIng()));
                        cnt2.addMarker(
                                EncodedImage.createFromImage(markerImg, false),
                                cnt2.getCameraPosition(),
                                "Hi " + e.getName(),
                                "" + e.getAddress(),
                                evtt -> {
                                    ToastBar.showMessage(""+e.getAddress(), FontImage.MATERIAL_PLACE);
                                }
                        );
                        
                         
                        
                    }

                } catch (IOException ex) {
                }
       }
};
NetworkManager.getInstance().addToQueue(request); 
}


public void verifDes(TextField t)
{
    int a = Integer.valueOf(t.getText());
    if((a<0)){
                        Dialog.show("Alert", "La distance ne doit pas etre négative", "OK", null);
    }              
}


}
