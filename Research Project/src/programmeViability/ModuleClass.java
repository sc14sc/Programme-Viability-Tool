/**
 * 
 */
package programmeViability;

/**
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
	 * @return the semester
	 */
	public String getSemester() {
		return semester;
	}

	/**
	 * @return the credits
	 */
	public float getCredits() {
		return credits;
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
	
	public String getType() {
		return type;
	}

	/**
	 * @return the optionalGroup
	 */
	public String getOptionalGroup() {
		return optionalGroup;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
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

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOptionalGroup(String optionalGroup) {
		this.optionalGroup = optionalGroup;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public void setCredits(float credits) {
		this.credits = credits;
	}


}
