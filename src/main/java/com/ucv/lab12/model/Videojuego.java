package com.ucv.lab12.model;

import javafx.beans.property.*;

public class Videojuego {

    private final IntegerProperty idVideojuego = new SimpleIntegerProperty();
    private final StringProperty consola = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty genero = new SimpleStringProperty();
    private final StringProperty clasificacion = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final IntegerProperty idDesarrollador = new SimpleIntegerProperty();
    private final IntegerProperty idDistribuidor = new SimpleIntegerProperty();
    private final StringProperty distribuidorNombre = new SimpleStringProperty();
    private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);

    public Videojuego() {}

    public Videojuego(int idVideojuego, String consola, String nombre, String genero,
                      String clasificacion, String descripcion, int idDesarrollador,
                      int idDistribuidor, String distribuidorNombre) {
        setIdVideojuego(idVideojuego);
        setConsola(consola);
        setNombre(nombre);
        setGenero(genero);
        setClasificacion(clasificacion);
        setDescripcion(descripcion);
        setIdDesarrollador(idDesarrollador);
        setIdDistribuidor(idDistribuidor);
        setDistribuidorNombre(distribuidorNombre);
    }

    public IntegerProperty idVideojuegoProperty() { return idVideojuego; }
    public StringProperty consolaProperty() { return consola; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty generoProperty() { return genero; }
    public StringProperty clasificacionProperty() { return clasificacion; }
    public StringProperty descripcionProperty() { return descripcion; }
    public IntegerProperty idDesarrolladorProperty() { return idDesarrollador; }
    public IntegerProperty idDistribuidorProperty() { return idDistribuidor; }
    public StringProperty distribuidorNombreProperty() { return distribuidorNombre; }
    public BooleanProperty seleccionadoProperty() { return seleccionado; }

    public int getIdVideojuego() { return idVideojuego.get(); }
    public String getConsola() { return consola.get(); }
    public String getNombre() { return nombre.get(); }
    public String getGenero() { return genero.get(); }
    public String getClasificacion() { return clasificacion.get(); }
    public String getDescripcion() { return descripcion.get(); }
    public int getIdDesarrollador() { return idDesarrollador.get(); }
    public int getIdDistribuidor() { return idDistribuidor.get(); }
    public String getDistribuidorNombre() { return distribuidorNombre.get(); }
    public boolean isSeleccionado() { return seleccionado.get(); }

    public void setIdVideojuego(int value) { idVideojuego.set(value); }
    public void setConsola(String value) { consola.set(value); }
    public void setNombre(String value) { nombre.set(value); }
    public void setGenero(String value) { genero.set(value); }
    public void setClasificacion(String value) { clasificacion.set(value); }
    public void setDescripcion(String value) { descripcion.set(value); }
    public void setIdDesarrollador(int value) { idDesarrollador.set(value); }
    public void setIdDistribuidor(int value) { idDistribuidor.set(value); }
    public void setDistribuidorNombre(String value) { distribuidorNombre.set(value); }
    public void setSeleccionado(boolean value) { seleccionado.set(value); }
}
