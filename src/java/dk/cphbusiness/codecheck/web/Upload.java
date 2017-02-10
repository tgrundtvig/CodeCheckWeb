/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.db.DBUtil;
import dk.cphbusiness.codecheck.web.data.Task;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Tobias Grundtvig
 */
public class Upload extends HttpServlet
{

    // Documentation: https://commons.apache.org/proper/commons-fileupload/using.html
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

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(!isMultipart)
        {
            try(PrintWriter out = response.getWriter())
            {
                out.println("Error: not a multipart request.");
                return;
            }
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        //ServletContext servletContext = this.getServletConfig().getServletContext();
        //File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        File repository = new File(Config.UPLOAD_FOLDER);
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        int taskID = -1;
        try
        {
            // Parse the request
            List<FileItem> items = upload.parseRequest(request);
            FileItem ft = null;
            for(FileItem item : items)
            {
                if(item.isFormField())
                {
                    if("TaskID".equals(item.getFieldName()))
                    {
                        taskID = Integer.parseInt(item.getString());
                    }
                }
                else
                {
                    ft = item;
                }
            }
            if(taskID > 0 && ft != null)
            {
                int reportID = DBUtil.createReport(taskID);
                String fileName = "HandIn_" + reportID + ".jar";
                ft.write(new File(Config.UPLOAD_FOLDER + fileName));
                Task task = DBUtil.getTask(taskID);
                CodeChecker codeChecker = new CodeChecker(reportID, task, fileName);
                Thread t = new Thread(codeChecker);
                t.start();
                //codeChecker.run();
                response.sendRedirect("/CodeCheckWeb/ShowReport?ReportID=" + reportID);
            }
        }
        catch(FileUploadException ex)
        {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(Exception ex)
        {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
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
