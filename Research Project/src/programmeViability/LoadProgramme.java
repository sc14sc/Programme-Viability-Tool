/**
 * 
 */
package programmeViability;

import java.awt.Dialog.ModalityType;
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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @author Sitong Chen
 *
 */
public class LoadProgramme {

	/**
	 * @param args
	 */
	JPanel midPanel = new JPanel();
	ProgrammeClass programme = null;
	JLabel programmeLabel = new JLabel("Please Load File");
	JFrame screen1 = new JFrame("Programme Viability Tool");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadProgramme loadProgramme = new LoadProgramme();

	}
	public LoadProgramme() {
		
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
		midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
		JPanel botPanel = new JPanel();
		botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));
		
		JButton loadFileButton = new JButton("Load File");
		loadFileButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				loadFile();
				displayProgramme();
			}

		});
		JButton addModuleButton = new JButton("Add Module");
		
		addModuleButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				addModule();
			}

		});
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				System.exit(1);
			}
		});
		
		//listModel.addElement("Please Load A file");
		
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
		
		ProgrammeClass prog = null;
		GroupClass group = null;
		ModuleClass module = null;
		ActivityClass activity = null;
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try (
					FileInputStream fis = new FileInputStream(selectedFile);
					InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
					BufferedReader br = new BufferedReader(isr);
					) {
				
				String line = br.readLine();
	            while ((line = br.readLine()) != null) {
	            	
	            	group = null;
	            	module = null;
	            	activity = null;
	            	
	                // use comma as separator
	                String[] lineElement = line.split(",");
	                
	                if (prog == null) {
	                	prog = new ProgrammeClass(lineElement[0], lineElement[1], lineElement[2]);
	                	/*for (int i = 0; i < lineElement.length; i++){
	                		System.out.println("lineElement " + i + " " + lineElement[i]);
	                	}*/
	                }
	                for (GroupClass grp: prog.getGroups()) {
	                	if (grp.getOptionalGroup().equals(lineElement[6])){
	                		group = grp;
	                	}
	                }
	                if (group == null) {
	                	group = new GroupClass(lineElement[3], lineElement[4], lineElement[5], 
	                			lineElement[6], lineElement[8], lineElement[9]);
	                	prog.getGroups().add(group);
	                }
	                for (ModuleClass mdl: prog.getModules()) {
	                	if (mdl.getSubjCode().equals(lineElement[10]) &&
	                			mdl.getCrseNumb().equals(lineElement[11])) {
	                		module = mdl;
	                	}
	                }
	                if (module == null) {
	                	module = new ModuleClass(lineElement[12], lineElement[10],
	                			 lineElement[11], lineElement[6]);
	                	prog.getModules().add(module);
	                }
	                activity = new ActivityClass(lineElement[14],
	                		Float.valueOf(lineElement[15]), module.getSubjCode(), module.getCrseNumb());
	                
	                prog.getActivities().add(activity);
	                
	                module.setHoursPerWeek(module.getHoursPerWeek() + activity.getLength());
	                prog.Update(module);
	                
	                //System.out.println("Module code = " + lineElement[5] + lineElement[6]);

	                programmeLabel.setText(prog.getProgCode() + " " + prog.getProgShortTitle() + " Year " + prog.getYear());
	        		//listModel.addElement(mod);
	         
	            }
	            	            
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		programme = prog;
	}
	
	public void displayProgramme() {
		
		midPanel.removeAll();
		String header [] = new String[] {
				"Module",
				"Module Title",
				"Hours Taught"};
						
		for (GroupClass group: programme.getGroups()){
			
			DefaultTableModel tableModel = new DefaultTableModel(0, 0);	
			JTable lineElementTable = new JTable();
			JScrollPane lineElementList = new JScrollPane(lineElementTable);
			
			JPanel groupPanel = new JPanel();
			groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
			JLabel groupLabel = new JLabel(group.getType() + " " + group.getOptionalGroup());
			groupLabel.setHorizontalAlignment(JLabel.CENTER);
			groupLabel.setFont(new Font("Serif", Font.BOLD, 16));
			
			tableModel.setColumnIdentifiers(header);
			lineElementTable.setModel(tableModel);
			groupPanel.add(groupLabel);
			groupPanel.add(lineElementList);
			midPanel.add(groupPanel);
			tableModel.setRowCount(0);
			
			for (ModuleClass module: programme.getModules()){
				if (module.getOptionalGroup().equals(group.getOptionalGroup())) {
					tableModel.addRow(new Object[] {module.getSubjCode() + module.getCrseNumb(), module.getTitle(),
							module.getHoursPerWeek()});
				}
			}
		}
	}
	
	public void addModule() {
		JDialog addModuleDialog = new JDialog(screen1);
		
		addModuleDialog.setTitle("Add Module");
		addModuleDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		addModuleDialog.setSize(500, 300);
		addModuleDialog.setLocationRelativeTo(screen1);
		addModuleDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addModuleDialog.setVisible(true);
	}

}
