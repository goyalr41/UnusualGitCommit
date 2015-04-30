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
	
	public double round2(double d) { //For rounding Decimal values
	     return Math.round(d * 10000) / 10000.0;
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
	
	public void write(String commitid, String email, 
			ResultDatastr totalloc, ResultDatastr locadded, ResultDatastr locremoved, 
			ResultDatastr totalfilechanged, ResultDatastr totalfileadded, ResultDatastr totalfileremoved, 
			ResultDatastr commitmsg, ResultDatastr timeofcommit, 
			ResultDatastr filpercentchan, ResultDatastr filpercommit, 
			ResultDatastr combfrequency, ResultDatastr combprobability, 
			String Decision, Double Decisionval) throws IOException{
		
		File resultfile = new File(rs.Resultpath+"//result.tsv");
		if(!resultfile.exists()){
			createresultfile(resultfile);
		}
		
		FileWriter writer = new FileWriter(resultfile,true); 
		
		writer.append(commitid);
		int len = email.length();
		int minval = Math.min(len,7);
		writer.append("\t" + email.substring(0, minval));
		
		writer.append("\t" + totalloc.value);
		writer.append("\t" + round2(totalloc.globalorg) + " , " + round2(totalloc.globalmapped));
		writer.append("\t" + round2(totalloc.authororg) + " , " + round2(totalloc.authormapped));
		writer.append("\t" + locadded.value);
		writer.append("\t" + round2(locadded.globalorg) + " , " + round2(locadded.globalmapped));
		writer.append("\t" + round2(locadded.authororg) + " , " + round2(locadded.authormapped));
		writer.append("\t" + locremoved.value);
		writer.append("\t" + round2(locremoved.globalorg) + " , " + round2(locremoved.globalmapped));
		writer.append("\t" + round2(locremoved.authororg) + " , " + round2(locremoved.authormapped));
		writer.append("\t" + totalfilechanged.value);
		writer.append("\t" + round2(totalfilechanged.globalorg) + " , " + round2(totalfilechanged.globalmapped));
		writer.append("\t" + round2(totalfilechanged.authororg) + " , " + round2(totalfilechanged.authormapped));
		
		writer.append("\t" + totalfileadded.value);
		writer.append("\t" + round2(totalfileadded.authororg) + " , " + round2(totalfileadded.authormapped));
		writer.append("\t" + totalfileremoved.value);
		writer.append("\t" + round2(totalfileremoved.authororg) + " , " + round2(totalfileremoved.authormapped));
		
		writer.append("\t" + commitmsg.value);
		writer.append("\t" + round2(commitmsg.globalorg) + " , " + round2(commitmsg.globalmapped));
		writer.append("\t" + round2(commitmsg.authororg) + " , " + round2(commitmsg.authormapped));
		
		writer.append("\t" + timeofcommit.value);
		writer.append("\t" + round2(timeofcommit.authororg) + " , " + round2(timeofcommit.authormapped));
				
		writer.append("\t" + filpercentchan.valuestrglb);
		writer.append("\t" + round2(filpercentchan.globalorg) + " , " + round2(filpercentchan.globalmapped));
		writer.append("\t" + filpercommit.valuestrglb);
		writer.append("\t" + round2(filpercommit.globalorg) + " , " + round2(filpercommit.globalmapped));
		writer.append("\t" + combfrequency.valuestrglb);
		writer.append("\t" + round2(combfrequency.globalorg) + " , " + round2(combfrequency.globalmapped));
		writer.append("\t" + combprobability.valuestrglb);
		writer.append("\t" + round2(combprobability.globalorg) + " , " + round2(combprobability.globalmapped));
		
		writer.append("\t" + filpercentchan.valuestrauth);
		writer.append("\t" + round2(filpercentchan.authororg) + " , " + round2(filpercentchan.authormapped));
		writer.append("\t" + filpercommit.valuestrauth);
		writer.append("\t" + round2(filpercommit.authororg) + " , " + round2(filpercommit.authormapped));
		writer.append("\t" + combfrequency.valuestrauth);
		writer.append("\t" + round2(combfrequency.authororg) + " , " + round2(combfrequency.authormapped));
		writer.append("\t" + combprobability.valuestrauth);
		writer.append("\t" + round2(combprobability.authororg) + " , " + round2(combprobability.authormapped));
		
		writer.append("\t" + Decision);
		writer.append("\t" + round2(Decisionval));
		writer.append("\n");
		
		writer.flush();
		writer.close();

	}
}
