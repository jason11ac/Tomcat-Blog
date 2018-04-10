import java.io.IOException;
import java.sql.* ;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;



/**
 * Servlet implementation class for Servlet: ConfigurationTest
 *
 */
public class Editor extends HttpServlet {
    /**
     * The Servlet constructor
     *
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public Editor() {}

    String use = null;
    int post = 0;

    Connection c = null;
    Statement  s = null;

    public void init() throws ServletException
    {
        //Open DB connection here
        /*  write any servlet initialization code here or remove this function */
        /* load the driver */

        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
            return;
        }
        try {
          //create an instance of a Connection object 
          c = DriverManager.getConnection("jdbc:mysql://localhost:3306/CS144", "cs144", "");

        } catch (SQLException ex) {
          
          System.out.println(ex.getMessage());
        } 
    }

    /**
     * Handles HTTP GET requests
     *
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {

      //Get parameters
      String title = request.getParameter("title");
      String body = request.getParameter("body");

      boolean newPost;

      if (request.getParameter("username") != null) {
        use = request.getParameter("username");
      }
      if (request.getParameter("postid") != null) {
        post = Integer.parseInt(request.getParameter("postid"));
      }

      request.setAttribute("username", use);
      request.setAttribute("postid", post);


      if (request.getParameter("action") != null) {
        String param = request.getParameter("action");
        if (param.equals("open")) {
          if (request.getParameter("title") == null && request.getParameter("body") == null) {
            ResultSet rs = null;

            try {
              String get_post = "SELECT * from Posts WHERE username = ? and postid = ?";
              PreparedStatement prepared = c.prepareStatement(get_post);
              prepared.setString(1, use);
              prepared.setInt(2, postid);
              rs = prepared.executeQuery();

              if (rs.next()) { //Already is a post with this username and postid
                  request.setAttribute("dtitle", rs.getString("title"));
                  request.setAttribute("dbody", rs.getString("body"));
                  request.getRequestDispatcher("/edit.jsp").forward(request, response);
              } else {
                request.setAttribute("dtitle", "");
                request.setAttribute("dbody", "");
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
              }

            } catch (SQLException ex) {
              PrintWriter out = response.getWriter();
              out.println("<!DOCTYPE html>");
              out.println("<html>");
              out.println("<body>" + ex.getMessage() + "<br>");
              out.println("</html>");
              out.close();
            }

          } else {
            request.setAttribute("dtitle", title);
            request.setAttribute("dbody", body);
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
          }
        }
        else if (param.equals("save")) {
          doPost(request, response);
        }
        else if (param.equals("preview")) {

          Parser parser = Parser.builder().build();
          HtmlRenderer renderer = HtmlRenderer.builder().build();
          String html_title = renderer.render(parser.parse(title));
          String html_body = renderer.render(parser.parse(body));

          request.setAttribute("username", request.getParameter("username"));
          request.setAttribute("title", html_title);
          request.setAttribute("body", html_body);
          request.setAttribute("username", use);
          request.getRequestDispatcher("/preview.jsp").forward(request, response);
        }
        else if (param.equals("delete")) {
          doPost(request, response);
        }
        else if (param.equals("list")) {
          request.setAttribute("use", use);
          request.getRequestDispatcher("/home.jsp").forward(request, response);
        }
        else if (param.equals("close")) {
          request.setAttribute("use", use);
          request.getRequestDispatcher("/home.jsp").forward(request, response);
        }
      }
  }


    /**
     * Handles HTTP POST requests
     *
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
      
      String action = request.getParameter("action");
      String title = request.getParameter("title");
      String body = request.getParameter("body");
      int postid = Integer.parseInt(request.getParameter("postid"));

      PrintWriter out3 = response.getWriter();
      out3.println(request.getParameter("postid"));
      out3.close();


      int update = 1;

      if (request.getParameter("username") != null) {
        use = request.getParameter("username");
      }

      if (request.getParameter("action") != null) {
        String param = request.getParameter("action");

        //If saved clicked
        if (param.equals("save")) {

          //First check to just do an update
          if (request.getParameter("postid") != null) {

            if (postid <= 0) {
              ResultSet rs = null;

              try {
                String get_postid = "SELECT * from Data WHERE username = ?";
                PreparedStatement prepared1 = c.prepareStatement(get_postid);
                prepared1.setString(1, use);
                //prepared1.setString(2, postid);
                rs = prepared1.executeQuery();
                if (!rs.next()) {

                  try {
                    String insert = "INSERT INTO Data (username, postid)" + " values (?,?)";
                    PreparedStatement prepared = c.prepareStatement(insert);
                    prepared.setString(1, use);
                    prepared.setInt(2, 1);
                    prepared.execute();
                  } catch (SQLException ex) {
                      PrintWriter out = response.getWriter();
                      out.println("<!DOCTYPE html>");
                      out.println("<html>");
                      out.println("<body>" + ex.getMessage() + "<br>");
                      out.println("</html>");
                  }
                } else {
                  int post = rs.getInt("postid");
                  update = post += 1;
                  //Changed from update to Insert for project
                    
                  try {
                    String insert = "UPDATE Data SET postid = ? WHERE username = ?";
                    PreparedStatement prepared = c.prepareStatement(insert);
                    prepared.setInt(1, update);
                    prepared.setString(2, use);
                    prepared.execute();
                  } catch (SQLException ex) {
                    PrintWriter out = response.getWriter();
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<body>" + ex.getMessage() + "<br>");
                    out.println("</html>");
                  }
                }

              } catch (SQLException ex) {
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<body>" + ex.getMessage() + "<br>");
                out.println("</html>");
              }

              DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              Date date = new Date();
              String created = dateFormat.format(date);

              try {
                //Actually insert the new post
                String insert = "INSERT INTO Posts (username, postid, title, body, modified, created)" + " values (?,?,?,?,?,?)";
                PreparedStatement prepared = c.prepareStatement(insert);
                prepared.setString(1, use);
                prepared.setInt(2, update);
                prepared.setString(3, title);
                prepared.setString(4, body);
                prepared.setString(5, created);
                prepared.setString(6, created);
                prepared.execute();

              } catch (SQLException ex) {
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<body>" + ex.getMessage() + "testtest<br>");
                out.println("</html>");
              }

            } else { //postid > 0
              ResultSet rs = null;
                
              try {
                String get_post = "SELECT * from Posts WHERE username = ? and postid = ?";
                PreparedStatement prepared = c.prepareStatement(get_post);
                prepared.setString(1, use);
                prepared.setInt(2, postid);
                rs = prepared.executeQuery();

                if (rs.next()) { //Already is a post with this username and postid
                  try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String mod = dateFormat.format(date);
      
                    String update_post = "UPDATE Posts SET title = ?, body = ?, modified = ? WHERE username = ? and postid = ?";
                    PreparedStatement prepared1 = c.prepareStatement(update_post);
                    prepared1.setString(1, title);
                    prepared1.setString(2, body);
                    prepared1.setString(3, mod);
      
                    prepared1.setString(4, use);
                    prepared1.setInt(5, postid);
                    prepared1.execute(); 
                  } catch (SQLException ex) {
                    PrintWriter out = response.getWriter();
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<body>" + ex.getMessage() + "<br>");
                    out.println("</html>");
                  }
                } else {
                  //make no changes
                }

              } catch (SQLException ex) {
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<body>" + ex.getMessage() + "<br>");
                out.println("</html>");
              }
            }
          }
        } else if (param.equals("open")) {

            if (request.getParameter("title") == null && request.getParameter("body") == null) {
              ResultSet rs = null;

              try {
                String get_post = "SELECT * from Posts WHERE username = ? and postid = ?";
                PreparedStatement prepared = c.prepareStatement(get_post);
                prepared.setString(1, use);
                prepared.setInt(2, postid);
                rs = prepared.executeQuery();
              
                if (rs.next()) { //Already is a post with this username and postid
                    request.setAttribute("dtitle", rs.getString("title"));
                    request.setAttribute("dbody", rs.getString("body"));
                    request.getRequestDispatcher("/edit.jsp").forward(request, response);
                } else {
                  request.setAttribute("dtitle", "");
                  request.setAttribute("dbody", "");
                  request.getRequestDispatcher("/edit.jsp").forward(request, response);
                }

              } catch (SQLException ex) {
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<body>" + ex.getMessage() + "testtest<br>");
                out.println("</html>");
              }
            } else {
              request.setAttribute("dtitle", title);
              request.setAttribute("dbody", body);
              request.getRequestDispatcher("/edit.jsp").forward(request, response);
            }
      } else if (param.equals("delete")) {
        try {
          //Print out home screen with data

          String display = "DELETE FROM Posts WHERE username = ? AND postid = ?";
          PreparedStatement prepared = c.prepareStatement(display);

          prepared.setString(1, use);
          prepared.setInt(2, post);

          prepared.executeUpdate();
        } catch (SQLException ex) {
          System.out.println(ex.getMessage());
        }

        request.setAttribute("use", use);
        request.getRequestDispatcher("/home.jsp").forward(request, response);
      }
    }
  }
