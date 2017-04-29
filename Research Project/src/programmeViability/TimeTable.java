package programmeViability;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TimeTable {
	
	private ProgrammeClass programme;

	public TimeTable(ProgrammeClass programme) {
		// TODO Auto-generated constructor stub
		this.programme = programme;
	}
	
	public JScrollPane createTimeTable(String semester) {
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
			    {"Monday"   , " "," "," "," "," "," "," "," "},
			    {"Tuesday"  , " "," "," "," "," "," "," "," "},
			    {"Wednesday", " "," "," "," "," "," "," "," "},
			    {"Thursday",  " "," "," "," "," "," "," "," "},
			    {"Friday",    " "," "," "," "," "," "," "," "},
			};

		for (ActivityClass activity : programme.getActivities()) {
			System.out.println(activity.getSubjectCode() + activity.getCourseNumber());
			ModuleClass mod = programme.getModule(activity.getSubjectCode() + activity.getCourseNumber());
			
			if (activity.getDay() != 99) {
				if (mod.getSemester().equals(semester) || mod.getSemester().equals("3")) {
					data[activity.getDay()][activity.getStartTime()] += activity.getSubjectCode()
							 + activity.getCourseNumber() + " " + activity.getDescription() + "\n";
				}				
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
		JTable timetable = new JTable(timeTableModel) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		timetable.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
		JScrollPane timetablePane = new JScrollPane(timetable);
		timetable.getColumnModel().getColumn(0).setMaxWidth(75);
		timetable.getColumnModel().getColumn(0).setCellRenderer(new RowHeaderRenderer(timetable));
		timetable.setFillsViewportHeight(true);
		
		return timetablePane;
	}

}
