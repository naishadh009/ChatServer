import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.lang.NullPointerException;

public class ConnectionManager implements Runnable {

	public Socket socket;
	public DataInputStream input;
	public DataOutputStream output;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			try{

				//input and output for texts
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());

				while (true) {
					VerifyConnection();
					String message = "";
					//Manages all incoming messages
					message = input.readUTF();
					
					if(message != null){
						for(int i =1; i <= Chat_server.ConnectionList.size(); i++){
							
							Socket Temp = (Socket)Chat_server.ConnectionList.get(i-1);
							DataOutputStream output = new DataOutputStream(Temp.getOutputStream());
							output.writeUTF(message);
							output.flush();
							
						}
					}
					
				}
			}
			finally{
				socket.close();
			}
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	
	public ConnectionManager(Socket socket){
		this.socket = socket;
	}

	//verifys if the connection is still alive. if not releases the socket and notifies others
	public void VerifyConnection() throws IOException{
		
		if(!socket.isConnected()){
			for(int i =1; i <= Chat_server.ConnectionList.size(); i++){
				if(Chat_server.ConnectionList.get(i) == socket){
					Chat_server.ConnectionList.remove(i);
				}
			}
			
			for(int i =1; i <= Chat_server.ConnectionList.size(); i++){
				
				Socket Temp = (Socket)Chat_server.ConnectionList.get(i-1);
				DataOutputStream output = new DataOutputStream(Temp.getOutputStream());
				output.writeUTF(Temp.getLocalAddress().getHostName()+"has left the room!");
				output.flush();
				
			}
		}
		
	}
}
