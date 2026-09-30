package com.chat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        // Check session
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(
                "{\"ok\":false,\"error\":\"Not logged in\"}"
            );
            return;
        }

        String sender = (String) session.getAttribute("username");
        String receiver = request.getParameter("receiver");
        String message = request.getParameter("message");

        // Validate input
        if (receiver == null || receiver.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                "{\"ok\":false,\"error\":\"Receiver is missing\"}"
            );
            return;
        }

        if (message == null || message.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                "{\"ok\":false,\"error\":\"Message is empty\"}"
            );
            return;
        }

        receiver = receiver.trim();
        message = message.trim();

        String sql =
            "INSERT INTO messages (sender, receiver, message) " +
            "VALUES (?, ?, ?)";

        try (
            Connection con = DButil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, message);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                response.getWriter().print(
                    "{\"ok\":true}"
                );
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().print(
                    "{\"ok\":false,\"error\":\"Message was not inserted\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            String error = e.getMessage()
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", " ")
                    .replace("\r", " ");

            response.getWriter().print(
                "{\"ok\":false,\"error\":\"" + error + "\"}"
            );
        }
    }
}