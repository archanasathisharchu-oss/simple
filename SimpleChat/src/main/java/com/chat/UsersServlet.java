package com.chat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UsersServlet")
public class UsersServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("username") == null) {

            response.sendError(401, "Not logged in");
            return;
        }

        String currentUser =
            (String) session.getAttribute("username");

        response.setContentType("text/html;charset=UTF-8");

        String sql =
            "SELECT username FROM users " +
            "WHERE username <> ? " +
            "ORDER BY username";

        try (
            Connection con = DButil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, currentUser);

            ResultSet rs = ps.executeQuery();

            var out = response.getWriter();

            boolean found = false;

            while (rs.next()) {

                found = true;

                String username =
                    rs.getString("username");

                String firstLetter =
                    username.substring(0, 1).toUpperCase();

                out.println(
                    "<div class='user' " +
                    "onclick=\"selectUser('" +
                    username.replace("'", "\\'") +
                    "')\">" +

                    "<div class='avatar'>" +
                    firstLetter +
                    "</div>" +

                    "<span>" +
                    username +
                    "</span>" +

                    "</div>"
                );
            }

            if (!found) {
                out.println(
                    "<div>No other users found.</div>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                500,
                "Database error: " + e.getMessage()
            );
        }
    }
}