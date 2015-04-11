package model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import settings.RepoSettings;

public class Buildmodel {
	
	public void build(Git git, List<RevCommit> logs, RepoSettings rs) throws NoHeadException, GitAPIException, IOException {
		
		 //ObjectId lastCommitId = repository.resolve(Constants.MASTER);
        ObjectId parentid = null;
        ObjectId currid = null;
        
        Repository repository = git.getRepository();
        
        //a new reader to read objects from getObjectDatabase()
        ObjectReader reader = repository.newObjectReader();
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        
        List<DiffEntry> diffs = null;
        
        WriteData wd = new WriteData(); //Class to write data to an external file
        wd.initiate(rs);
        
        for (RevCommit rev : logs) {
        	
        	if(rev.getParentCount() == 1) { //Not taking Merge commits, for taking them make it >= .
	    	       
        		try {
    	        	
    	        	parentid = repository.resolve(rev.getParent(0).getName()+"^{tree}");
    	        	currid = repository.resolve(rev.getName()+"^{tree}");
	    	       
    	            //Reset this parser to walk through the given tree
    	            oldTreeIter.reset(reader, parentid);
    	            newTreeIter.reset(reader, currid);
    	            diffs = git.diff() //Returns a command object to execute a diff command
    	                    .setNewTree(newTreeIter)
    	                    .setOldTree(oldTreeIter)
    	                    .call(); //returns a DiffEntry for each path which is different

    	        } catch (RevisionSyntaxException | IOException | GitAPIException e) {
    	            e.printStackTrace();
    	        }
        		
        		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	    //Create a new formatter with a default level of context.
        		DiffFormatter df = new DiffFormatter(out);
	    	    //Set the repository the formatter can load object contents from.
	            df.setRepository(git.getRepository());
	    	        
	   	        ArrayList<String> diffText = new ArrayList<String>();
	   	        ArrayList<String> filetypes = new ArrayList<String>();
	    	        
	   	        //A DiffEntry is 'A value class representing a change to a file' therefore for each file you have a diff entry.
	   	        int ovllineadd = 0;
                int ovllinerem = 0;
	            int totallinechanged = 0;
	            int totalfilrem = 0; 
	            int totalfiladd = 0;
	            
	            for(DiffEntry diff : diffs) {
	    	           
    	        	try {
    	                
    	                df.format(diff);  //Format a patch script for one file entry.
    	                RawText r = new RawText(out.toByteArray());
    	                r.getLineDelimiter();
    	                diffText.add(out.toString());
    	                
    	                String st = out.toString();
    	                String filetype = null;
    	                int filerem = 0,fileadd = 0, filtyp = 0;;
    	                int lineadd = 0, linerem = 0;
    	                
    	                String[] temp = st.split("\n");
    	                
    	                for(String tem : temp) {
    	                	
    	                	if(tem.startsWith("---") || tem.startsWith("+++")) {
	    	                	
    	                		if(tem.startsWith("---")) {
	    	                		if(tem.endsWith("null")) {
	    	                			fileadd++;                          //File added
	    	                		}else {
	    	                			int h = tem.lastIndexOf(".");
	    	                			if(h >= 0) {
	    	                				filetype = tem.substring(h, tem.length());
	    	                				filetype = filetype.replace(" ", "");

	    	                				if(filtyp != 1) {
	    		    	                		filetypes.add(filetype);
	    		    	                		filtyp = 1;
	    		    	                	}
	    	                			}
	    	                		}
	    	                	}
	    	                	if(tem.startsWith("+++")) {
	    	                		if(tem.endsWith("null")) {
	    	                			filerem++;							//Fil removed
	    	                		} else {	
	    	                			int h = tem.lastIndexOf(".");
	    	                			if(h >= 0) {
	    	                				filetype = tem.substring(h, tem.length());
	    	                				filetype = filetype.replace(" ", "");
	    	                				if(filtyp != 1) {
	    		    	                		filetypes.add(filetype);
	    		    	                		filtyp = 1;
	    		    	                	}
	    	                			}
	    	                		}
	    	                	}
	    	                
    	                	}
    	                	
    	                	if(tem.startsWith("+") && !tem.startsWith("+++")) {
    	                		lineadd++;
    	                	}else if(tem.startsWith("-") && !tem.startsWith("---")) {
    	                		linerem++;
    	                	}
    	                	
    	                }
    	                ovllineadd += lineadd;
    	                ovllinerem += linerem;
    	                totallinechanged += (lineadd + linerem);
    	            	totalfiladd += fileadd;
    	                totalfilrem += filerem;
    	                
    	                out.reset();
    	                
    	            } catch (IOException e) {
    	                e.printStackTrace();
    	            } 
    	         }
	            
	            PersonIdent p = rev.getAuthorIdent();
        		//DateFormat formatter= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        		DateFormat formatter= new SimpleDateFormat("HH");
        		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        	
        		String email = p.getEmailAddress();
        		//So that email folder is created properly
	    	    if(email.contains("://") || email.contains("//")) {
	    	        	email = email.replace(":", "");
	    	        	email = email.replace("//", "");
	    	    }
	    	    
	    	    String[] commsgwords = rev.getFullMessage().split(" ");
	    	    
	    	    wd.write(rev.getName(), email, totallinechanged, ovllineadd, ovllinerem, filetypes.size(), totalfiladd, totalfilrem, Integer.parseInt(formatter.format(p.getWhen())), filetypes, commsgwords.length);
	    	              
        	}  
        }
        
        repository.close();
	}
}
