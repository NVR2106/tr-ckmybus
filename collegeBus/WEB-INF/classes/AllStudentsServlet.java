import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

@WebServlet("/AllStudentsServlet")
public class AllStudentsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/college_bus";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "P@ssw0rd";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                String sql = "SELECT * FROM students";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                List<Map<String, String>> students = new ArrayList<>();
                while (rs.next()) {
                    Map<String,String> s = new HashMap<>();
                    s.put("roll_no", rs.getString("roll_no"));
                    s.put("name", rs.getString("name"));
                    s.put("branch", rs.getString("branch"));
                    s.put("year_of_study", rs.getString("year_of_study"));
                    s.put("bus_route", rs.getString("bus_route"));
                    s.put("phone", rs.getString("phone"));
                    s.put("email", rs.getString("email"));
                    students.add(s);
                }

                Gson gson = new Gson();
                response.getWriter().write(gson.toJson(students));
            }
        } catch (Exception e) {
            response.getWriter().write("[]");
        }
    }
}
