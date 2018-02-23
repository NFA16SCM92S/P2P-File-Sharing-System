import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread{
	private BufferedReader br;
	private PrintWriter pw;
	private Socket connectToClient;
	private ServerMethod serverfunction;
	public ServerThread(Socket soc,ServerMethod serverfunction)throws IOException{
		super();
		connectToClient = soc;
		this.serverfunction = serverfunction;
		br = getReader(connectToClient);
		pw = getWriter(connectToClient);
		start();
	}
	//Socket Utils
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
			String message = null;
			while((message = br.readLine())!=null){
				StringTokenizer st = new StringTokenizer(message);
				System.out.println(message);
				String command = st.nextToken();
				String ID = st.nextToken();
				String fileName = st.nextToken();
				if("register".equals(command)){
					serverfunction.registery(ID, fileName);
					
				}else if("unregister".endsWith(command)){
					serverfunction.unregistery(ID, fileName);
					
				}else if("search".equals(command)){
					ArrayList<String> peerList = new ArrayList<String>();
					peerList = serverfunction.search(fileName);
					if(peerList.size() != 0){
						for(int i =0; i<peerList.size();i++){
							pw.println(peerList.get(i));
						}
					}	
					pw.println("bye");
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
