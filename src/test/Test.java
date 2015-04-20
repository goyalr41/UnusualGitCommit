package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.DataStatistics;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import control.Control;
import detection.CommitResultObject;
import detection.ResultDatastr;
import extract.DownloadRepo;
import settings.RepoSettings;
import settings.Settings;

public class Test {
	public static void main(String args[]) throws InvalidRemoteException, TransportException, GitAPIException, IOException, ClassNotFoundException {
		new Settings();
		//Write to inititate R. As no servlet here
		DataStatistics ds = new DataStatistics();
		ds.init();
		DownloadRepo dr = new DownloadRepo();
		dr.cloneRepo("rails", "rails");
		
	}
}
