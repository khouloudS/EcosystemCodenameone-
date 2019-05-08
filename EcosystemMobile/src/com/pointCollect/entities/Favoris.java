/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pointCollect.entities;


/**
 *
 * @author khouloud
 */
public class Favoris {
    private int id;
    private int idAddress;
    private int idUser;

    public Favoris(int id, int idAddress, int idUser) {
        this.id = id;
        this.idAddress = idAddress;
        this.idUser = idUser;
    }

    public Favoris(int idAddress) {
        this.idAddress = idAddress;
    }

    
    public Favoris() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }



    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    
    
}
