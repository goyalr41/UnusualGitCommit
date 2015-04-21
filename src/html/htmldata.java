package html;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import settings.Settings;

public class htmldata {
	public static void addheader(StringBuilder sb) throws IOException {
		File header = new File(Settings.Htmlpath + "header.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public static void addfooter(StringBuilder sb) throws IOException {
		File header = new File(Settings.Htmlpath + "footer.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public static void addend(StringBuilder sb) throws IOException {
		File header = new File(Settings.Htmlpath + "end.txt");
		String heade = FileUtils.readFileToString(header);
		sb.append(heade); 
	}
	
	public static void addtag(StringBuilder sb, String[] fil) throws IOException {
		File header = new File(Settings.Htmlpath + "data.txt");
		String heade = FileUtils.readFileToString(header);
		heade = heade.replace("$author", Settings.owner);
		heade = heade.replace("$repository", Settings.repo);
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
	
	public static void call(File data) throws IOException {
		StringBuilder sb = new StringBuilder();
		addheader(sb);
		List<String> h = FileUtils.readLines(data);
		h.remove(0);
		/*for(String dat: h) {
			System.out.println(dat);
		}*/
		for(String dat: h) {
			String[] fil = dat.split("\t");
			addtag(sb,fil);
		}
		addfooter(sb);
		addend(sb);
		File HtmlFile = new File(Settings.Resultpath + Settings.Repositoryname + "//result.html");
		FileUtils.writeStringToFile(HtmlFile, sb.toString());
	}
	
	public static void main(String[] args) throws IOException {
		Settings s = new Settings();
		Settings.Repositorypath = "C://Users//Raman Workstation//Documents//GitHub//node.git";
		Settings.Repositoryname = "Nodefin";
		Settings.owner = "joyent";
		Settings.repo = "node";
		s.initiate();
		
		File res = new File(Settings.Resultpath + Settings.Repositoryname +   "//result.tsv");
		call(res);
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
		
	}
	
}
