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
public class Task
{
    private final int id;
    private final int user_id;
    private final int assignment_id;
    private final String assignment_name;
    private final Date begin;
    private final Date end;
    private final int max_handins;
    private final String status;
    private int attempts;

    public Task(int id, int user_id, int assignment_id, String assignment_name, Date begin, Date end, int max_handins,
                String status)
    {
        this.id = id;
        this.user_id = user_id;
        this.assignment_id = assignment_id;
        this.assignment_name = assignment_name;
        this.begin = begin;
        this.end = end;
        this.max_handins = max_handins;
        this.status = status;
        this.attempts = 0;
    }

    public int getID()
    {
        return id;
    }

    public int getUserID()
    {
        return user_id;
    }

    public int getAssignmentID()
    {
        return assignment_id;
    }

    public String getName()
    {
        return assignment_name;
    }

    public Date getBegin()
    {
        return begin;
    }

    public Date getEnd()
    {
        return end;
    }

    public int getMaxHandins()
    {
        return max_handins;
    }

    public String getStatus()
    {
        return status;
    }
    
    public int getAttempts()
    {
        return attempts;
    }
    
    public void setAttempts(int attempts)
    {
        this.attempts = attempts;
    }
    
}
