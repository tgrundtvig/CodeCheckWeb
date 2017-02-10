/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.db.DBUtil;
import dk.cphbusiness.codecheck.web.data.Assignment;
import dk.cphbusiness.codecheck.web.data.AssignmentTest;
import dk.cphbusiness.codecheck.web.data.Report;
import dk.cphbusiness.codecheck.web.data.Task;
import dk.cphbusiness.codecheck.web.data.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
public class ShowTask extends HttpServlet
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
            response.sendRedirect("/CodeCheckWeb/Login");
            return;
        }
        int taskID = Integer.parseInt(request.getParameter("TaskID"));
        Task task = DBUtil.getTask(taskID);
        if(task == null || task.getUserID() != user.getId())
        {
            try(PrintWriter out = response.getWriter())
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Show assignment</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Invalid task id</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
        }
        Assignment assignment = DBUtil.getAssignment(task.getAssignmentID());
        List<AssignmentTest> tests = DBUtil.getAssignmentTests(task.getAssignmentID());
        List<Report> reports = DBUtil.getTaskReports(taskID);
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Show assignment</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Assignment: " + task.getName() + "</h1>");
            out.println("<h2>Description:</h2>");
            out.println("<div>" + assignment.getDescription() + "</div>");
            out.println("<h2>Visible tests:</h2>");
            out.println("<table border=\"1\">");
            out.println("<tr><th>Input</th><th>Expected output</th></tr>");
            int hiddenCount = 0;
            for(AssignmentTest test : tests)
            {
                if(test.isHidden())
                {
                    ++hiddenCount;
                }
                else
                {
                    out.print("<tr><td>");
                    out.print(test.getInput());
                    out.print("</td><td>");
                    out.print(test.getExpected());
                    out.println("</td></tr>");
                }
            }
            out.println("</table>");
            out.print("<h2>Hidden tests: ");
            out.print(hiddenCount);
            out.println("</h2>");
            out.println("<h2>Uploaded attempts: " + task.getAttempts() + " / " + ((task.getMaxHandins() < 1) ? "INF" : task.
                                                                         getMaxHandins()) + "</h2>");
            if(task.getAttempts() > 0)
            {
                out.println("<table border=\"1\">");
                out.println("<tr><th>When</th><th>Status</th></tr>");
                for(Report report : reports)
                {
                    out.print("<tr><td>");
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
                    out.print(ft.format(report.getCreated()));
                    out.print("</td><td><a href=\"/CodeCheckWeb/ShowReport?ReportID=" + report.getId() + "\">");
                    out.print(report.getStatus());
                    out.println("</a></td></tr>");
                }
                out.println("</table>");
            }
            if(task.getMaxHandins() < 1 || task.getAttempts() < task.getMaxHandins())
            {
                out.println("<h2>Upload solution jar-file:</h2>");
                out.println("<form action=\"/CodeCheckWeb/Upload\" method=\"post\" enctype=\"multipart/form-data\">");
                out.println("<input type=\"hidden\" name=\"TaskID\" value=\"" + task.getID() + "\" />");
                out.println("<div>Choose file to upload:</div>");
                out.println("<input type=\"file\" name=\"file\" />");
                out.println("<input type=\"submit\" />");
                out.println("</form>");
            }

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
