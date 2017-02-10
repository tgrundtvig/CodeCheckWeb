/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.db.DBUtil;
import dk.cphbusiness.codecheck.web.data.Task;
import dk.cphbusiness.codecheck.web.data.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tobias Grundtvig
 */
@WebServlet(name = "ShowTasks", urlPatterns =
          {
              "/ShowTasks"
})
public class ShowTasks extends HttpServlet
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
            session.setAttribute("GoNext", "/CodeCheckWeb/ShowTasks");
            response.sendRedirect("/CodeCheckWeb/Login");
            return;
        }
        List<Task> tasks = DBUtil.getUserTasks(user.getId());
        printAssignments(tasks, user, response);
    }

    private void printAssignments(List<Task> tasks, User user, HttpServletResponse response) throws
      IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Assignments</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Assignments for " + user.getFullName() + "</h1>");
            out.println("<table border=1>");
            out.println("<tr><th>Name</th><th>Begin</th><th>End</th><th>Attempts</th><th>Status</th></tr>");
            for(Task t : tasks)
            {
                out.print("<tr><td><a href=\"/CodeCheckWeb/ShowTask?TaskID=" + t.getID() + "\">");
                out.print(t.getName());
                out.print("</a></td><td>");
                out.print(t.getBegin());
                out.print("</td><td>");
                out.print(t.getEnd());
                out.print("</td><td>");
                out.print(t.getAttempts());
                out.print(" / ");
                if(t.getMaxHandins() < 1)
                {
                    out.print(" INF");
                }
                else
                {
                    out.print(t.getMaxHandins());
                }
                out.print("</td><td>");
                out.print(t.getStatus());
                out.println("</td></tr>");
            }
            out.println("</table>");
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
