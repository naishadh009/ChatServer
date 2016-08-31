import java.awt.EventQueue;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.NullPointerException;
import javax.swing.JTextArea;

public class Chat_server extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private static DataOutputStream output;
	public static ServerSocket socket;

	static JTextArea textArea;
	public static ArrayList<Socket> ConnectionList = new ArrayList<Socket>();
	public static ArrayList<String> ConnectedUsers = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			}
		});
		try {
			int port = 9090;
			socket = new ServerSocket(port);
			// Accept the port
			System.out.println(socket + "\nServer Started");
			while (true) {
				//limit the number of users to 4. Any socket requests after 4 will not be entertained.
				if (ConnectionList.size() < 4) {
					Socket ss = socket.accept();

					ConnectionList.add(ss);
					System.out.println("Client connection" + ss.getLocalPort() + ss.getPort());

					Users(ss);

					//New thread alloted to each client
					ConnectionManager connectionManager = new ConnectionManager(ss);
					Thread connectionThread = new Thread(connectionManager);
					connectionThread.start();
				} 
			} 

		} catch (Exception e) {
			System.out.println("Server disconnected...");
			e.printStackTrace();
		}
	
	}

	/**
	 * Create the frame.
	 */
	public Chat_server() {
		
	}
	
	//Adds users to the list
	public static void Users(Socket user) throws IOException {
		@SuppressWarnings("resource")
		Scanner getUserName = new Scanner(user.getInputStream());
		String name = getUserName.nextLine();
		ConnectedUsers.add(name);
		
		for (int i = 1; i <= ConnectionList.size(); i++) {
			Socket Temp = (Socket) ConnectionList.get(i - 1);
			output = new DataOutputStream(Temp.getOutputStream());
			output.writeUTF("#$%" + ConnectedUsers);
			output.flush();
		}

	}
}
