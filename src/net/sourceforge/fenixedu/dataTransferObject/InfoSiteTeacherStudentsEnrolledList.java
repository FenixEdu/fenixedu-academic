/*
 * Created on 28/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 */
public class InfoSiteTeacherStudentsEnrolledList extends DataTranferObject implements ISiteComponent {
    private List infoStudents;

    private InfoExam infoExam;

    private InfoWrittenTest infoWrittenTest;

    private List infoWrittenEvaluationEnrolmentList;

    public InfoSiteTeacherStudentsEnrolledList() {

    }

    public InfoSiteTeacherStudentsEnrolledList(List infoStudents, InfoExam infoExam, List infoWrittenEvaluationEnrolmentList) {
	setInfoExam(infoExam);
	setInfoWrittenTest(null);
	setInfoStudents(infoStudents);
	setInfoWrittenEvaluationEnrolmentList(infoWrittenEvaluationEnrolmentList);
    }

    public InfoSiteTeacherStudentsEnrolledList(List infoStudents, InfoWrittenTest infoWrittenTest,
	    List infoWrittenEvaluationEnrolmentList) {
	setInfoExam(null);
	setInfoWrittenTest(infoWrittenTest);
	setInfoStudents(infoStudents);
	setInfoWrittenEvaluationEnrolmentList(infoWrittenEvaluationEnrolmentList);
    }

    public List getInfoWrittenEvaluationEnrolmentList() {
	return this.infoWrittenEvaluationEnrolmentList;
    }

    public void setInfoWrittenEvaluationEnrolmentList(List infoWrittenEvaluationEnrolmentList) {
	this.infoWrittenEvaluationEnrolmentList = infoWrittenEvaluationEnrolmentList;
    }

    public int getSize() {
	if (getInfoStudents() == null) {
	    return 0;
	}
	return getInfoStudents().size();

    }

    public List getInfoStudents() {
	return infoStudents;
    }

    public void setInfoStudents(List list) {
	infoStudents = list;
    }

    public boolean equals(Object arg0) {
	boolean result = false;

	if (arg0 instanceof InfoSiteTeacherStudentsEnrolledList) {
	    InfoSiteTeacherStudentsEnrolledList component = (InfoSiteTeacherStudentsEnrolledList) arg0;
	    result = component.getInfoStudents().containsAll(this.getInfoStudents())
		    && this.getInfoStudents().containsAll(component.getInfoStudents());
	}

	return result;
    }

    public InfoExam getInfoExam() {
	return infoExam;
    }

    public void setInfoExam(InfoExam exam) {
	infoExam = exam;
    }

    public InfoWrittenTest getInfoWrittenTest() {
	return infoWrittenTest;
    }

    public void setInfoWrittenTest(InfoWrittenTest infoWrittenTest) {
	this.infoWrittenTest = infoWrittenTest;
    }
}