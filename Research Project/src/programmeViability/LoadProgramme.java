package programmeViability;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LoadProgramme {
	
	private String url = "jdbc:ucanaccess://Resource/ProgrammeCatalog.accdb";
	
	public LoadProgramme() {
		
	}
	
	public ProgrammeClass loadFile() {
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

	            	try {

	            	//System.out.println(line);

	            	group = null;
	            	module = null;
	            	activity = null;

	                // use comma as separator
	                String[] lineElement = line.split(",");
	                //System.out.println("Split line");
	                for (int i = 0; i < lineElement.length; i++) {
	                	if (lineElement[i].equals("#Error")) {
	                		if (i == 14 || i == 19 ) {
	                			lineElement[i] = "0";
	                		} else {
		                   	 	lineElement[i] = "";
	                		}
	                	}
	                }
	                //System.out.println("Cleaned line : ");
                	//for (int i = 0; i < lineElement.length; i++){
                	//	System.out.println("lineElement " + i + " " + lineElement[i]);
                	//}

	                if (prog == null) {
	                	prog = new ProgrammeClass(lineElement[0], lineElement[1], lineElement[2]);
	                	//for (int i = 0; i < lineElement.length; i++){
	                	//	System.out.println("lineElement " + i + " " + lineElement[i]);
	                	//}
	                }
	                
	                for (GroupClass grp: prog.getGroups()) {
	                	if (grp.getOptionalGroup().equals(lineElement[6]) &&
	                			grp.getType().equals(lineElement[3])){
	                		group = grp;
	                	}
	                }
	                if (group == null) {
	                	//for (int i = 3; i < 9; i++){
	                	//	System.out.println("lineElement " + i + " " + lineElement[i]);
	                	//}
	                	group = new GroupClass(lineElement[3], lineElement[4], lineElement[5],
	                			lineElement[6], lineElement[7], lineElement[8]);
	                	prog.getGroups().add(group);
	                	//System.out.println("New Group: " + lineElement[3] + lineElement[6]);
	                }
	                
	                if (lineElement[9].equals("")) {
					} else {
						for (ModuleClass mdl: prog.getModules()) {
							if (mdl.getSubjCode().equals(lineElement[9]) &&
									mdl.getCrseNumb().equals(lineElement[10])) {
								module = mdl;
							}
						}
						if (module == null) {
							if (lineElement[18].equals("1 2")) {
								lineElement[17] = "3";
							}
		                	//for (int i = 9; i < 20; i++){
		                	//	System.out.println("lineElement " + i + " " + lineElement[i]);
		                	//}
							module = new ModuleClass(lineElement[11], lineElement[9],
									 lineElement[10], lineElement[3], lineElement[6], lineElement[17], Float.valueOf(lineElement[19]));
							prog.getModules().add(module);
		                	//System.out.println("New Module: " + lineElement[9] + lineElement[10]);

						}
						if (lineElement[13].equals("")) {
						} else {
		                	//System.out.println("New Activity: " + lineElement[13] + lineElement[14]);
							int index = 0;
							for (ActivityClass act : prog.getActivities()) {
								if (act.getSubjectCode().equals(module.getSubjCode()) &&
										act.getCourseNumber().equals(module.getCrseNumb())) {
									index++;
								}
							}
							activity = new ActivityClass(index, lineElement[13],
									Float.valueOf(lineElement[14]), module.getSubjCode(), module.getCrseNumb());

								prog.getActivities().add(activity);

								module.setHoursPerWeek(module.getHoursPerWeek() + activity.getLength());
								prog.updateModule(module);
			                	//System.out.println("New Activity: " + lineElement[13]);

						}
					}
	                //System.out.println("Module code = " + lineElement[5] + lineElement[6]);

	        		//listModel.addElement(mod);
	            	} catch (Exception e) {
	            		System.out.println("ERROR"  + line);
	    				JOptionPane.showMessageDialog(null,
	    						"Problem with line: " + line,
	    						"ERROR",
	    						JOptionPane.ERROR_MESSAGE);
	            	}

	            }

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
        		System.out.println("ERROR "  + "FileNotFoundException");
				JOptionPane.showMessageDialog(null,
						"FileNotFoundException",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
	       		System.out.println("ERROR "  + "IOException");
				JOptionPane.showMessageDialog(null,
						"IOException",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		
		return prog;
	}

	public ProgrammeClass loadAccess(String[] progCode) {
	//http://www.javacodehelp.com/java-tutorial/DatabaseExamples.html
	//https://sourceforge.net/projects/ucanaccess/
	//http://stackoverflow.com/questions/21955256/manipulating-an-access-database-from-java-without-odbc
		
		String[] lineElement;
		lineElement = new String[20];
		
		ProgrammeClass prog = null;
		GroupClass group = null;
		ModuleClass module = null;
		ActivityClass activity = null;

		//String prog_code = "BSC-NAT/SC";
		String year = "3";
		String queryProgs = "SELECT DISTINCT SWRPCAT_PROG_CODE, SWRPCAT_PROG_SHORT_TITLE, SWRPCMG_YEAR, SWRPCMG_TYPE, SWRPCMG_MIN_CREDITS, "
				+ "SWRPCMG_MAX_CREDITS,SWRPCOG_OPTIONAL_GROUP,SWRPCOG_MIN_CREDITS,SWRPCOG_MAX_CREDITS,SWRPCMD_SUBJ_CODE,SWRPCMD_CRSE_NUMB, "
				+ "SWRMCAT_TITLE,SWRTMDS_DLVT_CODE,SWVDLVT_DESC,SWRTMDS_LENGTH_HRS,SWRTMDS_NUMBER_OF_DLVT,SWRTMDS_STUDENT_HRS,SWRMCAT_SEMESTER, "
				+ "SWRMCAT_MULTI_SECTIONS,SWRMCAT_CREDITS "
				+ "FROM PROGRAM LEFT JOIN [MODULE] "
				+ "ON (MODULE.SWRMCAT_SUBJ = PROGRAM.SWRPCMD_SUBJ_CODE "
				+ "AND MODULE.SWRMCAT_CRSE_NUMB = PROGRAM.SWRPCMD_CRSE_NUMB) "
				+ "WHERE SWRPCAT_PROG_CODE = ? "
				+ "AND SWRPCMG_YEAR = ? "
				+ "ORDER BY SWRPCOG_OPTIONAL_GROUP, SWRPCMD_SUBJ_CODE, SWRPCMD_CRSE_NUMB";
		PreparedStatement getProgDtls = null;
		
 		
		try {
			Connection conn = DriverManager.getConnection(url,"","");
			conn.setAutoCommit(false);
			getProgDtls = conn.prepareStatement(queryProgs);
			getProgDtls.setString(1, progCode[0]);
			getProgDtls.setString(2, progCode[1]);
			
			ResultSet rs = getProgDtls.executeQuery();  
			while (rs.next()) {
				for (int i = 0; i < lineElement.length; i++ ) {
					lineElement[i] = "";
				}
				for (int i = 0; i < lineElement.length; i++ ) {
					lineElement[i] = rs.getString(i + 1);
				}
                for (int i = 0; i < lineElement.length; i++) {
                	if (lineElement[i] == null) {
                		if (i == 14 || i == 19 ) {
                			lineElement[i] = "0";
                		} else {
	                   	 	lineElement[i] = "";
                		}
                	}
                }
            	//for (int i = 0; i < lineElement.length; i++){
            	//	System.out.println("lineElement " + i + " " + lineElement[i]);
            	//}
            	
            	group = null;
            	module = null;
            	activity = null;
            	
                if (prog == null) {
                	prog = new ProgrammeClass(lineElement[0], lineElement[1], lineElement[2]);
                	//for (int i = 0; i < lineElement.length; i++){
                	//	System.out.println("lineElement " + i + " " + lineElement[i]);
                	//}
                }
               
                for (GroupClass grp: prog.getGroups()) {
                	if (grp.getOptionalGroup().equals(lineElement[6]) &&
                			grp.getType().equals(lineElement[3])){
                		group = grp;
                	}
                }
                if (group == null) {
                	//for (int i = 3; i < 9; i++){
                	//	System.out.println("lineElement " + i + " " + lineElement[i]);
                	//}
                	group = new GroupClass(lineElement[3], lineElement[4], lineElement[5],
                			lineElement[6], lineElement[7], lineElement[8]);
                	prog.getGroups().add(group);
                	//System.out.println("New Group: " + lineElement[3] + lineElement[6]);
                }
               
                if (lineElement[9].equals("")) {
				} else {
					for (ModuleClass mdl: prog.getModules()) {
						if (mdl.getSubjCode().equals(lineElement[9]) &&
								mdl.getCrseNumb().equals(lineElement[10])) {
							module = mdl;
						}
					}
					if (module == null) {
						if (lineElement[18].equals("1 2")) {
							lineElement[17] = "3";
						}
	                	//for (int i = 9; i < 20; i++){
	                	//	System.out.println("lineElement " + i + " " + lineElement[i]);
	                	//}
						module = new ModuleClass(lineElement[11], lineElement[9],
								 lineElement[10], lineElement[3], lineElement[6], lineElement[17], Float.valueOf(lineElement[19]));
						prog.getModules().add(module);
	                	//System.out.println("New Module: " + lineElement[9] + lineElement[10]);

					}
					if (lineElement[13].equals("")) {
					} else {
	                	//System.out.println("New Activity: " + lineElement[13] + lineElement[14]);
						int index = 0;
						for (ActivityClass act : prog.getActivities()) {
							if (act.getSubjectCode().equals(module.getSubjCode()) &&
									act.getCourseNumber().equals(module.getCrseNumb())) {
								index++;
							}
						}
						activity = new ActivityClass(index, lineElement[13],
								Float.valueOf(lineElement[14]), module.getSubjCode(), module.getCrseNumb());

							prog.getActivities().add(activity);

							module.setHoursPerWeek(module.getHoursPerWeek() + activity.getLength());
							prog.updateModule(module);
		                	//System.out.println("New Activity: " + lineElement[13]);

					}
                }
			}
			
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL Exception: " + ex.getMessage());
				ex = ex.getNextException();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return prog;
	}
	
	public void saveDB(ProgrammeClass prog) {
		String updateProgs = "SELECT DISTINCT SWRPCAT_PROG_CODE, SWRPCAT_PROG_SHORT_TITLE, SWRPCMG_YEAR, SWRPCMG_TYPE, SWRPCMG_MIN_CREDITS, "
				+ "SWRPCMG_MAX_CREDITS,SWRPCOG_OPTIONAL_GROUP,SWRPCOG_MIN_CREDITS,SWRPCOG_MAX_CREDITS,SWRPCMD_SUBJ_CODE,SWRPCMD_CRSE_NUMB, "
				+ "SWRMCAT_TITLE,SWRTMDS_DLVT_CODE,SWVDLVT_DESC,SWRTMDS_LENGTH_HRS,SWRTMDS_NUMBER_OF_DLVT,SWRTMDS_STUDENT_HRS,SWRMCAT_SEMESTER, "
				+ "SWRMCAT_MULTI_SECTIONS,SWRMCAT_CREDITS "
				+ "FROM PROGRAM LEFT JOIN [MODULE] "
				+ "ON (MODULE.SWRMCAT_SUBJ = PROGRAM.SWRPCMD_SUBJ_CODE "
				+ "AND MODULE.SWRMCAT_CRSE_NUMB = PROGRAM.SWRPCMD_CRSE_NUMB) "
				+ "WHERE SWRPCAT_PROG_CODE = ? "
				+ "AND SWRPCMG_YEAR = ? "
				+ "ORDER BY SWRPCOG_OPTIONAL_GROUP, SWRPCMD_SUBJ_CODE, SWRPCMD_CRSE_NUMB";
		PreparedStatement updProgDtls = null;
		
	}
}
