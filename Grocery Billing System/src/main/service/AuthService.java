package service;

import db.DBConnection;
import java.sql.*;

public class AuthService {

    public static String login(String u, String p) {

        String sql =
            "SELECT role FROM users WHERE LOWER(username)=LOWER(?) AND password=?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) return null;

            ps.setString(1, u);
            ps.setString(2, p);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
