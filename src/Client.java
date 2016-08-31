import java.awt.EventQueue;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.lang.NullPointerException;

public class Client extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static String name = "";
	public static JList<String> list;
	private JTextField textField;
	private JTextField textField_1;
	protected static JTextArea textArea;
	private static ClientManager chatClient;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JButton btnSend;
	protected static JButton btnSelect;

//-------------------------------------------------------------------------------------------------//
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Client().setVisible(true);
			}
		});

//---------------------------------------------------------------------------------------------------//
	}
	/**
	 * Create the frame.
	 */
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 596, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 11, 343, 139);
		contentPane.add(textArea);
		
		textField = new JTextField();
		textField.setBounds(10, 161, 343, 50);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.requestFocus();

//--------------------------------------------------------------------------------------------------//
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Client Btn");
				chatClient.ClientSend(textField, textArea);
			}
		});
		btnSend.setBounds(366, 12, 89, 23);
		btnSend.setEnabled(false);
		contentPane.add(btnSend);
		
		textField_1 = new JTextField();
		textField_1.setBounds(194, 223, 159, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		textField_1.setEditable(false);
		
		JLabel lblName = new JLabel("Name : ");
		lblName.setBounds(145, 226, 46, 14);
		contentPane.add(lblName);
//-------------------------------------------------------------------------------------------------//		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				LogIn();
				//chatClient.ClientConnect();
				System.out.println("Client Connected");
			}
		});
		btnConnect.setBounds(363, 61, 89, 23);
		contentPane.add(btnConnect);
		
//-----------------------------------------------------------------------------------------------------//
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				chatClient.ClientDisconnect();
				System.out.println("Client Disconnected");
			}
		});
		btnDisconnect.setBounds(366, 112, 89, 23);
		btnDisconnect.setEnabled(false);
		contentPane.add(btnDisconnect);
		
		list = new JList<String>();
		//Chat_server.ConnectedUsers.add(name);
		list.setBounds(472, 11, 87, 207);
		list.setLayoutOrientation(JList.VERTICAL);
		contentPane.add(list);
		
		JLabel lblOnline = new JLabel("Online");
		lblOnline.setBounds(493, 231, 46, 14);
		contentPane.add(lblOnline);
		
		/* JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Client.selection();
				System.out.println(name + "Selected");
				name = (String)JOptionPane.showInputDialog(null, "Enter your name", null, JOptionPane.PLAIN_MESSAGE, null, null, "");
				//if(name != null && name.length() > 0){
					//textField1.getText(name);
					
				
			}
		});
		btnSelect.setBounds(366, 161, 89, 23);
		contentPane.add(btnSelect); */
	} 
	
	//accepts the username
	protected void LogIn() {
		// TODO Auto-generated method stub
		name = (String)JOptionPane.showInputDialog(null, "Enter your name", "Set Name", JOptionPane.PLAIN_MESSAGE, null, null, "");
		if(name != null && name.length() > 0){
			textField_1.setText(name);
			ClientConnect();
			btnConnect.setEnabled(false);
			btnDisconnect.setEnabled(true);
			btnSend.setEnabled(true);
		}
	}
	
	//connects the client to the server
	private void ClientConnect() {
		// TODO Auto-generated method stub

		try {
			// Server port JAVA - Preferences - Networking.
			Socket serverSocket = new Socket("127.0.0.1", 9090);
			System.out.println(serverSocket + "\n Client connected to Server");

			chatClient = new ClientManager(serverSocket);
			
			PrintWriter out = new PrintWriter(serverSocket.getOutputStream());
			System.out.println("Send :"+name);
			out.println(name);
			out.flush();
			
			//threads each client to a management loop
			Thread clientThread = new Thread(chatClient);
			clientThread.start();
			
		} catch (Exception e) {
			System.out.println("Client disconnected..");

		}
		
	}
//----------------------------------------------------------------------------------------------//

	public static void selection(){
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
		      public void valueChanged(ListSelectionEvent listSelectionEvent) {
		        java.util.List<String> objs = list.getSelectedValuesList();
		      }
		    };
		    list.addListSelectionListener(listSelectionListener);
		   
	}
}
