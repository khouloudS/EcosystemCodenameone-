/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pointCollect.gui;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkManager;
import com.codename1.processing.Result;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.List;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.list.ListCellRenderer;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.pointCollect.entities.Favoris;
import com.pointCollect.entities.Markers;
import com.pointCollect.services.ListAddressService;
import com.pointCollect.services.ServiceAddress;
import com.pointCollect.services.ServicesFavoris;
import com.pointCollect.services.pointCollect;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author Khouloud
 */
public  class addressList  {
  
    
     int idFav = 0;
      private Form current;
  java.util.List<Integer> lst2 = new ArrayList<>();
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
  int cpt = 0;
    public void start()
    {

//int fiveMM = Display.getInstance().convertToPixels(5);
//final Image finalDuke = duke.scaledWidth(fiveMM);
Toolbar.setGlobalToolbar(true);
Form hi = new Form("Rechercher addresse", BoxLayout.y());

   buildSideMenu(hi);

hi.add(new InfiniteProgress());
Display.getInstance().scheduleBackgroundTask(()-> {
    // this will take a while...
   
    Display.getInstance().callSerially(() -> {
        hi.removeAll();
        
        
         
        ConnectionRequest request = new ListAddressService() {
      protected void readResponse(InputStream input) throws IOException {
          
             
          
           Result result = Result.fromContent(input, Result.JSON); // ... expressions here
           System.err.println("sss"+result);
           
                //   String f = new String(result.toString());
                   String f= String.valueOf(result);
                ArrayList<Markers> listTasks = new ArrayList<>();

                try {
                    JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

                    Map<String, Object> tasks = j.parseJSON(new CharArrayReader(f.toCharArray()));

                    java.util.List<Map<String, Object>> list = (java.util.List<Map<String, Object>>) tasks.get("root");
                   
          
                    //Parcourir la liste des tâches Json
                    for (Map<String, Object> obj : list) {
                        //Création des tâches et récupération de leurs données

                        Style style = new Style();
                       
                        MultiButton m = new MultiButton("CheckBox");
                     
//                       float id = Float.parseFloat(obj.get("id").toString());

                    //    e.setId((int) id);
                        //   e.setEtat(obj.get("state").toString());
                        m.setTextLine1(obj.get("name").toString());
                        m.setTextLine2(obj.get("distance").toString()+" KM");
                        m.setTextLine3(obj.get("address").toString());
                        m.setIcon(FontImage.createMaterial(FontImage.MATERIAL_FAVORITE_BORDER, style));
                         float id = Float.parseFloat(obj.get("id").toString());
                         int c = (int)id;
                         
                         
                         System.err.println("compteur init"+ cpt);
        java.util.List<Integer> lst2 = new ArrayList<>();
    ListAddressService request = new ListAddressService(c) {
      protected void readResponse(InputStream input) throws IOException {
           Result result = Result.fromContent(input, Result.JSON); // ... expressions here
          
                //   String f = new String(result.toString());
                   String f= String.valueOf(result);
 
  float id =0;

                try {
                    
                   
                    JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

                    Map<String, Object> tasks = j.parseJSON(new CharArrayReader(f.toCharArray()));

                    java.util.List<Map<String, Object>> list = (java.util.List<Map<String, Object>>) tasks.get("root");

                    //Parcourir la liste des tâches Json
                    for (Map<String, Object> obj : list) {
                        //Création des tâches et récupération de leurs données
                        ArrayList<Integer> e = new ArrayList<>();

                 float idAddress = Float.parseFloat(obj.get("idAddress").toString());
                  float id2 = Float.parseFloat(obj.get("id").toString());
                         idFav = (int)id2;
             
                e.add((int)idAddress);
             
              //  System.out.println(e);
                 
                   
                       lst2.addAll(e);
                        m.setIcon(FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, style));
                       cpt=1;
                           System.err.println("compteur action deja ajouté "+ cpt);  
                    }
                    
                 

                } catch (IOException ex) {
                    
                    ex.printStackTrace();
                }
                
             //  lst2.add((int)id);
             
       }
      
};
  
NetworkManager.getInstance().addToQueue(request); 
                         
                    //    System.out.println("test "+ verifAddress(c));
                        
                                      //  m.setIcon(FontImage.createMaterial(FontImage.MATERIAL_THUMB_UP, style));
 
                       m.setHorizontalLayout(true);
                       // e.setIng(Double.parseDouble(obj.get("lng").toString()));
                      //
                                   
                      
                                   hi.add(m);
                                   
                      Markers mr = new Markers();
                    
                      mr.setId((int)id);
                      
                  System.err.println("compteur avant l'action  "+ cpt);                             
                       m.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent ev) {
        // button was clicked, you can do anything you want here...
        //routeDirection p = new routeDirection();
      //  p.start(Double.parseDouble(obj.get("lat").toString()), Double.parseDouble(obj.get("lng").toString()));
       ServicesFavoris f = new ServicesFavoris();
     boolean test= f.verifFavoris(mr.getId());
      if(cpt==0){
       
         System.err.println("verif ajout "+f.verifFavoris(mr.getId()));
        f.ajoutFavoris(mr);
         
        m.setIcon(FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, style));
       
        System.err.println("ajout succee");
          System.err.println("compteur action ajout "+ cpt);
         cpt=1;
       
      }
      else if(cpt==1)
      {
        
           System.err.println("verif ajout "+f.verifFavoris(mr.getId()));
           f.SuppFavoris(idFav);
            m.setIcon(FontImage.createMaterial(FontImage.MATERIAL_FAVORITE_BORDER, style));
            
              System.err.println("compteur action supp "+ cpt);  
            
            cpt=0;
            
        
      }
        
        
        
    }
    
    
});
                      
                      
                      
                      
                       

                    }
                    
        hi.revalidate();
                    System.err.println("refresh cpt "+ cpt);
                } catch (IOException ex) {
                }
       }
};
NetworkManager.getInstance().addToQueue(request); 
        
      
    });
});

hi.getToolbar().addSearchCommand(e -> {
    String text = (String)e.getSource();
    if(text == null || text.length() == 0) {
        // clear search
        for(Component cmp : hi.getContentPane()) {
            cmp.setHidden(false);
            cmp.setVisible(true);
        }
        hi.getContentPane().animateLayout(150);
    } else {
        text = text.toLowerCase();
        for(Component cmp : hi.getContentPane()) {
            MultiButton mb = (MultiButton)cmp;
            String line1 = mb.getTextLine1();
            String line2 = mb.getTextLine2();
            boolean show = line1 != null && line1.toLowerCase().indexOf(text) > -1 ||
                    line2 != null && line2.toLowerCase().indexOf(text) > -1;
            mb.setHidden(!show);
            mb.setVisible(show);
        }
        hi.getContentPane().animateLayout(150);
    }
}, 4);
Style s = new Style();
FontImage searchIcon = FontImage.createMaterial(FontImage.MATERIAL_KEYBOARD_BACKSPACE,s);
hi.getToolbar().addCommandToLeftBar("", searchIcon,e-> {
       pointCollect p = new pointCollect();
       p.start();
    });

hi.show();


    }
   

    void newHiForm(String title) {
        Form hi = new Form(title);
        hi.setName(title);

        hi.show();
    }

    void buildSideMenu(Form hi) {
        Command changeToSideMenuLeft = new Command("Point collecte") {
            public void actionPerformed(ActionEvent ev) {
                mode = addressList.SideMenuMode.SIDE;
                newHiForm("Point collecte");
                pointCollect h = new pointCollect();
                h.start();
               /* ServiceAddress a = new ServiceAddress();
                a.getList();*/
            }
        };
       
      //  Command dummy = new Command("Dummy 1");
        //Command dummy2 = new Command("Dummy 2");

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

  
}
    
    

