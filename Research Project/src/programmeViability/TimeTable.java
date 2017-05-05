package programmeViability;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * This Class allocates times to activities in a ProgrammeClass, and sets up the display of the timetable
 * 
 * @author Sitong Chen
 *
 */
public class TimeTable {
	
	private ProgrammeClass programme;

	/**
	 * @param programme
	 */
	public TimeTable(ProgrammeClass programme) {
		// TODO Auto-generated constructor stub
		this.programme = programme;
	}
	
	/**
	 * @param semester to be timetabled
	 * @return JScrollPane with the display for a semesters timetable.
	 */
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
				"16:00 - 17:00",
				"17:00 - 18:00"};

		Object[][] data = {
			    {"Monday"   , " "," "," "," "," "," "," "," "," "},
			    {"Tuesday"  , " "," "," "," "," "," "," "," "," "},
			    {"Wednesday", " "," "," "," "," "},
			    {"Thursday",  " "," "," "," "," "," "," "," "," "},
			    {"Friday",    " "," "," "," "," "," "," "," "," "},
			};
		int actLength = 0;
		
		for (ActivityClass activity : programme.getActivities()) {
			//System.out.println(activity.getSubjectCode() + activity.getCourseNumber());
			ModuleClass mod = programme.getModule(activity.getSubjectCode() + activity.getCourseNumber() + activity.getSemester());
			
			if (activity.getDay() != 99) {
				if (mod.getSemester().equals(semester) || mod.getSemester().equals("3")) {
					actLength = (int) Math.ceil(activity.getLength());
					//System.out.println(activity.getSubjectCode()+activity.getCourseNumber() + " " + 
					//			activity.getDescription()+" "+activity.getDay()+" "+activity.getStartTime()+" "+actLength);
					for (int i = 0; i < actLength; i++) {
						data[activity.getDay()][activity.getStartTime()+i] += activity.getSubjectCode()
								 + activity.getCourseNumber() + " " + activity.getDescription() + "\n";						
					}
					//data[activity.getDay()][activity.getStartTime()] += activity.getSubjectCode()
					//					 + activity.getCourseNumber() + " " + activity.getDescription() + "\n";
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
	
	/**
	 * Goes through each activity in the programmeClass, and allocates day and start times for the activity
	 */
	public void timeTableActivities() {
		for (ActivityClass act : programme.getActivities()) {
			act.setDay(99);
			act.setStartTime(99);
			programme.updateActivity(act);
		}
		
		for (ActivityClass act : programme.getActivities()) {
			int[] timeSlot = findFreeTimeSlot(act);
			act.setDay(timeSlot[0]);
			act.setStartTime(timeSlot[1]);
            programme.updateActivity(act);
		}
	}
	
	/**
	 * Goes through each day and start time, and tests whether it is free for the activity
	 * Once a free time slot has been found for the search is terminated and the time slot is returned
	 * 
	 * @param activity to be allocated a time slot
	 * @return two integers in an array specifying the day and start time
	 */
	private int[] findFreeTimeSlot(ActivityClass act) {
		int[] timeSlot = new int[2];
		
		outerloop:
        for (int day = 0; day < 5; day++) {
        	if (day == 2) {
        		for (int time = 1; time < 6; time++) {
        			timeSlot[0] = day;
        			timeSlot[1] = time;
        			if (checkTimeSlot(timeSlot, act)) {
        				break outerloop;
        			}
        		}
        	} else {
        		for (int time = 1; time < 10; time++) {
           			timeSlot[0] = day;
        			timeSlot[1] = time;
        			if (checkTimeSlot(timeSlot, act)) {
        				break outerloop;
        			}
        		}
        	}
        }
        
		return timeSlot;
	}
	
	/**
	 * This goes through every activity to see if the received timeslot is free to timetable the received activity.
	 * Complications considered:
	 * 		Are the activities in the same Semester
	 * 		Length of the activities - one timeslot is one hour
	 * 		Are the activities in groups mutually exclusive with each other
	 * 
	 * @param timeSlot to be checked
	 * @param activity to be timetabled
	 * @return boolean (free/not free)
	 */
	private boolean checkTimeSlot(int[] timeSlot, ActivityClass activity) {
		boolean freeSlot = true;
		int activityLength = (int) Math.ceil(activity.getLength());
		//System.out.println("Activity: " + activity.getCourseNumber()+activity.getSubjectCode()+" "+activity.getSemester()+" "+activity.getDescription());
		ModuleClass module = programme.getModule(activity.getSubjectCode()+activity.getCourseNumber()+activity.getSemester());
		GroupClass group = programme.getGroup(module.getType() + module.getOptionalGroup());
		
		for (ActivityClass act : programme.getActivities()) {
			ModuleClass mdl = programme.getModule(act.getSubjectCode()+act.getCourseNumber()+act.getSemester());
			boolean check = true;
			if (group.getType().equals(mdl.getType())) {
				if (group.getExGroup().contains(mdl.getOptionalGroup())) {
					check = false;
				}					
			}
			
			if (act.getSemester().equals(activity.getSemester()) || 
					activity.getSemester().equals("3") ||
					act.getSemester().equals("3")) {
			} else {
				check = false;
			}
			
			if (check) {
				if (act.getDay() == timeSlot[0]) {
					int actLength = (int) Math.ceil(act.getLength());
					for (int i = 0; i < activityLength; i++) {
						for (int j = 0; j < actLength; j++) {
							if ((act.getStartTime()+j) == (timeSlot[1] + i)) {
								freeSlot = false;
							}
						}
						if (timeSlot[0] == 2) {
							if ((timeSlot[1] + i) > 5) {
								freeSlot = false;
							}
								
						} else {
							if ((timeSlot[1] + i) > 9) {
								freeSlot = false;
							}
						}
					}
				}
			}
		}
		
		return freeSlot;
	}


}
