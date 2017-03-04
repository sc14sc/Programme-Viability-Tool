/**
 * 
 */
package programmeViability;

/**
 * @author Sitong Chen
 *
 */
public class ActivityClass {
	
	private String description;
	private float length;
	private String subjectCode;
	private String courseNumber;
	
	public ActivityClass(String description, float length, String subjectCode,
			String courseNumber) {
		this.description = description;
		this.length = length;
		this.subjectCode = subjectCode;
		this.courseNumber = courseNumber;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the length
	 */
	public float getLength() {
		return length;
	}

	/**
	 * @return the subjectCode
	 */
	public String getSubjectCode() {
		return subjectCode;
	}

	/**
	 * @return the courseNumber
	 */
	public String getCourseNumber() {
		return courseNumber;
	}

}