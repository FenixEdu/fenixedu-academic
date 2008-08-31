/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;

/**
 * @author joaosa & rmalo
 * 
 */
public class InfoSiteStudentAndGroup extends DataTranferObject implements ISiteComponent {

    public static final Comparator<InfoSiteStudentAndGroup> COMPARATOR_BY_NUMBER = new Comparator<InfoSiteStudentAndGroup>() {

	@Override
	public int compare(InfoSiteStudentAndGroup o1, InfoSiteStudentAndGroup o2) {
	    return InfoSiteStudentInformation.COMPARATOR_BY_NUMBER.compare(o1.getInfoSiteStudentInformation(), o2.getInfoSiteStudentInformation());
	}
	
    };

    //Collections.sort(infoSiteStudentsAndGroupsList, new BeanComparator("infoSiteStudentInformation.number"));

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