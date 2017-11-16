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
public class BootstrapNavigagtion
{
    public static String navBar(String header, String headerUrl, Iterable<NavItem> items)
    {
        StringBuilder res = new StringBuilder();
        res.append( "<nav class=\"navbar navbar-default\">\n" +
                    "  <div class=\"container-fluid\">\n" +
                    "    <div class=\"navbar-header\">\n" +
                    "      <a class=\"navbar-brand\" href=\"");
        res.append(headerUrl);
        res.append("\">");
        res.append(header);
        res.append("</a>\n</div>\n <ul class=\"nav navbar-nav\">\n");
        for(NavItem item : items)
        {
            if(item.isActive())
            {
                res.append("<li class=\"active\">");
            }
            else
            {
                res.append("<li>");
            }
            res.append("<a href=\"");
            res.append(item.getUrl());
            res.append("\">");
            res.append(item.getText());
            res.append("</a></li>\n");
        }
        res.append("</ul></div></nav>\n");
        return res.toString();
    }
}
