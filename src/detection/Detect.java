package detection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.DataStatistics;
import ml.FileTypeStatistics;

import org.apache.commons.io.FileUtils;

import settings.RepoSettings;

public class Detect {
	
	public double aggregate(double a, double b) { //Aggregating values of all attributes
		return (a + b - a*b);
	}
	
	public double mapping(double x) { //Mapping function to decrease values 
		return (1-Math.pow((1.0-x),(1.0/15.0)));
	}
	
	public ResultDatastr expocdffitval(double value, double alphaglobal, double betaglobal, double alphaauthor, double betaauthor, boolean exists){
		
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
	
	public ResultDatastr timecheck(int value, boolean exists, FileTypeStatistics fts) {
		ResultDatastr rds = new ResultDatastr();
		rds.value = value;
		
		if(exists) {
			double time0to23prev =  value - 1;
	        if(time0to23prev < 0) {
	        	time0to23prev = time0to23prev + 24.0;
	        }
	        
	        double time0to23next = value + 1;
	        if(time0to23next >= 24) {
	        	time0to23next = time0to23next % 24.0;
	        }
	        
	        //Average over 3 times
	        double timefreq = (fts.authtimemap.get((double)value) + fts.authtimemap.get(time0to23prev) + fts.authtimemap.get(time0to23next))/3;
	        
	        //Only binary values
            if(timefreq < 0.05*(fts.authortotalcommits)) {
            	rds.authororg = (timefreq/(fts.authortotalcommits));
            	rds.authormapped = mapping(0.995); //Taking original value, but mapping is binary
	        }else {
	        	rds.authororg = (timefreq/(fts.authortotalcommits));
            	rds.authormapped = mapping(0.0);
	        }
	    }else {
	    	rds.authororg = 0.5;
        	rds.authormapped = mapping(0.5);
 		}	
		rds.globalorg = 0.0; //As no profile for global
    	rds.globalmapped = mapping(0.0);
		return rds;
	}
	
	public void filetypecheck (List<String> filetypes, FileTypeStatistics fts, boolean exists, ResultDatastr filpercentchan, ResultDatastr filpercommit, ResultDatastr combfrequency, ResultDatastr combprobability){
		Map<String,Long> filecounts = new HashMap<>();
    	//This is for current commit only
    	for(String filetype : filetypes) {
    		if(filecounts.containsKey(filetype)) {
    			filecounts.put(filetype, filecounts.get(filetype) + 1);
    		}else {
    			filecounts.put(filetype, (long)1);
    		}    		
    	}
    	
		List<String> combinations = new ArrayList<String>();
		
    	for(String filetyp: filecounts.keySet()){
    			combinations.add(filetyp);
    	}
    	
    	Collections.sort(combinations);
    	
    	filpercentchan.valuestrglb = "NA";
      	filpercentchan.globalorg = 100.0;
      	filpercommit.valuestrglb = "NA";
    	filpercommit.globalorg = 1.0;
    	filpercentchan.valuestrauth = "NA";
      	filpercentchan.authororg = 100.0;
      	filpercommit.valuestrauth = "NA";
    	filpercommit.authororg = 1.0;
    	
    	for(String filetype : filecounts.keySet()){
    		if(fts.mapfileperchanged.containsKey(filetype)){
    			if(filpercentchan.globalorg   > Math.min(fts.mapfileperchanged.get(filetype), filpercentchan.globalorg  )) {
    				filpercentchan.globalorg   = Math.min(fts.mapfileperchanged.get(filetype), filpercentchan.globalorg);
    				filpercentchan.valuestrglb = filetype;
    			}
    		} else {
    			filpercentchan.globalorg  = 0.0;
    			filpercentchan.valuestrglb = filetype;
    		}
    		
    		if(fts.mapfilecommitchanged.containsKey(filetype)){
    			if(filpercommit.globalorg > Math.min((fts.mapfilecommitchanged.get(filetype)/fts.totalcommits),filpercommit.globalorg)) {
    				filpercommit.globalorg = Math.min((fts.mapfilecommitchanged.get(filetype)/fts.totalcommits),filpercommit.globalorg);
    				filpercommit.valuestrglb = filetype;
    			}
    		} else {
    			filpercommit.globalorg = 0.0;
    			filpercommit.valuestrglb = filetype;
    		}
    		

        	if(exists) {
            	
            	if(fts.mapauthfileperchanged.containsKey(filetype)){
           			if(filpercentchan.authororg   > Math.min(fts.mapauthfileperchanged.get(filetype), filpercentchan.authororg  )) {
           				filpercentchan.authororg   = Math.min(fts.mapauthfileperchanged.get(filetype), filpercentchan.authororg);
           				filpercentchan.valuestrauth = filetype;
           			}
           		} else {
           			filpercentchan.authororg  = 0.0;
           			filpercentchan.valuestrauth = filetype;
           		}
            		
           		if(fts.mapauthfilecommitchanged.containsKey(filetype)){
           			if(filpercommit.authororg > Math.min((fts.mapauthfilecommitchanged.get(filetype)/fts.authortotalcommits),filpercommit.authororg)) {
           				filpercommit.authororg = Math.min((fts.mapauthfilecommitchanged.get(filetype)/fts.authortotalcommits),filpercommit.authororg);
           				filpercommit.valuestrauth = filetype;
           			}
           		} else {
           			filpercommit.authororg = 0.0;
           			filpercommit.valuestrauth = filetype;
           		}
           	}else {
        		filpercentchan.authororg = 100.0;
            	filpercommit.authororg = 1.0;
    			filpercentchan.valuestrauth = "NA";
    			filpercommit.valuestrauth = "NA";
        	}
        	
    	}
    	
    	combfrequency.valuestrglb = "NA";
    	combfrequency.globalorg = 1.0;
    	combprobability.valuestrglb = "NA";
    	combprobability.globalorg = 1.0;
    	combfrequency.valuestrauth = "NA";
    	combfrequency.authororg = 1.0;
    	combprobability.valuestrauth = "NA";
    	combprobability.authororg = 1.0;
    	
    	for(int s = 0; s + 1 < combinations.size(); s++) {
	    	for(int u = s + 1; u < combinations.size(); u++) {
	    		String combkey = combinations.get(s) + "," + combinations.get(u);
	    		
	    		if(fts.twocombinations.containsKey(combkey)){
	    			
	    			List<String> temp1 = fts.twocombinations.get(combkey);
	    			if(combfrequency.globalorg > Math.min(combfrequency.globalorg,((double)temp1.size())/fts.totalcommits)) {
	    				combfrequency.globalorg = Math.min(combfrequency.globalorg,((double)temp1.size())/fts.totalcommits);
	    				combfrequency.valuestrglb = combkey;
	    			}
	    		}else {
	    			combfrequency.globalorg = 0.0;	 
	    			combfrequency.valuestrglb = combkey;
	    		}
	    		
	    		if(fts.meanmap.containsKey(combkey) && fts.sdtmap.containsKey(combkey)) {

	    			double prob;
	    			double chck = Math.log10(filecounts.get(combinations.get(s))/filecounts.get(combinations.get(u)));
	    			
	    			if(chck - fts.meanmap.get(combkey) != 0) {
	    				prob = (Math.pow(fts.sdtmap.get(combkey),2.0))/(Math.pow((chck-fts.meanmap.get(combkey)), 2.0));
	    			}else {
	    				prob = 1.0;
	    			}
	    			
	    			if(combprobability.globalorg > prob) {
	    				combprobability.globalorg = prob;
	    				combprobability.valuestrglb = combkey;
	    			}
	    		}
	    		
	    		if(exists) {
		    		if(fts.authtwocombinations.containsKey(combkey)){
		    			
		    			List<String> temp1 = fts.authtwocombinations.get(combkey);
		    			if(combfrequency.authororg > Math.min(combfrequency.authororg,((double)temp1.size())/fts.authortotalcommits)) {
		    				combfrequency.authororg = Math.min(combfrequency.authororg,((double)temp1.size())/fts.authortotalcommits);
		    				combfrequency.valuestrauth = combkey;
		    			}
		    		}else {
		    			combfrequency.authororg = 0.0;	 
		    			combfrequency.valuestrauth = combkey;
		    		}
		    		
		    		if(fts.authormeanmap.containsKey(combkey) && fts.authorsdtmap.containsKey(combkey)) {
	
		    			double prob;
		    			double chck = Math.log10(filecounts.get(combinations.get(s))/filecounts.get(combinations.get(u)));
		    			
		    			if(chck - fts.authormeanmap.get(combkey) != 0) {
		    				prob = (Math.pow(fts.authorsdtmap.get(combkey),2.0))/(Math.pow((chck-fts.authormeanmap.get(combkey)), 2.0)); //Chebyshev's Inquality
		    			}else {
		    				prob = 1.0;
		    			}
		    			
		    			if(combprobability.authororg > prob) {
		    				combprobability.authororg = prob;
		    				combprobability.valuestrauth = combkey;
		    			}
		    		}
	    		}else {
	    	    	combfrequency.valuestrauth = "NA";
	    	    	combfrequency.authororg = 1.0;
	    	    	combprobability.valuestrauth = "NA";
	    	    	combprobability.authororg = 1.0;
	    		}
	    		
	    	}
		}
    	
    	//if(filpercentchan.globalorg < 0.02){
    	//	filpercentchan.globalmapped = mapping(0.995);
    		filpercentchan.globalmapped = mapping(1.0-(filpercentchan.globalorg/100.0));
		/*}else {
			filpercentchan.globalmapped = mapping(0.0);
		}*/
		
		//if(filpercommit.globalorg < 0.001){ //1 in 1000 commits
		//	filpercommit.globalmapped = mapping(0.995);
    		filpercommit.globalmapped = mapping(1.0-filpercommit.globalorg);
		/*}else {
			filpercommit.globalmapped = mapping(0.0);
		}*/
		
		//if(combfrequency.globalorg < 0.001) { //1 in 1000 commits
		//	combfrequency.globalmapped = mapping(0.995);
    		combfrequency.globalmapped = mapping(1.0-combfrequency.globalorg);
		/*}else {
			combfrequency.globalmapped = mapping(0.0);
		}*/
		
    		combprobability.globalmapped = mapping(1.0-combprobability.globalorg);
    		
    	//if(filpercentchan.authororg < 0.02){
       	//	filpercentchan.authormapped = mapping(0.995);
    		filpercentchan.authormapped = mapping(1.0-(filpercentchan.authororg/100.0));
    	/*}else {
    		filpercentchan.authormapped = mapping(0.0);
   		}*/
    		
    	//if(filpercommit.authororg < 0.01){ //1 in 100 commits
    	//	filpercommit.authormapped = mapping(0.995);
       		filpercommit.authormapped = mapping(1.0-filpercommit.authororg);
   		/*}else {
   			filpercommit.authormapped = mapping(0.0);
   		}*/
    		
    	//if(combfrequency.authororg < 0.01) { //1 in 100 commits
    	//	combfrequency.authormapped = mapping(0.995);
       		combfrequency.authormapped = mapping(1.0-combfrequency.authororg);
   		/*}else {
   			combfrequency.authormapped = mapping(0.0);
   		}*/
    		
        combprobability.authormapped = mapping(1.0-combprobability.authororg);    		

	}
	
	public void detect(RepoSettings rs, int numofcommits) throws IOException, ClassNotFoundException{
		
		File repo_global = new File(rs.Datapath + "//Global//Training_data.tsv");
		List<String> CommitsData = FileUtils.readLines(repo_global);
		CommitsData.remove(0);
		Collections.reverse(CommitsData);
		
		File file_global_matrix = new File(rs.Datapath + "//Global//Training_filestypescountcommit.tsv");
		List<String> CommitsFileData = FileUtils.readLines(file_global_matrix);
		Collections.reverse(CommitsFileData);
		
		DataStatistics ds = new DataStatistics();
		ds.initiate(rs);
		ds.calcglobal();
		
		FileTypeStatistics fts = new FileTypeStatistics();
		fts.initiate(rs,ds);
		fts.calcglobal();
		
		WriteResult wr = new WriteResult();
		wr.initiate(rs);
		
		CommitResultSerializer crs = new CommitResultSerializer();
		crs.initiate(rs);	
		Map<String,CommitResultObject> mscro = new HashMap<String,CommitResultObject>();
		
		Reason r = new Reason();
		
		int len = numofcommits;
		int unusualnum = 0;
		
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
            List<String> filetypes = new ArrayList<String>();
            
            for(int m = 1; m < commitfiledata.length; m++) {
            	String[] filetype = commitfiledata[m].split(",");
            	filetypes.add(filetype[0]);
            }
            
			boolean exists = false;
			
			File authfile = new File(rs.Datapath +"//Author//"+ email +"_train.tsv"); 

			if(authfile.exists()) {
				 List<String> sizech = FileUtils.readLines(authfile);  
			     if(sizech.size() > 20) {  //Author profile is built only when he has done more than 20 commits.
			    	 exists = true;
			    	 ds.calculateauthor(email);
			    	 fts.calcauthor(email);
			     }
	    	}
			
			ResultDatastr totalloc = expocdffitval(totallinechanged, ds.totallocalpha, ds.totallocbeta, ds.totallocauthalpha, ds.totallocauthbeta, exists);
			ResultDatastr locadded = expocdffitval(ovllineadd, ds.locaddalpha, ds.locaddbeta, ds.locaddauthalpha, ds.locaddauthbeta, exists);
			ResultDatastr locremoved = expocdffitval(ovllinerem, ds.locremalpha, ds.locrembeta, ds.locremauthalpha, ds.locremauthbeta, exists);

			ResultDatastr totalfilechanged = expocdffitval(totalfilchan, ds.nofalpha, ds.nofbeta, ds.nofauthalpha, ds.nofauthbeta, exists);
			ResultDatastr totalfileadded = expocdffitval(totalfiladd, ds.nofaddalpha, ds.nofaddbeta, ds.nofaddauthalpha, ds.nofaddauthbeta, exists);
			ResultDatastr totalfileremoved = expocdffitval(totalfilrem, ds.nofremalpha, ds.nofrembeta, ds.nofremauthalpha, ds.nofremauthbeta, exists);

			ResultDatastr commitmsg = expocdffitval(commsgsize, ds.commsgalpha, ds.commsgbeta, ds.commsgauthalpha, ds.commsgauthbeta, exists);

			ResultDatastr timeofcommit = timecheck(tiofcommit, exists, fts); //As time is only for author.

	    	//These all contains the min value from all filetypes
	    	ResultDatastr filpercentchan = new ResultDatastr(); //Percentage of filetype changed
	    	ResultDatastr filpercommit = new ResultDatastr(); //Filetype changed in how many commits
	    	ResultDatastr combfrequency = new ResultDatastr(); //Combination changed in how many commits
	    	ResultDatastr combprobability = new ResultDatastr(); //Combination probability, ratio of files
	    	
	    	filetypecheck(filetypes, fts, exists, filpercentchan, filpercommit, combfrequency, combprobability);
	    	
	    	List<Double> Values = new ArrayList<Double>();
	    	String Decision;
	    	Double Decisionval;
	    	
	    	Values.add(totalloc.globalmapped);
	    	Values.add(totalloc.authormapped);
	    	Values.add(locadded.globalmapped);
	    	Values.add(locadded.authormapped);
	    	Values.add(locremoved.globalmapped);
	    	Values.add(locremoved.authormapped);
	    	Values.add(totalfilechanged.globalmapped);
	    	Values.add(totalfilechanged.authormapped);
	    	
	    	Values.add(totalfileadded.authormapped);
	    	Values.add(totalfileremoved.authormapped);
	    	
	    	Values.add(commitmsg.globalmapped);
	    	Values.add(commitmsg.authormapped);
	    	
	    	Values.add(timeofcommit.authormapped);
	    	
	    	Values.add(filpercentchan.globalmapped);
	    	Values.add(filpercentchan.authormapped);
	    	Values.add(filpercommit.globalmapped);
	    	Values.add(filpercommit.authormapped);
	    	Values.add(combfrequency.globalmapped);
	    	Values.add(combfrequency.authormapped);
	    	Values.add(combprobability.globalmapped);
	    	Values.add(combprobability.authormapped);
	    	
	    	Decisionval = 0.0;
	    	for(Double v: Values){
	    		Decisionval = aggregate(Decisionval, v);
	    	}
	    	
	    	if(Decisionval < 0.8) {
	    		Decision = "Normal";
	    	}else {
	    		Decision = "Unusual";
	    		unusualnum++;
	    	}
	    		    	
	    	wr.write(commitid.substring(0,7), email, totalloc, locadded, locremoved, totalfilechanged, totalfileadded, totalfileremoved, commitmsg, timeofcommit, filpercentchan, filpercommit, combfrequency, combprobability, Decision, Decisionval);
	    	
			CommitResultObject cro = new CommitResultObject(email, totalloc, locadded, locremoved, totalfilechanged, totalfileadded, totalfileremoved, commitmsg, timeofcommit, filpercentchan, filpercommit, combfrequency, combprobability, Decision, Decisionval);
			cro.Reason = r.reasonis(totalloc, locadded, locremoved, totalfilechanged, totalfileadded, totalfileremoved, commitmsg, timeofcommit, filpercentchan, filpercommit, combfrequency, combprobability, Decision, Decisionval);

			mscro.put(commitid.substring(0,7), cro);
		
	    	System.out.println(commitid.substring(0,7));
		}
		crs.write(mscro);
		System.out.println(unusualnum);
	}
}
