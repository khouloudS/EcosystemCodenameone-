/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pointCollect.gui;

import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.URL;
import com.codename1.io.URL.HttpURLConnection;
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
import com.pointCollect.entities.Markers;
import com.pointCollect.services.ListAddressService;


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
public class routeDirection {
    
 private static final String HTML_API_KEY = "AIzaSyDb_mKcVpUW3pv4Yq98VrDp84xXtTkfQu8";
     
      
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

    public void start(double lat, double lng) {
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
           
        } catch (Exception ex) {
            ex.getMessage();
        }
        // cnt.setCameraPosition(new Coord(41.889, -87.622));
        
        ConnectionRequest con = new ConnectionRequest();
        ListAddressService l = new ListAddressService();
        Coord origin = new Coord(lat, lng);
         Coord dest = new Coord(lo1, lo2);
       String direction =  l.getDirectionsUrl(origin, dest);
         System.out.println("url = "+direction);

     
      ConnectionRequest request = new  ListAddressService(direction) {
      protected void readResponse(InputStream input) throws IOException {
           Result result = Result.fromContent(input, Result.JSON); // ... expressions here
           System.err.println("resultat  = "+result);
           
                //   String f = new String(result.toString());
                   String f= String.valueOf(result);
                   System.err.println("resultat 2 "+ f);
                ArrayList<Markers> listTasks = new ArrayList<>();

                try {
                    JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

                    Map<String, Object> tasks = j.parseJSON(new CharArrayReader(f.toCharArray()));

                    List<Map<String, Object>> list = (List<Map<String, Object>>) tasks.get("root");

                    //Parcourir la liste des tâches Json
                    for (Map<String, Object> obj : list) {
                        //Création des tâches et récupération de leurs données
                       
                        //   e.setEtat(obj.get("state").toString());
                     
                   
                    double lat1 = Double.parseDouble(obj.get("lat").toString());
                    double lng1 = Double.parseDouble(obj.get("lng").toString());

                        System.err.println("coor : "+lat1+" "+lng1);
                       
                    }

                } catch (IOException ex) {
                }
                
              
       }
      
};

  NetworkManager.getInstance().addToQueueAndWait(request);

      

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
          cnt.setCameraPosition(new Coord(lat,lng));
                         cnt.addMarker(
                                EncodedImage.createFromImage(markerImg, false),
                                cnt.getCameraPosition(),
                                "Hi je suis ici",
                                "" ,
                                evtt -> {
                                    ToastBar.showMessage("You clicked the marker", FontImage.MATERIAL_PLACE);
                                }
                        );
        
        Toolbar.setGlobalToolbar(true);

  
       
        
 
        
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

      
        hi.add(BorderLayout.CENTER, root);
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
        Command changeToSideMenuLeft = new Command("Left Menu") {
            public void actionPerformed(ActionEvent ev) {
                mode = routeDirection.SideMenuMode.SIDE;
                newHiForm("Left");
                routeDirection h = new routeDirection();
               // h.start(lat, 0);
               /* ServiceAddress a = new ServiceAddress();
                a.getList();*/
            }
        };
        Command changeToSideMenuRight = new Command("Right Menu") {
            public void actionPerformed(ActionEvent ev) {
                mode = routeDirection.SideMenuMode.RIGHT_SIDE;
                newHiForm("Right");
            }
        };
        Command changeToSideMenuBoth = new Command("Both Menu") {
            public void actionPerformed(ActionEvent ev) {
                mode = routeDirection.SideMenuMode.BOTH_SIDES;
                newHiForm("Both");
            }
        };
        Command changeToSideMenuTop = new Command("Top Menu") {
            public void actionPerformed(ActionEvent ev) {
                mode = routeDirection.SideMenuMode.TOP;
                newHiForm("Top");
            }
        };

        Command dummy = new Command("Dummy 1");
        Command dummy2 = new Command("Dummy 2");

        mode.updateCommand(dummy);
        hi.addCommand(dummy);
        mode.updateCommand(dummy2);
        hi.addCommand(dummy2);
        mode.updateCommand(changeToSideMenuLeft);
        hi.addCommand(changeToSideMenuLeft);
        mode.updateCommand(changeToSideMenuRight);
        hi.addCommand(changeToSideMenuRight);
        mode.updateCommand(changeToSideMenuBoth);
        hi.addCommand(changeToSideMenuBoth);
        mode.updateCommand(changeToSideMenuTop);
        hi.addCommand(changeToSideMenuTop);
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }

    public void destroy() {
    }

   
   



}
