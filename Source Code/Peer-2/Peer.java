import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
//Peer class
public class Peer {
	//Register all files in the local
	public static void Check_file(String path, PeerProcedure peerfunction){
		File file = new File(path);
		String test[];
		test = file.list();
		if(test.length!=0){
			for(int i = 0; i<test.length; i++){
			Thread_for_register(test[i],peerfunction);
			}
		}
		//To update automatically
		new AutoUpThread(path,peerfunction);
	}

	//Register the file to the index server
	public static void Thread_for_register(String fileName, PeerProcedure peerfunction){
		Socket socket = null;
		StringBuffer sb = new StringBuffer("register ");
		try{			
			peerSocket peersocket = new peerSocket();
			socket = peersocket.socket;
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
			peerfunction.Add_register(PeerConfFile.localVar.ID, fileName);
			sb.append(PeerConfFile.localVar.ID);
			sb.append(" "+fileName);
			pw.println(sb.toString());
		}catch (IOException e) 
		{ e.printStackTrace(); }
		finally{
			try{
				if(socket!=null){ socket.close();}
					
			}catch(IOException e){ e.printStackTrace(); }		
		}
	}

	public void myExecFunc(PeerProcedure peerfunction)throws IOException{
		
		boolean exit = false;
		String fileName = null;			
		BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
		// Print Peer Menu
		while(!exit)
		{
			System.out.println("\n Choose one option from below Menu:");
			System.out.println("\n1 -> To register all file\n2 -> To search a file\n3 -> To Exit");
			switch(Integer.parseInt(localReader.readLine()))
			{
			case 1:{	
				Check_file(PeerConfFile.localVar.path,peerfunction);				
				break;					
				}
			case 2:{					
				boolean find;
				System.out.println("Enter the file name:");
				fileName = localReader.readLine();
				find = searchThread(fileName, peerfunction);
				// If file is found on the server ask for Download option
				if(find){
					System.out.println("\n Now you can either download or go back. Choose from below Menu:");
					System.out.println("\n1 -> Download the file\n2 -> Cancel and back");
					switch(Integer.parseInt(localReader.readLine())){
					case 1:					
						{
							download(fileName, peerfunction);					
							break;
						}
					default:
							break;			
					}
				 }
				break;
			}
			
			case 3:
			{
				exit = true;
				System.exit(0);
				break;
			}
			default:
				break;
			}
			
		}	
	}
	// TO unregister -> Unregister Thread
	public static void Thread_for_unregister(String fileName, PeerProcedure peerfunction){
		Socket socket = null;	
		System.out.println("in");
		StringBuffer sb = new StringBuffer("unregister ");
		try{			
			peerSocket peersocket = new peerSocket();
			socket = peersocket.socket;
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
			peerfunction.Remove_register(PeerConfFile.localVar.ID, fileName);
			sb.append(PeerConfFile.localVar.ID);
			sb.append(" "+fileName);
			pw.println(sb.toString());
		}catch (IOException e) { e.printStackTrace();
		}
			finally{
			try{
				if(socket!=null){ socket.close();
				}
					
			}catch(IOException e){ e.printStackTrace();
			}
				
		}
	}
	
	public boolean searchThread(String fileName, PeerProcedure peerfunction){
		PeerConfFile peerinfo = new PeerConfFile();
		peerinfo.setInitialValues();
		Socket socket = null;
		StringBuffer sb = new StringBuffer("search ");
		boolean find = false;
		String findID = null;	
		try{			
			peerSocket peersocket = new peerSocket();
			socket = peersocket.socket;
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
			sb.append(PeerConfFile.localVar.ID);
			sb.append(" "+fileName);
			pw.println(sb.toString());
			while(!("bye".equals(findID = br.readLine()))){
				PeerConfFile.destVar.destList.add(findID);							
			}
			if((find = peerfunction.search(fileName))== true){
				for(int i=0; i<PeerConfFile.destVar.destPath.size(); i++){
					System.out.println(fileName+" file exists on "+PeerConfFile.destVar.destPath.get(i));
				}
			}

		}catch (IOException e) { e.printStackTrace();
		}
			finally{
			try{
				if(socket!=null){ socket.close();
				}		
			}catch(IOException e){ e.printStackTrace();
			}		
		}
		return find;
	}
	
	//To download
	public void download(String fileName, PeerProcedure peerfunction){
		
		String IP = null;
		String folder = null;
		int port = 0;
		int sPort = 0;
		String address = PeerConfFile.destVar.destPath.get(0);
		String[] infoArr = address.split("\\:");
		IP = infoArr[0];
		port = Integer.parseInt(infoArr[1]);
		folder = infoArr[2];
		sPort = PeerConfFile.localVar.dPort;
		// Set up a server socket to receive file
		new DownloadThread(sPort,fileName);
		Socket socket = null;
		StringBuffer sb = new StringBuffer("download ");
		try{			
			peerSocket peersocket = new peerSocket(IP, port);
			socket = peersocket.socket;	
			BufferedReader br = peersocket.getReader(socket);
			PrintWriter pw = peersocket.getWriter(socket);
			sb.append(fileName);
			sb.append(" " + sPort);
			sb.append(" " + PeerConfFile.localVar.IP);
			pw.println(sb.toString());
		}catch (IOException e) { e.printStackTrace();
		}
			finally{
			try{
				if(socket!=null){ socket.close();
				}
					
			}catch(IOException e){ e.printStackTrace();
			}
				
		}
	
	}
	// Main function
	public static void main(String args[])throws IOException{
	    PeerProcedure peerfunction = new PeerProcedure();
	    peerfunction.settingValues();
	    ServerSocket server = null;
	    try{
	    	server = new ServerSocket(PeerConfFile.localVar.sPort);
	    	System.out.println("\nPeer has started...");
	    	new PeerThread(server);
	    }catch(IOException e){ e.printStackTrace();
	    }	
		new Peer().myExecFunc(peerfunction);

	}
}

//Download Thread
class DownloadThread extends Thread{
	int port;
	String fileName;
	public DownloadThread(int port,String fileName){
		this.port = port;
		this.fileName = fileName;
		start();
	}
	
	public void run(){
		try {
			ServerSocket server = new ServerSocket(port);
			Socket socket = server.accept();  
            receiveFile(socket,fileName);  
            socket.close();
            server.close();	
		} catch (IOException e) { e.printStackTrace();
		} 
			
	}
	
	public static void receiveFile(Socket socket, String fileName) throws IOException{
		byte[] inputByte = null;  
        int length = 0;  
        DataInputStream dis = null;  
        FileOutputStream fos = null;  
        String filePath = "./peerFileDir/" + fileName;  
        try {  
            try {  
                dis = new DataInputStream(socket.getInputStream());  
                File f = new File("./peerFileDir");  
                if(!f.exists()){  
                    f.mkdir();    
                }  

                fos = new FileOutputStream(new File(filePath));      
                inputByte = new byte[1024];     
                System.out.println("\nStart receiving..."); 
                System.out.println("display file " + fileName);
                while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {  
                    fos.write(inputByte, 0, length);  
                    fos.flush();      
                }  
                System.out.println("Finish receive:"+filePath);  
            } finally {  
                if (fos != null)  
                    fos.close();  
                if (dis != null)  
                    dis.close();  
                if (socket != null)  
                    socket.close();   
            }  
        } catch (Exception e) {  e.printStackTrace();  
        }   
            
	}
}

// Look for any change in folder and automatically register the files if any change
class AutoUpThread extends Thread {
	String path = null;
	PeerProcedure peerfunction = null;
	public AutoUpThread(String path,PeerProcedure peerfunction){
		this.path = path;
		this.peerfunction = peerfunction;
		start();
	}	
	public void run(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {	
				if(PeerConfFile.localVar.fileList.size()!=0){
					for(int i = 0; i < PeerConfFile.localVar.fileList.size(); i++){
						File file = new File(path + File.separator +
								PeerConfFile.localVar.fileList.get(i));
						if(!file.exists()){
							System.out.println(PeerConfFile.localVar.fileList.get(i)+" was removed.");
							Peer.Thread_for_unregister(PeerConfFile.localVar.fileList.get(i),peerfunction);
							
						}
					}
				}
			}
			
		}, 1000, 100);
		     
	}
}
//Peer Server
class peerServer{
	public ServerSocket serversocket;
	public int port;
	public peerServer()throws IOException{
		port = PeerConfFile.localVar.sPort;
		serversocket = new ServerSocket(port);
	}	
	public peerServer(int port)throws IOException{
		this.port = port;
		serversocket = new ServerSocket(port);
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
//Peer socket
class peerSocket{
	public Socket socket=null;
	private PeerProcedure pf = new PeerProcedure();
	public peerSocket()throws IOException{
		pf.settingValues();
		socket = new Socket(PeerConfFile.localVar.IP,PeerConfFile.localVar.cPort);
	}
	public peerSocket(String IP, int port)throws IOException{
		pf.settingValues();
		PeerConfFile.localVar.cPort = port;
		socket = new Socket(IP,PeerConfFile.localVar.cPort);
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
