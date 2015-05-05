package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
				//System.out.println(res.get(commitid).Decision);
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
				return "Cloning '" + reponame + "' Repository!";
			}else if(s.get(0).startsWith("Building")) {
				if(s.get(0).split(" ").length >= 2) {
					return "Building Model for " + s.get(0).split(" ")[1] + " Commits!";
				}
			}else if(s.get(0).equals("Detecting")) {
				return "Detecting Commits!";
			}else if(s.get(0).equals("Completed")) {
				return "Completed!";
			}else if(s.get(0).equals("Completed2")) {
				return "Completed!";
			}else if(s.get(0).equals("Fetching")) {
				return "Fetching '" + reponame + "' Repository!";
			}else {
				return "Nothing";
			}
		}
		return "Nothing";
	}
	
	public List<CommitOut> randomcommits(String username, String reponame) throws IOException, ClassNotFoundException{
		RepoSettings rs = new RepoSettings(username,reponame,false);
		CommitResultSerializer crs = new CommitResultSerializer();
		crs.initiate(rs);
		Map<String,CommitResultObject> res = crs.read();
		
		Random random = new Random();
		List<String> keys = new ArrayList<String>(res.keySet());
	
		List<CommitOut> commitres = new ArrayList<CommitOut>();
		List<String> commit0to5 = new ArrayList<String>();
		List<String> commit5to7 = new ArrayList<String>();
		List<String> commit7to8 = new ArrayList<String>();
		List<String> commit8to9 = new ArrayList<String>();
		List<String> commit9to95 = new ArrayList<String>();
		List<String> commit95to1 = new ArrayList<String>();
		
		for(String commitkey: res.keySet()) {
			double dval = res.get(commitkey).Decisionval;
			if(dval < 0.5) {
				commit0to5.add(commitkey);
			}else if(dval >= 0.5 && dval < 0.7) {
				commit5to7.add(commitkey);
			}else if(dval >= 0.7 && dval < 0.8){
				commit7to8.add(commitkey);
			}else if(dval >= 0.8 && dval < 0.9){
				commit8to9.add(commitkey);
			}else if(dval >= 0.9 && dval < 0.95){
				commit9to95.add(commitkey);
			}else {
				commit95to1.add(commitkey);
			}
		}
		
		int len95to1 = 2;
		int len9to95 = 2;
		int len8to9 = 2;
		int len7to8 = 2;
		int len5to7 = 1;
		int len0to5 = 1;
		
		Set<String> setofcommits = new HashSet<String>();
		
		if(commit95to1.size()!=0) {
			while(len95to1 != 0) {
				setofcommits.add(commit95to1.get(random.nextInt(commit95to1.size())));
				len95to1--;
			}
		}
		
		if(commit9to95.size()!=0) {
			while(len9to95 != 0) {
				setofcommits.add(commit9to95.get(random.nextInt(commit9to95.size())));
				len9to95--;
			}
		}
		
		if(commit8to9.size()!=0) {
			while(len8to9 != 0) {
				setofcommits.add(commit8to9.get(random.nextInt(commit8to9.size())));
				len8to9--;
			}
		}
		
		if(commit7to8.size()!=0) {
			while(len7to8 != 0) {
				setofcommits.add(commit7to8.get(random.nextInt(commit7to8.size())));
				len7to8--;
			}
		}
		
		if(commit5to7.size()!=0) {
			while(len5to7 != 0) {
				setofcommits.add(commit5to7.get(random.nextInt(commit5to7.size())));
				len5to7--;
			}
		}
		
		if(commit0to5.size()!=0) {
			while(len0to5 != 0) {
				setofcommits.add(commit0to5.get(random.nextInt(commit0to5.size())));
				len0to5--;
			}
		}
		
		if(keys.size() >= 40) {
			while(setofcommits.size() != 10) {
				setofcommits.add(keys.get(random.nextInt(keys.size())));
			}
		}

		List<String> setofcommits1 = new ArrayList<String>();
		for(String commitid: setofcommits) {
			setofcommits1.add(commitid);
		}
		
		Collections.shuffle(setofcommits1);

		for(String commitid: setofcommits1) {
			CommitOut co = new CommitOut();
			co.commitid = commitid;
			if(res.containsKey(commitid)) {
				co.result = res.get(commitid).Decision;
				co.Decisionval = res.get(commitid).Decisionval+"";
				co.Reason = res.get(commitid).Reason;
				co.username = username;
				co.reponame = reponame;
				co.reasonlist = res.get(commitid).reasonlist;
				co.numofcommits = keys.size()+"";
			}
			commitres.add(co);
		}
	    return commitres;
	}
}
