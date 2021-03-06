package programmeViability;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This class reads the supplied Microsoft Access database for individual programme years 
 * and enables you to select one programme year.
 * 
 * @author Sitong Chen
 *
 */
public class SelectProgDB extends JDialog {
	//https://www.tutorialspoint.com/swing/swing_jlist.htm	
	//http://www.java2s.com/Tutorial/Java/0240__Swing/ASimpleModalDialog.htm
	
	//https://sourceforge.net/projects/ucanaccess/
	//http://stackoverflow.com/questions/21955256/manipulating-an-access-database-from-java-without-odbc
	
	private String[] result = new String[2];
	private JTextField progText = new JTextField("",14);
	//private JTextField yearText = new JTextField("",3);
	private List <String> progLocs = new ArrayList <String>();
	
	/**
	 * Outputs a list of programme years, with a search box which goes to the nearest
	 * matching programme as characters are entered into the search box.
	 * 
	 */
	public SelectProgDB(JFrame parent, String title, final String currProg) {
		super(parent, title, true);
		if (parent != null) {
			Dimension parentSize = parent.getSize();
			Point p = parent.getLocation(); 
			this.setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		}
		
		final JFrame mainFrame = new JFrame("Select Programme");
		mainFrame.setSize(800,800);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				mainFrame.dispose();
			}
		});
		
		final DefaultListModel progCodes = new DefaultListModel();
		final JList progList = new JList(progCodes);
		if (currProg.equals(" ")) {
		} else {
			progText.setText(currProg);			
		}
		//String queryProgs = "SELECT DISTINCT SWRPCAT_PROG_CODE, SWRPCMG_YEAR FROM PROGRAM WHERE SWRPCAT_PROG_CODE LIKE ? ORDER BY SWRPCAT_PROG_CODE,SWRPCMG_YEAR ";
		String queryProgs = "SELECT DISTINCT SWRPCAT_PROG_CODE, SWRPCMG_YEAR FROM PROGRAM ORDER BY SWRPCAT_PROG_CODE,SWRPCMG_YEAR ";
		PreparedStatement getProgCodes = null;
		try {
			String url = "jdbc:ucanaccess://Resource/ProgrammeCatalog.accdb";
			Connection conn = DriverManager.getConnection(url,"","");
			getProgCodes = conn.prepareStatement(queryProgs);
			//getProgCodes.setString(1, "ADD-NUR%");
			ResultSet rs = getProgCodes.executeQuery();  
			while (rs.next()) {
				String prog = rs.getString(1) + " " + rs.getString(2);
				progLocs.add(prog);
				progCodes.addElement(prog); 
			}
			
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception: " + ex.getMessage());
				JOptionPane.showMessageDialog(null,
						"SQL Exception: " + ex.getMessage(),
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
				ex = ex.getNextException();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}


		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//System.out.println("mouseClicked ");
				progText.setText(progList.getSelectedValue().toString());
			}
		};
		progList.addMouseListener(mouseListener);
		progList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		progList.setSelectedIndex(0);
		/*progList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (progText.getText().equals(progList.getSelectedValue().toString())) {
				} else {
					progText.setText(progList.getSelectedValue().toString());			}								
				}
		});*/
		
		
		Runnable doRun = new Runnable() {
			@Override
			public void run() {
				if (currProg.equals(" ")) {
					progList.setSelectedIndex(0);			
				} else {
					for (int i = 0; i < progLocs.size(); i++) {
						if (currProg.equals(progLocs.get(i))) {
							//System.out.println("Initial: " + i);
							progList.setSelectedIndex(i);
							progList.ensureIndexIsVisible(i);
							//progText.setText(progList.getSelectedValue().toString());
							break;					
						}
					}
				}
			}
		};
		SwingUtilities.invokeLater(doRun);

		JScrollPane progListScrollPane = new JScrollPane(progList);
		getContentPane().add(progListScrollPane);
		
		
	    JPanel buttonPane = new JPanel();
	    JLabel progLabel = new JLabel("Search: ");
		//progText.setText(progList.getSelectedValue().toString());
		progText.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				//System.out.println("insertUpdate " + progText.getText());
				search();
			}
			public void removeUpdate(DocumentEvent e) {
				//System.out.println("removeUpdate " + progText.getText());
				search();
			}
			public void changedUpdate(DocumentEvent e) {
				//System.out.println("changedUpdate " + progText.getText() + progText.getText().length());
				search();
			}
			public void search() {
				String searchString = progText.getText().toUpperCase();
				//System.out.println("searchString " + searchString);
				//System.out.println("searchString.length " + searchString.length());
				if (searchString.length() > 0) {
					for (int i = 0; i < progLocs.size(); i++) {
						//System.out.println("progLocs.get(i).length() " + progLocs.get(i).length());
						if (searchString.length() <= progLocs.get(i).length()) {
							//System.out.println("Substr progLocs " + progLocs.get(i).substring(0, (searchString.length())));
							if (searchString.equals(progLocs.get(i).substring(0, (searchString.length()))))  {
								//System.out.println("Later: " + i);
								progList.setSelectedIndex(i);
								progList.ensureIndexIsVisible(i);
								break;
							}							
						}
					}					
				}
			}
		});
		progText.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent event) {
	    		result = progList.getSelectedValue().toString().split(" ");
	    		if (result[1] == null) {
	    			result[1] = "1";
	    		}
	    		setVisible(false);
	    	}	    				
		});

		buttonPane.add(progLabel, BorderLayout.WEST);
		buttonPane.add(progText, BorderLayout.CENTER);
		
	    JButton button = new JButton("OK"); 
	    buttonPane.add(button); 
	    button.addActionListener(new ActionListener(){
	    	@Override
			public void actionPerformed(ActionEvent event) {
	    		result = progList.getSelectedValue().toString().split(" ");
	    		if (result[1] == null) {
	    			result[1] = "1";
	    		}
	    		setVisible(false);
	    	}	    	
	    });
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    pack(); 
	    setVisible(true);
	}
	
	/**
	 * @return the stored program code and year 
	 * 
	 */
	public String[] getProgCode() {
		return result;
	}
}
