/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.data.Course;
import dk.cphbusiness.codecheck.web.data.Task;
import dk.cphbusiness.codecheck.web.data.User;
import dk.cphbusiness.codecheck.web.db.DBUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tobias Grundtvig
 */
public class CourseAssign extends HttpServlet
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
            session.setAttribute("GoNext", "/CodeCheckWeb/CourseAssign");
            response.sendRedirect("/CodeCheckWeb/Login");
            return;
        }
        String strAssignID = request.getParameter("AssignID");
        if(strAssignID != null)
        {
            int assignID = Integer.parseInt(strAssignID);
            DBUtil.assignUserToCourse(user.getId(), assignID);
        }
        String strUnassignID = request.getParameter("UnassignID");
        if(strUnassignID != null)
        {
            int unassignID = Integer.parseInt(strUnassignID);
            DBUtil.unassignUserFromCourse(user.getId(), unassignID);
        }
        List<Course> assignedCourses = DBUtil.getAssignedCourses(user.getId());
        List<Course> unassignedCourses = DBUtil.getUnassignedCourses(user.getId());
        printCourses(assignedCourses, unassignedCourses, user, response);
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

    private void printCourses(List<Course> assignedCourses, List<Course> unassignedCourses, User user,
                              HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Course assign</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + user.getFullName() + " is assigned to the following courses:</h1>");
            out.println("<table border=1>");
            out.println("<tr><th>Name</th><th>Unassign</th></tr>");
            for(Course c : assignedCourses)
            {
                out.print("<tr><td>");
                out.print(c.getName());
                out.print("</td><td><a href=\"/CodeCheckWeb/CourseAssign?UnassignID=" + c.getId() + "\">Unassign</a></td></tr>");
            }
            out.println("</table>");
            
            out.println("<h1>Assign " + user.getFullName() + " to a course:</h1>");
            out.println("<table border=1>");
            out.println("<tr><th>Name</th><th>Assign</th></tr>");
            for(Course c : unassignedCourses)
            {
                out.print("<tr><td>");
                out.print(c.getName());
                out.print("</td><td><a href=\"/CodeCheckWeb/CourseAssign?AssignID=" + c.getId() + "\">Assign</a></td></tr>");
            }
            out.println("</table>");
            
            out.println("</body>");
            out.println("</html>");
        }
    }

}
