/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package my.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author ADMIN
 */
public class DeleteServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getParameter("id");

        try (PrintWriter out = response.getWriter()) {
            // Kiểm tra id có hợp lệ hay không
            if (id == null || id.isEmpty()) {
                out.println("<h2>Id không hợp lệ</h2>");
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://PC302;databaseName=demodb", "sa", "sa")) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                System.out.println("Driver ok");

                // Chuẩn bị câu lệnh SQL
                String sql = "DELETE FROM users WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, id); // Đặt giá trị cho tham số

                    int kq = ps.executeUpdate();
                    if (kq > 0) {
                        out.println("<h2>Xóa user thành công</h2>");
                    } else {
                        out.println("<h2>Xóa user thất bại</h2>");
                    }
                }
            } catch (Exception e) {
                System.out.println("Lỗi: " + e.toString());
                out.println("<h2>Xóa user thất bại do lỗi hệ thống</h2>");
            }

            // Chuyển hướng lại trang ViewServlet để xem danh sách sau khi xóa
            response.sendRedirect("ViewServlet");
        } catch (Exception e) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "DeleteServlet";
    }
}