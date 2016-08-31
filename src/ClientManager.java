import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

import java.lang.NullPointerException;


public class ClientManager implements Runnable {

	public Socket socket;
	public DataInputStream input;
	public DataOutputStream output;
	public String user = "";

	// -----------------------------------------------------------------------------------------------//

	public void run() {
		// TODO Auto-generated method stub
		try {
			try {
				//manages all incoming messages
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				String name = "";
				while (name != null) {
					//string parsing. Java built in method
					name = input.readUTF();
					if (name.contains("#$%")) {
						String Temp1 = name.substring(3);
						Temp1 = Temp1.replace("[", "");
						Temp1 = Temp1.replace("]", "");

						String[] names = Temp1.split(",");

						Client.list.setListData(names);
					}
					else {
						Client.textArea.setText(Client.textArea.getText() + "\n" + name.toString());
					}
				}

			} finally {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------------------------------
	/**
	 * @wbp.parser.entryPoint
	 */
	public ClientManager(Socket socket) {
		this.socket = socket;
	}
	//-------------------------------------------------------------------------------------------------//

	// ---------------------------------------------------------------------------------------------------//
	//Send btn functionality
	public void ClientSend(JTextField textField, JTextArea textArea) {

		try {
			String userOutput = "";
			if (textField.getText() != null || !textField.getText().equals("")) {
				userOutput += textField.getText();
				textField.requestFocus();
				textField.setText("");
				output = new DataOutputStream(socket.getOutputStream());
				output.writeUTF(Client.name+": "+userOutput);
				output.flush();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	// -----------------------------------------------------------------------------------------------//
	//Disconnect functionality
	public void ClientDisconnect() {
		// TODO Auto-generated method stub
		try {
			output = new DataOutputStream(socket.getOutputStream());
			output.writeUTF(Client.name+" has left the room");
			output.flush();
			
			
		} catch (Exception e) {
			System.out.println("Client Disconnected");
			// e.printStackTrace();
		}
	}
	// ---------------------------------------------------------------------------------------------------//

	//sends the user input name
	/**
	 * @wbp.parser.entryPoint
	 */
	public void SendName(String name) throws IOException {
		// TODO Auto-generated method stub
		output = new DataOutputStream(socket.getOutputStream());
		output.writeUTF(name);
		output.flush();
	}
	
}
