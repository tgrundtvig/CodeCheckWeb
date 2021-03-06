/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web;

import java.util.Scanner;

/**
 *
 * @author Tobias
 */
public class BootstrapUtil
{
    public static String createPage(String title, String navigation, String body)
    {
        return createPage(title, navigation, body, false);
    }
    
    public static String createPage(String title, String body)
    {
        return createPage(title, "", body, false);
    }
    
    public static String createPage(String title, String navigation, String body, boolean refresh)
    {
        StringBuilder res = new StringBuilder();
        res.append("<!DOCTYPE html>");
        res.append("<html lang=\"en\">");
        res.append("<head><title>");
        res.append(title);
        res.append("</title><meta charset=\"utf-8\">");
        res.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        res.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">");
        res.append("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>");
        res.append("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>");
        if(refresh)
        {
            res.append("<META HTTP-EQUIV=\"refresh\" CONTENT=\"1\">");
        }
        res.append("</head>");
        res.append("<body>");
        res.append(navigation);
        res.append("<div class=\"container\">");
        res.append(body);
        res.append("</div></body></html>");
        return res.toString();
    }
    
    public static String code(String s)
    {
        StringBuilder res = new StringBuilder();
        Scanner scanner = new Scanner(s);
        while(scanner.hasNextLine())
        {
            res.append("<div style=\"white-space: nowrap\"><code>");
            res.append(scanner.nextLine());
            res.append("</code></div><br/>");
        }
        /*
        if(s.contains("\n"))
        {
            return "<code>" + s.replace("\n", "<br/>") + "</code>";
        }
        else
        {
            return "<div style=\"white-space: nowrap\"><code>" + s + "</code></div>";
        }
        */
        return res.toString();
    }
}
