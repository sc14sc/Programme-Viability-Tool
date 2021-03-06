/**
 * 
 */
package programmeViability;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Holds Type and Group level information. 
 * 
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
	ArrayList<String> exGroup = new ArrayList<String>();
	//private String[] exGroup = {""};
	
	/**
	 * @param type
	 * @param typeMinCredits
	 * @param typeMaxCredits
	 * @param optionalGroup
	 * @param groupMinCredits
	 * @param groupMaxCredits
	 */
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
	 * @param typeMinCredits
	 */
	public void setTypeMinCredits(String typeMinCredits) {
		this.typeMinCredits = typeMinCredits;
	}

	/**
	 * @param typeMaxCredits
	 */
	public void setTypeMaxCredits(String typeMaxCredits) {
		this.typeMaxCredits = typeMaxCredits;
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

	/**
	 * @return list of groups this group is mutually exclusive with
	 */
	public ArrayList<String> getExGroup() {
		return exGroup;
	}

	/**
	 * Adds a new group to the list of mutually exclusive groups, and sorts that list.
	 */
	public void updExGroup(String exGrp) {
		ArrayList<String> cleanxGrps = new ArrayList<String>();
		for (int i = 0; i < exGroup.size(); i++) {
			cleanxGrps.add(exGroup.get(i));
		}
		if (cleanxGrps.contains(exGrp)) {
		} else {
			cleanxGrps.add(exGrp);
		}
		Collections.sort(cleanxGrps);
		
		this.exGroup = cleanxGrps;
	}
	
	/**
	 * deletes a group from the list of mutually exclusive groups
	 */
	public void delExGroup(String exGrp) {
		ArrayList<String> cleanxGrps = new ArrayList<String>();
		//System.out.println("delExGroup: "+ exGroup.size());
		for (int i = 0; i < exGroup.size(); i++) {
			//System.out.println("delExGroup: "+ exGroup.get(i));
			if (exGrp.equals(exGroup.get(i))) {
			} else {
				cleanxGrps.add(exGroup.get(i));
			}
		}
		//System.out.println("delExGroup: "+ cleanxGrps.size());
		
		//Collections.sort(cleanxGrps);
		
		this.exGroup = cleanxGrps;
	}
	
	/**
	 * gets a comma separated list of mutually exclusive groups in a String, and stores that list as a
	 * sorted ArrayList.
	 */
	public void setExGroup(String exGroup) {
		ArrayList<String> cleanxGrps = new ArrayList<String>();
		int commaCount = 0;
		for (int i = 0; i < exGroup.length(); i++) {
			if (exGroup.charAt(i) == ',') {
				commaCount++;
			}
		}
		if (commaCount == 0) {
			if (exGroup.trim().equals("")) {
				
			} else {
				cleanxGrps.add(exGroup.trim());				
			}
		} else {
			String[] mexGrps = exGroup.split(",");
			for (int i = 0; i < mexGrps.length; i++) {
				cleanxGrps.add(mexGrps[i].trim());			
			}
			
		}
		Collections.sort(cleanxGrps);
		
		this.exGroup = cleanxGrps;
	}

}
