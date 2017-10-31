/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.db.DBUtil;
import dk.cphbusiness.codecheck.web.data.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tobias Grundtvig
 */
public class Login extends HttpServlet
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        //Get parameters
        HttpSession session = request.getSession();
        session.setAttribute("USER", null);
        String email = request.getParameter("email");
        String passwd = request.getParameter("passwd");
        if(email == null || passwd == null)
        {
            printForm(response, "Please enter your email and password:");
            return;
        }

        User user = DBUtil.getUser(email, passwd);

        if(user == null)
        {
            printForm(response, "Email and password did not match!");
            return;
        }

        session.setAttribute("USER", user);
        String goNext = (String) session.getAttribute("GoNext");
        if(goNext == null)
        {
            goNext = "/CodeCheckWeb/ShowTasks";
        }
        response.sendRedirect(goNext);
    }

    private void printForm(HttpServletResponse response, String msg) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        
        StringBuilder body = new StringBuilder();
        body.append("<h2>Login</h2>");
        body.append("<form method=\"post\">");
        body.append("<div class=\"form-group\">");
        body.append("<label for=\"email\">Email:</label>");
        body.append("<input type=\"email\" class=\"form-control\" id=\"email\" name=\"email\" placeholder=\"Enter email\">");
        body.append("</div>");
        body.append("<div class=\"form-group\">");
        body.append("<label for=\"pwd\">Password:</label>");
        body.append("<input type=\"password\" class=\"form-control\" id=\"pwd\" name=\"passwd\" placeholder=\"Enter password\">");
        body.append("</div>");
        body.append("<button type=\"submit\" class=\"btn btn-default\">Submit</button>");
        body.append("</form>");

        String page = BootstrapUtil.createPage("Login", body.toString());
        
        try(PrintWriter out = response.getWriter())
        {
            out.println(page);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
