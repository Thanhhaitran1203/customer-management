package com.example.customermanagement.service;

import com.example.customermanagement.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerServiceJDBC implements CustomerService {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/customer";
    private final String jdbcUserName = "root";
    private final String jdbcPassword = "1234";
    private final String SELECT_ALL_USERS = "select * from customers;";
    private final String INSERT_CUSTOMER_SQL = "insert into customers(name,email,address) value(?,?,?);";
    private final String UPDATE_CUSTOMER_SQL = "update customers set name=?,email=?,address=? where id=?";
    private final String FIND_CUSTOMER_BY_ID = "select * from customers where id = ?;";
    private final String REMOVE_CUSTOMER_BY_ID = "delete from customers where id = ?;";
    Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL,jdbcUserName,jdbcPassword);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    @Override
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                list.add(new Customer(id,name,email,address));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void save(Customer customer) {
    Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_SQL);
            preparedStatement.setString(1,customer.getName());
            preparedStatement.setString(2,customer.getEmail());
            preparedStatement.setString(3,customer.getAddress());
            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Customer findById(int id) {
        Connection connection = getConnection();
        Customer customer = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMER_BY_ID);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                customer = new Customer(name,email,address);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public void update(int id, Customer customer) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_SQL);
            preparedStatement.setString(1,customer.getName());
            preparedStatement.setString(2,customer.getEmail());
            preparedStatement.setString(3,customer.getAddress());
            preparedStatement.setInt(4,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void remote(int id) {
        Connection connection =getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_CUSTOMER_BY_ID);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
