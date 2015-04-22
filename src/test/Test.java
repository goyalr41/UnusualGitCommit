package test;


import java.io.IOException;

import ml.DataStatistics;

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
		dr.cloneRepo("ckaestne", "TypeChef");
		
	}
}
