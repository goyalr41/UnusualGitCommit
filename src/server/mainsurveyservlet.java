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
 * Servlet implementation class mainsurveyservlet
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/mainsurvey" })
public class mainsurveyservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String MainSurveyPath;
	public static File mainsurveypath;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mainsurveyservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		new Settings();
		MainSurveyPath = Settings.UserDir + "//SurveyData//MainSurvey";
		mainsurveypath = new File(MainSurveyPath);
		mainsurveypath.mkdirs();	
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
		
		File mainsurvdat = new File(mainsurveypath + "//" + suc.participantid + "mainsurvey.tsv");
		if(!mainsurvdat.exists()) {
			mainsurvdat.createNewFile();
		}
		FileWriter fr = new FileWriter(mainsurvdat,true);
		fr.append("PID: "+suc.participantid + "\t" + suc.data);  //"\n" is in client
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
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	}

}
