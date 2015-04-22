package ml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import settings.RepoSettings;

public class FileTypeStatistics {
	
	RepoSettings rs;
	DataStatistics ds;
	
	public Map<String,List<String> > twocombinations = new HashMap<String,List<String>>();
	public Map<String,List<String> > authtwocombinations = new HashMap<String,List<String>>();
	public double totalcommits, authortotalcommits;
	
	public Map<String,Double> mapfileperchanged, mapauthfileperchanged;
	public Map<String,Double> mapfilecommitchanged, mapauthfilecommitchanged; 
	public Map<Double,Double> authtimemap = new  HashMap<Double,Double>();
	public Map<String,Double> meanmap, sdtmap;
	public Map<String,Double> authormeanmap, authorsdtmap;
	
	public void initiate(RepoSettings rs1, DataStatistics ds1) throws IOException{
		rs = rs1;
		ds = ds1;
	}
	

	public void fileperchanged() throws IOException{ //File percentage changed
        File file_global = new File(rs.Datapath +"//Global//Training_filescount.tsv");
        
		List<String> lis = FileUtils.readLines(file_global);
		Map<String,Double> mn = new HashMap<String,Double>();
		long cnt = 0;
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			cnt += Integer.parseInt(dat[1]);
		}
		
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			mn.put(dat[0], (Double.parseDouble(dat[1])*100.0/(double)cnt));
		}
		mapfileperchanged = mn;
	}
	
	public void authfileperchanged(String email) throws IOException{
        File file_author = new File(rs.Datapath + "//Author//" + email +"_filescount.tsv");
		List<String> lis = FileUtils.readLines(file_author);
		Map<String,Double> mn = new HashMap<String,Double>();
		long cnt = 0;
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			cnt += Integer.parseInt(dat[1]);
		}
		
		for(String hj : lis) {
			String[] dat = hj.split("\t");
			mn.put(dat[0], (Double.parseDouble(dat[1])*100.0/(double)cnt));
		}
		mapauthfileperchanged = mn;
	}
	
	public void filecommitchanged() throws IOException { //If file is changed in each commit, it is binary
        File file_global_matrix = new File(rs.Datapath + "//Global//Training_filestypescountcommit.tsv");

		List<String> files = FileUtils.readLines(file_global_matrix);
		totalcommits = files.size();
    	
		Map<String,Double> mn = new HashMap<String,Double>();
    	
    	for(String filecount : files) {
    		String[] temp = filecount.split("\t");
    		for(int i = 1; i < temp.length; i++) {
    				String[] temp1 = temp[i].split(",");
    				if(mn.containsKey(temp1[0])) {
    					mn.put(temp1[0],mn.get(temp1[0])+1.0);
    				}else {
    					mn.put(temp1[0],1.0);
    				}			
    		}	
        }
    	
    	mapfilecommitchanged = mn;	
	}
	
	public void authfilecommitchanged(String email) throws IOException {
        File file_author_matrix = new File(rs.Datapath + "//Author//" + email + "_filepercom.tsv");

		List<String> files = FileUtils.readLines(file_author_matrix);
		authortotalcommits = files.size();
    	
		Map<String,Double> mn = new HashMap<String,Double>();
    	
    	for(String filecount : files) {
    		String[] temp = filecount.split("\t");
    		for(int i = 1; i < temp.length; i++) {
    				String[] temp1 = temp[i].split(",");
    				if(mn.containsKey(temp1[0])) {
    					mn.put(temp1[0],mn.get(temp1[0])+1.0);
    				}else {
    					mn.put(temp1[0],1.0);
    				}			
    		}	
        }
    	
    	mapauthfilecommitchanged = mn;		
	}
	
	public void filecombinations() throws IOException {
		
		File file_global_matrix = new File(rs.Datapath + "//Global//Training_filestypescountcommit.tsv");

		List<String> files = FileUtils.readLines(file_global_matrix);
    			
    	for(String filecount : files) {
    		String[] temp = filecount.split("\t");
    		SortedMap<String,String> tempmap = new TreeMap<String,String>();
    		List<String> templist = new ArrayList<>();
    		for(int i = 1; i < temp.length; i++) { //0 being the commit id
    				String[] temp1 = temp[i].split(",");
    				tempmap.put(temp1[0],(temp1[1]));
    				templist.add(temp1[0]);
    		}	
    		Collections.sort(templist);
    	    		
	    	for(int s = 0; s + 1 < templist.size(); s++) {
		    	for(int u = s + 1; u < templist.size(); u++) {
		    		String combkey = templist.get(s) + "," + templist.get(u);
		    		if(twocombinations.containsKey(combkey)){
		    			List<String> temp1 = twocombinations.get(combkey);
		    			temp1.add(tempmap.get(templist.get(s)) + "," + tempmap.get(templist.get(u)));
		    			twocombinations.put(combkey,temp1);
		    		}else {
		    			List<String> temp1 = new ArrayList<String>();
		    			temp1.add(tempmap.get(templist.get(s)) + "," + tempmap.get(templist.get(u)));
		    			twocombinations.put(combkey,temp1);		    		
		    		}
		    	}
    		}
    		
        }

    	for(String comb: twocombinations.keySet()) {
    		try {
	    		String[] temp = comb.split(",");
	    		//System.out.println(temp[0]);
	    		
	    		List<String> listem = twocombinations.get(comb);
	    		
	    		if(listem.size() < 10) { //Atleast 10 combinations required
	    			continue;
	    		}
	    		
	    		File twocombcount = new File(rs.Datapath +"//Global//FileCombinations//"+ temp[0] + temp[1]+".tsv");
	    		twocombcount.getParentFile().mkdirs();
	    		FileWriter writ = new FileWriter(twocombcount);
	    		
	    		for(String num: listem){
	    			temp = num.split(",");
	    			writ.append(temp[0] + "\t" + temp[1] +"\n");
	    		}
	    		
	    		writ.flush();
	    		writ.close();
	    		
    		}catch(Exception e){
    			
    		}		
    	}
	}
	
	public void authfilecombinations(String email) throws IOException {
		
		authtwocombinations.clear();
		
        File file_author_matrix = new File(rs.Datapath + "//Author//" + email + "_filepercom.tsv");

		List<String> files = FileUtils.readLines(file_author_matrix);
    	    
    	for(String filecount : files) {
    		String[] temp = filecount.split("\t");
    		SortedMap<String,String> tempmap = new TreeMap<String,String>();
    		List<String> templist = new ArrayList<>();
    		for(int i = 1; i < temp.length; i++) {
    				String[] temp1 = temp[i].split(",");
    				tempmap.put(temp1[0],(temp1[1]));
    				templist.add(temp1[0]);
    		}	
    		Collections.sort(templist);
    	    		
	    	for(int s = 0; s + 1 < templist.size(); s++) {
		    	for(int u = s + 1; u < templist.size(); u++) {
		    		String combkey = templist.get(s) + "," + templist.get(u);
		    		if(authtwocombinations.containsKey(combkey)){
		    			List<String> temp1 = authtwocombinations.get(combkey);
		    			temp1.add(tempmap.get(templist.get(s)) + "," + tempmap.get(templist.get(u)));
		    			authtwocombinations.put(combkey,temp1);
		    		}else {
		    			List<String> temp1 = new ArrayList<String>();
		    			temp1.add(tempmap.get(templist.get(s)) + "," + tempmap.get(templist.get(u)));
		    			authtwocombinations.put(combkey,temp1);		    		
		    		}
		    	}
    		}
    		
        }
    	
    	
    	for(String comb: authtwocombinations.keySet()) {
    		try {
    		String[] temp = comb.split(",");
    		//System.out.println(temp[0]);
    		
    		List<String> listem = authtwocombinations.get(comb);
    		
    		if(listem.size() < 10) {
    			continue; //For author 10 combinations atleast
    		}
    		
    		File twocombcount = new File(rs.Datapath +"//Author//FileCombinations//"+ email + "//" + temp[0] + temp[1]+".tsv");
    		twocombcount.getParentFile().mkdirs();
    		FileWriter writ = new FileWriter(twocombcount);
    		//writ.append(temp[0] + "\t" + temp[1] + "\n");
    		
    		for(String num: listem){
    			temp = num.split(",");
    			writ.append(temp[0] + "\t" + temp[1] +"\n");
    		}
    		
    		writ.flush();
    		writ.close();
    		}catch(Exception e){
    			
    		}	
    	}
	}
	
	public void authortimeofcommit(String email) throws IOException{
		Map<Double, Double> timemap = new HashMap<>();
	   	for(double i = 0; i <= 23; i++) {
	   		timemap.put(i, 0.0);
	    }
	   	
	   	File authtime = new File(rs.Datapath +"//Author//"+ email +"_train.tsv"); 
	   	List<String> sizech = FileUtils.readLines(authtime);
	    sizech.remove(0);
        int v = 0;
        for(String qwer: sizech) {
	       	if(v >= sizech.size()-1){
	       		break;
         	}
		    String[] asd = qwer.split("\t");
		  	double zxc =  Double.parseDouble(asd[8]);
		  	if(timemap.containsKey(zxc)) {
	      		timemap.put(zxc, timemap.get(zxc)+1.0);
	       	}else {
	       		timemap.put(zxc,1.0);
	       	}
	    }
        authtimemap = timemap;
	}
	
	public void calcglobal() throws IOException{
		
		filecombinations(); //Two combinations
		filecommitchanged();
		fileperchanged();
		ds.combinationgraphglobal();	
		meanmap = ds.meanmap;
		sdtmap = ds.sdtmap;
	}
	
	public void calcauthor(String email) throws IOException {
		
		authfilecombinations(email); //Two combinations
		authfilecommitchanged(email);
		authfileperchanged(email);
		authortimeofcommit(email);
		ds.combinationgraphauthor(email);
		authormeanmap = ds.authormeanmap;
		authorsdtmap = ds.authorsdtmap;
	}
}
