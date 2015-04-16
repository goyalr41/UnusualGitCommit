package detection;

import java.util.HashMap;
import java.util.Map;

public class Reason {
	
	Map<String,String> resmap = new HashMap<String,String>();
	
	public double round2(double d) { //For rounding Decimal values
	     return Math.round(d * 100) / 100.0;
	}
	
	public Reason(){
		resmap.put("totalloc", "Line of code changed is at X% of data for this repository");
		resmap.put("totallocauth", "Line of code changed is at X% of data for the author of commit");
		resmap.put("locadded", "Line of code added is at X% of data for this repository");
		resmap.put("locaddedauth", "Line of code added is at X% of data for the author of commit");
		resmap.put("locremoved", "Line of code removed is at X% of data for this repository");
		resmap.put("locremovedauth", "Line of code removed is at X% of data for the author of commit");
		resmap.put("totalfilechanged", "Number of files changed is at X% of data for this repository");
		resmap.put("totalfilechangedauth", "Number of files changed is at X% of data for the author of commit");
		resmap.put("totalfileaddedauth", "Number of files added is at X% of data for the author of commit");
		resmap.put("totalfileremovedauth", "Number of files removed is at X% of data for the author of commit");
		resmap.put("commitmsg", "Number of words in the commit message is at X% of data for this repository");
		resmap.put("commitmsgauth", "Number of words in the commit message is at X% of data for the author of commit");
		resmap.put("timeofcommitauth", "Author commited X% of commits at this hour");
		resmap.put("filpercentchan", "Filetype Y is changed X% of times in this repository");
		resmap.put("filpercentchanauth", "Filetype Y is changed X% of times by the author of commit");
		resmap.put("filpercommit", "Filetype Y is changed in X% of commits in this repository");
		resmap.put("filpercommitauth", "Filetype Y is changed in X% of commits by the author of commit");
		resmap.put("combfrequency", "Combination of filetype Y and Z is changed in X% of commits in this repository");
		resmap.put("combfrequencyauth", "Combination of filetype Y and Z is changed in X% of commits by the author of commit");
		resmap.put("combpropbability", "Probability of ratio of filetype Y and Z is X in this repository");
		resmap.put("combpropbabilityauth", "Probability of ratio of filetype Y and Z is X by the author of commit");
	}
	
	public String reasonis(ResultDatastr totalloc, ResultDatastr locadded, ResultDatastr locremoved, 
			ResultDatastr totalfilechanged, ResultDatastr totalfileadded, ResultDatastr totalfileremoved, 
			ResultDatastr commitmsg, ResultDatastr timeofcommit, 
			ResultDatastr filpercentchan, ResultDatastr filpercommit, 
			ResultDatastr combfrequency, ResultDatastr combprobability, 
			String Decision, Double Decisionval) {
		
		String Result = "";
		Result = Result + resmap.get("totalloc").replace("X", round2(totalloc.globalorg*100.0)+"");
		Result = Result + "\n";
		if(totalloc.authororg != 0.5) {
			Result = Result + resmap.get("totallocauth").replace("X", round2(totalloc.authororg*100.0)+"");
			Result = Result + "\n";
		}
		Result = Result + resmap.get("locadded").replace("X", round2(locadded.globalorg*100.0)+"");
		Result = Result + "\n";
		if(locadded.authororg != 0.5) {
			Result = Result + resmap.get("locaddedauth").replace("X", round2(locadded.authororg*100.0)+"");
			Result = Result + "\n";
		}
		Result = Result + resmap.get("locremoved").replace("X", round2(locremoved.globalorg*100.0)+"");
		Result = Result + "\n";
		if(locremoved.authororg != 0.5) {
			Result = Result + resmap.get("locremovedauth").replace("X", round2(locremoved.authororg*100.0)+"");
			Result = Result + "\n";
		}
		Result = Result + resmap.get("totalfilechanged").replace("X", round2(totalfilechanged.globalorg*100.0)+"");
		Result = Result + "\n";
		if(totalfilechanged.authororg != 0.5) {
			Result = Result + resmap.get("totalfilechangedauth").replace("X", round2(totalfilechanged.authororg*100.0)+"");
			Result = Result + "\n";
		}
		if(totalfileadded.authororg != 0.5) {
			Result = Result + resmap.get("totalfileaddedauth").replace("X", round2(totalfileadded.authororg*100.0)+"");
			Result = Result + "\n";
		}
		if(totalfileremoved.authororg != 0.5) {
			Result = Result + resmap.get("totalfileremovedauth").replace("X", round2(totalfileremoved.authororg*100.0)+"");
			Result = Result + "\n";
		}
		Result = Result + resmap.get("commitmsg").replace("X", round2(commitmsg.globalorg*100.0)+"");
		Result = Result + "\n";
		if(commitmsg.authororg != 0.5) {
			Result = Result + resmap.get("commitmsgauth").replace("X", round2(commitmsg.authororg*100.0)+"");
			Result = Result + "\n";
		}
		if(timeofcommit.authororg != 0.5) {
			Result = Result + resmap.get("timeofcommitauth").replace("X", round2(timeofcommit.authororg*100.0)+"");
			Result = Result + "\n";
		}
		
		if(!filpercentchan.valuestrglb.equals("NA")) {
			String temp = resmap.get("filpercentchan").replace("X", round2(filpercentchan.globalorg)+"");
			Result = Result + temp.replace("Y", filpercentchan.valuestrglb);
			Result = Result + "\n";
		}
		if(!filpercentchan.valuestrauth.equals("NA")) {
			String temp = resmap.get("filpercentchanauth").replace("X", round2(filpercentchan.authororg)+"");
			Result = Result + temp.replace("Y", filpercentchan.valuestrauth);
			Result = Result + "\n";
		}
		
		if(!filpercommit.valuestrglb.equals("NA")) {
			String temp = resmap.get("filpercommit").replace("X", round2(filpercommit.globalorg*100.0)+"");
			Result = Result + temp.replace("Y", filpercommit.valuestrglb);
			Result = Result + "\n";
		}
		if(!filpercommit.valuestrauth.equals("NA")) {
			String temp = resmap.get("filpercommitauth").replace("X", round2(filpercommit.authororg*100.0)+"");
			Result = Result + temp.replace("Y", filpercommit.valuestrauth);
			Result = Result + "\n";
		}
		
		String temp;
		if(!combfrequency.valuestrglb.equals("NA")) {
			String[] temp1 = combfrequency.valuestrglb.split(",");
			temp = resmap.get("combfrequency").replace("X", round2(combfrequency.globalorg*100.0)+"");
			temp = temp.replace("Z", temp1[1]);
			Result = Result + temp.replace("Y", temp1[0]);
			Result = Result + "\n";
		}
		
		if(!combfrequency.valuestrauth.equals("NA")) {
			String[] temp1 = combfrequency.valuestrauth.split(",");
			temp = resmap.get("combfrequencyauth").replace("X", round2(combfrequency.authororg*100.0)+"");
			temp = temp.replace("Z", temp1[1]);
			Result = Result + temp.replace("Y", temp1[0]);
			Result = Result + "\n";
		}
		
		if(!combprobability.valuestrglb.equals("NA")) {
			String[] temp1 = combprobability.valuestrglb.split(",");
			temp = resmap.get("combprobability").replace("X", round2(combprobability.globalorg*100.0)+"");
			temp = temp.replace("Z", temp1[1]);
			Result = Result + temp.replace("Y", temp1[0]);
			Result = Result + "\n";
		}
	
		if(!combprobability.valuestrauth.equals("NA")) {
			String[] temp1 = combprobability.valuestrauth.split(",");
			temp = resmap.get("combprobabilityauth").replace("X", round2(combprobability.authororg*100.0)+"");
			temp = temp.replace("Z", temp1[1]);
			Result = Result + temp.replace("Y", temp1[0]);
			Result = Result + "\n";
		}
		
		return Result;
	}
}
