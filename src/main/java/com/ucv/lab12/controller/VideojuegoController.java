package com.ucv.lab12.controller;

import com.ucv.lab12.config.AppContext;
import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.service.IVideojuegoService;
import com.ucv.lab12.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class VideojuegoController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtGenero;
    @FXML private TextField txtDistribuidor;
    @FXML private Label lblTotal;

    @FXML private TableView<Videojuego> tableView;
    @FXML private TableColumn<Videojuego, Boolean> colSeleccion;
    @FXML private TableColumn<Videojuego, Integer> colId;
    @FXML private TableColumn<Videojuego, String> colConsola;
    @FXML private TableColumn<Videojuego, String> colNombre;
    @FXML private TableColumn<Videojuego, String> colGenero;
    @FXML private TableColumn<Videojuego, String> colClasificacion;
    @FXML private TableColumn<Videojuego, String> colDescripcion;
    @FXML private TableColumn<Videojuego, Integer> colIdDesarrollador;
    @FXML private TableColumn<Videojuego, String> colDistribuidor;
    @FXML private TableColumn<Videojuego, Void> colAcciones;

    private final IVideojuegoService service;
    private final ObservableList<Videojuego> data = FXCollections.observableArrayList();

    public VideojuegoController(IVideojuegoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        cargarDatos("", "", "");

        txtNombre.setOnAction(e -> onBuscar());
        txtGenero.setOnAction(e -> onBuscar());
        txtDistribuidor.setOnAction(e -> onBuscar());
    }

    private void configurarColumnas() {
        tableView.setEditable(true);

        colSeleccion.setCellValueFactory(cell -> cell.getValue().seleccionadoProperty());
        colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
        colSeleccion.setEditable(true);
        colSeleccion.setSortable(false);

        colId.setCellValueFactory(new PropertyValueFactory<>("idVideojuego"));
        colConsola.setCellValueFactory(new PropertyValueFactory<>("consola"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colIdDesarrollador.setCellValueFactory(new PropertyValueFactory<>("idDesarrollador"));
        colDistribuidor.setCellValueFactory(new PropertyValueFactory<>("distribuidorNombre"));
        colAcciones.setCellFactory(crearCeldaAcciones());
        colAcciones.setSortable(false);

        tableView.setItems(data);
    }

    private Callback<TableColumn<Videojuego, Void>, TableCell<Videojuego, Void>> crearCeldaAcciones() {
        return col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox = new HBox(5, btnEditar, btnEliminar);

            {
                hbox.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color:#1976D2;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");
                btnEliminar.setStyle("-fx-background-color:#D32F2F;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");

                btnEditar.setOnAction(e -> {
                    Videojuego videojuego = getTableView().getItems().get(getIndex());
                    abrirFormulario(videojuego);
                });
                btnEliminar.setOnAction(e -> {
                    Videojuego videojuego = getTableView().getItems().get(getIndex());
                    confirmarEliminar(videojuego);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        };
    }

    private void cargarDatos(String nombre, String genero, String distribuidor) {
        try {
            List<Videojuego> lista = service.buscar(nombre, genero, distribuidor);
            data.setAll(lista);
            lblTotal.setText("Total: " + data.size() + " registro(s)");
        } catch (Exception e) {
            AlertUtil.error("Error de conexion", "No se pudo cargar los datos:", e);
        }
    }

    @FXML
    private void onBuscar() {
        cargarDatos(txtNombre.getText(), txtGenero.getText(), txtDistribuidor.getText());
    }

    @FXML
    private void onCrear() {
        abrirFormulario(null);
    }

    @FXML
    private void onEliminarSeleccionados() {
        List<Integer> ids = data.stream()
            .filter(Videojuego::isSeleccionado)
            .map(Videojuego::getIdVideojuego)
            .collect(Collectors.toList());

        if (ids.isEmpty()) {
            AlertUtil.advertencia("Sin seleccion", "Marque al menos un registro para eliminar.");
            return;
        }

        boolean ok = AlertUtil.confirmar("Confirmar eliminacion",
            "Esta seguro de eliminar " + ids.size() + " videojuego(s) seleccionado(s)?");
        if (!ok) return;

        try {
            service.eliminarSeleccionados(ids);
            cargarDatos(txtNombre.getText(), txtGenero.getText(), txtDistribuidor.getText());
            AlertUtil.info("Exito", "Se eliminaron " + ids.size() + " registro(s).");
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar:", e);
        }
    }

    private void confirmarEliminar(Videojuego videojuego) {
        boolean ok = AlertUtil.confirmar("Confirmar eliminacion",
            "Eliminar videojuego: " + videojuego.getNombre() + "?");
        if (!ok) return;

        try {
            service.eliminar(videojuego.getIdVideojuego());
            cargarDatos(txtNombre.getText(), txtGenero.getText(), txtDistribuidor.getText());
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar:", e);
        }
    }

    private void abrirFormulario(Videojuego videojuego) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/lab12/videojuego-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();

            VideojuegoFormController formCtrl = loader.getController();
            formCtrl.setVideojuego(videojuego);
            formCtrl.setOnGuardar(() ->
                cargarDatos(txtNombre.getText(), txtGenero.getText(), txtDistribuidor.getText()));

            Stage modal = new Stage();
            modal.setTitle(videojuego == null ? "Nuevo Videojuego" : "Editar Videojuego");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario:\n" + e.getMessage());
        }
    }
}
