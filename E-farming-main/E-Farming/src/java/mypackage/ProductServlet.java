package mypackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;


/**
 *
 * @author ASUS
 */
public class ProductServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();

        Connection con = null;
        PreparedStatement stmt = null;
        PreparedStatement updateStmt = null; // Declare updateStmt variable

        String url = "jdbc:mysql://127.0.0.1:3306/register";
        String user = "root";
        String pw = "Prasad@7006";

        String pname = req.getParameter("name");
        String price = req.getParameter("price");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String add = req.getParameter("address");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pw);
            String query = "insert into product values(?,?,?,?,?)";
            stmt = con.prepareStatement(query);

            stmt.setString(1, pname);
            stmt.setString(2, price);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, add);
            stmt.executeUpdate();
            
            // Prepare and execute the update statement for inventory
            String inventoryUpdateQuery = "UPDATE inventory SET quantity = quantity - 1 WHERE name = ?";
            updateStmt = con.prepareStatement(inventoryUpdateQuery);
            updateStmt.setString(1, pname);
            updateStmt.executeUpdate();

            res.sendRedirect("thanks.jsp");
        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            try {
                if (updateStmt != null) {
                    updateStmt.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                out.println("Error in closing resources: " + ex);
            }
        }
    }
}
