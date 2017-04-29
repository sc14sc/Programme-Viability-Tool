/**
 *
 */
package programmeViability;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    JLabel programmeLabel = new JLabel("Please Load Programme");
    JFrame screen1 = new JFrame("Programme Viability Tool");
	JButton validateButton = new JButton("Validate");
	JButton timeTableButton = new JButton("Timetable");
    JButton saveDBButton = new JButton("Save to Database");
    JButton addGroupButton = new JButton("Add Group");
    //JButton editGroupButton = new JButton("Edit Group");
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

        JButton loadDBButton = new JButton("Load Programme");
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

        //JButton validateButton = new JButton("Validate");
        validateButton.setVisible(false);
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
        //buttonPanel.add(loadFileButton);
        //buttonPanel.add(Box.createRigidArea(new Dimension(50,0)));
        buttonPanel.add(loadDBButton);
        buttonPanel.add(saveDBButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(250,0)));
        buttonPanel.add(addGroupButton);
        //buttonPanel.add(editGroupButton);
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

    	validateButton.setVisible(true);
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

        for (final GroupClass group: programme.getGroups()){
            //System.out.println("Group " + group.getOptionalGroup());

            DefaultTableModel tableModel = new DefaultTableModel(0, 0);
            tableModel.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					int row = e.getFirstRow();
					int col = e.getColumn();
	                DefaultTableModel dm = (DefaultTableModel) e.getSource();
	                if (row > -1 && col > -1) {
	                	ModuleClass mod = programme.getModule((String) dm.getValueAt(row, 0) + (String) dm.getValueAt(row, 2));
	                	switch (col) {
	                	case 1 : mod.setTitle((String) dm.getValueAt(row, col));
	                	         break;
	                	case 2 : mod.setSemester((String) dm.getValueAt(row, col));
	                	         break;
	                	case 3 : mod.setCredits(Float.parseFloat((String) dm.getValueAt(row, col)));
	                	         break;
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
            groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));

            //JPopupMenu grpPopUpMenu = new JPopupMenu();
            //JMenuItem grpItemAdd = new JMenuItem("Edit Group");
            //JMenuItem grpItemDel = new JMenuItem("Delete Group");
            //grpPopUpMenu.add(grpItemAdd);
            //grpPopUpMenu.add(grpItemDel);

            //groupPanel.add(grpPopUpMenu);

            JLabel groupLabel = new JLabel(group.getType() + " " + group.getOptionalGroup());
            groupLabel.setHorizontalAlignment(JLabel.CENTER);
            groupLabel.setFont(new Font("Serif", Font.BOLD, 16));
            groupPanel.add(groupLabel);

            JPanel grpDtlsPanel = new JPanel();
            grpDtlsPanel.setLayout(new BoxLayout(grpDtlsPanel, BoxLayout.Y_AXIS));

            JPanel typeCrPanel = new JPanel();
            typeCrPanel.setLayout(new BoxLayout(typeCrPanel, BoxLayout.X_AXIS));
            JLabel typeMinCrLabel = new JLabel("Type Min. Credits: " + group.getTypeMinCredits());
            JLabel typeMaxCrLabel = new JLabel("Type Max. Credits: " + group.getTypeMaxCredits());
            typeCrPanel.add(typeMinCrLabel);
            typeCrPanel.add(Box.createRigidArea(new Dimension(40,0)));
            typeCrPanel.add(typeMaxCrLabel);

            JPanel grpCrPanel = new JPanel();
            grpCrPanel.setLayout(new BoxLayout(grpCrPanel, BoxLayout.X_AXIS));
            JLabel minCrLabel = new JLabel("Group Min. Credits: " + group.getGroupMinCredits());
            JLabel maxCrLabel = new JLabel("Group Max. Credits: " + group.getGroupMaxCredits());
            grpCrPanel.add(minCrLabel);
            grpCrPanel.add(Box.createRigidArea(new Dimension(40,0)));
            grpCrPanel.add(maxCrLabel);

            JPanel grpXCPanel = new JPanel();
            grpXCPanel.setLayout(new BoxLayout(grpXCPanel, BoxLayout.X_AXIS));
            String exGrps = "";
            for (String grp : group.getExGroup()) {
            	exGrps += grp + " ";
            }
            JLabel grpXCLabel = new JLabel("Mutually Exclusive with Groups: " + exGrps);
            JLabel grpGrpsLabel = new JLabel();
            grpXCPanel.add(grpXCLabel);
            //grpXCPanel.add(Box.createRigidArea(new Dimension(40,0)));
            grpXCPanel.add(grpGrpsLabel);



            grpDtlsPanel.add(typeCrPanel);
            grpDtlsPanel.add(grpCrPanel);
            grpDtlsPanel.add(grpXCPanel);
            groupPanel.add(grpDtlsPanel);

            tableModel.setColumnIdentifiers(header);
            //lineElementTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            lineElementTable.setModel(tableModel);
            lineElementTable.getColumnModel().getColumn(0).setMaxWidth(80);
            lineElementTable.getColumnModel().getColumn(2).setMaxWidth(35);
            lineElementTable.getColumnModel().getColumn(3).setMaxWidth(45);
            lineElementTable.getColumnModel().getColumn(4).setMaxWidth(40);
            groupPanel.add(lineElementList);
            midPanel.add(groupPanel);
            tableModel.setRowCount(0);

            //http://www.codejava.net/java-se/swing/jtable-popup-menu-example
            JPopupMenu modPopUpMenu = new JPopupMenu();
            JMenuItem menuGrpEdit = new JMenuItem("Edit Group");
            JMenuItem menuItemAdd = new JMenuItem("Add New Module");
            JMenuItem menuItemDel = new JMenuItem("Delete Module");
            JMenuItem menuItemMove = new JMenuItem("Move Module");
            JMenuItem menuItemDispAct = new JMenuItem("Activities");

            menuGrpEdit.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    editGroup(lineElementTable, group);
                }
            });
            menuItemAdd.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    addModule(lineElementTable, group);
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

            modPopUpMenu.add(menuGrpEdit);
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
            if (tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[] {" ", " "," ", " ", " "});
            }
        }
        saveDBButton.setVisible(true);
        addGroupButton.setVisible(true);
        //editGroupButton.setVisible(true);
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

        // Type Min Credits
        JLabel typeMinCrLabel = new JLabel("Type Min Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(typeMinCrLabel,gbc);

        final JTextField typeMinCrText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(typeMinCrText,gbc);

        // Type Max Credits
        JLabel typeMaxCrLabel = new JLabel("Type Max Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(typeMaxCrLabel,gbc);

        final JTextField typeMaxCrText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(typeMaxCrText,gbc);

        // Group
        JLabel grpLabel = new JLabel("Group: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(grpLabel,gbc);

        final JTextField grpText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(grpText,gbc);

        // Group Min Credits
        JLabel minCrLabel = new JLabel("Group Min Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(minCrLabel,gbc);

        final JTextField minCrText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(minCrText,gbc);

        // Group Max Credits
        JLabel maxCrLabel = new JLabel("Group Max Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(maxCrLabel,gbc);

        final JTextField maxCrText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(maxCrText,gbc);

        // Exclusive Credits
        /*JLabel exGrpsLabel = new JLabel("Mutually exclusive with: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addGroupPanel.add(exGrpsLabel,gbc);

        final JTextField exGrpsText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addGroupPanel.add(exGrpsText,gbc);*/

        // Buttons
        JButton addBtn = new JButton("Add");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(10,10,10,0);
        gbc.weightx = 1;
        addBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                GroupClass grp = new GroupClass(typeText.getText().toUpperCase(), typeMinCrText.getText(),typeMaxCrText.getText(),
                		 grpText.getText().toUpperCase(), minCrText.getText(),maxCrText.getText());
                programme.addGroup(grp);

                addGroupDialog.dispose();
                displayProgramme();
            }
        });
        addGroupPanel.add(addBtn,gbc);

        JButton cancelBtn = new JButton("Cancel");
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 7;
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

    public void editGroup(JTable table, final GroupClass grp) {
        final JDialog editGroupDialog = new JDialog(screen1);

        JPanel editGroupPanel = new JPanel();
        editGroupPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        // Type
        JLabel typeLabel = new JLabel("Type: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        editGroupPanel.add(typeLabel,gbc);

        final JTextField typeText = new JTextField(grp.getType());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        editGroupPanel.add(typeText,gbc);

        // Type Min Credits
        JLabel typeMinCrLabel = new JLabel("Type Min Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        editGroupPanel.add(typeMinCrLabel,gbc);

        final JTextField typeMinCrText = new JTextField(grp.getTypeMinCredits());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        editGroupPanel.add(typeMinCrText,gbc);

        // Type Max Credits
        JLabel typeMaxCrLabel = new JLabel("Type Max Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        editGroupPanel.add(typeMaxCrLabel,gbc);

        final JTextField typeMaxCrText = new JTextField(grp.getTypeMaxCredits());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        editGroupPanel.add(typeMaxCrText,gbc);

        // Group
        JLabel grpLabel = new JLabel("Group: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        editGroupPanel.add(grpLabel,gbc);

        final JTextField grpText = new JTextField(grp.getOptionalGroup());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        editGroupPanel.add(grpText,gbc);

        // Group Min Credits
        JLabel minCrLabel = new JLabel("Group Min Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        editGroupPanel.add(minCrLabel,gbc);

        final JTextField minCrText = new JTextField(grp.getGroupMinCredits());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        editGroupPanel.add(minCrText,gbc);

        // Group Max Credits
        JLabel maxCrLabel = new JLabel("Group Max Credits: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        editGroupPanel.add(maxCrLabel,gbc);

        final JTextField maxCrText = new JTextField(grp.getGroupMaxCredits());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        editGroupPanel.add(maxCrText,gbc);

        // Exclusive Credits
        JLabel exGrpsLabel = new JLabel("Mutually exclusive with: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        editGroupPanel.add(exGrpsLabel,gbc);

        String exGrps = "";
        for (String exGrp : grp.getExGroup()) {
        	exGrps += exGrp + ",";
        }
        int ind = exGrps.lastIndexOf(",");
        if (ind == -1) {
        } else {
        	exGrps = exGrps.substring(0, ind);
        }
        final JTextField exGrpsText = new JTextField(exGrps);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        editGroupPanel.add(exGrpsText,gbc);

        // Buttons
        JButton saveBtn = new JButton("Save");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.insets = new Insets(10,10,10,0);
        gbc.weightx = 1;
        saveBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
        		for (String exGrp : grp.getExGroup()) {
        			//System.out.println("Deleting : " + exGrp);
                	GroupClass tempGrp = programme.getGroup(grp.getType() + exGrp);
        			//System.out.println("Found : " + grp.getType() + exGrp);
                	tempGrp.delExGroup(grp.getOptionalGroup());
                	programme.updateGroup(tempGrp);
        		}

                GroupClass group = new GroupClass(typeText.getText().toUpperCase(), typeMinCrText.getText(),typeMaxCrText.getText(),
                		 grpText.getText().toUpperCase(), minCrText.getText(),maxCrText.getText());
                group.setExGroup(exGrpsText.getText().toUpperCase());
                for (String optGrp : group.getExGroup()) {
                	GroupClass tempGrp = programme.getGroup(group.getType() + optGrp);
                	tempGrp.updExGroup(group.getOptionalGroup());
                	programme.updateGroup(tempGrp);

                }
                programme.updateGroup(group);

                editGroupDialog.dispose();
                displayProgramme();
            }
        });
        editGroupPanel.add(saveBtn,gbc);

        JButton cancelBtn = new JButton("Cancel");
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.insets = new Insets(10,0,10,10);
        gbc.weightx = 1;
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	editGroupDialog.dispose();
            }
        });
        editGroupPanel.add(cancelBtn,gbc);

        editGroupDialog.add(editGroupPanel);
        editGroupDialog.setTitle("Edit Group");
        editGroupDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        editGroupDialog.pack();
        editGroupDialog.setLocationRelativeTo(screen1);
        editGroupDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        editGroupDialog.setVisible(true);

    }

    public void delGroup() {
        final JDialog delGroupDialog = new JDialog(screen1);

        JPanel delGroupPanel = new JPanel();
        delGroupPanel.setLayout(new GridBagLayout()); //new BoxLayout(editModulePanel, BoxLayout.Y_AXIS));

        //System.out.println(table.getValueAt(table.getSelectedRow(),0));

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
        grpText.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent event) {
	    		String[] group = new String[2];
	    		int spaceCount = 0;
	    		for (int i = 0; i < grpText.getText().length(); i++) {
	    			if (grpText.getText().charAt(i) == ' ') {
	    				spaceCount++;
	    			}
	    		}
	    		if (spaceCount == 0) {
	    			group[0] = grpText.getText().toUpperCase();
	    			group[1] = "";
	    		} else if (spaceCount == 1) {
	            	group = grpText.getText().toUpperCase().split(" ");
	    		} else {
                    JOptionPane.showMessageDialog(screen1,
                            "Group does not exist, check your input."
                            ,"ERROR"
                            ,JOptionPane.ERROR_MESSAGE);
	    		}
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
	    		if (group[1] == null) {
	    			group[1] = " ";
	    		}
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

    public void addModule(final JTable table, final GroupClass grp) {
        final JDialog addModuleDialog = new JDialog(screen1);

        JPanel addModulePanel = new JPanel();
        addModulePanel.setLayout(new GridBagLayout()); //new BoxLayout(editModulePanel, BoxLayout.Y_AXIS));

        //System.out.println(table.getValueAt(table.getSelectedRow(),0));
        //ModuleClass mod = programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString());

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
                ModuleClass module = new ModuleClass(titleText.getText(), subjText.getText().toUpperCase(),
                		crseText.getText().toUpperCase(),
                		grp.getType(), grp.getOptionalGroup(),
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
        //DefaultTableModel dm = (DefaultTableModel) table.getModel();
        //dm.removeRow(table.getSelectedRow());
        displayProgramme();
    }

    public void moveModule(JTable table) {
        final JDialog moveModuleDialog = new JDialog(screen1);

        JPanel moveModulePanel = new JPanel();
        moveModulePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        final ModuleClass mod = programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString() + table.getValueAt(table.getSelectedRow(),2).toString());

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
            	boolean validType = false;
            	boolean validGroup = false;
                for (GroupClass grp : programme.getGroups()) {
                	if (grp.getType().equals(toTypeText.getText().toUpperCase())) {
                		validType = true;
                		if (grp.getOptionalGroup().equals(toGrpText.getText().toUpperCase())) {
                			validGroup = true;
                		}
                	}
                }
                if (validType == false || validGroup == false) {
                    JOptionPane.showMessageDialog(screen1,
                            "Group does not exist, check your input."
                            ,"ERROR"
                            ,JOptionPane.ERROR_MESSAGE);                	
                } else {
                	mod.setType(toTypeText.getText().toUpperCase());
                	mod.setOptionalGroup(toGrpText.getText().toUpperCase());
                	programme.updateModule(mod);
                	moveModuleDialog.dispose();
                	displayProgramme();                	
                }
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
                "Index",
                "Activity",
                "Length"};

        final ModuleClass mod = programme.getModule(table.getValueAt(table.getSelectedRow(),0).toString() + table.getValueAt(table.getSelectedRow(),2).toString());

        final DefaultTableModel actModel = new DefaultTableModel(0, 0);
        actModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				Float hrs = mod.getHoursPerWeek();
				int row = e.getFirstRow();
				int col = e.getColumn();
                DefaultTableModel dm = (DefaultTableModel) e.getSource();
                if (row > -1 && col > -1) {
      				ActivityClass act = programme.getActivity(mod.getSubjCode()+mod.getCrseNumb(), (int) dm.getValueAt(row, 0));
                 	switch (col) {
                		case 1 : act.setDescription((String) dm.getValueAt(row, col));
                		         break;
                		case 2 : hrs = hrs - act.getLength();
                		         act.setLength(Float.parseFloat((String) dm.getValueAt(row, col)));
                		         hrs = hrs + act.getLength();
                		         mod.setHoursPerWeek(hrs);
                		         programme.updateModule(mod);
       		         			 break;
                	}
                	programme.updateActivity(act);
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
        actTable.getColumnModel().getColumn(0).setMaxWidth(40);
        actTable.getColumnModel().getColumn(2).setMaxWidth(40);


        JScrollPane actList = new JScrollPane(actTable);

        //http://www.codejava.net/java-se/swing/jtable-popup-menu-example
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuActAdd = new JMenuItem("Add New Activity");
        JMenuItem menuActDel = new JMenuItem("Delete Activity");

        menuActAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	addActivity(mod, actTable);
            	//dispActDialog.pack();
            	dispActDialog.repaint();
            	dispActDialog.revalidate();
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
                displayProgramme();
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
        dispActDialog.setSize(300, 150);
        //dispActDialog.pack();
        dispActDialog.setLocationRelativeTo(screen1);
        dispActDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dispActDialog.setVisible(true);
    }

    public void addActivity(final ModuleClass mod, final JTable table) {
        final JDialog addActivityDialog = new JDialog(screen1);

        JPanel addActivityPanel = new JPanel();
        addActivityPanel.setLayout(new GridBagLayout());

        //http://stackoverflow.com/questions/17204760/how-can-i-make-a-component-span-multiple-cells-in-a-gridbaglayout
        GridBagConstraints gbc = new GridBagConstraints();

        // Description
        JLabel descLabel = new JLabel("Description: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addActivityPanel.add(descLabel,gbc);

        final JTextField descText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addActivityPanel.add(descText,gbc);

        // Hours
        JLabel hrsLabel = new JLabel("Hours: ");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,10,0,0);
        gbc.weightx = 1;
        addActivityPanel.add(hrsLabel,gbc);

        final JTextField hrsText = new JTextField();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5,0,0,10);
        gbc.weightx = 1;
        addActivityPanel.add(hrsText,gbc);

        // Buttons
        JButton saveBtn = new JButton("Save");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10,10,10,0);
        gbc.weightx = 1;
        saveBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
            	int index = 0;
            	for(ActivityClass act : programme.getActivities()) {
            		if (act.getSubjectCode().equals(mod.getSubjCode()) &&
            				act.getCourseNumber().equals(mod.getCrseNumb())) {
            			if (act.getIndex() >= index) {
            				index = act.getIndex() + 1;
            			}
            		}
            	}
            	ActivityClass act = new ActivityClass(index, descText.getText(), Float.parseFloat(hrsText.getText()),
            			 mod.getSubjCode(), mod.getCrseNumb(), mod.getSemester());
            	programme.getActivities().add(act);

                DefaultTableModel dm = (DefaultTableModel) table.getModel();
                dm.addRow(new Object[] {act.getIndex(),act.getDescription(), act.getLength()});

                Float actHrs = mod.getHoursPerWeek();
                actHrs += act.getLength();
                mod.setHoursPerWeek(actHrs);
                programme.updateModule(mod);

                addActivityDialog.dispose();
            }
        });
        addActivityPanel.add(saveBtn,gbc);

        JButton cancelBtn = new JButton("Cancel");
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.insets = new Insets(10,0,10,10);
        gbc.weightx = 1;
        cancelBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                addActivityDialog.dispose();
            }
        });
        addActivityPanel.add(cancelBtn,gbc);

        addActivityDialog.add(addActivityPanel);
        addActivityDialog.setTitle("Add Activity");
        addActivityDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        addActivityDialog.setSize(200, 140);
        //addActivityDialog.pack();
        addActivityDialog.setLocationRelativeTo(screen1);
        addActivityDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addActivityDialog.setVisible(true);
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

        TimeTable timeTable = new TimeTable(programme);
        timeTable.timeTableActivities();
        //JPanel sem1Panel = new JPanel();
        middlePanel.add(new JLabel("Semester 1"));
        middlePanel.add(timeTable.createTimeTable("1"));
        //JPanel sem2Panel = new JPanel();
        middlePanel.add(new JLabel("Semester 2"));
        middlePanel.add(timeTable.createTimeTable("2"));

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
    	final JDialog valDialog = new JDialog();
    	JPanel valPanel = new JPanel();
    	valPanel.setLayout(new BoxLayout(valPanel, BoxLayout.Y_AXIS));

    	JPanel rptValPanel = new JPanel();
    	JPanel btnValPanel = new JPanel();
    	String report = new String("<html>");

    	boolean progViable = true;
    	boolean missingData = false;
    	boolean tooManyHrs = false;

    	float compTypeMinCredits = 0;
    	float optTypeMinCredits = 0;
    	float elecTypeMinCredits = 0;
    	float compTypeMaxCredits = 0;
    	float optTypeMaxCredits = 0;
    	float elecTypeMaxCredits = 0;
    	for (GroupClass grp : programme.getGroups()) {
    		if (grp.getType().equals("COMP")) {
       			compTypeMinCredits = Float.parseFloat(grp.getTypeMinCredits());
       			compTypeMaxCredits = Float.parseFloat(grp.getTypeMaxCredits());
    		} else if (grp.getType().equals("OPT")) {
       			optTypeMinCredits = Float.parseFloat(grp.getTypeMinCredits());
       			optTypeMaxCredits = Float.parseFloat(grp.getTypeMaxCredits());
    		} else if (grp.getType().equals("ELEC")) {
       			elecTypeMinCredits = Float.parseFloat(grp.getTypeMinCredits());
       			elecTypeMaxCredits = Float.parseFloat(grp.getTypeMaxCredits());
    		}
    	}
    	float compModCredits = 0;
    	float optModCredits = 0;
    	float elecModCredits = 0;
    	for (ModuleClass mod : programme.getModules()) {
    		if (mod.getType().equals("COMP")) {
    			compModCredits += mod.getCredits();
    		} else if (mod.getType().equals("OPT")) {
    			optModCredits += mod.getCredits();
    		} else if (mod.getType().equals("ELEC")) {
    			elecModCredits += mod.getCredits();
    		}
    		if (mod.getSemester().equals("1") ||
    				mod.getSemester().equals("2") ||
    				mod.getSemester().equals("3")) {
    		} else {
    			report += "Invalid Semester for module " + mod.getSubjCode()+mod.getCrseNumb() + "<br>";
    			progViable = false;
    			missingData = true;
    		}
    		if (mod.getHoursPerWeek() == 0) {
    			report += "Module " + mod.getSubjCode()+mod.getCrseNumb() + " has zero teaching hours<br>";
    			progViable = false;
    			missingData = true;
    		}
    	}
    	if (compModCredits < compTypeMinCredits) {
    		report += "Must choose a minimum of COMP " + compTypeMinCredits + " credits. " + compModCredits +" credits available.<br>";
    		progViable = false;
			missingData = true;
    	}
    	if (compModCredits < compTypeMaxCredits) {
    		report += "Must choose a maximum of COMP " + compTypeMinCredits + " credits. " + compModCredits +" credits available.<br>";
    		progViable = false;
			missingData = true;
    	}
    	if (optModCredits < optTypeMinCredits) {
    		report += "Must choose a minimum of OPT " + optTypeMinCredits + " credits. " + optModCredits +" credits available.<br>";
    		progViable = false;
			missingData = true;
    	}
    	if (optModCredits < optTypeMaxCredits) {
    		report += "Must choose a maximum of OPT " + optTypeMaxCredits + " credits.  " + optModCredits +" credits available.<br>";
    		progViable = false;
			missingData = true;
    	}
    	if (elecModCredits < elecTypeMinCredits) {
    		report += "Must choose a minimum of ELEC " + elecTypeMinCredits + " credits. " + elecModCredits +" credits available.<br>";
    	}
    	if (elecModCredits < elecTypeMaxCredits) {
    		report += "Must choose a maximum of ELEC " + elecTypeMaxCredits + " credits. " + elecModCredits +" credits available.<br>";
    	}

    	report += "<p><b>Sets: </b><br>";
    	programme.setGrpGrps(programme.createGrpGrps());

    	for (ArrayList<String> set : programme.getGrpGrps()) {
    		//for (String y : set) {
    		//	report += y + " ";
    		//}
    	   	report += "<b>" + set + " </b><br>";
    	   	float sem1Hrs = 0;
    	   	float sem2Hrs = 0;
    	   	for (ModuleClass mod : programme.getModules()) {
    	   		if (set.contains(mod.getType()+mod.getOptionalGroup())) {
    	   			if (mod.getSemester().equals("1")) {
    	   				sem1Hrs += mod.getHoursPerWeek();
    	   			} else if (mod.getSemester().equals("2")) {
    	   				sem2Hrs += mod.getHoursPerWeek();
    	   			} else if (mod.getSemester().equals("3")) {
    	   				sem1Hrs += mod.getHoursPerWeek();
    	   				sem2Hrs += mod.getHoursPerWeek();
    	   			}
    	   		}
    	   	}
	   		report += "   Semester 1 has " + Float.toString(sem1Hrs) +  " hrs teaching per week<br>";
	   		report += "   Semester 2 has " + Float.toString(sem2Hrs) +  " hrs teaching per week<br>";
    	   	if (sem1Hrs > 40.1) {
    	   		progViable = false;
    	   		tooManyHrs = true;
    	   	}
    	   	if (sem2Hrs > 40.1) {
    	   		progViable = false;
    	   		tooManyHrs = true;
    	   	}
      	}

    	if (progViable == false) {
    		report += "<p><b><u><font color=\"red\"> Programme is NOT VIABLE.</font></b></u><br>";
    		if (missingData) {
        		report += "Enter missing data.<br>";
    		}
    		if (tooManyHrs) {
        		report += "Try splitting Optional module groups into mutually exclusive sets.<br>";
    		}
       	} else {
    		report += "<p><b><u> Programme is IS VIABLE.</b></u><br>";
    		timeTableButton.setVisible(true);
    	}

    	JEditorPane valTextArea = new JEditorPane("text/html",report);
    	valTextArea.setPreferredSize(new Dimension(580,400));
    	valTextArea.setEditable(false);

    	JScrollPane valScrollPane = new JScrollPane(valTextArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	rptValPanel.add(valScrollPane);
    	valPanel.add(rptValPanel);

    	JButton backRptBtn = new JButton("Back");
    	backRptBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent event) {
    			valDialog.dispose();
    		}
    	});
    	btnValPanel.add(backRptBtn);
    	valPanel.add(btnValPanel);

        valDialog.add(valPanel);
        valDialog.setTitle("Validation Report");
        valDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        //valDialog.setSize(600, 400);
        valDialog.pack();
        valDialog.setLocationRelativeTo(screen1);
        valDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        valDialog.setVisible(true);
    }
}
