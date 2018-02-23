import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerMethod {
	private ArrayList<ServerConfFile> registryList = new ArrayList<ServerConfFile>(); 
	// Registering file and then implementing multiuser register file at the same time using multithreading
	public void registery(String peerId, String fileName){
		registerThread register = new registerThread(peerId, fileName);
		Thread thread = new Thread(register);
		thread.start();
		thread = null;
	}
	class registerThread implements Runnable{
		private String peerId;
		private String fileName;	
		public registerThread(String peerId, String fileName){
			this.peerId = peerId;
			this.fileName = fileName;
		}
		@Override
		public void run() {	
			if(registryList.size()==0){			
				try{					
					FileWriter writer = new FileWriter("./mylogFile.txt",true);
					// Add register file to the registery list
		            registryList.add(new ServerConfFile(peerId,fileName));
					System.out.println(""+fileName+" file has been registered from "+"Client:"+peerId+"");
					
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = df.format(new Date());
					writer.write(time + "\t"+fileName + " file is registered on the Central Index server!\r\n");
					writer.close();		
				}catch(Exception e) {e.printStackTrace();}
						
			}
			else{
				try{
					if(noFileExist(peerId,fileName)){	
						FileWriter writer = new FileWriter("./mylogFile.txt",true);
						registryList.add(new ServerConfFile(peerId,fileName));
						System.out.println(""+fileName+" file has been registered from "+"Client:"+peerId+"");
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = df.format(new Date());
						writer.write(time + "\t"+fileName + " file is registered on the Central Index server!\r\n");
						writer.close();
					}
				}catch(Exception e){ e.printStackTrace();}
			}	
		}
		
	}
	// check if the file exists
	private boolean noFileExist(String peerId, String fileName) {	
		for(int i=0;i<registryList.size();i++){
			if(registryList.get(i).getName().equals(fileName)&&
					registryList.get(i).getID().equals(peerId)){
				return false;
			}
		}
		return true;
		
	}

	// TO unregister the file using one user and multiple user at the same time by implementing
	// multithreading
	public void unregistery(String peerId, String fileName){		
		unregisteryThread unregister = new unregisteryThread(peerId, fileName);
		Thread thread = new Thread(unregister);
		thread.start();
		thread = null;
	}
	class unregisteryThread implements Runnable{
		private String peerId;
		private String fileName;
		public unregisteryThread(String peerId, String fileName){
			this.peerId = peerId;
			this.fileName = fileName;
		}
		@Override
		public void run() {	
			for(int i=0;i<registryList.size();i++){
				try{		
					if(registryList.get(i).getName().equals(fileName)&&
						registryList.get(i).getID().equals(peerId)){
						FileWriter writer = new FileWriter("./mylogFile.txt",true);
						registryList.remove(i);
						System.out.println(""+fileName+" file has been removed from "+"Client:"+peerId+"");
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String time = df.format(new Date());
						writer.write(time + "\t"+fileName + "file is unregistered on the Central index server!\r\n");
						writer.close();
					}
				}catch(Exception e){ e.printStackTrace();}
			}
		}
		
	}
	
// To search for the given file on Central Indexing Server 
// 1-> for single user search
// 2 -> multiuser search using search
	public ArrayList<String> search(String fileName) {
		ArrayList<String> peerList = new ArrayList<String>();
		ExecutorService execPool = Executors.newCachedThreadPool();
		Callable<ArrayList<String>> call = new searchThread(fileName);
		Future<ArrayList<String>> result = execPool.submit(call);
		try{
			if(result.get().size() != 0)
				peerList = (ArrayList<String>) result.get().clone();
		}catch (InterruptedException e) { e.printStackTrace(); }
		catch(ExecutionException e){ e.printStackTrace();}
		finally{execPool.shutdown();}
		return peerList;
	}
	
	class searchThread implements Callable<ArrayList<String>>{
		private ArrayList<String> peerList = new ArrayList<String>();
		private String fileName;		
		public searchThread(String fileName){
			this.fileName = fileName;
		}
		@Override
		public ArrayList<String> call() throws Exception {			
			for(int i=0;i<registryList.size();i++){
				if(registryList.get(i).getName().equals(fileName)){
					peerList.add(registryList.get(i).getID());					
				}
			}
			return peerList;
		}
		
	}

}
