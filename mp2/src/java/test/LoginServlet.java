/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package test;

import java.sql.*;
import java.io.IOException;
import java.util.Random;
import test.SecurityHandling;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jonathanseanestaya
 */
public class LoginServlet extends HttpServlet {
    Connection conn;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
 
        
        String driver = config.getInitParameter("jdbcDriver");
        String dbUsername = config.getInitParameter("username");
        String dbPassword = config.getInitParameter("password");
        StringBuilder dbUrl = new StringBuilder(config.getInitParameter("dbDriver"))
                .append("://")
                .append(config.getInitParameter("host"))
                .append(":")
                .append(config.getInitParameter("port"))
                .append("/")
                .append(config.getInitParameter("dbName"));
        
        try {
            Class.forName(driver);
            System.out.println("Driver " + driver +  " is loaded");
            
            conn = DriverManager.getConnection(dbUrl.toString(), dbUsername, dbPassword);
            System.out.println("Connection established");
        }
        catch(ClassNotFoundException dbe) {
            System.err.println("Driver or DatabaseURL not found!");
        }
        catch(SQLException dbe) {
            System.err.println("Driver or DatabaseURL not found!");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("uname");
        String password = request.getParameter("upass");
        HttpSession session = request.getSession();
        
        Cookie cookieSession = new Cookie("JSESSIONID", session.getId());
        cookieSession.setMaxAge(60 * 5);
        response.addCookie(cookieSession);
        
        try {
            String certRedirect = certify(session, username, password);
            
            if(certRedirect.equals("CaptchaServlet")) { //og: success.jsp
                session.setAttribute("auth", "auth");
                session.setAttribute("username", username);
                session.setAttribute("CaptchaString","");
          
            }
            
            response.sendRedirect(certRedirect);
        }
        catch(SQLException e ) {
            System.err.println(e);
        }
        catch(NullValueException e) {
            System.err.println(e);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
  
    public String certify(HttpSession session, String username, String password) 
            throws SQLException, NullValueException {
        
        ServletConfig config=getServletConfig(); 
        boolean correctUser = false;
        boolean correctPass = false;
        PreparedStatement getUserStatement = conn.prepareStatement("SELECT USERNAME FROM USER_INFO");
        ResultSet getUserResult = getUserStatement.executeQuery();
        PreparedStatement getPassStatement = conn.prepareStatement("SELECT PASSWORD FROM USER_INFO WHERE USERNAME = ?");
        getPassStatement.setString(1, username);
        ResultSet getPassResult = getPassStatement.executeQuery();
        PreparedStatement getRoleStatement = conn.prepareStatement("SELECT ROLE FROM USER_INFO WHERE USERNAME = ?");
        getRoleStatement.setString(1, username);
        ResultSet getRoleResult = getRoleStatement.executeQuery();
        
        
        
        if(username.isEmpty() && password.isEmpty()) {
            throw new NullValueException();
        }
        
        while(getUserResult.next()) {
            if(getUserResult.getString("USERNAME").equals(username)) {
                correctUser = true;
                break;
            }
        }
        
        while(getPassResult.next()) {
          
            String p = getPassResult.getString("PASSWORD");
            String s = SecurityHandling.decrypt(p,config.getInitParameter("Cipher"),config.getInitParameter("Key"));
            if(s.equals(password)) {
                System.out.println("Encrypted: " + p);
                System.out.println("Decrypted: "+s);
                correctPass = true;
                break;
            }
        }
        
        if(correctUser && correctPass) {
            getRoleResult.next();
            session.setAttribute("auth", "auth");
            session.setAttribute("username", username);
            session.setAttribute("role", getRoleResult.getString("ROLE"));
            getRoleResult.close();
            return "CaptchaServlet"; //note og: success.jsp
        }
        else if(correctUser == false && password.isEmpty())
            return "error_1.jsp";
        else if(correctUser == true && correctPass == false)
            return "error_2.jsp";
        else
            return "error_3.jsp";
    }
}