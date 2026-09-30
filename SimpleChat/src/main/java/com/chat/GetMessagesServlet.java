package com.chat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/GetMessagesServlet")
public class GetMessagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String SQL =
        "SELECT sender, receiver, message, created_at " +
        "FROM messages " +
        "WHERE (sender = ? AND receiver = ?) " +
        "OR (sender = ? AND receiver = ?) " +
        "ORDER BY id ASC";

    @Override
    protected void doGet(HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");

        // Check login session
        HttpSession session = req.getSession(false);

        String me = null;

        if (session != null) {
            me = (String) session.getAttribute("username");
        }

        // User is not logged in
        if (me == null || me.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("[]");
            return;
        }

        // Get receiver
        String other = req.getParameter("receiver");

        if (other == null || other.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("[]");
            return;
        }

        other = other.trim();

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        try (
            Connection con = DButil.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL)
        ) {

            ps.setString(1, me);
            ps.setString(2, other);
            ps.setString(3, other);
            ps.setString(4, me);

            try (ResultSet rs = ps.executeQuery()) {

                boolean first = true;

                while (rs.next()) {

                    if (!first) {
                        sb.append(",");
                    }

                    first = false;

                    String sender = rs.getString("sender");
                    String receiver = rs.getString("receiver");
                    String message = rs.getString("message");

                    Timestamp timestamp = rs.getTimestamp("created_at");

                    String time = "";

                    if (timestamp != null) {
                        time = timestamp.toString();
                    }

                    sb.append("{");

                    sb.append("\"sender\":")
                      .append(jsonEscape(sender));

                    sb.append(",\"receiver\":")
                      .append(jsonEscape(receiver));

                    sb.append(",\"message\":")
                      .append(jsonEscape(message));

                    sb.append(",\"time\":")
                      .append(jsonEscape(time));

                    sb.append("}");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            resp.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            resp.getWriter().print(
                "{\"error\":\"Database error\"}"
            );

            return;
        }

        sb.append("]");

        resp.getWriter().print(sb.toString());
    }

    // Convert Java String into safe JSON string
    private String jsonEscape(String value) {

        if (value == null) {
            return "\"\"";
        }

        String escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        return "\"" + escaped + "\"";
    }
}