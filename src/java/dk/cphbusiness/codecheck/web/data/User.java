/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web.data;

import java.util.Collection;

/**
 *
 * @author Tobias Grundtvig
 */
public class User
{
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String mobilePhone;
    private final String email;
    private final Collection<String> roles;

    public User(int id, String firstName, String lastName, String mobilePhone, String email, Collection<String> roles)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.roles = roles;
    }

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getMobilePhone()
    {
        return mobilePhone;
    }

    public String getEmail()
    {
        return email;
    }
    
    public String getFullName()
    {
        return firstName + " " + lastName;
    }
    
    public boolean hasRole(String role)
    {
        return roles.contains(role);
    }
}
