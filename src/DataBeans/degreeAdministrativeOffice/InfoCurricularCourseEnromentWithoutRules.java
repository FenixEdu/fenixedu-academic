package DataBeans.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoStudent;

public class InfoCurricularCourseEnromentWithoutRules {

	private List infoExecutionDegreesList;
	private List lastEnrolmentsList;
	private List enrolmentsToRemoveList;
	private InfoStudent infoStudent;
	private InfoExecutionDegree chosenInfoExecutionDegree;
	private Integer chosenSemester;
	private Integer chosenYear;

	public InfoCurricularCourseEnromentWithoutRules() {
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "infoExecutionDegreesList = " + this.infoExecutionDegreesList + "; ";
		result += "infoStudent = " + this.infoStudent + "]\n";
		return result;
	}

	public List getExecutionDegreesLableValueBeanList() {
		ArrayList result = null;
		if ( (this.infoExecutionDegreesList != null) && (!this.infoExecutionDegreesList.isEmpty()) ) {
			result = new ArrayList();
			result.add(new LabelValueBean("Escolha", ""));
			Iterator iterator = infoExecutionDegreesList.iterator();
			while(iterator.hasNext()) {
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
				Integer index = new Integer(infoExecutionDegreesList.indexOf(infoExecutionDegree));
				result.add(new LabelValueBean(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(), index.toString()));
			}
		}
		return result;	
	}
	/**
	 * @return
	 */
	public List getInfoExecutionDegreesList() {
		return infoExecutionDegreesList;
	}

	/**
	 * @return
	 */
	public InfoStudent getInfoStudent() {
		return infoStudent;
	}

	/**
	 * @param list
	 */
	public void setInfoExecutionDegreesList(List list) {
		infoExecutionDegreesList = list;
	}

	/**
	 * @param student
	 */
	public void setInfoStudent(InfoStudent student) {
		infoStudent = student;
	}

	/**
	 * @return
	 */
	public InfoExecutionDegree getChosenInfoExecutionDegree() {
		return chosenInfoExecutionDegree;
	}

	/**
	 * @return
	 */
	public Integer getChosenSemester() {
		return chosenSemester;
	}

	/**
	 * @return
	 */
	public Integer getChosenYear() {
		return chosenYear;
	}

	/**
	 * @param degree
	 */
	public void setChosenInfoExecutionDegree(InfoExecutionDegree degree) {
		chosenInfoExecutionDegree = degree;
	}

	/**
	 * @param integer
	 */
	public void setChosenSemester(Integer integer) {
		chosenSemester = integer;
	}

	/**
	 * @param integer
	 */
	public void setChosenYear(Integer integer) {
		chosenYear = integer;
	}

	/**
	 * @return
	 */
	public List getEnrolmentsToRemoveList() {
		return enrolmentsToRemoveList;
	}

	/**
	 * @return
	 */
	public List getLastEnrolmentsList() {
		return lastEnrolmentsList;
	}

	/**
	 * @param list
	 */
	public void setEnrolmentsToRemoveList(List list) {
		enrolmentsToRemoveList = list;
	}

	/**
	 * @param list
	 */
	public void setLastEnrolmentsList(List list) {
		lastEnrolmentsList = list;
	}

}