/**
 * 
 */
package programmeViability;

/**
 * @author Sitong Chen
 *
 */
public class GroupClass {
	
	private String type;
	private String typeMinCredits;
	private String typeMaxCredits;
	private String optionalGroup;
	private String groupMinCredits;
	private String groupMaxCredits;
	
	public GroupClass (String type, String typeMinCredits, String typeMaxCredits,
			String optionalGroup, String groupMinCredits, String groupMaxCredits) {
		this.type = type;
		this.typeMinCredits = typeMinCredits;
		this.typeMaxCredits = typeMaxCredits;
		this.optionalGroup = optionalGroup;
		this.groupMinCredits = groupMinCredits;
		this.groupMaxCredits = groupMaxCredits;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the typeMinCredits
	 */
	public String getTypeMinCredits() {
		return typeMinCredits;
	}

	/**
	 * @return the typeMaxCredits
	 */
	public String getTypeMaxCredits() {
		return typeMaxCredits;
	}

	/**
	 * @return the optionalGroup
	 */
	public String getOptionalGroup() {
		return optionalGroup;
	}

	/**
	 * @return the groupMinCredits
	 */
	public String getGroupMinCredits() {
		return groupMinCredits;
	}

	/**
	 * @return the groupMaxCredits
	 */
	public String getGroupMaxCredits() {
		return groupMaxCredits;
	}

}
