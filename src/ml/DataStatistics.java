package ml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import settings.RepoSettings;
import ml.TextConsole;

public class DataStatistics {
	
	public double totallocbeta, totallocalpha, totallocauthbeta, totallocauthalpha;
	public double locaddbeta, locaddalpha, locaddauthbeta, locaddauthalpha;
	public double locrembeta, locremalpha, locremauthbeta, locremauthalpha;
	public double nofbeta, nofalpha, nofauthbeta, nofauthalpha;
	public double nofaddbeta, nofaddalpha, nofaddauthbeta, nofaddauthalpha;
	public double nofrembeta, nofremalpha, nofremauthbeta, nofremauthalpha;
	public double commsgalpha, commsgbeta, commsgauthbeta, commsgauthalpha;
	public double timeauthbeta, timeauthalpha;
	
	public Map<String,Double> meanmap = new HashMap<String,Double>();
	public Map<String,Double> sdtmap = new HashMap<String,Double>();
	public Map<String,Double> authormeanmap = new HashMap<String,Double>();
	public Map<String,Double> authorsdtmap = new HashMap<String,Double>();
	
	public Rengine re;
	public RepoSettings rs;
	
	public void initiate(RepoSettings rs1) {
		// Making sure we have the right version of everything
		if (!Rengine.versionCheck()) {
		    System.err.println("** Version mismatch - Java files don't match library version.");
		    System.exit(1);
		}
		
		String[] args = null;
		re = new Rengine(args, false, new TextConsole());
		rs = rs1;
		
		// the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
		
	}
	
	public void calcglobal() {
       
		
		try {
			
			String Datapath = rs.Datapath;
			String DatapathforR = Datapath.replace("//", "/");
			String read1 = "Data <- read.delim(\"" + DatapathforR + "/Global/Training_data.tsv\")";
			
			re.eval(read1);
			
			re.eval("p = Data$Total.LOC");
			re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			REXP bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {
				re.eval("p = append(p,max(p)+1)");
			}
			re.eval("Y = ecdf(p)");
			//0.000001 added to handle log(0) case.
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			String temp = "b = -fit$coef[[\"x\"]]";
			REXP b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			REXP a = re.eval("a = exp(a/b)");			
			
		    totallocbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    totallocalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));

		    
		    re.eval("p = Data$LOC.added");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			locaddbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    locaddalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		     
		    re.eval("p = Data$LOC.removed");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			locrembeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    locremalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Files.affected");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Files.added");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofaddbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofaddalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Files.removed");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofrembeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofremalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Commit.Msg");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			commsgbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    commsgalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    		    
			
		} catch (Exception e) {
			System.out.println("EX:"+e);
			e.printStackTrace();
		}
		
		 re.end();
		 
    }
	
	public void calculateauthor(String email){
        		
		try {
			String Datapath = rs.Datapath;
			String DatapathforR = Datapath.replace("//", "/");
			String read1 = "Data <- read.delim(\"" + DatapathforR + "/Author/"+ email +"_train.tsv\")";
	
				
			re.eval(read1);
			
			re.eval("p = Data$Time");
			re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			REXP bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			String temp = "b = -fit$coef[[\"x\"]]";
			REXP b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			REXP a = re.eval("a = exp(a/b)");			
			
		    timeauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    timeauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    
			re.eval("p = Data$Total.LOC");
			re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {					
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
		    totallocauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    totallocauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));

		    
		    re.eval("p = Data$LOC.added");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {					
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			locaddauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    locaddauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		     
		    re.eval("p = Data$LOC.removed");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {					
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			locremauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    locremauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Files.affected");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {					
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Files.added");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {					
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofaddauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofaddauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$LOC.removed");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {					
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			nofremauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    nofremauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    
		    re.eval("p = Data$Commit.Msg");
		    re.eval("j = 0");
			re.eval("if(isTRUE(all.equal( max(p) , min(p) ))) {j = 1}");
			bool = re.eval("j");
			if(bool.toString().substring(8, bool.toString().length()-2).equals("1.0")) {					
				re.eval("p = append(p,max(p)+1)"); //This is to handle case of all same values.
			}
			re.eval("Y = ecdf(p)");
			re.eval("df <- data.frame(x=log(p+0.000001), y = log(1.000001 - Y(p)))");
			re.eval("fit <- lm(y~x,df)");
			temp = "b = -fit$coef[[\"x\"]]";
			b = re.eval(temp);
			temp = "a = fit$coef[[\"(Intercept)\"]]";
			re.eval(temp);
			a = re.eval("a = exp(a/b)");			
			
			commsgauthbeta = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
		    commsgauthalpha = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
		    	
		    /*System.out.println(totallocbeta + "\n" + totallocalpha + "\n" + totallocauthbeta + "\n" + totallocauthalpha + "\n" +
			locaddbeta + "\n" + locaddalpha + "\n" + locaddauthbeta + "\n" + locaddauthalpha + "\n" +
			locrembeta + "\n" + locremalpha + "\n" + locremauthbeta + "\n" + locremauthalpha + "\n" +
			nofbeta + "\n" + nofalpha + "\n" + nofauthbeta + "\n" + nofauthalpha + "\n" +
			nofaddbeta + "\n" + nofaddalpha + "\n" + nofaddauthbeta + "\n" + nofaddauthalpha + "\n" +
			nofrembeta + "\n" + nofremalpha + "\n" + nofremauthbeta + "\n" + nofremauthalpha + "\n" +
		timebeta + "\n" + timealpha);*/
		    
		} catch (Exception e) {
			System.out.println("EX:"+e);
			e.printStackTrace();
		}
		
		 re.end();
    
	}
	
public void combinationgraphglobal() {
    	
		meanmap.clear();
		sdtmap.clear();
		
        File combdir =  new File( rs.Datapath + "//Global//FileCombinations");
	    File[] files = combdir.listFiles();
	    if(files == null) {
	    	return;
	    }
	    for(File f: files){
	    	String path = f.getAbsolutePath();
	    	//System.out.println(path);
	    	int p = path.lastIndexOf("\\");
            String fil_name = path.substring(p+1, path.length());
	    	int h = fil_name.lastIndexOf(".");
            String fil_name_key = fil_name.substring(0,h);
            h = fil_name_key.lastIndexOf(".");
	        fil_name_key = fil_name_key.substring(0,h) + "," + fil_name_key.substring(h,fil_name_key.length()) ;
            fil_name = "`" + fil_name + "`"; 
            path = path.replace("\\", "/");
            //System.out.println(path);
            String authread = fil_name +" <- read.delim(\""+path+"\", header=FALSE)";
	    	re.eval(authread);
	    	//System.out.println(authread);
			
			String mea = "s <- mean(log10("+fil_name+"$V1/"+fil_name+"$V2))";
			String sd = "d <- sd(log10("+fil_name+"$V1/"+fil_name+"$V2))";
			//String val = "abline(v=c("+mean+" - 3*" + sdt+ " , " + mean + ", "+ mean + " + 3*" + sdt + "), col=\"red\")"; 
			REXP a = re.eval(mea);
			REXP b = re.eval(sd);
			
			Double mean = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
			Double std = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
			
			meanmap.put(fil_name_key,mean);
			sdtmap.put(fil_name_key,std);
				
	    }
	    
	    re.end();
    }

	public void combinationgraphauthor(String email) {
		
		authormeanmap.clear();
		authorsdtmap.clear();
		
	    File combdir =  new File( rs.Datapath + "//Author//FileCombinations//"+email);
	    File[] files = combdir.listFiles();
	    if(files == null) {
	    	return;
	    }
	    for(File f: files){
	    	String path = f.getAbsolutePath();
	    	//System.out.println(path);
	    	int p = path.lastIndexOf("\\");
	        String fil_name = path.substring(p+1, path.length());
	        int h = fil_name.lastIndexOf(".");
	        String fil_name_key = fil_name.substring(0,h);
	        h = fil_name_key.lastIndexOf(".");
	        fil_name_key = fil_name_key.substring(0,h) + "," + fil_name_key.substring(h,fil_name_key.length()) ;
	        fil_name = "`" + fil_name + "`"; 
	        path = path.replace("\\", "/");
	        //System.out.println(path);
	        String authread = fil_name +" <- read.delim(\""+path+"\", header=FALSE)";
	    	re.eval(authread);
	    	//System.out.println(authread);
			
			String mea = "s <- mean(log10("+fil_name+"$V1/"+fil_name+"$V2))";
			String sd = "d <- sd(log10("+fil_name+"$V1/"+fil_name+"$V2))";
			//String val = "abline(v=c("+mean+" - 3*" + sdt+ " , " + mean + ", "+ mean + " + 3*" + sdt + "), col=\"red\")"; 
			REXP a = re.eval(mea);
			REXP b = re.eval(sd);
			
			Double mean = Double.parseDouble(a.toString().substring(8, a.toString().length()-2));
			Double std = Double.parseDouble(b.toString().substring(8, b.toString().length()-2));
			
			authormeanmap.put(fil_name_key,mean);
			authorsdtmap.put(fil_name_key,std);
				
	    	}
	    
	    	re.end();
	}
	
	
	
	
}
