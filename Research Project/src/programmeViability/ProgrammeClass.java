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
	
	public void Update(ModuleClass module) {
		for (int i = 0; i < modules.size(); i++) {
			if (module.getSubjCode().equals(modules.get(i).getSubjCode()) &&
					module.getCrseNumb().equals(modules.get(i).getCrseNumb())) {
				modules.set(i, module);
			}
		}
	}

}
