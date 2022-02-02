package ClientPackage;

import Utils.TransferData;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Font;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class Client {

	private static JFrame frame;
	private static JTextField txtMessage;
	private static JButton btnSend = new JButton("Enviar");
	private static JButton btnExit = new JButton("Salir");
	private static JComboBox<String> cmbClients = new JComboBox<>();
	private static JTextPane txtChat = new JTextPane();
	private static JLabel lblNickname = new JLabel("Chat con: -");
	private static JButton btnAbrirChat;

	private static final int SERVERPORT = 4444;
	private static final String SERVERIP = "localhost";
	private static Socket ownSocket;
	private static ObjectInputStream ois;
	private static ObjectOutputStream ous;
	private static TransferData infoFromServer;
	private static String target = null;
	private static Integer ownID, targetID;
	private static boolean gotID = false;
	private ClientThread clientThread;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Client window = new Client();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtMessage.getText().trim().equals("")) JOptionPane.showMessageDialog(null, "Introduce un mensaje");
				else{
					try{
						//Recogemos datos para enviar al servidor y se lo enviamos
						ous.reset();
						ous.writeObject(new TransferData(infoFromServer.getCurrentsClients(), txtMessage.getText(), ownID, targetID));
						clientThread.addMessages(targetID, "\n"+ownID+">"+txtMessage.getText());
						txtChat.setText(txtChat.getText()+"\n"+ownID+">"+txtMessage.getText());
					}catch (Exception e2){
						e2.printStackTrace();
					}
				}
				txtMessage.setText("");
			}
		});


		btnSend.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnSend.setBounds(419, 409, 155, 41);
		frame.getContentPane().add(btnSend);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});


		btnExit.setBounds(441, 19, 133, 23);
		frame.getContentPane().add(btnExit);

		txtMessage = new JTextField();
		txtMessage.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtMessage.setBounds(24, 409, 385, 41);
		frame.getContentPane().add(txtMessage);
		txtMessage.setColumns(10);


		txtChat.setEditable(false);
		txtChat.setBounds(24, 53, 385, 345);
		frame.getContentPane().add(txtChat);

		JLabel lblCombo = new JLabel("Clientes disponibles");
		lblCombo.setBounds(441, 53, 133, 14);
		frame.getContentPane().add(lblCombo);


		lblNickname.setBounds(24, 23, 219, 14);
		frame.getContentPane().add(lblNickname);

		btnAbrirChat = new JButton("Abrir Chat");
		btnAbrirChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Se habilitan las funcionalidades para el chat y leemos el chat
				txtChat.setEnabled(true);
				txtMessage.setEnabled(true);
				btnSend.setEnabled(true);
				txtChat.setEnabled(true);
				txtChat.setText("");
				clientThread.openChat();
			}
		});
		btnAbrirChat.setBounds(441, 111, 133, 23);
		frame.getContentPane().add(btnAbrirChat);

		try{
			cmbClients = new JComboBox();
			cmbClients.addItem("");
			txtChat.setEnabled(false);
			txtMessage.setEnabled(false);
			btnSend.setEnabled(false);
			cmbClients.setBounds(441, 78, 133, 22);
			frame.getContentPane().add(cmbClients);
			ownSocket = new Socket(SERVERIP, SERVERPORT);
			ous = new ObjectOutputStream(ownSocket.getOutputStream());
			clientThread = new ClientThread();
			clientThread.start();

		}catch(Exception e){
			System.err.println("Error al comunicarse con el Servidor: "+e.getMessage());
		}
	}

	public static class ClientThread extends Thread{

		private HashMap<Integer, ArrayList<String>> messages;

		public ClientThread(){
			messages = new HashMap<Integer, ArrayList<String>>();
		}

		//Se agregan los mensajes al historial del target que se le pasa
		public void addMessages(Integer target, String msg){
			ArrayList<String> targetMessages = messages.get(target);
			if (targetMessages!=null){
				targetMessages.add(msg);
				messages.put(target, targetMessages);
			}else{
				targetMessages = new ArrayList<String>();
				targetMessages.add(msg);
				messages.put(target, targetMessages);
			}
		}

		//Devuelve los mensajes del target el cual sele pase
		public ArrayList<String> getMessages(Integer target){
			return messages.get(target);
		}

		//Lee los mensajes con el id que se le pasa
		public void readMessage(String clientID){
			txtChat.setText("");
			ArrayList<String> listMsg = getMessages(Integer.valueOf(clientID));
			if (listMsg!=null) for (String msg : listMsg) txtChat.setText(txtChat.getText()+msg);

		}

		//Identifica el usuario con el que va a hablar y carga los mensajes
		public void openChat() {
			targetID = Integer.parseInt((String) cmbClients.getSelectedItem());
			lblNickname.setText("Chat con: "+targetID);
			readMessage(String.valueOf(targetID));
		}

		@Override
		public void run() {
			try{
				while (true){
					ois = new ObjectInputStream(ownSocket.getInputStream());
					infoFromServer = (TransferData) ois.readObject();

					//Recogemos nuestro id como cliente del servidor solo la primera vez
					if(!gotID){
						ownID = infoFromServer.getClientID();
						frame.setTitle("Cliente: "+ownID);
						gotID = true;
					}

					//Cargamos la lista de clientes disponibles
					cmbClients.removeAllItems();
					for (Integer currentsClients : infoFromServer.getCurrentsClients()) if(!Objects.equals(currentsClients, ownID)) cmbClients.addItem(String.valueOf(currentsClients));

					//Si el mensaje no es nulo, lo añadimos al historial de mensajes del client target
					if (infoFromServer.getMessage()!=null){
						addMessages(infoFromServer.getTarget(), "\n"+infoFromServer.getTarget()+">"+infoFromServer.getMessage());
						if (targetID==infoFromServer.getTarget()) txtChat.setText(txtChat.getText()+"\n"+infoFromServer.getTarget()+">"+infoFromServer.getMessage());
					}

					//Sirve para cuando recibamos un mensaje ir a ese chat y leemos sus mensajes anteriores si los tiene
					if(infoFromServer.getTarget()!=null){
						for (int i = 0; i < infoFromServer.getCurrentsClients().size(); i++) {
							if(infoFromServer.getTarget()==Integer.parseInt(cmbClients.getItemAt(i))){
								cmbClients.setSelectedIndex(i);
								openChat();
								break;
							}
						}
					}


				}

			}catch(Exception e){
				System.err.println("Error al comunicarse con el Servidor: "+e.getMessage());
			}
		}

	}
}
