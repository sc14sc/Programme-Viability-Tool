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
	private String optionalGroup;
	private float hoursPerWeek = 0;
	
	public ModuleClass(String title, String subjCode, String crseNumb
			, String optionalGroup) {
		this.title = title;
		this.subjCode = subjCode;
		this.crseNumb = crseNumb;
		this.optionalGroup = optionalGroup;
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


}
