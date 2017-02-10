/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Tobias Grundtvig
 */
public class WebUtil
{
    public static void showAccessDenied(HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>AccessDenied</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>You do not have permission to view this page!</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    public static void showError(String error, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Error</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Error: " + error + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
