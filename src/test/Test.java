package test;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ml.DataStatistics;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import extract.DownloadRepo;
import settings.Settings;

public class Test {
	public static void main(String args[]) throws InvalidRemoteException, TransportException, GitAPIException, IOException, ClassNotFoundException {
		new Settings();
		//Write to inititate R. As no servlet here
		DataStatistics ds = new DataStatistics();
		ds.init();
		DownloadRepo dr = new DownloadRepo();
		dr.cloneRepo("libgdx", "libgdx");

		
		File f = new File("E://githubrepos.txt");
		List<String> ReposName = FileUtils.readLines(f);
		
		/*for(String reponame: ReposName) {
			String[] temp = reponame.split("/");
			if(temp.length == 2) {
				dr.cloneRepo(temp[0], temp[1]);
			}
		}*/
		
		/*File f = new File("E://githubrepos.txt");
		List<String> CommitsData = FileUtils.readLines(f);
		FileWriter f1 = new FileWriter("E://githubreposfin.txt");
		
		Set<String> s = new HashSet<String>();
		for(String str: CommitsData) {
			s.add(str.toLowerCase());	
		}
		
		for(String str1: s) {
			f1.append(str1 +"\n");
		}

		f1.flush();
		f1.close();*/
		
	}
}
