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
            StringBuilder body = new StringBuilder();
            
            body.append("<h1>Assignment: ").append(assignment.getName()).append("</h1>");
            body.append("<div>").append(assignment.getDescription()).append("</div>");
            body.append("<h2>Attempt #").append(task.getAttempts()).append("</h2>");
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
            body.append("<div>Time: ").append(ft.format(report.getCreated())).append("</div>");
            body.append("<h3>Status: ").append(report.getStatus()).append("</h3>");
            body.append("<h2>Visible tests:</h2>");
            body.append("<table class=\"table\">");
            body.append("<tr><th>Input</th><th>Expected output</th><th>Actual output</th><th>Status</th></tr>");
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
                    body.append("<tr><td>");
                    body.append(BootstrapUtil.code(line.getTest().getInput()));
                    body.append("</td><td>");
                    body.append(BootstrapUtil.code(line.getTest().getExpected()));
                    body.append("</td><td>");
                    body.append(BootstrapUtil.code(line.getOutput()));
                    body.append("</td><td>");
                    body.append(line.getStatus());
                    body.append("</td></tr>");
                }
            }
            body.append("</table>");
            body.append("<h2>Hidden tests: ");
            body.append(hiddenCount);
            body.append(", ").append(hiddenOK).append(" OK, ").append(hiddenFailed).append(" FAILED");
            body.append("</h2>");
            String page = BootstrapUtil.createPage("Show Report", BootstrapNavigagtion.navBar(Nav.getWebSiteName(), Nav.getWebSiteUrl(), Nav.getNavItems()), body.toString(), true);
            out.println(page);
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
