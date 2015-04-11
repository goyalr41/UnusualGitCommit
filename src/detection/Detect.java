package detection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ml.DataStatistics;
import ml.FileTypeStatistics;

import org.apache.commons.io.FileUtils;

import settings.RepoSettings;

public class Detect {
	
	public double round2(double d) { //For rounding Decimal values
	     return Math.round(d * 10000) / 10000.0;
	}
	
	public double aggregate(double a, double b) { //Aggregating values of all attributes
		return (a + b - a*b);
	}
	
	public double mapping(double x) { //Mapping function to decrease values 
		return (1-Math.pow((1.0-x),(1.0/15.0)));
	}
	
	public ResultDatastr resultdatanum(double value, double alphaglobal, double betaglobal, double alphaauthor, double betaauthor, boolean exists){
		
		double j;
		if(value != 0) {
			j = (1- Math.pow((alphaglobal/value),betaglobal));
		    if(j < 0) {
		    	j = 0.0;
		    }
		}else {
			j = 0.0;
		}
		 
		
	    double g = mapping(j);
	    
	    ResultDatastr rds = new ResultDatastr();
	    rds.value = (int) value;
	    rds.globalorg = j;
	    rds.globalmapped = g;
	     
	    if(exists) {
	    	if(value != 0) {
	    		j = (1- Math.pow((alphaauthor/value),betaauthor));
	    		if(j < 0) {
	    			j = 0.0; //As lower values are not bad, they are kind of normal
	    		}
	    	}else {
	    		j = 0.0;
	    	}
	    }else {
	    	j = 0.5; //If author profile doesn't exists, we assume data is at 50% i.e. median.
	    }
	     
	    g = mapping(j);
	     
	    rds.authororg = j;
	    rds.authormapped = g;
	     
	    return rds;
	     
	}
	
	@SuppressWarnings("unused")
	public void detect(RepoSettings rs, List<String> Commitids) throws IOException{
		
		File repo_global = new File(rs.Datapath + "//Global//Training_data.tsv");
		List<String> CommitsData = FileUtils.readLines(repo_global);
		CommitsData.remove(0);
		Collections.reverse(CommitsData);
		
		File file_global_matrix = new File(rs.Datapath + "//Global//Training_filestypescountcommit.tsv");
		List<String> CommitsFileData = FileUtils.readLines(file_global_matrix);
		Collections.reverse(CommitsFileData);
		
		WriteResult wr = new WriteResult();
		wr.initiate(rs);
		
		DataStatistics ds = new DataStatistics();
		ds.initiate(rs);
		ds.calcglobal();
		
		FileTypeStatistics fts = new FileTypeStatistics();
		fts.initiate(rs,ds);
		fts.calcglobal();
		
		int len = Commitids.size();
		for(int i = 0; i < len; i++) {
			
			String[] commitdata = CommitsData.get(i).split("\t");
			
			String commitid = commitdata[0];
			String email = commitdata[1];
			int totallinechanged = Integer.parseInt(commitdata[2]);
			int ovllineadd = Integer.parseInt(commitdata[3]);
            int ovllinerem = Integer.parseInt(commitdata[4]);
            int totalfilchan = Integer.parseInt(commitdata[5]);
            int totalfiladd = Integer.parseInt(commitdata[6]); 
            int totalfilrem = Integer.parseInt(commitdata[7]);
            int commsgsize = Integer.parseInt(commitdata[8]);
            int tiofcommit = Integer.parseInt(commitdata[9]);
            
            String[] commitfiledata = CommitsFileData.get(i).split("\t");
            String[] filetype = commitfiledata[1].split(",");
            List<String> filetypes = new ArrayList<String>();
            
            for(String filtyp : filetype) {
            	filetypes.add(filtyp);
            }
            
			boolean exists = false;
			
			File authtime = new File(rs.Datapath +"//Author//"+ email +"_train.tsv"); 

			if(authtime.exists()) {
				 List<String> sizech = FileUtils.readLines(authtime);  
			     if(sizech.size() > 20) {  //Author profile is built only when he has done more than 20 commits.
			    	 exists = true;
			    	 ds.calculateauthor(email);
			     }
	    	}
			
			ResultDatastr totalloc = resultdatanum(totallinechanged, ds.totallocalpha, ds.totallocbeta, ds.totallocauthalpha, ds.totallocauthbeta, exists);
			ResultDatastr locadded = resultdatanum(ovllineadd, ds.locaddalpha, ds.locaddbeta, ds.locaddauthalpha, ds.locaddauthbeta, exists);
			ResultDatastr locremoved = resultdatanum(ovllinerem, ds.locremalpha, ds.locrembeta, ds.locremauthalpha, ds.locremauthbeta, exists);

			ResultDatastr totalfilechanged = resultdatanum(totalfilchan, ds.nofalpha, ds.nofbeta, ds.nofauthalpha, ds.nofauthbeta, exists);
			ResultDatastr totalfileadded = resultdatanum(totalfiladd, ds.nofaddalpha, ds.nofaddbeta, ds.nofaddauthalpha, ds.nofaddauthbeta, exists);
			ResultDatastr totalfileremoved = resultdatanum(totalfilrem, ds.nofremalpha, ds.nofrembeta, ds.nofremauthalpha, ds.nofremauthbeta, exists);

			ResultDatastr commitmsg = resultdatanum(commsgsize, ds.commsgalpha, ds.commsgbeta, ds.commsgauthalpha, ds.commsgauthbeta, exists);

			ResultDatastr timeofcommit = resultdatanum(tiofcommit, ds.timeauthalpha, 0.0, ds.timeauthalpha, ds.timeauthbeta, exists); //As time is only for author, so global value is 0

		}
		
		
	}
}
