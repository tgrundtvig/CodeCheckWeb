/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.db.DBUtil;
import dk.cphbusiness.codecheck.web.data.Assignment;
import dk.cphbusiness.codecheck.web.data.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tobias Grundtvig
 */
public class AssignmentList extends HttpServlet
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
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("USER");
        if(user == null)
        {
            session.setAttribute("GoNext", "/CodeCheckWeb/AssignmentList");
            response.sendRedirect("/CodeCheckWeb/Login");
            return;
        }
        if(!user.hasRole("TEACHER"))
        {
            WebUtil.showAccessDenied(response);
            return;
        }
        if("TRUE".equals(request.getParameter("CreateNew")))
        {
            String name = request.getParameter("name");
            String desc = request.getParameter("description");
            Assignment assignment = DBUtil.createAssignment(name, desc);
        }
        String deleteID = request.getParameter("DeleteID");
        if(deleteID != null)
        {
            int id = Integer.parseInt(deleteID);
            DBUtil.deleteAssignment(id);
        }
        
        Collection<Assignment> res = DBUtil.getAllAssignments();
        showList(response, res);
    }
    
    private void showList(HttpServletResponse response, Collection<Assignment> list) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AssignmentList</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>All assignments:</h1>");
            out.println("<table border=\"1\">");
            out.println("<tr><th>Name</th><th>Description</th><th>Edit</th><th>Delete</th></tr>");
            for(Assignment a : list)
            {
                out.print("<tr><td>");
                out.print(a.getName());
                out.print("</td><td>");
                out.print(cutString(a.getDescription(),80));
                out.print("</td><td><a href=\"AssignmentEdit?ID=" + a.getId() + "\">Edit</a></td>");
                out.print("</td><td><a href=\"AssignmentList?DeleteID=" + a.getId() + "\">Delete</a></td>");
                out.println("</tr>");
                
            }
            out.println("</table>");
            out.println("<h2>Create new assignment:</h2>");
            out.println("<form method=\"post\">");
            out.println("Name:<br>");
            out.println("<input type=\"text\" name=\"name\"><br>");
            out.println("Description:<br>");
            out.println("<textarea name=\"description\"></textarea>");
            out.println("<input type=\"hidden\" name=\"CreateNew\" value=\"TRUE\"/>");
            out.println("<input type=\"submit\" name=\"submit\" value=\"GO\"/>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private String cutString(String s, int maxLength)
    {
        if(s.length() <= maxLength)
        {
            return s;
        }
        return s.substring(0, maxLength-4) + "...";
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
