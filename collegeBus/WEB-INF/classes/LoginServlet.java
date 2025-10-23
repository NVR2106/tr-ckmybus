import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String userId = request.getParameter("userId");
        String role = request.getParameter("role");

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the "college_bus" database
            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/college_bus", "root", "P@ssw0rd")) {

                boolean authenticated = false;

                // Check in admin table
                if ("admin".equals(role)) {
                    String sql = "SELECT * FROM admin WHERE admin_id=?";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, userId);
                        try (ResultSet rs = ps.executeQuery()) {
                            authenticated = rs.next();
                        }
                    }
                }
                // Check in student table
                else if ("student".equals(role)) {
                    String sql = "SELECT * FROM students WHERE roll_no=?";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, userId);
                        try (ResultSet rs = ps.executeQuery()) {
                            authenticated = rs.next();
                        }
                    }
                }

                // Redirect based on role
                if (authenticated) {
                    if ("admin".equals(role)) {
                        response.sendRedirect("admin.html");
                    } else {
                        response.sendRedirect("student.html");
                    }
                } else {
                    out.println("<script>alert('Invalid ID!'); window.history.back();</script>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}