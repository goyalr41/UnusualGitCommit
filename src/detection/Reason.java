package detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.MapUtil;

public class Reason {
	
	Map<String,String> resmap = new HashMap<String,String>();
	Map<String,Double> top5map = new HashMap<String,Double>();
	Map<String,Double> top5map1 = new HashMap<String,Double>();
	
	
	public double round2(double d) { //For rounding Decimal values
	     return Math.round(d * 100) / 100.0;
	}
	
	public Reason(){
		resmap.put("totalloc", "X% of commits have these many or more Lines of Code changed.");
		resmap.put("totallocauth", "X% of commits by this author have these many or more Lines of Code changed.");
		resmap.put("locadded", "X% of commits have these many or more Lines of Code added.");
		resmap.put("locaddedauth", "X% of commits by this author have these many or more Lines of Code added.");
		resmap.put("locremoved", "X% of commits have these many or more Lines of Code removed.");
		resmap.put("locremovedauth", "X% of commits by this author have these many or more Lines of Code removed.");
		resmap.put("totalfilechanged", "X% of commits have these many or more Number of Files changed.");
		resmap.put("totalfilechangedauth", "X% of commits by this author have these many or more Number of Files changed.");
		resmap.put("totalfileaddedauth", "X% of commits by this author have these many or more Number of Files added.");
		resmap.put("totalfileremovedauth", "X% of commits by this author have these many or more Number of Files removed.");
		resmap.put("commitmsg", "X% of commits have these many or more words in commit message.");
		resmap.put("commitmsgauth", "X% of commits by this author have these many or more words in commit message.");
		resmap.put("timeofcommitauth", "X% of commits by this author have been commited at this hour.");
		resmap.put("filpercentchan", "X% of times, filetype Y is changed.");
		resmap.put("filpercentchanauth", "X% of times by this author, filetype Y is changed.");
		resmap.put("filpercommit", "X% of commits have filetype Y changed.");
		resmap.put("filpercommitauth", "X% of commits by this author have filetype Y changed.");
		resmap.put("combfrequency", "X% of commits have combination of filetypes Y and Z changed.");
		resmap.put("combfrequencyauth", "X% of commits by this author have combination of filetypes Y and Z changed.");
		resmap.put("combprobability", "X% is the probability of filetypes Y and Z changed in this ratio.");
		resmap.put("combprobabilityauth", "X% is the probability of filetypes Y and Z changed in this ratio by this author.");
	}
	
	public List<DataReason> top5(ResultDatastr totalloc, ResultDatastr locadded, ResultDatastr locremoved, 
			ResultDatastr totalfilechanged, ResultDatastr totalfileadded, ResultDatastr totalfileremoved, 
			ResultDatastr commitmsg, ResultDatastr timeofcommit, 
			ResultDatastr filpercentchan, ResultDatastr filpercommit, 
			ResultDatastr combfrequency, ResultDatastr combprobability, 
			String Decision, Double Decisionval) {
		
		top5map.clear();
		top5map1.clear();
		
		top5map.put("totalloc", totalloc.globalmapped);
		top5map.put("totallocauth", totalloc.authormapped);
		top5map.put("locadded", locadded.globalmapped);
		top5map.put("locaddedauth", locadded.authormapped);
		top5map.put("locremoved", locremoved.globalmapped);
		top5map.put("locremovedauth", locremoved.authormapped);
		top5map.put("totalfilechanged", totalfilechanged.globalmapped);
		top5map.put("totalfilechangedauth", totalfilechanged.authormapped);
		top5map.put("totalfileaddedauth", totalfileadded.authormapped);
		top5map.put("totalfileremovedauth", totalfileremoved.authormapped);
		top5map.put("commitmsg", commitmsg.globalmapped);
		top5map.put("commitmsgauth",commitmsg.authormapped);
		top5map.put("timeofcommitauth",timeofcommit.authormapped);
		top5map.put("filpercentchan", filpercentchan.globalmapped);
		top5map.put("filpercentchanauth", filpercentchan.authormapped);
		top5map.put("filpercommit", filpercommit.globalmapped);
		top5map.put("filpercommitauth", filpercommit.authormapped);
		top5map.put("combfrequency", combfrequency.globalmapped);
		top5map.put("combfrequencyauth", combfrequency.authormapped);
		top5map.put("combprobability", combprobability.globalmapped);
		top5map.put("combprobabilityauth", combprobability.authormapped);
		
		top5map1.put("totalloc", totalloc.globalorg);
		top5map1.put("totallocauth", totalloc.authororg);
		top5map1.put("locadded", locadded.globalorg);
		top5map1.put("locaddedauth", locadded.authororg);
		top5map1.put("locremoved", locremoved.globalorg);
		top5map1.put("locremovedauth", locremoved.authororg);
		top5map1.put("totalfilechanged", totalfilechanged.globalorg);
		top5map1.put("totalfilechangedauth", totalfilechanged.authororg);
		top5map1.put("totalfileaddedauth", totalfileadded.authororg);
		top5map1.put("totalfileremovedauth", totalfileremoved.authororg);
		top5map1.put("commitmsg", commitmsg.globalorg);
		top5map1.put("commitmsgauth",commitmsg.authororg);
		top5map1.put("timeofcommitauth",timeofcommit.authororg);
		top5map1.put("filpercentchan", filpercentchan.globalorg/100.0);
		top5map1.put("filpercentchanauth", filpercentchan.authororg/100.0);
		top5map1.put("filpercommit", filpercommit.globalorg);
		top5map1.put("filpercommitauth", filpercommit.authororg);
		top5map1.put("combfrequency", combfrequency.globalorg);
		top5map1.put("combfrequencyauth", combfrequency.authororg);
		top5map1.put("combprobability", combprobability.globalorg);
		top5map1.put("combprobabilityauth", combprobability.authororg);
				
		top5map = MapUtil.sortByValue(top5map);
		return makelist(top5map, top5map1, totalloc, locadded, locremoved, totalfilechanged, totalfileadded, totalfileremoved, commitmsg, timeofcommit, filpercentchan, filpercommit, combfrequency, combprobability, Decision, Decisionval);
		
	}
	
	public List<DataReason> makelist(Map<String,Double> top5map, Map<String,Double> top5map1, ResultDatastr totalloc, ResultDatastr locadded, ResultDatastr locremoved, 
			ResultDatastr totalfilechanged, ResultDatastr totalfileadded, ResultDatastr totalfileremoved, 
			ResultDatastr commitmsg, ResultDatastr timeofcommit, 
			ResultDatastr filpercentchan, ResultDatastr filpercommit, 
			ResultDatastr combfrequency, ResultDatastr combprobability, 
			String Decision, Double Decisionval) {
		
		List<DataReason> reasonlist = new ArrayList<DataReason>();
		
		for(String str: top5map.keySet()) {
				
				if(str == "totalloc") {
					DataReason r = new DataReason();
					r.name = str;
					r.value = totalloc.value +"";
					r.valorg = (1.0-totalloc.globalorg)*100.0+"";
					r.valmap = totalloc.globalmapped+"";
					reasonlist.add(r);
				}
				
				if(str == "totallocauth") {
					if(top5map1.get(str) != 0.5) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = totalloc.value +"";
						r.valorg = (1.0-totalloc.authororg)*100.0+"";
						r.valmap = totalloc.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "locadded") {
					DataReason r = new DataReason();
					r.name = str;
					r.value = locadded.value +"";
					r.valorg = (1.0-locadded.globalorg)*100.0+"";
					r.valmap = locadded.globalmapped+"";
					reasonlist.add(r);
				}
				
				if(str == "locaddedauth") {
					if(top5map1.get(str) != 0.5) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = locadded.value +"";
						r.valorg = (1.0-locadded.authororg)*100.0+"";
						r.valmap = locadded.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "locremoved") {
					DataReason r = new DataReason();
					r.name = str;
					r.value = locremoved.value +"";
					r.valorg = (1.0-locremoved.globalorg)*100.0+"";
					r.valmap = locremoved.globalmapped+"";
					reasonlist.add(r);
				}
				
				if(str == "locremovedauth") {
					if(top5map1.get(str) != 0.5) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = locremoved.value +"";
						r.valorg = (1.0-locremoved.authororg)*100.0+"";
						r.valmap = locremoved.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "totalfilechanged") {
					DataReason r = new DataReason();
					r.name = str;
					r.value = totalfilechanged.value +"";
					r.valorg = (1.0-totalfilechanged.globalorg)*100.0+"";
					r.valmap = totalfilechanged.globalmapped+"";
					reasonlist.add(r);
				}
				
				if(str == "totalfilechangedauth") {
					if(top5map1.get(str) != 0.5) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = totalfilechanged.value +"";
						r.valorg = (1.0-totalfilechanged.authororg)*100.0+"";
						r.valmap = totalfilechanged.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "totalfileaddedauth") {
					if(top5map1.get(str) != 0.5) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = totalfileadded.value +"";
						r.valorg = (1.0-totalfileadded.authororg)*100.0+"";
						r.valmap = totalfileadded.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "totalfileremovedauth") {
					if(top5map1.get(str) != 0.5) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = totalfileremoved.value +"";
						r.valorg = (1.0-totalfileremoved.authororg)*100.0+"";
						r.valmap = totalfileremoved.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "commitmsg") {
					DataReason r = new DataReason();
					r.name = str;
					r.value = commitmsg.value +"";
					r.valorg = (1.0-commitmsg.globalorg)*100.0+"";
					r.valmap = commitmsg.globalmapped+"";
					reasonlist.add(r);
				}
				
				if(str == "commitmsgauth") {
					if(top5map1.get(str) != 0.5) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = commitmsg.value +"";
						r.valorg = (1.0-commitmsg.authororg)*100.0+"";
						r.valmap = commitmsg.authormapped+"";
						reasonlist.add(r);
					}
				}
				if(str == "timeofcommitauth") {
					if(top5map1.get(str) != 0.5) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = timeofcommit.value +"";
						r.valorg = (timeofcommit.authororg)*100.0+"";
						r.valmap = timeofcommit.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "filpercentchan") {
					if(!filpercentchan.valuestrglb.equals("NA")) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = filpercentchan.valuestrglb +"";
						r.valorg = filpercentchan.globalorg+"";
						r.valmap = filpercentchan.globalmapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "filpercentchanauth") {
					if(!filpercentchan.valuestrauth.equals("NA")) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = filpercentchan.valuestrauth +"";
						r.valorg = filpercentchan.authororg+"";
						r.valmap = filpercentchan.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "filpercommit") {
					if(!filpercommit.valuestrglb.equals("NA")) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = filpercommit.valuestrglb +"";
						r.valorg = (filpercommit.globalorg*100.0)+"";
						r.valmap = filpercommit.globalmapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "filpercommitauth") {
					if(!filpercommit.valuestrauth.equals("NA")) {
						DataReason r = new DataReason();
						r.name = str;
						r.value = filpercommit.valuestrauth +"";
						r.valorg = (filpercommit.authororg*100.0)+"";
						r.valmap = filpercommit.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "combfrequency") {
					if(!combfrequency.valuestrglb.equals("NA")) {
						DataReason r = new DataReason();
						String[] temp1 = combfrequency.valuestrglb.split(",");
						r.name = str;
						r.value = temp1[0] + " and " + temp1[1] + "";
						r.valorg = (combfrequency.globalorg*100.0)+"";
						r.valmap = combfrequency.globalmapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "combfrequencyauth") {
					if(!combfrequency.valuestrauth.equals("NA")) {
						DataReason r = new DataReason();
						String[] temp1 = combfrequency.valuestrauth.split(",");
						r.name = str;
						r.value = temp1[0] + " and " + temp1[1] + "";
						r.valorg = (combfrequency.authororg*100.0)+"";
						r.valmap = combfrequency.authormapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "combprobability") {
					if(!combprobability.valuestrglb.equals("NA")) {
						DataReason r = new DataReason();
						String[] temp1 = combprobability.valuestrglb.split(",");
						r.name = str;
						r.value = temp1[0] + " and " + temp1[1] + "";
						r.valorg = (combprobability.globalorg*100.0)+"";
						r.valmap = combprobability.globalmapped+"";
						reasonlist.add(r);
					}
				}
				
				if(str == "combprobabilityauth") {
					if(!combprobability.valuestrauth.equals("NA")) {
						DataReason r = new DataReason();
						String[] temp1 = combprobability.valuestrauth.split(",");
						r.name = str;
						r.value = temp1[0] + " and " + temp1[1] + "";
						r.valorg = (combprobability.authororg*100.0)+"";
						r.valmap = combprobability.authormapped+"";
						reasonlist.add(r);
					}
				}	
			
		}
		return reasonlist;
		
	}
	
	public String reasonis(ResultDatastr totalloc, ResultDatastr locadded, ResultDatastr locremoved, 
			ResultDatastr totalfilechanged, ResultDatastr totalfileadded, ResultDatastr totalfileremoved, 
			ResultDatastr commitmsg, ResultDatastr timeofcommit, 
			ResultDatastr filpercentchan, ResultDatastr filpercommit, 
			ResultDatastr combfrequency, ResultDatastr combprobability, 
			String Decision, Double Decisionval){
		String Result = "";
		int i = 0;
		for(String str: top5map.keySet()) {
			if(i < 6) {
				
				if(str == "totalloc") {
					Result = Result + resmap.get("totalloc").replace("X", round2((1.0-totalloc.globalorg)*100.0)+"");
					Result = Result + "\n";
					i++;
				}
				
				if(str == "totallocauth") {
					if(top5map1.get(str) != 0.5) {
						Result = Result + resmap.get("totallocauth").replace("X", round2((1.0-totalloc.authororg)*100.0)+"");
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "locadded") {
					Result = Result + resmap.get("locadded").replace("X", round2((1.0-locadded.globalorg)*100.0)+"");
					Result = Result + "\n";
					i++;
				}
				
				if(str == "locaddedauth") {
					if(top5map1.get(str) != 0.5) {
						Result = Result + resmap.get("locaddedauth").replace("X", round2((1.0-locadded.authororg)*100.0)+"");
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "locremoved") {
					Result = Result + resmap.get("locremoved").replace("X", round2((1.0-locremoved.globalorg)*100.0)+"");
					Result = Result + "\n";
					i++;
				}
				
				if(str == "locremovedauth") {
					if(top5map1.get(str) != 0.5) {
						Result = Result + resmap.get("locremovedauth").replace("X", round2((1.0-locremoved.authororg)*100.0)+"");
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "totalfilechanged") {
					Result = Result + resmap.get("totalfilechanged").replace("X", round2((1.0-totalfilechanged.globalorg)*100.0)+"");
					Result = Result + "\n";
					i++;
				}
				
				if(str == "totalfilechangedauth") {
					if(top5map1.get(str) != 0.5) {
						Result = Result + resmap.get("totalfilechangedauth").replace("X", round2((1.0-totalfilechanged.authororg)*100.0)+"");
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "totalfileaddedauth") {
					if(top5map1.get(str) != 0.5) {
						Result = Result + resmap.get("totalfileaddedauth").replace("X", round2((1.0-totalfileadded.authororg)*100.0)+"");
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "totalfileremovedauth") {
					if(top5map1.get(str) != 0.5) {
						Result = Result + resmap.get("totalfileremovedauth").replace("X", round2((1.0-totalfileremoved.authororg)*100.0)+"");
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "commitmsg") {
					Result = Result + resmap.get("commitmsg").replace("X", round2((1.0-commitmsg.globalorg)*100.0)+"");
					Result = Result + "\n";
					i++;
				}
				
				if(str == "commitmsgauth") {
					if(top5map1.get(str) != 0.5) {
						Result = Result + resmap.get("commitmsgauth").replace("X", round2((1.0-commitmsg.authororg)*100.0)+"");
						Result = Result + "\n";
						i++;
					}
				}
				if(str == "timeofcommitauth") {
					if(top5map1.get(str) != 0.5) {
						Result = Result + resmap.get("timeofcommitauth").replace("X", round2(timeofcommit.authororg*100.0)+"");
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "filpercentchan") {
					if(!filpercentchan.valuestrglb.equals("NA")) {
						String temp = resmap.get("filpercentchan").replace("X", round2(filpercentchan.globalorg)+"");
						Result = Result + temp.replace("Y", filpercentchan.valuestrglb);
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "filpercentchanauth") {
					if(!filpercentchan.valuestrauth.equals("NA")) {
						String temp = resmap.get("filpercentchanauth").replace("X", round2(filpercentchan.authororg)+"");
						Result = Result + temp.replace("Y", filpercentchan.valuestrauth);
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "filpercommit") {
					if(!filpercommit.valuestrglb.equals("NA")) {
						String temp = resmap.get("filpercommit").replace("X", round2(filpercommit.globalorg*100.0)+"");
						Result = Result + temp.replace("Y", filpercommit.valuestrglb);
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "filpercommitauth") {
					if(!filpercommit.valuestrauth.equals("NA")) {
						String temp = resmap.get("filpercommitauth").replace("X", round2(filpercommit.authororg*100.0)+"");
						Result = Result + temp.replace("Y", filpercommit.valuestrauth);
						Result = Result + "\n";
						i++;
					}
				}
				
				String temp;
				if(str == "combfrequency") {
					if(!combfrequency.valuestrglb.equals("NA")) {
						String[] temp1 = combfrequency.valuestrglb.split(",");
						temp = resmap.get("combfrequency").replace("X", round2(combfrequency.globalorg*100.0)+"");
						temp = temp.replace("Z", temp1[1]);
						Result = Result + temp.replace("Y", temp1[0]);
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "combfrequencyauth") {
					if(!combfrequency.valuestrauth.equals("NA")) {
						String[] temp1 = combfrequency.valuestrauth.split(",");
						temp = resmap.get("combfrequencyauth").replace("X", round2(combfrequency.authororg*100.0)+"");
						temp = temp.replace("Z", temp1[1]);
						Result = Result + temp.replace("Y", temp1[0]);
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "combprobability") {
					if(!combprobability.valuestrglb.equals("NA")) {
						String[] temp1 = combprobability.valuestrglb.split(",");
						temp = resmap.get("combprobability").replace("X", round2(combprobability.globalorg*100.0)+"");
						temp = temp.replace("Z", temp1[1]);
						Result = Result + temp.replace("Y", temp1[0]);
						Result = Result + "\n";
						i++;
					}
				}
				
				if(str == "combprobabilityauth") {
					if(!combprobability.valuestrauth.equals("NA")) {
						String[] temp1 = combprobability.valuestrauth.split(",");
						temp = resmap.get("combprobabilityauth").replace("X", round2(combprobability.authororg*100.0)+"");
						temp = temp.replace("Z", temp1[1]);
						Result = Result + temp.replace("Y", temp1[0]);
						Result = Result + "\n";
						i++;
					}
				}	
			}else {
				break;
			}
		}
	
		return Result;
	}
	
	public String reasoniss(ResultDatastr totalloc, ResultDatastr locadded, ResultDatastr locremoved, 
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
