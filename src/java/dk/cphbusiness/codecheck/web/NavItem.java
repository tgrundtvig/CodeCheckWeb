/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

/**
 *
 * @author Tobias
 */
public class NavItem
{
    private final String text;
    private final String url;
    private boolean active;

    public NavItem(String text, String url)
    {
        this.text = text;
        this.url = url;
        this.active = false;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getText()
    {
        return text;
    }

    public String getUrl()
    {
        return url;
    }
}
