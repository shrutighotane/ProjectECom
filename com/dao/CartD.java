package com.dao;
import com.connection.DBConnection;
import com.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartD {

    public boolean addToCart(int userId, int productId) {
        String sql = "INSERT INTO cart (user_id, product_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add to Cart Error: " + e.getMessage());
            return false;
        }
    }

    public List<Product> viewCart(int userId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.price FROM products p " +
                     "JOIN cart c ON p.id = c.product_id WHERE c.user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println("View Cart Error: " + e.getMessage());
        }
        return list;
    }

    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Clear Cart Error: " + e.getMessage());
            return false;
        }
    }
}
