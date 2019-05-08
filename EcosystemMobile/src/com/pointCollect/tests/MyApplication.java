package com.pointCollect.tests;


import com.codename1.ui.Command;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.pointCollect.gui.StatsForm;

import com.pointCollect.services.ServiceAddress;
import com.pointCollect.services.SignIn;
import com.pointCollect.services.pointCollect;
import java.io.IOException;

public class MyApplication {

    private Form current;
    private enum SideMenuMode { 
        SIDE, RIGHT_SIDE {
            public String getCommandHint() {
                return SideMenuBar.COMMAND_PLACEMENT_VALUE_RIGHT;
            }
        }, BOTH_SIDES {
            boolean b;
            public String getCommandHint() {
                b = !b;
                if(b) {
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
            if(h == null) {
                return;
            }
            c.putClientProperty(SideMenuBar.COMMAND_PLACEMENT_KEY, h);
        }
    };

    SideMenuMode mode = SideMenuMode.SIDE;
    
    public void init(Object context) {
        try{
            Resources theme = Resources.openLayered("/theme");
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
            UIManager.getInstance().getLookAndFeel().setMenuBarClass(SideMenuBar.class);
            Display.getInstance().setCommandBehavior(Display.COMMAND_BEHAVIOR_SIDE_NAVIGATION);
       }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        newHiForm("Clean");
        
            
    }

    void newHiForm(String title) {
        Form hi = new Form(title);
        hi.setName(title);
      
        buildSideMenu(hi);
        hi.show();
    }
    
    void buildSideMenu(Form hi) {
        Command changeToSideMenuLeft = new Command("Point de collecte") {
            public void actionPerformed(ActionEvent ev) {
                mode = SideMenuMode.SIDE;
                newHiForm("Left");
                pointCollect h = new pointCollect();
            h.start();
          /*  ServiceAddress  a = new ServiceAddress();
            a.getList();*/
            }
        };
      
       // Command dummy = new Command("Dummy 1");
      //  Command dummy2 = new Command("Dummy 2");
        
      /*  mode.updateCommand(dummy);
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