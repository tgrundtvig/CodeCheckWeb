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
public class AssignmentTest
{
    private final int id;
    private final int assignment_id;
    private final String input;
    private final String expected;
    private final long timeout;
    private final boolean hidden;
    private final int order;

    public AssignmentTest(int id, int assignment_id, String input, String expected, long timeout, boolean hidden, int order)
    {
        this.id = id;
        this.assignment_id = assignment_id;
        this.input = input;
        this.expected = expected;
        this.timeout = timeout;
        this.hidden = hidden;
        this.order = order;
    }

    public int getID()
    {
        return id;
    }

    public int getAssignmentID()
    {
        return assignment_id;
    }

    public String getInput()
    {
        return input;
    }

    public String getExpected()
    {
        return expected;
    }
    
    public long getTimeout()
    {
        return timeout;
    }

    public boolean isHidden()
    {
        return hidden;
    }
    
    public int getOrder()
    {
        return order;
    }
}
