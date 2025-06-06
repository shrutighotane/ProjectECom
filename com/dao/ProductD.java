package com.dao;

import com.connection.DBConnection;
import com.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductD {

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, price, description, quantity) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getDescription());
            ps.setInt(4, product.getQuantity());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add Product Error: " + e.getMessage());
            return false;
        }
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setDescription(rs.getString("description"));
                p.setQuantity(rs.getInt("quantity"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Get Products Error: " + e.getMessage());
        }
        return list;
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete Product Error: " + e.getMessage());
            return false;
        }
    }
}

