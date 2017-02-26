/**
 * 
 */
package programmeViability;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author sitongchen
 *
 */
public class LoadProgramme {

	/**
	 * @param args
	 */
	
	JLabel programmeLabel = new JLabel("Please Load File");
	List<ModuleClass> modules = new ArrayList<ModuleClass>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadProgramme loadProgramme = new LoadProgramme();

	}
	public LoadProgramme() {
		JFrame screen1 = new JFrame("Programme Viability Tool");
		
		JPanel basic = new JPanel();
		basic.setMaximumSize(basic.getPreferredSize());
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		screen1.add(basic);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JPanel midPanel = new JPanel();
		midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
		JPanel botPanel = new JPanel();
		botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));
		
		JButton loadFileButton = new JButton("Load File");
		loadFileButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				loadFile();
			}

		});
		JButton addModuleButton = new JButton("Add Module");
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				System.exit(1);
			}
		});
		
		programmeLabel.setHorizontalAlignment(JLabel.LEFT);
		programmeLabel.setFont(new Font("Serif", Font.BOLD, 24));
		
		titlePanel.add(programmeLabel);
		titlePanel.add(Box.createRigidArea(new Dimension(20,0)));
		topPanel.add(titlePanel);
		topPanel.add(buttonPanel);
		buttonPanel.add(loadFileButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(500,0)));
		buttonPanel.add(addModuleButton);
		botPanel.add(exitButton);
		
		basic.add(topPanel);
		basic.add(midPanel);
		basic.add(botPanel);
		
		screen1.setSize(1000, 600);
		screen1.setLocationRelativeTo(null);
		screen1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen1.setVisible(true);
	}
	protected void loadFile() {
		// TODO Auto-generated method stub
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			programmeLabel.setText(selectedFile.getName());
			try (
					FileInputStream fis = new FileInputStream(selectedFile);
					InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
					BufferedReader br = new BufferedReader(isr);
					) {
				String line = br.readLine();
	            while ((line = br.readLine()) != null) {

	                // use comma as separator
	                String[] module = line.split(",");

	                System.out.println("Module code = " + module[5] + module[6]);
	                ModuleClass mod = new ModuleClass(module[0], module[1], module[2], module[3], module[4], module[5], module[6]);
	                modules.add(mod);
	                

	            }
	            
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
