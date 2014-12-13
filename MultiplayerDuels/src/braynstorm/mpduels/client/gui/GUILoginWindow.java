package braynstorm.mpduels.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import braynstorm.mpduels.client.Client;
import braynstorm.mpduels.common.Packet;

public class GUILoginWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9034401688398598195L;
	private JPanel contentPane;
	private JTextField loginUsername;
	private JPasswordField loginPassword;
	private JPasswordField registerPassword1;
	private JTextField registerUsername;
	private JPasswordField registerPassword2;

	public static Thread gameWindow;
	public static GUIGameWindow gw;
	
	public static GUILoginWindow loginWindow;
	private JLabel errorLabelRegister;
	private JLabel errorLabelLogin;
	
	/**
	 * Create the frame.
	 */
	public GUILoginWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 284, 261);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(2, 0, 256, 211);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Log In", null, panel, null);
		panel.setLayout(null);
		
		loginPassword = new JPasswordField();
		loginPassword.setBounds(77, 39, 166, 19);
		panel.add(loginPassword);
		loginPassword.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 40, 63, 19);
		panel.add(lblPassword);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 12, 63, 19);
		panel.add(lblUsername);
		
		loginUsername = new JTextField();
		loginUsername.setBounds(77, 11, 166, 19);
		panel.add(loginUsername);
		loginUsername.setColumns(10);
		
		JButton butLogIn = new JButton("Log In");
		butLogIn.setBounds(77, 64, 166, 23);
		panel.add(butLogIn);
		
		errorLabelLogin = new JLabel("");
		errorLabelLogin.setBounds(77, 98, 166, 14);
		panel.add(errorLabelLogin);
		butLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(gameWindow != null && gameWindow.isAlive())
					return;
				
				gw = new GUIGameWindow();
				gameWindow = new Thread(gw);
				gameWindow.start();
				
				loginWindow.dispose();
				
				/*
				//TODO Connect and login. AFter that display the OPENGL window and start the fun.
				String name = getLoginUsername().getText().trim();
				String pass = String.valueOf(getLoginPassword().getPassword()).trim();
				
				
				
				MessageDigest md;
				Packet p = new Packet(Packet.LOGIN_PACKET);
				try {
					md = MessageDigest.getInstance("MD5");

					md.update(pass.getBytes());
					byte[] digest = md.digest();
					StringBuffer sb = new StringBuffer();
					for (byte b : digest) {
						sb.append(String.format("%02x", b & 0xff));
					}
					p.addData(name);
					p.addData(sb.toString());
					
					Client.connect(name);
					
					p.send(new PrintWriter(Client.socket.getOutputStream(),true));
				} catch (NoSuchAlgorithmException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 */
			}
			
			
		});
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Register", null, panel_1, null);
		panel_1.setLayout(null);
		
		registerPassword1 = new JPasswordField();
		registerPassword1.setBounds(81, 42, 154, 20);
		panel_1.add(registerPassword1);
		
		registerUsername = new JTextField();
		registerUsername.setBounds(81, 11, 154, 20);
		panel_1.add(registerUsername);
		registerUsername.setColumns(10);
		
		registerPassword2 = new JPasswordField();
		registerPassword2.setBounds(81, 73, 154, 20);
		panel_1.add(registerPassword2);
		
		JButton butRegister = new JButton("Register");
		butRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				String pass1,pass2,name;
				name = getRegisterUsername().getText().trim();
				pass1 =	String.valueOf(getRegisterPassword1().getPassword()).trim();
				pass2 =	String.valueOf(getRegisterPassword2().getPassword()).trim();
				
				
				
				if(name.length() <= 4)
					System.out.println("Name too short");

				if(name.length() > 4 && pass1.equals(pass2) && pass1.length() > 5){
					Packet p = new Packet(Packet.REGISTER_PACKET);
					p.addData(name);
					MessageDigest md;
					try {
						md = MessageDigest.getInstance("MD5");

						md.update(pass2.getBytes());
						byte[] digest = md.digest();
						StringBuffer sb = new StringBuffer();
						for (byte b : digest) {
							sb.append(String.format("%02x", b & 0xff));
						}

						p.addData(sb.toString());
						
						System.out.println(this.getClass().getName()+ " ");
						
						Client.connect(name);
						p.send(new PrintWriter(Client.socket.getOutputStream(),true));
					} catch (NoSuchAlgorithmException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		butRegister.setBounds(81, 104, 154, 23);
		panel_1.add(butRegister);
		
		JLabel lblUsername_1 = new JLabel("Username");
		lblUsername_1.setBounds(10, 14, 63, 14);
		panel_1.add(lblUsername_1);
		
		JLabel lblPassword_1 = new JLabel("Password");
		lblPassword_1.setBounds(10, 45, 61, 14);
		panel_1.add(lblPassword_1);
		
		JLabel lblRepeat = new JLabel("Repeat");
		lblRepeat.setBounds(10, 76, 61, 14);
		panel_1.add(lblRepeat);
		
		errorLabelRegister = new JLabel("");
		errorLabelRegister.setBounds(81, 138, 154, 14);
		panel_1.add(errorLabelRegister);
	}
	public JTextField getRegisterUsername() {
		return registerUsername;
	}
	public JPasswordField getRegisterPassword1() {
		return registerPassword1;
	}
	public JPasswordField getRegisterPassword2() {
		return registerPassword2;
	}
	public JLabel getErrorLabelRegister() {
		return errorLabelRegister;
	}
	public JLabel getErrorLabelLogin() {
		return errorLabelLogin;
	}
	public JPasswordField getLoginPassword() {
		return loginPassword;
	}
	public JTextField getLoginUsername() {
		return loginUsername;
	}
}
