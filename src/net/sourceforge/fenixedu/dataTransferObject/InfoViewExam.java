/*
 * InfoExam.java
 *
 * Created on 2003/03/19
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

public class InfoViewExam extends InfoObject {
    protected List infoViewExamsByDayAndShift;

    public InfoViewExam() {
    }

    public InfoViewExam(List infoViewExamsByDayAndShift) {
	this.setInfoViewExamsByDayAndShift(infoViewExamsByDayAndShift);
    }

    public boolean equals(Object obj) {
	if (obj instanceof InfoViewExam) {
	    InfoViewExam infoViewExam = (InfoViewExam) obj;
	    return this.getInfoViewExamsByDayAndShift().size() == infoViewExam.getInfoViewExamsByDayAndShift().size();
	}
	return false;
    }

    public List getInfoViewExamsByDayAndShift() {
	return infoViewExamsByDayAndShift;
    }

    public void setInfoViewExamsByDayAndShift(List list) {
	infoViewExamsByDayAndShift = list;
    }
}