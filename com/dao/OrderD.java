package com.dao;

import com.connection.DBConnection;
import com.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderD {
	 public boolean placeOrder(int userId, int productId) {
	        String sql = "INSERT INTO orders (user_id, product_id) VALUES (?, ?)";
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, userId);
	            ps.setInt(2, productId);
	            return ps.executeUpdate() > 0;
	        } catch (SQLException e) {
	            System.out.println("Place Order Error: " + e.getMessage());
	            return false;
	        }
	    }

	    public List<Product> viewOrderHistory(int userId) {
	        List<Product> list = new ArrayList<>();
	        String sql = "SELECT p.id, p.name, p.price FROM products p " +
	                     "JOIN orders o ON p.id = o.product_id WHERE o.user_id=?";
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
	            System.out.println("Order History Error: " + e.getMessage());
	        }
	        return list;
	    }
}
