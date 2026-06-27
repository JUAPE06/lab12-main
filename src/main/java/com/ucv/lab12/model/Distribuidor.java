package com.ucv.lab12.model;

import javafx.beans.property.*;

/**
 * Entidad Distribuidor con JavaFX Properties para binding en TableView.
 */
public class Distribuidor {

    private final IntegerProperty idDistribuidor = new SimpleIntegerProperty();
    private final StringProperty  razonSocial    = new SimpleStringProperty();
    private final StringProperty  ruc            = new SimpleStringProperty();
    private final StringProperty  telefono       = new SimpleStringProperty();
    private final StringProperty  email          = new SimpleStringProperty();
    private final StringProperty  direccion      = new SimpleStringProperty();
    private final BooleanProperty activo         = new SimpleBooleanProperty(true);
    /** Propiedad auxiliar para el checkbox de selección en la tabla. */
    private final BooleanProperty seleccionado   = new SimpleBooleanProperty(false);

    public Distribuidor() {}

    public Distribuidor(int idDistribuidor, String razonSocial, String ruc,
                        String telefono, String email, String direccion, boolean activo) {
        setIdDistribuidor(idDistribuidor);
        setRazonSocial(razonSocial);
        setRuc(ruc);
        setTelefono(telefono);
        setEmail(email);
        setDireccion(direccion);
        setActivo(activo);
    }

    // --- Properties ---
    public IntegerProperty idDistribuidorProperty() { return idDistribuidor; }
    public StringProperty  razonSocialProperty()    { return razonSocial; }
    public StringProperty  rucProperty()            { return ruc; }
    public StringProperty  telefonoProperty()       { return telefono; }
    public StringProperty  emailProperty()          { return email; }
    public StringProperty  direccionProperty()      { return direccion; }
    public BooleanProperty activoProperty()         { return activo; }
    public BooleanProperty seleccionadoProperty()   { return seleccionado; }

    // --- Getters ---
    public int     getIdDistribuidor() { return idDistribuidor.get(); }
    public String  getRazonSocial()    { return razonSocial.get(); }
    public String  getRuc()            { return ruc.get(); }
    public String  getTelefono()       { return telefono.get(); }
    public String  getEmail()          { return email.get(); }
    public String  getDireccion()      { return direccion.get(); }
    public boolean isActivo()          { return activo.get(); }
    public boolean isSeleccionado()    { return seleccionado.get(); }

    // --- Setters ---
    public void setIdDistribuidor(int v)    { idDistribuidor.set(v); }
    public void setRazonSocial(String v)    { razonSocial.set(v); }
    public void setRuc(String v)            { ruc.set(v); }
    public void setTelefono(String v)       { telefono.set(v); }
    public void setEmail(String v)          { email.set(v); }
    public void setDireccion(String v)      { direccion.set(v); }
    public void setActivo(boolean v)        { activo.set(v); }
    public void setSeleccionado(boolean v)  { seleccionado.set(v); }

    @Override
    public String toString() {
        return getRazonSocial() + " (" + getRuc() + ")";
    }
}
