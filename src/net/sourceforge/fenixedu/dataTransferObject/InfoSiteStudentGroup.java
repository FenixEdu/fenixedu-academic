/*
 * Created on 4/Ago/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 * 
 */
public class InfoSiteStudentGroup extends DataTranferObject implements ISiteComponent {

    public static final Comparator<InfoSiteStudentGroup> COMPARATOR_BY_NUMBER = new Comparator<InfoSiteStudentGroup>() {

	@Override
	public int compare(InfoSiteStudentGroup o1, InfoSiteStudentGroup o2) {
	    final InfoStudentGroup g1 = o1.getInfoStudentGroup();
	    final InfoStudentGroup g2 = o2.getInfoStudentGroup();
	    return g1.getGroupNumber().compareTo(g2.getGroupNumber());
	}
	
    };

    //Collections.sort(infoSiteStudentGroupsList, new BeanComparator("infoStudentGroup.groupNumber"));

    private List infoSiteStudentInformationList;

    private InfoStudentGroup infoStudentGroup;

    private Object nrOfElements;

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
    public Object getNrOfElements() {
	return nrOfElements;
    }

    /**
     * @param list
     */
    public void setNrOfElements(Object nrOfElements) {
	this.nrOfElements = nrOfElements;
    }

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

    public boolean equals(Object objectToCompare) {
	boolean result = false;

	if (objectToCompare instanceof InfoSiteStudentGroup) {
	    if (this.getInfoStudentGroup() == null && ((InfoSiteStudentGroup) objectToCompare).getInfoStudentGroup() == null)
		result = true;
	    else
		result = (this.getInfoStudentGroup().equals(((InfoSiteStudentGroup) objectToCompare).getInfoStudentGroup()));

	}

	if (((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList() == null
		&& this.getInfoSiteStudentInformationList() == null && result) {

	    return true;
	}

	if (((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList() == null
		|| this.getInfoSiteStudentInformationList() == null
		|| ((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList().size() != this
			.getInfoSiteStudentInformationList().size()) {

	    return false;
	}

	ListIterator iter1 = ((InfoSiteStudentGroup) objectToCompare).getInfoSiteStudentInformationList().listIterator();
	ListIterator iter2 = this.getInfoSiteStudentInformationList().listIterator();
	while (result && iter1.hasNext()) {
	    InfoSiteStudentInformation infoSiteStudentInformation1 = (InfoSiteStudentInformation) iter1.next();
	    InfoSiteStudentInformation infoSiteStudentInformation2 = (InfoSiteStudentInformation) iter2.next();

	    if (!infoSiteStudentInformation1.equals(infoSiteStudentInformation2)) {

		result = false;
	    }
	}
	return result;
    }
}