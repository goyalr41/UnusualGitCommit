package control;

import java.io.File;

import settings.Settings;

public class Control {
	public void check(String username, String reponame) {
		
		 String repoworkingDir = Settings.RepositoriesPath;
		 
		 String RepositoryPath = repoworkingDir + username + "//" + reponame +"//";
		 File repopath = new File(RepositoryPath);
		 
		 if(repopath.exists()) {
			 //Calling Pull Command
		 }else {
			 //Calling Clone Command
		 }
	}
}
