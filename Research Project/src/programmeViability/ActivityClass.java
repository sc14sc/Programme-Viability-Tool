/**
 * 
 */
package programmeViability;

/**
 * Holds Activity Level Information
 * Activities are the objects which get timetabled, so also need to store day and time they 
 * start, set to 99 to indicate the activity has not been timetabled yet.
 * 
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
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the length
	 */
	public float getLength() {
		return length;
	}

	/**
	 * @param length
	 */
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

	/**
	 * @return the startTime
	 */
	public int getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

}
