/**
 * 
 */
package programmeViability;

/**
 * Holds module level information
 * 
 * @author Sitong Chen
 *
 */
public class ModuleClass {
	
	private String title;
	private String subjCode;
	private String crseNumb;
	private String type;
	private String optionalGroup;
	private float hoursPerWeek = 0;
	private String semester;
	private float credits;
	
	public ModuleClass(String title, String subjCode, String crseNumb
			, String type, String optionalGroup, String semester, float credits) {
		this.title = title;
		this.subjCode = subjCode;
		this.crseNumb = crseNumb;
		this.type = type;
		this.optionalGroup = optionalGroup;
		this.semester = semester;
		this.credits = credits;
	}

	/**
	 * @return the subjCode
	 */
	public String getSubjCode() {
		return subjCode;
	}

	/**
	 * @return the crseNumb
	 */
	public String getCrseNumb() {
		return crseNumb;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the semester
	 */
	public String getSemester() {
		return semester;
	}

	/**
	 * @param semester
	 */
	public void setSemester(String semester) {
		this.semester = semester;
	}

	/**
	 * @return the Type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the optionalGroup
	 */
	public String getOptionalGroup() {
		return optionalGroup;
	}

	/**
	 * @param optionalGroup
	 */
	public void setOptionalGroup(String optionalGroup) {
		this.optionalGroup = optionalGroup;
	}

	/**
	 * @return the credits
	 */
	public float getCredits() {
		return credits;
	}

	/**
	 * @param credits
	 */
	public void setCredits(float credits) {
		this.credits = credits;
	}

	/**
	 * @return the hoursPerWeek 
	 */
	public float getHoursPerWeek() {
		return hoursPerWeek;
	}

	/**
	 * @param hoursPerWeek the hoursPerWeek to set
	 */
	public void setHoursPerWeek(float hoursPerWeek) {
		this.hoursPerWeek = hoursPerWeek;
	}


}
