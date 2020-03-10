package test3;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.*;

public class Test3 { 
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				//set the size of the application to 3/5 of the screen size
				int width = (int)screenSize.getWidth()*3/5;
				int height = (int)screenSize.getHeight()*3/5;
				
				String path = "C:/Users/kaizhao/Desktop/AWSdata.xlsx";
				JFrame frame = null;				
				InputData example;
				
				try {
					example = new InputData(path);
					frame = new MainFrame(example,width, height);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.setSize(width, height);
				//set the location to the middle of the screen
				frame.setLocation(((int)screenSize.getWidth()/2-width/2), (int)(screenSize.getHeight()/2-height/2));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setMinimumSize(new Dimension(960,460));
				frame.setResizable(false);
			}
		});
	}
}