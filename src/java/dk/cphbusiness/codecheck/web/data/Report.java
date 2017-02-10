/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web.data;

import java.util.Date;

/**
 *
 * @author Tobias Grundtvig
 */
public class Report
{
    private final int id;
    private final int task_id;
    private final String status;
    private final Date created;

    public Report(int id, int task_id, String status, Date created)
    {
        this.id = id;
        this.task_id = task_id;
        this.status = status;
        this.created = created;
    }

    public int getId()
    {
        return id;
    }
    
    public int getTaskID()
    {
        return task_id;
    }

    public String getStatus()
    {
        return status;
    }

    public Date getCreated()
    {
        return created;
    }
}
