package detection;

public class CommitResultObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String email;
	public ResultDatastr totalloc;
	public ResultDatastr locadded;
	public ResultDatastr locremoved;
	public ResultDatastr totalfilechanged;
	public ResultDatastr totalfileadded;
	public ResultDatastr totalfileremoved; 
	public ResultDatastr commitmsg; 
	public ResultDatastr timeofcommit;
	public ResultDatastr filpercentchan;
	public ResultDatastr filpercommit; 
	public ResultDatastr combfrequency;
	public ResultDatastr combprobability; 
	public String Decision;
	public Double Decisionval;
	public String Reason;
	
	public CommitResultObject(String email1, 
			ResultDatastr totalloc1, ResultDatastr locadded1, ResultDatastr locremoved1, 
			ResultDatastr totalfilechanged1, ResultDatastr totalfileadded1, ResultDatastr totalfileremoved1, 
			ResultDatastr commitmsg1, ResultDatastr timeofcommit1, 
			ResultDatastr filpercentchan1, ResultDatastr filpercommit1, 
			ResultDatastr combfrequency1, ResultDatastr combprobability1, 
			String Decision1, Double Decisionval1){
		
		email = email1;
		totalloc = totalloc1;
		locadded = locadded1;
		locremoved = locremoved1;
		totalfilechanged = totalfilechanged1;
		totalfileadded = totalfileadded1;
		totalfileremoved = totalfileremoved1; 
		commitmsg = commitmsg1; 
		timeofcommit = timeofcommit1;
		filpercentchan = filpercentchan1;
		filpercommit = filpercommit1; 
		combfrequency = combfrequency1;
		combprobability = combprobability1; 
		Decision = Decision1;
		Decisionval = Decisionval1;
		
	}
	
}
