/*
 * Created on Oct 14, 2003
 *  
 */
package DataBeans;

/**
 * @author: 
 * 			- Shezad Anavarali (sana@mega.ist.utl.pt)
 * 			- Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */
public class InfoExternalPerson extends InfoObject {
	private InfoPerson infoPerson;
	private String workLocation;

	/**
	 * @return Returns the infoPerson.
	 */
	public InfoPerson getInfoPerson() {
		return infoPerson;
	}
	/**
	 * @param infoPerson The infoPerson to set.
	 */
	public void setInfoPerson(InfoPerson infoPerson) {
		this.infoPerson = infoPerson;
	}
	/**
	 * @return Returns the workLocation.
	 */
	public String getWorkLocation() {
		return workLocation;
	}
	/**
	 * @param workLocation The workLocation to set.
	 */
	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof InfoExternalPerson) {
			InfoExternalPerson infoExternalPerson = (InfoExternalPerson) obj;
			result = this.getInfoPerson().equals(infoExternalPerson.getInfoPerson());
		}
		return result;
	}
}