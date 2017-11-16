/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import java.util.ArrayList;

/**
 *
 * @author Tobias
 */
public class Nav
{
    public static Iterable<NavItem> getNavItems()
    {
        ArrayList<NavItem> res = new ArrayList<>();
        res.add(new NavItem("Assignments", "/CodeCheckWeb/ShowTasks"));
        return res;
    }
    
    public static Iterable<NavItem> getNavItems(String active)
    {
        Iterable<NavItem> res = getNavItems();
        for(NavItem item : res)
        {
            if(active.equals(item.getText()))
            {
                item.setActive(true);
            }
        }
        return res;
    }
    
    public static String getWebSiteName()
    {
        return "CodeCheckWeb";
    }
    
    public static String getWebSiteUrl()
    {
        return "/CodeCheckWeb";
    }
}
