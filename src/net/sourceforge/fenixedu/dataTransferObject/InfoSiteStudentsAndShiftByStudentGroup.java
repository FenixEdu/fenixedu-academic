/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;
import java.util.List;

/**
 * @author joaosa & rmalo
 * 
 */
public class InfoSiteStudentsAndShiftByStudentGroup extends DataTranferObject implements ISiteComponent {

    public static final Comparator<InfoSiteStudentsAndShiftByStudentGroup> COMPARATOR_BY_NUMBER
    		= new Comparator<InfoSiteStudentsAndShiftByStudentGroup>() {

		    @Override
		    public int compare(InfoSiteStudentsAndShiftByStudentGroup o1, InfoSiteStudentsAndShiftByStudentGroup o2) {
			final InfoStudentGroup g1 = o1.getInfoStudentGroup();
			final InfoStudentGroup g2 = o2.getInfoStudentGroup();
			return g1.getGroupNumber().compareTo(g2.getGroupNumber());
		    }
	
    };

    private List infoSiteStudentInformationList;
    private InfoStudentGroup infoStudentGroup;
    private InfoShift infoShift;

    /**
     * @return
     */
    public List getInfoSiteStudentInformationList() {
	return infoSiteStudentInformationList;
    }

    /**
     * @param list
     */
    public void setInfoSiteStudentInformationList(List infoSiteStudentInformationList) {
	this.infoSiteStudentInformationList = infoSiteStudentInformationList;
    }

    /**
     * @return
     */
    public InfoStudentGroup getInfoStudentGroup() {
	return infoStudentGroup;
    }

    /**
     * @param list
     */
    public void setInfoStudentGroup(InfoStudentGroup infoStudentGroup) {
	this.infoStudentGroup = infoStudentGroup;
    }

    /**
     * @return
     */
    public InfoShift getInfoShift() {
	return infoShift;
    }

    /**
     * @param list
     */
    public void setInfoShift(InfoShift infoShift) {
	this.infoShift = infoShift;
    }

}
