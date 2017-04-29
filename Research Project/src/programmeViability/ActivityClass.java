/**
 * 
 */
package programmeViability;

/**
 * @author Sitong Chen
 *
 */
public class ActivityClass {
	
	private int index;
	private String description;
	private float length;
	private String subjectCode;
	private String courseNumber;
	private String semester;
	private int startTime = 99;
	private int day = 99;
	
	/**
	 * @return the startTime
	 */
	public int getStartTime() {
		return startTime;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	public ActivityClass(int index, String description, float length, String subjectCode,
			String courseNumber, String semester) {
		this.index = index;
		this.description = description;
		this.length = length;
		this.subjectCode = subjectCode;
		this.courseNumber = courseNumber;
		this.semester = semester;
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

	public void setIndex(int index) {
		this.index = index;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLength(float length) {
		this.length = length;
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

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

}
