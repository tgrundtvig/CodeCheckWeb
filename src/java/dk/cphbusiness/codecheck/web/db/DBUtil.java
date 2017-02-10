/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.codecheck.web.db;

import dk.cphbusiness.codecheck.web.Config;
import dk.cphbusiness.codecheck.web.Login;
import dk.cphbusiness.codecheck.web.data.Assignment;
import dk.cphbusiness.codecheck.web.data.AssignmentTest;
import dk.cphbusiness.codecheck.web.data.Course;
import dk.cphbusiness.codecheck.web.data.Report;
import dk.cphbusiness.codecheck.web.data.ReportLine;
import dk.cphbusiness.codecheck.web.data.Task;
import dk.cphbusiness.codecheck.web.data.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tobias Grundtvig
 */
public class DBUtil
{

    static
    {
        try
        {
            Class.forName(Config.JDBC_DRIVER);
        }
        catch(ClassNotFoundException ex)
        {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(Config.DB_URL, Config.USER, Config.PASS);
    }
    
    
    public static boolean addRole(int userID, String roleName)
    {
        boolean res = false;
        int roleID = getRoleID(roleName);
        if(roleID >= 0)
        {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try
            {
                conn = getConnection();
                stmt = conn.prepareStatement("INSERT INTO user_role (user_id, role_id) VALUES (?, ?)");
                stmt.setInt(1, userID);
                stmt.setInt(2, roleID);
                stmt.executeUpdate();
                res = true;
            }
            catch(SQLException ex)
            {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                close(rs);
                close(stmt);
                close(conn);
            }
        }
        return res;
    }
    
    public static User createStudent(String firstName, String lastName, String mobile, String email, String passWord)
    {
        User res = createUser(firstName, lastName, mobile, email, passWord);
        int roleID = getRoleID("STUDENT");
        if(res != null && roleID >= 0)
        {
            Connection conn = null;
            PreparedStatement stmt = null;
            try
            {
                conn = getConnection();
                stmt = conn.prepareStatement("INSERT INTO user_role (user_id, role_id) VALUES (?, ?)");
                stmt.setInt(1, res.getId());
                stmt.setInt(2, roleID);
                stmt.executeUpdate();
            }
            catch(SQLException ex)
            {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                close(stmt);
                close(conn);
            }
        }
        return res;
    }
    
    
    public static User createUser(String firstName, String lastName, String mobile, String email, String passWord)
    {
        User res = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("INSERT INTO user (first_name, last_name, mobile_phone, email, passwd_hash) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, mobile);
            stmt.setString(4, email);
            stmt.setString(5, passWord);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.first())
            {
                int id = rs.getInt(1);
                res = new User(id, firstName, lastName, mobile, email, new ArrayList<>());
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }
    
    public static void createCourse(String name)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("INSERT INTO course (name) VALUES (?)");
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(stmt);
            close(conn);
        }
    }
    
    public static void deleteCourse(int id)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("DELETE FROM course WHERE ID = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(stmt);
            close(conn);
        }
    }
    
    public static Course getCourse(int courseID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Course res = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT name FROM course WHERE id = ?");
            stmt.setInt(1, courseID);
            rs = stmt.executeQuery();

            // Extract data from result set
            if(rs.next())
            {
                //Retrieve by column name
                String name = rs.getString("name");
                res = new Course(courseID, name);
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }
    
    public static List<Course> getCourses()
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Course> courses = new ArrayList<>();
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT id, name FROM course ORDER BY name");
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int id = rs.getInt("id");
                String name = rs.getString("name");
                courses.add(new Course(id, name));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return courses;
    }
    
    public static void assignUserToCourse(int userID, int courseID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("INSERT INTO user_course (user_id, course_id) VALUES (?, ?)");
            stmt.setInt(1, userID);
            stmt.setInt(2, courseID);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(stmt);
            close(conn);
        }
    }
    
    public static void unassignUserFromCourse(int userID, int courseID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("DELETE FROM user_course WHERE user_id = ? AND course_id = ?");
            stmt.setInt(1, userID);
            stmt.setInt(2, courseID);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(stmt);
            close(conn);
        }
    }
    
    public static List<Course> getAssignedCourses(int userID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Course> courses = new ArrayList<>();
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT c.id, c.name FROM course AS c, user_course AS uc WHERE uc.user_id = ? AND uc.course_id = c.id ORDER BY c.name");
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int id = rs.getInt(1);
                String name = rs.getString(2);
                courses.add(new Course(id, name));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return courses;
    }
    
    public static List<Course> getUnassignedCourses(int userID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Course> courses = new ArrayList<>();
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT c.id, c.name FROM course AS c WHERE c.id NOT IN (SELECT c.id FROM course AS c, user_course AS uc WHERE uc.user_id = ? AND uc.course_id = c.id) ORDER BY c.name");
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int id = rs.getInt(1);
                String name = rs.getString(2);
                courses.add(new Course(id, name));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return courses;
    }
    
    public static List<User> getUsersOnCourse(int courseID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT u.id, u.first_name, u.last_name, u.mobile_phone, u.email FROM user AS u, user_course AS uc WHERE uc.course_id = ? AND u.id = uc.user_id");
            stmt.setInt(1, courseID);
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String mobile = rs.getString(4);
                String email = rs.getString(5);
                users.add(new User(id, firstName, lastName, mobile, email, null));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return users;
    }
    
    public static Assignment createAssignment(String name, String description)
    {
        Assignment res = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("INSERT INTO assignment (name, description) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.first())
            {
                int id = rs.getInt(1);
                res = new Assignment(id, name, description);
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }
    
    public static void addTest(AssignmentTest test)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT MAX(test_order) FROM assignment_test WHERE assignment_id = ?");
            stmt.setInt(1, test.getAssignmentID());
            rs = stmt.executeQuery();
            int order = 0;
            if(rs.next())
            {
                order = rs.getInt(1);
            }
            ++order;
            stmt = conn.prepareStatement("INSERT INTO assignment_test (assignment_id, input, expected, timeout, hidden, test_order) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, test.getAssignmentID());
            stmt.setString(2, test.getInput());
            stmt.setString(3, test.getExpected());
            stmt.setLong(4, test.getTimeout());
            stmt.setBoolean(5, test.isHidden());
            stmt.setInt(6, order);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
    public static void deleteTest(int id)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("DELETE FROM assignment_test WHERE ID = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(stmt);
            close(conn);
        }
    }
    
    public static void moveTestUp(int assignmentID, int testID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT test_order FROM assignment_test WHERE id = ?");
            stmt.setInt(1, testID);
            rs = stmt.executeQuery();
            int orderA = -1;
            if(rs.next())
            {
                orderA = rs.getInt(1);
            }
            if(orderA < 0) return;
            stmt = conn.prepareStatement("SELECT id, test_order FROM assignment_test WHERE test_order < ? AND assignment_id = ? ORDER BY test_order DESC");
            stmt.setInt(1, orderA);
            stmt.setInt(2, assignmentID);
            rs = stmt.executeQuery();
            int orderB = -1;
            int testIDB = -1;
            if(rs.next())
            {
                testIDB = rs.getInt(1);
                orderB = rs.getInt(2);
            }
            if(orderB < 0) return;
            
            stmt = conn.prepareStatement("UPDATE assignment_test SET test_order = ? WHERE id = ?");
            stmt.setInt(1, orderB);
            stmt.setInt(2, testID);
            stmt.executeUpdate();
            stmt = conn.prepareStatement("UPDATE assignment_test SET test_order = ? WHERE id = ?");
            stmt.setInt(1, orderA);
            stmt.setInt(2, testIDB);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
    public static void moveTestDown(int assignmentID, int testID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT test_order FROM assignment_test WHERE id = ?");
            stmt.setInt(1, testID);
            rs = stmt.executeQuery();
            int orderA = -1;
            if(rs.next())
            {
                orderA = rs.getInt(1);
            }
            if(orderA < 0) return;
            stmt = conn.prepareStatement("SELECT id, test_order FROM assignment_test WHERE test_order > ? AND assignment_id = ? ORDER BY test_order");
            stmt.setInt(1, orderA);
            stmt.setInt(2, assignmentID);
            rs = stmt.executeQuery();
            int orderB = -1;
            int testIDB = -1;
            if(rs.next())
            {
                testIDB = rs.getInt(1);
                orderB = rs.getInt(2);
            }
            if(orderB < 0) return;
            
            stmt = conn.prepareStatement("UPDATE assignment_test SET test_order = ? WHERE id = ?");
            stmt.setInt(1, orderB);
            stmt.setInt(2, testID);
            stmt.executeUpdate();
            stmt = conn.prepareStatement("UPDATE assignment_test SET test_order = ? WHERE id = ?");
            stmt.setInt(1, orderA);
            stmt.setInt(2, testIDB);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
    public static int getRoleID(String roleName)
    {
        int res = -1;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(
              "SELECT id FROM role WHERE name = ?");
            stmt.setString(1, roleName);
            rs = stmt.executeQuery();
            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                res = rs.getInt("id");
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }
    
    public static User getUser(String email, String passwd)
    {
        User res = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(
              "SELECT id, first_name, last_name, mobile_phone FROM user WHERE email = ? AND passwd_hash = ?");
            stmt.setString(1, email);
            stmt.setString(2, passwd);
            rs = stmt.executeQuery();
            // Extract data from result set
            if(rs.next())
            {
                //Retrieve by column name
                int id = rs.getInt("id");
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                String mobile_phone = rs.getString("mobile_phone");
                close(rs);
                close(stmt);
                stmt = conn.prepareStatement(
                    "SELECT r.name FROM role AS r, user_role AS ur WHERE ur.user_id = ? AND ur.role_id = r.id");
                stmt.setInt(1, id);
                rs = stmt.executeQuery();
                Collection<String> roles = new ArrayList<>();
                while(rs.next())
                {
                    roles.add(rs.getString("r.name"));
                }
                res = new User(id, first, last, mobile_phone, email, roles);
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }

    public static Task getTask(int taskID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Task res = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(
              "SELECT t.user_id, t.assignment_id, a.name, t.begin, t.end, t.max_handins, t.status "
              + "FROM assignment AS a, task AS t "
              + "WHERE t.id = ? AND t.assignment_id = a.id AND t.begin <= CURRENT_TIMESTAMP");
            stmt.setInt(1, taskID);
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int uID = rs.getInt("user_id");
                int aID = rs.getInt("assignment_id");
                String name = rs.getString("name");
                Date begin = rs.getTimestamp("begin");
                Date end = rs.getTimestamp("end");
                int maxHandins = rs.getInt("max_handins");
                String status = rs.getString("status");
                res = new Task(taskID, uID, aID, name, begin, end, maxHandins, status);
            }
            if(res == null)
            {
                return null;
            }
            rs.close();
            stmt.close();
            stmt = conn.prepareStatement("SELECT count(id) AS count FROM report WHERE task_id = ?");
            stmt.setInt(1, taskID);
            rs = stmt.executeQuery();
            while(rs.next())
            {
                res.setAttempts(rs.getInt("count"));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }

    public static List<Task> getUserTasks(int userID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Task> tasks = new ArrayList<>();
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(
              "SELECT a.id AS a_id, a.name, t.id AS t_id, t.begin, t.end, t.max_handins, t.status "
              + "FROM assignment AS a, task AS t "
              + "WHERE t.assignment_id = a.id AND t.user_id = ? AND t.begin <= CURRENT_TIMESTAMP");
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int aID = rs.getInt("a_id");
                String name = rs.getString("name");
                int tID = rs.getInt("t_id");
                Date begin = rs.getTimestamp("begin");
                Date end = rs.getTimestamp("end");
                int maxHandins = rs.getInt("max_handins");
                String status = rs.getString("status");
                tasks.add(new Task(tID, userID, aID, name, begin, end, maxHandins, status));
            }
            rs.close();
            stmt.close();
            stmt = conn.prepareStatement("SELECT count(id) AS count FROM report WHERE task_id = ?");
            for(Task h : tasks)
            {
                stmt.setInt(1, h.getID());
                rs = stmt.executeQuery();
                while(rs.next())
                {
                    h.setAttempts(rs.getInt("count"));
                }
                rs.close();
            }
            stmt.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return tasks;
    }

    public static List<ReportLine> getReportLines(int reportID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ReportLine> reportLines = new ArrayList<>();
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT rl.id, rl.assignment_test_id, rl.output, rl.status, "
                                         + "at.assignment_id, at.input, at.expected, at.timeout, at.hidden, at.test_order "
                                         + "FROM report_line AS rl, assignment_test AS at "
                                         + "WHERE rl.assignment_test_id = at.id AND rl.report_id = ? "
                                         + "ORDER BY at.test_order");
            stmt.setInt(1, reportID);
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int lineID = rs.getInt("id");
                int testID = rs.getInt("assignment_test_id");
                String output = rs.getString("output");
                String status = rs.getString("status");
                int assignmentID = rs.getInt("assignment_id");
                String input = rs.getString("input");
                String expected = rs.getString("expected");
                long timeout = rs.getLong("timeout");
                boolean hidden = rs.getBoolean("hidden");
                int order = rs.getInt("test_order");
                AssignmentTest test = new AssignmentTest(testID, assignmentID, input, expected, timeout, hidden, order);
                reportLines.add(new ReportLine(lineID, reportID, test, output, status));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return reportLines;
    }

    public static Report getReport(int reportID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Report result = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT task_id, status, created "
                                         + "FROM report "
                                         + "WHERE id = ?");
            stmt.setInt(1, reportID);
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int task_id = rs.getInt("task_id");
                String status = rs.getString("status");
                Date created = rs.getTimestamp("created");
                result = new Report(reportID, task_id, status, created);
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return result;
    }

    public static List<Report> getTaskReports(int taskID)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Report> reports = new ArrayList<>();
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT id, status, created "
                                         + "FROM report "
                                         + "WHERE task_id = ? "
                                         + "ORDER BY created");
            stmt.setInt(1, taskID);
            rs = stmt.executeQuery();

            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int id = rs.getInt("id");
                String status = rs.getString("status");
                Date created = rs.getTimestamp("created");
                reports.add(new Report(id, taskID, status, created));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return reports;
    }

    public static int createReport(int taskID) throws SQLException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("INSERT INTO report (task_id) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, taskID);
            stmt.executeUpdate();

            ResultSet tableKeys = stmt.getGeneratedKeys();
            tableKeys.next();
            return tableKeys.getInt(1);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
    }

    public static void updateReportStatus(int reportID, String status) throws SQLException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("UPDATE report SET status = ? WHERE id = ?");
            stmt.setString(1, status);
            stmt.setInt(2, reportID);
            stmt.executeUpdate();
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
    public static void updateTaskStatus(int taskID, String status) throws SQLException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("UPDATE task SET status = ? WHERE id = ?");
            stmt.setString(1, status);
            stmt.setInt(2, taskID);
            stmt.executeUpdate();
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
    }

    public static void createReportLine(int reportID, int assignmentTestID, String output, String status)
      throws
      SQLException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(
              "INSERT INTO report_line (report_id, assignment_test_id, output, status) VALUES(?,?,?,?)");
            stmt.setInt(1, reportID);
            stmt.setInt(2, assignmentTestID);
            stmt.setString(3, output);
            stmt.setString(4, status);
            stmt.executeUpdate();
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
    public static List<Assignment> getAllAssignments()
    {
        List<Assignment> res = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT id, name, description FROM assignment");
            rs = stmt.executeQuery();
            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                res.add(new Assignment(id, name, description));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }
    
    public static void deleteAssignment(int id)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("DELETE FROM assignment WHERE ID = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(stmt);
            close(conn);
        }
    }
    
    public static Assignment getAssignment(int assignmentID)
    {
        Assignment res = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT name, description FROM assignment WHERE id = ?");
            stmt.setInt(1, assignmentID);
            rs = stmt.executeQuery();
            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                String name = rs.getString("name");
                String description = rs.getString("description");
                res = new Assignment(assignmentID, name, description);
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }

    public static List<AssignmentTest> getAssignmentTests(int assignmentID)
    {
        List<AssignmentTest> res = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement(
              "SELECT id, input, expected, timeout, hidden, test_order FROM assignment_test WHERE assignment_id = ? ORDER BY test_order");
            stmt.setInt(1, assignmentID);
            rs = stmt.executeQuery();
            // Extract data from result set
            while(rs.next())
            {
                //Retrieve by column name
                int id = rs.getInt("id");
                String input = rs.getString("input");
                String expected = rs.getString("expected");
                long timeout = rs.getLong("timeout");
                boolean hidden = rs.getBoolean("hidden");
                int order = rs.getInt("test_order");
                res.add(new AssignmentTest(id, assignmentID, input, expected, timeout, hidden, order));
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            close(rs);
            close(stmt);
            close(conn);
        }
        return res;
    }
    
    public static void createTask(int userID, int assignmentID, Date begin, Date end, int numberOfHandins) throws SQLException
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try
        {
            conn = getConnection();
            stmt = conn.prepareStatement("INSERT INTO task (user_id, assignment_id, begin, end, max_handins) VALUES(?, ?, ?, ?, ?)");
            stmt.setInt(1, userID);
            stmt.setInt(2, assignmentID);
            stmt.setTimestamp(3, new Timestamp(begin.getTime()));
            stmt.setTimestamp(4, new Timestamp(end.getTime()));
            stmt.setInt(5, numberOfHandins);
            stmt.executeUpdate();
        }
        finally
        {
            close(stmt);
            close(conn);
        }
    }

    private static void close(AutoCloseable c)
    {
        if(c != null)
        {
            try
            {
                c.close();
            }
            catch(Exception ex)
            {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    

    
}
