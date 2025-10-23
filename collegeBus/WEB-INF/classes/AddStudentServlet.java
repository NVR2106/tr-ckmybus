import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/AddStudentServlet")
public class AddStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/college_bus";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "P@ssw0rd";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String rollNo = request.getParameter("roll_no").trim();
        String name = request.getParameter("name").trim();
        String branch = request.getParameter("branch").trim();
        String year = request.getParameter("year_of_study").trim();
        String busRoute = request.getParameter("bus_route").trim();
        String phone = request.getParameter("phone").trim();
        String email = request.getParameter("email").trim();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                String sql = "INSERT INTO students (roll_no, name, branch, year_of_study, bus_route, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, rollNo);
                    stmt.setString(2, name);
                    stmt.setString(3, branch);
                    stmt.setString(4, year);
                    stmt.setString(5, busRoute);
                    stmt.setString(6, phone);
                    stmt.setString(7, email);

                    int added = stmt.executeUpdate();
                    if (added > 0) {
                        response.getWriter().write("success");
                    } else {
                        response.getWriter().write("error: Could not add student");
                    }
                }
            }
        } catch (SQLException e) {
            response.getWriter().write("error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            response.getWriter().write("error: JDBC Driver not found");
        }
    }
}
