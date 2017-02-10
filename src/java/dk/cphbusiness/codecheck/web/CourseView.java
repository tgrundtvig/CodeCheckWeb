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
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tobias Grundtvig
 */
public class CourseView extends HttpServlet
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
        String strCourseID = request.getParameter("CourseID");
        if(strCourseID == null)
        {
            WebUtil.showError("No CourseID", response);
            return;
        }
        int courseID = Integer.parseInt(strCourseID);
        Course course = DBUtil.getCourse(courseID);
        if(course == null)
        {
            WebUtil.showError("Course width id: " + courseID + " does not exist.", response);
            return;
        }
        User user = (User) session.getAttribute("USER");
        if(user == null)
        {
            session.setAttribute("GoNext", "/CodeCheckWeb/CourseView?CourseID=" + courseID);
            response.sendRedirect("/CodeCheckWeb/Login");
            return;
        }
        if(!user.hasRole("TEACHER"))
        {
            WebUtil.showAccessDenied(response);
            return;
        }
        List<User> participants = DBUtil.getUsersOnCourse(courseID);
        if("TRUE".equals(request.getParameter("CreateTask")))
        {
            int assignmentID = Integer.parseInt(request.getParameter("AssignmentID"));
            int handins = Integer.parseInt(request.getParameter("Handins"));
            String strStartDate = request.getParameter("StartDate");
            String strStartTime = request.getParameter("StartTime");
            String strEndDate = request.getParameter("EndDate");
            String strEndTime = request.getParameter("EndTime");
            
            Date start = getDate(strStartDate, strStartTime);
            Date end = getDate(strEndDate, strEndTime);
            for(User u : participants)
            {
                try
                {
                    DBUtil.createTask(u.getId(), assignmentID, start, end, handins);
                }
                catch(SQLException ex)
                {
                    Logger.getLogger(CourseView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        List<Assignment> assignments = DBUtil.getAllAssignments();
        showCourse(response, course, participants, assignments);
    }
    
    private void showCourse(HttpServletResponse response, Course course, List<User> participants, List<Assignment> assignments) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + course.getName() + "</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + course.getName() + "</h1>");
            out.println("<table border=\"1\">");
            out.println("<tr><th>Name</th></tr>");
            for(User user : participants)
            {
                out.print("<tr><td>");
                out.print(user.getFullName());
                out.println("</td></tr>");
            }
            out.println("</table>");
            out.println("<h2>Assign task:</h2>");
            out.println("<form method=\"post\">");
            out.println("Assignment: ");
            out.println("<select name=\"AssignmentID\">");
            for(Assignment assignment : assignments)
            {
                out.println("<option value=\"" + assignment.getId() + "\">" + assignment.getName() + "</option>");
            }
            out.println("</select><br>");
            out.print("Start date: ");
            out.print("<input type=\"date\" name=\"StartDate\">");
            out.print(" Start time: ");
            out.println("<input type=\"time\" name=\"StartTime\"><br>");
            
            out.println("End date: ");
            out.println("<input type=\"date\" name=\"EndDate\">");
            out.print(" End time: ");
            out.println("<input type=\"time\" name=\"EndTime\"><br>");
            
            out.println("Number of attempts: ");
            out.println("<input type=\"number\" name=\"Handins\" min=\"1\" max=\"100\" /><br>");
            
            out.println("<input type=\"hidden\" name=\"CreateTask\" value=\"TRUE\"/>");
            out.println("<input type=\"submit\" name=\"submit\" value=\"GO\"/>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private Date getDate(String strDate, String strTime)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date res = null;
        try
        {
            res = df.parse(strDate + " " + strTime);
        }
        catch(ParseException ex)
        {
            Logger.getLogger(CourseView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
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
