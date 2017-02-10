/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.data.Assignment;
import dk.cphbusiness.codecheck.web.data.Course;
import dk.cphbusiness.codecheck.web.data.User;
import dk.cphbusiness.codecheck.web.db.DBUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
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
public class CourseList extends HttpServlet
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
            session.setAttribute("GoNext", "/CodeCheckWeb/CourseList");
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
            DBUtil.createCourse(name);
        }
        String deleteID = request.getParameter("DeleteID");
        if(deleteID != null)
        {
            int id = Integer.parseInt(deleteID);
            DBUtil.deleteCourse(id);
        }
        
        List<Course> res = DBUtil.getCourses();
        showList(response, res);
    }
    
    private void showList(HttpServletResponse response, List<Course> list) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Course list</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>All courses:</h1>");
            out.println("<table border=\"1\">");
            out.println("<tr><th>Name</th><th>Delete</th></tr>");
            for(Course c : list)
            {
                out.print("<tr><td>");
                out.print("<a href=\"CourseView?CourseID=" + c.getId() + "\">");
                out.print(c.getName());
                out.print("</a></td><td><a href=\"CourseList?DeleteID=" + c.getId() + "\">Delete</a></td>");
                out.println("</tr>");
                
            }
            out.println("</table>");
            out.println("<h2>Create new course:</h2>");
            out.println("<form method=\"post\">");
            out.println("Name:<br>");
            out.println("<input type=\"text\" name=\"name\"><br>");
            out.println("<input type=\"hidden\" name=\"CreateNew\" value=\"TRUE\"/>");
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
