package ml;

import java.io.IOException;

import settings.RepoSettings;

public class FileTypeStatistics {
	
	RepoSettings rs;
	DataStatistics ds;
	
	public void initiate(RepoSettings rs1, DataStatistics ds1) throws IOException{
		rs = rs1;
		ds = ds1;
	}
	
	public void calcglobal() {
		
	}
}
