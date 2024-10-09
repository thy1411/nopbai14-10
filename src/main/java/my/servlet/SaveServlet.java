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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "SaveServlet", urlPatterns = {"/SaveServlet"})
public class SaveServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            //b1. Lấy giá trị tham số từ client
            String uname = request.getParameter("uname");
            String upass = request.getParameter("upass");
            String uemail = request.getParameter("uemail");
            String country = request.getParameter("country");
            //b2. Xử lý yêu cầu ( truy cập CSDL để thêm mới user)
            Connection conn = null;
            PreparedStatement ps = null;

            try {
                //1. nạp driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                //System.out.println("Nap Driver ok");
                //2. thiết lập kết nối
                conn = DriverManager.getConnection("jdbc:sqlserver://PC302;databaseName=demodb", "sa", "sa");
                //System.out.println("Ket noi ok");
                //3.
                ps = conn.prepareStatement("insert into users(name, password, email, country) values (?,?,?,?)");
                ps.setString(1, uname);
                ps.setString(2, upass);
                ps.setString(3, uemail);
                ps.setString(4, country);
              
                //4. thi han truy vấn
                int kq = ps.executeUpdate();
                //5. Xử lý kết quả trả về
                if (kq > 0) {
                    out.print("<h2>Thêm User thành công</h2>");
                } else {
                    out.print("<h2>Thêm thất bại</h2>");
                }
                //6. đóng kết nối
                conn.close();

            } catch (Exception e) {

                System.out.println("Loi:" + e.toString());
                out.print("<h2>Thêm thất bại</h2>");
            }
            //chèn nội dung của trang index.html vào phản hồi kết quả
            request.getRequestDispatcher("index.html").include(request, response);

          
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
