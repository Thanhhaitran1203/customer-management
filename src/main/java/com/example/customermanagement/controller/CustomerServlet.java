package com.example.customermanagement.controller;

import com.example.customermanagement.model.Customer;
import com.example.customermanagement.service.CustomerService;
import com.example.customermanagement.service.CustomerServiceImpl;
import com.example.customermanagement.service.CustomerServiceJDBC;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {
//    private final CustomerService customerService = new CustomerServiceImpl();
    private final CustomerService customerService = new CustomerServiceJDBC();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action){
            case "create":
                createCustomer(req,resp);
                break;
            case "edit":
                updateCustomer(req,resp);
                break;
            case "delete":
                deleteCustomer(req,resp);
                break;
            default:break;
        }
    }

    private void deleteCustomer(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher dispatcher;
        if (customer == null){
            dispatcher = req.getRequestDispatcher("error-404.jsp");
        }else {
            this.customerService.remote(id);
            try {
                resp.sendRedirect("/customers");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateCustomer(HttpServletRequest req, HttpServletResponse resp) {
    int id = Integer.parseInt(req.getParameter("id"));
    String name = req.getParameter("name");
    String email = req.getParameter("email");
    String address = req.getParameter("address");
    Customer customer = this.customerService.findById(id);
    RequestDispatcher dispatcher;
    if (customer == null){
        dispatcher = req.getRequestDispatcher("error-404.jsp");
    }else {
        customer.setName(name);
        customer.setEmail(email);
        customer.setAddress(address);
        this.customerService.update(id,customer);
        req.setAttribute("customer",customer);
        req.setAttribute("message","Customer information was updated");
        dispatcher = req.getRequestDispatcher("customer/edit.jsp");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    }

    private void createCustomer(HttpServletRequest req, HttpServletResponse resp) {
    String name = req.getParameter("name");
    String email = req.getParameter("email");
    String address = req.getParameter("address");
    Customer customer = new Customer(name,email,address);
    this.customerService.save(customer);
    RequestDispatcher dispatcher  =req.getRequestDispatcher("customer/create.jsp");
    req.setAttribute("message","new customer was");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                showCreateForm(req, resp);
                break;
            case "edit":
                showEditForm(req, resp);
                break;
            case "delete":
                showDeleteForm(req, resp);
                break;
            case "view":
                viewCustomer(req, resp);
                break;
            default:
                listCustomers(req, resp);
                break;
        }
    }

    private void listCustomers(HttpServletRequest req, HttpServletResponse resp) {
        List<Customer> customers = this.customerService.findAll();
        req.setAttribute("customers", customers);

        RequestDispatcher dispatcher = req.getRequestDispatcher("customer/list.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void viewCustomer(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher dispatcher;
        if (customer == null) {
            dispatcher = req.getRequestDispatcher("error-404.jsp");
        } else {
            req.setAttribute("customer", customer);
            dispatcher = req.getRequestDispatcher("customer/view.jsp");
        }
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showDeleteForm(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher dispatcher;
        if (customer == null) {
            dispatcher = req.getRequestDispatcher("error-404.jsp");
        } else {
            req.setAttribute("customer", customer);
            dispatcher = req.getRequestDispatcher("customer/delete.jsp");
        }
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        Customer customer = this.customerService.findById(id);
        RequestDispatcher dispatcher;
        if (customer == null) {
            dispatcher = req.getRequestDispatcher("error-404.jsp");
        } else {
            req.setAttribute("customer", customer);
            dispatcher = req.getRequestDispatcher("customer/edit.jsp");
        }
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) {
        RequestDispatcher dispatcher = req.getRequestDispatcher("customer/create.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
