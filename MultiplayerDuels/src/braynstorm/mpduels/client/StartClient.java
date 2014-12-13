package braynstorm.mpduels.client;

import java.awt.EventQueue;

import javax.swing.UIManager;

import braynstorm.mpduels.client.gui.GUILoginWindow;

public class StartClient {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					GUILoginWindow.loginWindow = new GUILoginWindow();
					GUILoginWindow.loginWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
