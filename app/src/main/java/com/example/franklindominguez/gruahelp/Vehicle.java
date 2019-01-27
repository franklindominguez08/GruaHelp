package com.example.franklindominguez.gruahelp;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Vehicle {
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("ClientID")
    @Expose
    private Integer clientID;
    @SerializedName("Placa")
    @Expose
    private String placa;
    @SerializedName("Marca")
    @Expose
    private String marca;
    @SerializedName("Modelo")
    @Expose
    private String modelo;
    @SerializedName("Ano")
    @Expose
    private String ano;

    @SerializedName("creationDate")
    @Expose
    private Date creationDate;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Date getCreationDate() {
        return creationDate;
    }


}