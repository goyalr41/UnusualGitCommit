package html;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Htmldata {
	
	public String workingDir = "";
	public String HtmlfilesPath = "";
	public String username = "";
	public String reponame = "";
	//Add/Change it in Server.xml as well. <Context part in server.xml>
	public String HtmlFolderPath = "C://apache-tomcat-7.0.59-windows-x64//apache-tomcat-7.0.59//webapps//unusualenhanced";
	
	public void initiate(String username1, String reponame1) {
		 workingDir = System.getProperty("user.dir");		 
		 workingDir = workingDir.replace("\\", "//");		 
		 HtmlfilesPath = workingDir + "//src//html//";
		 username = username1;
		 reponame = reponame1;
	}

	public void addheader(StringBuilder sb) throws IOException {
		File header = new File( HtmlfilesPath + "header.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public void addfooter(StringBuilder sb) throws IOException {
		File header = new File( HtmlfilesPath + "footer.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public void addend(StringBuilder sb) throws IOException {
		File header = new File( HtmlfilesPath + "end.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public void addtag(StringBuilder sb, String[] fil) throws IOException {
		File header = new File(HtmlfilesPath + "data.txt");
		String heade = FileUtils.readFileToString(header);
		heade = heade.replace("$author", username);
		heade = heade.replace("$repository", reponame);
		int i = 0;
		heade = heade.replace("$id", fil[i++]);
		heade = heade.replace("$email",fil[i++]);
		heade = heade.replace("$locvalue",fil[i++]);
		heade = heade.replace("$locglb",fil[i++]);
		heade = heade.replace("$locauth",fil[i++]);
		heade = heade.replace("$locaddvalue",fil[i++]);
		heade = heade.replace("$locaddglb",fil[i++]);
		heade = heade.replace("$locaddauth",fil[i++]);
		heade = heade.replace("$locremvalue",fil[i++]);
		heade = heade.replace("$locremglb",fil[i++]);
		heade = heade.replace("$locremauth",fil[i++]);
		heade = heade.replace("$nofvalue",fil[i++]);
		heade = heade.replace("$nofglb",fil[i++]);
		heade = heade.replace("$nofauth",fil[i++]);
		heade = heade.replace("$nofaddvalue",fil[i++]);
		heade = heade.replace("$nofaddauth",fil[i++]);
		heade = heade.replace("$nofremvalue",fil[i++]);
		heade = heade.replace("$nofremauth",fil[i++]);
		//heade = heade.replace("$tofvalue",fil[i++]);
		//heade = heade.replace("$tofglb",fil[i++]);
		//heade = heade.replace("$tofauth",fil[i++]);
		heade = heade.replace("$commsgvalue", fil[i++]);
		heade = heade.replace("$commmsgglb", fil[i++]);
		heade = heade.replace("$commmsgauth", fil[i++]);
		heade = heade.replace("$tocvalue",fil[i++]);
		heade = heade.replace("$tocauth",fil[i++]);  
		heade = heade.replace("$filepervalue",fil[i++]);
		heade = heade.replace("$fileperglb",fil[i++]);
		heade = heade.replace("$filecommitvalue",fil[i++]);
		heade = heade.replace("$filecommitglb",fil[i++]);
		heade = heade.replace("$filecombivalue",fil[i++]);
		heade = heade.replace("$filecombiglb",fil[i++]);
		heade = heade.replace("$filecombiratiovalue",fil[i++]);
		heade = heade.replace("$filecombiratioglb",fil[i++]);
		heade = heade.replace("$authfpervalue",fil[i++]);
		heade = heade.replace("$fileperauth",fil[i++]);
		heade = heade.replace("$authfcommitvalue",fil[i++]);
		heade = heade.replace("$filecommitauth",fil[i++]);
		heade = heade.replace("$authfcombivalue",fil[i++]);
		heade = heade.replace("$filecombiauth",fil[i++]);
		heade = heade.replace("$authfcombiratiovalue",fil[i++]);
		heade = heade.replace("$filecombiratioauth",fil[i++]);
		heade = heade.replace("$decision",fil[i++]);
		heade = heade.replace("$decvalue",fil[i++]);
		//heade = heade.replace("$comment",fil[i++]);
		
		sb.append(heade); 
		
	}
	
	public void createhtml(File data) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		addheader(sb);
		List<String> h = FileUtils.readLines(data);
		h.remove(0);
		for(String dat: h) {
			String[] fil = dat.split("\t");
			addtag(sb,fil);
		}
		addfooter(sb);
		addend(sb);
		File HtmlFile = new File(HtmlFolderPath+ "//" + username + reponame + ".html");
		FileUtils.writeStringToFile(HtmlFile, sb.toString());
		return;
	}
	
	/*public static void main(String[] args) throws IOException {
		
	
		//call(res);
		/*StringBuilder sb = new StringBuilder();
		addheader(sb);
		String[] fil = new String[26];
		for(int i = 0; i < 26; i++) {
			fil[i] = "check";
		}
		addtag(sb,fil);
		addtag(sb,fil);
		addfooter(sb);
		addend(sb);
		File HtmlFile = new File("C:\\Users\\Raman Workstation\\workspace\\UnusualCommits\\src\\html\\htmlfile.html");
		FileUtils.writeStringToFile(HtmlFile, sb.toString());*/
		
	//}
	
}
