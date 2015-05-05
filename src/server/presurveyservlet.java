package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import settings.Settings;

/**
 * Servlet implementation class presurveyservlet
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/presurvey" })
public class presurveyservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String PreSurveyPath;
	public static File presurveypath;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public presurveyservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		new Settings();
		PreSurveyPath = Settings.UserDir + "//SurveyData//PreSurvey";
		presurveypath = new File(PreSurveyPath);
		presurveypath.mkdirs();	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
		surveyclass suc = mapper.readValue(json, surveyclass.class);
		
		File presurvdat = new File(presurveypath + "//" + suc.participantid + "presurvey.tsv");
		if(!presurvdat.exists()) {
			presurvdat.createNewFile();
		}
		FileWriter fr = new FileWriter(presurvdat,true);
		fr.append("PID: "+suc.participantid + "\n" + suc.data); //"\n" is in client
		fr.flush();
		fr.close();
		
		//mapper.writeValue(response.getOutputStream(), idc);
	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");	}

}
