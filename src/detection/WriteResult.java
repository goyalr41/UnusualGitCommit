package detection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import settings.RepoSettings;

public class WriteResult {
	
	RepoSettings rs;
	
	public void initiate(RepoSettings rs1) throws IOException{
		rs = rs1;
	}
	
	public void createresultfile(File resultfile) throws IOException {
		
		resultfile.getParentFile().mkdirs();
	     
	    FileWriter writer = new FileWriter(resultfile); 
	        
	    writer.append("Commit id\t");
        writer.append("Author E-mail\t");
        writer.append("LOC Changed\t");
        writer.append("Global LOC?\t");
        writer.append("Author LOC?\t");
        writer.append("LOC Added\t");
        writer.append("Global LOC Added?\t");
        writer.append("Author LOC  Added?\t");
        writer.append("LOC Removed\t");
        writer.append("Global LOC Removed?\t");
        writer.append("Author LOC  Removed?\t");
        writer.append("NOF Changed\t");
        writer.append("Global NOF Change?\t");
        writer.append("Author NOF Changed?\t");
        writer.append("NOF Added\t");
        writer.append("Author NOF Added?\t");
        writer.append("NOF Removed\t");
        writer.append("Author NOF Removed?\t");  
        writer.append("Commit Msg\t");
        writer.append("Global Commit Msg?\t");
        writer.append("Author Commit Msg?\t");
        writer.append("Time of commit\t");
        writer.append("Author time quartile?\t");
        writer.append("File Percent Type\t");
        writer.append("File percent changed\t");
        writer.append("File Per Commit Type\t");
        writer.append("File per commit\t");
        writer.append("File Comb Type\t");
        writer.append("Combination frequency\t");
        writer.append("Unusual Comb Ratio Type\t");
        writer.append("Unusual Comb Ratio\t");
        
        writer.append("Author File Percent Type\t");
        writer.append("Author File percent changed\t");
        writer.append("Author File Per Commit Type\t");
        writer.append("Author File per commit\t");
        writer.append("Author File Comb Type\t");
        writer.append("Author Combination frequency\t");
        writer.append("Author Comb Ratio Type\t");
        writer.append("Author Comb Ratio\t");
       
        writer.append("Decision\t");
        writer.append("Value");
        
        writer.append("\n");
	        
        writer.flush();
	    writer.close();
		
	}
	
	public void write() throws IOException{
		
		File resultfile = new File(rs.Resultpath+"//result.tsv");
		if(!resultfile.exists()){
			createresultfile(resultfile);
		}
		
		FileWriter writer = new FileWriter(resultfile,true); 


	}
}
