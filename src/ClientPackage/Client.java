package ClientPackage;

import Utils.ClientInfo;

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


public class Client {

	private JFrame frame;
	private static JTextField txtMessage;
	private static JButton btnSend = new JButton("Enviar");
	private static JButton btnExit = new JButton("Salir");
	private static JComboBox<String> cmbClients = new JComboBox<>();
	private static JTextPane txtChat = new JTextPane();
	private static JLabel lblNickname = new JLabel("Cliente:");
	private static JButton btnAbrirChat;

	private static final int SERVERPORT = 4444;
	private static final String SERVERIP = "localhost";
	private static ObjectInputStream ois;
	private static ObjectOutputStream ous;
	private static ClientInfo infoFromServer;
	private static String target = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
						ous.reset();
						ous.writeObject(new ClientInfo(infoFromServer.getCurrentsClients(), txtMessage.getText(), target.substring(0, target.indexOf(":")), Integer.valueOf(target.substring(target.indexOf(":")+1,target.length() ))));
						txtChat.setText(txtChat.getText()+"\n"+txtMessage.getText());
						txtMessage.setText("");
					}catch (Exception e2){
						e2.printStackTrace();
					}
				}
			}
		});
		
		
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnSend.setBounds(419, 409, 155, 41);
		frame.getContentPane().add(btnSend);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
				String client= (String) cmbClients.getSelectedItem();
				if (client.equals("")) JOptionPane.showMessageDialog(null, "Selecciona un cliente valido");
				else{
					target = client;
					txtChat.setEnabled(true);
					txtMessage.setEnabled(true);
					btnSend.setEnabled(true);
				}
			}
		});
		btnAbrirChat.setBounds(441, 111, 133, 23);
		frame.getContentPane().add(btnAbrirChat);

		try{
			Socket ownSocket = new Socket(SERVERIP, SERVERPORT);
			ois = new ObjectInputStream(ownSocket.getInputStream());
			ous = new ObjectOutputStream(ownSocket.getOutputStream());

			infoFromServer = (ClientInfo) ois.readObject();
			cmbClients = new JComboBox();
			cmbClients.addItem("");
			txtChat.setEnabled(false);
			txtMessage.setEnabled(false);
			btnSend.setEnabled(false);
			cmbClients.setBounds(441, 78, 133, 22);
			frame.getContentPane().add(cmbClients);

			for (String currentsClients : infoFromServer.getCurrentsClients()) {
				if(!currentsClients.equals(ownSocket.getInetAddress().getHostAddress()+":"+ownSocket.getLocalPort())) {
					System.out.println(currentsClients);
					cmbClients.addItem(currentsClients);
				}
			}
		}catch(Exception e){
			System.err.println("Error al comunicarse con el Servidor: "+e.getMessage());
		}
	}
}
