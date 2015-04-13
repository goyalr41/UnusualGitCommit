package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.api.errors.GitAPIException;

import settings.Settings;

import com.fasterxml.jackson.databind.ObjectMapper;

import control.Control;

/**
 * Servlet implementation class ucservlet
 */
@WebServlet(asyncSupported = true, description = "Servlet to call Unusual Commits classes", urlPatterns = { "/unusualcommit" })
public class ucservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ucservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init(ServletConfig config) throws ServletException {
		Settings s = new Settings();
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
		List<String> commitids = cin.commitids;
		
		System.out.println(username);
		System.out.println(reponame);
		for(String s: commitids) {
			System.out.println(s);
		}
		
		//Setting response type as JSON
		response.setContentType("application/json");
		
		Control cont = new Control();
		try {
			cont.check(username, reponame);
		} catch (ClassNotFoundException | GitAPIException e) {
			e.printStackTrace();
			System.out.println("Exception in doPost building model");
		}
		
		List<CommitOut> commitres = new ArrayList<CommitOut>();
		try {
			commitres = cont.result(username, reponame, commitids);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Exception in doPost fetching results");
		}
		
		mapper.writeValue(response.getOutputStream(), commitres);
	}
	

	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		
	}

}
