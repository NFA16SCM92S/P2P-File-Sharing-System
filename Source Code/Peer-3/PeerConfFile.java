import java.util.ArrayList;
public class PeerConfFile {
	//Local variables
	public static class localVar{
		public static int sPort = 9791;
		public static int cPort = 8881;
		public static int dPort = 11311;
		public static String IP = "";	
		public static String name = "";
		public static String ID = "";
		public static String path = "./peerFileDir";
		public static ArrayList<String> fileList = new ArrayList<String>();
	}
	//Destination variables
	public static class destVar{
		public static ArrayList<String> destList = new ArrayList<String>();
		public static String destination = "127.0.0.1:8881";
		public static ArrayList<String> destPath = new ArrayList<String>();
		public static String path = "./peerFileDir";
	}
	public void setInitialValues(){
		PeerConfFile.destVar.destination = "";
		PeerConfFile.destVar.destPath = new ArrayList<String>();
		PeerConfFile.destVar.destList = new ArrayList<String>();
	
	}
	
}
