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
            StringBuilder body = new StringBuilder();
            body.append("<h1>Assignment: ").append(task.getName()).append("</h1>");
            body.append("<h2>Description:</h2>");
            body.append("<p>").append(assignment.getDescription()).append("</p>");
            body.append("<h2>Visible tests:</h2>");
            body.append("<table class=\"table\">");
            body.append("<tr><th>Input</th><th>Expected output</th></tr>");
            int hiddenCount = 0;
            for(AssignmentTest test : tests)
            {
                if(test.isHidden())
                {
                    ++hiddenCount;
                }
                else
                {
                    body.append("<tr><td>");
                    body.append(BootstrapUtil.code(test.getInput()));
                    body.append("</td><td>");
                    body.append(BootstrapUtil.code(test.getExpected()));
                    body.append("</td></tr>");
                }
            }
            body.append("</table>");
            body.append("<h2>Hidden tests: ");
            body.append(hiddenCount);
            body.append("</h2>");
            body.append("<h2>Uploaded attempts: ").append(task.getAttempts()).append(" / ").append((task.getMaxHandins() < 1) ? "INF" : task.
                    getMaxHandins()).append("</h2>");
            if(task.getAttempts() > 0)
            {
                body.append("<table class=\"table\">");
                body.append("<tr><th>When</th><th>Status</th></tr>");
                for(Report report : reports)
                {
                    body.append("<tr><td>");
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
                    body.append(ft.format(report.getCreated()));
                    body.append("</td><td><a href=\"/CodeCheckWeb/ShowReport?ReportID=").append(report.getId()).append("\">");
                    body.append(report.getStatus());
                    body.append("</a></td></tr>");
                }
                body.append("</table>");
            }
            if(task.getMaxHandins() < 1 || task.getAttempts() < task.getMaxHandins())
            {
                
                
                body.append("<h2>Upload solution jar-file:</h2>");
                body.append("<form action=\"/CodeCheckWeb/Upload\" method=\"post\" enctype=\"multipart/form-data\">");
                body.append("<input type=\"hidden\" name=\"TaskID\" value=\"").append(task.getID()).append("\" />");
                body.append("<div>Choose file to upload:</div>");
                body.append("<input type=\"file\" name=\"file\" />");
                body.append("<input type=\"submit\" />");
                body.append("</form>");
                
                
                //body.append("<h2>Upload solution jar-file:</h2>");
                //body.append("<form action=\"/CodeCheckWeb/Upload\" method=\"post\" enctype=\"multipart/form-data\">");
                //body.append("<input type=\"hidden\" name=\"TaskID\" value=\"").append(task.getID()).append("\" />");
                
               /* 
                body.append("<div class=\"form-group\">");
		body.append("<div class=\"input-group input-file\" name=\"Fichier1\">");
                body.append("<span class=\"input-group-btn\">");
                body.append("<button class=\"btn btn-default btn-choose\" type=\"button\">Choose</button>");
    		body.append("</span>");
    		body.append("<input type=\"text\" class=\"form-control\" placeholder='Choose a file...' />");
    		body.append("<span class=\"input-group-btn\">");
                body.append("<button class=\"btn btn-warning btn-reset\" type=\"button\">Reset</button>");
    		body.append("</span>");
		body.append("</div>");
                body.append("</div>");
                */
                
                
                
                
                
                
                
                
                
                
                
                
                
                
               // body.append("<div class=\"form-group\">");
                //body.append("<label for=\"file\">Choose file to upload:");
                //body.append("<input class=\"btn btn-default btn-file\" type=\"file\" name=\"file\" id=\"file\" />");
                //body.append("</label>");
                /*
                body.append("<label class=\"btn btn-primary\" for=\"file-selector\">");
                body.append("<input id=\"file-selector\" type=\"file\" style=\"display:none\" ");
                body.append("onchange=\"$('#file-info').html(this.files[0].name)\">");
                body.append("Choose file:");
                body.append("</label>");
                body.append("<span class='label label-info' id=\"file-info\">No file chosen!</span>");
                
                body.append("<input class=\"btn btn-primary\" type=\"submit\" />");
                body.append("</div>");
                
                
                
                body.append("</form>");
                */
            }
            
            String page = BootstrapUtil.createPage("Show assignment", BootstrapNavigagtion.navBar(Nav.getWebSiteName(), Nav.getWebSiteUrl(), Nav.getNavItems()), body.toString());
            out.println(page);
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
