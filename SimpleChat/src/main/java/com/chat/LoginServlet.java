package com.chat;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");

        if (username == null || username.trim().isEmpty()) {
            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                "<h3>Please enter a username</h3>" +
                "<a href='login.jsp'>Go Back</a>"
            );

            return;
        }

        username = username.trim();

        // Create session
        HttpSession session = request.getSession(true);
        session.setAttribute("username", username);

        // Redirect to chat page
        response.sendRedirect(
            request.getContextPath() + "/chat.jsp"
        );
    }
}