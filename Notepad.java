import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Notepad {
	
	private JFrame frame;	
	private JTextArea text;
	private JScrollPane scroller;
	
	private MenuBar topMenu;
	
	private Boolean isDark = false;
	private Boolean isLineWrap = false;
	
	public Notepad(){
		setUpGui();
	}
	
	class MenuBar extends JMenuBar {
		
		private static final long serialVersionUID = -7351907841476553158L;
		
		private Color bgColor=this.getBackground();
		
		private JMenu[] menuItems;
		
		public MenuBar(){
			menuItems = new JMenu[5];
			
			menuItems[0] = new JMenu("File"); 
			menuItems[1] = new JMenu("Edit");
			menuItems[2] = new JMenu("Format");
			menuItems[3] = new JMenu("View");
			menuItems[4] = new JMenu("Help");
		
			JMenuItem[] fileItems = new JMenuItem[4];
			fileItems[0] = new JMenuItem("New");
			fileItems[0].addActionListener(new NewListener());
			fileItems[1] = new JMenuItem("Open");	
			fileItems[1].addActionListener(new OpenListener());
			fileItems[2] = new JMenuItem("Save");
			fileItems[2].addActionListener(new SaveListener());
			fileItems[3] = new JMenuItem("Exit");
			fileItems[3].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int a = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit? All unsaved work will be lost.");
					if(a == JOptionPane.YES_OPTION)
						System.exit(0);
				}
			});
			
			JMenuItem date = new JMenuItem("Date");
			date.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					text.append("\n" + timeNow()+ "\n");
				}
			});
			
			JMenuItem[] formatItems = new JMenuItem[2];
			formatItems[0] = new JMenuItem("Line wrap text");
			formatItems[0].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(!isLineWrap){
						text.setLineWrap(true);
						scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
						formatItems[0].setText("Stop line wrapping");
					}else{
						text.setLineWrap(false);
						scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						formatItems[0].setText("Line wrap text");
					}
					isLineWrap = !isLineWrap;
				}
			});
			formatItems[1] = new JMenuItem("Font");
			formatItems[1].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JFontChooser font = new JFontChooser();
					font.showDialog(frame);
					text.setFont(font.getSelectedFont());
				}
			});
			
			JMenuItem[] viewItems = new JMenuItem[1];
			viewItems[0] = new JMenuItem("Dark Theme");
			viewItems[0].addActionListener(new ThemeListener());
			
			JMenuItem about = new JMenuItem("About SuperText");
			about.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					 JOptionPane.showMessageDialog(frame, 
							 "                          SuperText is a notepad created by Abhay Bhat\n The code for the Font Chooser was found at https://stackoverflow.com/a/7528129");
				}
			});
			
			
			for(int i = 0; i < fileItems.length;i++){
				menuItems[0].add(fileItems[i]);
			}
			
			menuItems[1].add(date);
			
			for(int i = 0; i< formatItems.length;i++){
				menuItems[2].add(formatItems[i]);
			}
			
			for(int i = 0; i < viewItems.length;i++){
				menuItems[3].add(viewItems[i]);
			}
			
			menuItems[4].add(about);
			
			for(int i = 0; i < menuItems.length;i++){
				this.add(menuItems[i]);
			}
						
		}

	    public void setColor(Color color) {
	        bgColor=color;
	    }
	    
	    public Color getColor(){
	    	return bgColor;
	    }

	    public JMenu[] getMenuItems(){
	    	return menuItems;
	    }
	    
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setColor(bgColor);
	        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

	    }
		
	}
	
	private void setUpGui() {
		
		frame = new JFrame("SuperText");
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		text = new JTextArea();
		
		scroller = new JScrollPane(text);
		text.setLineWrap(false);
		
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		frame.getContentPane().add(scroller);
		
		topMenu = new MenuBar();
		frame.setJMenuBar(topMenu);
		topMenu.setColor(Color.white);
		topMenu.repaint();
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	class NewListener implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			int a = JOptionPane.showConfirmDialog(frame, "Are you sure you want to continue? All unsaved work will be lost.");
			if(a == JOptionPane.YES_OPTION)
				text.setText("");
		}
		
	}
	
	class OpenListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			int a = JOptionPane.showConfirmDialog(frame, "Are you sure you want to continue? All unsaved work will be lost.");
			if(a != JOptionPane.YES_OPTION)
				return;
			text.setText("");
			FileDialog fileDialog = new FileDialog(frame,"Select file");
			fileDialog.setVisible(true);
			try{
				
				File f = new File(fileDialog.getDirectory() + fileDialog.getFile());
				if(fileDialog.getFile() == null)
					return;
				Scanner s = new Scanner(f);
				while(s.hasNextLine()){
					text.append(s.nextLine() + "\n");
				}
				s.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
			
	}
	
	class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			try{
				FileDialog fd = new FileDialog(frame, "Select directory");
				fd.setVisible(true);
				
				File f = new File(fd.getDirectory() + fd.getFile());
				
				String[] tokens = text.getText().split("\n");
				
				FileWriter fw = new FileWriter(f);
				
				for(int i = 0; i < tokens.length;i++){
					fw.write(tokens[i] + "\n");
				}
				
				fw.close();
				
			}catch(Exception e){
				
			}
		}
	}
	
	class ThemeListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			JMenuItem it = (JMenuItem)e.getSource();
			if(!isDark){
				text.setForeground(Color.WHITE);
				text.setBackground(Color.DARK_GRAY);
				it.setText("Light Theme");
				topMenu.setColor(Color.black);
			}else{
				text.setForeground(Color.DARK_GRAY);
				text.setBackground(Color.WHITE);
				it.setText("Dark Theme");
				topMenu.setColor(Color.white);
			}
			isDark = !isDark;
			topMenu.repaint();
		}
		
	}
	
	private String timeNow(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
	    LocalDateTime now = LocalDateTime.now().minusHours(7);
		LocalTime time = LocalTime.now();  
		return dtf.format(now) + ": "+ time;  
	}
	
}