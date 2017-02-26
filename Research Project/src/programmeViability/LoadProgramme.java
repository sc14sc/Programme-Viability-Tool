/**
 * 
 */
package programmeViability;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
		JButton addModuleButton = new JButton("Add Module");
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evebt){
				System.exit(1);
			}
		});
		
		JLabel programmeLabel = new JLabel("Please Load File");
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

}
