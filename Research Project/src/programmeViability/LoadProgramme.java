package programmeViability;

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
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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
	                	prog.addGroup(group);   //getGroups().add(group);
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
									Float.valueOf(lineElement[14]), module.getSubjCode(), module.getCrseNumb(), module.getSemester());

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

		String queryExGrps = "SELECT EXGROUPS "
				+ "FROM PROGGROUP_EXCL "
				+ "WHERE SWRPCAT_PROG_CODE = ? "
				+ "AND SWRPCMG_YEAR = ? "
				+ "AND SWRPCMG_TYPE = ? "
				+ "AND SWRPCOG_OPTIONAL_GROUP = ? ";
		PreparedStatement getExGrps = null;

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

                	getExGrps = conn.prepareStatement(queryExGrps);
                	getExGrps.setString(1, prog.getProgCode());
                	getExGrps.setString(2, prog.getYear());
                   	getExGrps.setString(3, group.getType());
                   	getExGrps.setString(4, group.getOptionalGroup());

                   	String exGrp = new String();
                   	ResultSet ex = getExGrps.executeQuery();
        			while (ex.next()) {
        				exGrp = ex.getString(1);
        			}
        			group.setExGroup(exGrp);
                	prog.addGroup(group);   //getGroups().add(group);
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
						if (lineElement[18].replaceAll(" ", "").equals("1,2")) {
							lineElement[17] = "3";
						}
	                	//for (int i = 0; i < lineElement.length; i++){
	                	//	System.out.println("lineElement " + i + " " + lineElement[i]);
	                	//}
						module = new ModuleClass(lineElement[11], lineElement[9],
								 lineElement[10], lineElement[3], lineElement[6], lineElement[17].trim(), Float.valueOf(lineElement[19]));
						prog.getModules().add(module);
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
								Float.valueOf(lineElement[14]), module.getSubjCode(), module.getCrseNumb(), module.getSemester());

						prog.getActivities().add(activity);

						module.setHoursPerWeek(module.getHoursPerWeek() + activity.getLength());
						prog.updateModule(module);

					}
                }
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
			JOptionPane.showMessageDialog(null,
					ex.getMessage(),
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		return prog;
	}

	public void saveDB(ProgrammeClass prog) {

		ArrayList<ArrayList<String>> newProg = getProgData(prog);
		ArrayList<ArrayList<String>> newMod = getModData(prog);
		updateProg(prog, newProg);
		updateMod(newMod);

	}

	private ArrayList<ArrayList<String>> getProgData(ProgrammeClass prog) {
		ArrayList<ArrayList<String>> oldProg = new ArrayList<ArrayList<String>>();

		String queryProgram = "SELECT PROGRAM.SWRPCAT_REF_NUMBER, "	//  0
				+ "PROGRAM.SWRPCAT_PROG_CODE, "						//  1
				+ "PROGRAM.SWRPCAT_PROG_SHORT_TITLE, "				//  2
				+ "PROGRAM.SWRPCMG_YEAR, "							//  3
				+ "PROGRAM.SWRPCMG_TYPE, "							//  4
				+ "PROGRAM.SWRPCMG_TYPE_HEADER, "					//  5
				+ "PROGRAM.SWRPCMG_COMMENTS, "						//  6
				+ "PROGRAM.SWRPCMG_MIN_CREDITS, "					//  7
				+ "PROGRAM.SWRPCMG_MAX_CREDITS, "					//  8
				+ "PROGRAM.SWRPCOG_OPTIONAL_GROUP, "				//  9
				+ "PROGRAM.SWRPCOG_GROUP_HEADER, "					// 10
				+ "PROGRAM.SWRPCOG_GROUP_FOOTER, "					// 11
				+ "PROGRAM.SWRPCOG_MIN_CREDITS, "					// 12
				+ "PROGRAM.SWRPCOG_MAX_CREDITS, "					// 13
				+ "PROGRAM.SWRPCMD_SUBJ_CODE, "						// 14
				+ "PROGRAM.SWRPCMD_CRSE_NUMB, "						// 15
				+ "PROGRAM.SWRPCMD_PRE_REQ_FOR, "					// 16
				+ "PROGRAM.SWRPCMD_CO_REQ_FOR "						// 17
				+ "FROM PROGRAM "
				+ "WHERE (((PROGRAM.SWRPCAT_PROG_CODE)= ?) "
				+ "AND ((PROGRAM.SWRPCMG_YEAR)=?)) "
				+ "ORDER BY PROGRAM.SWRPCMG_TYPE, PROGRAM.SWRPCOG_OPTIONAL_GROUP, PROGRAM.SWRPCMD_SUBJ_CODE, PROGRAM.SWRPCMD_CRSE_NUMB ";
		PreparedStatement getProgram = null;

		try {
			Connection conn = DriverManager.getConnection(url,"","");
			getProgram = conn.prepareStatement(queryProgram);
			getProgram.setString(1, prog.getProgCode());
			getProgram.setString(2, prog.getYear());

			ResultSet rs = getProgram.executeQuery();
			while (rs.next()) {
				ArrayList<String> line = new ArrayList<String>();
				for (int i = 0; i < 18; i++ ) {
					line.add(rs.getString(i + 1));
				}
				oldProg.add(line);
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
			JOptionPane.showMessageDialog(null,
					ex.getMessage(),
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}

		//for (ArrayList<String> line : oldProg) {
		//	System.out.println(line.get(4) + line.get(9));
		//}


		ArrayList<ArrayList<String>> newProg = new ArrayList<ArrayList<String>>();
		for (GroupClass grp : prog.getGroups()) {
			boolean oldTypeFound = false;
			int oldIdx = 0;
			boolean oldGrpFound = false;
			for (int i = 0; i < oldProg.size(); i++) {
				if (oldProg.get(i).get(4) != null && oldProg.get(i).get(4).equals(grp.getType())) {
					if (oldTypeFound == false) {
						oldTypeFound = true;
						oldIdx = i;
					}
					if (oldProg.get(i).get(9) != null && oldProg.get(i).get(9).equals(grp.getOptionalGroup())) {
						oldGrpFound = true;
						oldIdx = i;
					}
				}
			}
			ArrayList<String> line = new ArrayList<String>();
			line.add(oldProg.get(0).get(0));
			line.add(prog.getProgCode());		//SWRPCAT_PROG_CODE, "			//  1
			line.add(oldProg.get(0).get(2));
			line.add(prog.getYear());			//SWRPCMG_YEAR, "				//  3
			line.add(grp.getType());			//SWRPCMG_TYPE, "				//  4
			if (oldTypeFound) {
				line.add(oldProg.get(oldIdx).get(5));
				line.add(oldProg.get(oldIdx).get(6));
			} else {
				line.add("");						//SWRPCMG_TYPE_HEADER, "		//  5
				line.add("");						//SWRPCMG_COMMENTS, "			//  6
			}
			line.add(grp.getTypeMinCredits());	//SWRPCMG_MIN_CREDITS, "		//  7
			line.add(grp.getTypeMaxCredits());	//SWRPCMG_MAX_CREDITS, "		//  8
			line.add(grp.getOptionalGroup());	//SWRPCOG_OPTIONAL_GROUP, "		//  9
			if (oldGrpFound) {
				line.add(oldProg.get(oldIdx).get(10));
				line.add(oldProg.get(oldIdx).get(11));
			} else {
				line.add("");						//SWRPCOG_GROUP_HEADER, "		// 10
				line.add("");						//SWRPCOG_GROUP_FOOTER, "		// 11
			}
			line.add(grp.getGroupMinCredits());	//SWRPCOG_MIN_CREDITS, "		// 12
			line.add(grp.getGroupMaxCredits());	//SWRPCOG_MAX_CREDITS, "		// 13
			line.add("");						//SWRPCMD_SUBJ_CODE, "		// 14
			line.add("");						//SWRPCMD_CRSE_NUMB, "		// 15
			line.add("");						//SWRPCMD_PRE_REQ_FOR, "	// 16
			line.add("");						//SWRPCMD_CO_REQ_FOR "		// 17

			int modCount = 0;
			for (ModuleClass mdl : prog.getModules()) {
				if (mdl.getType().equals(grp.getType()) && mdl.getOptionalGroup().equals(grp.getOptionalGroup())) {
					ArrayList<String> newLine = new ArrayList<String>();
					for (int i = 0; i < line.size(); i++) {
						if (i == 14) {
							newLine.add(mdl.getSubjCode());
						} else if (i == 15) {
							newLine.add(mdl.getCrseNumb());
						} else {
							newLine.add(line.get(i));
						}
					}
					newProg.add(newLine);
					modCount++;
				}
			}
			if (modCount == 0) {
				newProg.add(line);
			}
		}

		/*for (int i = 0; i < newProg.size(); i++) {
			for (int j = 0; j < 18; j++) {
				System.out.println(newProg.get(i).get(j));
			}
		}*/

		return newProg;

	}

	private void updateProg(ProgrammeClass prog, ArrayList<ArrayList<String>> newProg) {
		String deleteProgram = "DELETE  "
				+ "FROM PROGRAM "
				+ "WHERE (((PROGRAM.SWRPCAT_PROG_CODE)= ?) "
				+ "AND ((PROGRAM.SWRPCMG_YEAR)=?)) ";
		PreparedStatement delProgram = null;

		String insertProgram = "INSERT  "
				+ "INTO PROGRAM "
				+ "VALUES "
				+ "(? "		//  0
				+ ",? "		//  1
				+ ",? "		//  2
				+ ",? "		//  3
				+ ",? "		//  4
				+ ",? "		//  5
				+ ",? "		//  6
				+ ",? "		//  7
				+ ",? "		//  8
				+ ",? "		//  9
				+ ",? "		// 10
				+ ",? "		// 11
				+ ",? "		// 12
				+ ",? "		// 13
				+ ",? "		// 14
				+ ",? "		// 15
				+ ",? "		// 16
				+ ",? "		// 17
				+ ") ";
		PreparedStatement insProgram = null;

		String deleteProgExcl = "DELETE  "
				+ "FROM PROGGROUP_EXCL "
				+ "WHERE PROGGROUP_EXCL.SWRPCAT_PROG_CODE = ? "
				+ "AND   PROGGROUP_EXCL.SWRPCMG_YEAR = ? "
				+ "AND   PROGGROUP_EXCL.SWRPCMG_TYPE = ? "
				+ "AND   PROGGROUP_EXCL.SWRPCOG_OPTIONAL_GROUP = ? ";
		PreparedStatement delProgExcl = null;

		String insertProgExcl = "INSERT  "
				+ "INTO PROGGROUP_EXCL "
				+ "VALUES "
				+ "(? "		//  0
				+ ",? "		//  1
				+ ",? "		//  2
				+ ",? "		//  3
				+ ",? "		//  4
				+ ") ";
		PreparedStatement insProgExcl = null;


		Connection conn;
		try {
			conn = DriverManager.getConnection(url,"","");
			conn.setAutoCommit(false);
			try {
				delProgram = conn.prepareStatement(deleteProgram);
				delProgram.setString(1, prog.getProgCode());
				delProgram.setString(2, prog.getYear());
				delProgram.executeUpdate();

				for (int i = 0; i < newProg.size(); i++) {
					insProgram = conn.prepareStatement(insertProgram);
					insProgram.setString(1, newProg.get(i).get(0));
					insProgram.setString(2, newProg.get(i).get(1));
					insProgram.setString(3, newProg.get(i).get(2));
					insProgram.setString(4, newProg.get(i).get(3));
					insProgram.setString(5, newProg.get(i).get(4));
					insProgram.setString(6, newProg.get(i).get(5));
					insProgram.setString(7, newProg.get(i).get(6));
					insProgram.setString(8, newProg.get(i).get(7));
					insProgram.setString(9, newProg.get(i).get(8));
					insProgram.setString(10, newProg.get(i).get(9));
					insProgram.setString(11, newProg.get(i).get(10));
					insProgram.setString(12, newProg.get(i).get(11));
					insProgram.setString(13, newProg.get(i).get(12));
					insProgram.setString(14, newProg.get(i).get(13));
					insProgram.setString(15, newProg.get(i).get(14));
					insProgram.setString(16, newProg.get(i).get(15));
					insProgram.setString(17, newProg.get(i).get(16));
					insProgram.setString(18, newProg.get(i).get(17));

					insProgram.executeUpdate();
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
				try {
					conn.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (GroupClass grp : prog.getGroups()) {
				delProgExcl = conn.prepareStatement(deleteProgExcl);
				delProgExcl.setString(1, prog.getProgCode());
				delProgExcl.setString(2, prog.getYear());
				delProgExcl.setString(3, grp.getType());
				delProgExcl.setString(4, grp.getOptionalGroup());
				delProgExcl.executeUpdate();

				if (grp.getExGroup().size() > 0) {
					String exGroups = new String();
					String exGroup = new String();
					for (String exGrp : grp.getExGroup()) {
						exGroups += exGrp + ",";
					}
					if (exGroups.contains(",")) {
						exGroup = exGroups.substring(0, exGroups.lastIndexOf(","));
					} else {
						exGroup = exGroups;
					}
					insProgExcl = conn.prepareStatement(insertProgExcl);
					insProgExcl.setString(1, prog.getProgCode());
					insProgExcl.setString(2, prog.getYear());
					insProgExcl.setString(3, grp.getType());
					insProgExcl.setString(4, grp.getOptionalGroup());
					insProgExcl.setString(5, exGroup);
					insProgExcl.executeUpdate();
				}
			}

			conn.commit();

		} catch (SQLException e1) {
		// TODO Auto-generated catch block
			System.out.println("SQL Exception: " + e1.getMessage());
			JOptionPane.showMessageDialog(null,
					"SQL Exception: " + e1.getMessage(),
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private ArrayList<ArrayList<String>> getModData(ProgrammeClass prog) {
		ArrayList<ArrayList<String>> newMod = new ArrayList<ArrayList<String>>();

		String queryModule = "SELECT MODULE.SWRMCAT_MODULE_ID, "	//  0
				+ "MODULE.SWRMCAT_TITLE, "							//  1
				+ "MODULE.SWRMCAT_SUBJ, "							//  2
				+ "MODULE.SWRMCAT_CRSE_NUMB, "						//  3
				+ "MODULE.SWRMCAT_PRE_REQ_MODULE, "					//  4
				+ "MODULE.SWRMCAT_CO_REQ_MODULE, "					//  5
				+ "MODULE.SWRMCAT_INDPDT_OL_HRS, "					//  6
				+ "MODULE.SWRMCAT_PRIVATE_STUDY_HRS1, "				//  7
				+ "MODULE.SWRMCAT_PRIVATE_STUDY_HOW, "				//  8
				+ "MODULE.SWRMCAT_TOTAL_CONTACT_HRS, "				//  9
				+ "MODULE.SWRMCAT_TOTAL_HRS, "						// 10
				+ "MODULE.SWRTMDS_ACADSTF_CONT_HRS, "				// 11
				+ "MODULE.SWRTMDS_DLVT_CODE, "						// 12
				+ "MODULE.SWVDLVT_DESC, "							// 13
				+ "MODULE.SWRTMDS_LENGTH_HRS, "						// 14
				+ "MODULE.SWRTMDS_NUMBER_OF_DLVT, "					// 15
				+ "MODULE.SWRTMDS_STUDENT_HRS, "					// 16
				+ "MODULE.SWRMCAT_SEMESTER, "						// 17
				+ "MODULE.SWRMCAT_MULTI_SECTIONS, "					// 18
				+ "MODULE.SWRMCAT_CREDITS "							// 19
				+ "FROM MODULE "
				+ "WHERE (((MODULE.SWRMCAT_SUBJ) = ? ) "
				+ "AND ((MODULE.SWRMCAT_CRSE_NUMB) = ? )) "
				+ "ORDER BY MODULE.SWVDLVT_DESC ";
		PreparedStatement getModule = null;


		for (ModuleClass mdl : prog.getModules()) {
			ArrayList<String> line = new ArrayList<String>();
			ArrayList<ArrayList<String>> oldMod = new ArrayList<ArrayList<String>>();
			//System.out.println(mdl.getSubjCode()+mdl.getCrseNumb());

			try {
				Connection conn = DriverManager.getConnection(url,"","");
				getModule = conn.prepareStatement(queryModule);
				getModule.setString(1, mdl.getSubjCode());
				getModule.setString(2, mdl.getCrseNumb());

				ResultSet rs = getModule.executeQuery();
				while (rs.next()) {
					ArrayList<String> oldLine = new ArrayList<String>();
					for (int i = 0; i < 20; i++ ) {
						oldLine.add(rs.getString(i + 1));
					}
					oldMod.add(oldLine);
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

			if (oldMod.isEmpty()) {
				line.add(""); 						// 1 SWRMCAT_MODULE_ID
				line.add(mdl.getTitle());		 	// 2 SWRMCAT_TITLE
				line.add(mdl.getSubjCode());	 	// 3 SWRMCAT_SUBJ
				line.add(mdl.getCrseNumb());	 	// 4 SWRMCAT_CRSE_NUMB
				line.add("");					 	// 5 SWRMCAT_PRE_REQ_MODULE
				line.add("");						// 6 SWRMCAT_CO_REQ_MODULE
				line.add("");						// 7 SWRMCAT_INDPDT_OL_HRS
				line.add("");						// 8 SWRMCAT_PRIVATE_STUDY_HRS1
				line.add("");						// 9 SWRMCAT_PRIVATE_STUDY_HOW
				line.add("");						// 10 SWRMCAT_TOTAL_CONTACT_HRS
				line.add("");						// 11 SWRMCAT_TOTAL_HRS
				line.add("");						// 12 SWRTMDS_ACADSTF_CONT_HRS

			} else {
				line.add(oldMod.get(0).get(0)); 	// 1 SWRMCAT_MODULE_ID
				line.add(mdl.getTitle());			// 2 SWRMCAT_TITLE
				line.add(mdl.getSubjCode());		// 3 SWRMCAT_SUBJ
				line.add(mdl.getCrseNumb());		// 4 SWRMCAT_CRSE_NUMB
				line.add(oldMod.get(0).get(4));		// 5 SWRMCAT_PRE_REQ_MODULE
				line.add(oldMod.get(0).get(5));		// 6 SWRMCAT_CO_REQ_MODULE
				line.add(oldMod.get(0).get(6));		// 7 SWRMCAT_INDPDT_OL_HRS
				line.add(oldMod.get(0).get(7));		// 8 SWRMCAT_PRIVATE_STUDY_HRS1
				line.add(oldMod.get(0).get(8));		// 9 SWRMCAT_PRIVATE_STUDY_HOW
				line.add(oldMod.get(0).get(9));		// 10 SWRMCAT_TOTAL_CONTACT_HRS
				line.add(oldMod.get(0).get(10));	// 11 SWRMCAT_TOTAL_HRS
				line.add(oldMod.get(0).get(11));	// 12 SWRTMDS_ACADSTF_CONT_HRS

			}

			int actCount = 0;
			for (ActivityClass act : prog.getActivities()) {
				if (act.getSubjectCode().equals(mdl.getSubjCode()) &&
						act.getCourseNumber().equals(mdl.getCrseNumb()) &&
						act.getSemester().equals(mdl.getSemester())) {
					ArrayList<String> newLine = new ArrayList<String>();
					for (String val : line) {
						newLine.add(val);
					}
					int actIdx = 999;
					for (int i = 0; i < oldMod.size(); i++) {
						if (oldMod.get(i).get(2).equals(mdl.getSubjCode()) &&
								oldMod.get(i).get(3).equals(mdl.getCrseNumb()) &&
								oldMod.get(i).get(17).equals(mdl.getSemester())) {
							actIdx = i;
						}
					}

					if (actIdx != 999) {
						newLine.add(oldMod.get(actIdx).get(12));	// 13 SWRTMDS_DLVT_CODE

					} else {
						newLine.add("");							// 13 SWRTMDS_DLVT_CODE
					}
					newLine.add(act.getDescription()); 			// 14 SWVDLVT_DESC
					newLine.add(Float.toString(act.getLength())); 	// 15 SWRTMDS_LENGTH
					if (actIdx != 999) {
						newLine.add(oldMod.get(actIdx).get(15)); 	// 16 SWRTMDS_NUMBER_OF_DLVT
						newLine.add(oldMod.get(actIdx).get(16)); 	// 17 SWRTMDS_STUDENT_HRS
					} else {
						newLine.add(""); 							// 16 SWRTMDS_NUMBER_OF_DLVT
						newLine.add(""); 							// 17 SWRTMDS_STUDENT_HRS
					}
					newLine.add(act.getSemester());				// 18 SWRTMDS_SEMESTER
					if (actIdx != 999) {
						newLine.add(oldMod.get(actIdx).get(18));	// 19 SWRTMDS_MULTI_SECTIONS
					} else {
						newLine.add("");							// 19 SWRTMDS_MULTI_SECTIONS
					}
					//for (String val : newLine) {
					//	System.out.print("    "+val);
					//}
					newLine.add(Float.toString(mdl.getCredits()));			// 20 SWRTMDS_CREDITS
					newMod.add(newLine);
					//System.out.println("");
					actCount++;
				}
			}
			if (actCount == 0) {
				line.add("");					// 13 SWRTMDS_DLVT_CODE
				line.add(""); 					// 14 SWVDLVT_DESC
				line.add(""); 					// 15 SWRTMDS_LENGTH
				line.add(""); 					// 16 SWRTMDS_NUMBER_OF_DLVT
				line.add(""); 					// 17 SWRTMDS_STUDENT_HRS
				line.add("");					// 18 SWRTMDS_SEMESTER
				line.add("");					// 19 SWRTMDS_MULTI_SECTIONS
				line.add(Float.toString(mdl.getCredits()));			// 20 SWRTMDS_CREDITS
				newMod.add(line);
			}
		}

		/*for (int i = 0; i < newMod.size(); i++) {
			System.out.println(" ");
			for (String val : newMod.get(i)) {
				System.out.println("    "+val);
			}
		}*/

		return newMod;
	}

	private void updateMod(ArrayList<ArrayList<String>> newMod) {
		String deleteMdl = "DELETE  "
				+ "FROM MODULE "
				+ "WHERE (((MODULE.SWRMCAT_SUBJ)= ?) "
				+ "AND ((MODULE.SWRMCAT_CRSE_NUMB)=?)) ";
		PreparedStatement delMdl = null;

		String insertMdl = "INSERT  "
				+ "INTO MODULE "
				+ "VALUES "
				+ "(? "		//  0
				+ ",? "		//  1
				+ ",? "		//  2
				+ ",? "		//  3
				+ ",? "		//  4
				+ ",? "		//  5
				+ ",? "		//  6
				+ ",? "		//  7
				+ ",? "		//  8
				+ ",? "		//  9
				+ ",? "		// 10
				+ ",? "		// 11
				+ ",? "		// 12
				+ ",? "		// 13
				+ ",? "		// 14
				+ ",? "		// 15
				+ ",? "		// 16
				+ ",? "		// 17
				+ ",? "		// 18
				+ ",? "		// 19
				+ ") ";
		PreparedStatement insMdl = null;

		Connection conn;
		try {
			conn = DriverManager.getConnection(url,"","");
			conn.setAutoCommit(false);
			try {
				for (int i = 0; i < newMod.size(); i++) {
					delMdl = conn.prepareStatement(deleteMdl);
					delMdl.setString(1, newMod.get(i).get(2));
					delMdl.setString(2, newMod.get(i).get(3));
					delMdl.executeUpdate();
				}

				for (int i = 0; i < newMod.size(); i++) {
					//System.out.println(" ");
					//System.out.println(newMod.get(i).get(2)+newMod.get(i).get(3)+" "+newMod.get(i).get(17)+" "+newMod.get(i).get(13));

					insMdl = conn.prepareStatement(insertMdl);
					insMdl.setString(1, newMod.get(i).get(0));
					insMdl.setString(2, newMod.get(i).get(1));
					insMdl.setString(3, newMod.get(i).get(2));
					insMdl.setString(4, newMod.get(i).get(3));
					insMdl.setString(5, newMod.get(i).get(4));
					insMdl.setString(6, newMod.get(i).get(5));
					insMdl.setString(7, newMod.get(i).get(6));
					insMdl.setString(8, newMod.get(i).get(7));
					insMdl.setString(9, newMod.get(i).get(8));
					insMdl.setString(10, newMod.get(i).get(9));
					insMdl.setString(11, newMod.get(i).get(10));
					insMdl.setString(12, newMod.get(i).get(11));
					insMdl.setString(13, newMod.get(i).get(12));
					insMdl.setString(14, newMod.get(i).get(13));
					insMdl.setString(15, newMod.get(i).get(14));
					insMdl.setString(16, newMod.get(i).get(15));
					insMdl.setString(17, newMod.get(i).get(16));
					insMdl.setString(18, newMod.get(i).get(17));
					insMdl.setString(19, newMod.get(i).get(18));
					insMdl.setString(20, newMod.get(i).get(19));

					insMdl.executeUpdate();
				}

				conn.commit();

			} catch (SQLException ex) {
				while (ex != null) {
					System.out.println("SQL Exception: " + ex.getMessage());
					JOptionPane.showMessageDialog(null,
							"SQL Exception: " + ex.getMessage(),
							"ERROR",
							JOptionPane.ERROR_MESSAGE);
					ex = ex.getNextException();
				}
				try {
					conn.rollback();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
		// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,
					"SQL Exception: " + e1.getMessage(),
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
