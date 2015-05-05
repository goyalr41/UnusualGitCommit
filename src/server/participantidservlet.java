package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import settings.Settings;

/**
 * Servlet implementation class participantid
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/participantid" })
public class participantidservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String SurveyPath;
	public static int idlock = 0;
	public File idfile;
	public FileWriter fr;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public participantidservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		new Settings();
		SurveyPath = Settings.UserDir + "//SurveyData";
		File surveypath = new File(SurveyPath);
		surveypath.mkdirs();
		idfile = new File(surveypath + "//latestid.txt");
		if(!idfile.exists()) {
			try {
				idfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fr = new FileWriter(idfile,true);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		try {
			fr.flush();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		while(idlock != 0){
			
		}
		idlock = 1;
		List<String> pids = FileUtils.readLines(idfile);
		int latestid;
		if(pids.size() != 0) {
			latestid = Integer.parseInt(pids.get(pids.size()-1));
		}else {
			latestid = 0;
		}
		int lid = latestid+1;
		fr.append(lid+""+"\n");
		fr.flush();
		idlock = 0;
		
		IdClass idc = new IdClass();
		idc.participantid = lid+"";
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), idc);

	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	}

}
