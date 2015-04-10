package settings;

import java.io.File;

//Global FileStructure Settings

/*Convention: String on right side of '+' should have "//"*/

public class Settings {
	public static String RepositoriesPath = "";
	public static String ModelsPath = "";
	public static String UserDir = "D://Temporary";  //Directory to Store Data
	
	public Settings() {
		//Path for all Repositories
		RepositoriesPath = UserDir + "//UnusualGitCommits//Repositories";
		File repospath = new File(RepositoriesPath);
		repospath.mkdirs();
		
		//Path for all models
		ModelsPath = UserDir + "//UnusualGitCommits//Models";
		File modelpath = new File(ModelsPath);
		modelpath.mkdirs();
	}
	
	
}
