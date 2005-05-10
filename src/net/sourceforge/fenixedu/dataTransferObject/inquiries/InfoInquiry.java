/*
 * Created on 29/Mar/2005
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoInquiry extends InfoObject implements Comparable {

	private InfoExecutionPeriod executionPeriod;
	private InfoExecutionCourse executionCourse;
	private InfoExecutionDegree executionDegreeCourse;
	private InfoExecutionDegree executionDegreeStudent;
	
	private InfoInquiriesCourse inquiriesCourse;
    private List<InfoInquiriesTeacher> inquiriesTeachersList = new ArrayList<InfoInquiriesTeacher>();
    private List<InfoInquiriesRoom> inquiriesRoomsList = new ArrayList<InfoInquiriesRoom>();
	

	public InfoInquiry() {
		this.inquiriesCourse = new InfoInquiriesCourse();
	}
	
	/**
	 * @return Returns the executionDegreeStudent.
	 */
	public InfoExecutionDegree getExecutionDegreeStudent() {
		return executionDegreeStudent;
	}
	


	/**
	 * @param executionDegreeStudent The executionDegreeStudent to set.
	 */
	public void setExecutionDegreeStudent(InfoExecutionDegree executionDegreeStudent) {
		this.executionDegreeStudent = executionDegreeStudent;
	}
	


	/**
	 * @return Returns the executionPeriod.
	 */
	public InfoExecutionPeriod getExecutionPeriod() {
		return executionPeriod;
	}
	


	/**
	 * @param executionPeriod The executionPeriod to set.
	 */
	public void setExecutionPeriod(InfoExecutionPeriod executionPeriod) {
		this.executionPeriod = executionPeriod;
	}
	

	/**
	 * @return Returns the inquiriesCourse.
	 */
	public InfoInquiriesCourse getInquiriesCourse() {
		return inquiriesCourse;
	}
	

	/**
	 * @param inquiriesCourse The inquiriesCourse to set.
	 */
	public void setInquiriesCourse(InfoInquiriesCourse inquiriesCourse) {
		this.inquiriesCourse = inquiriesCourse;
	}
	

	/**
	 * @param inquiriesRoomsList The inquiriesRoomsList to set.
	 */
	public void setInquiriesRoomsList(List<InfoInquiriesRoom> inquiriesRoomsList) {
		this.inquiriesRoomsList = inquiriesRoomsList;
	}
	

	/**
	 * @return Returns the inquiriesRoomsList.
	 */
	public List<InfoInquiriesRoom> getInquiriesRoomsList() {
		return inquiriesRoomsList;
	}
	
	

	/**
	 * @param inquiriesTeachersList The inquiriesTeachersList to set.
	 */
	public void setInquiriesTeachersList(
			List<InfoInquiriesTeacher> inquiriesTeachersList) {
		this.inquiriesTeachersList = inquiriesTeachersList;
	}
	

	/**
	 * @return Returns the inquiriesTeachersList.
	 */
	public List<InfoInquiriesTeacher> getInquiriesTeachersList() {
		return inquiriesTeachersList;
	}
	
	

	/**
	 * @return Returns the executionCourse.
	 */
	public InfoExecutionCourse getExecutionCourse() {
		return executionCourse;
	}
	

	/**
	 * @param executionCourse The executionCourse to set.
	 */
	public void setExecutionCourse(InfoExecutionCourse executionCourse) {
		this.executionCourse = executionCourse;
	}
	

	/**
	 * @return Returns the executionDegreeCourse.
	 */
	public InfoExecutionDegree getExecutionDegreeCourse() {
		return executionDegreeCourse;
	}
	

	/**
	 * @param executionDegreeCourse The executionDegreeCourse to set.
	 */
	public void setExecutionDegreeCourse(InfoExecutionDegree executionDegreeCourse) {
		this.executionDegreeCourse = executionDegreeCourse;
	}
	

	public int compareTo(Object arg0) {
        return 0;
    }
	
	public String toString() {
        String result = "[INFOINQUIRY";
        result += ", id=" + getIdInternal();

		if(inquiriesCourse != null)
			result += ", inquiriesCourse=" + inquiriesCourse.toString();
		if(inquiriesTeachersList != null) {
			result += "inquiriesTeachersList: {";
			Iterator iter = inquiriesTeachersList.iterator();
			while(iter.hasNext()) {
				InfoInquiriesTeacher teacher = (InfoInquiriesTeacher) iter.next();
				result += teacher.toString();
			}
			result += "} ";
		}
		
		if(inquiriesRoomsList != null) {
			result += "inquiriesRoomsList: {";
			Iterator iter = inquiriesRoomsList.iterator();
			while(iter.hasNext()) {
				InfoInquiriesRoom room = (InfoInquiriesRoom) iter.next();
				result += room.toString();
			}
			result += "} ";
		}
		
        result += "]\n";
        return result;
	}

    
}
