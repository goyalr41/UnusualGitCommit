package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import detection.CommitResultObject;
import detection.CommitResultSerializer;
import extract.DownloadRepo;
import server.CommitOut;
import settings.RepoSettings;
import settings.Settings;

public class Control {
	public void check(String username, String reponame, HttpServletResponse response) throws InvalidRemoteException, TransportException, ClassNotFoundException, GitAPIException, IOException {
		
		 String repoworkingDir = Settings.RepositoriesPath;
		 
		 String RepositoryPath = repoworkingDir + "//" + username + "//" + reponame;
		 File repopath = new File(RepositoryPath);
		 
		 if(repopath.exists()) {
			 RepoSettings rs = new RepoSettings(username,reponame,false);
			 File f = new File(rs.Progress);
			 if(f.exists()) {
				 List<String> s = FileUtils.readLines(f);
				 if(!s.isEmpty()){
					 if(!s.get(0).startsWith("Complete")){ //If not complete, clone again
						 DownloadRepo dr = new DownloadRepo();
						 dr.cloneRepo(username, reponame);
						 return;
					 }
				 }else {
					 DownloadRepo dr = new DownloadRepo(); //It can't be empty, clone again
					 dr.cloneRepo(username, reponame);
					 return;
				 }
			 }else {
				 DownloadRepo dr = new DownloadRepo(); //the file should exist, clone again
				 dr.cloneRepo(username, reponame);
				 return; 
			 }
			 DownloadRepo dr = new DownloadRepo();
			 dr.pullRepo(username, reponame);
		 }else {
			 DownloadRepo dr = new DownloadRepo();
			 dr.cloneRepo(username, reponame);
		 }
	}
	
	public List<CommitOut> result(String username, String reponame, List<String> commitids) throws IOException, ClassNotFoundException{
		RepoSettings rs = new RepoSettings(username,reponame,false);
		CommitResultSerializer crs = new CommitResultSerializer();
		crs.initiate(rs);
		Map<String,CommitResultObject> res = crs.read();
		
		List<CommitOut> commitres = new ArrayList<CommitOut>();
		for(String commitid: commitids) {
			CommitOut co = new CommitOut();
			co.commitid = commitid;
			if(res.containsKey(commitid)) {
				System.out.println(res.get(commitid).Decision);
				co.result = res.get(commitid).Decision;
				co.Decisionval = res.get(commitid).Decisionval+"";
				co.Reason = res.get(commitid).Reason;
			}else {
				co.result = "Merge/First Commit";
				co.Reason = "Merge/First Commit";
			}
			commitres.add(co);
		}
	    return commitres;
	}
	
	public String getStatus(String username, String reponame) throws IOException {
		RepoSettings rs = new RepoSettings(username,reponame,false);
		File f = new File(rs.Progress);
		if(f.exists()) {
			List<String> s = FileUtils.readLines(f);
			if(s.isEmpty()){
				return "Writing";
			}
			if(s.get(0).equals("Cloning")){
				return "Cloning " + reponame + " repository";
			}else if(s.get(0).startsWith("Building")) {
				if(s.get(0).split(" ").length >= 2) {
					return "Building Model for " + s.get(0).split(" ")[1] + " commits";
				}
			}else if(s.get(0).equals("Detecting")) {
				return "Detecting Commits";
			}else if(s.get(0).equals("Completed")) {
				return "Completed";
			}else if(s.get(0).equals("Completed2")) {
				return "No Updates, Completed";
			}else if(s.get(0).equals("Fetching")) {
				return "Fetching " + reponame + " repository";
			}else {
				return "Nothing";
			}
		}
		return "Nothing";
	}
}
