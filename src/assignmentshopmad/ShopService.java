/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentshopmad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author rajinthan
 */
public class ShopService {
    
    public static Connection con;
    
    ShopService(){
        try{
            Class.forName("com.mysql.jdbc.Driver");  
            con = DriverManager.getConnection(DbCredentital.CONNECTION,DbCredentital.USERNAME,DbCredentital.PASSOWRD); 
        }catch(java.lang.ClassNotFoundException e){ 
            showError("Something went wrong");
            System.out.println("Class Not found Err: " + e.getMessage());
        }catch(Exception e){ 
            showError("Something went wrong");
            System.out.println("Common Err: " + e.getMessage());
        }
    }
    
    final void showError(String ex) {
        JOptionPane optionPane = new JOptionPane(ex, JOptionPane.ERROR_MESSAGE);    
        JDialog dialog = optionPane.createDialog("Failure");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    final void sucessMsg(String msg) {
        JOptionPane optionPane = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);    
        JDialog dialog = optionPane.createDialog("Success");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
    
    public boolean saveData(String code, String name, double price){
        try{
            if(code.length() > 5){
                showError("Code should not more than 5 characters");
                return false;
            }else if(name.length() > 20){
                showError("Name should not more than 20 characters");
                return false;
            }else if(price <= 0){
                showError("Price should be more than zero");
                return false;
            }
            Statement statement = con.createStatement();
            String sql = "INSERT INTO items VALUES ('"+ code +"','"+ name + "','" + price +"')";
            statement.executeUpdate(sql);
            return true;
        }catch(SQLException ex){ 
            showError(ex.getMessage());
            System.out.println("Query Err: " + ex.getMessage());
            return false;
        }catch(Exception e){ 
            showError(e.getMessage());
            System.out.println("Common Err: " + e.getMessage());
            return false;
        }
        
    }

    ResultSet getData(String search, String maxPrice, String minPrice) {
        ResultSet data = null;
        try{
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM items WHERE item_price >= 0 ";
            if(maxPrice != null && !maxPrice.equalsIgnoreCase("")){
                sql += " AND item_price <= " + Double.parseDouble(maxPrice);
            }
            if(minPrice != null && !minPrice.equalsIgnoreCase("")){
                sql += " AND item_price >= " + Double.parseDouble(minPrice);
            }
            if(search != null && !search.equalsIgnoreCase("")){
                sql += " AND (item_code LIKE '%"+ search +"%' "+ "OR item_name LIKE '%"+ search +"%' )" ;
            }
            data = statement.executeQuery(sql);
            return data;
        }catch(SQLException ex){ 
            showError(ex.getMessage());
            System.out.println("Query Err: " + ex.getMessage());
            return data;
        }catch(Exception e){ 
            showError(e.getMessage());
            System.out.println("Common Err: " + e.getMessage());
            return data;
        }
    }

    ResultSet getSingleData(String search) {
        ResultSet data = null;
        try{
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM items WHERE (item_code LIKE '%"+ search +"%' "+ "OR item_name LIKE '%"+ search +"%' ) LIMIT 1";
            data = statement.executeQuery(sql);
            return data;
        }catch(SQLException ex){ 
            showError(ex.getMessage());
            System.out.println("Query Err: " + ex.getMessage());
            return data;
        }catch(Exception e){ 
            showError(e.getMessage());
            System.out.println("Common Err: " + e.getMessage());
            return data;
        }
    }

    void saveData(String code, String price) {
        try{
            Statement statement = con.createStatement();
            String sql = "UPDATE items SET item_price = "+Double.parseDouble(price)+" WHERE item_code = " + code;
            statement.executeUpdate(sql);
            sucessMsg("Sucessfully Update");
        }catch(SQLException ex){ 
            showError(ex.getMessage());
            System.out.println("Query Err: " + ex.getMessage());
        }catch(Exception e){ 
            showError(e.getMessage());
            System.out.println("Common Err: " + e.getMessage());
        }
    }
}
