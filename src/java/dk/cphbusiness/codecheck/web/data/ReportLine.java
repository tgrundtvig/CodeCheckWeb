/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web.data;

/**
 *
 * @author Tobias Grundtvig
 */
public class ReportLine
{
    private final int id;
    private final int report_id;
    private final AssignmentTest test;
    private final String output;
    private final String status;

    public ReportLine(int id, int report_id, AssignmentTest test, String output, String status)
    {
        this.id = id;
        this.report_id = report_id;
        this.test = test;
        this.output = output;
        this.status = status;
    }

    public int getID()
    {
        return id;
    }

    public int getReportID()
    {
        return report_id;
    }

    public AssignmentTest getTest()
    {
        return test;
    }

    public String getOutput()
    {
        return output;
    }

    public String getStatus()
    {
        return status;
    }
}
