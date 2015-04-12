package test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import control.Control;
import extract.DownloadRepo;
import settings.RepoSettings;
import settings.Settings;

public class Test {
	public static void main(String args[]) throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		new Settings();
		DownloadRepo dr = new DownloadRepo();
		dr.cloneRepo("chjj", "marked");
		
	}
}
