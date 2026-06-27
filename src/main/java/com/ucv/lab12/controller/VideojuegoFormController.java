package com.ucv.lab12.controller;

import com.ucv.lab12.model.Distribuidor;
import com.ucv.lab12.model.Videojuego;
import com.ucv.lab12.service.IDistribuidorService;
import com.ucv.lab12.service.IVideojuegoService;
import com.ucv.lab12.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VideojuegoFormController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private TextField txtConsola;
    @FXML private TextField txtNombre;
    @FXML private TextField txtGenero;
    @FXML private TextField txtClasificacion;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtIdDesarrollador;
    @FXML private ComboBox<Distribuidor> cboDistribuidor;
    @FXML private Label lblNombreError;
    @FXML private Label lblConsolaError;
    @FXML private Label lblIdDesarrolladorError;
    @FXML private Label lblDistribuidorError;
    @FXML private Button btnCancelar;

    private final IVideojuegoService videojuegoService;
    private final IDistribuidorService distribuidorService;
    private Videojuego videojuego;
    private Runnable onGuardar;

    public VideojuegoFormController(IVideojuegoService videojuegoService,
                                    IDistribuidorService distribuidorService) {
        this.videojuegoService = videojuegoService;
        this.distribuidorService = distribuidorService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ocultarErrores();
        cargarDistribuidores();

        limitarLongitud(txtConsola, 45);
        limitarLongitud(txtNombre, 45);
        limitarLongitud(txtGenero, 45);
        limitarLongitud(txtClasificacion, 45);
        limitarLongitud(txtDescripcion, 45);

        txtConsola.textProperty().addListener((o, a, b) -> lblConsolaError.setVisible(false));
        txtNombre.textProperty().addListener((o, a, b) -> lblNombreError.setVisible(false));
        txtIdDesarrollador.textProperty().addListener((o, a, b) -> lblIdDesarrolladorError.setVisible(false));
        cboDistribuidor.valueProperty().addListener((o, a, b) -> lblDistribuidorError.setVisible(false));
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
        if (videojuego == null) {
            lblTitulo.setText("Nuevo Videojuego");
            return;
        }

        lblTitulo.setText("Editar Videojuego");
        txtConsola.setText(nvl(videojuego.getConsola()));
        txtNombre.setText(nvl(videojuego.getNombre()));
        txtGenero.setText(nvl(videojuego.getGenero()));
        txtClasificacion.setText(nvl(videojuego.getClasificacion()));
        txtDescripcion.setText(nvl(videojuego.getDescripcion()));
        txtIdDesarrollador.setText(String.valueOf(videojuego.getIdDesarrollador()));
        seleccionarDistribuidor(videojuego.getIdDistribuidor());
    }

    public void setOnGuardar(Runnable onGuardar) {
        this.onGuardar = onGuardar;
    }

    @FXML
    private void onGuardar() {
        if (!validarFormulario()) return;

        Videojuego v = videojuego != null ? videojuego : new Videojuego();
        Distribuidor distribuidor = cboDistribuidor.getValue();

        v.setConsola(txtConsola.getText().trim());
        v.setNombre(txtNombre.getText().trim());
        v.setGenero(txtGenero.getText().trim());
        v.setClasificacion(txtClasificacion.getText().trim());
        v.setDescripcion(txtDescripcion.getText().trim());
        v.setIdDesarrollador(Integer.parseInt(txtIdDesarrollador.getText().trim()));
        v.setIdDistribuidor(distribuidor.getIdDistribuidor());

        try {
            if (videojuego == null) {
                videojuegoService.crear(v);
                AlertUtil.info("Exito", "Videojuego creado exitosamente.");
            } else {
                videojuegoService.actualizar(v);
                AlertUtil.info("Exito", "Videojuego actualizado exitosamente.");
            }
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (IllegalArgumentException ex) {
            AlertUtil.advertencia("Validacion", ex.getMessage());
        } catch (Exception ex) {
            AlertUtil.error("Error", "No se pudo guardar:", ex);
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    private void cargarDistribuidores() {
        try {
            cboDistribuidor.setItems(FXCollections.observableArrayList(distribuidorService.listar()));
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudieron cargar los distribuidores:", e);
        }
    }

    private boolean validarFormulario() {
        ocultarErrores();
        boolean ok = true;

        if (txtConsola.getText() == null || txtConsola.getText().trim().isEmpty()) {
            lblConsolaError.setText("La Consola es obligatoria.");
            lblConsolaError.setVisible(true);
            if (ok) txtConsola.requestFocus();
            ok = false;
        }
        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            lblNombreError.setText("El Nombre es obligatorio.");
            lblNombreError.setVisible(true);
            if (ok) txtNombre.requestFocus();
            ok = false;
        }
        if (!esEnteroPositivo(txtIdDesarrollador.getText())) {
            lblIdDesarrolladorError.setText("Ingrese un ID mayor que cero.");
            lblIdDesarrolladorError.setVisible(true);
            if (ok) txtIdDesarrollador.requestFocus();
            ok = false;
        }
        if (cboDistribuidor.getValue() == null) {
            lblDistribuidorError.setText("Seleccione un distribuidor.");
            lblDistribuidorError.setVisible(true);
            if (ok) cboDistribuidor.requestFocus();
            ok = false;
        }

        return ok;
    }

    private void seleccionarDistribuidor(int idDistribuidor) {
        cboDistribuidor.getItems().stream()
            .filter(d -> d.getIdDistribuidor() == idDistribuidor)
            .findFirst()
            .ifPresent(cboDistribuidor::setValue);
    }

    private boolean esEnteroPositivo(String value) {
        try {
            return Integer.parseInt(value.trim()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void ocultarErrores() {
        lblNombreError.setVisible(false);
        lblConsolaError.setVisible(false);
        lblIdDesarrolladorError.setVisible(false);
        lblDistribuidorError.setVisible(false);
    }

    private void cerrar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    private void limitarLongitud(TextInputControl control, int max) {
        control.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > max) {
                control.setText(oldVal);
            }
        });
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }
}
