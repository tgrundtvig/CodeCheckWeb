/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.db.DBUtil;
import dk.cphbusiness.codecheck.web.data.Assignment;
import dk.cphbusiness.codecheck.web.data.Report;
import dk.cphbusiness.codecheck.web.data.ReportLine;
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
public class ShowReport extends HttpServlet
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
        int reportID = Integer.parseInt(request.getParameter("ReportID"));
        Report report = DBUtil.getReport(reportID);
        if(report == null)
        {
            printError(response);
            return;
        }
        Task task = DBUtil.getTask(report.getTaskID());
        if(task == null || task.getUserID() != user.getId())
        {
            printError(response);
            return;
        }
        Assignment assignment = DBUtil.getAssignment(task.getAssignmentID());
        List<ReportLine> lines = DBUtil.getReportLines(reportID);
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Show report</title>");
            if("RUNNING".equals(report.getStatus()))
            {
                out.println("<META HTTP-EQUIV=\"refresh\" CONTENT=\"5\">");
            }
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Assignment: " + assignment.getName() + "</h1>");
            out.println("<div>" + assignment.getDescription() + "</div>");
            out.println("<h2>Attempt #" + task.getAttempts() + "</h2>");
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
            out.println("<div>Time: " + ft.format(report.getCreated()) + "</div>");
            out.println("<h3>Status: " + report.getStatus() + "</h3>");
            out.println("<h2>Visible tests:</h2>");
            out.println("<table border=\"1\">");
            out.println("<tr><th>Input</th><th>Expected output</th><th>Actual output</th><th>Status</th></tr>");
            int hiddenCount = 0;
            int hiddenOK = 0;
            int hiddenFailed = 0;
            for(ReportLine line : lines)
            {
                if(line.getTest().isHidden())
                {
                    ++hiddenCount;
                    if("OK".equals(line.getStatus()))
                    {
                        ++hiddenOK;
                    }
                    else
                    {
                        ++hiddenFailed;
                    }
                }
                else
                {
                    out.print("<tr><td>");
                    out.print(line.getTest().getInput());
                    out.print("</td><td>");
                    out.print(line.getTest().getExpected());
                    out.print("</td><td>");
                    out.print(line.getOutput());
                    out.print("</td><td>");
                    out.print(line.getStatus());
                    out.println("</td></tr>");
                }
            }
            out.println("</table>");
            out.print("<h2>Hidden tests: ");
            out.print(hiddenCount);
            out.print(", " + hiddenOK + " OK, " + hiddenFailed + " FAILED");
            out.println("</h2>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void printError(HttpServletResponse response) throws IOException
    {
        try(PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Show assignment</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Invalid report id</h1>");
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
