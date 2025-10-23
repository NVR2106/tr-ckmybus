import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/DeleteStudentServlet")
public class DeleteStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/college_bus";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "P@ssw0rd";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String rollNo = request.getParameter("roll_no").trim();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                String sql = "DELETE FROM students WHERE roll_no=?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, rollNo);
                    int deleted = stmt.executeUpdate();
                    if (deleted > 0) {
                        response.getWriter().write("success");
                    } else {
                        response.getWriter().write("error: Student not found");
                    }
                }
            }
        } catch (Exception e) {
            response.getWriter().write("error: " + e.getMessage());
        }
    }
}
