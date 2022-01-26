package ClientPackage;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JLabel;

public class Client {

	private JFrame frame;
	private static JTextField txtMessage;
	private static JButton btnSend = new JButton("Enviar");
	private static JButton btnExit = new JButton("Salir");
	private static JComboBox cmbClients = new JComboBox();
	private static JTextPane txtChat = new JTextPane();
	private static JLabel lblNickname = new JLabel("Cliente:");
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
		
		
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnSend.setBounds(419, 409, 155, 41);
		frame.getContentPane().add(btnSend);
		
		
		btnExit.setBounds(441, 19, 133, 23);
		frame.getContentPane().add(btnExit);
		
		
		cmbClients.setBounds(441, 78, 133, 22);
		frame.getContentPane().add(cmbClients);
		
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
	}
}
