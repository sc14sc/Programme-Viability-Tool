/**
 * 
 */
package programmeViability;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @author Sitong Chen
 *
 */
public class TimeTable {
	
	private ProgrammeClass programme = null;
	
	public TimeTable(ProgrammeClass programme) {
		this.programme = programme;
	}
	
	public JScrollPane createTimeTable(String semester) {
		
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
				if (programme.getModule(activity.getSubjectCode(), 
						activity.getCourseNumber()).getSemester().equals(semester) || programme.getModule(activity.getSubjectCode(), 
								activity.getCourseNumber()).getSemester().equals("3")) {
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
		JTable timetable = new JTable(timeTableModel);
		timetable.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
		JScrollPane timetablePane = new JScrollPane(timetable);
		timetable.getColumnModel().getColumn(0).setMaxWidth(75);
		timetable.getColumnModel().getColumn(0).setCellRenderer(new RowHeaderRenderer(timetable));
		timetable.setFillsViewportHeight(true);
		
		return timetablePane;
		
	}
	
}
