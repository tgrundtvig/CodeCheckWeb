/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.data.Assignment;
import dk.cphbusiness.codecheck.web.data.AssignmentTest;
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
public class AssignmentEdit extends HttpServlet
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
        String strID = request.getParameter("ID");
        if(strID == null)
        {
            WebUtil.showError("ID parameter is missing!", response);
            return;
        }
        int id = Integer.parseInt(strID);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("USER");
        if(user == null)
        {
            session.setAttribute("GoNext", "/CodeCheckWeb/AssignmentEdit?ID=" + id);
            response.sendRedirect("/CodeCheckWeb/Login");
            return;
        }
        if(!user.hasRole("TEACHER"))
        {
            WebUtil.showAccessDenied(response);
            return;
        }
        if("TRUE".equals(request.getParameter("CreateNewTest")))
        {
            String input = request.getParameter("input");
            String expected = request.getParameter("expected");
            Long timeout = Long.parseLong(request.getParameter("timeout"));
            boolean hidden = "true".equals(request.getParameter("hidden"));
            int assignmentID = Integer.parseInt(request.getParameter("assignmentID"));
            AssignmentTest test = new AssignmentTest(0, assignmentID, input, expected, timeout, hidden, 0);
            DBUtil.addTest(test);
        }
        String strUpID = request.getParameter("TestUpID");
        if(strUpID != null)
        {
            int upID = Integer.parseInt(strUpID);
            DBUtil.moveTestUp(id, upID);
        }
        String strDownID = request.getParameter("TestDownID");
        if(strDownID != null)
        {
            int downID = Integer.parseInt(strDownID);
            DBUtil.moveTestDown(id, downID);
        }
        String strDelID = request.getParameter("DeleteTestID");
        if(strDelID != null)
        {
            int delID = Integer.parseInt(strDelID);
            DBUtil.deleteTest(delID);
        }
        Assignment assignment = DBUtil.getAssignment(id);
        List<AssignmentTest> tests = DBUtil.getAssignmentTests(id);
        showAssignment(response, assignment, tests);
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
    
    private void showAssignment(HttpServletResponse response, Assignment assignment, List<AssignmentTest> tests) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Assignment Edit</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Assignment: " + assignment.getName() + "</h1>");
            out.println("<p>" + assignment.getDescription() + "</p>");
            out.println("<table border=\"1\">");
            out.println("<tr><th>Input</th><th>Expected output</th><th>Timeout</th><th>Hidden</th><th>Delete</th><th>Up</th><th>Down</th></tr>");
            for(AssignmentTest t : tests)
            {
                out.print("<tr><td>");
                out.print(t.getInput());
                out.print("</td><td>");
                out.print(t.getExpected());
                out.print("</td><td>");
                out.print(t.getTimeout());
                out.print("</td><td>");
                if(t.isHidden())
                {
                    out.print("Hidden");
                }
                else
                {
                    out.print("Visible");
                }
                //out.print("</td><td><a href=\"AssignmentTestEdit?ID=" + t.getID() + "\">Edit</a></td>");
                out.print("</td><td><a href=\"AssignmentEdit?DeleteTestID=" + t.getID() + "&ID=" + assignment.getId() + "\">Delete</a></td>");
                out.print("</td><td><a href=\"AssignmentEdit?TestUpID=" + t.getID() + "&ID=" + assignment.getId() + "\">Up</a></td>");
                out.print("</td><td><a href=\"AssignmentEdit?TestDownID=" + t.getID() + "&ID=" + assignment.getId() + "\">Down</a></td>");
                out.println("</tr>");
                
            }
            out.println("</table>");
            out.println("<h2>Create new test:</h2>");
            out.println("<form method=\"post\">");
            out.println("Input:<br>");
            out.println("<input type=\"text\" name=\"input\"><br>");
            out.println("Expected output:<br>");
            out.println("<input type=\"text\" name=\"expected\"><br>");
            out.println("Timeout:<br>");
            out.println("<input type=\"text\" name=\"timeout\"><br>");
            out.println("Hidden:<br>");
            out.println("<input type=\"checkbox\" name=\"hidden\" value=\"true\"> Hidden<br>");
            out.println("<input type=\"hidden\" name=\"assignmentID\" value=\"" + assignment.getId() + "\"/>");
            out.println("<input type=\"hidden\" name=\"CreateNewTest\" value=\"TRUE\"/>");
            out.println("<input type=\"submit\" name=\"submit\" value=\"GO\"/>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

}
