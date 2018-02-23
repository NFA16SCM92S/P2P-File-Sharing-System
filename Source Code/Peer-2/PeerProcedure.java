import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PeerProcedure {
	//Initializing the variables
	public void settingValues(){
		try{
			
			PeerConfFile.localVar.cPort = 8881;
			PeerConfFile.localVar.sPort = 9891;
			PeerConfFile.localVar.dPort = 11211;
			InetAddress addr = InetAddress.getLocalHost();
			PeerConfFile.localVar.IP = addr.getHostAddress();
			PeerConfFile.localVar.name = addr.getHostName();
			PeerConfFile.localVar.ID = PeerConfFile.localVar.IP + ":" + PeerConfFile.localVar.sPort;
		}catch(Exception e){ e.printStackTrace();
		}
			
	}
	//Registering files to local
	public void Add_register(String ID, String filename){
		FileWriter writer = null;
		try{
			writer = new FileWriter("./myPeerLog.txt",true);
			PeerConfFile.localVar.fileList.add(filename);
			System.out.println(""+filename + " file is registered: Done");	
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			writer.write(time + "\t"+filename + " file is registered on the central index server:Done\r\n");
			writer.close();		

		}catch(Exception e){e.printStackTrace();
		}		
	}
	//Unregistering file from local
	public void Remove_register(String ID, String filename){
		FileWriter writer = null;
		try{
		
			writer = new FileWriter("./myPeerLog.txt",true);
			PeerConfFile.localVar.fileList.remove(filename);		
			System.out.println(""+filename + " file is unregistered:Done");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			writer.write(time + "\t"+filename + " file is unregistered on the central index server:Done\r\n");
			writer.close();	
		}catch(Exception e){ e.printStackTrace();
		}		
	}
	// Searching file on local
	public boolean search(String filename){
		boolean filePresent = false;
		FileWriter writer = null;
		try{
			writer = new FileWriter("./myPeerLog.txt",true);
			if(PeerConfFile.destVar.destList.size()!=0){	
				for(int i=0; i<PeerConfFile.destVar.destList.size(); i++){
					String destination = PeerConfFile.destVar.destList.get(i);
					String[] infoArr = destination.split("\\:");
					String IP = infoArr[0];
					String port = infoArr[1];
					PeerConfFile.destVar.destination = destination + ":peerFileDir";
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = df.format(new Date());
					writer.write(time + "\t"+filename + " file exists on "+IP+" .\r\n");
					PeerConfFile.destVar.destPath.add(PeerConfFile.destVar.destination);
				}			
				filePresent = true;
			}else{
				System.out.println(""+filename + " file does not exist.");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = df.format(new Date());
				writer.write(time + "\t"+filename + " file does not exist.\r\n");
			}
			writer.close();
		}catch(IOException e){ 	e.printStackTrace();
		}	
		return filePresent;
	}
	//Socket utils
	public PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream sOut = socket.getOutputStream();
		return new PrintWriter(sOut, true);
	}
	public BufferedReader getReader(Socket socket)throws IOException{
		InputStream sIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(sIn));	
	}
	
}
