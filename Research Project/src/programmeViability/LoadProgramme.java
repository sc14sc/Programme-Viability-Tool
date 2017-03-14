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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
		midPanel.add(Box.createVerticalStrut(480));
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
		
		JButton validateButton = new JButton("Validate");
		validateButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				validate();
			}
		});
		
		JButton timeTableButton = new JButton("Timetable");
		timeTableButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				displayTimeTable();
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
		botPanel.add(validateButton);
		botPanel.add(timeTableButton);
		botPanel.add(Box.createRigidArea(new Dimension(500,0)));
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
	                			lineElement[6], lineElement[7], lineElement[8]);
	                	prog.getGroups().add(group);
	                }
	                for (ModuleClass mdl: prog.getModules()) {
	                	if (mdl.getSubjCode().equals(lineElement[9]) &&
	                			mdl.getCrseNumb().equals(lineElement[10])) {
	                		module = mdl;
	                	}
	                }
	                if (module == null) {
	                	module = new ModuleClass(lineElement[11], lineElement[9],
	                			 lineElement[10], lineElement[6], lineElement[17], Float.valueOf(lineElement[19]));
	                	prog.getModules().add(module);
	                }
	                activity = new ActivityClass(lineElement[13],
	                		Float.valueOf(lineElement[14]), module.getSubjCode(), module.getCrseNumb());
	                
	                prog.getActivities().add(activity);
	                
	                module.setHoursPerWeek(module.getHoursPerWeek() + activity.getLength());
	                prog.updateModule(module);
	                
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
				"Sem",
				"Credits",
				"Hours"};
						
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
			//lineElementTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			lineElementTable.setModel(tableModel);
			lineElementTable.getColumnModel().getColumn(0).setMaxWidth(80);
			lineElementTable.getColumnModel().getColumn(2).setMaxWidth(35);
			lineElementTable.getColumnModel().getColumn(3).setMaxWidth(45);
			lineElementTable.getColumnModel().getColumn(4).setMaxWidth(40);
			groupPanel.add(groupLabel);
			groupPanel.add(lineElementList);
			midPanel.add(groupPanel);
			tableModel.setRowCount(0);
			
			JPopupMenu popupMenu = new JPopupMenu();
			JMenuItem menuItemAdd = new JMenuItem("Add New Module");
			JMenuItem menuItemEdit = new JMenuItem("Edit Module");
			
			menuItemAdd.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					addModule();
				}
			});
			
			popupMenu.add(menuItemAdd);
			popupMenu.add(menuItemEdit);
			
			lineElementTable.setComponentPopupMenu(popupMenu);
			lineElementTable.addMouseListener(new TableMouseListener(lineElementTable));
			
			for (ModuleClass module: programme.getModules()){
				if (module.getOptionalGroup().equals(group.getOptionalGroup())) {
					tableModel.addRow(new Object[] {module.getSubjCode() + module.getCrseNumb(), module.getTitle(),
							module.getSemester(), module.getCredits(), module.getHoursPerWeek()});
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
	
	public void displayTimeTable() {
		JDialog addTimeTableDialog = new JDialog();
		
		JPanel basic = new JPanel();
		basic.setMaximumSize(basic.getPreferredSize());
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		addTimeTableDialog.add(basic);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		JPanel middlePanel = new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		JPanel botPanel = new JPanel();
		botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));
		
		JLabel timeTableLabel = new JLabel(programme.getProgCode() + " " + programme.getProgShortTitle() + " Year " + programme.getYear());
		timeTableLabel.setHorizontalAlignment(JLabel.LEFT);
		timeTableLabel.setFont(new Font("Serif", Font.BOLD, 24));
		
		titlePanel.add(timeTableLabel);
		titlePanel.add(Box.createRigidArea(new Dimension(20,0)));
		
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				addTimeTableDialog.dispose();
			}
		});
		
		topPanel.add(titlePanel);
		
		//https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple
		String times [] = new String[] {
				" ",
				"09:00 - 10:00",
				"10:00 - 11:00",
				"11:00 - 12:00",
				"12:00 - 13:00",
				"13:00 - 14:00",
				"14:00 - 15:00",
				"15:00 - 16:00",
				"16:00 - 17:00"};
						
		Object[][] data = {
			    {"Monday", " ",
					" ",
					" ",
					" ",
					" ",
					" ",
					" ",
					" "},
			    {"Tuesday", " ",
						" ",
						" ",
						" ",
						" ",
						" ",
						" ",
						" "},
			    {"Wedneday", " ",
							" ",
							" ",
							" ",
							" ",
							" ",
							" ",
							" "},
			    {"Thursday", " ",
								" ",
								" ",
								" ",
								" ",
								" ",
								" ",
								" "},
			    {"Friday", " ",
									" ",
									" ",
									" ",
									" ",
									" ",
									" ",
									" "}
			};
		
		for (ActivityClass activity: programme.getActivities()) {
			if (activity.getDay() != 99) {
				data[activity.getDay()][activity.getStartTime()] += activity.getSubjectCode()
						 + activity.getCourseNumber() + " " + activity.getDescription() + "\n";
			}
		}
		
		DefaultTableModel timeTableModel = new DefaultTableModel() {
			public Class<String> getColumnClass (int col) {
				return String.class;
			}
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		timeTableModel.setDataVector(data, times);
		JTable timetable = new JTable(timeTableModel);
		timetable.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
		JScrollPane timetablePane = new JScrollPane(timetable);
		timetable.getColumnModel().getColumn(0).setMaxWidth(75);
		timetable.getColumnModel().getColumn(0).setCellRenderer(new RowHeaderRenderer(timetable));
		timetable.setFillsViewportHeight(true);
        
		middlePanel.add(timetablePane);

		botPanel.add(backButton);
		
		basic.add(topPanel);
		basic.add(middlePanel);
		basic.add(botPanel);
		
		addTimeTableDialog.setTitle("Timetable");
		addTimeTableDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		addTimeTableDialog.setSize(1000, 600);
		addTimeTableDialog.setLocationRelativeTo(screen1);
		addTimeTableDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addTimeTableDialog.setVisible(true);
	}
	
	public void validate() {
		int time = 1;
		int day = 0;
		for (ActivityClass activity: programme.getActivities()) {
			activity.setDay(day);
			activity.setStartTime(time);
			programme.updateActivity(activity);
			time++;
			if (time > 8) {
				day++;
				time = 1;
			}
			if (day > 4) {
				time = 1;
				day = 0;
			}
		}
	}
	
}
