package com.ucv.lab12.repository;

import com.ucv.lab12.config.DatabaseConfig;
import com.ucv.lab12.model.Distribuidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DistribuidorRepository implements IDistribuidorRepository, AutoCloseable {

    private final DatabaseConfig dbConfig;

    public DistribuidorRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<Distribuidor> findAll() {
        return findByFilters("", "");
    }

    @Override
    public List<Distribuidor> findByFilters(String razonSocial, String ruc) {
        String sql = """
                SELECT IdDistribuidor, RazonSocial, RUC, Telefono, Email, Direccion, Activo
                FROM Distribuidor
                WHERE RazonSocial LIKE ?
                  AND RUC          LIKE ?
                ORDER BY RazonSocial
                """;
        List<Distribuidor> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + (razonSocial == null ? "" : razonSocial.trim()) + "%");
            ps.setString(2, "%" + (ruc         == null ? "" : ruc.trim())         + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar distribuidores", e);
        }
        return list;
    }

    @Override
    public void save(Distribuidor d) {
        String sql = """
                INSERT INTO Distribuidor (RazonSocial, RUC, Telefono, Email, Direccion, Activo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getRazonSocial());
            ps.setString(2, nvl(d.getRuc()));
            ps.setString(3, nvl(d.getTelefono()));
            ps.setString(4, nvl(d.getEmail()));
            ps.setString(5, nvl(d.getDireccion()));
            ps.setBoolean(6, d.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar distribuidor", e);
        }
    }

    @Override
    public void update(Distribuidor d) {
        String sql = """
                UPDATE Distribuidor
                SET RazonSocial = ?,
                    RUC         = ?,
                    Telefono    = ?,
                    Email       = ?,
                    Direccion   = ?,
                    Activo      = ?
                WHERE IdDistribuidor = ?
                """;
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getRazonSocial());
            ps.setString(2, nvl(d.getRuc()));
            ps.setString(3, nvl(d.getTelefono()));
            ps.setString(4, nvl(d.getEmail()));
            ps.setString(5, nvl(d.getDireccion()));
            ps.setBoolean(6, d.isActivo());
            ps.setInt(7, d.getIdDistribuidor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar distribuidor", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Distribuidor WHERE IdDistribuidor = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar distribuidor", e);
        }
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sb = new StringBuilder(
            "DELETE FROM Distribuidor WHERE IdDistribuidor IN (");
        for (int i = 0; i < ids.size(); i++) {
            sb.append(i == 0 ? "?" : ",?");
        }
        sb.append(")");

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar distribuidores en bloque", e);
        }
    }

    // --- helpers ---

    private Distribuidor mapRow(ResultSet rs) throws SQLException {
        return new Distribuidor(
            rs.getInt("IdDistribuidor"),
            rs.getString("RazonSocial"),
            rs.getString("RUC"),
            rs.getString("Telefono"),
            rs.getString("Email"),
            rs.getString("Direccion"),
            rs.getBoolean("Activo")
        );
    }

    /** Convierte null a cadena vacía para evitar NPE en PreparedStatement. */
    private String nvl(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public void close() {
        dbConfig.close();
    }
}
