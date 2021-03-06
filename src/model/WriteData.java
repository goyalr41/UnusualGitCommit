package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import settings.RepoSettings;

public class WriteData {

	RepoSettings rs;
	
	public void initiate(RepoSettings rs1) throws IOException{
				rs = rs1;
	}
	
	public void createglobalfile(File repo_global) throws IOException {
		
		repo_global.getParentFile().mkdirs();
	     
	    FileWriter writer = new FileWriter(repo_global); 
	        
	    writer.append("Commit Id\t");
	    writer.append("Author E-Mail\t");
	    writer.append("Total LOC\t");
	    writer.append("LOC added\t");
	    writer.append("LOC removed\t");
	    writer.append("Files affected\t");
        writer.append("Files added\t");
        writer.append("Files removed\t");
	    writer.append("Commit Msg\t");
	    writer.append("Time");
        writer.append("\n");
	        
        writer.flush();
	    writer.close();
		
	}
	
	public void createauthorfile(File authfile) throws IOException{
		authfile.getParentFile().mkdirs();
    	FileWriter writer_authtime = new FileWriter(authfile);
    	
    	writer_authtime.append("Commit Id\t");
        writer_authtime.append("Total LOC\t");
        writer_authtime.append("LOC added\t");
        writer_authtime.append("LOC removed\t");
        writer_authtime.append("Files affected\t");
        writer_authtime.append("Files added\t");
        writer_authtime.append("Files removed\t");
        writer_authtime.append("Commit Msg\t");
        writer_authtime.append("Time");
        writer_authtime.append("\n");
        
        writer_authtime.flush();
	    writer_authtime.close();
	}
	
	public void write(String commitid, String email, int totalloc, int locadd, int locrem, int nof, int nofadd, int nofrem, int timeofcommit, List<String> filetypes, int commsgsize) throws IOException{
		
		File repo_global = new File(rs.Datapath + "//Global//Training_data.tsv");
		
		if(!repo_global.exists()) {
    		createglobalfile(repo_global);
    	}
		
		FileWriter writer = new FileWriter(repo_global,true); 
		writer.append(commitid + "\t");
		writer.append(email + "\t");
		writer.append(totalloc + "\t");
		writer.append(locadd + "\t");
		writer.append(locrem + "\t");
		writer.append(nof + "\t");
		writer.append(nofadd + "\t");
		writer.append(nofrem + "\t");
		writer.append(commsgsize + "\t");
		writer.append(timeofcommit + "\n");
		
		writer.flush();
		writer.close();
		
		File authfile = new File(rs.Datapath +"//Author//"+ email+"_train.tsv"); 
    	if(!authfile.exists()) {
    		createauthorfile(authfile);
    	}
    	
    	FileWriter writer_authtime = new FileWriter(authfile, true);
            
        writer_authtime.append(commitid + "\t");
        writer_authtime.append(totalloc + "\t");
        writer_authtime.append(locadd + "\t");
        writer_authtime.append(locrem + "\t");
        writer_authtime.append(nof + "\t");
        writer_authtime.append(nofadd + "\t");
        writer_authtime.append(nofrem + "\t");
        writer_authtime.append(commsgsize + "\t");
        writer_authtime.append(timeofcommit + "\n");
            
        writer_authtime.flush();
        writer_authtime.close();
    
    	writeforfiles(commitid, email, filetypes);

	}
	
	//Write about types of file changed and files count in all commit
	public void writeforfiles(String commitid, String email, List<String> filetypes) throws IOException {
	      
		File file_global = new File(rs.Datapath + "//Global//Training_filescount.tsv");
		File file_global_matrix = new File(rs.Datapath + "//Global//Training_filestypescountcommit.tsv");
	  
		Map<String,Long> mapfilecount = new HashMap<String,Long>();
	        	        
	        if(file_global.exists()) {
	        	List<String> files = FileUtils.readLines(file_global);
	        	for(String filetype : files) {
	        		String[] temp = filetype.split("\t");
	        		if(temp.length > 1) {
	        			mapfilecount.put(temp[0], Long.parseLong(temp[1]));
	        		}
	        	}
	        }
	        
	     	for(String filetype : filetypes) {
	    		if(mapfilecount.containsKey(filetype)) {
	    			mapfilecount.put(filetype, mapfilecount.get(filetype) + 1);
	    		}else {
	    			mapfilecount.put(filetype, (long)1);
	    		}    		
	    	}
	        
	        mapfilecount = MapUtil.sortByValue(mapfilecount);
	        
	        FileWriter writer1 = new FileWriter(file_global); 
	        
	        for (String filtype : mapfilecount.keySet()){
		    	writer1.append(filtype + "\t" + mapfilecount.get(filtype) + "\n");
		    }
	        
	        writer1.flush();
	        writer1.close();
	        
	        Map<String,Long> filecounts = new HashMap<>();
	    	//This is for current commit only
	    	for(String filetype : filetypes) {
	    		if(filecounts.containsKey(filetype)) {
	    			filecounts.put(filetype, filecounts.get(filetype) + 1);
	    		}else {
	    			filecounts.put(filetype, (long)1);
	    		}    		
	    	}
	    	
	    	FileWriter writer2 = new FileWriter(file_global_matrix, true);
	    	
	    	writer2.append(commitid);
	    	
	    	for(String filetype: filecounts.keySet()){
	    		writer2.append("\t" + filetype + "," + filecounts.get(filetype));
	    	}
	    	
	    	writer2.append("\n");
	    	
	    	writer2.flush();
	    	writer2.close();
	    	
	    	   
	        File authfiles = new File(rs.Datapath + "//Author//" + email +"_filescount.tsv"); 
	        File auth_file_global_matrix = new File(rs.Datapath + "//Author//" + email + "_filepercom.tsv");

	        
	        Map<String,Long> mapauthorfilecount = new HashMap<String,Long>();
		    
		    if(authfiles.exists()) {
		    	List<String> files = FileUtils.readLines(authfiles);
		    	for(String filetype : files) {
		    		String[] temp = filetype.split("\t");
	        		if(temp.length > 1) {
	        			mapauthorfilecount.put(temp[0], Long.parseLong(temp[1]));
	        		}
		    	}
		    }
		    
		    for(String filetype : filetypes) {
	    		if(mapauthorfilecount.containsKey(filetype)) {
	    			mapauthorfilecount.put(filetype, mapauthorfilecount.get(filetype) + 1);
	    		}else {
	    			mapauthorfilecount.put(filetype, (long)1);
	    		}
	    	}
		    
		    authfiles.getParentFile().mkdirs();
			FileWriter writer_authfiles = new FileWriter(authfiles); 
			FileWriter authwriter2 = new FileWriter(auth_file_global_matrix, true);
	        mapauthorfilecount = MapUtil.sortByValue(mapauthorfilecount);    	

	        for (String filtype : mapauthorfilecount.keySet()){
		    	writer_authfiles.append(filtype + "\t" + mapauthorfilecount.get(filtype) + "\n");
		    }
	       
	        writer_authfiles.flush();
			writer_authfiles.close();
				    	
	    	authwriter2.append(commitid);
	    	for(String filetype: filecounts.keySet()){
	    		authwriter2.append("\t" + filetype + "," + filecounts.get(filetype));
	    	}
	    	authwriter2.append("\n");
	    	
	    	authwriter2.flush();
	    	authwriter2.close();        
	}
	
	
}
