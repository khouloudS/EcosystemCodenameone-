/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package com.pointCollect.gui;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.CubicLineChart;
import com.codename1.charts.views.PointStyle;
import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import static com.codename1.ui.ComponentSelector.$;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableModel;
import com.codename1.ui.util.Resources;
import com.pointCollect.entities.Favoris;
import  com.pointCollect.entities.Markers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Shai Almog
 */
//public class FavorisForm extends SideMenuBaseForm {

   /*
 
    private static final int[] COLORS = {0xf8e478, 0x60e6ce, 0x878aee};
    private static final String[] LABELS = {"Design", "Coding", "Learning"};
    private Resources res = UIManager.initFirstTheme("/theme_1");

   
    public FavorisForm(Resources res) {
        super(new BorderLayout());
  
        Toolbar tb = getToolbar();
        tb.setTitleCentered(false);
        /*Image profilePic = res.getImage("user-picture.jpg");        
        Image tintedImage = Image.createImage(profilePic.getWidth(), profilePic.getHeight());
        Graphics g = tintedImage.getGraphics();
        g.drawImage(profilePic, 0, 0);
        g.drawImage(res.getImage("gradient-overlay.png"), 0, 0, profilePic.getWidth(), profilePic.getHeight());
      
        tb.getUnselectedStyle().setBgImage(tintedImage);
         */
    /*    Button menuButton = new Button("");
        menuButton.setUIID("Title");
        FontImage.setMaterialIcon(menuButton, FontImage.MATERIAL_MENU);
        menuButton.addActionListener(e -> getToolbar().openSideMenu());

        Button settingsButton = new Button("");
        settingsButton.setUIID("Title");
        FontImage.setMaterialIcon(settingsButton, FontImage.MATERIAL_SETTINGS);

        Label space = new Label("", "TitlePictureSpace");
        space.setShowEvenIfBlank(true);
        Container titleComponent
                = BorderLayout.north(
                        BorderLayout.west(menuButton).add(BorderLayout.EAST, settingsButton)
                ).
                        // add(BorderLayout.CENTER, space).
                        add(BorderLayout.SOUTH,
                                FlowLayout.encloseIn(
                                        new Label(" Favoris ", "WelcomeBlue"),
                                        new Label("", "WelcomeWhite")
                                ));
        titleComponent.setUIID("BottomPaddingContainer");
        tb.setTitleComponent(titleComponent);

        Label separator = new Label("", "BlueSeparatorLine");
        separator.setShowEvenIfBlank(true);
        add(BorderLayout.NORTH, separator);

    
        
        
        
        ServiceFilm serviceFilm = new ServiceFilm();
        ArrayList l = new ArrayList();
        l = serviceFilm.getListFav();

        Container list = new Container(BoxLayout.y());
        list.setScrollableY(true);

        for (Iterator it = l.iterator(); it.hasNext();) {
            Favoris f = (Favoris) it.next();
            list.add(createContactContainer(f));
        }

        add(BorderLayout.CENTER, list);

        setupSideMenu(res);
      
    }

    @Override
    protected void showOtherForm(Resources res) {
        new ProfileForm(res).show();
    }

    private Container createContactContainer(Favoris f) {
             Container cnt = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        Button favoris = new Button("favoris");
      
                int rowNum = f.getId();
        int colNum = f.getId();
        Button myButton = $(new Button("Supprimer"))
                .addTags("cell", "row-" + f.getId(), "Buttoncol-" + f.getId(), rowNum % 2 == 0 ? "even" : "odd")
                .putClientProperty("row", rowNum)
                .putClientProperty("col", colNum)
                .asComponent(Button.class);
        myButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //   Dialog.show("Dialog Title", rowNum+"", "OK", "Cancel");
                ServiceFilm sf = new ServiceFilm();
                
                        sf.SuppFavoris(f.getNomFilm());
                        cnt.remove();
                        cnt.refreshTheme();
            }

        });

        
        //Button b = new Button("Detail");
      
        
       
        
       Label name = new Label("");
        
       
         
        name.getAllStyles().setBgTransparency(0);
        name.getAllStyles().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        // duree.getAllStyles().setBgTransparency(0);
        name.setText(f.getNomFilm());
        cnt.add(name);
        cnt.add(myButton);
        
        

        return BorderLayout.center(cnt);
    

    }
     
      
*/
    

//}
