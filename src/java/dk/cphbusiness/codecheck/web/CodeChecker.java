/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import dk.cphbusiness.codecheck.web.db.DBUtil;
import dk.cphbusiness.codecheck.web.data.AssignmentTest;
import dk.cphbusiness.codecheck.web.data.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tobias Grundtvig
 */
public class CodeChecker implements Runnable
{

    private final int reportID;
    private final Task task;
    private final String jarFile;

    public CodeChecker(int reportID, Task task, String jarFile)
    {
        this.reportID = reportID;
        this.task = task;
        this.jarFile = jarFile;
    }

    @Override
    public void run()
    {
        List<AssignmentTest> tests = DBUtil.getAssignmentTests(task.getAssignmentID());
        String status = "PASSED";
        for(AssignmentTest test : tests)
        {
            try
            {
                if(!performTest(test))
                {
                    status = "NOT PASSED";
                }
            }
            catch(IOException ex)
            {
                Logger.getLogger(CodeChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            DBUtil.updateReportStatus(reportID, status);
        }
        catch(SQLException ex)
        {
            Logger.getLogger(CodeChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!"PASSED".equals(task.getStatus()))
        {
            if("NOT PASSED".equals(status) && task.getAttempts() >= task.getMaxHandins())
            {
                status = "FAILED";
            }
            if(!status.equals(task.getStatus()))
            {
                try
                {
                    DBUtil.updateTaskStatus(task.getID(), status);
                }
                catch(SQLException ex)
                {
                    Logger.getLogger(CodeChecker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        File file = new File(Config.UPLOAD_FOLDER + jarFile);
        file.delete();
    }

    private boolean performTest(AssignmentTest test) throws IOException
    {
        ProcessBuilder builder = new ProcessBuilder("java", "-Djava.security.manager",
                                                    "-Djava.security.policy=studentpolicy", "-jar", jarFile);
        builder.directory(new File(Config.UPLOAD_FOLDER));
        builder.redirectErrorStream(true);
        Process process = builder.start();
        long startTime = System.currentTimeMillis();
        PrintStream out = new PrintStream(process.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        out.println(test.getInput());
        out.flush();
        while(System.currentTimeMillis() - startTime < test.getTimeout() && process.isAlive())
        {
            //Busy wait...
        }
        if(process.isAlive())
        {
            process.destroyForcibly();
            try
            {
                process.waitFor();
            }
            catch(InterruptedException ex)
            {
            }
            try
            {
                DBUtil.createReportLine(reportID, test.getID(), "", "TIMEOUT");
            }
            catch(SQLException ex)
            {
                Logger.getLogger(CodeChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
        StringBuilder res = new StringBuilder();
        int ch = in.read();
        while(ch >= 0)
        {
            res.append((char) ch);
            ch = in.read();
        }
        String result = res.toString();
        String status = "FAILED";
        boolean ok = false;
        if(result.trim().equals(test.getExpected().trim()))
        {
            status = "OK";
            ok = true;
        }
        try
        {
            DBUtil.createReportLine(reportID, test.getID(), result, status);
        }
        catch(SQLException ex)
        {
            Logger.getLogger(CodeChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ok;
    }
}
