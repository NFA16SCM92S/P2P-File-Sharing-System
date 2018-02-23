import java.io.*;
import java.net.*;
//This file consists of two Classes: centralindexServer and Server
class centralindexServer{
	public ServerSocket serversocket;
	public int port;
	public centralindexServer()throws IOException{
		port = 8881;
		serversocket = new ServerSocket(port);
		System.out.println("Server Started at port:"+ port);
	}
	public centralindexServer(int port)throws IOException{
		this.port = port;
		serversocket = new ServerSocket(port);
		System.out.println("Server Started at port:" + port);
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
}
public class Server {
	public void serverOps() throws IOException{
		ServerMethod serverfunction = new ServerMethod();
		ServerSocket serverSocket;
		centralindexServer indexserver = new centralindexServer();
		serverSocket = indexserver.serversocket;
		try{
			Socket socket = null;
			while(true){
				socket = serverSocket.accept();
			System.out.println("Accepting New Connection... ");
				new ServerThread(socket,serverfunction);
			}
		}catch(Exception e){ e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args)throws IOException{
		new Server().serverOps();
	}
}