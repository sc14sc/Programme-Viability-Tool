/**
 * 
 */
package programmeViability;

/**
 * @author sitongchen
 *
 */
public class ModuleClass {
	
	private String progCode;
	private String progShortTitle;
	private String year;
	private String type;
	private String optionalGroup;
	private String subjCode;
	private String crseNumb;
	
	public ModuleClass(String progCode, String progShortTitle, String year, String type, String optionalGroup, String subjCode, String crseNumb) {
		this.progCode = progCode;
		this.progShortTitle = progShortTitle;
		this.year = year;
		this.type = type;
		this.optionalGroup = optionalGroup;
		this.subjCode = subjCode;
		this.crseNumb = crseNumb;
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
	 * @return the type
	 */
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
