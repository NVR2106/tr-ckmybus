import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;
import com.google.gson.*;

@WebServlet("/SearchStudentServlet")
public class SearchStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/college_bus";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "P@ssw0rd";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        String rollNo = request.getParameter("roll_no");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                String sql = "SELECT * FROM students WHERE roll_no=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, rollNo);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Student s = new Student(
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getString("branch"),
                        rs.getString("year_of_study"),
                        rs.getString("bus_route"),
                        rs.getString("phone"),
                        rs.getString("email")
                    );
                    Gson gson = new Gson();
                    response.getWriter().write(gson.toJson(s));
                } else {
                    response.getWriter().write("null");
                }
            }
        } catch (Exception e) {
            response.getWriter().write("{}");
        }
    }

    // Inner class for JSON mapping
    class Student {
        String roll_no, name, branch, year_of_study, bus_route, phone, email;
        Student(String roll_no, String name, String branch, String year_of_study, String bus_route, String phone, String email){
            this.roll_no = roll_no;
            this.name = name;
            this.branch = branch;
            this.year_of_study = year_of_study;
            this.bus_route = bus_route;
            this.phone = phone;
            this.email = email;
        }
    }
}
