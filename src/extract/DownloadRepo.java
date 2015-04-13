package extract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Buildmodel;

import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.revwalk.RevCommit;

import detection.Detect;
import settings.RepoSettings;

public class DownloadRepo {

	Git git;
	
	public void cloneRepo(String username, String reponame) throws InvalidRemoteException, TransportException, GitAPIException, IOException, ClassNotFoundException{
		
		CloneCommand clonecommand = Git.cloneRepository();
		RepoSettings rs = new RepoSettings(username,reponame,true);
		File f = new File(rs.RepositoryPath);
		
		clonecommand.setDirectory(f);
		clonecommand.setBare(true);
		String URI = "https://github.com/" + username + "/" +  reponame + ".git";
		clonecommand.setURI(URI);
		
		//Cloning the Bare Repo
		git = clonecommand.call(); 
		
		Iterable<RevCommit> logsreverse = git.log().all().call(); //Latest top oldest last

	    List<RevCommit> logs = new ArrayList<RevCommit>();
	    for(RevCommit rev: logsreverse){
	    	logs.add(rev);
        }
	    Collections.reverse(logs); //Latest last ldest top
	    
	    if(logs.size() == 0) {
	    	
	    }
	    
	    Buildmodel bm = new Buildmodel();
		int numofcommits = bm.build(git,logs, rs); //How many commits written, i.e not merge commits in this
		
		Detect detect = new Detect();
		detect.detect(rs, numofcommits);
		
	}
	
	public void pullRepo(String username, String reponame) throws IOException, InvalidRemoteException, TransportException, GitAPIException, ClassNotFoundException {
		
		RepoSettings rs = new RepoSettings(username,reponame,false);
		File f = new File(rs.RepositoryPath);
		
		git = new Git(new FileRepository(f));	
		
		Iterable<RevCommit> logsreverseold = git.log().all().call();
		List<RevCommit> logsold = new ArrayList<RevCommit>();
	    for(RevCommit rev: logsreverseold){
	    	logsold.add(rev);
        }
	    
		FetchCommand fetch = git.fetch();
		
		//Pulling the Repo, since its Bare Repo only Fetch is sufficient, merging not required
		fetch.call();
		
		Iterable<RevCommit> logsreverse = git.log().all().call();

	    List<RevCommit> logs = new ArrayList<RevCommit>();
	    for(RevCommit rev: logsreverse){
	    	logs.add(rev);
        }
	    
	    logs = (List<RevCommit>) CollectionUtils.subtract(logs, logsold);
	    Collections.reverse(logs);
	    
	    if(logs.size() == 0) {
	    	
	    }
	      
		Buildmodel bm = new Buildmodel();
		int numofcommits = bm.build(git,logs, rs);
		
		Detect detect = new Detect();
		detect.detect(rs, numofcommits);
		
	}
}
