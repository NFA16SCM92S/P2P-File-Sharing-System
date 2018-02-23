Please follow below Steps to execute
1) I have created, compiled and executed this code on AWS Ubuntu 16.04 server.
2) Source Code is in “Source Code” folder. Deploy the code in this folder into different directories. There are 4 directories:
 CentralIndexingServer
 Peer-1
 Peer-2
 Peer-3
3) To start the Central indexing Server. Open CentralIndexingServer directory on a terminal and compile the code. You can do that using below two commands
 $ make clean; make
 $ java Server
A message will be displayed “Server Started at port:8881”
4) To start Peer-1. Open Peer-1 directory in another terminal and run below commands
 $ make clean; make
 $ java Peer
Below message will be displayed:
Peer has started...
Choose one option from below Menu:
1 -> To register all file
2 -> To search a file
3 -> To Exit
5) Similarly Start Peer-2 and Peer-3 on different terminals. You will have 4 open terminals at this time.
6) After the Peer-1 is successfully connected to the Server, It will ask your input to perform some operations such as,
1 -> To register all file
2 -> To search a file
3 -> To Exit
a) If you choose 1-> it will register all the files under peerFileDir to the Server.
Peer has started...
Choose one option from below Menu:
1 -> To register all file
2 -> To search a file
3 -> To Exit
1
p1.3.txt file is registered: Done
p1.4.txt file is registered: Done
p1.10.txt file is registered: Done
p1.1.txt file is registered: Done
p1.8.txt file is registered: Done
p1.2.txt file is registered: Done
p1.9.txt file is registered: Done
p1.6.txt file is registered: Done
p1.7.txt file is registered: Done
p1.5.txt file is registered: Done
Choose one option from below Menu:
1 -> To register all file
2 -> To search a file
3 -> To Exit
b) This operation to register Peer-1 files will display below messages on the Server terminal:
r$ java Server
Server Started at port:8881
Accepting New Connection...
register 172.31.80.120:9991 p1.3.txt
Accepting New Connection...
p1.3.txt file has been registered from Client:172.31.80.120:9991
Accepting New Connection...
Accepting New Connection...
Accepting New Connection...
Accepting New Connection...
register 172.31.80.120:9991 p1.4.txt
Accepting New Connection...
register 172.31.80.120:9991 p1.1.txt
register 172.31.80.120:9991 p1.10.txt
register 172.31.80.120:9991 p1.8.txt
p1.4.txt file has been registered from Client:172.31.80.120:9991
p1.1.txt file has been registered from Client:172.31.80.120:9991
Accepting New Connection...
Accepting New Connection...
register 172.31.80.120:9991 p1.2.txt
p1.8.txt file has been registered from Client:172.31.80.120:9991
register 172.31.80.120:9991 p1.9.txt
Accepting New Connection...
p1.10.txt file has been registered from Client:172.31.80.120:9991
p1.9.txt file has been registered from Client:172.31.80.120:9991
register 172.31.80.120:9991 p1.6.txt
p1.2.txt file has been registered from Client:172.31.80.120:9991
p1.6.txt file has been registered from Client:172.31.80.120:9991
register 172.31.80.120:9991 p1.7.txt
register 172.31.80.120:9991 p1.5.txt
p1.7.txt file has been registered from Client:172.31.80.120:9991
p1.5.txt file has been registered from Client:172.31.80.120:9991
NOTE: For convenience, I have logged all the activities in the log files. For CentralIndexing Server, log file = myLogFile.txt, and for all the three Peers log file name is myPeerLog.txt, which resides under Peer-1, Peer-2 and Peer-3 directories.
c) If you choose option 2 -> It will prompt for file name and search for that particular file and display the list of peers.
i. Now you will get 2 operation options to perform on this file: You can either choose to download the file or go back.
ii. To download the file choose 1
Choose one option from below Menu:
1 -> To register all file
2 -> To search a file
3 -> To Exit
2
Enter the file name:
p1.8.txt
p1.8.txt file exists on 172.31.80.120:9991:peerFileDir
Now you can either download or go back. Choose from below Menu:
1 -> Download the file
2 -> Cancel and back
1
Choose one option from below Menu:
1 -> To register all file
2 -> To search a file
3 -> To Exit
Start receiving...
display file p1.8.txt
Finish receive:./peerFileDir/p1.8.txt
Success
d) If you search for a file that doesn’t exist it will display an error message.
Choose one option from below Menu:
1 -> To register all file
2 -> To search a file
3 -> To Exit
2
Enter the file name:
dfg.txt
dfg.txt file does not exist.
Choose one option from below Menu:
1 -> To register all file
2 -> To search a file
3 -> To Exit
e) If you choose 3 -> It Peer will exist, and program execution will stop.
7) For concurrent execution of all the peers, please perform above step 6(a to e) for Peers-2 and Peer-3.