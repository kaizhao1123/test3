package Controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.*;

import GUI.MainFrame;

public class AWS {
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				// set the size of the application to 3/5 of the screen size
				int width = (int) screenSize.getWidth() * 3 / 5;
				int height = (int) screenSize.getHeight() * 3 / 5;

				// read data
				String path = "C:/Users/Kai Zhao/Desktop/AWMdata.xlsx";
				
				// initial main frame and panel manager
				JFrame frame = null;
				PanelManager panelManager;
				try {
					panelManager = new PanelManager(path);
					frame = new MainFrame(panelManager, width, height);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				frame.setSize(width, height);
				// set the location to the middle of the screen
				frame.setLocation(((int) screenSize.getWidth() / 2 - width / 2),
						(int) (screenSize.getHeight() / 2 - height / 2));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setMinimumSize(new Dimension(960, 540));
				frame.setResizable(false);
			}
		});
	}
}