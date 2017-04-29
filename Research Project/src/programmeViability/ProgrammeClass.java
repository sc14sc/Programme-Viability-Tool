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
	private List <GroupClass> groups = new ArrayList <GroupClass>();
	private List <ModuleClass> modules = new ArrayList <ModuleClass>();
	private List <ActivityClass> activities = new ArrayList <ActivityClass>();
	
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
	
	public ModuleClass getModule(String module) {
		ModuleClass mod = null;
		for (int i = 0; i < modules.size(); i++) {
			if (module.equals(modules.get(i).getSubjCode() + modules.get(i).getCrseNumb())) {
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
	
	public void deleteGroup(String[] grp) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getType().equals(grp[0]) && groups.get(i).getOptionalGroup().equals(grp[1])) {
				groups.remove(i);
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
	

}
