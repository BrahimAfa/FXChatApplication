package application;


import java.io.*;
import java.net.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
public class ChatController implements Initializable,Observer{
	
	@FXML Button btnConnect;
	@FXML Button btnSend;
	@FXML TextArea txtChatArea;
	@FXML TextField txtMessage;
	@FXML TextField txtPort;
	@FXML TextField txtServer;
	@FXML TextField txtUsername;
	@FXML ListView<String> listConnectedUsers;
	
	StringBuilder strbuilder ;
	DataInputStream dis= null;
	DataOutputStream dos = null;
	String Message ="";
	Socket socket = null;
	String Username = "";

    //public static SimpleListProperty<String> listProperty = new SimpleListProperty<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//listProperty.set(FXCollections.observableArrayList());
		//listProperty.set(FXCollections.observableArrayList());
		//listConnectedUsers.itemsProperty().bind(Server_Socket.ss);
		//listConnectedUsers.getItems().add("HIII :(");
	}
	
	@FXML 
	public void connectClick() {
		Username = txtUsername.getText();
		try {
			int port = Integer.parseInt(txtPort.getText());
			String server = txtServer.getText().isEmpty()?"localhost":txtServer.getText().trim();
			txtServer.setText(server);
			ConnectToServer(port,server);
		} catch (NumberFormatException e) {
			System.out.println("PORT>> Only numbers Accepted....");
		}
	}
	
	@FXML 
	public void sendClick() {
		try {
			
			sendMessage(txtMessage.getText());
		//	addMessageToScreen(txtMessage.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(String Message) throws IOException {
		dos.writeUTF(Message);
		dos.flush();
	}
	
	private void ConnectToServer(int Port,String server) {
		try {
		
			socket = new Socket(server,Port);
			System.out.println("CLIENT :: <"+Username+">:: Connected Successfully");
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(Username);
			Client_Thread CT = new Client_Thread(dis,this);
			CT.setDaemon(true);
			CT.start();
		//	System.out.println(Server_Socket.Server.Sockets.size());
		//	listProperty.set(FXCollections.observableArrayList(Server_Socket.Sockets.values()));
			} catch (IOException e) {
			System.out.println("CLIENT :: Failed To connect");
			e.printStackTrace();

		}

	}
	
	public synchronized void addMessageToScreen(String Message){	
		txtChatArea.appendText(Message+'\n');
	}
	
	public synchronized void addUsersConnected(List<String> users){	
		
		for (String user : users) {
			
			listConnectedUsers.getItems().add(user);
		}
		
		//listProperty.set(FXCollections.observableArrayList(users));
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("hello"+arg);
	}


}
