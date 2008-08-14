/*
 * Created on 23/Setembro/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;
import java.util.ListIterator;

/**
 * @author asnr and scpo
 * 
 */
public class InfoSiteShiftsAndGroups extends DataTranferObject implements ISiteComponent {

    private List infoSiteGroupsByShiftList;
    private InfoGrouping infoGrouping;
    private Integer numberOfStudentsOutsideGrouping;
    private Integer numberOfStudentsInsideGrouping;

    /**
     * @return
     */
    public List getInfoSiteGroupsByShiftList() {
	return infoSiteGroupsByShiftList;
    }

    /**
     * @param list
     */
    public void setInfoSiteGroupsByShiftList(List infoSiteGroupsByShiftList) {
	this.infoSiteGroupsByShiftList = infoSiteGroupsByShiftList;
    }

    public boolean equals(Object objectToCompare) {
	boolean result = true;

	if (objectToCompare instanceof InfoSiteShiftsAndGroups)
	    result = true;

	if (((InfoSiteShiftsAndGroups) objectToCompare).getInfoSiteGroupsByShiftList() == null
		&& this.getInfoSiteGroupsByShiftList() == null) {
	    return true;
	}

	if (((InfoSiteShiftsAndGroups) objectToCompare).getInfoSiteGroupsByShiftList() == null
		|| this.getInfoSiteGroupsByShiftList() == null
		|| ((InfoSiteShiftsAndGroups) objectToCompare).getInfoSiteGroupsByShiftList().size() != this
			.getInfoSiteGroupsByShiftList().size()) {

	    return false;
	}

	ListIterator iter1 = ((InfoSiteShiftsAndGroups) objectToCompare).getInfoSiteGroupsByShiftList().listIterator();
	ListIterator iter2 = this.getInfoSiteGroupsByShiftList().listIterator();
	while (result && iter1.hasNext()) {
	    InfoSiteGroupsByShift infoSiteGroupsByShift1 = (InfoSiteGroupsByShift) iter1.next();
	    InfoSiteGroupsByShift infoSiteGroupsByShift2 = (InfoSiteGroupsByShift) iter2.next();

	    if (!infoSiteGroupsByShift1.equals(infoSiteGroupsByShift2)) {
		result = false;
	    }
	}
	return result;
    }

    public void setInfoGrouping(InfoGrouping infoGrouping) {
	this.infoGrouping = infoGrouping;
    }

    public InfoGrouping getInfoGrouping() {
	return infoGrouping;
    }

    public Integer getNumberOfStudentsOutsideAttendsSet() {
	return numberOfStudentsOutsideGrouping;
    }

    public void setNumberOfStudentsOutsideAttendsSet(Integer numberOfStudentsOutsideAttendsSet) {
	this.numberOfStudentsOutsideGrouping = numberOfStudentsOutsideAttendsSet;
    }

    public Integer getNumberOfStudentsInsideAttendsSet() {
	return numberOfStudentsInsideGrouping;
    }

    public void setNumberOfStudentsInsideAttendsSet(Integer numberOfStudentsInsideAttendsSet) {
	this.numberOfStudentsInsideGrouping = numberOfStudentsInsideAttendsSet;
    }
}
