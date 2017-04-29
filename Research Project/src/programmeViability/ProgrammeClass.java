/**
 * 
 */
package programmeViability;

import java.util.ArrayList;
import java.util.List;

/**
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
	private List<ArrayList<String>> grpGrps= new ArrayList<ArrayList<String>>();
	
	/**
	 * @return the activities
	 */
	public List<ActivityClass> getActivities() {
		return activities;
	}

	/**
	 * @return the modules
	 */
	public List<ModuleClass> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<ModuleClass> modules) {
		this.modules = modules;
	}

	/**
	 * @return the groups
	 */
	public List<GroupClass> getGroups() {
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(List<GroupClass> groups) {
		this.groups = groups;
	}

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
	
	public GroupClass getGroup(String group) {
		GroupClass grp = null;
		for (int i = 0; i < groups.size(); i++) {
			if (group.equals(groups.get(i).getType() + groups.get(i).getOptionalGroup())) {
				grp = groups.get(i);
			}
		}
		return grp;
	}
	
	public ModuleClass getModule(String module) {
		ModuleClass mod = null;
		for (int i = 0; i < modules.size(); i++) {
			if (module.equals(modules.get(i).getSubjCode() + modules.get(i).getCrseNumb() + modules.get(i).getSemester())) {
				mod = modules.get(i);
			}
		}
		return mod;
	}
	
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

						/*int result = grp.getOptionalGroup().compareTo(groups.get(i).getOptionalGroup());
						if (result < 0) {
							groupSort.add(grp);
							groupSort.add(groups.get(i));							
						} else {
							groupSort.add(groups.get(i));							
					
		}*/
		//for (int i = 0; i < groupSort.size(); i++) {
		//	System.out.println(i + " " + groupSort.get(i).getType() + " " + groupSort.get(i).getOptionalGroup());
		//}
		groups = groupSort;
	}
	
	public void deleteGroup(String[] grp) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getType().equals(grp[0]) && groups.get(i).getOptionalGroup().equals(grp[1])) {
				groups.remove(i);
			}
		}
	}
	
	
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
	public void updateModule(ModuleClass module) {
		for (int i = 0; i < modules.size(); i++) {
			if (module.getSubjCode().equals(modules.get(i).getSubjCode()) &&
					module.getCrseNumb().equals(modules.get(i).getCrseNumb()) &&
					module.getSemester().equals(modules.get(i).getSemester())) {
				modules.set(i, module);
			}
		}
	}
	
	public void updateActivity(ActivityClass activity) {
		for (int i = 0; i < activities.size(); i++) {
			if (activity.getDescription().equals(activities.get(i).getDescription()) &&
					activity.getSubjectCode().equals(activities.get(i).getSubjectCode())
					&& activity.getCourseNumber().equals(activities.get(i).getCourseNumber())) {
				activities.set(i, activity);
			}
		}
	}
	
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
	
	public List<ArrayList<String>> getGrpGrps() {
		return grpGrps;
	}

	public void setGrpGrps(List<ArrayList<String>> grpGrps) {
		this.grpGrps = grpGrps;
	}

	public List<ArrayList<String>> createGrpGrps() {
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
}
