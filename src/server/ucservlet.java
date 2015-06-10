package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ml.DataStatistics;

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
	public static Map<String,Integer> clonestatus;
	public static int Rstatus = 0;
       
    public ucservlet() {
        super();
        // TODO Auto-generated constructor stub
    	new Settings();
		if(Rstatus == 0) {
			clonestatus = new HashMap<String,Integer>();
			DataStatistics ds = new DataStatistics(); //To create REngine thread
			ds.init();
			Rstatus = 1;
		}
    }

	public void init(ServletConfig config) throws ServletException {
	
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
		/*for(String s: commitids) {
			System.out.println(s);
		}*/
		
		//Setting response type as JSON
		response.setContentType("application/json");
		
		Control cont = new Control();
		try {
			while(clonestatus.containsKey("username+reponame")) {
				//System.out.println("Other thread accessing");
			}
			clonestatus.put("username+reponame",1);
			cont.check(username, reponame,response);
			clonestatus.remove("username+reponame");
			
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
