package org.graphstream.distributed.stream.old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DistributedGraphFileDispatch {


	public DistributedGraphFileDispatch() {
	}

	//"Usage : dispatch <infos.ftp> <fileName1> <instance1> ... <fileNameX> <instanceX>");
	public void run(String[] args) {
		// Traitement des param�tres
		String ftpFile = args[0];
		String[] instX = new String[(args.length-1)/2];
		String[] fileNameX = new String[(args.length-1)/2];

		int j = 0 ;
		for(int i = 1 ; i < args.length ; i=i+2) {
			fileNameX[j] = args[i];
			instX[j] = args[i+1];
			j++;
		}

		// Recherche des fichiers
		//List<String> files = listerRepertoire(".", (fileId+"-"+"g*.dgs"));

		// lecture ssh account
		String[][] ftpTab = readFile(ftpFile);
		HashMap<String, SshAccount> ssh = new HashMap<String, SshAccount>();

		for(int i = 0 ; i < ftpTab.length ; i++) {
			ssh.put(ftpTab[i][0], new SshAccount(ftpTab[i]));
		}

		// dispatch des fichiers
		for(int i = 0 ; i < instX.length ; i++) {
			sendSftpFile(	ssh.get(instX[i]).getHost(),
							ssh.get(instX[i]).getUser(),
							ssh.get(instX[i]).getPass(),
							fileNameX[i],
							ssh.get(instX[i]).getPath());
		}


	}


	public void sendSftpFile(String host, String user, String pass, String fileName, String remotePath) {
		try {
			JSch jsch = new JSch();
			int port = 22;
			Session session = jsch.getSession(user, host, port);
			UserInfo ui = new MyUserInfo(pass);
		    session.setUserInfo(ui);

		    session.connect();

		    Channel channel = session.openChannel("sftp");
		    channel.connect();
		    ChannelSftp c = (ChannelSftp)channel;

		    c.cd(remotePath);
		    File fi = new File(fileName);
		    InputStream in2 = new FileInputStream(fi);
		    c.put(in2, fileName);

		    session.disconnect();
		}
		catch(Exception e){
		      System.out.println("Error : " + e);
		}
	}


	public String[][] readFile(String fileName) {
	    try {
	    	String[][] res = null ;
	    	String ligne;
	    	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

	    	int c;
	        for (c=0;br.readLine() != null;c++) {}
	        res = new String[c][] ;
	        br.close();

	        br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
	        c = 0 ;
	    	while ((ligne=br.readLine())!=null) {
	    		res[c] = ligne.split(";");
	    		c++ ;
	    	}
	    	br.close();
	    	return res ;
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	      return null ;
	    } catch (IOException e) {
	      e.printStackTrace();
	      return null ;
	    }
	}


}

class MyUserInfo implements UserInfo {

	private String pass ;

	// Constructor

	public MyUserInfo(String thePass) {
		this.pass = thePass ;
	}

	// Modifiers

	public boolean promptPassword(String arg0) {
		return true ;
	}

	public boolean promptPassphrase(String arg0) {
		return false ;
	}

	public boolean promptYesNo(String value) {
		return true ;
		/*if (value.equalsIgnoreCase("yes")) return true ;
		else return false ;*/
	}

	public void showMessage(String arg0) {

	}

	// Accessors

	public String getPassphrase() {
		  return null ;
	}

	public String getPassword() {
		return pass ;
	}

}

/**
 * Classe SSH account
 * */

class SshAccount {

	String id ;
	String user ;
	String pass ;
	String host ;
	String path ;

	public SshAccount(String[] data) {
		this.id = data[0];
		this.host = data[1];
		this.user = data[2];
		this.pass = data[3];
		this.path = data[4];
	}

	public String getId() {
		return this.id ;
	}

	public String getUser() {
		return this.user ;
	}

	public String getPass() {
		return this.pass ;
	}

	public String getHost() {
		return this.host ;
	}

	public String getPath() {
		return this.path ;
	}

	public List<String> listerRepertoire(String path, String filtre){
		try	{
			Pattern p = Pattern.compile(filtre);
			String [] s = new File(path).list();
			List<String> listeFichiers = new ArrayList<String>();
			for (int i=0; i<s.length;i++) {
				Matcher m = p.matcher(s[i]);
				if ( m.matches()) {
					System.out.println(m.group(1));
					listeFichiers.add(s[i]);
				}
			}
			return listeFichiers ;
		}
		catch (PatternSyntaxException pse) {
			pse.printStackTrace();
			return null ;
		}
	}

}

