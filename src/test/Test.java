package test;

import java.io.IOException;

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
		dr.pullRepo("goyalr41", "githubtry");
		
	}
}
