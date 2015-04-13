package detection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import settings.RepoSettings;

public class CommitResultSerializer {

	RepoSettings rs;
	
	public void initiate(RepoSettings rs1) throws IOException{
		rs = rs1;
	}
	
	@SuppressWarnings("unchecked")
	public void write(Map<String,CommitResultObject> mscro) throws IOException, ClassNotFoundException {
		File resultobjectfile = new File(rs.Resultpath+"//resultobject.data");
		Map<String,CommitResultObject> oldmscro = new HashMap<String,CommitResultObject>();
		if(resultobjectfile.exists()){
			FileInputStream f_in = new FileInputStream(resultobjectfile);
			ObjectInputStream obj_in = new ObjectInputStream (f_in);
			Object obj = obj_in.readObject();
			oldmscro = (Map<String, CommitResultObject>)obj;
			obj_in.close();
		}
		
		mscro.putAll(oldmscro);
		
		FileOutputStream f_out = new FileOutputStream(resultobjectfile);
		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
		obj_out.writeObject (mscro);
		obj_out.close();
	}
}
