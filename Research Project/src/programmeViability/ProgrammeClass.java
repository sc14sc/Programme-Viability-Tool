/**
 * 
 */
package programmeViability;

import java.util.ArrayList;
import java.util.List;

/**
 * The ProgrammeClass holds all the information about a programme year, and has the methods written
 * to maintain the list of groups, modules and activities. The ProgrammeClass object is all that needs to be passed for any other class to have complete 
 * information about the Programme Year.
 *
 * @author Sitong Chen
 * 
 */
public class ProgrammeClass {
	
	private String progCode;
	private String progShortTitle;
	private String year;
	private List<GroupClass> groups = new ArrayList <GroupClass>();
	private List<ModuleClass> modules = new ArrayList <ModuleClass>();
	private List<ActivityClass> activities = new ArrayList <ActivityClass>();
	private List<ArrayList<String>> grpSets= new ArrayList<ArrayList<String>>();
	
	/**
	 * The minimum information to create a ProgrammeClass instance is:
	 * @param progCode
	 * @param progShortTitle
	 * @param year
	 */
	public ProgrammeClass (String progCode, String progShortTitle, String year) {		
		this.progCode = progCode;
		this.progShortTitle = progShortTitle;
		this.year = year;		
	}

	/**
	 * @return the progCode
	 */
	public String getProgCode() {
		return progCode;
	}

	/**
	 * @return the progShortTitle
	 */
	public String getProgShortTitle() {
		return progShortTitle;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @return all the groups in a ProgrammeClass
	 */
	public List<GroupClass> getGroups() {
		return groups;
	}

	/**
	 * @param set all the groups in a ProgrammeClass
	 */
	public void setGroups(List<GroupClass> groups) {
		this.groups = groups;
	}

	/**
	 * Finds individual group specified by a String (e.g. "OPTA") and returns one GroupClass object 
	 * 
	 * @return one group
	 */
	public GroupClass getGroup(String group) {
		GroupClass grp = null;
		for (int i = 0; i < groups.size(); i++) {
			if (group.equals(groups.get(i).getType() + groups.get(i).getOptionalGroup())) {
				grp = groups.get(i);
			}
		}
		return grp;
	}
	
	/**
	 * Adds a Group to the list of Groups stored in ProgrammeClass
	 * Groups have to be added in order COMP, then OPT, then ELEC, sorted by Optional Group within these Types.
	 * Have to write my own routine to do this, as collections.sort cannot sort class objects in this way. 
	 * 
	 */
	public void addGroup(GroupClass grp) {
		//System.out.println(grp.getType() + " " + grp.getOptionalGroup());
		boolean added = false;
		List <GroupClass> groupSort = new ArrayList <GroupClass>();
		if (groups.size() == 0) {
			groupSort.add(grp);
			added = true;
		} else {
			for (int i = 0; i < groups.size(); i++) {
				if (grp.getType().equals("COMP")) {
					if (groups.get(i).getType().equals("COMP")) {
						int result = grp.getOptionalGroup().compareTo(groups.get(i).getOptionalGroup());
						if (result < 0) {
							if (added == false) {
								//System.out.println("d. Adding " + grp.getType() + " " + grp.getOptionalGroup());
								groupSort.add(grp);
								added = true;
							}
							groupSort.add(groups.get(i));
							
						} else {
							groupSort.add(groups.get(i));
						}
						
					} else if (groups.get(i).getType().equals("OPT")) {
						if (added == false) {
							//System.out.println("b. Adding " + grp.getType() + " " + grp.getOptionalGroup());
							groupSort.add(grp);
							added = true;
						}
						groupSort.add(groups.get(i));
						
					} else if (groups.get(i).getType().equals("ELEC")) {
						if (added == false) {
							//System.out.println("c. Adding " + grp.getType() + " " + grp.getOptionalGroup());
							groupSort.add(grp);
							added = true;
						}
						groupSort.add(groups.get(i));
					}	
					
				} else if (grp.getType().equals("OPT")) {
					if (groups.get(i).getType().equals("COMP")) {
						groupSort.add(groups.get(i));
						
					} else if (groups.get(i).getType().equals("OPT")) {
						int result = grp.getOptionalGroup().compareTo(groups.get(i).getOptionalGroup());
						if (result < 0) {
							if (added == false) {
								//System.out.println("d. Adding " + grp.getType() + " " + grp.getOptionalGroup());
								groupSort.add(grp);
								added = true;
							}
							groupSort.add(groups.get(i));
							
						} else {
							groupSort.add(groups.get(i));
						}
						
						
					} else if (groups.get(i).getType().equals("ELEC")) {
						if (added == false) {
							//System.out.println("e. Adding " + grp.getType() + " " + grp.getOptionalGroup());
							groupSort.add(grp);
							added = true;
						}
						groupSort.add(groups.get(i));
						
					}	
					
				} else if (grp.getType().equals("ELEC")) {
					if (groups.get(i).getType().equals("COMP")) {
						groupSort.add(groups.get(i));
						
					} else if (groups.get(i).getType().equals("OPT")) {
						groupSort.add(groups.get(i));
						
					} else if (groups.get(i).getType().equals("ELEC")) {
						int result = grp.getOptionalGroup().compareTo(groups.get(i).getOptionalGroup());
						if (result < 0) {
							if (added == false) {
								//System.out.println("d. Adding " + grp.getType() + " " + grp.getOptionalGroup());
								groupSort.add(grp);
								added = true;
							}
							groupSort.add(groups.get(i));
							
						} else {
							groupSort.add(groups.get(i));
						}
						
					}						
				}	
			}	
		}		
		if (added == false) {
			//System.out.println("g. Adding " + grp.getType() + " " + grp.getOptionalGroup());
			groupSort.add(grp);
			added = true;
		}

		//for (int i = 0; i < groupSort.size(); i++) {
		//	System.out.println(i + " " + groupSort.get(i).getType() + " " + groupSort.get(i).getOptionalGroup());
		//}
		groups = groupSort;
	}
	
	/**
	 * DeleteGroup simply a matter of removing Group from the ProgrammClass list of groups.
	 */
	public void deleteGroup(String[] grp) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getType().equals(grp[0]) && groups.get(i).getOptionalGroup().equals(grp[1])) {
				groups.remove(i);
			}
		}
	}
	
	
	/**
	 * Find the group in the ProgrammeClass list of groups, and set this group to the new object.
	 * If Min or Max Credits for the type have been changed, then every group of that type must be updated to the new value.
	 */
	public void updateGroup(GroupClass group) {
		for (int i = 0; i < groups.size(); i++) {
			if (group.getType().equals(groups.get(i).getType())) {
				if (group.getOptionalGroup().equals(groups.get(i).getOptionalGroup())) {
					groups.set(i, group);
				} else {
					groups.get(i).setTypeMinCredits(group.getTypeMinCredits());
					groups.get(i).setTypeMaxCredits(group.getTypeMaxCredits());
				}
			}
		}
	}

	/**
	 * @return the list of all modules in the ProgrammeClass
	 */
	public List<ModuleClass> getModules() {
		return modules;
	}

	/**
	 * set the list of all modules in the ProgrammeClass
	 */
	public void setModules(List<ModuleClass> modules) {
		this.modules = modules;
	}

	/**
	 * @return the ModuleClass specified by the input string which consist of
	 *         Subject, Course, Semester (e.g. CLAS32502)
	 */
	public ModuleClass getModule(String module) {
		ModuleClass mod = null;
		for (int i = 0; i < modules.size(); i++) {
			if (module.equals(modules.get(i).getSubjCode() + modules.get(i).getCrseNumb() + modules.get(i).getSemester())) {
				mod = modules.get(i);
			}
		}
		return mod;
	}

	/**
	 * Deletes the module from the ProgrammeClass list of modules specified by the input string which consist of
	 *         Subject, Course, Semester (e.g. CLAS32502)
	 */
	public void deleteModule(String module) {
		for (int i = 0; i < modules.size(); i++) {
			if (module.equals(modules.get(i).getSubjCode() + modules.get(i).getCrseNumb())) {
				for (int j = activities.size() -1 ; j > -1; j--) {
					if (activities.get(j).getSubjectCode().equals(modules.get(i).getSubjCode()) &&
							activities.get(j).getCourseNumber().equals(modules.get(i).getCrseNumb())) {
						System.out.println(activities.get(j).getSubjectCode()+activities.get(j).getCourseNumber()+activities.get(j).getDescription() );
						activities.remove(j);
					}
				}
				modules.remove(i);
			}
		}
	}

	/**
	 * Updates the appropriate module in the ProgrammeClass module list
	 */
	public void updateModule(ModuleClass module) {
		for (int i = 0; i < modules.size(); i++) {
			if (module.getSubjCode().equals(modules.get(i).getSubjCode()) &&
					module.getCrseNumb().equals(modules.get(i).getCrseNumb()) &&
					module.getSemester().equals(modules.get(i).getSemester())) {
				modules.set(i, module);
			}
		}
	}

	/**
	 * @return the list of all activities in the ProgrammeClass
	 */
	public List<ActivityClass> getActivities() {
		return activities;
	}

	/**
	 * @return an individual activity for the received module and activity index number
	 */
	public ActivityClass getActivity(String module, int index) {
		ActivityClass act = null;
		for (int i = 0; i < activities.size(); i++) {
			if (module.equals(activities.get(i).getSubjectCode() + activities.get(i).getCourseNumber()) &&
					index == activities.get(i).getIndex()) {
				act = activities.get(i);
			}
		}
		return act;
	}
	
	/**
	 * @param an individual activity for the received module and activity index number
	 */
	public void updateActivity(ActivityClass activity) {
		for (int i = 0; i < activities.size(); i++) {
			if (activity.getIndex() == activities.get(i).getIndex() &&
					activity.getSubjectCode().equals(activities.get(i).getSubjectCode())
					&& activity.getCourseNumber().equals(activities.get(i).getCourseNumber())) {
				activities.set(i, activity);
			}
		}
	}
	
	/**
	 * Deletes an individual activity for the received module, activity description and activity index number
	 * @return the recalculated total weekly module hours
	 */
	public Float deleteActivity(int index, String activity, ModuleClass mod) {
		Float hrs = mod.getHoursPerWeek();
		for (int i = 0; i < activities.size(); i++) {
			if (activities.get(i).getSubjectCode().equals(mod.getSubjCode()) &&
					activities.get(i).getCourseNumber().equals(mod.getCrseNumb()) &&
					activities.get(i).getDescription().equals(activity) &&
					activities.get(i).getIndex() == index) {
				hrs -= activities.get(i).getLength();
				activities.remove(i);
			}
		}
		return hrs;
	}
	
	/**
	 * When groups are mutually exclusive with other groups, need to calculate a list of group sets
	 * e.g. COMP,OPTA,ELECA and COMP,OPTB,ELECA if OPTA is mutually exclusive with OPTB
	 * @return list of group lists 
	 */
	public List<ArrayList<String>> createGrpSets() {
		List<ArrayList<String>> stringArrayFinal = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> stringArrayFinalDeDup = new ArrayList<ArrayList<String>>();
		
		stringArrayFinal.add(new ArrayList<String>());
		for (GroupClass grp : groups) {
			stringArrayFinal.get(0).add(grp.getType() + grp.getOptionalGroup());
		}
		
		for (GroupClass grp : groups) {
			//System.out.println("1. Group: " + grp.getType()+grp.getOptionalGroup() + " " + stringArrayFinal.size());
			
			List<ArrayList<String>> stringArray = new ArrayList<ArrayList<String>>();			
			for (ArrayList<String> newGrp1 : stringArrayFinal) {
				if (grp.getExGroup().size() > 0) {
					ArrayList<String> newGrp2 = new ArrayList<String>();
					for (int i = 0; i < newGrp1.size(); i++) {
						newGrp2.add(newGrp1.get(i));
					}
					
					for (String exStr : grp.getExGroup()) {
						if (exStr.compareTo(grp.getOptionalGroup()) > 0) {
							if ((newGrp1.contains(grp.getType()+grp.getOptionalGroup())) &&
									newGrp1.contains(grp.getType()+exStr)) {
								//System.out.println("      Removing: " + grp.getType()+exStr);
								newGrp1.remove(newGrp1.indexOf(grp.getType()+exStr));								
							}
						}
					}
					boolean addGrp1 = true;
					for (ArrayList<String> testGrp : stringArray) {
						//System.out.println("   testGrp " + testGrp);
						boolean subExists = true;
						for (String testStr : newGrp1) {
							if (testGrp.contains(testStr)) {								
							} else {
								subExists = false;
							}
						}
						if (subExists == true) {
							addGrp1 = false;
						}
					}
					if (addGrp1 == true) {
						//System.out.println("   1.Adding newGrp1 " + newGrp1);
						stringArray.add(newGrp1);											
					}
	
					//System.out.println("   newGrp2 " + newGrp2);
					for (String exStr : grp.getExGroup()) {
						if (exStr.compareTo(grp.getOptionalGroup()) > 0) {
							if (newGrp2.contains(grp.getType()+exStr) &&
									newGrp2.contains(grp.getType()+grp.getOptionalGroup())) {
								//System.out.println("      Removing: " + grp.getType()+grp.getOptionalGroup());
								newGrp2.remove(newGrp2.indexOf(grp.getType()+grp.getOptionalGroup()));
							}
						}
					}
					
					//System.out.println("   newGrp2 " + newGrp2);
					boolean addGrp2 = true;
					for (ArrayList<String> testGrp : stringArrayFinal) {
						//System.out.println("   testGrp " + testGrp);
						boolean subExists = true;
						for (String testStr : newGrp2) {
							if (testGrp.contains(testStr)) {								
							} else {
								subExists = false;
							}
						}
						if (subExists == true) {
							addGrp2 = false;
						}
					}
					if (addGrp2 == true) {
						//System.out.println("   Adding newGrp2 " + newGrp2);
						stringArray.add(newGrp2);											
					}
				} else {
					//System.out.println("   2.Adding newGrp1 " + newGrp1);
					stringArray.add(newGrp1);
				}
			}
			stringArrayFinal = stringArray;				
		}	
				
		for (int i = 0; i < stringArrayFinal.size(); i++) {
			boolean found = false;
			if (stringArrayFinalDeDup.contains(stringArrayFinal.get(i))) {
				found = true;
			}
			if (found == false) {
				stringArrayFinalDeDup.add(stringArrayFinal.get(i));
			}
		}
		return stringArrayFinalDeDup;
	}

	/**
	 * @return list of group lists, e.g. COMP,OPTA,ELECA and COMP,OPTB,ELECA if OPTA is mutually exclusive with OPTB
	 */
	public List<ArrayList<String>> getGrpSets() {
		return grpSets;
	}

	/**
	 * Set the ProgrammeClass list of group sets 
	 */
	public void setGrpSets(List<ArrayList<String>> grpSets) {
		this.grpSets = grpSets;
	}
}
