package DataBeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Util.NumberUtils;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class InfoShiftGroupStatistics extends InfoObject {

	private List shiftsInGroup;
	public InfoShiftGroupStatistics() {
		this.shiftsInGroup = new ArrayList();
	}	

	
	
	/**
	 * @return
	 */
	public List getShiftsInGroup() {
		return shiftsInGroup;
	}


	/**
	 * @return
	 */
	public Integer getTotalCapacity() {
		Integer totalCapacity = new Integer(0);

		Iterator iterator = this.shiftsInGroup.iterator();
		while(iterator.hasNext()) {
			InfoShift infoShift = (InfoShift) iterator.next();
			totalCapacity = new Integer(totalCapacity.intValue() + infoShift.getLotacao().intValue());
		}
		
		return totalCapacity;
	}

	/**
	 * @return
	 */
	public Double getTotalPercentage() {
		Double totalPercentage = null;
		Integer totalCapacity = new Integer(0);
		Integer students = new Integer(0);

		Iterator iterator = this.shiftsInGroup.iterator();
		while(iterator.hasNext()) {
			InfoShift infoShift = (InfoShift) iterator.next();
			totalCapacity = new Integer(totalCapacity.intValue() + infoShift.getLotacao().intValue());
			students = new Integer(students.intValue() + infoShift.getOcupation().intValue());
		}

		totalPercentage = NumberUtils.formatNumber(new Double(students.floatValue() * 100 / totalCapacity.floatValue()), 1);

		return totalPercentage;
	}

	/**
	 * @param shiftsInGroup
	 */
	public void setShiftsInGroup(List shiftsInGroup) {
		this.shiftsInGroup = shiftsInGroup;
	}

	/**
	 * @return
	 */
	public Integer getTotalNumberOfStudents() {
		Integer totalNumberOfStudents = new Integer(0);

		Iterator iterator = this.shiftsInGroup.iterator();
		while(iterator.hasNext()) {
			InfoShift infoShift = (InfoShift) iterator.next();
			totalNumberOfStudents = new Integer(totalNumberOfStudents.intValue() + infoShift.getOcupation().intValue());
		}
		
		return totalNumberOfStudents;
	}

}