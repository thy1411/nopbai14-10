package my.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra và chuyển đổi 'id' từ request parameter
        String idStr = request.getParameter("id");
        int id = 0;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            // Nếu không phải là số nguyên hợp lệ, báo lỗi và dừng lại
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<h2>Invalid User ID</h2>");
            }
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                // Kết nối với cơ sở dữ liệu
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection("jdbc:sqlserver://PC302;databaseName=demodb", "sa", "sa");

                // Tạo câu lệnh truy vấn
                ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
                ps.setInt(1, id);

                // Thực hiện truy vấn và lấy kết quả
                rs = ps.executeQuery();

                // Nếu tìm thấy user
                if (rs.next()) {
                    out.println("<h1>Edit User</h1>");
                    out.println("<form action='UpdateServlet' method='POST'>");
                    out.println("<input type='hidden' name='id' value='" + id + "' />");

                    // Sử dụng <table> để căn chỉnh ô văn bản trong bảng
                    out.println("<table style='border-collapse: collapse;'>");
                    out.println("<tr><td style='padding: 8px;'><label>Name:</label></td>");
                    out.println("<td style='padding: 8px;'><input type='text' name='uname' value='" + rs.getString("name") + "' /></td></tr>");

                    out.println("<tr><td style='padding: 8px;'><label>Password:</label></td>");
                    out.println("<td style='padding: 8px;'><input type='password' name='upass' value='" + rs.getString("password") + "' /></td></tr>");

                    out.println("<tr><td style='padding: 8px;'><label>Email:</label></td>");
                    out.println("<td style='padding: 8px;'><input type='email' name='uemail' value='" + rs.getString("email") + "' /></td></tr>");

                    out.println("<tr><td style='padding: 8px;'><label>Country:</label></td>");
                    out.println("<td style='padding: 8px;'><input type='text' name='country' value='" + rs.getString("country") + "' /></td></tr>");
                    out.println("</table>");
                    
                    out.println("<input type='submit' value='Update' style='margin-top: 10px;' />");
                    out.println("</form>");
                } else {
                    out.println("<h2>User not found</h2>");
                }

            } catch (Exception e) {
                out.println("<h2>Error: " + e.getMessage() + "</h2>");
            } finally {
                // Đảm bảo đóng ResultSet, PreparedStatement, và Connection
                if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
                if (ps != null) try { ps.close(); } catch (Exception e) { e.printStackTrace(); }
                if (conn != null) try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
