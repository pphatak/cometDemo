package org.cometd.tutorials;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ChatServlet extends HttpServlet {
    public void init() throws ServletException {
        new ProcessMessageService(getServletContext());
    }
}
