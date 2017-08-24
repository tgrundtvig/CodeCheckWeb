/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

/**
 *
 * @author Tobias Grundtvig
 */
public class Config
{
    // JDBC driver name and database URL
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/CBA_Code_Check";
    //static final String DB_URL = "jdbc:mysql://10.0.0.42:3306/CBA_Code_Check";

    
    //  Database credentials
    public static final String USER = "Edutor";
    public static final String PASS = "qwerty123";
    
    //public static final String UPLOAD_FOLDER = "/home/pi/CodeCheck/HandIn/";
    public static final String UPLOAD_FOLDER = "C:\\tmp\\HandIn\\"; 
}
