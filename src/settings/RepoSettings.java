package settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class RepoSettings {
	
	public String RepositoryPath = "";
	public String ModelPath = "";
	public String Datapath = "";
	public String Resultpath = "";
	public String Statspath = "";
	public String Progress = "";
	
	public RepoSettings(String username, String reponame, boolean clone) throws IOException {
		 
		 String repoworkingDir = Settings.RepositoriesPath;
		 String modelworkingDir = Settings.ModelsPath;
		
		 RepositoryPath = repoworkingDir + "//" + username + "//" + reponame;
		 if(clone){
			 File repopath = new File(RepositoryPath);
			 FileUtils.deleteDirectory(repopath);
			 repopath.mkdirs();
		 }
		 
		 ModelPath = modelworkingDir + "//" + username + "//" + reponame;
		 if(clone){
			 File modelpath = new File(ModelPath);
			 FileUtils.deleteDirectory(modelpath);
			 modelpath.mkdirs();
		 }
		 
		 Datapath = ModelPath + "//Data";
		 if(clone){
			 File datapath = new File(Datapath);
			 FileUtils.deleteDirectory(datapath);
			 datapath.mkdirs();
		 }
		 
		 Resultpath = ModelPath + "//Results";
		 if(clone){
			 File resultpath = new File(Resultpath);
			 FileUtils.deleteDirectory(resultpath);
			 resultpath.mkdirs();
		 }
		 
		 Statspath = ModelPath + "//Stats";
		 if(clone){
			 File statspath = new File(Statspath);
			 FileUtils.deleteDirectory(statspath);
			 statspath.mkdirs();
		 }
		 
		 Progress = ModelPath + "//" + "Progress.txt";
		 if(clone){
			 File progress = new File(Progress);
			 progress.getParentFile().mkdirs();
			 progress.createNewFile();
			 FileWriter p = new FileWriter(progress);
			 p.append("Nothing Done");
			 p.flush();
			 p.close();
		 }
		 
	}
	
	
}
