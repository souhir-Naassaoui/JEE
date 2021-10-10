package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ListeClients")
public class ListeClients extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ATT_CLIENT = "client";
	public static final String ATT_FORM = "form";

	public static final String VUE = "/WEB-INF/listeClients.jsp";

	public ListeClients() {
		super();

	}

	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
       
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
