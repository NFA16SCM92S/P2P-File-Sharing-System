import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

//Obtaining file from other Peers
class ObtainThread extends Thread{
	
	int port = 0;
	String fileName = null;
	String IP = null;
	public ObtainThread(String fileName, String IP, int port){
		this.fileName = fileName;
		this.IP = IP;
		this.port = port;
		start();
	}
	
	public void run(){
		Socket socket = null;  
        DataOutputStream dos = null;
		int length = 0;  
        double sumL = 0 ;  
        byte[] sendBytes = null;   
        FileInputStream fis = null;  
        boolean bool = false;
     
        try {  
            File file = new File("./peerFileDir/" + fileName); 
            long l = file.length();   
            socket = new Socket(IP,port);                
            dos = new DataOutputStream(socket.getOutputStream());  
            fis = new FileInputStream(file);        
            sendBytes = new byte[1024];   
            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {  
                sumL=sumL+ length;               
                System.out.println("Sent:"+((sumL/l)*100)+"%");
                dos.write(sendBytes, 0, length);  
                dos.flush();  
            }   
            if(sumL==l){  
                bool = true;  
            }  
            
        }catch (Exception e) {  
            System.out.println("error");  
            bool = false;  
            e.printStackTrace();    
        }finally{    
            if (dos != null)
				try {
					dos.close();
				} catch (IOException e) { e.printStackTrace();
				} 			 
            if (fis != null)
				try {
					fis.close();
				} catch (IOException e) { e.printStackTrace();
				}  	   
            if (socket != null)
				try {
					socket.close();
				} catch (IOException e) { e.printStackTrace();
				}   					
					   
        }  
        System.out.println(bool?"Success":"Fail");  
        
	}
}
// PeerThread Class
public class PeerThread extends Thread{
	private ServerSocket serversocket;
	public PeerThread(ServerSocket serversocket)throws IOException{
		super();
		this.serversocket = serversocket;	
		start();
	}	
	public void run(){  
	    Socket socket = null;  
          
		try{
			while(true){
	
				socket = serversocket.accept();
				new clientThread(socket);		
				
			}
		}catch(Exception e){e.printStackTrace();}
			finally{
			try{
				if(socket!=null){ socket.close();}
			}catch(IOException e){ e.printStackTrace();}
		}
	}
}

class clientThread extends Thread{
	private PrintWriter pw;
	private Socket connectToClient;
	private BufferedReader br;	
	public clientThread(Socket soc)throws IOException{
		super();
		connectToClient = soc;
		br = getReader(connectToClient);
		pw = getWriter(connectToClient);
		start();
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
	public void run(){
		try{			
			String msg = null;
			
			while((msg = br.readLine())!=null){
				StringTokenizer st = new StringTokenizer(msg);
				String command = st.nextToken();
				String fileName = st.nextToken();
				int port = Integer.parseInt(st.nextToken());
				String IP = st.nextToken();

	            if("download".equals(command)){
	            	// Create a receive file thread 
	            	new ObtainThread(fileName, IP, port);    	
				}	
			}		
		}catch(Exception e){ e.printStackTrace();}
		
	}

}



