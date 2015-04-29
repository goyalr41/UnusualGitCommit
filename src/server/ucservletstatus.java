package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import control.Control;

/**
 * Servlet implementation class ucservletstatus
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/unusualcommitstatus" })
public class ucservletstatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public ucservletstatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	    String json = "";
	    if(br != null){
	    	json = br.readLine();
	    }
		
		ObjectMapper mapper = new ObjectMapper();
		CommitIn cin = mapper.readValue(json, CommitIn.class);
		
		String username = cin.username;
		String reponame = cin.reponame;
		
		Control cont = new Control();
		String status = cont.getStatus(username, reponame);
		
		response.setContentType("application/json");
		
		StatusClass sc = new StatusClass();
		sc.status = status;
		sc.username = cin.username;
		sc.reponame = cin.reponame;
		mapper.writeValue(response.getOutputStream(), sc);
	}

	
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	}

}
