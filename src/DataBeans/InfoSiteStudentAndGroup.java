/*
 * Created on 8/Jan/2005
 *
 */
package DataBeans;


/**
 * @author joaosa & rmalo
 *
 */
public class InfoSiteStudentAndGroup extends DataTranferObject implements ISiteComponent {

	private InfoSiteStudentInformation infoSiteStudentInformation;
	private InfoStudentGroup infoStudentGroup;
	
	
	/**
	* @return InfoSiteStudentInformation
	*/
	public InfoSiteStudentInformation getInfoSiteStudentInformation() {
		return infoSiteStudentInformation;
	}

	/**
	 * @param InfoSiteStudentInformation
	 */
	public void setInfoSiteStudentInformation(InfoSiteStudentInformation infoSiteStudentInformation) {
		this.infoSiteStudentInformation = infoSiteStudentInformation;
	}
	

	/**
	* @return InfoStudentGroup
	*/
	public InfoStudentGroup getInfoStudentGroup() {
		return infoStudentGroup;
	}

	/**
	 * @param InfoStudentGroup
	 */
	public void setInfoStudentGroup(InfoStudentGroup infoStudentGroup) {
		this.infoStudentGroup = infoStudentGroup;
	}
}