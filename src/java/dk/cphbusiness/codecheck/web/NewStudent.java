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
public class NewStudent extends HttpServlet
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
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String mobile = request.getParameter("mobile");
        String email = request.getParameter("email");
        String passwd = request.getParameter("passwd");
        if(firstName == null || lastName == null || mobile == null || email == null || passwd == null)
        {
            printForm(response, "All fields must be filled out:");
            return;
        }

        User user = DBUtil.createStudent(firstName, lastName, mobile, email, passwd);

        if(user == null)
        {
            printForm(response, "Student could not be created!");
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
        try(PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create student</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Register student</h1>");
            if(msg != null)
            {
                out.println("<h4>" + msg + "</h4>");
            }
            out.println("<form method=\"post\">");
            out.println("First name:<br>");
            out.println("<input type=\"text\" name=\"firstName\"><br>");
            out.println("Last name:<br>");
            out.println("<input type=\"text\" name=\"lastName\"><br>");
            out.println("Mobile:<br>");
            out.println("<input type=\"text\" name=\"mobile\"><br>");
            out.println("Email:<br>");
            out.println("<input type=\"text\" name=\"email\"><br>");
            out.println("Choose a password:<br>");
            out.println("<input type=\"password\" name=\"passwd\">");
            out.println("<input type=\"submit\" name=\"submit\" value=\"GO\"/>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
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
