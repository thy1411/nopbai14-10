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
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author ADMIN
 */
public class ViewServlet extends HttpServlet {

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
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PrintWriter out = response.getWriter(); // Trả về nội dung response
        StringBuilder data = new StringBuilder();
        
        try {
            //1. nạp driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                //System.out.println("Nap Driver ok");
                //2. thiết lập kết nối
                conn = DriverManager.getConnection("jdbc:sqlserver://PC302;databaseName=demodb", "sa", "sa");
                //System.out.println("Ket noi ok");
            
            ps = conn.prepareStatement("SELECT * FROM users");
            rs = ps.executeQuery();
            
            data.append("<table border='1'>");
            data.append("<tr><th>Id</th><th>Name</th><th>Password</th><th>Email</th><th>Country</th><th>Edit</th><th>Delete</th></tr>");
            
            while (rs.next()) {
                data.append("<tr>");
                data.append("<td>").append(rs.getInt("id")).append("</td>");
                data.append("<td>").append(rs.getString("name")).append("</td>");
                data.append("<td>").append(rs.getString("password")).append("</td>");
                data.append("<td>").append(rs.getString("email")).append("</td>");
                data.append("<td>").append(rs.getString("country")).append("</td>");
                data.append("<td><a href='EditServlet?id=").append(rs.getInt("id")).append("'>Edit</a></td>");
                data.append("<td><a href='DeleteServlet?id=").append(rs.getInt("id")).append("'>Delete</a></td>");
                data.append("</tr>");
            }
            data.append("</table>");
            
            out.println(data.toString()); // Xuất kết quả ra trình duyệt
            
        } catch (Exception e) {
            out.println("<h2>Thêm user thất bại</h2>");
            e.printStackTrace(out);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace(out);
            }
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
        return "ViewServlet to display users";
    }
}
