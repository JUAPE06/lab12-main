package com.ucv.lab12.controller;

import com.ucv.lab12.model.Distribuidor;
import com.ucv.lab12.service.IDistribuidorService;
import com.ucv.lab12.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DistribuidorFormController implements Initializable {

    @FXML private Label    lblTitulo;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtRuc;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextArea  txtDireccion;
    @FXML private CheckBox  chkActivo;
    @FXML private Label     lblRazonSocialError;
    @FXML private Label     lblEmailError;
    @FXML private Button    btnGuardar;
    @FXML private Button    btnCancelar;

    private final IDistribuidorService service;
    private Distribuidor distribuidor;
    private Runnable onGuardar;

    public DistribuidorFormController(IDistribuidorService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chkActivo.setSelected(true);
        lblRazonSocialError.setVisible(false);
        lblEmailError.setVisible(false);

        // Límites de caracteres en tiempo real
        limitarLongitud(txtRazonSocial, 150);
        limitarLongitud(txtRuc,         20);
        limitarLongitud(txtTelefono,    20);
        limitarLongitud(txtEmail,       100);
        limitarLongitud(txtDireccion,   200);

        // Limpiar error de razón social al escribir
        txtRazonSocial.textProperty().addListener((o, v1, v2) ->
            lblRazonSocialError.setVisible(false));
        txtEmail.textProperty().addListener((o, v1, v2) ->
            lblEmailError.setVisible(false));
    }

    // -------------------------------------------------------------------------
    // API pública para el controller padre
    // -------------------------------------------------------------------------

    public void setDistribuidor(Distribuidor d) {
        this.distribuidor = d;
        if (d != null) {
            lblTitulo.setText("Editar Distribuidor");
            txtRazonSocial.setText(d.getRazonSocial());
            txtRuc.setText(d.getRuc()       != null ? d.getRuc()       : "");
            txtTelefono.setText(d.getTelefono() != null ? d.getTelefono() : "");
            txtEmail.setText(d.getEmail()    != null ? d.getEmail()    : "");
            txtDireccion.setText(d.getDireccion() != null ? d.getDireccion() : "");
            chkActivo.setSelected(d.isActivo());
        } else {
            lblTitulo.setText("Nuevo Distribuidor");
        }
    }

    public void setOnGuardar(Runnable callback) {
        this.onGuardar = callback;
    }

    // -------------------------------------------------------------------------
    // Handlers FXML
    // -------------------------------------------------------------------------

    @FXML
    private void onGuardar() {
        if (!validarFormulario()) return;

        Distribuidor d = distribuidor != null ? distribuidor : new Distribuidor();
        d.setRazonSocial(txtRazonSocial.getText().trim());
        d.setRuc(txtRuc.getText().trim());
        d.setTelefono(txtTelefono.getText().trim());
        d.setEmail(txtEmail.getText().trim());
        d.setDireccion(txtDireccion.getText().trim());
        d.setActivo(chkActivo.isSelected());

        try {
            if (distribuidor == null) {
                service.crear(d);
                AlertUtil.info("Éxito", "Distribuidor creado exitosamente.");
            } else {
                service.actualizar(d);
                AlertUtil.info("Éxito", "Distribuidor actualizado exitosamente.");
            }
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (IllegalArgumentException ex) {
            AlertUtil.advertencia("Validación", ex.getMessage());
        } catch (Exception ex) {
            AlertUtil.error("Error", "No se pudo guardar:\n" + ex.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    // -------------------------------------------------------------------------
    // Helpers privados
    // -------------------------------------------------------------------------

    private boolean validarFormulario() {
        boolean ok = true;

        // Razón Social obligatoria
        if (txtRazonSocial.getText() == null || txtRazonSocial.getText().trim().isEmpty()) {
            lblRazonSocialError.setText("La Razón Social es obligatoria.");
            lblRazonSocialError.setVisible(true);
            txtRazonSocial.requestFocus();
            ok = false;
        }

        // Email: formato si no está vacío
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w.+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            lblEmailError.setText("Formato de email inválido.");
            lblEmailError.setVisible(true);
            if (ok) txtEmail.requestFocus();
            ok = false;
        }

        return ok;
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
}
