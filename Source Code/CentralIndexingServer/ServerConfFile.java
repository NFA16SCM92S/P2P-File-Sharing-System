//Creating config file for Server
public class ServerConfFile {
	private String peerId;
	private String fileName;
	public ServerConfFile(){
	//default	
	}
	public ServerConfFile(String peerId,String fileName){
		this.peerId = peerId;
		this.fileName = fileName;
	}
	public String getID(){
		return peerId;
	}
	public String getName(){
		return fileName;
	}
	public void setID(String peerId){
		this.peerId = peerId;
	}	
	public void setName(String fileName){
		this.fileName = fileName;
	}

}
