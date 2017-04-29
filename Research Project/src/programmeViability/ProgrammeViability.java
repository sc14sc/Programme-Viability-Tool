/**
 *
 */
package programmeViability;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * @author Sitong Chen
 *
 */
public class ProgrammeViability {

    /**
     * @param args
     */
    JPanel midPanel = new JPanel();
    ProgrammeClass programme = null;
    JLabel programmeLabel = new JLabel("Please Load File");
    JFrame screen1 = new JFrame("Programme Viability Tool");
    JButton timeTableButton = new JButton("Timetable");
    JButton saveDBButton = new JButton("Save to Database");
    JButton addGroupButton = new JButton("Add Group");
    JButton delGroupButton = new JButton("Delete Group");
    LoadProgramme loadProgramme = new LoadProgramme();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ProgrammeViability programmeViability = new ProgrammeViability();

    }
    public ProgrammeViability() {

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
        //midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        JScrollPane midScroll = new JScrollPane(midPanel);
        midScroll.setPreferredSize(new Dimension(1240,590));

        JPanel botPanel = new JPanel();
        botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));


        //final JTextField progCode = new JTextField("BA-HIST");
        //final JTextField progYear = new JTextField("3");

        JButton loadFileButton = new JButton("Load File");
        loadFileButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                programme = loadProgramme.loadFile();
                displayProgramme();
            }

        });

        JButton loadDBButton = new JButton("Load from Database");
        loadDBButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	String progCode = " ";
            	if (programme != null) {
            		progCode = programme.getProgCode() + " " + programme.getYear();            		
            	}
            	SelectProgDB progSelect = new SelectProgDB(screen1,"Choose Programme",progCode);            		
                String[] progCodes = progSelect.getProgCode();;
                programme = loadProgramme.loadAccess(progCodes);
                displayProgramme();
            }

        });

        saveDBButton.setVisible(false);
        saveDBButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	loadProgramme.saveDB(programme);
            }

        });

        addGroupButton.setVisible(false);
        addGroupButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                addGroup();
            }

        });

        delGroupButton.setVisible(false);
        delGroupButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                delGroup();
            }

        });

        JButton validateButton = new JButton("Validate");
        validateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                validate();
            }
        });

        //JButton timeTableButton = new JButton("Timetable");
        timeTableButton.setVisible(false);
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

        //buttonPanel.add(progCode);
        //buttonPanel.add(progYear);
        buttonPanel.add(loadFileButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(50,0)));
        buttonPanel.add(loadDBButton);
        buttonPanel.add(saveDBButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(250,0)));
        buttonPanel.add(addGroupButton);
        buttonPanel.add(delGroupButton);

        botPanel.add(validateButton);
        botPanel.add(timeTableButton);
        botPanel.add(Box.createRigidArea(new Dimension(500,0)));
        botPanel.add(exitButton);

        basic.add(topPanel);
        //basic.add(midPanel);
        basic.add(midScroll);
        basic.add(botPanel);

        screen1.setSize(1280, 720);
        //screen1.pack();
        screen1.setLocationRelativeTo(null);
        screen1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen1.setVisible(true);
    }

    public void displayProgramme() {
    	//http://zetcode.com/tutorials/javaswingtutorial/resizablecomponent/
    	
        timeTableButton.setVisible(false);
        midPanel.removeAll();

        //System.out.println("displayProgramme");
        programmeLabel.setText(programme.getProgCode() + " " + programme.getProgShortTitle() + " Year " + programme.getYear());

        String header [] = new String[] {
                "Module",
                "Module Title",
                "Sem",
                "Credits",
                "Hours"};

        for (GroupClass group: programme.getGroups()){
            //System.out.println("Group " + group.getOptionalGroup());

            DefaultTableModel tableModel = new DefaultTableModel(0, 0);
            tableModel.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					int row = e.getFirstRow();
					int col = e.getColumn();
	                DefaultTableModel dm = (DefaultTableModel) e.getSource();
	                if (row > -1 && col > -1) {
	                	ModuleClass mod = programme.getModule((String) dm.getValueAt(row, 0));
	                	switch (col) {
	                	case 1 : mod.setTitle((String) dm.getValueAt(row, col));
	                	case 2 : mod.setSemester((String) dm.getValueAt(row, col));
	                	case 3 : mod.setCredits(Float.parseFloat((String) dm.getValueAt(row, col)));
	                	}
	                	programme.updateModule(mod);
	                }
				}
            	
            });
            final JTable lineElementTable = new JTable() {
            	@Override
            	public boolean isCellEditable(int row, int col) {
            		if (col == 0 || col == 4) {
            			return false;
            		} else {
            			return true;
            		}
            	}
            };
            lineElementTable.putClientProperty("terminateEditOnFocusLost", true);
            lineElementTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane lineElementList = new JScrollPane(lineElementTable);
            lineElementList.setPreferredSize(new Dimension(400, 520));


            JPanel groupPanel = new JPanel();
            //groupPanel.setPreferredSize(new Dimension(400, 590));
            //groupPanel.setMaximumSize(new Dimension(400, 590));
            //groupPanel.setMinimumSize(new Dimension(400, 590));
            groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
            
            JPopupMenu grpPopUpMenu = new JPopupMenu();
            JMenuItem grpItemAdd = new JMenuItem("Edit Group");
            JMenuItem grpItemDel = new JMenuItem("Delete Group");
            grpPopUpMenu.add(grpItemAdd);
            grpPopUpMenu.add(grpItemDel);
            
            groupPanel.add(grpPopUpMenu);

            JLabel groupLabel = new JLabel(group.getType() + " " + group.getOptionalGroup());
            groupLabel.setHorizontalAlignment(JLabel.CENTER);
            groupLabel.setFont(new Font("Serif", Font.BOLD, 16));
            groupPanel.add(groupLabel);


            JPanel grpDtlsPanel = new JPanel();
            grpDtlsPanel.setLayout(new BoxLayout(grpDtlsPanel, BoxLayout.Y_AXIS));

            JPanel grpCrPanel = new JPanel();
            grpCrPanel.setLayout(new BoxLayout(grpCrPanel, BoxLayout.X_AXIS));
            JLabel minCrLabel = new JLabel("Min. Credits: " + group.getGroupMinCredits());
            JLabel maxCrLabel = new JLabel("Max. Credits: " + group.getGroupMaxCredits());
            grpCrPanel.add(minCrLabel);
            grpCrPanel.add(Box.createRigidArea(new Dimension(40,0)));
            grpCrPanel.add(maxCrLabel);

            JPanel grpXCPanel = new JPanel();
            grpXCPanel.setLayout(new BoxLayout(grpXCPanel, BoxLayout.X_AXIS));
            JLabel grpXCLabel = new JLabel("Mutually Exclusive with Groups: ");
            JLabel grpGrpsLabel = new JLabel();
            grpXCPanel.add(grpXCLabel);
            //grpXCPanel.add(Box.createRigidArea(new Dimension(40,0)));
            grpXCPanel.add(grpGrpsLabel);

            grpDtlsPanel.add(grpCrPanel);
            grpDtlsPanel.add(grpXCPanel);
            groupPanel.add(grpDtlsPanel);

            tableModel.setColumnIdentifiers(header);
            //lineElementTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            lineElementTable.setModel(tableModel);
            lineElementTable.getColumnModel().getColumn(0).setMaxWidth(80);;
            lineElementTable.getColumnModel().getColumn(2).setMaxWidth(35);;
            lineElementTable.getColumnModel().getColumn(3).setMaxWidth(45);;
            lineElementTable.getColumnModel().getColumn(4).setMaxWidth(40);;
            groupPanel.add(lineElementList);
            midPanel.add(groupPanel);
            tableModel.setRowCount(0);

            //http://www.codejava.net/java-se/swing/jtable-popup-menu-example
            JPopupMenu modPopUpMenu = new JPopupMenu();
            JMenuItem menuItemAdd = new JMenuItem("Add New Module");
            JMenuItem menuItemDel = new JMenuItem("Delete Module");
            JMenuItem menuItemMove = new JMenuItem("Move Module");
            JMenuItem menuItemDispAct = new JMenuItem("Activities");

            menuItemAdd.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    addModule(lineElementTable);
                }
            });
            menuItemDel.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    delModule(lineElementTable);
                }
            });
            menuItemMove.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    moveModule(lineElementTable);
                }
            });
            menuItemDispAct.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    displayActivities(lineElementTable);
                }
            });

            modPopUpMenu.add(menuItemAdd);
            modPopUpMenu.add(menuItemDel);
            modPopUpMenu.add(menuItemMove);
            modPopUpMenu.add(menuItemDispAct);

            lineElementTable.setComponentPopupMenu(modPopUpMenu);
            lineElementTable.addMouseListener(new TableMouseListener(lineElementTable));


            for (ModuleClass module: programme.getModules()){
                //System.out.println("Module " + module.getSubjCode() + module.getCrseNumb());
                if (module.getType().equals(group.getType()) && module.getOptionalGroup().equals(group.getOptionalGroup())) {
                    tableModel.addRow(new Object[] {module.getSubjCode() + module.getCrseNumb(), module.getTitle(),
                            module.getSemester(), module.getCredits(), module.getHoursPerWeek()});
                }
            }
        }
        saveDBButton.setVisible(true);
        addGroupButton.setVisible(true);
        delGroupButton.setVisible(true);
        screen1.revalidate();
        screen1.repaint(); 
    }

    public void addGroup() {
        final JDialog addGroupDialog = new JDialog(screen1);

        JPanel addGroupPanel = new JPanel();
        addGroupPanel.setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Type
        JLabel typeLabel = new JLabel("Type: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(typeLabel,gbc);

        final JTextField typeText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(typeText,gbc);

        // Group
        JLabel grpLabel = new JLabel("Group: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(grpLabel,gbc);

        final JTextField grpText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(grpText,gbc);

        // Min Credits
        JLabel minCrLabel = new JLabel("Minimum Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(minCrLabel,gbc);

        final JTextField minCrText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(minCrText,gbc);

        // Max Credits
        JLabel maxCrLabel = new JLabel("Maximum Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(maxCrLabel,gbc);

        final JTextField maxCrText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(maxCrText,gbc);

        // Exclusive Credits
        JLabel exGrpsLabel = new JLabel("Mutually exclusive with: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(exGrpsLabel,gbc);

        final JTextField exGrpsText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(exGrpsText,gbc);

        // Buttons
        JButton addBtn = new JButton("Add");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(10,10,10,0);
        gbc.weightx = 1;
        addBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	// check no modules in group first
            	int count = 0;
            	for (ModuleClass mod : programme.getModules()) {
            		if (mod.getType().equals(typeText.getText().toUpperCase()) && 
            				mod.getOptionalGroup().equals(grpText.getText().toUpperCase())) {
            			count++;
            		}
            	}
            	if (count == 0) {
                	programme.deleteGroup(new String[] {typeText.getText().toUpperCase(), grpText.getText().toUpperCase()});
                    //delGroupDialog.dispose();
                    displayProgramme();
            		
            	} else {
                    JOptionPane.showMessageDialog(screen1,
                            "Cannot delete Group which contains modules"
                            ,"ERROR"
                            ,JOptionPane.ERROR_MESSAGE);
                    timeTableButton.setVisible(true);
           		
            	}
            }
        });
        addGroupPanel.add(addBtn,gbc);

        JButton cancelBtn = new JButton("Cancel");
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.insets = new Insets(10,0,10,10);
        gbc.weightx = 1;
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	addGroupDialog.dispose();
            }
        });
        addGroupPanel.add(cancelBtn,gbc);
        
        addGroupDialog.add(addGroupPanel);
        addGroupDialog.setTitle("Add Group");
        addGroupDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        addGroupDialog.pack();
        addGroupDialog.setLocationRelativeTo(screen1);
        addGroupDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addGroupDialog.setVisible(true);
    }

    public void delGroup() {
        final JDialog delGroupDialog = new JDialog(screen1);

        JPanel delGroupPanel = new JPanel();
        delGroupPanel.setLayout(new GridBagLayout()); //new BoxLayout(editModulePanel, BoxLayout.Y_AXIS));

        //System.out.println(table.getValueAt(table.getSelectedRow(),0));
        //ModuleClass mod = programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString());

        //http://stackoverflow.com/questions/17204760/how-can-i-make-a-component-span-multiple-cells-in-a-gridbaglayout
        GridBagConstraints gbc = new GridBagConstraints();

        // Group
        JLabel grpLabel = new JLabel("Group: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        delGroupPanel.add(grpLabel,gbc);

        final JTextField grpText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        delGroupPanel.add(grpText,gbc);

        // Buttons
        JButton delBtn = new JButton("Delete");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,10,10,0);
        gbc.weightx = 1;
        delBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	// check no modules in group first
            	String[] group = grpText.getText().toUpperCase().split(" ");
            	int count = 0;
            	for (ModuleClass mod : programme.getModules()) {
            		if (mod.getType().equals(group[0]) && 
            				mod.getOptionalGroup().equals(group[1])) {
            			count++;
            		}
            	}
            	if (count == 0) {
                	programme.deleteGroup(group);
                    delGroupDialog.dispose();
                    displayProgramme();
            		
            	} else {
                    JOptionPane.showMessageDialog(screen1,
                            "Cannot delete Group which contains modules"
                            ,"ERROR"
                            ,JOptionPane.ERROR_MESSAGE);
                    timeTableButton.setVisible(true);
           		
            	}
            }
        });
        delGroupPanel.add(delBtn,gbc);

        JButton cancelBtn = new JButton("Cancel");
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,0,10,10);
        gbc.weightx = 1;
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	delGroupDialog.dispose();
            }
        });
        delGroupPanel.add(cancelBtn,gbc);

        delGroupDialog.add(delGroupPanel);
        delGroupDialog.setTitle("Delete Group");
        delGroupDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        //delGroupDialog.setSize(500, 300);
        delGroupDialog.pack();
        delGroupDialog.setLocationRelativeTo(screen1);
        delGroupDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        delGroupDialog.setVisible(true);
    }

    public void addModule(final JTable table) {
        final JDialog addModuleDialog = new JDialog(screen1);

        JPanel addModulePanel = new JPanel();
        addModulePanel.setLayout(new GridBagLayout()); //new BoxLayout(editModulePanel, BoxLayout.Y_AXIS));

        //System.out.println(table.getValueAt(table.getSelectedRow(),0));
        ModuleClass mod = programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString());

        //http://stackoverflow.com/questions/17204760/how-can-i-make-a-component-span-multiple-cells-in-a-gridbaglayout
        GridBagConstraints gbc = new GridBagConstraints();

        // Subject
        JLabel subjLabel = new JLabel("Subject: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addModulePanel.add(subjLabel,gbc);

        final JTextField subjText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addModulePanel.add(subjText,gbc);

        // Course
        JLabel crseLabel = new JLabel("Course Number: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addModulePanel.add(crseLabel,gbc);

        final JTextField crseText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addModulePanel.add(crseText,gbc);

        // Title
        JLabel titleLabel = new JLabel("Title: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addModulePanel.add(titleLabel,gbc);

        final JTextField titleText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addModulePanel.add(titleText,gbc);

        // Semester
        JLabel semLabel = new JLabel("Semester: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addModulePanel.add(semLabel,gbc);

        final JTextField semText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addModulePanel.add(semText,gbc);

        // Credits
        JLabel crLabel = new JLabel("Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addModulePanel.add(crLabel,gbc);

        final JTextField crText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addModulePanel.add(crText,gbc);

        // Buttons
        JButton saveBtn = new JButton("Save");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(10,10,10,0);
        gbc.weightx = 1;
        saveBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                ModuleClass module = new ModuleClass(titleText.getText(), subjText.getText(),crseText.getText(),
                        programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString()).getType(),
                        programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString()).getOptionalGroup(),
                        semText.getText(), Float.valueOf(crText.getText()));
                programme.getModules().add(module);

                DefaultTableModel dm = (DefaultTableModel) table.getModel();
                dm.addRow(new Object[] {module.getSubjCode() + module.getCrseNumb(), module.getTitle(),
                        module.getSemester(), module.getCredits(), module.getHoursPerWeek()});

                addModuleDialog.dispose();
            }
        });
        addModulePanel.add(saveBtn,gbc);

        JButton cancelBtn = new JButton("Cancel");
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.insets = new Insets(10,0,10,10);
        gbc.weightx = 1;
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                addModuleDialog.dispose();
            }
        });
        addModulePanel.add(cancelBtn,gbc);

        addModuleDialog.add(addModulePanel);
        addModuleDialog.setTitle("Add Module");
        addModuleDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        //addModuleDialog.setSize(300, 200);
        addModuleDialog.pack();
        addModuleDialog.setLocationRelativeTo(screen1);
        addModuleDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addModuleDialog.setVisible(true);
    }

    public void delModule(JTable table) {
        programme.deleteModule(table.getValueAt(table.getSelectedRow(),0).toString());
        DefaultTableModel dm = (DefaultTableModel) table.getModel();
        dm.removeRow(table.getSelectedRow());
    }

    public void moveModule(JTable table) {
        final JDialog moveModuleDialog = new JDialog(screen1);

        JPanel moveModulePanel = new JPanel();
        moveModulePanel.setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        final ModuleClass mod = programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString());

        // To Type
        JLabel toTypeLabel = new JLabel("To Type: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        moveModulePanel.add(toTypeLabel,gbc);

        final JTextField toTypeText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        moveModulePanel.add(toTypeText,gbc);

        // To Group
        JLabel toGrpLabel = new JLabel("To Group: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        moveModulePanel.add(toGrpLabel,gbc);

        final JTextField toGrpText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        moveModulePanel.add(toGrpText,gbc);

        // Buttons
        JButton moveBtn = new JButton("Move");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10,10,10,0);
        gbc.weightx = 1;
        moveBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	mod.setType(toTypeText.getText().toUpperCase());
            	mod.setOptionalGroup(toGrpText.getText().toUpperCase());
            	programme.updateModule(mod);
            	moveModuleDialog.dispose();
            	displayProgramme();
            }
        });
        moveModulePanel.add(moveBtn,gbc);

        JButton cancelBtn = new JButton("Cancel");
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10,0,10,10);
        gbc.weightx = 1;
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	moveModuleDialog.dispose();
            }
        });
        moveModulePanel.add(cancelBtn,gbc);

        moveModuleDialog.add(moveModulePanel);
        moveModuleDialog.setTitle("Move Module");
        moveModuleDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        moveModuleDialog.pack(); //setSize(500, 300);
        moveModuleDialog.setLocationRelativeTo(screen1);
        moveModuleDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        moveModuleDialog.setVisible(true);
    }

    public void displayActivities(final JTable table) {
        final JDialog dispActDialog = new JDialog(screen1);

        JPanel basic = new JPanel();
        basic.setMaximumSize(basic.getPreferredSize());
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        JPanel botPanel = new JPanel();
        botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));

        String actHeader [] = new String[] {
                "Row",
                "Activity",
                "Length"};

        final ModuleClass mod = programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString());

        final DefaultTableModel actModel = new DefaultTableModel(0, 0);
        actModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int col = e.getColumn();
                DefaultTableModel dm = (DefaultTableModel) e.getSource();
                if (row > -1 && col > -1) {
                	ActivityClass act = programme.getActivity(mod.getSubjCode()+mod.getCrseNumb(), Integer.parseInt((String) dm.getValueAt(row, 0)));
                	switch (col) {
                	case 1 : act.setDescription((String) dm.getValueAt(row, col)); 
                	case 2 : act.setLength(Float.parseFloat((String) dm.getValueAt(row, col)));
                	}
                	programme.updateModule(mod);
                }
			}
        	
        });
        actModel.setColumnIdentifiers(actHeader);
        
        final JTable actTable = new JTable(actModel) {
        	@Override
        	public boolean isCellEditable(int row, int col) {
        		if (col == 0 ) {
        			return false;
        		} else {
        			return true;
        		}
        	}
        };
        actTable.putClientProperty("terminateEditOnFocusLost", true);
        actTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
       JScrollPane actList = new JScrollPane(actTable);

        //http://www.codejava.net/java-se/swing/jtable-popup-menu-example
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuActAdd = new JMenuItem("Add New Activity");
        JMenuItem menuActDel = new JMenuItem("Delete Activity");

        menuActAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                dispActDialog.dispose();
            }
        });
        menuActDel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                Float actHrs = programme.deleteActivity((int) actModel.getValueAt(actTable.getSelectedRow(),0), actModel.getValueAt(actTable.getSelectedRow(),1).toString(), mod);
                actModel.removeRow(actTable.getSelectedRow());
                mod.setHoursPerWeek(actHrs);
                DefaultTableModel dm = (DefaultTableModel) table.getModel();
                dm.setValueAt(mod.getHoursPerWeek(), table.getSelectedRow(), 4);
                programme.updateModule(mod);
            }
        });

 
        popupMenu.add(menuActAdd);
        popupMenu.add(menuActDel);
 
        actTable.setComponentPopupMenu(popupMenu);
        actTable.addMouseListener(new TableMouseListener(actTable));

        int i = 0;
        for (ActivityClass act: programme.getActivities()){
            if (act.getSubjectCode().equals(mod.getSubjCode()) &&
                    act.getCourseNumber().equals(mod.getCrseNumb())) {
                actModel.addRow(new Object[] {act.getIndex(),act.getDescription(), act.getLength()});
                i++;
            }
        }
        if (i == 0) {
            actModel.addRow(new Object[] {" ", " "});
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                dispActDialog.dispose();
            }
        });

        actTable.setPreferredScrollableViewportSize(actTable.getPreferredSize());
        actTable.setFillsViewportHeight(true);

        middlePanel.add(actList);
        botPanel.add(backButton);

        //basic.add(topPanel);
        basic.add(middlePanel);
        basic.add(botPanel);

        dispActDialog.add(basic);
        dispActDialog.setTitle("Activities");
        dispActDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        //dispActDialog.setSize(500, 300);
        dispActDialog.pack();
        dispActDialog.setLocationRelativeTo(screen1);
        dispActDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dispActDialog.setVisible(true);
    }

    public void displayTimeTable() {
        final JDialog addTimeTableDialog = new JDialog();

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

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));

        TimeTable timetablePane = new TimeTable(programme);
        //JPanel sem1Panel = new JPanel();
        middlePanel.add(new JLabel("Semester 1"));
        middlePanel.add(timetablePane.createTimeTable("1"));
        //JPanel sem2Panel = new JPanel();
        middlePanel.add(new JLabel("Semester 2"));
        middlePanel.add(timetablePane.createTimeTable("2"));

        //middlePanel.add(sem1Panel);
        //middlePanel.add(sem2Panel);

        botPanel.add(backButton);

        basic.add(topPanel);
        basic.add(middlePanel);
        basic.add(botPanel);

        addTimeTableDialog.setTitle("Timetable");
        addTimeTableDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        addTimeTableDialog.setSize(1280, 720);
        //addTimeTableDialog.pack();
        addTimeTableDialog.setLocationRelativeTo(screen1);
        addTimeTableDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addTimeTableDialog.setVisible(true);
    }

    public void validate() {
        float sem1Hrs = 0,sem2Hrs = 0;
        int time = 1;
        int day = 0;
        for (ModuleClass mod : programme.getModules()) {
            if (mod.getSemester().equals("1") ||mod.getSemester().equals("3")) {
                for (ActivityClass activity: programme.getActivities()) {
                    if (activity.getSubjectCode().equals(mod.getSubjCode()) &&
                            activity.getCourseNumber().equals(mod.getCrseNumb())) {
                        activity.setDay(day);
                        activity.setStartTime(time);
                        programme.updateActivity(activity);
                        sem1Hrs += activity.getLength();
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
        }

        time = 1;
        day = 0;
        for (ModuleClass mod : programme.getModules()) {
            if (mod.getSemester().equals("2") ||mod.getSemester().equals("3")) {
                for (ActivityClass activity: programme.getActivities()) {
                    if (activity.getSubjectCode().equals(mod.getSubjCode()) &&
                            activity.getCourseNumber().equals(mod.getCrseNumb())) {
                        activity.setDay(day);
                        activity.setStartTime(time);
                        programme.updateActivity(activity);
                        sem2Hrs += activity.getLength();
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
        }

        if (sem1Hrs < 40.1 && sem2Hrs < 40.1) {
            JOptionPane.showMessageDialog(screen1,
                    "Programme is viable"
                    ,"Viable"
                    ,JOptionPane.INFORMATION_MESSAGE);
            timeTableButton.setVisible(true);

        } else if (sem1Hrs > 40.1 && sem2Hrs > 40.1) {
            JOptionPane.showMessageDialog(screen1,
                    "Neither Semester 1 (" + Float.toString(sem1Hrs) +
                    "hrs) nor Semester2 ("+ Float.toString(sem2Hrs) + "hrs) are viable",
                    "NOT viable",
                    JOptionPane.ERROR_MESSAGE);

        } else if (sem1Hrs > 40.1) {
            JOptionPane.showMessageDialog(screen1,
                    "Semester 1 (" + Float.toString(sem1Hrs) +  "hrs) is not viable",
                    "NOT viable",
                    JOptionPane.ERROR_MESSAGE);

        } else if (sem2Hrs > 40.1) {
            JOptionPane.showMessageDialog(screen1,
                    "Semester 2 (" + Float.toString(sem2Hrs) +  "hrs) is not viable",
                    "NOT viable",
                    JOptionPane.ERROR_MESSAGE);

        }
    }
}
