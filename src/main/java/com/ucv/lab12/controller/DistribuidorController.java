package com.ucv.lab12.controller;

import com.ucv.lab12.config.AppContext;
import com.ucv.lab12.model.Distribuidor;
import com.ucv.lab12.service.IDistribuidorService;
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

public class DistribuidorController implements Initializable {

    // --- Filtros ---
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtRuc;
    @FXML private Button    btnBuscar;

    // --- Acciones superiores ---
    @FXML private Button btnCrear;
    @FXML private Button btnEliminarSeleccionados;
    @FXML private Label  lblTotal;

    // --- Tabla ---
    @FXML private TableView<Distribuidor>              tableView;
    @FXML private TableColumn<Distribuidor, Boolean>   colSeleccion;
    @FXML private TableColumn<Distribuidor, Integer>   colId;
    @FXML private TableColumn<Distribuidor, String>    colRazonSocial;
    @FXML private TableColumn<Distribuidor, String>    colRuc;
    @FXML private TableColumn<Distribuidor, String>    colTelefono;
    @FXML private TableColumn<Distribuidor, String>    colEmail;
    @FXML private TableColumn<Distribuidor, String>    colDireccion;
    @FXML private TableColumn<Distribuidor, Boolean>   colActivo;
    @FXML private TableColumn<Distribuidor, Void>      colAcciones;

    private final IDistribuidorService service;
    private final ObservableList<Distribuidor> data = FXCollections.observableArrayList();

    public DistribuidorController(IDistribuidorService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos("", "");

        // Permitir búsqueda con Enter en los filtros
        txtRazonSocial.setOnAction(e -> onBuscar());
        txtRuc.setOnAction(e -> onBuscar());
    }

    // -------------------------------------------------------------------------
    // Configuración de columnas
    // -------------------------------------------------------------------------

    private void configurarColumnas() {
        tableView.setEditable(true);

        // Checkbox de selección
        colSeleccion.setCellValueFactory(cell -> cell.getValue().seleccionadoProperty());
        colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
        colSeleccion.setEditable(true);
        colSeleccion.setSortable(false);

        colId.setCellValueFactory(new PropertyValueFactory<>("idDistribuidor"));
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<>("razonSocial"));
        colRuc.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Columna Activo: muestra Sí/No con color
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colActivo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Sí" : "No");
                    setStyle(item
                        ? "-fx-text-fill: #2e7d32; -fx-font-weight: bold;"
                        : "-fx-text-fill: #c62828; -fx-font-weight: bold;");
                }
            }
        });

        // Columna de acciones (Editar / Eliminar)
        colAcciones.setCellFactory(crearCeldaAcciones());
        colAcciones.setSortable(false);

        tableView.setItems(data);
    }

    private Callback<TableColumn<Distribuidor, Void>, TableCell<Distribuidor, Void>> crearCeldaAcciones() {
        return col -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox   hbox        = new HBox(5, btnEditar, btnEliminar);

            {
                hbox.setAlignment(Pos.CENTER);
                btnEditar.setStyle(
                    "-fx-background-color:#1976D2;-fx-text-fill:white;"
                    + "-fx-cursor:hand;-fx-font-size:11px;");
                btnEliminar.setStyle(
                    "-fx-background-color:#D32F2F;-fx-text-fill:white;"
                    + "-fx-cursor:hand;-fx-font-size:11px;");

                btnEditar.setOnAction(e -> {
                    Distribuidor d = getTableView().getItems().get(getIndex());
                    abrirFormulario(d);
                });
                btnEliminar.setOnAction(e -> {
                    Distribuidor d = getTableView().getItems().get(getIndex());
                    confirmarEliminar(d);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        };
    }

    // -------------------------------------------------------------------------
    // Carga de datos
    // -------------------------------------------------------------------------

    private void cargarDatos(String razonSocial, String ruc) {
        try {
            List<Distribuidor> lista = service.buscar(razonSocial, ruc);
            data.setAll(lista);
            lblTotal.setText("Total: " + data.size() + " registro(s)");
        } catch (Exception e) {
            AlertUtil.error("Error de conexión", "No se pudo cargar los datos:\n" + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Handlers FXML
    // -------------------------------------------------------------------------

    @FXML
    private void onBuscar() {
        cargarDatos(txtRazonSocial.getText(), txtRuc.getText());
    }

    @FXML
    private void onCrear() {
        abrirFormulario(null);
    }

    @FXML
    private void onEliminarSeleccionados() {
        List<Integer> ids = data.stream()
            .filter(Distribuidor::isSeleccionado)
            .map(Distribuidor::getIdDistribuidor)
            .collect(Collectors.toList());

        if (ids.isEmpty()) {
            AlertUtil.advertencia("Sin selección",
                "Marque al menos un registro para eliminar.");
            return;
        }

        boolean ok = AlertUtil.confirmar("Confirmar eliminación",
            "¿Está seguro de eliminar " + ids.size() + " distribuidor(es) seleccionado(s)?");
        if (!ok) return;

        try {
            service.eliminarSeleccionados(ids);
            cargarDatos(txtRazonSocial.getText(), txtRuc.getText());
            AlertUtil.info("Éxito", "Se eliminaron " + ids.size() + " registro(s).");
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Acciones de fila
    // -------------------------------------------------------------------------

    private void confirmarEliminar(Distribuidor d) {
        boolean ok = AlertUtil.confirmar("Confirmar eliminación",
            "¿Eliminar a: " + d.getRazonSocial() + "?");
        if (!ok) return;

        try {
            service.eliminar(d.getIdDistribuidor());
            cargarDatos(txtRazonSocial.getText(), txtRuc.getText());
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    private void abrirFormulario(Distribuidor distribuidor) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/ucv/lab12/distribuidor-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();

            DistribuidorFormController formCtrl = loader.getController();
            formCtrl.setDistribuidor(distribuidor);
            formCtrl.setOnGuardar(() ->
                cargarDatos(txtRazonSocial.getText(), txtRuc.getText()));

            Stage modal = new Stage();
            modal.setTitle(distribuidor == null ? "Nuevo Distribuidor" : "Editar Distribuidor");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario:\n" + e.getMessage());
        }
    }
}
