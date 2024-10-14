package my.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int id = 0;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<h2>Invalid User ID</h2>");
                out.println("<meta http-equiv='refresh' content='3;URL=ViewServlet' />");
            }
            return;
        }

        String uname = request.getParameter("uname");
        String upass = request.getParameter("upass");
        String uemail = request.getParameter("uemail");
        String ucountry = request.getParameter("country");

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                // Kết nối tới cơ sở dữ liệu
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection("jdbc:sqlserver://PC302;databaseName=demodb", "sa", "sa");

                // Câu lệnh cập nhật
                ps = conn.prepareStatement("UPDATE users SET name=?, password=?, email=?, country=? WHERE id=?");
                ps.setString(1, uname);
                ps.setString(2, upass);
                ps.setString(3, uemail);
                ps.setString(4, ucountry);
                ps.setInt(5, id);

                int kq = ps.executeUpdate();

                // Kiểm tra kết quả cập nhật và hiển thị thông báo
                if (kq > 0) {
                    out.println("<h2>Updated successfully!</h2>");
                } else {
                    out.println("<h2>Update failed!</h2>");
                }

            } catch (Exception e) {
                out.println("<h2>Error: " + e.getMessage() + "</h2>");
            } finally {
                // Đảm bảo đóng kết nối
                if (ps != null) try { ps.close(); } catch (Exception e) { e.printStackTrace(); }
                if (conn != null) try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
            }

            // Tự động chuyển hướng về ViewServlet sau 3 giây
            out.println("<meta http-equiv='refresh' content='3;URL=ViewServlet' />");
        }
    }
}
