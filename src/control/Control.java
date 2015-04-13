package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public void check(String username, String reponame) throws InvalidRemoteException, TransportException, ClassNotFoundException, GitAPIException, IOException {
		
		 String repoworkingDir = Settings.RepositoriesPath;
		 
		 String RepositoryPath = repoworkingDir + "//" + username + "//" + reponame;
		 File repopath = new File(RepositoryPath);
		 
		 if(repopath.exists()) {
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
			}else {
				co.result = "Merge/First Commit";
			}
			commitres.add(co);
		}
	    return commitres;
	}
}
